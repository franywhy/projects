package com.hqjy.pay.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hqjy.pay.BorrowMoneyEntity;
import com.hqjy.pay.BorrowMoneyService;
import com.hqjy.pay.mapper.BorrowMoneyDao;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
@Service
public class BorrowMoneyServiceImpl implements BorrowMoneyService {
	@Autowired
	private BorrowMoneyDao borrowMoneyDao;
	@Override
	public BorrowMoneyEntity queryObject(Map<String, Object> map){
		return borrowMoneyDao.queryObject(map);
	}
	
	@Override
	public void save(BorrowMoneyEntity borrowMoney){
		borrowMoneyDao.save(borrowMoney);
	}
	
	@Override
	public void update(BorrowMoneyEntity borrowMoney){
		borrowMoneyDao.update(borrowMoney);
	}
}
