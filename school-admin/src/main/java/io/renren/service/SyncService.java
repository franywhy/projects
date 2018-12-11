package io.renren.service;

import java.util.List;

import io.renren.entity.CourseLiveDetailsEntity;
import io.renren.entity.CoursesEntity;

/**
 * 同步课程和课时
 * @class io.renren.service.SysSyncCourseTimeService.java
 * @Description:
 * @author shihongjie
 * @dete 2017年6月7日
 */
public interface SyncService {
	
	/**
	 * 同步自考1.0出勤数据
	 * @param request
	 * @return
	 */
	public void syncKSLogMongodb();
	
	/**
	 * 同步课程和课时
	 * @param coursesEntity	课程
	 * @param courseLiveDetailsEntity 课时
	 */
	public void syncSaveCourse(CoursesEntity coursesEntity , List<CourseLiveDetailsEntity> courseLiveDetailsEntitys);
	
	/**
	 * 同步所有课程
	 * @param schoolId
	 */
	public void syncCourse(String schoolId);
	
	/**
	 * 同步所有商品
	 */
	public void syncCommodity(String schoolId);
	
	/**
	 * 同步排课
	 * @param schoolId
	 */
	public void syncClassplan(String schoolId);
	
	/**
	 * 同步学习计划
	 * @param schoolId
	 */
	public void syncCourseUserPlanClass(String schoolId);
	
	/**
	 * 同步授课老师
	 * @param schoolId
	 * @return
	 */
	public String syncTeacher(String schoolId);
	
	/**
	 * 同步所有直播间
	 */
	public void syncliveRoom(String schoolId);
	
	/**
	 * 批量生成学员规划-订单未生成学员规划的
	 * @param schoolId	平台ID
	 */
	void saveUserplanBatch(String schoolId);
	/**
	 * 同步学员
	 * @param schoolId	平台ID
	 */
	void syncUsers(String schoolId);
	
	/**
	 * 更新排课信息
	 * @param schoolId	平台ID
	 */
	void updateCourseClassPlanLives();
	
	/**
	 * 同步直播信息
	 * @param schoolId	平台ID
	 */
	void syncLiveLog(Integer type,String schoolId);
	
	/**
	 * 更新排课明细回放
	 * @param schoolId	平台ID
	 */
	void UpdatecourseClassplanLives();

	/*
	 * 同步客服
	 */
	public void syncAgent();

	/*
	 * 同步客户
	 */
	public void syncCustomers(Integer startOrderId, Integer endOrderId, String teacherMobile, String orderNos);
}
