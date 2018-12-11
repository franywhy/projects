package io.renren.service;

import io.renren.entity.ClassGoodsDeptsEntity;
import io.renren.pojo.ClassGoodsDeptsPOJO;

import java.util.List;
import java.util.Map;

/**
 * 班级-商品-部门对照表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-11-29 15:45:01
 */
public interface ClassGoodsDeptsService {
	
		
	ClassGoodsDeptsEntity queryObject(Map<String, Object> map);
	
	List<ClassGoodsDeptsEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ClassGoodsDeptsEntity classGoodsDepts);
	
	void update(ClassGoodsDeptsEntity classGoodsDepts);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	List<ClassGoodsDeptsPOJO> queryListPOJO(Map<String, Object> map);

	ClassGoodsDeptsPOJO queryObjectPOJO(Map<String, Object> map);

	int queryNumByGoodIdAndDeptId(Long classId, Long goodId, Long deptId);

	List<ClassGoodsDeptsEntity> queryClassList(Map<String, Object> classMap);

	int queryNumBydeptIdAndGoodId(Long goodId, Long deptId);

	void deleteById(Long id);
	
		
		
}
