package io.renren.dao;

import io.renren.entity.SysLogEntity;
import java.util.Map;

/**
 * 系统日志
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-29 09:57:44
 */
public interface SysLogDao extends BaseMDao<SysLogEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
