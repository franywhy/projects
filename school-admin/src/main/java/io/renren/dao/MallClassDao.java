package io.renren.dao;

import io.renren.entity.MallClassEntity;
import io.renren.pojo.Class.ClassPOJO;

import java.util.List;
import java.util.Map;

/**
 * 班级档案表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 10:24:52
 */
public interface MallClassDao extends BaseDao<MallClassEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	List<MallClassEntity> findClassList();
	
	MallClassEntity queryClassId(Map<String,Object> map);

	/**
	 * 查询列表数据
	 * @param map
	 * @return
	 */
	List<ClassPOJO> queryPojoList(Map<String, Object> map);

	/**
	 * 查询信息
	 * @param map
	 * @return
	 */
	ClassPOJO queryPojoObject(Map<String, Object> map);
	/**
	 * 取消原来的默认班级
	 * @param map
	 * @param schoolId		平台ID
	 * @param professionId	专业ID
	 * @param areaId		省份ID
	 * @param levelId		层次ID
	 * @return
	 */
	int removeOtherDefaultClass(Map<String, Object> map);
	/**
	 * 指定默认班级
	 * @param map
	 * @param schoolId		平台ID
	 * @param classId		班级ID
	 * @return
	 */
	int updateDefaultClass(Map<String, Object> map);
	
	int checkExist(Map<String,Object> map);

	List<MallClassEntity> queryClassList(Map<String, Object> classMap);

	int removeOtherDefaultClassNotZK(Map<String, Object> map);

	int updateDefaultClassNotZK(Map<String, Object> map);
}
