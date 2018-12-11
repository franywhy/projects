package io.renren.service;

import io.renren.entity.FreeAssessmentEntity;

import java.util.List;
import java.util.Map;

/**
 * 免考核档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-02-26 10:31:23
 */
public interface FreeAssessmentService {
	
	FreeAssessmentEntity queryObject(Long id);
	
	List<FreeAssessmentEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(FreeAssessmentEntity freeAssessment);

	void saveBatch(List<FreeAssessmentEntity> list);
	
	void update(FreeAssessmentEntity freeAssessment);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	void updateStatusBatch(Map<String, Object> map);
}
