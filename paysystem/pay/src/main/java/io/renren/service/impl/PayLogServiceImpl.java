package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.PayLogDao;
import io.renren.entity.PayLogEntity;
import io.renren.service.PayLogService;



@Service("payLogService")
public class PayLogServiceImpl implements PayLogService {
	@Autowired
	private PayLogDao payLogDao;
	
	@Override
	public PayLogEntity queryObject(Map<String, Object> map){
		return payLogDao.queryObject(map);
	}
	
	@Override
	public List<PayLogEntity> queryList(Map<String, Object> map){
		return payLogDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return payLogDao.queryTotal(map);
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
	public void delete(Map<String, Object> map){
		payLogDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		payLogDao.deleteBatch(map);
	}

	@Override
	public int queryLogExist(Map<String, Object> map) {
		return payLogDao.queryLogExist(map);
	}
	
	
}
