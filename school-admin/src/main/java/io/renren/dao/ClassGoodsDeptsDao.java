package io.renren.dao;

import io.renren.entity.ClassGoodsDeptsEntity;
import io.renren.pojo.ClassGoodsDeptsPOJO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 班级-商品-部门对照表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-11-29 15:45:01
 */
public interface ClassGoodsDeptsDao extends BaseMDao<ClassGoodsDeptsEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	List<ClassGoodsDeptsPOJO> queryListPOJO(Map<String, Object> map);

	ClassGoodsDeptsPOJO queryObjectPOJO(Map<String, Object> map);

	int queryNumByGoodIdAndDeptId(@Param("classId")Long classId, @Param("goodId")Long goodId, @Param("deptId")Long deptId);

	List<ClassGoodsDeptsEntity> queryClassList(Map<String, Object> classMap);

	int queryNumBydeptIdAndGoodId(@Param("goodId")Long goodId, @Param("deptId")Long deptId);

	int deleteById(@Param("id")Long id);
}
