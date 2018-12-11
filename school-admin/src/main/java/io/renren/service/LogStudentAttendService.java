package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.pojo.log.LiveAttendPOJO;
import io.renren.pojo.log.LogStudentAttendPOJO;

public interface LogStudentAttendService {
	
	List<LogStudentAttendPOJO> queryLiveAttend(Map<String,Object> map);
	
	List<LogStudentAttendPOJO> queryLiveAttendList(Map<String,Object> map);
	
	List<LiveAttendPOJO> queryWeekGroupByClass(Map<String,Object> map);
	
	List<LiveAttendPOJO> queryWeekGroupByGoods(Map<String,Object> map);
	
	List<LiveAttendPOJO> queryWeekGroupByClassplan(Map<String,Object> map);
	
	List<LiveAttendPOJO> queryMonthGroupByClass(Map<String,Object> map);
	
	List<LiveAttendPOJO> queryMonthGroupByGoods(Map<String,Object> map);
	
	List<LiveAttendPOJO> queryMonthGroupByClassplan(Map<String,Object> map);
	
	Long queryClassTotal(Map<String,Object> map);
}
