package com.school.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.school.entity.LogWatchRecordEntity;

import java.util.List;

/**
 * Created by Glenn on 2017/7/20 0020.
 */
@Repository
public interface LogWatchRecordDao {

    Integer queryAttendCount(@Param("userId") Long userId,
    						 @Param("list") List<Long> courseIdList,
    						 @Param("attend") Integer attend);

	void save(LogWatchRecordEntity logWatchRecordEntity);

	int queryRecordNum(@Param("recordId")Long recordId, @Param("userId")Long userId);

	int update(LogWatchRecordEntity logWatchRecordEntity);

}
