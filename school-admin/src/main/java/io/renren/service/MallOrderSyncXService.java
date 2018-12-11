package io.renren.service;

import io.renren.entity.MallOrderSyncXEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-20 20:59:42
 */
public interface MallOrderSyncXService {
	
		
	MallOrderSyncXEntity queryObject(Map<String, Object> map);
	
	List<MallOrderSyncXEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MallOrderSyncXEntity mallOrderSyncX);
	
	void update(MallOrderSyncXEntity mallOrderSyncX);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
		
		
}
