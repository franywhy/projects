package com.izhubo.userSystem.web;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.hqonline.model.HK;
import com.izhubo.rest.AppProperties;
import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.rest.common.util.http.HttpClientUtil4_3;
import com.izhubo.userSystem.mongo.qquser.QQUserRepositery;
import com.izhubo.userSystem.thirdPart.SmsSingleSender;
import com.izhubo.userSystem.thirdPart.SmsSingleSenderResult;
import com.izhubo.userSystem.utils.HttpUtils;
import com.izhubo.userSystem.vo.SSOResult;
import com.mongodb.BasicDBObject;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证码
* @ClassName: SecurityController 
* @Description: 验证码
* @author shihongjie
* @date 2015年8月4日 下午3:46:41 
*
 */
@Controller
public class SecurityController {
	
	private static Logger logger = LoggerFactory.getLogger(SecurityController.class);
	
//	/** 短信验证码题头 */
//	private static final String MESSAGE_KEY = "message:";
//	/** 短信验证码次数 */
//	private static final String TIMES_KEY = "times:";
//	/** 短信验证码存放时间 */
//	private static final String SECURITY_KEY = "security:";
//	/** 验证码存放时间 */
//	private static final long SECURITY_KEY_TIMEOUT = 600000 ; //10*60*1000
//	/** 验证码发送间隔时间 */
//	private static final long SECURITY_TIMEOUT = 20000 ; //3*60*1000
//	/** 验证码发送间隔时间 */
//	private static final long SECURITY_MAX_TIMES_TIMEOUT = 24 ; //小时
//	/** 一个手机号每天最大发送验证码次数 */
//	private static final int SECURITY_MAX_TIMES = 8;  //学员反馈过多，开放到8次
	
//	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

	private String promotiondomain = AppProperties.get("promotion.domain").toString();

	@Autowired
	protected QQUserRepositery qqUserRepositery;
	
	@Resource
	protected MongoTemplate mainMongo;
	@Resource
	protected StringRedisTemplate mainRedis;
	
	@Resource
	private UserService userService;
	
	@Autowired
	private Producer producer;
	
	/** 腾讯云相关配置，请先确保模板审核通过*/ 
	int appid = 1400025077;
	String appkey = "fe515ffd807c365c32bb81da48f841e3";
	/** 注意这个模板id，我们的模板为: 您的短信验证码为{1},本验证{2}分钟内有效。*/ 
	int tmplId = 11549;
	
	
	private void SendSms(String code,String phoneNum) throws Exception
	{
		SmsSingleSender singleSender = new SmsSingleSender(appid, appkey);
    	SmsSingleSenderResult singleSenderResult;
		ArrayList<String> params = new ArrayList<>();
		
    	params.add(code);
    	params.add("10");

    	
    	try {
			singleSenderResult = singleSender.sendWithParam("86", phoneNum, tmplId, params, "恒企会计", "", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
	
	
	
	@RequestMapping("/security_old")
	public Map<String , Object> securityOld(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String mobile = req.getParameter("phone");
		Map<String , Object> map = new HashMap<String , Object>();
		
//		String captcha = mainRedis.opsForValue().get(Constants.KAPTCHA_SESSION_KEY+":"+req.getSession().getId());
//		if(StringUtils.isBlank(captcha) || !captcha.equals(verifyCode)){
//			map.put("code", "30599");
//			map.put("msg", "图形验证码错误！");
//			map.put("data", "图形验证码错误！");
//			return map;
//		}
		
		if(StringUtils.isNotBlank(mobile) && isMobile(mobile)){
			//操作时间频繁校验
			if(mainRedis.opsForValue().get(HK.SECURITY.getSecurityTimeout(mobile)) != null){
				map.put("code", "0");
				map.put("msg", "操作太频繁！请稍后再试！");
				map.put("data", "操作太频繁！请稍后再试！");
			}
			else if(!addMobileTime(mobile)){
				map.put("code", "0");
				map.put("msg", "验证码接收太频繁，请稍后再试！");
				map.put("data", "验证码接收太频繁，请稍后再试！");
			}
			else{
				//验证码
				String code = HK.SECURITY.randomCode();
				//保存到redis
				mainRedis.opsForValue().set(HK.SECURITY.getSecurityKey(mobile), code, HK.SECURITY.SECURITY_KEY_TIMEOUT,TimeUnit.MILLISECONDS);
				mainRedis.opsForValue().set(HK.SECURITY.getSecurityTimeout(mobile), mobile, HK.SECURITY.SECURITY_TIMEOUT,TimeUnit.MILLISECONDS);

			
				//发送短信
				SendSms(code,mobile);
				
				logger.info("security method SMS");
			    map.put("code", "1");
				
				
			}
		}else{
			map.put("code", "30406");
			map.put("msg", "手机号码错误!");
			map.put("data", "手机号码错误!");
		}
		return map;
	}
	
	@RequestMapping("/security")
	public Map<String , Object> security(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String mobile = req.getParameter("phone");
		Map<String , Object> map = new HashMap<String , Object>();
		

		
		if(StringUtils.isNotBlank(mobile) && isMobile(mobile)){
			//操作时间频繁校验
			if(mainRedis.opsForValue().get(HK.SECURITY.getSecurityTimeout(mobile)) != null){
				map.put("code", "0");
				map.put("msg", "操作太频繁！请稍后再试！");
				map.put("data", "操作太频繁！请稍后再试！");
			}
			else if(!addMobileTime(mobile)){
				map.put("code", "0");
				map.put("msg", "验证码接收太频繁，请稍后再试！");
				map.put("data", "验证码接收太频繁，请稍后再试！");
			}
			else{
				/*//验证码
				String code = HK.SECURITY.randomCode();
				//保存到redis
				mainRedis.opsForValue().set(HK.SECURITY.getSecurityKey(mobile), code, HK.SECURITY.SECURITY_KEY_TIMEOUT,TimeUnit.MILLISECONDS);
				mainRedis.opsForValue().set(HK.SECURITY.getSecurityTimeout(mobile), mobile, HK.SECURITY.SECURITY_TIMEOUT,TimeUnit.MILLISECONDS);

			
				//发送短信
				SendSms(code,mobile);
				
				logger.info("security method SMS");*/
				
				//先判断手机号码是否存在，如果存在才下发短信
				SSOResult checkMobileResult = userService.checkMobileNo(mobile);
				if(checkMobileResult != null && StringUtils.isNotBlank(checkMobileResult.getCode())){
					String code = checkMobileResult.getCode();
					if("200".equals(code)){
						JSONObject dataObj = JSONObject.fromObject(checkMobileResult.getData());
						if(dataObj.getBoolean("isMobileNumber"))
						{
							SSOResult sendSMSResult = userService.sendSMS(mobile);
							if(sendSMSResult != null && StringUtils.isNotBlank(sendSMSResult.getCode())){
								String smscode = sendSMSResult.getCode();
								if("200".equals(smscode)){
									map.put("code", "1");						
								}else{
									map.put("code", "30496");
									map.put("msg", sendSMSResult.getMessage());
								}
							}else{
								map.put("code", "30499");
								map.put("msg", "发短信验证码出错!");
								map.put("data", "发短信验证码出错!");
							}
						}
						else
						{
							map.put("code", UsError.UsernameNotExisted.getCode());
							map.put("msg", UsError.UsernameNotExisted.getMessage());
							map.put("data",  UsError.UsernameNotExisted.getMessage());
						}
						
					}else{
						map.put("code", "30499");
						map.put("msg", checkMobileResult.getMessage());
					}
					
				
				}
			
				
			
			}
		}else{
			map.put("code", "30406");
			map.put("msg", "手机号码错误!");
			map.put("data", "手机号码错误!");
		}
		return map;
	}
	//2016-06-16 获取验证码时加入手机是否注册校验
	@RequestMapping("/security_v200")
	public Map<String , Object> security_v200(HttpServletRequest req, HttpServletResponse res,String verifyCode) throws Exception{
		String mobile = req.getParameter("phone");
		Map<String , Object> map = new HashMap<String , Object>();
		
		String captcha = mainRedis.opsForValue().get(Constants.KAPTCHA_SESSION_KEY+":"+req.getSession().getId());
		if(StringUtils.isBlank(captcha) || !captcha.equals(verifyCode)){
			map.put("code", "30599");
			map.put("msg", "图形验证码错误！");
			map.put("data", "图形验证码错误！");
			return map;
		}
		
		if(StringUtils.isNotBlank(mobile) && isMobile(mobile)){
			//操作时间频繁校验
			if(mainRedis.opsForValue().get(HK.SECURITY.getSecurityTimeout(mobile)) != null){
				map.put("code", "0");
				map.put("msg", "操作太频繁！请稍后再试！");
				map.put("data", "操作太频繁！请稍后再试！");
			}
			else if(!addMobileTime(mobile)){
				map.put("code", "0");
				map.put("msg", "验证码接收太频繁，请稍后再试！");
				map.put("data", "验证码接收太频繁，请稍后再试！");
			}
			else if(this.qqUserRepositery.findByUsername(mobile).size() > 0) {
				map.put("code", UsError.UsernameHasExisted.getCode());
				map.put("msg", UsError.UsernameHasExisted.getMessage());
				map.put("data",  UsError.UsernameHasExisted.getMessage());
			}
			else{
				//验证码
				String code = HK.SECURITY.randomCode();
				//保存到redis
				mainRedis.opsForValue().set(HK.SECURITY.getSecurityKey(mobile), code, HK.SECURITY.SECURITY_KEY_TIMEOUT,TimeUnit.MILLISECONDS);
				mainRedis.opsForValue().set(HK.SECURITY.getSecurityTimeout(mobile), mobile, HK.SECURITY.SECURITY_TIMEOUT,TimeUnit.MILLISECONDS);
     			SendSms(code,mobile);
				logger.info("security method SMS");
				map.put("code", "1");
			}
		}else{
			map.put("code", "30406");
			map.put("msg", "手机号码错误!");
			map.put("data", "手机号码错误!");
		}
		return map;
	}
	
	
         //新增图形验证码接口
		@RequestMapping("/security_v310")
		public Map<String , Object> security_v310(HttpServletRequest req, HttpServletResponse res,String verifyCode) throws Exception{
			String mobile = req.getParameter("phone");
			Map<String , Object> map = new HashMap<String , Object>();
			
			String captcha = mainRedis.opsForValue().get(Constants.KAPTCHA_SESSION_KEY+":"+req.getSession().getId());
			if(StringUtils.isBlank(captcha) || !captcha.equals(verifyCode)){
				map.put("code", "30599");
				map.put("msg", "图形验证码错误！");
				map.put("data", "图形验证码错误！");
				return map;
			}
			
			if(StringUtils.isNotBlank(mobile) && isMobile(mobile)){
				//操作时间频繁校验
				if(mainRedis.opsForValue().get(HK.SECURITY.getSecurityTimeout(mobile)) != null){
					map.put("code", "0");
					map.put("msg", "操作太频繁！请稍后再试！");
					map.put("data", "操作太频繁！请稍后再试！");
				}
				else if(!addMobileTime(mobile)){
					map.put("code", "0");
					map.put("msg", "验证码接收太频繁，请稍后再试！");
					map.put("data", "验证码接收太频繁，请稍后再试！");
				}
				else{
					SSOResult sendSMSResult = userService.sendSMS(mobile);
					if(sendSMSResult != null && StringUtils.isNotBlank(sendSMSResult.getCode())){
						String smscode = sendSMSResult.getCode();
						if("200".equals(smscode)){
							map.put("code", "1");						
						}else{
							map.put("code", "30496");
							map.put("msg", sendSMSResult.getMessage());
						}
					}else{
						map.put("code", "30499");
						map.put("msg", "发短信验证码出错!");
						map.put("data", "发短信验证码出错!");
					}
				}
			}else{
				map.put("code", "30406");
				map.put("msg", "手机号码错误!");
				map.put("data", "手机号码错误!");
			}
			return map;
		}
	
		 //新增图形验证码接口
		@RequestMapping("/security_psw_v310")
		public Map<String , Object> security_psw_v310(HttpServletRequest req, HttpServletResponse res,String verifyCode) throws Exception{
			String mobile = req.getParameter("phone");
			Map<String , Object> map = new HashMap<String , Object>();
			
			String captcha = mainRedis.opsForValue().get(Constants.KAPTCHA_SESSION_KEY+":"+req.getSession().getId());
			if(StringUtils.isBlank(captcha) || !captcha.equals(verifyCode)){
				map.put("code", "30599");
				map.put("msg", "图形验证码错误！");
				map.put("data", "图形验证码错误！");
				return map;
			}
			
			if(StringUtils.isNotBlank(mobile) && isMobile(mobile)){
				//操作时间频繁校验
				if(mainRedis.opsForValue().get(HK.SECURITY.getSecurityTimeout(mobile)) != null){
					map.put("code", "0");
					map.put("msg", "操作太频繁！请稍后再试！");
					map.put("data", "操作太频繁！请稍后再试！");
				}
				else if(!addMobileTime(mobile)){
					map.put("code", "0");
					map.put("msg", "验证码接收太频繁，请稍后再试！");
					map.put("data", "验证码接收太频繁，请稍后再试！");
				}
				else{
					//先判断手机号码是否存在，如果存在才下发短信
					SSOResult checkMobileResult = userService.checkMobileNo(mobile);
					if(checkMobileResult != null && StringUtils.isNotBlank(checkMobileResult.getCode())){
						String code = checkMobileResult.getCode();
						if("200".equals(code)){
							JSONObject dataObj = JSONObject.fromObject(checkMobileResult.getData());
							if(dataObj.getBoolean("isMobileNumber"))
							{
								SSOResult sendSMSResult = userService.sendSMS(mobile);
								if(sendSMSResult != null && StringUtils.isNotBlank(sendSMSResult.getCode())){
									String smscode = sendSMSResult.getCode();
									if("200".equals(smscode)){
										map.put("code", "1");						
									}else{
										map.put("code", "30496");
										map.put("msg", sendSMSResult.getMessage());
									}
								}else{
									map.put("code", "30499");
									map.put("msg", "发短信验证码出错!");
									map.put("data", "发短信验证码出错!");
								}
							}
							else
							{
								map.put("code", UsError.UsernameNotExisted.getCode());
								map.put("msg", UsError.UsernameNotExisted.getMessage());
								map.put("data",  UsError.UsernameNotExisted.getMessage());
							}
							
						}else{
							map.put("code", "30499");
							map.put("msg", checkMobileResult.getMessage());
						}
					}
				}
			}else{
				map.put("code", "30406");
				map.put("msg", "手机号码错误!");
				map.put("data", "手机号码错误!");
			}
			return map;
		}

	//小程序图形验证码接口
	@RequestMapping("/applet_security_v310")
	public Map<String , Object> applet_security_v310(HttpServletRequest req, HttpServletResponse res,String verifyCode) throws Exception{
		String mobile = req.getParameter("phone");
		String key = req.getParameter("key");
		Map<String , Object> map = new HashMap<String , Object>();

		String captcha = mainRedis.opsForValue().get(Constants.KAPTCHA_SESSION_KEY+":"+key);
		if(StringUtils.isBlank(captcha) || !captcha.equals(verifyCode)){
			map.put("code", "30599");
			map.put("msg", "图形验证码错误！");
			map.put("data", "图形验证码错误！");
			return map;
		}

		if(StringUtils.isNotBlank(mobile) && isMobile(mobile)){
			//操作时间频繁校验
			if(mainRedis.opsForValue().get(HK.SECURITY.getSecurityTimeout(mobile)) != null){
				map.put("code", "0");
				map.put("msg", "操作太频繁！请稍后再试！");
				map.put("data", "操作太频繁！请稍后再试！");
			}
			else if(!addMobileTime(mobile)){
				map.put("code", "0");
				map.put("msg", "验证码接收太频繁，请稍后再试！");
				map.put("data", "验证码接收太频繁，请稍后再试！");
			}
			else{
				SSOResult sendSMSResult = userService.sendSMS(mobile);
				if(sendSMSResult != null && StringUtils.isNotBlank(sendSMSResult.getCode())){
					String smscode = sendSMSResult.getCode();
					if("200".equals(smscode)){
						map.put("code", "1");
					}else{
						map.put("code", "30496");
						map.put("msg", sendSMSResult.getMessage());
					}
				}else{
					map.put("code", "30499");
					map.put("msg", "发短信验证码出错!");
					map.put("data", "发短信验证码出错!");
				}
			}
		}else{
			map.put("code", "30406");
			map.put("msg", "手机号码错误!");
			map.put("data", "手机号码错误!");
		}
		return map;
	}

	/**
	 * 找回密码的验证码
	 * @throws Exception 
	 * @Description: 找回密码的验证码
	 * @date 2015年9月1日 上午11:36:24 
	 */
	@RequestMapping("/security_psw")
	public Map<String , Object> security_psw(HttpServletRequest req, HttpServletResponse res, String verifyCode) throws Exception{
		return security(req , res);
	}
	
	
	/**
	 * 
	 * @throws Exception 
	 * @Description: 教师端申请验证码
	 * @date 2015年9月1日 上午11:36:24 
	 */
	@RequestMapping("/security_teacher_apply")
	public Map<String , Object> security_teacher_apply(HttpServletRequest req, HttpServletResponse res, String verifyCode) throws Exception{
		return security(req , res);
	}
	
	/**
	 * 生成图形验证码
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/verifyCode")
	@ResponseBody
	public void verifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
		
		//生成文字验证码
        String captcha = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(captcha);
        //保存到redis
        //request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, captcha);
        mainRedis.opsForValue().set(Constants.KAPTCHA_SESSION_KEY+":"+request.getSession().getId(), captcha, HK.SECURITY.SECURITY_KEY_TIMEOUT,TimeUnit.MILLISECONDS);
        ImageIO.write(image, "JPG", response.getOutputStream());
	}

	/**
	 * 小程序生成图形验证码
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/appletVerifyCode")
	@ResponseBody
	public void appletVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException{

		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");
		String key = request.getParameter("key");

		//生成文字验证码
		String captcha = producer.createText();
		//生成图片验证码
		BufferedImage image = producer.createImage(captcha);
		//保存到redis
		//request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, captcha);
		mainRedis.opsForValue().set(Constants.KAPTCHA_SESSION_KEY+":"+key, captcha, HK.SECURITY.SECURITY_KEY_TIMEOUT,TimeUnit.MILLISECONDS);
		ImageIO.write(image, "JPG", response.getOutputStream());
	}

	/**
	 * 小程序绑定手机
	 * @param phone
	 * @param security
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/appletBindPhone")
	public Map<String , Object> appletBindPhone(String phone, String security) {
		Map<String , Object> map = new HashMap<String , Object>();
		SSOResult sSOResult = null;
		Map<String,String> param = new HashMap<>();
		//拼装SSO接口的请求url
		String SSO_SENDSMS_GET = AppProperties.get("sso_sendSMS_get");
		param.put("mobileNo", phone);
		param.put("otp", security);
		String result = null;
		try {
			result = HttpClientUtil4_3.post(SSO_SENDSMS_GET, param, null);
			if(StringUtils.isNotBlank(result)){
				sSOResult = JSONUtil.jsonToBean(result, SSOResult.class);
				if(null != sSOResult && StringUtils.isNotBlank(sSOResult.getCode())){
					String code = sSOResult.getCode();
					String message = sSOResult.getMessage();
					if("200".equals(code)){
						//判断是否绑定过
						long count = mainMongo.getCollection("applet_phone").count(new BasicDBObject("phone",phone));
						if(count==0) {
							BasicDBObject applet = new BasicDBObject();
							applet.append("_id", UUID.randomUUID().toString());
							// 绑定手机号
							applet.append("phone",phone);
							// 绑定时间
							applet.append("bind_time",System.currentTimeMillis());
							mainMongo.getCollection("applet_phone").save(applet);
							Map<String,String> paramMap = new HashMap<>();
							paramMap.put("tel",phone);
							paramMap.put("code","nocode");
							paramMap.put("property","1");
							HttpClientUtil4_3.post(promotiondomain+"/promotion/guestbook/checkCodedata",paramMap,null);
						}
						map.put("code",code);
						map.put("message","绑定成功");
					} else {
						map.put("code",code);
						map.put("message",message);
					}
				} else {
					map.put("code","99");
					map.put("message","绑定失败");
				}
			} else {
				map.put("code","99");
				map.put("message","绑定失败");
			}
		} catch (IOException e) {
			map.put("code","99");
			map.put("message","绑定失败");
			logger.error("SSO[/inner/otpSMS]接口返回结果转换出错！接口返回结果result="+result);
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 自考小程序获取openid
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/appletLoginStatus")
	public Map<String , Object> appletLoginStatus(String code) {
		String url = "https://api.weixin.qq.com/sns/jscode2session";
		Map<String, String> param = new HashMap<>();
		param.put("appid","wxc3cce2482ac510f0");
		param.put("secret","db2aaef23afedda25a371c040108a354");
		param.put("js_code",code);
		param.put("grant_type","authorization_code");
		String result = HttpUtils.sendGet(url,param);
		Map<String , Object> resultMap = new HashMap<>();
		resultMap.put("code",401);
		resultMap.put("msg","登录失效");
		if(StringUtils.isNotBlank(result)) {
			Map<String, Object> map = com.izhubo.userSystem.utils.JSONUtil.jsonToMap(result);
			if(null != map) {
				String session_key = map.get("session_key") == null ? null : (String) map.get("session_key");
				String openid = map.get("openid") == null ? null : (String) map.get("openid");
				if(StringUtils.isNotBlank(session_key) && StringUtils.isNotBlank(openid)) {
					Long timeStamp = System.currentTimeMillis();
					StringBuilder sb = new StringBuilder();
					sb.append(getRandomString(session_key,5));
					sb.append(Long.toHexString(timeStamp ^ Long.MIN_VALUE));
					sb.append(getRandomString(openid,5));

					String rdSessionId_key = "rdSessionId:"+sb.toString();
					mainRedis.opsForHash().putAll(rdSessionId_key, map);
					mainRedis.expire(rdSessionId_key, 10 * 24 * 3600L, TimeUnit.SECONDS);

					resultMap.put("code",200);
					resultMap.put("msg","登录成功");
					resultMap.put("data",rdSessionId_key);
				}
			}
		}
		return resultMap;
	}

	private static String decrypt(byte[] key, byte[] iv, byte[] encData) throws Exception {
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec); //解析解密后的字符串
		return new String(cipher.doFinal(encData),"UTF-8");
	}

	/**
	 * 自考小程序获取手机号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/appletPhone")
	public Map<String, Object> appletPhone(@RequestBody String body) throws Exception {
		Map<String , Object> map = com.alibaba.fastjson.JSONObject.parseObject(body, Map.class);
		Map<String , Object> resultMap = new HashMap<>();
		String session_key = (String) mainRedis.opsForHash().get((String) map.get("rdSessionId"),"session_key");
		if(StringUtils.isNotBlank(session_key)) {
			byte[] encryptedByte = Base64.decodeBase64((String) map.get("encryptedData"));
			byte[] ivByte = Base64.decodeBase64((String) map.get("iv"));
			byte[] sessionKeyByte = Base64.decodeBase64(session_key);

			String userinfo = decrypt(sessionKeyByte, ivByte, encryptedByte);
			resultMap.put("code",200);
			resultMap.put("msg","成功获取用户信息");
			resultMap.put("data",userinfo);
			if(StringUtils.isNotBlank(userinfo)) {
				String phoneNumber = com.alibaba.fastjson.JSONObject.parseObject(userinfo).getString("phoneNumber");
				if(StringUtils.isNotBlank(phoneNumber)) {
					//判断是否绑定过
					long count = mainMongo.getCollection("applet_phone").count(new BasicDBObject("phone",phoneNumber));
					if(count==0) {
						BasicDBObject applet = new BasicDBObject();
						applet.append("_id", UUID.randomUUID().toString());
						// 绑定手机号
						applet.append("phone",phoneNumber);
						// 绑定时间
						applet.append("bind_time",System.currentTimeMillis());
						mainMongo.getCollection("applet_phone").save(applet);
						Map<String,String> paramMap = new HashMap<>();
						paramMap.put("tel",phoneNumber);
						paramMap.put("code","nocode");
						paramMap.put("property","1");
						HttpClientUtil4_3.post(promotiondomain+"/promotion/guestbook/checkCodedata",paramMap,null);
					}
				}
			}
		} else {
			resultMap.put("code",401);
			resultMap.put("msg","登录失效");
		}
		return resultMap;
	}

	/**
	 * 生成随机字符串
	 * @param string
	 * @param length
	 * @return
	 */
	private static String getRandomString(String string,int length){
		StringBuffer sb = new StringBuffer();
		int len = string.length();
		for (int i = 0; i < length; i++) {
			sb.append(string.charAt((int) Math.round(Math.random() * (len-1))));
		}
		return sb.toString();
	}

	/**
	 * 每个手机号每天发送次数限定
	 * @Description: 每个手机号每天发送次数限定
	 * @date 2015年11月4日 下午2:28:24 
	 * @param @param mobile
	 */
	private boolean addMobileTime(String mobile){
		boolean res = false;
		
		String acKey = HK.SECURITY.getMessageTimeKey(mobile);
		ValueOperations<String, String> ops = mainRedis.opsForValue();
		String timesString = ops.get(acKey);
		if(StringUtils.isBlank(timesString)){
			timesString = "0";
		}
		Integer times = Integer.valueOf(timesString);
		if(times < HK.SECURITY.SECURITY_MAX_TIMES){
			times++;
			ops.set(acKey, times.toString(), HK.SECURITY.SECURITY_MAX_TIMES_TIMEOUT,TimeUnit.HOURS);
			res = true;
		}
		return res;
	}
	
	
//	public static String getSecurityKey(String key){
//		return MESSAGE_KEY + SECURITY_KEY + key;
//	}
//	public static String getSecurityTimeout(String key){
//		return MESSAGE_KEY + SECURITY_KEY + key + ":time";
//	}
	/**
	 * 手机发送验证码次数KEY
	 * @Description: 手机发送验证码次数KEY
	 * @date 2015年11月4日 下午5:18:55 
	 * @param @param mobile
	 */
//	public static String getMessageTimeKey(String mobile){
//		return MESSAGE_KEY + TIMES_KEY + SDF.format(new Date()) + ":" + mobile;
//	}
	
//	private String randomCode(){
//		return new java.text.DecimalFormat("0").format(Math.random()*9000+1000) ;
//	}
	
	/** 
     * 手机号验证 
     *  
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isMobile(String str) {   
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^1\\d{10}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    } 
	
}
