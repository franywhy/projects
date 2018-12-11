package io.renren.dao;

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
public interface QuestionBankDao extends BaseMDao<QuestionBankEntity> {

	int saveBatch(List list);
	
	void deleteAll();
	
	List queryCourse();
	
	List queryChapter(Map<String, Object> map);
	
	List queryVerse(Map<String, Object> map);
	
}
