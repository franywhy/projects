package com.hqjy.pay.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hqjy.pay.PayLogEntity;
import com.hqjy.pay.PayLogService;
import com.hqjy.pay.mapper.PayLogDao;

import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
@Service
public class PayLogServiceImpl implements PayLogService {
	@Autowired
	private PayLogDao payLogDao;
	
	@Override
	public PayLogEntity queryObject(Map<String, Object> map){
		return payLogDao.queryObject(map);
	}
	@Override
	public void save(PayLogEntity payLog){
		payLogDao.save(payLog);
	}
	
	@Override
	public void update(PayLogEntity payLog){
		payLogDao.update(payLog);
	}
	@Override
	public boolean queryTotal(String tradeNo) {
		int count=payLogDao.queryTotal(tradeNo);
		if(count==1){
			return true;
		}else{
			return false;
		}
	}
}
