package io.renren.dao;

import io.renren.entity.CourseMaterialDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 资料库资料
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-16 10:10:01
 */
public interface CourseMaterialDetailDao extends BaseMDao<CourseMaterialDetailEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	CourseMaterialDetailEntity queryObjectById(Map<String, Object> map)	;
	List<CourseMaterialDetailEntity> queryObjectByMaterialTypeId(Map<String, Object> map)	;
	
	int countMaterialDetail(long materialDetailId);
	
}
