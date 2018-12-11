package io.renren.service;

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
public interface MallClassService {
	
	MallClassEntity queryObject(Map<String, Object> map);
	
	List<MallClassEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MallClassEntity mallClass);
	
	void update(MallClassEntity mallClass);
	
	void delete(Long classId);
	
	void deleteBatch(Long[] classIds);
	
	void delete(Map<String, Object> map);
	
	void pause(Long[] classIds);
	
	void resume(Long[] classIds);

	List<MallClassEntity> findClassList();

	/**
	 * 查询列表数据
	 * @param map
	 * @return
	 */
	List<ClassPOJO> queryPojoList(Map<String, Object> map);
	
	MallClassEntity queryClassId(Map<String,Object> map);

	/**
	 * 查询信息
	 * @param map
	 * @return
	 */
	ClassPOJO queryPojoObject(Map<String, Object> map);
	
//	/**
//	 * 取消原来的默认班级
//	 * @param map
//	 * @param schoolId		平台ID
//	 * @param professionId	专业ID
//	 * @param areaId		省份ID
//	 * @param levelId		层次ID
//	 * @return
//	 */
//	int removeOtherDefaultClass(Map<String, Object> map);
	/**
	 * 指定默认班级
	 * @param map
	 * @param schoolId		平台ID
	 * @param classId		班级ID
	 * @return
	 */
	boolean updateDefaultClass(Map<String, Object> map);
//	校验班级是否存在
	boolean checkExist(Map<String, Object> map);

	List<MallClassEntity> queryClassList(Map<String, Object> classMap);

	boolean updateDefaultClassNotZK(Map<String, Object> map);
}
