package io.renren.service;

import io.renren.entity.MallOrderSyncEntity;

import java.util.List;
import java.util.Map;

/**
 * NC订单同步异常表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-24 16:28:16
 */
public interface MallOrderSyncService {
	
		
	MallOrderSyncEntity queryObject(Map<String, Object> map);
	
	List<MallOrderSyncEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MallOrderSyncEntity mallOrderSync);
	
	void update(MallOrderSyncEntity mallOrderSync);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
		
		
}
