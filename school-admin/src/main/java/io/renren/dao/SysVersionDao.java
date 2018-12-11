package io.renren.dao;

import io.renren.entity.SysVersionEntity;
import java.util.Map;

/**
 * 移动端版本控制
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-27 16:31:29
 */
public interface SysVersionDao extends BaseMDao<SysVersionEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	int checkSysVersion(Map<String, Object> map);
}
