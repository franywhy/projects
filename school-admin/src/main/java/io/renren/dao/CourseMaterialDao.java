package io.renren.dao;

import io.renren.entity.CourseMaterialEntity;
import io.renren.pojo.coursematerial.CourserMaterialPOJO;
import io.renren.pojo.timetable.TimeTablePOJO;

import java.util.List;
import java.util.Map;

/**
 * 资料库
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 17:42:38
 */
public interface CourseMaterialDao extends BaseMDao<CourseMaterialEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	List<Map<String, Object>> queryListMap(Map<String, Object> map);
	Map<String, Object> queryMap(Map<String, Object> map);
	public void save(CourserMaterialPOJO courseMaterial);
	/**
	 * 查询信息
	 * @param number
	 * @return
	 */
	CourserMaterialPOJO queryPojoObject(Long number);
}
