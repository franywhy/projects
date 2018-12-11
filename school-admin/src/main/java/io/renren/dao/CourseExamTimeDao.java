package io.renren.dao;

import io.renren.entity.CourseExamTimeEntity;
import java.util.Map;

/**
 * 考试倒计时
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-27 09:48:19
 */
public interface CourseExamTimeDao extends BaseMDao<CourseExamTimeEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
