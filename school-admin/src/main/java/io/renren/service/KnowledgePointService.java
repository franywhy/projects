package io.renren.service;

import io.renren.entity.KnowledgePointEntity;
import io.renren.mongo.entity.CourseEntity;
import io.renren.mongo.entity.MongoPagination;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 14:06:17
 */
public interface KnowledgePointService {
	
		
	KnowledgePointEntity queryObject(Map<String, Object> map);
	
	List<KnowledgePointEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(KnowledgePointEntity knowledgePoint);
	
	void update(KnowledgePointEntity knowledgePoint);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
	/**
	 * 获取课程信息
	 * @return
	 */
	public MongoPagination findAllCourse(int pageNo, int pageSize);
	
	public MongoPagination findCourseByName(int pageNo, int pageSize,String name);
	
	/**
	 * 根据知识点ID获取资料信息
	 * @param map
	 * @return
	 */
	List queryListForDetail(Map<String, Object> map);

	int queryTotalForDetail(Map<String, Object> map);

	List queryList1(Map<String, Object> map);
	
		
		
}
