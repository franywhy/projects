package io.renren.modules.job.dao;

import io.renren.modules.job.entity.CourseUserplanEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 学员规划
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 12:04:16
 */
public interface CourseUserplanDao{

	/**
	 * 查询 新的学员规划（会计产品线）
	 * @param ts
	 * @return
	 */
	List<Map<String,Object>> queryKJClassMessage(@Param(value = "ts") String ts);

	/**
	 * 通过商品id查询题刻课程编号
	 * @param object
	 * @return
	 */
	List<String> queryCodeListByCommodityId(@Param("goodId") Object object);

	/**
	 * 根据订单id查学员规划详情
	 * @param orderId 订单id
	 * @return
	 */
	CourseUserplanEntity queryUserplanObjectByOrderId(@Param("orderId")Long orderId);

	/*
	@Description:查询一定时间内变动的学员规划
	@Author:DL
	@Date:15:23 2018/6/20
	@params:
	 * @param null
	*/
    List<Map<String,Object>> queryClassMessageByProductId(String ts);
}
