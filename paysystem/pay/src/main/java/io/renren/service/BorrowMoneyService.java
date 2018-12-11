package io.renren.service;

import io.renren.entity.BorrowMoneyEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-28 09:22:10
 */
public interface BorrowMoneyService {
	
		
	BorrowMoneyEntity queryObject(Map<String, Object> map);
	
	List<BorrowMoneyEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(BorrowMoneyEntity borrowMoney);
	
	void update(BorrowMoneyEntity borrowMoney);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
		
		
}
