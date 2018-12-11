package com.school.service;

import com.school.entity.CourseAbnormalFreeAssessmentEntity;
import com.school.pojo.CourseAbnormalFreeAssessmentPOJO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 免考记录单
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-19 09:26:43
 */
public interface CourseAbnormalFreeAssessmentService {
	/**
	 * 查询免考列表
	 * @param map
	 * @return
	 */
	List<CourseAbnormalFreeAssessmentPOJO> queryList(Map<String, Object> map);

	/**
	 * 添加免考单
	 * @param userId 用户id
	 * @param orderId 订单pk
	 * @param courseId 课程pk
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param abnormalReason 原因
	 */
    void save(Long userId,Long orderId,Long courseId,String startTime,String endTime,String abnormalReason);

	/**
	 * 作废
	 * @param auditStatus 状态值
	 * @param id pk
	 * @param userId 用户pk
	 * @param date 日期
	 */
	void updateCancel(Integer auditStatus, Long id, Long userId, Date date);

	/**
	 * 获取课程列表
	 * @param orderId 订单pk
	 * @return
	 */
	List<Map<String , Object>> queryCourseList(Long orderId);

	/**
	 * 校验同时间是否已存在该课程
	 * @param orderId 订单pk
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param courseId 课程pk
	 * @return
	 */
	String  verifyStatus(Long orderId, Date startTime, Date endTime,Long courseId);
}
