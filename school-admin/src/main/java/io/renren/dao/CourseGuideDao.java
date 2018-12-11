package io.renren.dao;

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
public interface CourseGuideDao extends BaseMDao<CourseGuideEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	List<Map<String, Object>> queryListMap(Map<String, Object> map);
	
	Map<String, Object> queryMap(Map<String, Object> map);
	//判断是否有专业的引用
	 int checkProfession(long id);
}
