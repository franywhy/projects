package io.renren.service;

import io.renren.entity.CourseGuideEntity;

import java.util.List;
import java.util.Map;

/**
 * 流程指南
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-09 09:58:40
 */
public interface CourseGuideService {
	
		
	Map<String, Object> queryMap(Map<String, Object> map);
	
	List<Map<String, Object>> queryListMap(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseGuideEntity courseGuide);
	
	void update(CourseGuideEntity courseGuide);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
	//判断是否有专业的引用
		 int checkProfession(long id);	
		 
		 void pause(Long[] numbers);
			
		void resume(Long[] numbers);
		
}
