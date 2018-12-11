package com.elise.userinfocenter.service.impl;

import com.elise.userinfocenter.dao.BalanceDao;
import com.elise.userinfocenter.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("balanceService")
public class BalanceServiceImpl implements BalanceService {

	@Autowired
	private BalanceDao balanceDao;

	@Override
	public Map<String,Object> queryBalanceByUserId(Long userId) {
		return balanceDao.queryBalanceByUserId(userId);
	}

	@Override
	public Double queryHqgByUserId(Long userId) {
		return balanceDao.queryHqgByUserId(userId);
	}
}
