package com.school.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.pojo.UserstopPOJO;
import com.school.service.UserstopService;
import com.school.dao.UserstopDao;
import com.school.entity.UserstopEntity;

@Service("userstopService")
public class UserstopServiceImpl implements UserstopService {
	@Autowired
	private UserstopDao UserstopDao;
	
	@Override
	public List<UserstopPOJO> quseryList(Long userId, List<Long> productIdList) {
		try {
			return this.UserstopDao.quserList(userId, productIdList);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void update(Long id) {
		this.UserstopDao.update(id);
	}

	@Override
	public void save(UserstopEntity userstopEntity) {
		
		this.UserstopDao.save(userstopEntity);
	}

	@Override
	public List<Map<String, Object>> queryClasstypeList(Long userId, List<Long> productIdList) {
		try {
			return this.UserstopDao.queryClasstypeList(userId, productIdList);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Long> queryProductId(String businessId) {
		try {
			return this.UserstopDao.queryProductId(businessId);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Long queryProductIdByUserplanId(Long userplanId) {
		return this.UserstopDao.queryProductIdByUserplanId(userplanId);
	}
}