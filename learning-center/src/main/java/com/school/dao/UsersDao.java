package com.school.dao;

import com.school.entity.UsersEntity;

/**
 * 用户
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 09:58:34
 */
public interface UsersDao{
	
	UsersEntity queryUser(Long userId);
	
	String queryNickname(Long userId);
}
