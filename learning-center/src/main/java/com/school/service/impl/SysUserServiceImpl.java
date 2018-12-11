package com.school.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.dao.SysUserDao;
import com.school.service.SysUserService;

@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;
	
	@Override
	public Long queryUserIdByMobile(Long mobileNo) {
		
		return sysUserDao.querUserIdByMobile(mobileNo);
	}

}
