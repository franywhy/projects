package io.renren.service;

import io.renren.entity.PushTimeTemplateEntity;

import java.util.List;
import java.util.Map;

/**
 * 推送内容模板表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-10-25 11:31:58
 */
public interface PushTimeTemplateService {
	
		
	PushTimeTemplateEntity queryObject(Map<String, Object> map);
	
	List<PushTimeTemplateEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(PushTimeTemplateEntity pushTimeTemplate);
	
	void update(PushTimeTemplateEntity pushTimeTemplate);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
		
		
}
