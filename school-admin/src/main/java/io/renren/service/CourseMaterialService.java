package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.CourseMaterialEntity;
import io.renren.pojo.coursematerial.CourserMaterialPOJO;

/**
 * 资料库
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 17:42:38
 */
public interface CourseMaterialService {
	
		
	CourseMaterialEntity queryObject(Map<String, Object> map);
	
	List<Map<String, Object>> queryListMap(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourserMaterialPOJO courseMaterial);
	
	void update(CourserMaterialPOJO courseMaterial);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
	//查询信息
    CourserMaterialPOJO queryPojoObject(Long number);
	
	int checkCourses(Map<String, Object> map);	
		
}
