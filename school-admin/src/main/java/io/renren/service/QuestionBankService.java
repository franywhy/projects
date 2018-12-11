package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.QuestionBankEntity;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 14:06:17
 */
public interface QuestionBankService {
	
		
	QuestionBankEntity queryObject(Map<String, Object> map);
	
	List<QuestionBankEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(QuestionBankEntity knowledgePoint);
	
	void update(QuestionBankEntity knowledgePoint);
	
	void delete(Map<String, Object> map);

	void syncData(List list);
	
	List queryCourse();
	
	List queryChapter(Map<String, Object> map);
	
	List queryVerse(Map<String, Object> map);
	
	void syncData();
		
}
