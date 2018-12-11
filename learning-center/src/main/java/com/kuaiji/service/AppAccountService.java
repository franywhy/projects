package com.kuaiji.service;

import com.kuaiji.entity.AppAccount;

import java.util.Date;

public interface AppAccountService {

	AppAccount findIsStudentByCode(String code);

	AppAccount findIsStudentByAppId(Integer appId);
	
	void updateDrByAccountId(Integer accountid);

	AppAccount findByUsernameAppid(String username, Integer appid);

	void addAppAccount(AppAccount appAccount);

	void updatePTByUsernameAppid(String userpass, Date createtime, String username, Integer appid);

	AppAccount findIsTeacherByAppId(Integer appId);
}
