package io.renren.service;

import io.renren.entity.MallBussinessOppEntity;

import java.util.List;
import java.util.Map;

/**
 * 商机记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-28 15:14:18
 */
public interface MallBussinessOppService {
	
		
	MallBussinessOppEntity queryObject(Map<String, Object> map);
	
	List<MallBussinessOppEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MallBussinessOppEntity mallBussinessOpp);
	
	void update(MallBussinessOppEntity mallBussinessOpp);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
		
		
}
