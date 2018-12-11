package com.hq.learningcenter.school.dao;

import com.hq.learningcenter.school.pojo.CourseAbnormalOrderPOJO;
import org.apache.ibatis.annotations.Param;
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
public interface  CourseAbnormalOrderDao<CourseAbnormalOrderEntity> {
	/**
	 * 新增休学单
	 * @param courseAbnormalOrderEntity
	 */
	void save(CourseAbnormalOrderEntity courseAbnormalOrderEntity);

	/**
	 * 校验同时间范围内该订单是否存在休学单
	 * @param map
	 * @return
	 */
	CourseAbnormalOrderPOJO verifyStatus(Map<String, Object> map);
	/**
	 * 查询休学列表
	 * @param map
	 * @return
	 */
    List<CourseAbnormalOrderPOJO> queryPojoList(Map<String, Object> map);
	/**
	 * 作废
	 */
	int updateCancel(Map<String, Object> map);

	/**
	 * 查询订单列表
	 * @param userId 用户pk
	 * @param businessId 业务线pk
	 * @return
	 */
	List<Map<String,Object>> queryOrderPOJOList(@Param("userId") Long userId,
									   @Param("businessId") String businessId);

	/**
	 * 获取产品线
	 * @param orderId 订单pk
	 * @return
	 */
	Long queryProductId(@Param("orderId") Long orderId);
}
