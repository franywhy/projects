package io.renren.dao;

import io.renren.entity.MsgContentEntity;
import io.renren.pojo.MsgContentPOJO;

import java.util.List;
import java.util.Map;

/**
 * 消息表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-15 17:00:41
 */
public interface MsgContentDao extends BaseMDao<MsgContentEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	List<MsgContentPOJO> queryPojoList(Map<String, Object> map);

	MsgContentPOJO queryPojoObject(Map<String, Object> map);
	
	//判断是否有班型的引用
	int checkClassType(long id);
}
