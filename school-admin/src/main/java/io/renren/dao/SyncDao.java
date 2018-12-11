package io.renren.dao;

import java.util.List;
import java.util.Map;

import io.renren.entity.CourseClassplanLivesEntity;
import io.renren.entity.CourseUserplanClassDetailEntity;
import io.renren.entity.CourseUserplanClassEntity;
import io.renren.entity.CoursesEntity;
import io.renren.pojo.classplan.ClassplanPOJO;

/**
 * 数据同步
 */
public interface SyncDao extends BaseMDao<CoursesEntity> {

	/**
	 * 同步服务-根据mongodbId查询课程
	 * @param map
	 * @param mid	mongodbid
	 * @return
	 */
	int syncQueryCourseByMid(Map<String, Object> map);
	
	/**
	 * 同步服务-新增排课
	 * @param coursesEntity
	 */
	void sysnCourseClassplanSave(ClassplanPOJO classplanPOJO);
	
	/**
	 * 同步服务-新增排课子表
	 * @param coursesEntity
	 */
	void sysnCourseClassplanLivesSave(CourseClassplanLivesEntity classplanLivePOJO);
	
	/**
	 * 同步服务-新增课程
	 * @param coursesEntity
	 */
	void syncCourseSave(CoursesEntity coursesEntity);
	
	/**
	 * 同步服务-基础课时
	 * @param courseLiveDetailsEntity
	 */
	void syncCourseLiveDetailSave(io.renren.entity.CourseLiveDetailsEntity courseLiveDetailsEntity);
	
	void syncTeacherSave();
	
	/**
	 * 同步服务-新增学习计划
	 * @param coursesEntity
	 */
	void courseUserplanClassSave(CourseUserplanClassEntity courseUserplanClass);
	/**
	 * 同步服务-新增学习计划子表
	 * @param coursesEntity
	 */
	void courseUserplanClassDetailSave(CourseUserplanClassDetailEntity courseUserplanClassDetail);
	/**
	 * 查询所有不为空back_url字段
	 * @return
	 */
	List<String> queryBackUrlInfo();
	/**
	 *  修改排课信息
	 * @param entity
	 */
	void updateCourseClassPlanLives(CourseClassplanLivesEntity entity);
     
	/**
	 * 同步直播信息
	 * @return
	 */
	List<String> syncLiveLog();
	
}
