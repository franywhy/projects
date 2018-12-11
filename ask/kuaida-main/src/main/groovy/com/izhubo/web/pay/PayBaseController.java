package com.izhubo.web.pay;

import java.util.UUID;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.izhubo.model.DR;
import com.izhubo.model.OrderStatus;
import com.izhubo.web.BaseController;
import com.mongodb.BasicDBObject;
import com.mysqldb.model.Orders;

/**
 * 生成支付的基类
 * @author Administrator
 *
 */
public abstract class PayBaseController extends BaseController {
	
	public static final String PAYHTML = "PAYHTML" ;
	
	@Resource
	private SessionFactory sessionFactory;
	/**
	 * 支付html生成方法
	 */
	public abstract Object factoryPayHtml(Orders order);
	/** 支付名称 */
	public abstract String payName();
	
	
	/** 生成支付html */
	public Object createPayHtml(Integer order_id){
		Object obj = null;
		Orders order = payOrder(order_id);
		if(order != null){
			obj = factoryPayHtml(order);
			savePayLogs(payName(), PAYHTML ,obj);
		}
		return obj;
	}
	
	/**
	 * 获取需要生成支付的订单url
	 * @param order_id
	 * @param session
	 * @return
	 */
	protected Orders payOrder(Integer order_id) {
		return (Orders) sessionFactory.getCurrentSession().createCriteria(Orders.class)
				.add(Restrictions.eq(Orders.PROP_ID, order_id))
				.add(Restrictions.eq(Orders.PROP_STATUS, OrderStatus.有效.ordinal())).uniqueResult();
	}
	
	

	/**
	 * 保存支付日志
	 * @param type 类型
	 * @param arrs 
	 */
	protected void savePayLogs(String type , Object... arrs){
		BasicDBObject bd = new BasicDBObject("_id" , UUID.randomUUID().toString());
		bd.append("timestamp" , System.currentTimeMillis());
		bd.append("type" , type);
		for(int i = 0 ; i < arrs.length ; i++){
			bd.append("P"+i, arrs[i]);
		}
		logMongo.getCollection("pay_logs").save(bd);
	}

}
