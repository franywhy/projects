package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeCheckingMode
import groovy.transform.TypeChecked;

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest

import org.springframework.web.bind.ServletRequestUtils
import org.apache.commons.lang3.StringUtils
import org.hibernate.Criteria
import org.hibernate.Session
import org.hibernate.Transaction
import org.hibernate.criterion.Order
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.hibernate.SessionFactory;

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.common.util.Pager;
import com.izhubo.rest.common.util.WebUtils;
import com.izhubo.rest.persistent.KGS;
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder
import com.mysqldb.model.Orders

/**
 * 订单列表
 * @author zhengxin
 * 2016-05-13
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class OrdersController extends BaseController {

	@Resource
	private SessionFactory sessionFactory;

	private static final String TAG = "orders";
	
	static final String DFMT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		//翻页查询
		int size = ServletRequestUtils.getIntParameter(req, "size", 20);
		int page = ServletRequestUtils.getIntParameter(req, "page", 1);
		
		String user_id = req["user_id"];
		String orderno = req["orderno"];
		String order_name = req["order_name"];
		String status = req["status"];
		String pay_on_line_type = req["pay_on_line_type"];
		String alipay_trade_no = req["alipay_trade_no"];
		String pay_type = req["pay_type"];
		String pay_status = req["pay_status"];
		String preference_scheme = req["preference_scheme"];
		String nc_school_id = req["nc_school_id"];
		String dr = req["dr"];
		//单据创建日期
		Date spay_time = Web.getTime(req, "spay_time");
		Date epay_time = Web.getTime(req, "epay_time");
		//支付日期
		Date screate_time = Web.getTime(req, "screate_time");
		Date ecreate_time = Web.getTime(req, "ecreate_time");
		
		Criteria criterion = sessionFactory.getCurrentSession().createCriteria(Orders.class)
		if (StringUtils.isNotBlank(user_id)) {
			criterion.add(Restrictions.eq(Orders.PROP_USERID, Integer.valueOf(user_id)));
		}
		if (StringUtils.isNotBlank(orderno)) {
			criterion.add(Restrictions.ilike(Orders.PROP_ORDERNO, "%"+orderno+"%"));
		}
		if (StringUtils.isNotBlank(order_name)) {
			criterion.add(Restrictions.ilike(Orders.PROP_ORDERNAME, "%"+order_name+"%"));
		}
		if (StringUtils.isNotBlank(status)) {
			criterion.add(Restrictions.eq(Orders.PROP_STATUS, Integer.valueOf(status)));
		}
		if (StringUtils.isNotBlank(pay_on_line_type)) {
			criterion.add(Restrictions.eq(Orders.PROP_PAYONLINETYPE, Integer.valueOf(pay_on_line_type)));
		}
		if (StringUtils.isNotBlank(alipay_trade_no)) {
			criterion.add(Restrictions.ilike(Orders.PROP_ALIPAYTRADENO, "%"+alipay_trade_no+"%"));
		}
		if (StringUtils.isNotBlank(pay_type)) {
			criterion.add(Restrictions.eq(Orders.PROP_PAYTYPE, Integer.valueOf(pay_type)));
		}
		if (StringUtils.isNotBlank(pay_status)) {
			criterion.add(Restrictions.eq(Orders.PROP_PAYSTATUS, Integer.valueOf(pay_status)));
		}
		if (StringUtils.isNotBlank(preference_scheme)) {
			criterion.add(Restrictions.eq(Orders.PROP_PREFERENCESCHEME, Integer.valueOf(preference_scheme)));
		}
		if (StringUtils.isNotBlank(nc_school_id)) {
			criterion.add(Restrictions.ilike(Orders.PROP_NCSCHOOLID, "%"+nc_school_id+"%"));
		}
		if (StringUtils.isNotBlank(dr)) {
			criterion.add(Restrictions.eq(Orders.PROP_DR, Integer.valueOf(dr)));
		}
		if(StringUtils.isNotBlank(spay_time)){
			criterion.add(Restrictions.ge(Orders.PROP_PAYTIME,new Timestamp(spay_time.getTime())));
		}
		if(StringUtils.isNotBlank(epay_time)){
			criterion.add(Restrictions.le(Orders.PROP_PAYTIME,new Timestamp(epay_time.getTime())));
		}
		if(StringUtils.isNotBlank(screate_time)){
			criterion.add(Restrictions.ge(Orders.PROP_CREATETIME,new Timestamp(screate_time.getTime())));
		}
		if(StringUtils.isNotBlank(ecreate_time)){
			criterion.add(Restrictions.le(Orders.PROP_CREATETIME,new Timestamp(ecreate_time.getTime())));
		}
		
		def ordersListCount = criterion.addOrder(Order.desc(Orders.PROP_CREATETIME)).addOrder(Order.desc(Orders.PROP_ID)).list().size();
		def ordersList = criterion.setFirstResult((page - 1) * size).setMaxResults(size).list();

		Pager pager = new Pager(ordersList, ordersListCount, page, size);
		return WebUtils.normalOutPager(pager);
		
		//return getResultOKS(ordersList);
	}
	
}
