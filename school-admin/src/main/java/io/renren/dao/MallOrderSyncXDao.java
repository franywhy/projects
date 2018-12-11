package io.renren.dao;

import io.renren.entity.MallOrderSyncXEntity;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-20 20:59:42
 */
public interface MallOrderSyncXDao extends BaseMDao<MallOrderSyncXEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
