package io.renren.dao;

import io.renren.entity.SysSchoolSyncEntity;
import java.util.Map;

/**
 * 部门校区同步异常表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-01-30 16:47:45
 */
public interface SysSchoolSyncDao extends BaseMDao<SysSchoolSyncEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
