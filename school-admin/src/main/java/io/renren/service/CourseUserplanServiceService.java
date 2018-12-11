package io.renren.service;

import io.renren.entity.CourseUserplanServiceEntity;

import java.util.List;
import java.util.Map;

/**
 * 服务记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-20 16:47:00
 */
public interface CourseUserplanServiceService {
	
		
	CourseUserplanServiceEntity queryObject(Map<String, Object> map);
	Map<String, Object> queryMap(Map<String, Object> map);
	
	List<CourseUserplanServiceEntity> queryList(Map<String, Object> map);
	List<Map<String, Object>> queryListMap(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseUserplanServiceEntity courseUserplanService);
	
	void update(CourseUserplanServiceEntity courseUserplanService);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
		
		
}
