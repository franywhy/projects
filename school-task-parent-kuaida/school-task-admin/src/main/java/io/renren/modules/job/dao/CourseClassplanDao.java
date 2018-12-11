package io.renren.modules.job.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import io.renren.modules.job.entity.CourseClassplanEntity;

/**
 * 排课计划表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-06 10:45:58
 */
public interface CourseClassplanDao extends BaseDao<CourseClassplanEntity> {

	/**
	 * 查询排课 根据排课ID
	 */
	CourseClassplanEntity queryObjectByClassplanId(@Param(value="classplanId")String classplanId);

	List<Map<String, Object>> queryListMap(Map<String, Object> map);

	Map<String, Object> queryOtherById(Map<String, Object> map);
	
	List<Map<String, Object>> querySsCourseClassplanMessage(String ts);
}
