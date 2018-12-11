package io.renren.dao;

import io.renren.entity.MallBussinessOppEntity;
import java.util.Map;

/**
 * 商机记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-28 15:14:18
 */
public interface MallBussinessOppDao extends BaseMDao<MallBussinessOppEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
