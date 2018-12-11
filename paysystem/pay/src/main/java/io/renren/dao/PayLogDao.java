package io.renren.dao;

import io.renren.entity.PayLogEntity;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-11 11:57:44
 */
public interface PayLogDao extends BaseDao<PayLogEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	int queryLogExist(Map<String,Object> map);
}
