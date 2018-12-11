package io.renren.service;

import java.util.List;
import java.util.Map;

public interface AttendanceStatisticsService {
	
	/**
	 * 学员考勤统计
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryAttendanceList(Map<String, Object> map);
	/**
	 * 被统计考勤的学员总数
	 * @param map
	 * @return
	 */
	int queryAttendanceTotal(Map<String, Object> map);
	/**
	 * 考勤详情
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryDetailsList(Map<String, Object> map);
	/**
	 * 考勤详情总数
	 * @param map
	 * @return
	 */
	int queryDetailsTotal(Map<String, Object> map);

}
