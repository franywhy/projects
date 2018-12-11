package io.renren.dao;

import io.renren.entity.CourseUserstopEntity;
import java.util.Map;

/**
 * 休学档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-12 10:38:36
 */
public interface CourseUserstopDao extends BaseMDao<CourseUserstopEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
}
