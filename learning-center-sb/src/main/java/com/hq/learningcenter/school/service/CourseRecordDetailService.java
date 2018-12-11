package com.hq.learningcenter.school.service;

import java.util.List;
import java.util.Map;

import com.hq.learningcenter.school.pojo.CourseRecordDetailPOJO;
import com.hq.learningcenter.school.pojo.CoursesPOJO;

public interface CourseRecordDetailService {
	
	/**
	 * 根据商品id获取商品详情（课程）列表,仅限含有录播课的课程
	 * @param orderId 订单id
	 * @param productIdList 
	 * @param businessId 
	 * @param userId 
	 * @return
	 */
	List<CoursesPOJO> getRecordCourseList(Long orderId, List<Long> productIdList, String businessId);

	/**
	 * 根据商品id获取录播课页面头
	 * @param commodityId 商品id
	 * @return
	 */
	Map<String, Object> getRecordHear(Long userId);
	
	//获取产品线id
	List<Long> queryProductId(String businessId);

	//获取用户录播课程名称
    List<CoursesPOJO> getCourseRecord(Long orderId, List<Long> productIdList, String businessId);

    //获取用户录播课章节明细
    List<CourseRecordDetailPOJO> getCourseRecordDetailByCourseId(Long userId,Long courseId);
}
