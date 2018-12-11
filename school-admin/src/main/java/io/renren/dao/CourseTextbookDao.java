package io.renren.dao;

import io.renren.entity.CourseTextbookDetailEntity;
import io.renren.entity.CourseTextbookEntity;

import java.util.Map;

/**
 * 教材档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-16 16:09:29
 */
public interface CourseTextbookDao extends BaseMDao<CourseTextbookDetailEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	void update(CourseTextbookEntity courseTextbook);

	void save(CourseTextbookEntity courseTextbook);
	
	
}
