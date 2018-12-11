package io.renren.dao;

import io.renren.entity.LogWatchEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 观看直播录播日志-考勤记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-18 14:43:29
 */
public interface LogWatchDao extends BaseMDao<LogWatchEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
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
	List<LogWatchEntity> queryListMongodbLog(@Param("offset")int offset , @Param("limit")int limit);
}
