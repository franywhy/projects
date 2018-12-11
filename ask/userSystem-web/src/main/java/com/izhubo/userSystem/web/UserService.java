package com.izhubo.userSystem.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.hqonline.model.HK;
import com.izhubo.rest.AppProperties;
import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.rest.common.util.MsgDigestUtil;
import com.izhubo.rest.common.util.http.HttpClientUtil4_3;
import com.izhubo.userSystem.mongo.qquser.QQUser;
import com.izhubo.userSystem.mongo.qquser.QQUserRepositery;
import com.izhubo.userSystem.utils.NCUserState;
import com.izhubo.userSystem.vo.SSOResult;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Service
public class UserService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected QQUserRepositery qqUserRepositery;

	@Resource
	protected UserApiController userApiController;
	
	@Resource
	protected StringRedisTemplate mainRedis;

	@Resource
	protected MongoTemplate qquserMongo;
	
	@Resource
	protected MongoTemplate mainMongo;
	
	public SSOResult login(String loginName, String password) throws UnsupportedEncodingException {
		SSOResult sSOResult = null;
		//拼装SSO接口的请求url
		String SSO_LOGIN_POST = AppProperties.get("sso_login_post");
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("mobileNo", loginName);
		params.put("passWord", Base64.encodeBase64String(password.getBytes("UTF-8")));
		params.put("clientType", "web");
		params.put("versionCode", "1");
		//请求SSO接口
		logger.debug("sso login url="+SSO_LOGIN_POST);
		String result = null;
		try {
			result = HttpClientUtil4_3.post(SSO_LOGIN_POST, params, null);
		} catch (Exception e) {
			logger.error("访问SSO登陆接口出错");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		if(StringUtils.isNotBlank(result)){
			try {
				sSOResult = JSONUtil.jsonToBean(result, SSOResult.class);
			} catch (Exception e) {
				logger.error("SSO登陆接口返回结果转换出错！接口返回结果result="+result);
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return sSOResult;
	}
	
	/**
	 * 对于SSO中存在，但Mongodb中不存在的用户，需要把SSO的用户信息同步到Mongodb
	 * @param mobileNo
	 * @param password 密码(明文)
	 * @return
	 */
	public void synUserToMongo(String token, String mobileNo, String password) {
		
		String username = mobileNo;
		String nickname = "";
		String pic = "";
		String temppsw = password;
		String ssoUserId = "";
		String channel = "";//原业务是从入参中获取
		String qd = "kuaida";
		String tuid = com.izhubo.rest.common.util.RandomUtil.getTuid();
		SSOResult userInfo = this.userInfo(token);
		if(userInfo != null && StringUtils.isNotBlank(userInfo.getCode()) && "200".equals(userInfo.getCode())){
			JSONObject dataObj = JSONObject.fromObject(userInfo.getData());
			pic = dataObj.getString("avatar");
			nickname = dataObj.getString("nickName");
		}else{
			return;
		}
		
		SSOResult userTokenDetail = this.userTokenDetail(token);
		if(userTokenDetail != null && StringUtils.isNotBlank(userTokenDetail.getCode()) && "200".equals(userTokenDetail.getCode())){
			JSONObject dataObj = JSONObject.fromObject(userTokenDetail.getData());
			ssoUserId = dataObj.getString("userId");
		}else{
			return;
		}
		try {
			// 用户的昵称在默认未填写情况下 省略手机号中间4位，例如：138****8000
			if(StringUtils.isNotBlank(username) && username.equals(nickname) && nickname.length() == 11){
				nickname = nickname.substring(0,3) + "****" + nickname.substring(7, 11);
			}
			password = MsgDigestUtil.MD5.digest2HEX(password);
			QQUser qqUser = new QQUser();
			qqUser.setNickName(nickname);
			qqUser.setPassword(password);
			qqUser.setQd(qd);
			qqUser.setTuid(tuid);
			qqUser.setUsername(username);
			this.qqUserRepositery.save(qqUser);
			userApiController.addUser(qqUser.getTuid(),qqUser.getNickName(), temppsw, username, "", "","", "", "", NCUserState.默认.ordinal(), qqUser.getTuid(),channel,qd,ssoUserId,pic);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 对于MongoDB里SSOUserId为空的用户，需要把SSO的userId同步到MongoDB
	 * @param token
	 * @param mobileNo
	 */
	public void synSSOUserIdToMongo(String token, String tuid) {
		
		String ssoUserId = "";
		SSOResult userTokenDetail = this.userTokenDetail(token);
		if(userTokenDetail != null && StringUtils.isNotBlank(userTokenDetail.getCode()) && "200".equals(userTokenDetail.getCode())){
			JSONObject dataObj = JSONObject.fromObject(userTokenDetail.getData());
			ssoUserId = dataObj.getString("userId");
			if(StringUtils.isNotBlank(ssoUserId)){
				//设置sso_userid
				BasicDBObject updateDB = new BasicDBObject("sso_userid", ssoUserId);
				mainMongo.getCollection("users").update(new BasicDBObject("tuid", tuid),new BasicDBObject("$set", updateDB));
			}
		}
	}

	/**获取用户头像，昵称等信息
	 * "avatar": "string",	头像
    	"email": "string",
    	"gender": 0,	性别
    	"nickName": "string"	昵称
	 * @param token
	 * @return
	 */
	public SSOResult userInfo(String token) {
		SSOResult sSOResult = null;
		//拼装SSO接口的请求url
		String SSO_USERINFO_GET = AppProperties.get("sso_userInfo_get");
		String userInfoUrl = SSO_USERINFO_GET+"?token="+token;
		String result = null;
		try {
			result = HttpClientUtil4_3.get(userInfoUrl, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(StringUtils.isNotBlank(result)){
			try {
				sSOResult = JSONUtil.jsonToBean(result, SSOResult.class);
			} catch (Exception e) {
				logger.error("SSO[/inner/userInfo]接口返回结果转换出错！接口返回结果result="+result);
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return sSOResult;
	}
	
	/**
	 * 获取userId等
	 * "clientType": "WEB",
    	"oneTimeToken": true,
	    "timeStamp": 0,
	    "userId": 0,
	    "versionCode": 0
	 * @param token
	 * @return
	 */
	public SSOResult userTokenDetail(String token) {
		SSOResult sSOResult = null;
		//拼装SSO接口的请求url
		String SSO_USERTOKENDETAIL_GET = AppProperties.get("sso_userTokenDetail_get");
		String mobileUrl = SSO_USERTOKENDETAIL_GET+"?token="+token;
		String result = null;
		try {
			result = HttpClientUtil4_3.get(mobileUrl,null);
			if(StringUtils.isNotBlank(result)){
				sSOResult = JSONUtil.jsonToBean(result, SSOResult.class);
			}
		} catch (IOException e) {
			logger.error("SSO[/inner/userTokenDetail]出错！"+result);
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return sSOResult;
	}
	
	public SSOResult userMobileNo(String token) {
		SSOResult sSOResult = null;
		//拼装SSO接口的请求url
		String SSO_USERMOBILENO_GET = AppProperties.get("sso_userMobileNo_get");
		String mobileUrl = SSO_USERMOBILENO_GET+"?token="+token;
		String result = null;
		try {
			result = HttpClientUtil4_3.get(mobileUrl,null);
			if(StringUtils.isNotBlank(result)){
				sSOResult = JSONUtil.jsonToBean(result, SSOResult.class);
			}
		} catch (IOException e) {
			logger.error("SSO[/inner/userMobileNo]接口返回结果转换出错！接口返回结果result="+result);
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return sSOResult;
	}
	
	public SSOResult logout(String token) {
		SSOResult sSOResult = null;
		Map<String,String> param = new HashMap<>();
		//拼装SSO接口的请求url
		String SSO_LOGOUT_POST = AppProperties.get("sso_logout_post");
		param.put("token", token);
		String result = null;
		try {
			result = HttpClientUtil4_3.post(SSO_LOGOUT_POST, param, null);
			if(StringUtils.isNotBlank(result)){
				sSOResult = JSONUtil.jsonToBean(result, SSOResult.class);
			}
		} catch (IOException e) {
			logger.error("SSO[/inner/logout]接口返回结果转换出错！接口返回结果result="+result);
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		return sSOResult;
	}
	
	public SSOResult sendSMS(String mobileNo) {
		SSOResult sSOResult = null;
		//拼装SSO接口的请求url
		String SSO_SENDSMS_GET = AppProperties.get("sso_sendSMS_get");
		String sendSMSUrl = SSO_SENDSMS_GET + "?mobileNo=" + mobileNo;
		String result = null;
		try {
			result = HttpClientUtil4_3.get(sendSMSUrl,null);
			if(StringUtils.isNotBlank(result)){
				sSOResult = JSONUtil.jsonToBean(result, SSOResult.class);
			}
		} catch (IOException e) {
			logger.error("SSO[/inner/otpSMS]接口返回结果转换出错！接口返回结果result="+result);
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		return sSOResult;
	}
	
	/**
	 * 
	 * @param mobileNo
	 * @param password	密码(明文)
	 * @param SMSCode
	 * @return
	 */
	public SSOResult register(String mobileNo, String password, String SMSCode) {
		SSOResult sSOResult = null;
		Map<String,String> param = new HashMap<>();
		String passEncode = new String(Base64.encodeBase64(password.getBytes()));
		//拼装SSO接口的请求url
		String SSO_REGISTER_POST = AppProperties.get("sso_register_post");
		param.put("mobileNo", mobileNo);
		param.put("passWord", passEncode);
		param.put("otp", SMSCode);
		String result = null;
		try {
			result = HttpClientUtil4_3.post(SSO_REGISTER_POST, param, null);
			if(StringUtils.isNotBlank(result)){
				sSOResult = JSONUtil.jsonToBean(result, SSOResult.class);
			}
		} catch (IOException e) {
			logger.error("SSO[/inner/register]接口返回结果转换出错！接口返回结果result="+result);
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		return sSOResult;
	}
	
	/**
	 * 重置密码
	 * @param mobileNo
	 * @param password
	 * @param SMSCode
	 * @return
	 */
	public SSOResult resetPassWord(String mobileNo, String password, String SMSCode) {
		SSOResult sSOResult = null;
		Map<String,String> param = new HashMap<>();
		String passEncode = new String(Base64.encodeBase64(password.getBytes()));
		//拼装SSO接口的请求url
		String SSO_PASSWORD_POST = AppProperties.get("sso_passWord_post");
		param.put("mobileNo", mobileNo);
		param.put("passWord", passEncode);
		param.put("otp", SMSCode);
		String result = null;
		try {
			result = HttpClientUtil4_3.post(SSO_PASSWORD_POST,param,null);
			if(StringUtils.isNotBlank(result)){
				sSOResult = JSONUtil.jsonToBean(result, SSOResult.class);
			}
		} catch (IOException e) {
			logger.error("SSO[/inner/passWord]接口返回结果转换出错！接口返回结果result="+result);
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		return sSOResult;
	}
	
	
	/**
	 * 判断手机号是否存在
	 * @param mobileNo
	 * @return
	 */
	public SSOResult checkMobileNo(String mobileNo) {
		SSOResult sSOResult = null;
		//拼装SSO接口的请求url
		String SSO_CHECKMOBILENO_GET = AppProperties.get("sso_checkMobileNo_get");
		String mobileUrl = SSO_CHECKMOBILENO_GET+"?mobileNo="+mobileNo;
		String result = null;
		try {
			result = HttpClientUtil4_3.get(mobileUrl,null);
			if(StringUtils.isNotBlank(result)){
				sSOResult = JSONUtil.jsonToBean(result, SSOResult.class);
			}
		} catch (IOException e) {
			logger.error("SSO[/inner/checkMobileNo]接口返回结果转换出错！接口返回结果result="+result);
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return sSOResult;
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
}
