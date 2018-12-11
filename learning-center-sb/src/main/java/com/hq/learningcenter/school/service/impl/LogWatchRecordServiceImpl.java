package com.hq.learningcenter.school.service.impl;

import com.hq.learningcenter.school.dao.LogWatchRecordDao;
import com.hq.learningcenter.school.entity.LogWatchRecordEntity;
import com.hq.learningcenter.school.service.LogWatchRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("logWatchRecordService")
public class LogWatchRecordServiceImpl implements LogWatchRecordService {
	@Autowired
	private LogWatchRecordDao logWatchRecordDao;
	
	@Override
	public void save(LogWatchRecordEntity logWatchRecordEntity) {
		logWatchRecordDao.save(logWatchRecordEntity);
	}

	@Override
	public int queryRecordNum(Long recordId, Long userId) {
		return this.logWatchRecordDao.queryRecordNum(recordId, userId);
	}

	@Override
	public void update(LogWatchRecordEntity logWatchRecordEntity) {
		this.logWatchRecordDao.update(logWatchRecordEntity);
	}

}
