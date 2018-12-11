package com.hq.learningcenter.school.service.impl;

import com.hq.learningcenter.school.dao.SysUserDao;
import com.hq.learningcenter.school.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;
	
	@Override
	public Long queryUserIdByMobile(Long mobileNo) {
		
		return sysUserDao.querUserIdByMobile(mobileNo);
	}

}
