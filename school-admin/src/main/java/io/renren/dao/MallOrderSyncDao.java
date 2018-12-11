package io.renren.dao;

import io.renren.entity.MallOrderSyncEntity;
import java.util.Map;

/**
 * NC订单同步异常表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-24 16:28:16
 */
public interface MallOrderSyncDao extends BaseMDao<MallOrderSyncEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
