package com.kuaiji.service.impl;

import com.kuaiji.dao.UsersMapper;
import com.kuaiji.entity.Users;
import com.kuaiji.entity.UsersExample;
import com.kuaiji.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {

	@Autowired
	private UsersMapper usersMapper;

	@Override
	public Users findByMobile(String mobile) {
		UsersExample example = new UsersExample();
		example.createCriteria().andMobileEqualTo(mobile).andDrEqualTo((byte)0);
		return usersMapper.selectByExampleFetchOne(example);
	}
}
