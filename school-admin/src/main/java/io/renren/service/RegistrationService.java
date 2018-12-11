package io.renren.service;

import io.renren.entity.RegistrationEntity;

import java.util.List;
import java.util.Map;

/**
 * 报考档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-14 17:21:05
 */
public interface RegistrationService {
	
		
	RegistrationEntity queryObject(Map<String, Object> map);
	
	RegistrationEntity queryObject(Long id);
	
	List<RegistrationEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(RegistrationEntity registration);
	
	void update(RegistrationEntity registration);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
	public void saveBatch(List<RegistrationEntity> list);	
		
}
