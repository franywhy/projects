package io.renren.dao;

import io.renren.entity.LiveLogEntity;
import java.util.Map;

/**
 * 观看直播日志-有效记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-22 09:57:27
 */
public interface LiveLogDao extends BaseMDao<LiveLogEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
