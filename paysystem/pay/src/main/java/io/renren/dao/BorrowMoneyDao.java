package io.renren.dao;

import io.renren.entity.BorrowMoneyEntity;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-28 09:22:10
 */
public interface BorrowMoneyDao extends BaseDao<BorrowMoneyEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
