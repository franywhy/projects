package io.renren.service;


import io.renren.entity.UserOldclassLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 记录学员转班前推送给题库的队列班级信息
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-02-26 14:37:05
 */
public interface UserOldclassLogService {
	
	UserOldclassLogEntity queryObject(Long id);
	
	List<UserOldclassLogEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(UserOldclassLogEntity userOldclassLog);
	
	void update(UserOldclassLogEntity userOldclassLog);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	//推送转班前信息到题库
    List<Map<String,Object>> queryMessage(String millisecond);
}
