package io.renren.service;

import io.renren.entity.LogWatchEntity;

import java.util.List;
import java.util.Map;

/**
 * 观看直播录播日志-考勤记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-18 14:43:29
 */
public interface LogWatchService {
	
		
	LogWatchEntity queryObject(Map<String, Object> map);
	
	List<LogWatchEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(LogWatchEntity logWatch);
	
	void update(LogWatchEntity logWatch);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
	LogWatchEntity queryByLvId(Map<String, Object> map);
	
	int queryExistCount(String mId);
	/**
	 * 查询自考1.0MongoDB数据库的录播考勤记录-总数
	 * @return
	 */
	int queryCountMongodbLog();
	/**
	 * 查询自考1.0MongoDB数据库的录播考勤记录-明细
	 * @return
	 */
	List<LogWatchEntity> queryListMongodbLog(int offset , int limit);
	
	
		
}
