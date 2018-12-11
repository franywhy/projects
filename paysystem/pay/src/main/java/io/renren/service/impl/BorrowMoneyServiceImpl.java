package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.BorrowMoneyDao;
import io.renren.entity.BorrowMoneyEntity;
import io.renren.service.BorrowMoneyService;



@Service("borrowMoneyService")
public class BorrowMoneyServiceImpl implements BorrowMoneyService {
	@Autowired
	private BorrowMoneyDao borrowMoneyDao;
	
	@Override
	public BorrowMoneyEntity queryObject(Map<String, Object> map){
		return borrowMoneyDao.queryObject(map);
	}
	
	@Override
	public List<BorrowMoneyEntity> queryList(Map<String, Object> map){
		return borrowMoneyDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return borrowMoneyDao.queryTotal(map);
	}
	
	@Override
	public void save(BorrowMoneyEntity borrowMoney){
		borrowMoneyDao.save(borrowMoney);
	}
	
	@Override
	public void update(BorrowMoneyEntity borrowMoney){
		borrowMoneyDao.update(borrowMoney);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		borrowMoneyDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		borrowMoneyDao.deleteBatch(map);
	}
	
	
}
