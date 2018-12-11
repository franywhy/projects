package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.NcAttendanceEntity;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 14:06:17
 */
public interface NcAttendanceService {
	
		
	NcAttendanceEntity queryObject(Map<String, Object> map);
	
	List<NcAttendanceEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(NcAttendanceEntity ncattendance);
	
	void update(NcAttendanceEntity ncattendance);
	
	void delete(Map<String, Object> map);
	
	void saveBatch(List<NcAttendanceEntity> ncattendances);
	
	List getAttendanceInfo(Map<String, Object> map);
	
	/**
	 * 同步数据
	 */
	void syncData();
	
	/**
	 * 定时任务获取考勤相关的数据
	 * @return
	 */
	List getAttendanceData();
	
	
	
	

		
}
