package io.renren.dao;

import io.renren.entity.CoursesEntity;
import io.renren.pojo.SelectionItem;
import io.renren.pojo.course.CoursesPOJO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 课程档案
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-22 14:06:37
 */
public interface CoursesDao extends BaseDao<CoursesEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	List<SelectionItem> querySelectionList(String schoolId);

	/**
	 * @param map[schoolId]=平台ID
	 * @param map[courseName]=课程名称
	 * @return  课程集合
	 */
	List<Map<String, Object>> queryListGrid(Map<String, Object> map);
	
	List<CoursesPOJO> queryPOJOList(Map<String, Object> map);

	int queryListGridTotal(Map<String, Object> map);
	
	int delete(Map<String, Object> map);
	
	long getCourseIdBycourseNo(Map<String, Object> map);
	
	int countCourseIdBycourseNo(Map<String, Object> map);
	/**
	 * 同步服务-根据mongodbId查询课程
	 * @param map
	 * @param mid	mongodbid
	 * @return
	 */
	int syncQueryByMid(Map<String, Object> map);
	
	/**
	 * 同步服务-新增课程
	 * @param coursesEntity
	 */
	void syncSave(CoursesEntity coursesEntity);
	
	/**
	 * 根据CourseIdID查询CoursesEntity
	 */
	CoursesEntity queryObjectbyCourseId(Map<String, Object> map);

	List<Map<String,Object>> queryMapByCourseId(@Param("courseId") Long courseId);
}
