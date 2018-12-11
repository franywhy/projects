package io.renren.dao;

import io.renren.entity.SchoolEntity;
import java.util.Map;

/**
 * 网校基本配置表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-13 14:58:08
 */
public interface SchoolDao extends BaseMDao<SchoolEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
