package io.renren.service;

import io.renren.entity.CourseExamScheduleEntity;
import io.renren.pojo.CourseExamSchedulePOJO;

import java.util.List;
import java.util.Map;

/**
 * 课程考试时段档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-24 18:55:01
 */
public interface CourseExamScheduleService {
	
		
	CourseExamScheduleEntity queryObject(Map<String, Object> map);
	
	List<CourseExamScheduleEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseExamScheduleEntity courseExamSchedule);
	
	void update(CourseExamScheduleEntity courseExamSchedule);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	/**
	 * 查询列表
	 * @param map
	 * @return
	 */
	List<CourseExamSchedulePOJO> queryPojoList(Map<String, Object> map);

	/**
	 * 根据id查询信息
	 * @param map
	 * @return
	 */
	CourseExamSchedulePOJO queryPojoObject(Map<String, Object> map);
	
		
		
}
