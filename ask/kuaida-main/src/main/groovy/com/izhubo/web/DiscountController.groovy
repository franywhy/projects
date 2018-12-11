package com.izhubo.web

import static com.izhubo.rest.common.util.WebUtils.$$

import java.sql.Timestamp
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.hibernate.Query
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.criterion.Restrictions
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.model.Code
import com.izhubo.model.DiscountTimeType
import com.izhubo.model.DiscountUserIsUseType

import org.hibernate.criterion.Restrictions
import org.hibernate.criterion.Projections

import com.izhubo.rest.anno.Rest
import com.izhubo.utils.DataUtils;
import com.mongodb.DBCollection
import com.mysqldb.model.Discount
import com.mysqldb.model.DiscountUser
import com.rabbitmq.client.AMQP.Basic.Return;
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation

/**
 * 优惠券
 */
@Rest
@RequestMapping("/discount")
class DiscountController extends BaseController {
	
	@Resource
	private SessionFactory sessionFactory;
	
	/** 活动优惠券映射关系表 */
	public DBCollection discount_map() {
		return logMongo.getCollection("discount_map");
	}
	/** 优惠券日志 */
	public DBCollection discount_logs() {
		return logMongo.getCollection("discount_logs");
	}
	
	
	@ResponseBody
	@RequestMapping(value = "registerGetDiscount", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "注册后获取优惠券", httpMethod = "GET",  notes = "用户经过邀请注册获取优惠券")
	@ApiImplicitParams([
						@ApiImplicitParam(name = "phone", value = "注册手机号码", required = true, dataType = "string", paramType = "query")
						])
	def registerGetDiscount(HttpServletRequest request){
		String phone = request["phone"];
		return getSysDiscountFromFirstShare(phone);
	}
	
	/**
	 * 领取优惠券
	 * @param phone
	 * @param discount_id
	 */
	def getSysDiscountFromFirstShare(String phone){
		//分享优惠券id
		Integer discount_id = getSysDiscountId("A0001");
		return getDiscountNotDays(phone, discount_id);
	}
	
	@ResponseBody
	@RequestMapping(value = "learningCardGetDiscount", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "首页商机领取优惠券", httpMethod = "GET",  notes = "首页商机领取优惠券")
	@ApiImplicitParams([
						@ApiImplicitParam(name = "phone", value = "注册手机号码", required = true, dataType = "string", paramType = "query")
						])
	def learningCardGetDiscount(HttpServletRequest request)
	{
		Integer discount_id = getSysDiscountId("A0002");
		String phone = request["phone"];
		return getDiscountDaysLimit(phone,discount_id);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "GetlearningCardCount", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "首页商机领取优惠券剩余数量", httpMethod = "GET",  notes = "首页商机领取优惠券剩余数量")
	def GetlearningCardCount(HttpServletRequest request)
	{
	
		Session session = sessionFactory.getCurrentSession();
		Discount di = (Discount)session.createCriteria(Discount.class)
		.add(Restrictions.eq(Discount.PROP_ID , getSysDiscountId("A0002")))//优惠券id
		.add(Restrictions.eq(Discount.PROP_ISSTOP , 0))//是否停用 0.否 1.是
		.uniqueResult();
		
		
		Map map = new HashMap();
		map.put("code", Code.OKS);
		map.put("allcount", di.getDayMaxNum());
		map.put("remain", GetDiscountCountToday( getSysDiscountId("A0002")));
		map.put("msg", Code.OK_S);
		
		return map;
	}
	
	
	
	
	
	
	
	
	/**
	 * 领取用户优惠券 (没有每日领取数量限制)
	 * @param phone			电话号码
	 * @param discount_id	优惠券id
	 * @return				map[code: 70201 data:"优惠券已领取过"] ["code" : 1 ,"data" : "success"]
	 */
	private Map getDiscountNotDays(String phone , Integer discount_id){
		//参数校验
		if(StringUtils.isBlank(phone) || discount_id == null){
			return getResultParamsError();
		}
		//优惠券是否已领取校验
		if(isGetDiscount(phone, discount_id)){
			return Code.DISCOUNT.优惠券已领取过();
		}
		
		Session session = sessionFactory.getCurrentSession();
		//优惠券
		Discount di = (Discount)session.createCriteria(Discount.class)
							.add(Restrictions.eq(Discount.PROP_ID , discount_id))//优惠券id
							.add(Restrictions.eq(Discount.PROP_ISSTOP , 0))//是否停用 0.否 1.是
							.uniqueResult();
		
		if(di){
			//当前日期
			Timestamp now = new Timestamp(System.currentTimeMillis());
			
			//时间类型 0.固定日期 1.领取后生效的天数 
			Integer timeType = di.getTimeType();
			//优惠券数量
			Integer limit_number = di.getLimitNumber();
			//领取数量
			Integer get_num = di.getGetNum();
			
			//优惠券生效起始日期
			Timestamp discountStartTime = null;
			//优惠券生效结束日期
			Timestamp discountEndTime = null;
			
			//日期类型决定优惠券的有效期
			if(DiscountTimeType.固定日期.ordinal() == timeType ){
				discountStartTime = di.getStartTime();
				discountEndTime = di.getEndTime();
			}else if(DiscountTimeType.领取后生效的天数.ordinal() == timeType ){
				discountStartTime = now;
				discountEndTime = addDay(discountStartTime, di.getValidDays());
			}
			
			
			//用户领取优惠券记录
			DiscountUser ds = new DiscountUser();
			//优惠券code
			ds.setDiscountCode(di.getCode());
			//优惠券id
			ds.setDiscount(di);
			//领取优惠券用户手机号码
			ds.setPhone(phone);
			//优惠券领取日期
			ds.setGetTime(now);
			//优惠券默认未使用
			ds.setIsUse(DiscountUserIsUseType.未使用.ordinal());
			//优惠券使用日期
			ds.setUseTime(null);
			//领取优惠券的失效日期
			ds.setDiscountEndTime(discountEndTime)
			//领取优惠券的生效日期
			ds.setDiscountStartTime(discountStartTime);
			
			//优惠券领取数量加一
			if(di.getGetNum() == null)
			{
				di.setGetNum(1);
			}
			else
			{
	     		di.setGetNum(di.getGetNum()++);
			}
			
			//开启事务
			Transaction tran = session.beginTransaction();
			
			//更新优惠券表头-领取数量
			session.update(di);
			//新增优惠券表体-领取用户信息
			session.save(ds);
			
			//提交事务
			tran.commit();
			//刷新session缓存
			session.flush();
			
			discount_logs().save($$("_id" : UUID.randomUUID().toString() , "phone" : phone , "discount_id" : discount_id , "timestamp" : System.currentTimeMillis()));
		}
		return getResultOK();
	}
	
	
	/**
	 * 领取用户优惠券 （每日有数量限制的）
	 * @param phone			电话号码
	 * @param discount_id	优惠券id
	 * @return				map[code: 70201 data:"优惠券已领取过"] ["code" : 1 ,"data" : "success"]
	 */
	private Map getDiscountDaysLimit(String phone , Integer discount_id){
		//参数校验
		if(StringUtils.isBlank(phone) || discount_id == null){
			return getResultParamsError();
		}
		//优惠券是否已领取校验
		if(isGetDiscount(phone, discount_id)){
			return Code.DISCOUNT.优惠券已领取过();
		}
		
		Session session = sessionFactory.getCurrentSession();
		//优惠券
		Discount di = (Discount)session.createCriteria(Discount.class)
							.add(Restrictions.eq(Discount.PROP_ID , discount_id))//优惠券id
							.add(Restrictions.eq(Discount.PROP_ISSTOP , 0))//是否停用 0.否 1.是
							.uniqueResult();
		
		if(di){
			//当前日期
			Timestamp now = new Timestamp(System.currentTimeMillis());
			
			//时间类型 0.固定日期 1.领取后生效的天数
			Integer timeType = di.getTimeType();
			//优惠券数量
			Integer limit_number = di.getLimitNumber();
			//领取数量
			Integer get_num = di.getGetNum();
			//每日限制领取的数量
			Integer day_limit = di.getDayMaxNum();
			
			Integer daycount = GetDiscountCountToday(discount_id);
			
			if(daycount<=0)
			{
				return Code.DISCOUNT.当天优惠券数量不足();
			}
							
							
							
							
							
						
			//优惠券生效起始日期
			Timestamp discountStartTime = null;
			//优惠券生效结束日期
			Timestamp discountEndTime = null;
			
			//日期类型决定优惠券的有效期
			if(DiscountTimeType.固定日期.ordinal() == timeType ){
				discountStartTime = di.getStartTime();
				discountEndTime = di.getEndTime();
			}else if(DiscountTimeType.领取后生效的天数.ordinal() == timeType ){
				discountStartTime = now;
				discountEndTime = addDay(discountStartTime, di.getValidDays());
			}
			
			
			//用户领取优惠券记录
			DiscountUser ds = new DiscountUser();
			//优惠券code
			ds.setDiscountCode(di.getCode());
			//优惠券id
			ds.setDiscount(di);
			//领取优惠券用户手机号码
			ds.setPhone(phone);
			//优惠券领取日期
			ds.setGetTime(now);
			//优惠券默认未使用
			ds.setIsUse(DiscountUserIsUseType.未使用.ordinal());
			//优惠券使用日期
			ds.setUseTime(null);
			//领取优惠券的失效日期
			ds.setDiscountEndTime(discountEndTime)
			//领取优惠券的生效日期
			ds.setDiscountStartTime(discountStartTime);
			
			//优惠券领取数量加一
			
			if(di.getGetNum() == null)
			{
				di.setGetNum(1);
			}
			else
			{
	     		di.setGetNum(di.getGetNum()++);
			}
			
			//开启事务
			Transaction tran = session.beginTransaction();
			
			//更新优惠券表头-领取数量
			session.update(di);
			//新增优惠券表体-领取用户信息
			session.save(ds);
			
			//提交事务
			tran.commit();
			//刷新session缓存
			session.flush();
			
			discount_logs().save($$("_id" : UUID.randomUUID().toString() , "phone" : phone , "discount_id" : discount_id , "timestamp" : System.currentTimeMillis()));
		}
		return getResultOK();
	}
	/**
	 * phone电话是否领取过对应的优惠券
	 * @param phone			电话号码
	 * @param discount_id	优惠券id
	 * @return Boolean		true:领取过      false:未领取过
	 */
	private boolean isGetDiscount(String phone , int discount_id){
		Session session = sessionFactory.getCurrentSession();
		//领取的数量
		StringBuffer sbf = new StringBuffer();
		sbf.append(" SELECT  ");
		sbf.append(" 		count(id) ");
		sbf.append("   FROM  ");
		sbf.append(" 		DiscountUser ");
		sbf.append("   WHERE 1=1");
		sbf.append("     AND phone = ? ");
		sbf.append("     AND discount_id = ? ")
		
		
	

	
		int count = (int)sessionFactory
		.getCurrentSession().createQuery(sbf.toString())
		.setParameter(0, phone)
		.setParameter(1, discount_id)
		.uniqueResult();
		

		
		return count > 0 ;
	}
	
	/**
	 * 获取系统优惠券id
	 * @param discount_map_id discount_map表的id
	 * @return 优惠券id
	 */
	private Integer getSysDiscountId(String discount_map_id){
		Integer discount_id = null;
		def discount_map = discount_map().findOne($$("_id" : discount_map_id) , $$("discount_id" : 1));
		if(discount_map){
			Object obj = discount_map.get("discount_id");
			if(obj){
				discount_id = obj as Integer;
			}
		}
		return discount_id;
	}
	
	/**
	 * 日期t往后延迟days天
	 * @param t
	 * @param days
	 * @return
	 */
	private Timestamp addDay(Timestamp t , int days){
		Date date = new Date(t.getTime());
		Calendar   calendar   =   new   GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE,days);//把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();   //这个时间就是日期往后推一天的结果
		return new Timestamp(date.getTime());
	}
	
	
	/**
	 * 获取本日优惠券领取的数量
	 * @param discount_id	优惠券id
	 */
	private int GetDiscountCountToday(int discount_id){
		Session session = sessionFactory.getCurrentSession();
		//领取的数量
		StringBuffer sbf = new StringBuffer();
		sbf.append(" SELECT  ");
		sbf.append(" 		count(id) ");
		sbf.append("   FROM  ");
		sbf.append(" 		DiscountUser ");
		sbf.append("   WHERE 1=1");
		sbf.append("     AND discount_id = ? ");
		sbf.append("   AND get_time between ?  and ?");
		
	
	
		int count = (int)sessionFactory
		.getCurrentSession().createQuery(sbf.toString())
		.setParameter(0, discount_id)
		.setParameter(1, DataUtils.dateToStringStart(System.currentTimeMillis()))
		.setParameter(2, DataUtils.dateToStringEnd(System.currentTimeMillis()))
		.uniqueResult();
		
		
		Discount di = (Discount)session.createCriteria(Discount.class)
		.add(Restrictions.eq(Discount.PROP_ID , discount_id))//优惠券id
		.add(Restrictions.eq(Discount.PROP_ISSTOP , 0))//是否停用 0.否 1.是
		.uniqueResult();
		
		if(di)
		{
			return di.getDayMaxNum() - count;
		}
		else
		{
			return 0;
		}
	
	
	}
	
	

	
	
	
	
	
	
}
