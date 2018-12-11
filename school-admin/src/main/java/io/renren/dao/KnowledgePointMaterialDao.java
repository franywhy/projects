package io.renren.dao;

import io.renren.entity.KnowledgePointMaterialEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 14:06:17
 */
public interface KnowledgePointMaterialDao extends BaseMDao<KnowledgePointMaterialEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	List findByPointId(Map<String, Object> map);
	
	int deleteByMaterialId(int id);
}
