package io.renren.dao;

import io.renren.entity.NcCourseClassplanLogEntity;
import java.util.Map;

/**
 * 双师排课,队列接收nc排课错误消息记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-08-21 15:04:35
 */
public interface NcCourseClassplanLogDao extends BaseMDao<NcCourseClassplanLogEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
