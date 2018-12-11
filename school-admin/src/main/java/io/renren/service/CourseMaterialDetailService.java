package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.CourseMaterialDetailEntity;

/**
 * 资料库资料
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-16 10:10:01
 */
public interface CourseMaterialDetailService {
	
		
	CourseMaterialDetailEntity queryObject(Map<String, Object> map);
	
	List<Map<String, Object>> queryListMap(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseMaterialDetailEntity courseMaterialDetail);
	
	void update(CourseMaterialDetailEntity courseMaterialDetail);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
	CourseMaterialDetailEntity queryObjectById(Map<String, Object> map)	;
	
	List<CourseMaterialDetailEntity> queryObjectByMaterialTypeId(Map<String, Object> map)	;
	
	boolean countMaterialDetail(long materialDetailId);
		
}
