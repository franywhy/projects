package io.renren.service;

import io.renren.entity.MallFeedbackRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-24 16:39:34
 */
public interface MallFeedbackRecordService {
	
	MallFeedbackRecordEntity queryObject(Long id);
	
	List<MallFeedbackRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MallFeedbackRecordEntity mallFeedbackRecord);
	
	void update(MallFeedbackRecordEntity mallFeedbackRecord);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
	
	void pause(Long[] ids);
	
	void resume(Long[] ids);
	
}
