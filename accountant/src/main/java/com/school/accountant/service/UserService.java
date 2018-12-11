package com.school.accountant.service;

import com.school.accountant.vo.SSOResult;

public interface UserService {

	/**
	 * 登录
	 * @param loginName
	 * @param password
	 * @param timeout
	 * @return
	 */
	SSOResult login(String loginName, String password, Integer timeout);
	
	/**
	 * 调用SSO接口获取用户昵称，头像等信息
	 * @param token
	 * @return
	 */
	SSOResult userInfo(String token);
	
	/**
	 * 调用SSO接口获取用户手机号码
	 * @param token
	 * @return
	 */
	SSOResult userMobileNo(String token);
	
	SSOResult logout(String token);
	
	SSOResult sendSMS(String mobileNo);
	
	SSOResult register(String mobileNo, String password, String SMSCode);
	
	/**
	 * 重置密码
	 * @param mobileNo	手机号码
	 * @param password	新密码
	 * @param SMSCode	短信验证码
	 * @return
	 */
	SSOResult resetPassWord(String mobileNo, String password, String SMSCode);
	
	/**
	 * 检查手机号是否已经注册
	 * @param mobileNo	手机号码
	 * @return
	 */
	SSOResult checkMobileNo(String mobileNo);
}
