package io.renren.service;

import io.renren.entity.AbandonedAssessmentEntity;
import io.renren.entity.FreeAssessmentEntity;

import java.util.List;
import java.util.Map;

/**
 * 弃考档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-02-26 16:03:06
 */
public interface AbandonedAssessmentService {
		
	public void updateStatusBatch(Map<String, Object> map); 
		
	AbandonedAssessmentEntity queryObject(Long id);
	
	List<AbandonedAssessmentEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(AbandonedAssessmentEntity abandonedAssessment);
	
	public void saveBatch(List<AbandonedAssessmentEntity> list);
			
	void update(AbandonedAssessmentEntity abandonedAssessment);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Long[] ids);

	
		
		
}
