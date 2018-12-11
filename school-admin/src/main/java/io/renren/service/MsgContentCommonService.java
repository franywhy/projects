package io.renren.service;

import io.renren.entity.MsgContentCommonEntity;
import io.renren.entity.UsersEntity;

import java.util.List;
import java.util.Map;

/**
 * 站内消息
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-16 17:41:49
 */
public interface MsgContentCommonService {
	
		
	MsgContentCommonEntity queryObject(Map<String, Object> map);
	
	List<MsgContentCommonEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MsgContentCommonEntity msgContentCommon);
	
	void update(MsgContentCommonEntity msgContentCommon);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
	/**
	 * 批量保存站内消息
	 * @param userIds	用户Id数组
	 * @param contentId	消息主键
	 * @param schoolId	平台ID
	 */
	void saveBatch(List<Long> userIds , Long contentId , String schoolId);

}
