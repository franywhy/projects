package io.renren.service;

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
public interface MallClassTypeService {
	/**
	 * 下拉数据接口
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> querySelectList(Map<String, Object> map);
	
	MallClassTypeEntity queryObject(Long classtypeId);
	
	MallClassTypeEntity queryClassId(String classtypeName);
	
	List<MallClassTypeEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	
	void save(MallClassTypeEntity mallClassType);
	
	void update(MallClassTypeEntity mallClassType);
	
	void delete(Long classtypeId);
	
	void deleteClassType(Map<String, Object> map);
	
	void deleteBatch(Long[] classtypeIds);
	
	void pause(Long[] classtypeIds);
	
	void resume(Long[] classtypeIds);

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
