package io.renren.dao;

import io.renren.entity.RelatedCommodityDetailEntity;
import java.util.Map;

/**
 * 关联商品档案子表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-29 10:35:56
 */
public interface RelatedCommodityDetailDao extends BaseDao<RelatedCommodityDetailEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	public void deleteBatchNotIn(Map<String, Object> map);
}
