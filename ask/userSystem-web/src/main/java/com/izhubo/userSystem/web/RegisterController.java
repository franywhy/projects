package com.izhubo.userSystem.web;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hqonline.model.HK;
import com.izhubo.rest.common.util.MsgDigestUtil;
import com.izhubo.rest.persistent.KGS;
import com.izhubo.rest.web.StaticSpring;
import com.izhubo.userSystem.mongo.qquser.QQUser;
import com.izhubo.userSystem.mongo.qquser.QQUserRepositery;
import com.izhubo.userSystem.utils.NCUserState;
import com.izhubo.userSystem.vo.SSOResult;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Controller
public class RegisterController {

	private static Logger logger = LoggerFactory
			.getLogger(RegisterController.class);

	@Resource
	protected StringRedisTemplate mainRedis;

	@Resource
	protected MongoTemplate mainMongo;
	@Resource
	protected MongoTemplate qquserMongo;

	@Autowired
	protected QQUserRepositery qqUserRepositery;

	@Resource
	protected UserApiController userApiController;

//	@Resource
//	private MessageProductor messageProductorService;
	
	private final String public_key = "fYhVueg6JHDie53UWHTb";
	
	private final String inner_key = "0y5aZcfHGJX7QmQF5azB";
	
	@Resource
	private UserService userService;
	
	@Resource
	KGS userKGS;

	/** 注册 */
	@RequestMapping("/register")
	public Map<String, Object> register(HttpServletRequest req, HttpServletResponse res) {
		Map<String, Object> map = new HashMap<String, Object>();

		String username = req.getParameter("user_name");
		String nickname = req.getParameter("nick_name");
		String password = req.getParameter("password");
		// 验证码
		String security = req.getParameter("security");
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(security)){
			map.put("code", "0");
			map.put("msg", "验证码错误");
			return map;
		}
		/*boolean isSecurity = isRegisterController(username, security);
		if(security.equals(public_key))
		{
			isSecurity = true;
		}
	   if (!isSecurity) {
			map.put("code", "0");
			map.put("msg", "验证码错误");
		} else {*/
		String qd = req.getParameter("qd");
		qd = StringUtils.isBlank(qd) ? "kuaida" : qd;

		if (this.qqUserRepositery.findByUsername(username).size() > 0) {
			map.put("code", UsError.UsernameHasExisted.getCode());
			map.put("msg", UsError.UsernameHasExisted.getMessage());
		} else {
			String temppsw = password;
			DBObject user = null;
			String tuid = com.izhubo.rest.common.util.RandomUtil.getTuid();
			String ssoUserId = null;
			String pic = null;
			String nicknameNew = null;
			boolean flag = true;
			try {
				//调用SSO的注册接口
				SSOResult registerResult = userService.register(username, password, security);
				if(registerResult != null && StringUtils.isNotBlank(registerResult.getCode())){
					String code = registerResult.getCode();
					String message = registerResult.getMessage();
					if("200".equals(code)){
						String token = null;
						//获取用户id
						SSOResult loginResult = userService.login(username, password);
						if(loginResult != null && StringUtils.isNotBlank(loginResult.getCode()) && "200".equals(loginResult.getCode())){
							JSONObject dataObj = JSONObject.fromObject(loginResult.getData());
							token = dataObj.getString("token");
							SSOResult userTokenDetailResult = userService.userTokenDetail(token);
							if(userTokenDetailResult != null && StringUtils.isNotBlank(userTokenDetailResult.getCode()) && "200".equals(userTokenDetailResult.getCode())){
								JSONObject detailDataObj = JSONObject.fromObject(userTokenDetailResult.getData());
								ssoUserId = detailDataObj.getString("userId");
							}else{
								logger.error("注册后调用获取userid的接口不成功,username={}",username);
								flag = false;
							}
							
							SSOResult userInfoResult = userService.userInfo(token);
							if(userInfoResult != null && StringUtils.isNotBlank(userInfoResult.getCode()) && "200".equals(userInfoResult.getCode())){
								JSONObject userInfoDataObj = JSONObject.fromObject(userInfoResult.getData());
								pic = userInfoDataObj.getString("avatar");
								nicknameNew = userInfoDataObj.getString("nickName");
							}else{
								logger.error("注册后调用获取userinfo的接口不成功,username={}",username);
								flag = false;
							}
						}else{
							logger.error("注册后调用登录接口不成功,username={}",username);
							flag = false;
						}
					}else if ("400".equals(code)) {
						//400, "验证码超时"
						map.put("code", "0");
						//map.put("msg", "验证码错误");
						map.put("msg", message);
						return map;
					}else if ("412".equals(code)) {
						//412, "号码重复注册"
						map.put("code", UsError.UsernameHasExisted.getCode());
						map.put("msg", UsError.UsernameHasExisted.getMessage());
						return map;
					}else{
						flag = false;
						logger.error("sso register result error,username={},code={},message={}",username,code,registerResult.getMessage());
					}
				}else{
					logger.error("SSO注册接口没返回正确的格式,username="+username);
					//SSO注册接口没返回正确格式
					flag = false;
				}
				if(!flag){
					map.put("code", "99");
					map.put("msg", "注册失败");
					logger.error("sso register result error,username="+username);
					return map;
				}
				//用户的昵称在默认未填写情况下 省略手机号中间4位，例如：138****8000
				if(StringUtils.isNotBlank(username) && username.equals(nickname) && nickname.length() == 11){
					if(StringUtils.isBlank(nicknameNew)){
						nickname = nickname.substring(0,3) + "****" + nickname.substring(7, 11);						
					}else{
						nickname = nicknameNew;
					}
				}
				password = MsgDigestUtil.MD5.digest2HEX(password);
				QQUser qqUser = new QQUser();
				qqUser.setNickName(nickname);
				qqUser.setPassword(password);
				qqUser.setQd(qd);
				qqUser.setTuid(tuid);
				qqUser.setUsername(username);
				this.qqUserRepositery.save(qqUser);
				
				String channel = req.getParameter("channel");

				//TODOchannel,qd, null, null 改为channel,qd, ssoUserId,pic
				user = userApiController.addUser(qqUser.getTuid(),
						qqUser.getNickName(), temppsw, username, "", "",
						"", "", "", NCUserState.默认.ordinal(),
						qqUser.getTuid(),channel,qd, ssoUserId, pic);

				/*********改造后取消调用hx.AddUser	BY lihaifei	2017-09-05**********/
				//hx.AddUser(user.get("_id").toString(), password, mainRedis);

				// 记录邀请信息
				String invite_user_id = req.getParameter("invite_user_id");
				String invite_user_type = req.getParameter("invite_user_type");
				String invite_time = req.getParameter("invite_time");
				String share_type = req.getParameter("share_type");
				if (StringUtils.isNotBlank(invite_user_id)) {
					BasicDBObject invite = new BasicDBObject();
					invite.append("_id", UUID.randomUUID().toString());
					// 邀请用户id
					invite.append("invite_user_id",Integer.valueOf(invite_user_id));
					// 邀请用户类型
					invite.append("invite_user_type",Integer.valueOf(invite_user_type));
					// 发起邀请的时间
					invite.append("invite_time", Long.valueOf(invite_time));
					// 邀请平台 0.微信朋友圈 1.微信 2.QQ 3.QQ空间
					invite.append("share_type", Integer.valueOf(share_type));
					// 注册时间
					invite.append("register_time",System.currentTimeMillis());
					// 被邀请人id
					invite.append("register_user_id", user.get("_id"));

					// System.out.println("=====invite_user_id:" +
					// invite_user_id);
					mainMongo.getCollection("invite").save(invite);
					
					// 赠送优惠券 消息对接发送消息
					
//						JSONObject jsonrequest = new JSONObject();
//						jsonrequest.put("user_id", user.get("_id"));
//						jsonrequest.put("time", System.currentTimeMillis());
//						messageProductorService.pushToMessageQueue("rabbit_queue_hqonline_discounts", jsonrequest.toString());
					
				}

				// "invite_user_id" : getUrlParam("invite_user_id"),
				// "invite_user_type" : getUrlParam("invite_user_type"),
				// "invite_time" : getUrlParam("invite_time"),
				// "share_type" : getUrlParam("share_type")

			} catch (Exception e) {
				e.printStackTrace();
				map.put("code", "99");
				map.put("msg", "注册失败");
			}

			map.put("code", "1");
			map.put("msg", "OK");
			
			/*********改造后取消调用AddUserToKjcity	BY lihaifei	2017-09-05**********/
			/*boolean isSuccess = userApiController.AddUserToKjcity(username, temppsw, tuid);
			if (isSuccess) {
				map.put("code", "1");
				map.put("msg", "OK");
			} else {
				// 删除原先写入的用户
				qquserMongo.getCollection("qQUser").remove(new BasicDBObject("tuid", tuid));
				mainMongo.getCollection("users").remove(new BasicDBObject("tuid", tuid));

				map.put("code", "99");
				map.put("msg", "注册失败");
			}*/
			// 删除redis中记录
			//removeRedisSecurity(username);
		}
		return map;

	}
	
	/** 注册 教师 雇员注册专属 */
	@RequestMapping("/tregister")
	public Map<String, Object> tregister(HttpServletRequest req, HttpServletResponse res) {
		Map<String, Object> map = new HashMap<String, Object>();

		String username = req.getParameter("user_name");
		String nickname = req.getParameter("nick_name");
		String password = req.getParameter("password");
		int priv = Integer.parseInt(req.getParameter("priv").toString());
		// 验证码
		String security = req.getParameter("security");
		/*boolean isSecurity = isRegisterController(username, security);
		if(security.equals(public_key))
		{
			isSecurity = true;
		}
	   if (!isSecurity) {
			map.put("code", "0");
			map.put("msg", "验证码错误");
		} else {*/
		String qd = req.getParameter("qd");
		qd = StringUtils.isBlank(qd) ? "kuaida" : qd;

		if (this.qqUserRepositery.findByUsername(username).size() > 0) {
			map.put("code", UsError.UsernameHasExisted.getCode());
			map.put("msg", UsError.UsernameHasExisted.getMessage());
		} else {
			String temppsw = password;
			DBObject user = null;
			String tuid = com.izhubo.rest.common.util.RandomUtil.getTuid();
			String ssoUserId = null;
			String pic = null;
			String nicknameNew = null;
			boolean flag = true;
			try {
				//调用SSO的注册接口
				SSOResult registerResult = userService.register(username, password, security);
				if(registerResult != null && StringUtils.isNotBlank(registerResult.getCode())){
					String code = registerResult.getCode();
					if("200".equals(code)){
						String token = null;
						//获取用户id
						SSOResult loginResult = userService.login(username, password);
						if(loginResult != null && StringUtils.isNotBlank(loginResult.getCode()) && "200".equals(loginResult.getCode())){
							JSONObject dataObj = JSONObject.fromObject(loginResult.getData());
							token = dataObj.getString("token");
							SSOResult userTokenDetailResult = userService.userTokenDetail(token);
							if(userTokenDetailResult != null && StringUtils.isNotBlank(userTokenDetailResult.getCode()) && "200".equals(userTokenDetailResult.getCode())){
								JSONObject detailDataObj = JSONObject.fromObject(userTokenDetailResult.getData());
								ssoUserId = detailDataObj.getString("userId");
							}else{
								logger.error("注册后调用获取userid的接口不成功,username={}",username);
								flag = false;
							}
							
							SSOResult userInfoResult = userService.userInfo(token);
							if(userInfoResult != null && StringUtils.isNotBlank(userInfoResult.getCode()) && "200".equals(userInfoResult.getCode())){
								JSONObject userInfoDataObj = JSONObject.fromObject(userInfoResult.getData());
								pic = userInfoDataObj.getString("avatar");
								nicknameNew = userInfoDataObj.getString("nickName");
							}else{
								logger.error("注册后调用获取userinfo的接口不成功,username={}",username);
								flag = false;
							}
						}else{
							logger.error("注册后调用登录接口不成功,username={}",username);
							flag = false;
						}
					}else if ("400".equals(code)) {
						//400, "验证码超时"
						map.put("code", "0");
						map.put("msg", "验证码错误");
						return map;
					}else if ("412".equals(code)) {
						//412, "号码重复注册"
						map.put("code", UsError.UsernameHasExisted.getCode());
						map.put("msg", UsError.UsernameHasExisted.getMessage());
						return map;
					}else{
						flag = false;
						logger.error("sso register result error,username={},code={},message={}",username,code,registerResult.getMessage());
					}
				}else{
					logger.error("SSO注册接口没返回正确的格式,username="+username);
					//SSO注册接口没返回正确格式
					flag = false;
				}
				if(!flag){
					map.put("code", "99");
					map.put("msg", "注册失败");
					logger.error("sso register result error,username="+username);
					return map;
				}
				
				
				// 用户的昵称在默认未填写情况下 省略手机号中间4位，例如：138****8000
				if(StringUtils.isNotBlank(username) && username.equals(nickname) && nickname.length() == 11){
					if(StringUtils.isBlank(nicknameNew)){
						nickname = nickname.substring(0,3) + "****" + nickname.substring(7, 11);						
					}else{
						nickname = nicknameNew;
					}
				}
				
				password = MsgDigestUtil.MD5.digest2HEX(password);
				QQUser qqUser = new QQUser();
				qqUser.setNickName(nickname);
				qqUser.setPassword(password);
				qqUser.setQd(qd);
				qqUser.setTuid(tuid);
				qqUser.setUsername(username);
				this.qqUserRepositery.save(qqUser);
				
				String channel = req.getParameter("channel");

				user = userApiController.addTUser(qqUser.getTuid(),
						qqUser.getNickName(), temppsw, username, "", "",
						"", "", "", NCUserState.默认.ordinal(),
						qqUser.getTuid(),channel,qd,priv,ssoUserId,pic);

				//hx.AddUser(user.get("_id").toString(), password, mainRedis);

				// 记录邀请信息
				String invite_user_id = req.getParameter("invite_user_id");
				String invite_user_type = req.getParameter("invite_user_type");
				String invite_time = req.getParameter("invite_time");
				String share_type = req.getParameter("share_type");
				if (StringUtils.isNotBlank(invite_user_id)) {
					BasicDBObject invite = new BasicDBObject();
					invite.append("_id", UUID.randomUUID().toString());
					// 邀请用户id
					invite.append("invite_user_id",Integer.valueOf(invite_user_id));
					// 邀请用户类型
					invite.append("invite_user_type",Integer.valueOf(invite_user_type));
					// 发起邀请的时间
					invite.append("invite_time", Long.valueOf(invite_time));
					// 邀请平台 0.微信朋友圈 1.微信 2.QQ 3.QQ空间
					invite.append("share_type", Integer.valueOf(share_type));
					// 注册时间
					invite.append("register_time",System.currentTimeMillis());
					// 被邀请人id
					invite.append("register_user_id", user.get("_id"));

					// System.out.println("=====invite_user_id:" +
					// invite_user_id);
					mainMongo.getCollection("invite").save(invite);
					
					
					// 赠送优惠券 消息对接发送消息
					
//						JSONObject jsonrequest = new JSONObject();
//						jsonrequest.put("user_id", user.get("_id"));
//						jsonrequest.put("time", System.currentTimeMillis());
//						messageProductorService.pushToMessageQueue("rabbit_queue_hqonline_discounts", jsonrequest.toString());
					
				}

				// "invite_user_id" : getUrlParam("invite_user_id"),
				// "invite_user_type" : getUrlParam("invite_user_type"),
				// "invite_time" : getUrlParam("invite_time"),
				// "share_type" : getUrlParam("share_type")

			} catch (Exception e) {
				e.printStackTrace();
				map.put("code", "99");
				map.put("msg", "注册失败");
			}

			map.put("code", "1");
			map.put("msg", "OK");
			
			/*boolean isSuccess = userApiController.AddUserToKjcity(username,
					temppsw, tuid);
			if (isSuccess) {
				map.put("code", "1");
				map.put("msg", "OK");
			} else {
				// 删除原先写入的用户
				qquserMongo.getCollection("qQUser").remove(
						new BasicDBObject("tuid", tuid));
				mainMongo.getCollection("users").remove(
						new BasicDBObject("tuid", tuid));

				map.put("code", "99");
				map.put("msg", "注册失败");
			}*/

			// 删除redis中记录
			//removeRedisSecurity(username);
		}
		return map;
	}

	/**
	 * 找回密码
	 * 
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	@RequestMapping("/resetpsw")
	public Map<String, Object> resetPsw(HttpServletRequest req, HttpServletResponse res) throws NumberFormatException, Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		String username = req.getParameter("user_name");
		String password = req.getParameter("password");
		// 验证码校验
		String security = req.getParameter("security");
		
		/*boolean isSecurity = isRegisterController(username, security);
		//给内部接口用的参数
		if(security.equals(public_key))
		{
			String k = req.getParameter("k");
			if(k.toLowerCase().equals(MsgDigestUtil.MD5.digest2HEX(username+password+inner_key).toLowerCase()) )
			{
				isSecurity = true;
			}
		}
		if (!isSecurity) {
			map.put("code", 0);
			map.put("msg", "验证码错误");
		}
	else {*/
		
		//调用SSO接口
		SSOResult resetPassWord = userService.resetPassWord(username, password, security);
		if(resetPassWord != null && StringUtils.isNotBlank(resetPassWord.getCode())){
			String code = resetPassWord.getCode();
			String message = resetPassWord.getMessage();
			if(code != null && "200".equals(code)){
				//SSO重置成功就行，不必更新mongodb的密码
				map.put("code", 1);
				map.put("msg", "OK");
				
				//DBObject db = qquserMongo.getCollection("qQUser").findOne(new BasicDBObject("username", username),new BasicDBObject("password", 1).append("tuid", 1));
				/*DBObject db = qquserMongo.getCollection("qQUser").findOne(new BasicDBObject("username", username));
				if (db != null) {
					DBObject user = mainMongo.getCollection("users").findOne(new BasicDBObject("tuid", db.get("tuid")));
					String password_md5 = MsgDigestUtil.MD5.digest2HEX(password);
					BasicDBObject up = new BasicDBObject("password", password_md5);
					// 修改密码
					qquserMongo.getCollection("qQUser").update(new BasicDBObject("username", username),new BasicDBObject("$set", up));
					// 同步修改用户的AES加密密码。
					logger.info(username + " start ackey reset");
					userApiController.SetAcKey(Integer.valueOf(user.get("_id").toString()), password);
					logger.info(username + " end ackey reset");
					// 删除redis中验证码记录
					//removeRedisSecurity(username);
					map.put("code", 1);
					map.put("msg", "OK");
				}else{
					map.put("code", 40005);
					map.put("msg", "此手机号未注册过，请返回注册！");
				}*/
			}else if (code != null && "400".equals(code)) {
				map.put("code", 49005);
				//map.put("msg", "验证码超时！");
				map.put("msg", message);
			}else{
				map.put("code", 49009);
				//map.put("msg", "请求重置密码失败!code="+code);
				map.put("msg", message);
			}
		}else{
			map.put("code", 40009);
			map.put("msg", "重置密码出错！");
		}
		return map;
	}
	/**
	 * 找回密码2.0(未被使用)
	 * 找回密码不能和原来的密码相同
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	@RequestMapping("/resetpsw_v200")
	public Map<String, Object> resetPsw_v200(HttpServletRequest req, HttpServletResponse res) throws NumberFormatException, Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String username = req.getParameter("user_name");
		String password = req.getParameter("password");
		// 验证码校验
		String security = req.getParameter("security");
		if (!isRegisterController(username, security)) {
			map.put("code", 0);
			map.put("msg", "验证码错误");
		}
		else if(isOldPassword(username, password)){
			map.put("code", 30319);
			map.put("msg", "新密码不能和上一次使用的密码相同");
		}
		else {
			//调用SSO接口
			SSOResult resetPassWord = userService.resetPassWord(username, password, security);
			if(resetPassWord != null && StringUtils.isNotBlank(resetPassWord.getCode())){
				String code = resetPassWord.getCode();
				if("200".equals(code)){
					//重置成功
					//DBObject db = qquserMongo.getCollection("qQUser").findOne(new BasicDBObject("username", username),new BasicDBObject("password", 1).append("tuid", 1));
					DBObject db = qquserMongo.getCollection("qQUser").findOne(new BasicDBObject("username", username));
					if (db != null) {
						DBObject user = mainMongo.getCollection("users").findOne(new BasicDBObject("tuid", db.get("tuid")));
						String password_md5 = MsgDigestUtil.MD5.digest2HEX(password);
						BasicDBObject up = new BasicDBObject("password", password_md5);
						// 修改密码
						qquserMongo.getCollection("qQUser").update(new BasicDBObject("username", username),new BasicDBObject("$set", up));
						// 同步修改用户的AES加密密码。
						logger.info(username + " start ackey reset");
						userApiController.SetAcKey(Integer.valueOf(user.get("_id").toString()), password);
						logger.info(username + " end ackey reset");
						// 删除redis中记录
						//removeRedisSecurity(username);
						map.put("code", 1);
						map.put("msg", "OK");
					}else{
						map.put("code", 40005);
						map.put("msg", "用户不存在！");
					}
				}else{
					map.put("code", 40005);
					map.put("msg", "用户不存在！");
				}
			}else{
				map.put("code", 40005);
				map.put("msg", "用户不存在！");
			}
		}
		return map;
	}

	/**
	 * 修改密码
	 * 
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	@RequestMapping("/changpsw")
	public Map<String, Object> changpsw(HttpServletRequest req,
			HttpServletResponse res) throws NumberFormatException, Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		String username = req.getParameter("user_name");
		String old_password = req.getParameter("old_password");
		String new_password = req.getParameter("new_password");

		String password_md5 = MsgDigestUtil.MD5.digest2HEX(old_password);
		DBObject db = qquserMongo.getCollection("qQUser").findOne(
				new BasicDBObject("username", username).append("password",
						password_md5),
				new BasicDBObject("password", 1).append("tuid", 1));
		if (db != null) {

			String newpassword_md5 = MsgDigestUtil.MD5.digest2HEX(new_password);

			/** 修改对应会计城中用户密码 异步 */
			syncData(username, old_password, new_password, newpassword_md5);

			BasicDBObject up = new BasicDBObject("password", newpassword_md5);
			// 修改密码
			qquserMongo.getCollection("qQUser").update(
					new BasicDBObject("username", username),
					new BasicDBObject("$set", up));

			DBObject user = mainMongo.getCollection("users").findOne(
					new BasicDBObject("tuid", db.get("tuid")));

			logger.info(username + " start huanxin reset");
			HuanxinController hx = new HuanxinController();
			hx.UpdatePassword(user.get("_id").toString(), newpassword_md5,
					mainRedis);
			logger.info(username + " end huanxin reset");

			// 同步修改用户的AES加密密码。
			logger.info(username + " start ackey reset");
			userApiController.SetAcKey(
					Integer.valueOf(user.get("_id").toString()), new_password);
			logger.info(username + " end ackey reset");
			// 删除redis中记录
			//removeRedisSecurity(username);
			map.put("code", 1);
			map.put("msg", "OK");
		} else {
			map.put("code", 40005);
			map.put("msg", "用户不存在！");
		}

		return map;
	}

	/**
	 * 判断验证码是否正确
	 * 
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	@RequestMapping("/isregkey")
	public Map<String, Object> isRegKey(HttpServletRequest req,
			HttpServletResponse res) throws NumberFormatException, Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		String username = req.getParameter("phone");
		String security = req.getParameter("security");
		// 验证码校验

		 if (!isRegisterController(username, security)) {
		//if (false) {
			map.put("code", 0);
			map.put("msg", "验证码错误");
		} else {

			map.put("code", 1);
			map.put("msg", "OK");

		}

		return map;
	}
	
	public boolean isOldPassword(String username , String password){
		boolean res = false;
		String password_md5 = MsgDigestUtil.MD5.digest2HEX(password);
		res = qquserMongo.getCollection("qQUser").count(new BasicDBObject("username", username).append("password", password_md5)) > 0;
		return res;
	}

	// 校验验证码
	public boolean isRegisterController(String phone, String security) {
		boolean res = false;
		if (StringUtils.isNotBlank(security) && StringUtils.isNotBlank(phone)) {
			String rs = mainRedis.opsForValue().get(
					HK.SECURITY.getSecurityKey(phone));
//			SecurityController.getSecurityKey(phone));
			res = security.equals(rs);
		}
		return res;
	}

	// 删除验证码
	public void removeRedisSecurity(String phone) {
		String k1 = mainRedis.opsForValue().get(HK.SECURITY.getSecurityKey(phone));

		if (k1 != null) {
			if (mainRedis.hasKey(k1)) {
				mainRedis.delete(k1);
			}
		}
	}

	public void syncData(final String username, final String oldpassword,
			final String password, final String password_md5) {
		StaticSpring.execute(new Runnable() {
			public void run() {

				logger.info(username + " start kjcity reset");
				Boolean syncSuccess = userApiController.UpdateKjcityPsw(
						username, oldpassword, password);
				logger.info(username + " end kjcity reset " + syncSuccess);

				BasicDBObject up = new BasicDBObject("sync_success",
						syncSuccess);
				// 修改密码
				qquserMongo.getCollection("qQUser").update(
						new BasicDBObject("username", username),
						new BasicDBObject("$set", up));
			}
		});
	}
	

}
