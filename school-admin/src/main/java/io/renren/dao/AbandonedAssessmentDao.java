package io.renren.dao;

import io.renren.entity.AbandonedAssessmentEntity;
import java.util.Map;

/**
 * 弃考档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-02-26 16:03:06
 */
public interface AbandonedAssessmentDao extends BaseDao<AbandonedAssessmentEntity> {
	
	 /**
     * 批量更新审核状态
     */	
	public void updateStatusBatch(Map<String, Object> map);
}
