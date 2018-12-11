package com.hq.learningapi.service;


import java.util.List;
import java.util.Map;

import com.hq.learningapi.entity.AppConfigEntity;

/**
 * app常量记录表
 * 
 * @author zhaowenwei
 * @date 2018-02-26 14:58:44
 */
public interface AppConfigService {

	String queryValueByKey(Long key);

	AppConfigEntity queryObject(Long id);
	
	List<AppConfigEntity> queryList(Map<String, Object> map);
	
	/*int queryTotal(Map<String, Object> map);
	
	void save(AppConfigEntity appConfig);
	
	void update(AppConfigEntity appConfig);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);*/
}
