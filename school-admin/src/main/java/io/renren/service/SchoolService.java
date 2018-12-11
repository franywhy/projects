package io.renren.service;

import io.renren.entity.SchoolEntity;

import java.util.List;
import java.util.Map;

/**
 * 网校基本配置表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-13 14:58:08
 */
public interface SchoolService {
	
		
	SchoolEntity queryObject(Map<String, Object> map);
	
	List<SchoolEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SchoolEntity school);
	
	void update(SchoolEntity school);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
		
		
}
