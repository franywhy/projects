package com.hq.learningcenter.school.service;

import com.hq.learningcenter.school.pojo.CourseAbnormalAbandonExamPOJO;
import com.hq.learningcenter.school.pojo.MallExamSchedulePOJO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 弃考档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-27 14:37:18
 */
public interface CourseAbnormalAbandonExamService {


	List<MallExamSchedulePOJO> queryScheduleDateList(Map<String,Object> map);

	CourseAbnormalAbandonExamPOJO queryObject(Long id);
	
	List<CourseAbnormalAbandonExamPOJO> queryPOJOList(Map<String, Object> map);
	
	void save(Long userId,Long orderId,String abnormalReason,Long registrationId);

	/**
	 * 验证是否存在弃考或报考
	 * @param orderId 订单
	 * @param courseId 课程
	 * @param bkAreaId 省份
	 * @param scheduleId 时间
	 * @return
	 */
	String verifyStatus(String orderId, Long courseId,Long bkAreaId,Long scheduleId);

	void updateCancel(Integer auditStatus, Long id, Long userId, Date date);
}
