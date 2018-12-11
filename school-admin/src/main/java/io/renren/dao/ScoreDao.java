package io.renren.dao;

import io.renren.entity.ScoreEntity;
import java.util.Map;

/**
 * 成绩档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-15 14:34:23
 */
public interface ScoreDao extends BaseDao<ScoreEntity> {
	/**
	 * 批量更新状态
	 */
	public void updateStatusBatch(Map<String, Object> map);
}
