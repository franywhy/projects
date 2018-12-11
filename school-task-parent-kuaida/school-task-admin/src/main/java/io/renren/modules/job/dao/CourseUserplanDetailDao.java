package io.renren.modules.job.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 学员规划-课程子表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 12:04:39
 */
public interface CourseUserplanDetailDao{
	/**
	 * 展示互动-安卓端-用户观看记录-获取排课信息
	 * @param back_id
	 * @param startTime
	 * @return
	 */
	Map<String , Object> getCourseClassPlanInfoGSync(@Param("backId") String backId, @Param("startTime") Long startTime);
	/**
	 * 展示互动-安卓端-用户观看记录-获取用户ID
	 * @param classplan_id
	 * @param userName
	 * @return
	 */
	List<Long> getUserIdGSync(@Param("classplanId") String classplanId, @Param("userName") String userName);
}
