package io.renren.dao;

import io.renren.entity.CourseScoreRecordDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 分数登记子表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-04 09:27:06
 */
public interface CourseScoreRecordDetailDao extends BaseMDao<CourseScoreRecordDetailEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	List<Map<String, Object>> queryAllByPK(Map<String, Object> map);

}
