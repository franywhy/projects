package com.school.accountant.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.school.accountant.service.UserService;
import com.school.accountant.util.EncryptionUtils;
import com.school.accountant.util.HttpUtil;
import com.school.accountant.util.JsonUtil;
import com.school.accountant.vo.SSOResult;

@Service
public class UserServiceImpl  implements UserService{

	@Value("${sso_login_post_url}")
	private String SSO_LOGIN_POST_URL;
	
	@Value("${sso_userInfo_get}")
	private String SSO_USERINFO_GET;
	
	@Value("${sso_userMobileNo_get}")
	private String SSO_USERMOBILENO_GET;
	
	@Value("${sso_logout_post}")
	private String SSO_LOGOUT_POST;
	
	@Value("${sso_sendSMS_get}")
	private String SSO_SENDSMS_GET;
	
	@Value("${sso_register_post}")
	private String SSO_REGISTER_POST;
	
	@Value("${sso_passWord_post}")
	private String SSO_PASSWORD_POST;
	
	@Value("${sso_checkMobileNo_get}")
	private String SSO_CHECKMOBILENO_GET;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public SSOResult login(String loginName, String password, Integer timeout) {
		SSOResult sSOResult = null;
		//拼装SSO接口的请求url
		String loginUrl = SSO_LOGIN_POST_URL.replaceAll("@mobileNo", loginName);
		loginUrl = loginUrl.replaceAll("@passWord", EncryptionUtils.base64Encode(password));
		//请求SSO接口
		logger.debug("sso login url="+loginUrl);
		String result = null;
		try {
			result = HttpUtil.doPostWithJson4SSO(loginUrl);
		} catch (Exception e) {
			logger.error("访问SSO登陆接口出错");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		if(StringUtils.isNotBlank(result)){
			try {
				sSOResult = JsonUtil.jsonToPojo(result, SSOResult.class);
			} catch (Exception e) {
				logger.error("SSO登陆接口返回结果转换出错！接口返回结果result="+result);
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return sSOResult;
	}

	@Override
	public SSOResult userInfo(String token) {
		SSOResult sSOResult = null;
		//拼装SSO接口的请求url
		String userInfoUrl = SSO_USERINFO_GET.replaceAll("@token", token);
		String result = HttpUtil.doGet4Json(userInfoUrl);
		if(StringUtils.isNotBlank(result)){
			try {
				sSOResult = JsonUtil.jsonToPojo(result, SSOResult.class);
			} catch (Exception e) {
				logger.error("SSO[/inner/userInfo]接口返回结果转换出错！接口返回结果result="+result);
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return sSOResult;
	}

	@Override
	public SSOResult userMobileNo(String token) {
		SSOResult sSOResult = null;
		//拼装SSO接口的请求url
		String mobileUrl = SSO_USERMOBILENO_GET.replaceAll("@token", token);
		String result = HttpUtil.doGet4Json(mobileUrl);
		if(StringUtils.isNotBlank(result)){
			try {
				sSOResult = JsonUtil.jsonToPojo(result, SSOResult.class);
			} catch (Exception e) {
				logger.error("SSO[/inner/userMobileNo]接口返回结果转换出错！接口返回结果result="+result);
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return sSOResult;
	}
	
	@Override
	public SSOResult logout(String token) {
		SSOResult sSOResult = null;
		//拼装SSO接口的请求url
		String logoutUrl = SSO_LOGOUT_POST.replaceAll("@token", token);
		String result = HttpUtil.doPostWithJson4SSO(logoutUrl);
		if(StringUtils.isNotBlank(result)){
			try {
				sSOResult = JsonUtil.jsonToPojo(result, SSOResult.class);
			} catch (Exception e) {
				logger.error("SSO[/inner/logout]接口返回结果转换出错！接口返回结果result="+result);
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return sSOResult;
	}
	
	@Override
	public SSOResult sendSMS(String mobileNo) {
		SSOResult sSOResult = null;
		//拼装SSO接口的请求url
		String sendSMSUrl = SSO_SENDSMS_GET.replaceAll("@mobileNo", mobileNo);
		String result = HttpUtil.doGet4Json(sendSMSUrl);
		if(StringUtils.isNotBlank(result)){
			try {
				sSOResult = JsonUtil.jsonToPojo(result, SSOResult.class);
			} catch (Exception e) {
				logger.error("SSO[/inner/otpSMS]接口返回结果转换出错！接口返回结果result="+result);
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return sSOResult;
	}
	
	@Override
	public SSOResult register(String mobileNo, String password, String SMSCode) {
		SSOResult sSOResult = null;
		//拼装SSO接口的请求url
		String register = SSO_REGISTER_POST.replaceAll("@mobileNo", mobileNo);
		register = register.replaceAll("@passWord", password);
		register = register.replaceAll("@otp", SMSCode);
		String result = HttpUtil.doPostWithJson4SSO(register);
		if(StringUtils.isNotBlank(result)){
			try {
				sSOResult = JsonUtil.jsonToPojo(result, SSOResult.class);
			} catch (Exception e) {
				logger.error("SSO[/inner/register]接口返回结果转换出错！接口返回结果result="+result);
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return sSOResult;
	}
	
	
	@Override
	public SSOResult resetPassWord(String mobileNo, String password, String SMSCode) {
		SSOResult sSOResult = null;
		//拼装SSO接口的请求url
		String resetPassword = SSO_PASSWORD_POST.replaceAll("@mobileNo", mobileNo);
		resetPassword = resetPassword.replaceAll("@passWord", password);
		resetPassword = resetPassword.replaceAll("@otp", SMSCode);
		String result = HttpUtil.doPostWithJson4SSO(resetPassword);
		if(StringUtils.isNotBlank(result)){
			try {
				sSOResult = JsonUtil.jsonToPojo(result, SSOResult.class);
			} catch (Exception e) {
				logger.error("SSO[/inner/passWord]接口返回结果转换出错！接口返回结果result="+result);
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return sSOResult;
	}
	
	@Override
	public SSOResult checkMobileNo(String mobileNo) {
		SSOResult sSOResult = null;
		//拼装SSO接口的请求url
		String checkMobileNo = SSO_CHECKMOBILENO_GET.replaceAll("@mobileNo", mobileNo);
		String result = HttpUtil.doGet4Json(checkMobileNo);
		if(StringUtils.isNotBlank(result)){
			try {
				sSOResult = JsonUtil.jsonToPojo(result, SSOResult.class);
			} catch (Exception e) {
				logger.error("SSO[/inner/checkMobileNo]接口返回结果转换出错！接口返回结果result="+result);
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return sSOResult;
	}
}
