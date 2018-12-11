package com.hq.learningcenter.kuaiji.service;

import com.hq.learningcenter.kuaiji.entity.Users;

public interface UsersService {

	Users findByMobile(String mobile);

}
