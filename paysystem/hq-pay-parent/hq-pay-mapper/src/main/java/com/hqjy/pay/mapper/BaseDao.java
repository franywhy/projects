package com.hqjy.pay.mapper;

import java.util.List;
import java.util.Map;

import com.hqjy.pay.PayOrderEntity;




/**
 * 基础Dao(还需在XML文件里，有对应的SQL语句)
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:31:36
 */
public interface BaseDao<T> {
	
	void save(T t);
	
	void save(Map<String, Object> map);
	
	int update(T t);
	
	int update(Map<String, Object> map);

	T queryObject(Map<String, Object> map);
	int queryTotal(String tradeNo);
	/**
	 * 判断订单是否支付成功
	 * @param payOrder
	 * @return
	 */
	PayOrderEntity judgeOrderPaySucceed(PayOrderEntity payOrder);	
	/**
	 * 判断订单是否存在
	 */
	PayOrderEntity queryOrderNo(Map<String,Object> map);
	
	void updatePayOrderNo(PayOrderEntity payOrder); 
}
