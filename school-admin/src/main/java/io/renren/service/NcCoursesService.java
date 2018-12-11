package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.NcCoursesEntity;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 14:06:17
 */
public interface NcCoursesService {
	
		
	NcCoursesEntity queryObject(Map<String, Object> map);
	
	List<NcCoursesEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(NcCoursesEntity courses);
	
	void update(NcCoursesEntity courses);
	
	void delete(Map<String, Object> map);
	
	void syncData(List<NcCoursesEntity> coursess);
	
	void syncData();
	

		
}
