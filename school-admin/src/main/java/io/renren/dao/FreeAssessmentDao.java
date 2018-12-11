package io.renren.dao;

import io.renren.entity.FreeAssessmentEntity;

import java.util.Map;

/**
 * 免考核档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-02-26 10:31:23
 */
public interface FreeAssessmentDao extends BaseDao<FreeAssessmentEntity> {
    /**
     * 批量更新审核状态
     */
	void updateStatusBatch(Map<String, Object> map);

}
