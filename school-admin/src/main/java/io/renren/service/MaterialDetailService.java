package io.renren.service;

import io.renren.entity.MaterialDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 11:16:41
 */
public interface MaterialDetailService {
	
		
	MaterialDetailEntity queryObject(Map<String, Object> map);
	
	MaterialDetailEntity queryObjectIsRe(Map<String, Object> map);
	
	List<MaterialDetailEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	int queryYesTotal(Map<String, Object> map);
	
	int queryNoTotal(Map<String, Object> map);
	
	void save(MaterialDetailEntity materialDetail);
	
	void update(MaterialDetailEntity materialDetail);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	

	List<MaterialDetailEntity> queryListForLay(Map<String, Object> map);
	
	int queryTotalForLay(Map<String, Object> map);
	

	List<MaterialDetailEntity> queryDetailEntities(List<MaterialDetailEntity> materialDetails);
	
	List<MaterialDetailEntity> queryMaterialDetailYesList(Map<String, Object> map);	
	
	List<MaterialDetailEntity> queryMaterialDetailNoList(Map<String, Object> map);
	
	int judgeIds(Map<String, Object> map);
	
	List<Integer> queryMaterialDetailYesIds(Map<String, Object> map);

}
