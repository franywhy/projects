package io.renren.service;

import io.renren.entity.RelatedCommodityEntity;
import io.renren.pojo.relatedCommodity.RelatedCommodityPOJO;

import java.util.List;
import java.util.Map;

/**
 * 商品关联档案表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-28 14:41:33
 */
public interface RelatedCommodityService {
	
	RelatedCommodityEntity queryObject(Long relatedCommodityId);
	
	List<RelatedCommodityEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(RelatedCommodityPOJO relatedCommodity);
	
	void update(RelatedCommodityPOJO relatedCommodity);
	
	void delete(Long relatedCommodityId);
	
	void deleteBatch(Long[] relatedCommodityIds);
	
	void pause(Long[] relatedCommodityIds);
	
	void resume(Long[] relatedCommodityIds);
	
}
