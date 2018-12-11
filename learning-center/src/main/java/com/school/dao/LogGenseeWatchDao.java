package com.school.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Glenn on 2017/7/20 0020.
 */
@Repository
public interface LogGenseeWatchDao {

    Float queryAttendPer(@Param("userId") Long userId,
	                     @Param("businessId") String businessId);

}
