package io.renren.dao;

import io.renren.entity.RelatedCommodityEntity;
import java.util.Map;

/**
 * 商品关联档案表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-28 14:41:33
 */
public interface RelatedCommodityDao extends BaseDao<RelatedCommodityEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
