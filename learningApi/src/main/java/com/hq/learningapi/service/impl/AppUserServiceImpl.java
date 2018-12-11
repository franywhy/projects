package com.hq.learningapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.learningapi.dao.AppUserDao;
import com.hq.learningapi.service.AppUserService;

@Service("appUserService")
public class AppUserServiceImpl implements AppUserService {
	@Autowired
	private AppUserDao appUserDao;
	@Override
	public List<Long> queryUserIdList() {
		return this.appUserDao.queryUserIdList();
	}

}
