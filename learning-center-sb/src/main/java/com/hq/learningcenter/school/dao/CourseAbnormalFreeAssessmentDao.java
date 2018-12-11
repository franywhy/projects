package com.hq.learningcenter.school.dao;

import com.hq.learningcenter.school.pojo.CourseAbnormalFreeAssessmentPOJO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 休学失联记录单
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-19 09:26:43
 */
@Repository
public interface CourseAbnormalFreeAssessmentDao<CourseAbnormalFreeAssessmentEntity> {
	/**
	 * 查询免考列表
	 * @param map
	 * @return
	 */
    List<CourseAbnormalFreeAssessmentPOJO> queryPojoList(Map<String, Object> map);

	/**
	 * 添加免考单
	 * @param entity
	 */
	void save(CourseAbnormalFreeAssessmentEntity entity);

	/**
	 * 校验同时间范围是否存在该课程的免考单
	 * @param map
	 * @return
	 */
	CourseAbnormalFreeAssessmentPOJO verifyStatus(Map<String, Object> map);

	/**
	 * 作废
	 * @param map
	 * @return
	 */
	int updateCancel(Map<String, Object> map);

	/**
	 * 查询学员规划pk
	 * @param orderId 订单pk
	 * @return
	 */
	String queryUserPlanId(String orderId);

	/**
	 * 根据学员规划pk获取课程列表
	 * @param map
	 * @return
	 */
	List<Map<String , Object>> courseListByUserPlanId(Map<String, Object> map);
}
