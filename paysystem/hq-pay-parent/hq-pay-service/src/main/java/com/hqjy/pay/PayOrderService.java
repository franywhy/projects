package com.hqjy.pay;


import java.util.List;
import java.util.Map;

import com.hqjy.pay.PayOrderEntity;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-06 18:29:01
 */
public interface PayOrderService {

	PayOrderEntity queryObject(Map<String, Object> map);
	
	void save(PayOrderEntity payOrder);
	
	void update(PayOrderEntity payOrder);
	
	/**
	 * 判断该订单是否支付成功
	 * @param payOrder
	 * @return
	 */
	PayOrderEntity judgeOrderPaySucceed(PayOrderEntity payOrder);	
	
	/**
	 *判断订单号是否存在
	 */
	PayOrderEntity queryOrderNo(Map<String,Object> map);
	void updatePayOrderNo(PayOrderEntity payOrder); 
}
