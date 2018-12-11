package io.renren.service;

import io.renren.entity.MsgContentTopEntity;

import java.util.List;
import java.util.Map;

/**
 * 弹窗消息
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-19 14:12:25
 */
public interface MsgContentTopService {
	
		
	MsgContentTopEntity queryObject(Map<String, Object> map);
	
	List<MsgContentTopEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MsgContentTopEntity msgContentTop);
	
	void update(MsgContentTopEntity msgContentTop);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	/**
	 * 批量保存弹窗消息
	 * @param userIds 用户id数组
	 * @param contentId 消息主键
	 * @param schoolId 平台id
	 */
	void saveBatch(List<Long> userIds, Long contentId, String schoolId);
	
		
		
}
