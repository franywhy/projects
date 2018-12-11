package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeCheckingMode
import groovy.transform.TypeChecked;

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.HashMap;
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
import com.izhubo.rest.persistent.KGS;
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder
import com.mysqldb.model.Discount

/**
 * 优惠券管理
 * @author zhengxin
 * 2016-05-11
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class DiscountController extends BaseController {

	@Resource
	KGS discountKGS;
	@Resource
	private SessionFactory sessionFactory;

	private static final String TAG = "discount";
	
	static final String DFMT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		//翻页查询
		int size = ServletRequestUtils.getIntParameter(req, "size", 20);
		int page = ServletRequestUtils.getIntParameter(req, "page", 1);
		
		String name = req["name"];
		String code = req["code"];
		String limit_number = req["limit_number"];
		String type = req["type"];
		String time_type = req["time_type"];
		String work_type = req["work_type"];
		String is_stop = req["is_stop"];
		
		Criteria criterion = sessionFactory.getCurrentSession().createCriteria(Discount.class)
		if (StringUtils.isNotBlank(name)) {
			criterion.add(Restrictions.ilike(Discount.PROP_NAME, "%"+name+"%"));
		}
		if (StringUtils.isNotBlank(code)) {
			criterion.add(Restrictions.eq(Discount.PROP_CODE, Integer.valueOf(code)));
		}
		if (StringUtils.isNotBlank(limit_number)) {
			criterion.add(Restrictions.eq(Discount.PROP_LIMITNUMBER, Integer.valueOf(limit_number)));
		}
		if (StringUtils.isNotBlank(type)) {
			criterion.add(Restrictions.eq(Discount.PROP_TYPE, Integer.valueOf(type)));
		}
		if (StringUtils.isNotBlank(time_type)) {
			criterion.add(Restrictions.eq(Discount.PROP_TIMETYPE, Integer.valueOf(time_type)));
		}
		if (StringUtils.isNotBlank(work_type)) {
			criterion.add(Restrictions.eq(Discount.PROP_WORKTYPE, Integer.valueOf(work_type)));
		}
		if (StringUtils.isNotBlank(is_stop)) {
			criterion.add(Restrictions.eq(Discount.PROP_ISSTOP, Integer.valueOf(is_stop)));
		}
		
		def discountList = criterion.addOrder(Order.desc(Discount.PROP_CREATETIME)).addOrder(Order.desc(Discount.PROP_ID)).setFirstResult((page - 1) * size).setMaxResults(size).list();
		
		return getResultOKS(discountList);
	}
	
	/**
	 * 新增
	 */
	def add(HttpServletRequest req){
		
		Transaction  transaction  = sessionFactory.getCurrentSession().beginTransaction();
		
		Map user = (Map) req.getSession().getAttribute("user");
		String name = req["name"];
		int code =  discountKGS.nextId()
		Double money = 0.0;
		if (StringUtils.isNotBlank(req["money"])) {
			money = Double.valueOf(req["money"]);
		}
		Integer limit_number = null;
		if (StringUtils.isNotBlank(req["limitNumber"])) {
			limit_number = Integer.valueOf(req["limitNumber"]);
		}
		Integer day_max_num = null;
		if (StringUtils.isNotBlank(req["dayMaxNum"])) {
			day_max_num = Integer.valueOf(req["dayMaxNum"]);
		}
		Integer time_type = null;
		if (StringUtils.isNotBlank(req["timeType"])) {
			time_type = Integer.valueOf(req["timeType"]);
		}
		String starttime = req["startTime"];
		String endtime = req["endTime"];
		Timestamp start_time = null;
		if (StringUtils.isNotBlank(starttime)) {
			start_time = Timestamp.valueOf(starttime);
		}
		Timestamp end_time = null;
		if (StringUtils.isNotBlank(starttime)) {
			end_time = Timestamp.valueOf(endtime);
		}
		Integer valid_days = null;
		if (StringUtils.isNotBlank(req["validDays"])) {
			valid_days = Integer.valueOf(req["validDays"]);
		}
		Integer type = null;
		if (StringUtils.isNotBlank(req["type"])) {
			type = Integer.valueOf(req["type"]);
		}
		Integer work_type = null;
		if (StringUtils.isNotBlank(req["workType"])) {
			work_type = Integer.valueOf(req["workType"]);
		}
		Double enough_money = 0.0;
		if (StringUtils.isNotBlank(req["enoughMoney"])) {
			enough_money = Double.valueOf(req["enoughMoney"]);
		}
		
		Discount discount = new Discount();
		discount.setName(name);
		discount.setCode(code);
		discount.setMoney(money);
		discount.setLimitNumber(limit_number);
		discount.setDayMaxNum(day_max_num);
		discount.setTimeType(time_type);
		discount.setStartTime(start_time);
		discount.setEndTime(end_time);
		discount.setValidDays(valid_days);
		discount.setType(type);
		discount.setWorkType(work_type);
		discount.setEnoughMoney(enough_money);
		discount.setCreateUserId(user.get("_id") as Integer);
		discount.setCreateTime(new Timestamp(System.currentTimeMillis()));
		
		sessionFactory.getCurrentSession().save(discount);
		transaction.commit(); // 提交事务
		sessionFactory.getCurrentSession().flush();
		return OK();
	}

	/**
	 * 修改
	 */
	def edit(HttpServletRequest req){
		Session session = sessionFactory.getCurrentSession();
		Transaction  transaction  = session.beginTransaction();
		
		int id =Integer.valueOf( req.getParameter("id"));
		Map user = (Map) req.getSession().getAttribute("user");
		String name = req["name"];
		int code =  discountKGS.nextId()
		Double money = 0.0;
		if (StringUtils.isNotBlank(req["money"])) {
			money = Double.valueOf(req["money"]);
		}
		Integer limit_number = null;
		if (StringUtils.isNotBlank(req["limitNumber"])) {
			limit_number = Integer.valueOf(req["limitNumber"]);
		}
		Integer day_max_num = null;
		if (StringUtils.isNotBlank(req["dayMaxNum"])) {
			day_max_num = Integer.valueOf(req["dayMaxNum"]);
		}
		Integer time_type = null;
		if (StringUtils.isNotBlank(req["timeType"])) {
			time_type = Integer.valueOf(req["timeType"]);
		}
		String starttime = req["startTime"];
		String endtime = req["endTime"];
		Timestamp start_time = null;
		if (StringUtils.isNotBlank(starttime)) {
			start_time = Timestamp.valueOf(starttime);
		}
		Timestamp end_time = null;
		if (StringUtils.isNotBlank(starttime)) {
			end_time = Timestamp.valueOf(endtime);
		}
		Integer valid_days = null;
		if (StringUtils.isNotBlank(req["validDays"])) {
			valid_days = Integer.valueOf(req["validDays"]);
		}
		Integer type = null;
		if (StringUtils.isNotBlank(req["type"])) {
			type = Integer.valueOf(req["type"]);
		}
		Integer work_type = null;
		if (StringUtils.isNotBlank(req["workType"])) {
			work_type = Integer.valueOf(req["workType"]);
		}
		Double enough_money = 0.0;
		if (StringUtils.isNotBlank(req["enoughMoney"])) {
			enough_money = Double.valueOf(req["enoughMoney"]);
		}
		
		Discount discount = (Discount) session.get(Discount.class, id);
		discount.setName(name);
		discount.setCode(code);
		discount.setMoney(money);
		discount.setLimitNumber(limit_number);
		discount.setDayMaxNum(day_max_num);
		discount.setTimeType(time_type);
		discount.setStartTime(start_time);
		discount.setEndTime(end_time);
		discount.setValidDays(valid_days);
		discount.setType(type);
		discount.setWorkType(work_type);
		discount.setEnoughMoney(enough_money);
		discount.setUpdateUserId(user.get("_id") as Integer);
		discount.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		session.update(discount);
		transaction.commit(); // 提交事务
		session.flush();
		return OK();
	}

	/**
	 * 删除
	 */
	def del(HttpServletRequest req){
		Integer id = req["_id"] as Integer
		Session session = sessionFactory.getCurrentSession();
		Discount discount = (Discount) session.get(Discount.class, id);
		Transaction  transaction  = session.beginTransaction();
		session.delete(discount);
		transaction.commit();
		session.flush();
		return OK();
	}
	
	/**
	 * 停用/恢复
	 */
	def stop(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		Integer id = req["id"] as Integer
		Integer status = null;
		if (StringUtils.isNotBlank(req["status"])) {
			status = Integer.valueOf(req["status"]);
		}
		if(status == 0){
			status = 1
		}else{
			status = 0
		}
		Session session = sessionFactory.getCurrentSession();
		Discount discount = (Discount) session.get(Discount.class, id);
		Transaction  transaction  = session.beginTransaction();
		discount.setIsStop(status);
		discount.setUpdateUserId(user.get("_id") as Integer);
		discount.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		session.update(discount);
		transaction.commit();
		session.flush();
		return OK();
	}
}
