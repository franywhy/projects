package io.renren.service;

import io.renren.entity.PushClassplanDetailRemindEntity;

import java.util.List;
import java.util.Map;

/**
 * 课次通知提醒详情表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-10-25 14:21:14
 */
public interface PushClassplanDetailRemindService {
	
		
	PushClassplanDetailRemindEntity queryObject(Map<String, Object> map);
	
	List<PushClassplanDetailRemindEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(PushClassplanDetailRemindEntity pushClassplanDetailRemind);
	
	void update(PushClassplanDetailRemindEntity pushClassplanDetailRemind);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	/**
	 * 审核
	 * @param map
	 */
	void theAudit(Map<String, Object> map);

	/**
	 * 通过课次通知id删除
	 * @param map
	 */
	void deleteBatchByIds(Map<String, Object> map);

	/**
	 * 通过推送课次id获取推送课次详情
	 * @param pushClassplanRemindId
	 * @return
	 */
	List<PushClassplanDetailRemindEntity> getListByParentId(Integer pushClassplanRemindId);

	/**
	 * 刷新
	 * @param map
	 */
	void refresh(Map<String, Object> map);
	
		
		
}
