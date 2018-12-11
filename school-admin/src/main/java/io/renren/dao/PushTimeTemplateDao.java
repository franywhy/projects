package io.renren.dao;

import io.renren.entity.PushTimeTemplateEntity;
import java.util.Map;

/**
 * 推送内容模板表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-10-25 11:31:58
 */
public interface PushTimeTemplateDao extends BaseMDao<PushTimeTemplateEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
