package com.school.service;

import com.school.entity.LogWatchRecordEntity;

public interface LogWatchRecordService {

	public void save(LogWatchRecordEntity logWatchRecordEntity);
	
	//根据recordId查寻对应观看日志数量
	public int queryRecordNum(Long recordId, Long userId);

	public void update(LogWatchRecordEntity logWatchRecordEntity);

}
