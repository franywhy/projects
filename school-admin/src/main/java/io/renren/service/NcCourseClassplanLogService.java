package io.renren.service;

import io.renren.entity.NcCourseClassplanLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 双师排课,队列接收nc排课错误消息记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-08-21 15:04:35
 */
public interface NcCourseClassplanLogService {
	
		
	NcCourseClassplanLogEntity queryObject(Map<String, Object> map);
	
	List<NcCourseClassplanLogEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(NcCourseClassplanLogEntity ncCourseClassplanLog);
	
	void update(NcCourseClassplanLogEntity ncCourseClassplanLog);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
		
		
}
