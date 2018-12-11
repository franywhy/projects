package io.renren.dao;

import io.renren.entity.RegistrationEntity;
import java.util.Map;

/**
 * 报考档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-14 17:21:05
 */
public interface RegistrationDao extends BaseDao<RegistrationEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
