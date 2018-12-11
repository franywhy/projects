package io.renren.dao;

import java.util.List;
import java.util.Map;

import io.renren.entity.CourseUserplanServiceEntity;

/**
 * 服务记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-20 16:47:00
 */
public interface CourseUserplanServiceDao extends BaseMDao<CourseUserplanServiceEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	List<Map<String, Object>> queryListMap(Map<String, Object> map);
	Map<String, Object> queryMap(Map<String, Object> map);
}
