package io.renren.dao;

import io.renren.entity.CourseMaterialTypeEntity;
import io.renren.pojo.coursematerial.CourserMaterialTypePOJO;

import java.util.List;
import java.util.Map;

/**
 * 资料库类型
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 17:42:53
 */
public interface CourseMaterialTypeDao extends BaseDao<CourseMaterialTypeEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	/**
	 * 删除ID不等于ids的数据
	 * @param ids = id数组
	 * @param relatedCommodityId = PK
	 */
	public void deleteBatchNotIn(Map<String, Object> map);
	
	//查询信息
		CourserMaterialTypePOJO queryPojoObject(Long number);
		
		int countMaterialType(long materialTypeId);
	
	
}
