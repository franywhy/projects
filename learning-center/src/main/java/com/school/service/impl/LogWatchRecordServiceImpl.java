package com.school.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.dao.LogWatchRecordDao;
import com.school.entity.LogWatchRecordEntity;
import com.school.service.LogWatchRecordService;

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
