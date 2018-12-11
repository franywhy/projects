package com.hq.learningapi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by LiuHai on 2017/12/29
 */

@Repository
public interface AppTodayTotalCourseNumDao {

	List<Long> queryUserplanIdListByUserId(@Param("userId")Long userId);

	List<Long> queryUserplanDetailIdListByUserplanId(@Param("userplanId")Long userplanId);

	String queryClassplanIdByUserplanDetailId(@Param("userplanDetailId")Long userplanDetailId);

	int queryCourseNumByClassplanIdAndToday(@Param("classplanId")String classplanId, @Param("today")String today, @Param("classTypeId")Long classTypeId);

	Long queryClassTypeIdByUserplanId(@Param("userplanId")Long userplanId);

}
