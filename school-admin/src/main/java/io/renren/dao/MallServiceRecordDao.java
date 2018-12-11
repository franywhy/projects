package io.renren.dao;

import io.renren.entity.MallServiceRecordEntity;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-24 16:39:34
 */
public interface MallServiceRecordDao extends BaseDao<MallServiceRecordEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
