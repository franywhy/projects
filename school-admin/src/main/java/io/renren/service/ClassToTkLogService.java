package io.renren.service;


import io.renren.entity.ClassToTkLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-03-09 11:28:21
 */
public interface ClassToTkLogService {
	
	ClassToTkLogEntity queryObject(Long id);
	
	List<ClassToTkLogEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ClassToTkLogEntity classToTkLog);
	
	void update(ClassToTkLogEntity classToTkLog);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}
