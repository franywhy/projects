package io.renren.dao;

import io.renren.entity.KnowledgePointEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 14:06:17
 */
public interface KnowledgePointDao extends BaseMDao<KnowledgePointEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	List queryListForDetail(Map<String, Object> map);

	int queryTotalForDetail(Map<String, Object> map);
	
	List queryList1(Map<String, Object> map);
}
