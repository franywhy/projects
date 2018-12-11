package io.renren.service;

import io.renren.entity.MallMarketCourseEntity;
import io.renren.pojo.MallMarketCoursePOJO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-01-19 11:58:28
 */
public interface MallMarketCourseService {
	
		
	MallMarketCourseEntity queryObject(Map<String, Object> map);
	
	List<MallMarketCourseEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MallMarketCourseEntity mallMarketCourse);
	
	void update(MallMarketCourseEntity mallMarketCourse);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	List<MallMarketCoursePOJO> queryListClassifyPOJO(Map<String, Object> map);

	int queryTotalClassify(Map<String, Object> map);

	List<MallMarketCoursePOJO> queryListPOJO(Map<String, Object> map);

	List<MallMarketCoursePOJO> queryClassifyDownList(Map<String, Object> map);

	void pause(Long[] ids);

	void resume(Long[] ids);

	MallMarketCoursePOJO queryObjectPOJO(Map<String, Object> map);
	
		
		
}
