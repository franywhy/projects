package io.renren.dao;

import io.renren.entity.MsgContentCommonEntity;
import java.util.Map;

/**
 * 站内消息
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-16 17:41:49
 */
public interface MsgContentCommonDao extends BaseMDao<MsgContentCommonEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
