package io.renren.service;

import io.renren.entity.SysLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 系统日志
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-29 09:57:44
 */
public interface SysLogService {
	
		
	SysLogEntity queryObject(Map<String, Object> map);
	
	List<SysLogEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysLogEntity sysLog);
	
	void update(SysLogEntity sysLog);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
		
		
}
