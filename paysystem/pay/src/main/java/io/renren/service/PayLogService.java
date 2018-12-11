package io.renren.service;

import io.renren.entity.PayLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-11 11:57:44
 */
public interface PayLogService {
	
		
	PayLogEntity queryObject(Map<String, Object> map);
	
	List<PayLogEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(PayLogEntity payLog);
	
	void update(PayLogEntity payLog);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
	int queryLogExist(Map<String,Object> map);
		
}
