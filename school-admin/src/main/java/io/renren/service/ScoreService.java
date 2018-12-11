package io.renren.service;

import io.renren.entity.AbandonedAssessmentEntity;
import io.renren.entity.RegistrationEntity;
import io.renren.entity.ScoreEntity;

import java.util.List;
import java.util.Map;

/**
 * 成绩档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-15 14:34:23
 */
public interface ScoreService {
	
	public void updateStatusBatch(Map<String, Object> map);	
	
	ScoreEntity queryObject(Long id);
	
	List<ScoreEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ScoreEntity score);
	
	public void saveBatch(List<ScoreEntity> detailList);
	
	void update(ScoreEntity score);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
		
		
}
