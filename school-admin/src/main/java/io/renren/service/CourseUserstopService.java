package io.renren.service;

import io.renren.entity.CourseUserstopEntity;

import java.util.List;
import java.util.Map;

/**
 * 休学档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-12 10:38:36
 */
public interface CourseUserstopService {
	
		
	CourseUserstopEntity queryObject(Map<String, Object> map);
	
	List<CourseUserstopEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseUserstopEntity courseUserstop);
	
	void update(CourseUserstopEntity courseUserstop);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
}
