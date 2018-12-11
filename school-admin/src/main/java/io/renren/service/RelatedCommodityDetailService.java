package io.renren.service;

import io.renren.entity.RelatedCommodityDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 关联商品档案子表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-29 10:35:56
 */
public interface RelatedCommodityDetailService {
	
	List<RelatedCommodityDetailEntity> queryObject(Long id);
	
	int queryTotal(Map<String, Object> map);
	
	void save(RelatedCommodityDetailEntity relatedCommodityDetail);
	
	void update(RelatedCommodityDetailEntity relatedCommodityDetail);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
	
	void pause(Long[] ids);
	
	void resume(Long[] ids);

	/**
	 * 删除ID不等于ids的数据
	 * @param ids = id数组
	 * @param relatedCommodityId = PK
	 */
	void deleteBatchNotIn(Map<String, Object> map);
	
}
