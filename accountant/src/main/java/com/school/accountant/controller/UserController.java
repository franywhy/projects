package com.school.accountant.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.school.accountant.common.SystemConfig;
import com.school.accountant.service.UserService;
import com.school.accountant.util.EncryptionUtils;
import com.school.accountant.util.HttpUtil;
import com.school.accountant.util.JsonUtil;
import com.school.accountant.vo.SSOResult;
import com.school.accountant.vo.UserInfo;

@Controller
@RequestMapping("/user")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Producer producer;
	
	@RequestMapping("/register")
	@ResponseBody
	public Object register(String mobileNo, String password, String SMSCode, String callback, HttpServletRequest request, HttpServletResponse response){
		
		JSONObject returnObj = new JSONObject();
		SSOResult result = userService.register(mobileNo,EncryptionUtils.base64Encode(password), SMSCode);
		if(result != null && StringUtils.isNotBlank(result.getCode())){
			returnObj.put("code", result.getCode());
			returnObj.put("msg", result.getMessage());
		}else{
			returnObj.put("code", "500");
			returnObj.put("msg", "注册出错，请稍后再试");
		}
		if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue jacksonValue = new MappingJacksonValue(returnObj.toJSONString());
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}else{
			return returnObj.toJSONString();					
		}
	}
	
	@RequestMapping("/login")
	@ResponseBody
	public Object login(String loginName, String password, Integer timeout, String callback, HttpServletRequest request, HttpServletResponse response){
		
		JSONObject returnObj = new JSONObject();
		
		//读取返回结果获得token
		String token = "";
		SSOResult result = userService.login(loginName.trim(), password.trim(), timeout);
		//{"code":200,"message":"操作成功","data":{"token":"8098beb68000015da75359fc80000001"}}
		if(result != null && StringUtils.isNotBlank(result.getCode()) && SystemConfig.SSO_SUCCESS_CODE.equals(result.getCode())){
			returnObj.put("code", result.getCode());
			returnObj.put("msg", result.getMessage());
			JSONObject dataObj = null;
			try {
				dataObj = (JSONObject) JSONObject.toJSON(result.getData());
			} catch (Exception e) {
				logger.error("SSO登录接口返回结果data转换出错！接口返回结果result.data="+result.getData());
				logger.error(e.getMessage());
				returnObj.put("code", "500");
				returnObj.put("msg", "登录出错，请稍后再试");
				if(StringUtils.isNotBlank(callback)){
					MappingJacksonValue jacksonValue = new MappingJacksonValue(returnObj.toJSONString());
					jacksonValue.setJsonpFunction(callback);
					return jacksonValue;
				}else{
					return returnObj.toJSONString();					
				}
				
			}
			token = dataObj.getString("token");
		}else if (result != null && StringUtils.isNotBlank(result.getCode()) && !SystemConfig.SSO_SUCCESS_CODE.equals(result.getCode())) {
			returnObj.put("code", result.getCode());
			returnObj.put("msg", result.getMessage());
			if(StringUtils.isNotBlank(callback)){
				MappingJacksonValue jacksonValue = new MappingJacksonValue(returnObj.toJSONString());
				jacksonValue.setJsonpFunction(callback);
				return jacksonValue;
			}else{
				return returnObj.toJSONString();					
			}
		} else{
			returnObj.put("code", "500");
			returnObj.put("msg", "登录出错，请稍后再试");
			if(StringUtils.isNotBlank(callback)){
				MappingJacksonValue jacksonValue = new MappingJacksonValue(returnObj.toJSONString());
				jacksonValue.setJsonpFunction(callback);
				return jacksonValue;
			}else{
				return returnObj.toJSONString();					
			}
		}
		//把token存入session
		request.getSession().setAttribute(SystemConfig.SESSION_TOKEN_KEY, token);
		
		String nickName = "";
		String avatar = "";
		SSOResult sSOResult = userService.userInfo(token);
		if(sSOResult != null && StringUtils.isNotBlank(sSOResult.getCode()) && SystemConfig.SSO_SUCCESS_CODE.equals(sSOResult.getCode())){
			JSONObject dataObj = (JSONObject) JSONObject.toJSON(sSOResult.getData());
			nickName = StringUtils.isBlank(dataObj.getString("nickName"))?"":dataObj.getString("nickName");
			avatar = dataObj.getString("avatar");
		}
		
		Cookie cookieNickname = null;
		try {
			cookieNickname = new Cookie(SystemConfig.COOKIE_NICKNAME, URLEncoder.encode(nickName,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		cookieNickname.setPath(SystemConfig.COOKIE_PATH);
		cookieNickname.setDomain(SystemConfig.COOKIE_DOMAIN);
		cookieNickname.setMaxAge(864000);//SSOTOKEN统一设置为10天
	    response.addCookie(cookieNickname);
	    
	    Cookie cookieAvatar = new Cookie(SystemConfig.COOKIE_AVATAR, avatar);
	    cookieAvatar.setPath(SystemConfig.COOKIE_PATH);
	    cookieAvatar.setDomain(SystemConfig.COOKIE_DOMAIN);
	    cookieAvatar.setMaxAge(864000);//SSOTOKEN统一设置为10天
	    response.addCookie(cookieAvatar);
	    
		//把token写入浏览器cookie,把与实训单点登录对接的cookie[SystemConfig.SHIXUN_SessionID]写入cookie
		Cookie cookie = new Cookie(SystemConfig.COOKIE_TOKEN_NAME, token);
	    cookie.setPath(SystemConfig.COOKIE_PATH);
	    cookie.setDomain(SystemConfig.COOKIE_DOMAIN);
	    cookie.setMaxAge(864000);//SSOTOKEN统一设置为10天
	    response.addCookie(cookie);
	    
	    //与学习中心对接的SSOTOKEN
	    Cookie ssoToken = new Cookie(SystemConfig.COOKIE_SSOTOKEN_NAME, token);
	    ssoToken.setPath(SystemConfig.COOKIE_PATH);
	    ssoToken.setDomain(SystemConfig.COOKIE_DOMAIN);
	    ssoToken.setMaxAge(864000);//SSOTOKEN统一设置为10天
	    response.addCookie(ssoToken);
	    
	    if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue jacksonValue = new MappingJacksonValue(returnObj.toJSONString());
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}else{
			return returnObj.toJSONString();
		}
	}
	
	/**
	 * 获取页面头部展示的用户昵称，头像，电话号码等信息
	 * @param token:	cookie中保存的token
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/userInfo")
	@ResponseBody
	public Object userInfo(String token, String callback, HttpServletRequest request, HttpServletResponse response){
		
		HttpSession session = request.getSession();
		//从session取token，与传过来的cookie中的token对比是否一致
		//String tokenInSession = session.getAttribute(SystemConfig.SESSION_TOKEN_NAME) == null?"":(String) session.getAttribute(SystemConfig.SESSION_TOKEN_NAME);
		String tokenInSession = (String) session.getAttribute(SystemConfig.SESSION_TOKEN_KEY);
		if(StringUtils.isNotBlank(token) && StringUtils.isNotBlank(tokenInSession) && token.equals(tokenInSession)){
			//从session中去用户信息，如果取到，直接返回；如果没有取到，继续下一步
			UserInfo userInfo = (UserInfo) session.getAttribute(SystemConfig.SESSION_USER_KEY);
			if(userInfo != null && StringUtils.isNotBlank(userInfo.getMobileNo())){
				//return JsonUtil.objectToJson(userInfo);
				if(StringUtils.isNotBlank(callback)){
					MappingJacksonValue jacksonValue = new MappingJacksonValue(JsonUtil.objectToJson(userInfo));
					jacksonValue.setJsonpFunction(callback);
					return jacksonValue;
				}else{
					return JsonUtil.objectToJson(userInfo);					
				}
			}else{
				//根据token调用SSO用户信息接口inner/userInfo
				SSOResult sSOResult = userService.userInfo(token);
				/*"data": {
				    "avatar": "http://zikao.hqjy.com/Avatar.png",
				    "nickName": "赵琨",
				    "email": null,
				    "gender": 1
				  }*/
				if(sSOResult != null && StringUtils.isNotBlank(sSOResult.getCode()) && SystemConfig.SSO_SUCCESS_CODE.equals(sSOResult.getCode())){
					JSONObject dataObj = (JSONObject) JSONObject.toJSON(sSOResult.getData());
					String nickName = StringUtils.isBlank(dataObj.getString("nickName"))?"":dataObj.getString("nickName");
					userInfo = new UserInfo();
					userInfo.setId("");
					//if(StringUtils.isBlank(nickName)){
						//调用inner/userMobileNo接口获取电话号码
						SSOResult sSOMobileResult = userService.userMobileNo(token);
						if(sSOMobileResult != null && StringUtils.isNotBlank(sSOMobileResult.getCode()) && SystemConfig.SSO_SUCCESS_CODE.equals(sSOMobileResult.getCode())){
							JSONObject mobileDataObj = (JSONObject) JSONObject.toJSON(sSOMobileResult.getData());
							//"mobileNo": "13824429749"
							String mobileNO = mobileDataObj.getString("mobileNo");
							//mobileNO = mobileNO.substring(0, 3) + "****" + mobileNO.substring(7);
							userInfo.setMobileNo(mobileNO);
						}
					//}
					userInfo.setNickname(nickName);
					userInfo.setPhoto(StringUtils.isBlank(dataObj.getString("avatar"))?"":dataObj.getString("avatar"));
					//把用户信息放到session中
					session.setAttribute(SystemConfig.SESSION_USER_KEY, userInfo);
					//return JsonUtil.objectToJson(userInfo);
					if(StringUtils.isNotBlank(callback)){
						MappingJacksonValue jacksonValue = new MappingJacksonValue(JsonUtil.objectToJson(userInfo));
						jacksonValue.setJsonpFunction(callback);
						return jacksonValue;
					}else{
						return JsonUtil.objectToJson(userInfo);					
					}
				}
			}
		}
		return "";
	}
	
	/**
	 * 注销
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response){
		
		HttpSession session = request.getSession();
		//从session中取token
		String tokenInSession = (String) session.getAttribute(SystemConfig.SESSION_TOKEN_KEY);
		if(StringUtils.isNotBlank(tokenInSession)){
			//调用SSO的登出接口
			SSOResult sSOResult = userService.logout(tokenInSession);
			if(sSOResult != null && StringUtils.isNotBlank(sSOResult.getCode()) && SystemConfig.SSO_SUCCESS_CODE.equals(sSOResult.getCode())){
				//清除session中的token和userinfo
				session.removeAttribute(SystemConfig.SESSION_TOKEN_KEY);
				session.removeAttribute(SystemConfig.SESSION_USER_KEY);
				//删除浏览器cookie中的token
				Cookie cookie = new Cookie(SystemConfig.COOKIE_TOKEN_NAME, null);
			    cookie.setPath(SystemConfig.COOKIE_PATH);
			    cookie.setDomain(SystemConfig.COOKIE_DOMAIN);
			    cookie.setMaxAge(0);
			    response.addCookie(cookie);
			    
			    //与学习中心对接的SSOTOKEN
			    Cookie ssoToken = new Cookie(SystemConfig.COOKIE_SSOTOKEN_NAME, null);
			    ssoToken.setPath(SystemConfig.COOKIE_PATH);
			    ssoToken.setDomain(SystemConfig.COOKIE_DOMAIN);
			    ssoToken.setMaxAge(0);
			    response.addCookie(ssoToken);
			    
			    Cookie cookieNickname = new Cookie(SystemConfig.COOKIE_NICKNAME, null);
				cookieNickname.setPath(SystemConfig.COOKIE_PATH);
				cookieNickname.setDomain(SystemConfig.COOKIE_DOMAIN);
				cookieNickname.setMaxAge(0);
			    response.addCookie(cookieNickname);
			    
			    Cookie cookieAvatar = new Cookie(SystemConfig.COOKIE_AVATAR, null);
			    cookieAvatar.setPath(SystemConfig.COOKIE_PATH);
			    cookieAvatar.setDomain(SystemConfig.COOKIE_DOMAIN);
			    cookieAvatar.setMaxAge(0);
			    response.addCookie(cookieAvatar);
			    
			    //kjcity_SessionID,与旧版会计官网单点登陆用到
			    Cookie cookieKjcity = new Cookie("kjcity_SessionID", null);
			    cookieKjcity.setPath(SystemConfig.COOKIE_PATH);
			    cookieKjcity.setDomain(SystemConfig.COOKIE_DOMAIN);
			    cookieKjcity.setMaxAge(0);
			    response.addCookie(cookieKjcity);
			}
		}
		ModelAndView modelAndView = new ModelAndView(new RedirectView("/index", false));
		return modelAndView;
	}
	
	/**
	 * 重置密码
	 * @param mobileNo
	 * @param password
	 * @param SMSCode
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/resetPassWord")
	@ResponseBody
	public Object resetPassWord(String mobileNo, String password, String SMSCode, String callback, HttpServletRequest request, HttpServletResponse response){
		JSONObject returnObj = new JSONObject();
		SSOResult result = userService.resetPassWord(mobileNo,EncryptionUtils.base64Encode(password), SMSCode);
		if(result != null && StringUtils.isNotBlank(result.getCode())){
			returnObj.put("code", result.getCode());
			returnObj.put("msg", result.getMessage());
		}else{
			returnObj.put("code", "500");
			returnObj.put("msg", "重置密码出错，请稍后再试");
		}
		if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue jacksonValue = new MappingJacksonValue(returnObj.toJSONString());
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}else{
			return returnObj.toJSONString();
		}
	}
	
	/**
	 * 注册发送手机验证码
	 * @param mobileNo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/registerSendSMS")
	@ResponseBody
	public Object registerSendSMS(String mobileNo, String verifyCode, String callback, HttpServletRequest request, HttpServletResponse response){
		JSONObject returnObj = new JSONObject();
		String captcha = (String)request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
		if(StringUtils.isNotBlank(captcha)){
			if(captcha.equals(verifyCode)){
				userService.sendSMS(mobileNo);
				request.getSession().removeAttribute(Constants.KAPTCHA_SESSION_KEY);
				returnObj.put("code", "200");
				returnObj.put("msg", "发送短信成功");
			}else{
				returnObj.put("code", "500");
				returnObj.put("msg", "图形验证码不正确，请重新输入");
			}
		}else{
			returnObj.put("code", "500");
			returnObj.put("msg", "图形验证码已过期");
		}
		if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue jacksonValue = new MappingJacksonValue(returnObj.toJSONString());
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}else{
			return returnObj.toJSONString();
		}
	}
	
	/**
	 * 获取图形验证码
	 * @param request
	 * @param response
	 * @return
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
        //保存到session
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, captcha);
        ImageIO.write(image, "JPG", response.getOutputStream());
	}
	
	/**
	 * 重置密码发送手机验证码
	 * @param mobileNo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/resetPWSendSMS")
	@ResponseBody
	public Object resetPWSendSMS(String mobileNo, String verifyCode, String callback, HttpServletRequest request, HttpServletResponse response) throws IOException{
		JSONObject returnObj = new JSONObject();
		//验证手机号是否已经注册
		SSOResult result = userService.checkMobileNo(mobileNo);
		if(result != null && StringUtils.isNotBlank(result.getCode())){
			returnObj.put("code", result.getCode());
			returnObj.put("msg", result.getMessage());
			JSONObject dataObj = null;
			try {
				dataObj = (JSONObject) JSONObject.toJSON(result.getData());
				//returnObj.put("isMobileNumber", dataObj.getBoolean("isMobileNumber"));
				if(dataObj.getBoolean("isMobileNumber")){
					//如果手机号已经注册,验证图形验证码
					String captcha = (String)request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
					if(StringUtils.isNotBlank(captcha)){
						if(captcha.equals(verifyCode)){
							userService.sendSMS(mobileNo);
							request.getSession().removeAttribute(Constants.KAPTCHA_SESSION_KEY);
							returnObj.put("code", "200");
							returnObj.put("msg", "发送短信成功");
						}else{
							returnObj.put("code", "500");
							returnObj.put("msg", "图形验证码不正确，请重新输入");
						}
					}else{
						returnObj.put("code", "500");
						returnObj.put("msg", "图形验证码已过期");
					}
				}else{
					returnObj.put("code", "500");
					returnObj.put("msg", "此手机号未注册过，请返回注册");
				}
			} catch (Exception e) {
				logger.error("SSO登录接口返回结果data转换出错！接口返回结果result.data="+result.getData());
				logger.error(e.getMessage());
				returnObj.put("code", "500");
				returnObj.put("msg", "账号验证出错,请稍后再试");
			}
		}else {
			returnObj.put("code", "500");
			returnObj.put("msg", "账号验证出错,请稍后再试");
		}
		if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue jacksonValue = new MappingJacksonValue(returnObj.toJSONString());
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}else{
			return returnObj.toJSONString();
		}
	}
	
}
