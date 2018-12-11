package io.renren.dao;

import io.renren.entity.PayOrderEntity;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-06 18:29:01
 */
public interface PayOrderDao extends BaseDao<PayOrderEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
