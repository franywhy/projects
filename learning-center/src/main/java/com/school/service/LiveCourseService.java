package com.school.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.school.pojo.*;
import com.school.utils.ClientEnum;

public interface LiveCourseService {
	
	List<ClassplanPOJO> getLiveDetail(Long userId, Long userplanId, String businessId, Date date, ClientEnum client);
	
	List<CourseCalendarPOJO> getLiveCalendar(Long userId, Long userplanId, String businessId, String startDate, String endDate);
	
	List<ClassplanPOJO> getLiveSchedule(Long userId, Long userplanId, String businessId, ClientEnum client);
	
	Map<String, Object> getLiveHear(Long userplanId, String businessId);
	
	List<HopePOJO> getLiveHope(Long userplanId, String businessId, Date date);

	List<HopePOJO> getLiveHopeForWeb(Long userplanId, String businessId, Date date);
	
	MaterialPOJO getMaterial(Long userplanId, String businessId);
	
	String getMaterialContent(Long detailId);

	//app4.0获取直播课程排课课程表
    List<ClassplanPOJO> getClassPlanSchedule(Long userId, Long userplanId, String businessId, ClientEnum client);

    //app4.0获取直播课程课次课程表
    List<ClassplanLivesPOJO> getClassPlanDetailSchedule(Long userId, String classplanId, Long classtypeId, String businessId, ClientEnum client);
}
