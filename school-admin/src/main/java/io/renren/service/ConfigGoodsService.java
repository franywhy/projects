package io.renren.service;

import io.renren.entity.ConfigGoodsEntity;

import java.util.List;
import java.util.Map;

/**
 * 消息队列配置商品表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-12-22 10:55:29
 */
public interface ConfigGoodsService {
	
		
	ConfigGoodsEntity queryObject(Map<String, Object> map);
	
	List<ConfigGoodsEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ConfigGoodsEntity configGoods);
	
	void update(ConfigGoodsEntity configGoods);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	int queryNumByGoodId(String nc_commodity_id);
	
		
		
}
