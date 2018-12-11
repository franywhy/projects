package io.renren.dao;

import io.renren.entity.PushClassplanRemindEntity;
import io.renren.pojo.PushClassplanRemindPojo;

import java.util.List;
import java.util.Map;

/**
 * 课次通知提醒表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-10-25 14:19:51
 */
public interface PushClassplanRemindDao extends BaseMDao<PushClassplanRemindEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	List<PushClassplanRemindPojo> queryListPojo(Map<String, Object> map);
	
	PushClassplanRemindPojo queryObjectPojo(Map<String, Object> map);

}
