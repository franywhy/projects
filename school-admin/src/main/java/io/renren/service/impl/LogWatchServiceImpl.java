package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.LogWatchDao;
import io.renren.entity.LogWatchEntity;
import io.renren.service.LogWatchService;
import io.renren.utils.Constant;



@Service("logWatchService")
public class LogWatchServiceImpl implements LogWatchService {
	@Autowired
	private LogWatchDao logWatchDao;
	
	@Override
	public LogWatchEntity queryObject(Map<String, Object> map){
		return logWatchDao.queryObject(map);
	}
	
	@Override
	public List<LogWatchEntity> queryList(Map<String, Object> map){
		return logWatchDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return logWatchDao.queryTotal(map);
	}
	
	@Override
	public void save(LogWatchEntity logWatch){
		logWatchDao.save(logWatch);
	}
	
	@Override
	public void update(LogWatchEntity logWatch){
		logWatchDao.update(logWatch);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		logWatchDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		logWatchDao.deleteBatch(map);
	}

	@Override
	public LogWatchEntity queryByLvId(Map<String, Object> map) {
		return logWatchDao.queryByLvId(map);
	}

	@Override
	public int queryExistCount(String mId) {
		return logWatchDao.queryExistCount(mId);
	}

	/**
	 * 查询自考1.0MongoDB数据库的录播考勤记录-总数
	 * @return
	 */
	@Override
	public int queryCountMongodbLog() {
		return logWatchDao.queryCountMongodbLog();
	}
	/**
	 * 查询自考1.0MongoDB数据库的录播考勤记录-明细
	 * @return
	 */
	@Override
	public List<LogWatchEntity> queryListMongodbLog(int offset, int limit) {
		return logWatchDao.queryListMongodbLog(offset, limit);
	}
	
	
}
