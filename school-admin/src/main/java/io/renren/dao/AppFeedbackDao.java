package io.renren.dao;

import io.renren.entity.AppFeedbackEntity;
import java.util.Map;

/**
 * APP意见反馈表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-20 14:27:17
 */
public interface AppFeedbackDao extends BaseMDao<AppFeedbackEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
