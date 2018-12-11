package io.renren.service;

import io.renren.entity.PayOrderEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-06 18:29:01
 */
public interface PayOrderService {
	
		
	PayOrderEntity queryObject(Map<String, Object> map);
	
	List<PayOrderEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(PayOrderEntity payOrder);
	
	void update(PayOrderEntity payOrder);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	/**
	 * 判断该订单是否支付成功
	 * @param payOrder
	 * @return
	 */
	PayOrderEntity judgeOrderPaySucceed(PayOrderEntity payOrder);	
	
	/**
	 * 判断微信订单是否支付成功
	 * @param payOrder
	 * @return
	 */
	int judgeWeiXinOrderPaySucceed(PayOrderEntity payOrder);
	/**
	 *判断订单是否存在
	 */
	int payOrderExist(String orderNo);	
}
