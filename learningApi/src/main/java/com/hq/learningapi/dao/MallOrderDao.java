package com.hq.learningapi.dao;

import com.hq.learningapi.entity.MallOrderEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 09:58:34
 */
public interface MallOrderDao {
	
	List<MallOrderEntity> queryOrderByUserId(@Param("userId") Long userId,
											 @Param("businessId") String businessId);

	String queryOrderName(@Param("orderId") Long orderId);

	MallOrderEntity queryOrder(@Param("orderId") Long orderId,
                               @Param("businessId") String businessId);
}
