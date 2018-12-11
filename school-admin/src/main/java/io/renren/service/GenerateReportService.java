package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.AttendReportEntity;
import io.renren.entity.CourseUserplanEntity;

public interface GenerateReportService {
	
	//根据时间,查询这个时间范围内有课的排课id
	List<String> queryClassplanIdList(Map<String,Object> map);
	
	//根据classplanId,查询关联有该排课的学员规划
	List<CourseUserplanEntity> queryUserplanByClassplan(String classplanId);
	
	List<Long> queryTeacherByClassplan(Map<String,Object> map);
	
	AttendReportEntity queryAttendPer(Map<String,Object> map);
	
	void saveWeekReport(AttendReportEntity entity);
	
	void saveMonthReport(AttendReportEntity entity);
}
