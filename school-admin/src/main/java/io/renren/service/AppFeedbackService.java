package io.renren.service;

import io.renren.entity.AppFeedbackEntity;

import java.util.List;
import java.util.Map;

/**
 * APP意见反馈表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-20 14:27:17
 */
public interface AppFeedbackService {
	
		
	AppFeedbackEntity queryObject(Map<String, Object> map);
	
	List<AppFeedbackEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(AppFeedbackEntity appFeedback);
	
	void update(AppFeedbackEntity appFeedback);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
		
		
}
