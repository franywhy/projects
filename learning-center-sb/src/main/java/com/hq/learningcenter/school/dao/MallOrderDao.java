package com.hq.learningcenter.school.dao;

import java.util.List;
import java.util.Map;

import com.hq.learningcenter.school.pojo.OrderPOJO;
import com.hq.learningcenter.school.entity.MallOrderEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 09:58:34
 */
public interface MallOrderDao{
	
	List<MallOrderEntity> queryOrderByUserId(@Param("userId") Long userId,
                                             @Param("businessId") String businessId);
	
	String queryOrderName(@Param("orderId") Long orderId);
	
	MallOrderEntity queryOrder(@Param("orderId") Long orderId,
							   @Param("businessId") String businessId);
	

	List<OrderPOJO> queryList(Map<String, Object> map);

    Integer queryTotalCount(Map<String, Object> map);
}
