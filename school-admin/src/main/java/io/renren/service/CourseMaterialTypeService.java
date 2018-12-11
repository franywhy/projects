package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.CourseMaterialTypeEntity;
import io.renren.pojo.coursematerial.CourserMaterialPOJO;
import io.renren.pojo.coursematerial.CourserMaterialTypePOJO;

/**
 * 资料库类型
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 17:42:53
 */
public interface CourseMaterialTypeService {
	
		
	CourseMaterialTypeEntity queryObject(Map<String, Object> map);
	
	List<CourseMaterialTypeEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseMaterialTypeEntity courseMaterialType);
	
	void update(CourseMaterialTypeEntity courseMaterialType);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
	void deleteBatchNotIn(Map<String, Object> map);
	
	List<CourseMaterialTypeEntity> queryObject(Long number);
	
	//查询信息
	CourserMaterialTypePOJO queryPojoObject(Long number);
	
	boolean countMaterialType(long materialTypeId);
		
}
