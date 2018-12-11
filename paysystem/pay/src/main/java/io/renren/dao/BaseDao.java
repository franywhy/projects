package io.renren.dao;

import java.util.List;
import java.util.Map;

import io.renren.entity.PayOrderEntity;



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
	
	void saveBatch(List<T> list);
	
	int update(T t);
	
	int update(Map<String, Object> map);
	
	int delete(Object id);
	
	int delete(Map<String, Object> map);
	
	int deleteBatch(Object[] id);
	
	int deleteBatch(Map<String, Object> map);

	T queryObject(Object id);
	
	T queryMid(Object id);
	T queryClassId(String classTypeName);
	
	List<T> queryList(Map<String, Object> map);
	
	List<T> findAreaGoodsList(Map<String, Object> map);
	
	Integer queryAreaId(Map<String,Object> map);
	Integer queryAreaIdByZKAreaId(Map<String,Object> map);
	

	
	List<T> queryLayList(Map<String, Object> map);
	
	List<T> queryList(Object id);
	
	int queryTotal(Map<String, Object> map);

	Integer queryUserId(Map<String,Object> map);
	
	int queryTotal();
	String queryGenseeLiveNum(String liveId);
	/**
	 * 判断订单是否支付成功
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
	 * 判断订单是否存在
	 */
	int payOrderExist(String orderNo);
}
