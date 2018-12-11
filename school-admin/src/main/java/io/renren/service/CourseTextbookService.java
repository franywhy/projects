package io.renren.service;

import io.renren.entity.CourseTextbookDetailEntity;
import io.renren.entity.CourseTextbookEntity;

import java.util.List;
import java.util.Map;

/**
 * 教材档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-16 16:09:29
 */
public interface CourseTextbookService {
	
		
	CourseTextbookDetailEntity queryObject(Map<String, Object> map);
	
	List<CourseTextbookDetailEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseTextbookEntity courseTextbook);
	
	void update(CourseTextbookEntity courseTextbook);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
	void pause(Long[] ids);

	void resume(Long[] ids);
		
}
