package com.hq.learningapi.service;

import java.util.List;
import java.util.Map;

/**
 * Created by LiuHai on 2017/12/29
 */

public interface AppTodayTotalCourseNumService {

	List<Long> queryUserplanIdListByUserId(Long userId);

	Long queryClassTypeIdByUserplanId(Long userplanId);
	
	List<Long> queryUserplanDetailIdListByUserplanId(Long userplanId);

	String queryClassplanIdByUserplanDetailId(Long userplanDetailId);

	int queryCourseNumByClassplanIdAndToday(String classplanId, String today, Long classTypeId);

	List<Map<String, Object>> getTodayTotalCourseNum(Long userId, String start_time, String end_time);


		
}
