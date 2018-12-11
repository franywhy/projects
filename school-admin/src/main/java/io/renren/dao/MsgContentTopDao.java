package io.renren.dao;

import io.renren.entity.MsgContentTopEntity;
import java.util.Map;

/**
 * 弹窗消息
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-19 14:12:25
 */
public interface MsgContentTopDao extends BaseMDao<MsgContentTopEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
