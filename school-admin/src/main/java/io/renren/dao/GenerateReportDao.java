package io.renren.dao;

import java.util.List;
import java.util.Map;

import io.renren.entity.AttendReportEntity;
import io.renren.entity.CourseUserplanEntity;

public interface GenerateReportDao {
	
	List<String> queryClassplanIdList(Map<String,Object> map);
	
	List<CourseUserplanEntity> queryUserplanByClassplan(String classplanId);
	
	AttendReportEntity queryAttendPer(Map<String,Object> map);
	
	void saveWeekReport(AttendReportEntity entity);
	
	void saveMonthReport(AttendReportEntity entity);
	
	List<Long> queryTeacherByClassplan(Map<String,Object> map);
	

}
