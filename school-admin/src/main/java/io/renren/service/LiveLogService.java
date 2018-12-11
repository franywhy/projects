package io.renren.service;

import io.renren.entity.LiveLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 观看直播日志-有效记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-22 09:57:27
 */
public interface LiveLogService {
	
		
	LiveLogEntity queryObject(Map<String, Object> map);
	
	List<LiveLogEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(LiveLogEntity liveLog);
	
	void update(LiveLogEntity liveLog);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
	int liveLogExist(String mId);
		
		
}
