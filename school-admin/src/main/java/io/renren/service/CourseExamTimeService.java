package io.renren.service;

import io.renren.entity.CourseExamTimeEntity;

import java.util.List;
import java.util.Map;

/**
 * 考试倒计时
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-27 09:48:19
 */
public interface CourseExamTimeService {
	
		
	CourseExamTimeEntity queryObject(Map<String, Object> map);
	
	List<CourseExamTimeEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseExamTimeEntity courseExamTime);
	
	void update(CourseExamTimeEntity courseExamTime);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
		
		
}
