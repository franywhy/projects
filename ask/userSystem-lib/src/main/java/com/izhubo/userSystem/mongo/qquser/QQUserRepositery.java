package com.izhubo.userSystem.mongo.qquser;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface QQUserRepositery extends
		PagingAndSortingRepository<QQUser, BigInteger> {

	List<QQUser> findByOpenId(String openId);

	List<QQUser> findByUsername(String username);

	List<QQUser> findByNickName(String nickName);

	List<QQUser> findByTuid(String tuid);

	List<QQUser> findByUsernameAndPassword(String username, 
			String password);
	
	List<QQUser> findByPk(String pk);

}
