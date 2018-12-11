package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.PushClassplanRemindEntity;
import io.renren.pojo.PushClassplanRemindPojo;

/**
 * 课次通知提醒表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-10-25 14:19:51
 */
public interface PushClassplanRemindService {
	
		
	PushClassplanRemindPojo queryObjectPojo(Map<String, Object> map);
	
	List<PushClassplanRemindPojo> queryListPojo(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(PushClassplanRemindEntity pushClassplanRemind);
	
	void update(PushClassplanRemindEntity pushClassplanRemind);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

}
