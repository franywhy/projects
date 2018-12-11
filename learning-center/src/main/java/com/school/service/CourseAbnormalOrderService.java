package com.school.service;

import com.school.entity.CourseAbnormalOrderEntity;
import com.school.pojo.CourseAbnormalOrderPOJO;
import com.school.pojo.OrderPOJO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 休学记录单
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-19 09:26:43
 */
public interface CourseAbnormalOrderService {
	/**
	 * 查询休学列表
	 * @param map
	 * @return
	 */
	List<CourseAbnormalOrderPOJO> queryList(Map<String, Object> map);

	/**
	 * 查询订单列表
	 * @param userId 用户pk
	 * @param businessId 业务线pk
	 * @return
	 */
	List<Map<String,Object>> queryOrderList(Long userId,String businessId);

	/**
	 * 新增休学单
	 * @param userId 用户pk
	 * @param orderId 订单pk
	 * @param startTime 预计开始时间
	 * @param expectEndTime 预计结束时间
	 * @param abnormalReason 原因
	 */
	void save(Long userId,Long orderId,String startTime,String expectEndTime,String abnormalReason);

	/**
	 * 作废
	 * @param auditStatus 状态值
	 * @param id pk
	 * @param userId 用户pk
	 * @param date 日期
	 */
	void updateCancel(Integer auditStatus,Long id,Long userId,Date date);

	/**
	 * 校验同时间范围内该订单是否存在休学单
	 * @param orderId 订单pk
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	String verifyStatus(Long orderId, Date startTime, Date endTime);
}
