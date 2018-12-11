package io.renren.dao;

import io.renren.entity.ConfigGoodsEntity;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 消息队列配置商品表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-12-22 10:55:29
 */
public interface ConfigGoodsDao extends BaseMDao<ConfigGoodsEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	int queryNumByGoodId(@Param("goodNcId")String nc_commodity_id);
}
