package io.renren.service;

import io.renren.entity.CoursesEntity;
import io.renren.pojo.SelectionItem;
import io.renren.pojo.course.CoursesPOJO;

import java.util.List;
import java.util.Map;

/**
 * 课程档案
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-22 14:06:37
 */
public interface CoursesService {
	
	CoursesEntity queryObject(Map<String , Object> map);
	
	CoursesEntity queryMid(Map<String , Object> map);
	
	List<CoursesEntity> queryList(Map<String, Object> map);
	
	List<CoursesPOJO> queryPOJOList(Map<String, Object> map);
	int queryTotal(Map<String, Object> map);
	
	void save(CoursesPOJO courses);
	
	void update(CoursesPOJO courses);
	
	void delete(Long courseId);
	
	void deleteBatch(Long[] courseIds);
	
	void pause(Long[] courseIds);
	
	void resume(Long[] courseIds);
	
	List<SelectionItem> querySelectionList(String schoolId);
    
	/**
	 * 查询课程--部分简易数据
	 * @param map[schoolId]=平台ID
	 * @param map[courseName]=课程名称
	 * @return  课程集合
	 */
	List<Map<String, Object>> queryListGrid(Map<String, Object> map);

	/**
	 * 查询课程--部分简易数据总数
	 * @param map
	 * @return
	 */
	int queryListGridTotal(Map<String, Object> map);
	public void delete(Map<String, Object> map);
	
	long getCourseIdBycourseNo(Map<String, Object> map);
	
	int countCourseIdBycourseNo(Map<String, Object> map);
	
	/**
	 * 根据课程ID获取课程
	 * @param map
	 * @return CoursesEntity
	 */
	CoursesEntity queryObjectbyCourseId(Map<String , Object> map);

	List<Map<String,Object>> queryMapByCourseId(Long courseId);
}
