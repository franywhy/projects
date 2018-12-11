package io.renren.service;

import io.renren.entity.SysVersionEntity;

import java.util.List;
import java.util.Map;

/**
 * 移动端版本控制
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-27 16:31:29
 */
public interface SysVersionService {
	
		
	SysVersionEntity queryObject(Map<String, Object> map);
	
	List<SysVersionEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysVersionEntity sysVersion);
	
	void update(SysVersionEntity sysVersion);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
    //	判断某版本是否存在	
	boolean checkSysVersion(Map<String, Object> map);
}
