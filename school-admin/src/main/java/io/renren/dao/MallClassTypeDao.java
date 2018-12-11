package io.renren.dao;

import io.renren.entity.MallClassTypeEntity;
import io.renren.pojo.classType.ClassTypePOJO;

import java.util.List;
import java.util.Map;

/**
 * 班型档案表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-21 14:56:53
 */
public interface MallClassTypeDao extends BaseDao<MallClassTypeEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	void deleteClassType(Map<String, Object> map);

	List<Map<String, Object>> querySelectList(Map<String, Object> map);

	/**
	 * 查询列表
	 * @param map
	 * @return
	 */
	List<ClassTypePOJO> queryPojoList(Map<String, Object> map);

	/**
	 * 查询信息
	 * @param classtypeId
	 * @return
	 */
	ClassTypePOJO queryPojoObject(Map<String, Object> map);
}
