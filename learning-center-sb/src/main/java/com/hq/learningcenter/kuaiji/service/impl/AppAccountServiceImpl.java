package com.hq.learningcenter.kuaiji.service.impl;

import com.hq.learningcenter.kuaiji.dao.AppAccountMapper;
import com.hq.learningcenter.kuaiji.entity.AppAccountExample;
import com.hq.learningcenter.kuaiji.service.AppAccountService;
import com.hq.learningcenter.kuaiji.entity.AppAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class AppAccountServiceImpl implements AppAccountService {

	@Autowired
	private AppAccountMapper appAccountMapper;

	@Override
	public AppAccount findIsStudentByCode(String code) {
		AppAccountExample example = new AppAccountExample();
		example.createCriteria().andCodeEqualTo(code).andDrEqualTo(0).andIsTeacherEqualTo(0);
		return appAccountMapper.selectByExampleFetchOne(example);
	}

	@Override
	public AppAccount findIsStudentByAppId(Integer appId) {
		AppAccountExample example = new AppAccountExample();
		example.createCriteria().andAppidEqualTo(appId).andDrEqualTo(0).andIsTeacherEqualTo(0);
		return appAccountMapper.selectByExampleFetchOne(example);
	}

	@Override
	public AppAccount findIsTeacherByAppId(Integer appId) {
		AppAccountExample example = new AppAccountExample();
		example.createCriteria().andAppidEqualTo(appId).andDrEqualTo(0).andIsTeacherEqualTo(1);
		return appAccountMapper.selectByExampleFetchOne(example);
	}

	@Override
	@Transactional(value = "transactionManager",readOnly = false)
	public void updateDrByAccountId(Integer accountid) {
		appAccountMapper.updateDrByPrimaryKey(1, accountid);
	}

	@Override
	public AppAccount findByUsernameAppid(String username, Integer appid) {
		AppAccountExample example = new AppAccountExample();
		example.createCriteria().andUsernameEqualTo(username).andAppidEqualTo(appid);
		return appAccountMapper.selectByExampleFetchOne(example);
	}

	@Override
	@Transactional(value = "transactionManager",readOnly = false)
	public void addAppAccount(AppAccount appAccount) {
		appAccountMapper.insert(appAccount);
	}

	@Override
	@Transactional(value = "transactionManager",readOnly = false)
	public void updatePTByUsernameAppid(String userpass, Date createtime, String username, Integer appid) {
		appAccountMapper.updatePTByUsernameAppid(userpass, createtime, username, appid);
	}
}
