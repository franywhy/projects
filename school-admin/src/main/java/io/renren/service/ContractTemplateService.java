package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.ContractTemplateEntity;

public interface ContractTemplateService {
	
	ContractTemplateEntity queryObject(Map<String, Object> map);
	
	List<ContractTemplateEntity> queryList(Map<String, Object> map);
	 
	List<Map<String,Object>> queryListMap(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);
	
	void save(ContractTemplateEntity contractTemplate);
	
	int update(ContractTemplateEntity contractTemplate);
	
	int delete(Map<String, Object> map);
	  
}
