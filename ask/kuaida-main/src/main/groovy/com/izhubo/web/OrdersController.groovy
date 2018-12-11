package com.izhubo.web

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.nio.charset.Charset
import java.sql.Timestamp

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.hibernate.Criteria
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.criterion.Order
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.model.AccScoreGainType
import com.izhubo.model.AccScoreType
import com.izhubo.model.Code
import com.izhubo.model.OrderPayStatus
import com.izhubo.model.OrderPayType
import com.izhubo.model.OrderPreferenceScheme
import com.izhubo.model.OrderStatus
import com.izhubo.model.OrdersPayOnLineType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.util.JSONUtil
import com.izhubo.rest.common.util.http.HttpClientUtil
import com.izhubo.rest.persistent.KGS
import com.izhubo.web.api.Web
import com.izhubo.web.pay.app.alipay.AlipayAppController
import com.izhubo.web.pay.app.wxpay.WXAppPayController
import com.izhubo.web.pay.h5.alipay.AlipaywapdirectController
import com.izhubo.web.pay.pc.alipay.AlipayController
import com.izhubo.web.pay.pc.bill99.Bill99SendController
import com.izhubo.web.pay.pc.wxpay.WXPayController
import com.izhubo.web.score.ScoreBase
import com.izhubo.web.vo.OrderListVO
import com.izhubo.web.vo.OrderListVO.OrderListVOData
import com.mchange.v2.c3p0.impl.NewPooledConnection;
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mysqldb.model.Discount
import com.mysqldb.model.DiscountUser
import com.mysqldb.model.Orders
import com.mysqldb.model.ScoreDetail
import com.mysqldb.model.UserScore
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation

/**
 * 订单
* @ClassName: OrdersController
* @Description: 订单
* @author shihongjie
* @date 2016年3月17日 上午11:11:46
*
 */
@RestWithSession
@RequestMapping("/orders")
class OrdersController extends BaseController {
	
	@Resource
	private SessionFactory sessionFactory;
	
	@Resource
	private AlipayController alipayController;
	@Resource
	private Bill99SendController bill99sendController;
	@Resource
	private AlipaywapdirectController alipaywapdirectController;
	@Resource
	private WXPayController wxpayController;
	
	@Resource
	private WXAppPayController wXAppPayController;
	
	
	@Resource
	private AlipayAppController alipayAppController;
	
	@Resource
	private CommoditysController commoditysController;
	
	
	@Resource
	KGS ordersComKGS;
	
	/** 支付宝单号补齐位 */
	@Value("#{application['orders.head']}")
	private String orders_head;
	
	/** 商品价格 */
	public DBCollection commodity_prices() {
		return mainMongo.getCollection("commodity_prices");
	}
	
	/** 虚拟货币价格 */
	public DBCollection currency_type() {
		return mainMongo.getCollection("currency_type");
	}
	
	
	
	/** 报名表 */
	public DBCollection signs() {
		return mainMongo.getCollection("signs");
	}
	
	public DBCollection app_review_state() {
		return mainMongo.getCollection("app_review_state");
	}
	
	
	
	private static final Charset UTF8= Charset.forName("utf8");
	
	/**
	 * 取消订单
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "cancleOrder/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "取消订单", httpMethod = "GET",  notes = "取消订单")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "order_no", value = "订单号", required = true, dataType = "String", paramType = "query")
	])
	def cancleOrder(HttpServletRequest request){
		//订单号
		String order_no = request[ "order_no"];
		if(StringUtils.isBlank(order_no) ){
			return getResultParamsError();
		}
		//用户id
		Integer user_id = Web.getCurrentUserId();
		
		Session session = sessionFactory.getCurrentSession();
		
		//订单信息
		Orders order = (Orders)session.createCriteria(Orders.class)
						.add(Restrictions.eq(Orders.PROP_ORDERNO, order_no))//id
						.add(Restrictions.eq(Orders.PROP_USERID, user_id))//用户id
						.add(Restrictions.or(
							Restrictions.eq(Orders.PROP_PAYSTATUS, OrderPayStatus.未支付.ordinal()),
							Restrictions.eq(Orders.PROP_PAYSTATUS, OrderPayStatus.待支付.ordinal())
							))//支付状态 0.未支付 1.待支付 ,2.支付成功
						.add(Restrictions.eq(Orders.PROP_STATUS, OrderStatus.有效.ordinal()))//点单有效情况
						.uniqueResult();
		if(order){
			//订单取消
			order.setStatus(OrderStatus.取消.ordinal());
			//开启事务
			Transaction tran = session.beginTransaction();
			
			cancle_order_preferencescheme(order, session);
			
			session.update(order);
			
			//提交事务
			tran.commit();
			//刷新session缓存
			session.flush();
			return getResultOK();
		}
		return getResultParamsError();
	}
	
	private Object cancle_order_preferencescheme(Orders order , Session session){
		Object obj = null;
		if(order != null){
			//优惠方式
			Integer pf = order.getPreferenceScheme();
			if(OrderPreferenceScheme.优惠券.ordinal() == pf){
//				//用户id
//				Integer user_id = order.getUserId();
//				//用户手机号
//				String telephone = qquserMongo.getCollection("qQUser").findOne(
//					$$("tuid" : users().findOne($$("_id" : user_id) , $$("tuid" : 1))?.get("tuid")),
//					$$("username" : 1)
//					)?.get("username");
				DiscountUser discountUser = (DiscountUser)session.createCriteria(DiscountUser.class)
												.add(Restrictions.eq(DiscountUser.PROP_ID, order.getCouponId()))
												.uniqueResult();
												
				if(discountUser != null){
					//未使用
					discountUser.setIsUse(0);
					//优惠券使用时间
					discountUser.setUseTime(null);
					
					session.update(discountUser);
					
					obj = discountUser;
				}								
			}else if(OrderPreferenceScheme.积分.ordinal() == pf){
				//积分扣减
				UserScore userScore = (UserScore)session.createCriteria(UserScore.class)
													.add(Restrictions.eq(UserScore.PROP_USERID, order.getUserId()))
													.uniqueResult();
				
				if(userScore != null){
					userScore.setUserScoreRemain(userScore.getUserScoreRemain().add(new BigDecimal(order.getUseIntegral().toDouble())));
					
					session.update(userScore);
					
					obj = userScore;
				}
				
			}
		}
		return obj;
	}
	
	/**
	 * 待支付-订单详情接口
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "orderInfo/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "待支付-订单详情接口", httpMethod = "GET",  notes = "待支付-订单详情接口,页面有两种状态 : pay_status=0未支付 可以修改支付信息 ; pay_status=1 待支付 不可修改优惠方案,只能修改支付方式")
	@ApiImplicitParams([
	    @ApiImplicitParam(name = "order_no", value = "订单号", required = false, dataType = "String", paramType = "query"),
	    @ApiImplicitParam(name = "order_id", value = "订单ID", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "commodity_id", value = "商品id", required = false, dataType = "String", paramType = "query")
	])
	def orderInfo(HttpServletRequest request){
		//商品id
		Integer order_id = ServletRequestUtils.getIntParameter(request , "order_id" , 0);
		//商品id
		String commodity_id = request[ "commodity_id"];
		//订单号
		String order_no = request[ "order_no"];
		if(StringUtils.isBlank(commodity_id) && StringUtils.isBlank(order_no) && order_id == 0 ){
			return getResultParamsError();
		}
		
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//订单信息
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Orders.class)
						.add(Restrictions.eq(Orders.PROP_USERID, user_id))//用户id
						.add(Restrictions.or(
							Restrictions.eq(Orders.PROP_PAYSTATUS, OrderPayStatus.未支付.ordinal()),
							Restrictions.eq(Orders.PROP_PAYSTATUS, OrderPayStatus.待支付.ordinal())
							))//支付状态 0.未支付 1.待支付 ,2.支付成功
						.add(Restrictions.eq(Orders.PROP_STATUS, OrderStatus.有效.ordinal()));//点单有效情况
		
		if(order_id > 0){
			c.add(Restrictions.eq(Orders.PROP_ID, order_id));
		}else if(StringUtils.isNotBlank(commodity_id)){
			c.add(Restrictions.eq(Orders.PROP_PRODUCTID, commodity_id));
		}else{
			c.add(Restrictions.eq(Orders.PROP_ORDERNO, order_no));
		}
		Orders order = (Orders)c.uniqueResult();				
			
		if(order){
			Map map = new HashMap();
			//订单编号
			map.put("orderno", order.getOrderno());
			//支付状态 0.未支付 1.待支付 ,2.支付成功
			map.put("pay_status", order.getPayStatus());
			//实际支付金额
			map.put("pay_money", order.getPayMoney());
			//订单金额
			map.put("total_money", order.getTotalMoney());
			//优惠方案 0.不使用优惠 1.积分 2.优惠券
			map.put("preference_scheme", order.getPreferenceScheme());
			//使用积分
			map.put("use_integral", order.getUseIntegral());
			//优惠券id
			map.put("coupon_id", order.getCouponId());
			
			Integer preferenceScheme = order.getPreferenceScheme()
			if(preferenceScheme != null && preferenceScheme == OrderPreferenceScheme.优惠券.ordinal() && order.getCouponId() != null){
				Discount discount = (Discount) sessionFactory.getCurrentSession().get(Discount.class, order.getCouponId());
				
				if(discount){
					//优惠券name
					map.put("coupon_name",discount.getName());
				}
				
			}
			if(
				order.getPayStatus() == OrderPayStatus.待支付.ordinal() &&
				order.getPreferenceScheme() == OrderPreferenceScheme.积分.ordinal()
				){
				//用户可使用的积分
				map.put("user_score", order.getUseIntegral());
				//积分可抵消余额
				map.put("offset_money", order.getFavorableMoney());
			}else{
				def userScoreMap = userCanUseScore(user_id, order.getProductId());
				//用户可使用的积分
				map.put("user_score", userScoreMap.get("user_score"));
				//积分可抵消余额
				map.put("offset_money", userScoreMap.get("offset_money"));
			}
			
			//线上付款方式 0.undefined 1.支付宝 2.微信 3.快钱
			map.put("pay_on_line_type", order.getPayOnLineType());
			
			return getResultOK(map);
		}
		return getResultParamsError()
	}
	
	
	/**
	 * 用户购买商品时可使用的最大积分和抵消的金额
	 * @param user_id		用户id
	 * @param commodity_id	商品id
	 * @return	map["user_score"]=用户可使用的积分 ; map["offset_money"] = 积分可抵消余额;
	 */
	def Map userCanUseScore(Integer user_id , String commodity_id){
		Map map = new HashMap();
		//用户可使用的积分
		map["user_score"] = 0;
		//积分可抵消余额
		map["offset_money"] = 0;
		Double offset_money = 0;
		//用户可使用积分
		Double user_score = 0
		//积分比例 1积分可抵消多少元
		Double scale = 0;
		
		if(scale > 0){
			Double paypoints = commoditys().findOne($$("_id" : commodity_id) , $$("paypoints" : 1))?.get("paypoints") as Double;
			
			if(paypoints > 0){
				//如果用户剩余积分大于商品使用的最高积分,则用户可使用的积分最大值为商品上限的积分
				if(paypoints < user_score){
					user_score = paypoints;
				}
				
				//可抵消的金额
				offset_money = scale * user_score;
			}
			
		}
		
		map["user_score"] = user_score;
		map["offset_money"] = offset_money;
		return map;
	}
	
	/**
	 * 商品购买接口
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "buyCommodityInfo/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "商品购买信息", httpMethod = "GET",  notes = "type:-1.未购买 0.未支付 1.待支付 ,2.支付成功  order_id:订单id commodity_id:商品id")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "commodity_id", value = "商品id", required = true, dataType = "String", paramType = "query")
	])
	def buyCommodityInfo(HttpServletRequest request){
		//-1:未购买 0.未支付 1.待支付 ,2.支付成功
		Integer result = -1;
		Integer order_id = null;
		String commodity_id = request["commodity_id"];
		if(StringUtils.isBlank(commodity_id)){
			return getResultParamsError();
		}
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//订单信息
		Orders order = (Orders)sessionFactory.getCurrentSession().createCriteria(Orders.class)
			.add(Restrictions.eq(Orders.PROP_USERID, user_id))
			.add(Restrictions.eq(Orders.PROP_PRODUCTID, commodity_id))
			.add(Restrictions.ne(Orders.PROP_STATUS, OrderStatus.取消.ordinal()))
			.uniqueResult();
		if(order){
			order_id = order.getId();
			result = order.getPayStatus();
		}else{
			//查询报名表
			//查询NCid
			String nc_id = commoditys().findOne(commoditysQuery().append("_id", commodity_id) , $$("nc_id" : 1))?.get("nc_id");
			if(StringUtils.isNotBlank(nc_id)){
				//报名表
				if(signs().findOne($$("user_id" : user_id , "nc_commodity_id" : nc_id)) != null){
					result = OrderPayStatus.支付成功.ordinal();
				}
			}
		}
		Map map = new HashMap();
		map["type"] = result;
		map["order_id"] = order_id;
		map["commodity_id"] = commodity_id;
		return getResultOK(map);
	}
	
	
//	/**
//	 * 虚拟货币商品购买接口
//	 * @param request
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value = "buyVirtualCurrency/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
//	@ApiOperation(value = "商品购买信息", httpMethod = "GET",  notes = "传入虚拟货币的额度类型，入1元 2元 6元档位的类型id")
//	@ApiImplicitParams([
//		@ApiImplicitParam(name = "currency_type", value = "商品类型", required = true, dataType = "String", paramType = "query")
//	])
//	def buyVirtualCurrency(HttpServletRequest request){
//		//-1:未购买 0.未支付 1.待支付 ,2.支付成功
//		Integer result = -1;
//		Integer order_id = null;
//		String currency_type = request["currency_type"];
//		if(StringUtils.isBlank(currency_type)){
//			return getResultParamsError();
//		}
//		//用户id
//		Integer user_id = Web.getCurrentUserId();
//		//订单信息
//		Orders order = (Orders)sessionFactory.getCurrentSession().createCriteria(Orders.class)
//			.add(Restrictions.eq(Orders.PROP_USERID, user_id))
//			.add(Restrictions.eq(Orders.PROP_PRODUCTID, currency_type))
//			.add(Restrictions.ne(Orders.PROP_STATUS, OrderStatus.取消.ordinal()))
//			.uniqueResult();
//		if(order){
//			order_id = order.getId();
//			result = order.getPayStatus();
//		}else{
//			//查询报名表
//			//查询NCid
//			String nc_id = commoditys().findOne(commoditysQuery().append("_id", commodity_id) , $$("nc_id" : 1))?.get("nc_id");
//			if(StringUtils.isNotBlank(nc_id)){
//				//报名表
//				if(signs().findOne($$("user_id" : user_id , "nc_commodity_id" : nc_id)) != null){
//					result = OrderPayStatus.支付成功.ordinal();
//				}
//			}
//		}
//		Map map = new HashMap();
//		map["type"] = result;
//		map["order_id"] = order_id;
//		map["commodity_id"] = commodity_id;
//		return getResultOK(map);
//	}
	
	//TODO 订单支付
	def pay_order(
		String order_id,
		HttpServletRequest request
		){
		
		Orders order = (Orders)sessionFactory.getCurrentSession().createCriteria(Orders.class)
						.add(Restrictions.eq(Orders.PROP_ORDERNO, order_id))
						.add(Restrictions.eq(Orders.PROP_STATUS, OrderStatus.有效.ordinal()))
						.uniqueResult();
						
		if(order != null){
			if(OrderPayStatus.待支付.ordinal() == order.getPayStatus()){
				
			}else if(OrderPayStatus.支付成功.ordinal() == order.getPayStatus()){
				return Code.ORDERS.订单已支付成功();
			}
			
		}
		
		return getResultParamsError();
	}
	/**
	 * 生成订单
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "create_currency_order/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "生成虚拟货币购买订单", httpMethod = "GET",  notes = "生成订单")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "currency_type_id", value = "传入虚拟货币的额度类型，入1元 2元 6元档位的类型id", required = true, dataType = "String", paramType = "query")
	])
	def create_currency_order(HttpServletRequest request){
		def arr =[];
		def a = arr.get(2)
		Map result = new HashMap();
		result["code"] = 80002;
		result["msg"] = "商品已下架!";
		result["data"] = "商品已下架!";
		return result;
		/*String currency_type_id = request["currency_type_id"];
		 * 
		//商品id非空
		if(StringUtils.isNotBlank(currency_type_id)){
			//用户id
			Integer user_id = Web.getCurrentUserId();
			def data = create_currency_order(currency_type_id , user_id);
			
			
			return data;
		}
		return getResultParamsError();*/
		
	}
	
	
	/**
	 * 生成订单
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "create_currency_order_iospay/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "生成虚拟货币购买订单", httpMethod = "GET",  notes = "生成订单")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "currency_type_id", value = "传入虚拟货币的额度类型，入1元 2元 6元档位的类型id", required = true, dataType = "String", paramType = "query")
	])
	def create_currency_order_iospay(HttpServletRequest request){
		Map result = new HashMap();
		result["code"] = 80002;
		result["msg"] = "商品已下架!";
		result["data"] = "商品已下架!";
		return result;
		/*String currency_type_id = request["currency_type_id"];
		//商品id非空
		if(StringUtils.isNotBlank(currency_type_id)){
			//用户id
			Integer user_id = Web.getCurrentUserId();
			def data = create_currency_order(currency_type_id , user_id);
			return data;
		}
		return getResultParamsError();*/
		
		
	}
	
	
	/**
	 * 判断状态，决定当前是否是审核状态，如果是审核状态，客户端会采取另外一套逻辑
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "is_review/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "是否在审核状态", httpMethod = "GET",  notes = "是否在审核状态")
	def is_review(HttpServletRequest request){
		
		
		return getResultOK(app_review_state().findOne(new BasicDBObject(),new BasicDBObject("_id":0)));
		
	
	}
	
	
	/**
	 * 生成订单
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "create_order/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "生成订单", httpMethod = "GET",  notes = "生成订单")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "commodity_id", value = "商品id", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "school_code", value = "校区nc_id", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "hope_classtime", value = "期望上课时间说明", required = false, dataType = "String", paramType = "query")
	])
	def create_order(HttpServletRequest request){
		
		String commodity_id = request["commodity_id"];
		//实际拿到的是nc_school_id
		String nc_school_id = request["school_code"];
		String hopeClassTimeType = request["hope_classtime"];
		
		//商品id非空
		if(StringUtils.isNotBlank(commodity_id)){
			//用户id
			Integer user_id = Web.getCurrentUserId();
			def data = create_commodity_order(commodity_id , user_id ,nc_school_id , hopeClassTimeType);
			return data;
		}
		return getResultParamsError();
	}
	
	
	
	/**
	 * 创建商品订单
	 * @Description: 创建商品订单
	 * @date 2016年3月18日 下午3:11:04
	 * @param @param currency_type 虚拟货币的类型
	 * @param @param user_id      用户id
	 * @param @param nc_school_id  校区nc_id
	 * @param @return             返回订单信息order 错误情况下返回错误信息
	 */
	def create_currency_order(String currency_type_id , Integer user_id){
		//TODO 消耗积分 NC通知
		
		
		//TODO 商机同步到NC
		
		//商品
		def currency_type = currency_type().findOne(
				$$("_id" : currency_type_id)
			);
		
		//商品存在
		if(currency_type == null){
			return Code.COMMODITYS.商品不存在();
		}
		Map result = new HashMap();
		
		//商品价格
//		def commodity_prices = commodity_prices().findOne($$("nc_commodity_id" : commodity["nc_id"] , "school_pks" : $$('$in' : [nc_school_id])));
		Double commodity_prices = (Double) currency_type.get("currency_type_money");
		
		if(commodity_prices == null || commodity_prices <= 0){//购买地区的价格情况
			result["code"] = 0;
			result["msg"] = "商品价格获取出错!";
			result["data"] = "商品价格获取出错!";
			return result;
		}
		
		//创建订单
		Orders order = new Orders();
		
		//订单号
		String orderno = orders_head + String.valueOf(ordersComKGS.nextId());
		
		//日期
		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		//点单编号
		order.setOrderno(orderno);
		//用户id
		order.setUserId(user_id);
		//商品ID
		order.setProductId(currency_type_id);
	
		//商品描述
		order.setOrderDescribe(currency_type.get("currency_type_text").toString());
		//订单名称
		order.setOrderName(currency_type.get("currency_type_text").toString());

		//商品价格
		order.setTotalMoney(commodity_prices);
		//支付状态
		order.setPayStatus(OrderPayStatus.未支付.ordinal());
		//支付方式
		order.setPayType(OrderPayType.在线支付.ordinal());
		//订单状态 商品行为-下架
		order.setStatus(OrderStatus.有效.ordinal());
		
		//订单有效期
		order.setValidity(null);
		//订单备注
		order.setRemark(null);
		//创建时间
		order.setCreateTime(timestamp);
		//修改人
		order.setModifier(user_id);
		//修改时间
		order.setModifiedtime(timestamp);
		
		//是否删除 用户行为
//		order.setDr(DR.未删除.ordinal());
		//支付时间
		order.setPayTime(null);
		//商品类型 0.vip , 1.商品
		order.setProductType(1);
		//实际支付金额
		order.setPayMoney(commodity_prices);
		
		//优惠金额 0.不使用优惠 1.积分 2.优惠券
		order.setFavorableMoney(0d);
		//优惠方案
		order.setPreferenceScheme(null);
		//使用积分
		order.setPreferenceScheme(null);
		//优惠券id
		order.setCouponId(null);
		//线上支付方式
		order.setPayOnLineType(null);
		
		//支付回调时间
		order.setPayCallblackTime(null);
		//支付回调备注
		order.setPayCallblackRemark(null);

		
		//保存商品
		sessionFactory.getCurrentSession().save(order);
		
		result["code"] = 1;
		result["data"] = order;
	
		
		//返回正常信息
		return result;
	}
	
	
	/**
	 * 创建商品订单
	 * @Description: 创建商品订单
	 * @date 2016年3月18日 下午3:11:04
	 * @param @param commodity_id 商品id
	 * @param @param user_id      用户id
	 * @param @param nc_school_id  校区nc_id
	 * @param @return             返回订单信息order 错误情况下返回错误信息
	 */
	def create_commodity_order(String commodity_id , Integer user_id , String nc_school_id , String hopeClassTime){
		//TODO 消耗积分 NC通知
		
		// 查询用户是否购买过该商品
		if(!isCreateOrder(commodity_id , user_id)){
			return Code.COMMODITYS.重复购买();
		}
		//TODO 商机同步到NC
		
		//商品
		def commodity = commoditys().findOne(
				$$("_id" : commodity_id),
				$$("_id" : 1 , "paypoints" : 1 , "short_name" : 1 ,"name" : 1 , "nc_id" : 1 , "thumbnail" : 1 , "is_shelves" : 1)
			);
		
		//商品存在
		if(commodity == null){
			return Code.COMMODITYS.商品不存在();
			
		}else if(commodity["is_shelves"] == 0){//商品上架情况校验
			return Code.COMMODITYS.商品已下架();
		}
		Map result = new HashMap();
		
		//商品价格
//		def commodity_prices = commodity_prices().findOne($$("nc_commodity_id" : commodity["nc_id"] , "school_pks" : $$('$in' : [nc_school_id])));
		Double commodity_prices = (Double) commoditysController.find_commodity_area_price(commodity["_id"].toString(), nc_school_id);
		
		if(commodity_prices == null || commodity_prices <= 0){//购买地区的价格情况
			result["code"] = 0;
			result["msg"] = "商品价格获取出错!";
			result["data"] = "商品价格获取出错!";
			return result;
		}
		
		//创建订单
		Orders order = new Orders();
		
		//订单号
		String orderno = orders_head + String.valueOf(ordersComKGS.nextId());
		
		//日期
		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		//点单编号
		order.setOrderno(orderno);
		//用户id
		order.setUserId(user_id);
		//商品ID
		order.setProductId(commodity_id);
		//商品对应的NCid
		order.setNcProductId(commodity["nc_id"].toString());
		//商品图片
		order.setPic(commodity["thumbnail"]?.toString());
		//商品描述
		order.setOrderDescribe(commodity["short_name"].toString());
		//订单名称
		order.setOrderName(commodity["name"].toString())
		//校区编号
		order.setNcSchoolId(nc_school_id);
		//商品价格
		order.setTotalMoney(commodity_prices);
		//支付状态
		order.setPayStatus(OrderPayStatus.未支付.ordinal());
		//支付方式
		order.setPayType(OrderPayType.在线支付.ordinal());
		//订单状态 商品行为-下架
		order.setStatus(OrderStatus.有效.ordinal());
		
		//订单有效期
		order.setValidity(null);
		//订单备注
		order.setRemark(null);
		//创建时间
		order.setCreateTime(timestamp);
		//修改人
		order.setModifier(user_id);
		//修改时间
		order.setModifiedtime(timestamp);
		
		//是否删除 用户行为
//		order.setDr(DR.未删除.ordinal());
		//支付时间
		order.setPayTime(null);
		//商品类型 0.vip , 1.商品
		order.setProductType(1);
		//实际支付金额
		order.setPayMoney(commodity_prices);
		
		//优惠金额 0.不使用优惠 1.积分 2.优惠券
		order.setFavorableMoney(0d);
		//优惠方案
		order.setPreferenceScheme(null);
		//使用积分
		order.setPreferenceScheme(null);
		//优惠券id
		order.setCouponId(null);
		//线上支付方式
		order.setPayOnLineType(null);
		
		//支付回调时间
		order.setPayCallblackTime(null);
		//支付回调备注
		order.setPayCallblackRemark(null);
		//用户是否取消
//		order.setIsCancel(0);
		//学生期望上课时间段
		order.setHopeClassTime(hopeClassTime);
		
		//保存商品
		sessionFactory.getCurrentSession().save(order);
		
		result["code"] = 1;
		result["data"] = order;
		//返回正常信息
		return result;
	}
	/**
	 * 生成订单第二部 - 确认支付方式
	 * @param order_id				订单id
	 * @param preference_scheme		优惠金额 0.不使用优惠 1.积分 2.优惠券
	 * @param coupon_id				优惠券id
	 * @param pay_money				实际支付金额(暂时无用)
	 * @param pay_online_type		线上付款方式 0.undefined 1.支付宝 2.微信 3.快钱
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value = "comfirmorder_v2/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "订单确认支付方式", httpMethod = "GET",  notes = "生成订单第二部 - 确认订单")
	@ApiImplicitParams([
						@ApiImplicitParam(name = "order_id", value = "订单ID", required = false, dataType = "string", paramType = "query"),
						@ApiImplicitParam(name = "order_no", value = "订单编号", required = false, dataType = "string", paramType = "query"),
						@ApiImplicitParam(name = "pay_online_type", value = "1.支付宝PC 2.微信PC 3.快钱PC 4.支付宝网银PC  11.支付宝H5 12.微信H5 13.块钱H5 14 支付宝原生 15 微信原生 ", required = true, dataType = "int", paramType = "query")
						])
	def comfirmorder_v2(HttpServletRequest request){
		//TODO 去掉订单编号
		//订单id
		Integer order_id = ServletRequestUtils.getIntParameter(request , "order_id" , 0);
		//订单编号
		String order_no = ServletRequestUtils.getStringParameter(request, "order_no");
		
		if(StringUtils.isBlank(order_no) && order_id == 0){
			return getResultParamsError();
		}
		

		//线上付款方式 0.undefined 1.支付宝 2.微信 3.快钱
		int pay_online_type = ServletRequestUtils.getIntParameter(request, "pay_online_type", 0);
		
		Session session = sessionFactory.getCurrentSession();
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Orders.class);
		if(order_id > 0){
			c.add(Restrictions.eq(Orders.PROP_ID, order_id));
		}else{
			c.add(Restrictions.eq(Orders.PROP_ORDERNO, order_no));
		}
		Orders order = (Orders)c.uniqueResult();
		
//		Orders order = (Orders)sessionFactory.getCurrentSession().createCriteria(Orders.class)
//		.add(Restrictions.or(
//				Restrictions.eq(Orders.PROP_ID, order_id),
//				Restrictions.eq(Orders.PROP_ORDERNO, order_no)
//			))
//		.uniqueResult();
		
		//订单不存在
		if(order == null){
			return getResultParamsError();
		}
		//商品id
		String currency_id = order.getProductId();
		//商品
		def currency_type = currency_type().findOne($$("_id" : currency_id));
		//订单是否被删除
		if(!check_valid(order)){
			return Code.ORDERS.订单无效();
		}
		//TDOD 微信支付
		//微信支付获取wxcode
		if(pay_online_type == OrdersPayOnLineType.微信PC.ordinal()){
			//微信code
			String wxcode = ServletRequestUtils.getStringParameter(request, "code");
			//根据微信code获取appid
			appid = getWXAppId(wxcode);
			println "appid=" + appid;
			//保存获取到的微信appid
			order.setWxopenId(appid);
		}
		
		//开启事务
		Transaction tran = session.beginTransaction();
		
		//用户id
		Integer user_id = Web.getCurrentUserId();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//TODO 如果已经确认过支付方式-支付的优惠方式不能修改
		if(OrderPayStatus.未支付.ordinal() == order.getPayStatus()){
			
			//用户手机号
			String telephone = qquserMongo.getCollection("qQUser").findOne(
				$$("tuid" : users().findOne($$("_id" : user_id) , $$("tuid" : 1))?.get("tuid")),
				$$("username" : 1)
				)?.get("username");
			
//			String telephone = users().findOne($$("_id" : user_id) , $$("telephone" : 1))?.get("telephone");
			
			//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 计算实际支付金额 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
			
			//商品实际支付金额
			Double apay_money = order.getTotalMoney();
			
			
			//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 计算实际支付金额 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
			
			if(apay_money <= 0){
				return Code.ORDERS.订单支付金额错误();
			}
			
			//订单数据修改
			
			//实际支付金额
			order.setPayMoney(apay_money);
	
			//设置 优惠方案 0.不使用优惠 1.积分 2.优惠券 的属性值
			order.setPreferenceScheme(0);
			//消耗多少积分
			order.setUseIntegral(0);
		}
		
		//保存order数据
		
		//重新发起支付时修改支付的基本信息
		//修改人
		order.setModifier(user_id);
		//修改时间
		order.setModifiedtime(timestamp);
		//支付IP
		order.setPayip(getIpAddr(request));
		//线上支付方式
		order.setPayOnLineType(pay_online_type);
		//支付状态
		order.setPayStatus(OrderPayStatus.待支付.ordinal());
		
		//微信支付 设置微信用户唯一id
		if(order.getPayOnLineType() == OrdersPayOnLineType.微信H5.ordinal() || order.getPayOnLineType() == OrdersPayOnLineType.微信PC.ordinal()){
			order.setWxopenId(null);
		}
		
		//TODO 生成url
		def pay_url = payUrl(order);
		
		//支付时间
		order.setPayTime(new Timestamp(System.currentTimeMillis()));
		order.setPayUrl(pay_url.toString());
		
		session.update(order);
		
		//提交事务
		tran.commit();
		//刷新session缓存
		session.flush();
		
		return getResultOK(pay_url);
	}
	
	
	/**
	 * 生成订单第二部 - 确认支付方式
	 * @param order_id				订单id
	 * @param preference_scheme		优惠金额 0.不使用优惠 1.积分 2.优惠券
	 * @param coupon_id				优惠券id
	 * @param pay_money				实际支付金额(暂时无用)
	 * @param pay_online_type		线上付款方式 0.undefined 1.支付宝 2.微信 3.快钱
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value = "comfirmorder/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "订单确认支付方式", httpMethod = "GET",  notes = "生成订单第二部 - 确认订单")
	@ApiImplicitParams([
						@ApiImplicitParam(name = "order_id", value = "订单ID", required = false, dataType = "string", paramType = "query"),
						@ApiImplicitParam(name = "order_no", value = "订单编号", required = false, dataType = "string", paramType = "query"),
						@ApiImplicitParam(name = "preference_scheme", value = "优惠金额 0.不使用优惠 1.积分 2.优惠券", required = true, dataType = "int", paramType = "query"),
						@ApiImplicitParam(name = "coupon_id", value = "优惠券id", required = false, dataType = "int", paramType = "query"),
						@ApiImplicitParam(name = "pay_online_type", value = "1.支付宝PC 2.微信PC 3.快钱PC 4.支付宝网银PC  11.支付宝H5 12.微信H5 13.块钱H5 ", required = true, dataType = "int", paramType = "query")
						])
	def comfirmorder(HttpServletRequest request){
		//TODO 去掉订单编号
		//订单id
		Integer order_id = ServletRequestUtils.getIntParameter(request , "order_id" , 0);
		//订单编号
		String order_no = ServletRequestUtils.getStringParameter(request, "order_no");
		
		if(StringUtils.isBlank(order_no) && order_id == 0){
			return getResultParamsError();
		}
		
//		//weixinCOde
//		String wxcode = ServletRequestUtils.getStringParameter(request, "code");
		//优惠金额 0.不使用优惠 1.积分 2.优惠券
		int preference_scheme = ServletRequestUtils.getIntParameter(request, "preference_scheme", 0);
		//优惠券id
		int coupon_id = ServletRequestUtils.getIntParameter(request, "coupon_id", 0);
		//线上付款方式 0.undefined 1.支付宝 2.微信 3.快钱
		int pay_online_type = ServletRequestUtils.getIntParameter(request, "pay_online_type", 0);
		
		Session session = sessionFactory.getCurrentSession();
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Orders.class);
		if(order_id > 0){
			c.add(Restrictions.eq(Orders.PROP_ID, order_id));
		}else{
			c.add(Restrictions.eq(Orders.PROP_ORDERNO, order_no));
		}
		Orders order = (Orders)c.uniqueResult();
		
//		Orders order = (Orders)sessionFactory.getCurrentSession().createCriteria(Orders.class)
//		.add(Restrictions.or(
//				Restrictions.eq(Orders.PROP_ID, order_id),
//				Restrictions.eq(Orders.PROP_ORDERNO, order_no)
//			))
//		.uniqueResult();
		
		//订单不存在
		if(order == null){
			return getResultParamsError();
		}
		//商品id
		String commodity_id = order.getProductId();
		//商品
		def commodity = commoditys().findOne($$("_id" : commodity_id));
		//订单是否被删除
		if(!check_valid(order)){
			return Code.ORDERS.订单无效();
		}
		//校验商品是否下架
		if(!check_commodity(order , commodity)){
			return Code.COMMODITYS.商品已下架();
		}
		//TDOD 微信支付
		//微信支付获取wxcode
		if(pay_online_type == OrdersPayOnLineType.微信PC.ordinal()){
			//微信code
			String wxcode = ServletRequestUtils.getStringParameter(request, "code");
			//根据微信code获取appid
			appid = getWXAppId(wxcode);
			println "appid=" + appid;
			//保存获取到的微信appid
			order.setWxopenId(appid);
		}
		
		//开启事务
		Transaction tran = session.beginTransaction();
		
		//用户id
		Integer user_id = Web.getCurrentUserId();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//TODO 如果已经确认过支付方式-支付的优惠方式不能修改
		if(OrderPayStatus.未支付.ordinal() == order.getPayStatus()){
			
			//用户手机号
			String telephone = qquserMongo.getCollection("qQUser").findOne(
				$$("tuid" : users().findOne($$("_id" : user_id) , $$("tuid" : 1))?.get("tuid")),
				$$("username" : 1)
				)?.get("username");
			
//			String telephone = users().findOne($$("_id" : user_id) , $$("telephone" : 1))?.get("telephone");
			
			//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 计算实际支付金额 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
			
			//商品实际支付金额
			Double apay_money = order.getTotalMoney();
			//优惠券
			Discount discount = null;
			DiscountUser discountUser = null;
			//优惠金额
			Double favorableMoney = 0d;
	//		//用户使用的积分
			Integer use_integral = 0;
			if(preference_scheme == OrderPreferenceScheme.不使用优惠.ordinal()){
				//实际支付用金额
				apay_money = order.getTotalMoney();
				
				
				
			}else if(preference_scheme == OrderPreferenceScheme.优惠券.ordinal()){
				//hibernate 关联  discount_user_id
				discountUser = (DiscountUser)session.createCriteria(DiscountUser.class)
							.add(Restrictions.eq(DiscountUser.PROP_ID, coupon_id))
							.add(Restrictions.eq(DiscountUser.PROP_PHONE, telephone))
							.uniqueResult();
							
				if(discountUser == null){
					return getResultParamsError();
				}
				
				discount = discountUser.getDiscount();
				//校验优惠券是否在使用日期范围 && 优惠券最大金额没有超过商品限制的最大使用金额范围
				if(!check_discount(discountUser) || !check_discount_commodity(discount , order)){
					return Code.ORDERS.优惠券使用错误();
				}
				//优惠多少金额
				favorableMoney = getApplyMoneyByDiscount(discount, order);
				
				//优惠券id
//				order.setCouponId(discount.getId());
				order.setCouponId(discountUser.getId());
				
			}else if(preference_scheme == OrderPreferenceScheme.积分.ordinal()){
				//积分
				def suerScoreMap = userCanUseScore(user_id, commodity_id);
				//优惠多少金额
				favorableMoney = suerScoreMap["offset_money"] as Double;
				//消耗多少积分
				use_integral = suerScoreMap["user_score"] as Integer;
			}
			//实际支付金额
			apay_money = sub(order.getTotalMoney() , favorableMoney);
			
			//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 计算实际支付金额 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
			
			if(apay_money <= 0){
				return Code.ORDERS.订单支付金额错误();
			}
			
			//订单数据修改
			
			//实际支付金额
			order.setPayMoney(apay_money);
			// 优惠金额
			order.setFavorableMoney(favorableMoney);
			//设置 优惠方案 0.不使用优惠 1.积分 2.优惠券 的属性值
			order.setPreferenceScheme(preference_scheme);
			//消耗多少积分
			order.setUseIntegral(use_integral);
		
			
			if(preference_scheme == OrderPreferenceScheme.优惠券.ordinal()){
				
				//优惠券状态修改
				discountUser.setIsUse(1);
				//优惠券使用时间
				discountUser.setUseTime(timestamp);
				//更新优惠券使用情况
				session.update(discountUser);
			}else if(preference_scheme == OrderPreferenceScheme.积分.ordinal()){
			
				//积分扣减
				UserScore userScore = (UserScore)session.createCriteria(UserScore.class)
														.add(Restrictions.eq(UserScore.PROP_USERID, user_id))
														.uniqueResult();
				
				if(userScore){
					userScore.setUserScoreRemain(userScore.getUserScoreRemain().subtract(new BigDecimal(use_integral)));
					session.update(userScore);
					
					//积分详情
					ScoreDetail scoreDetail = new ScoreDetail();
					//消耗积分
					scoreDetail.setScore(new BigDecimal(use_integral));
					//明细说明
					scoreDetail.setScoreDetail("订单编号:"+order.getOrderno() + ",商品名称:" + order.getOrderName() + ",消耗积分!" );
					//消费类型
					scoreDetail.setScoreGainType(AccScoreGainType.购买商品消耗积分.ordinal());
					//积分加减类型
					scoreDetail.setScoreType(AccScoreType.消耗积分.ordinal());
					//用户id
					scoreDetail.setUserId(user_id);
					//用户昵称
					scoreDetail.setUserNickname(Web.currentUserNick());
					//时间戳
					scoreDetail.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis()));
					session.save(scoreDetail);
				}
														
			}
		}
		//保存order数据
		
		//重新发起支付时修改支付的基本信息
		//修改人
		order.setModifier(user_id);
		//修改时间
		order.setModifiedtime(timestamp);
		//支付IP
		order.setPayip(getIpAddr(request));
		//线上支付方式
		order.setPayOnLineType(pay_online_type);
		//支付状态
		order.setPayStatus(OrderPayStatus.待支付.ordinal());
		
		//微信支付 设置微信用户唯一id
//		if(order.getPayOnLineType() == OrdersPayOnLineType.微信H5.ordinal() || order.getPayOnLineType() == OrdersPayOnLineType.微信PC.ordinal()){
//			order.setWxopenId(null);
//		}
		
		//TODO 生成url
		def pay_url = payUrl(order);
		
		//支付时间
		order.setPayTime(new Timestamp(System.currentTimeMillis()));
		order.setPayUrl(pay_url.toString());
		
		session.update(order);
		
		//提交事务
		tran.commit();
		//刷新session缓存
		session.flush();
		
		return getResultOK(pay_url);
	}
		
	
	/**
	 * 微信 通过code获取appid
	 * @see http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html
	 * @param code
	 * @return AppId
	 */
	private String getWXAppId(String code){
		String appId = null;
		if(StringUtils.isNotBlank(code)){
			// 微信通过code获取appid 地址  详情参见:http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html  https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
			String sUrl = com.izhubo.web.pay.pc.wxpay.common.Configure.OAUTH2_URL + "?appid=" + com.izhubo.web.pay.pc.wxpay.common.Configure.APPID + "&secret=" + com.izhubo.web.pay.pc.wxpay.common.Configure.APPSECRET + "&code=" + code + "&grant_type=authorization_code";
			//返回json
			String json =  HttpClientUtil.get(sUrl, null, UTF8);
			//结果解析
			Map<String, Object> map = JSONUtil.jsonToMap(json);
			//appid
			appId = map["openid"];
		}
		return appId;
	}
		
	
	private Object payUrl(Orders order){
		Integer pay_on_line_type = 	order.getPayOnLineType();
		switch(pay_on_line_type){
			case OrdersPayOnLineType.支付宝PC.ordinal() :
				return alipayController.createPayHtml(order.getId());
			case OrdersPayOnLineType.快钱PC.ordinal():
				return bill99sendController.createPayHtml(order.getId());
			case OrdersPayOnLineType.支付宝H5.ordinal():
				return alipaywapdirectController.createPayHtml(order.getId());
			case OrdersPayOnLineType.微信PC.ordinal():
				return wxpayController.createPayHtml(order.getId());
			case OrdersPayOnLineType.支付宝原生.ordinal():
			    return alipayAppController.createPayHtml(order.getId());
				
				case OrdersPayOnLineType.微信原生.ordinal():
				return wXAppPayController.createPayHtml(order.getId());
				
//			break;
			default: return null;
		}
	}
	
	/**
	 * 提供精确的乘法运算。
	 *
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
	 BigDecimal b1 = new BigDecimal(Double.toString(v1));
	 BigDecimal b2 = new BigDecimal(Double.toString(v2));
	 return b1.multiply(b2).doubleValue();
	}
	
	/**
	 * 提供精确的减法运算。
	 * @param v1 被减数
	 * @param v2 减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1,double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}
		
	/**
	 * 校验订单有效性
	 * @param order 订单
	 * @return true 有效 ; false 无效.
	 */
	private boolean check_valid(Orders order){
		if(order){
			//订单未被删除  订单未取消
			if(order.getStatus() == OrderStatus.有效.ordinal() ){
				return true
			}
		}
		return false;
	}
	
	/**
	 * 校验商品是否下架
	 * @param order
	 * @return true 商品处于上架状态  false 商品处于下架状态
	 */
	private boolean check_commodity(Orders order , def commodity){
		boolean result = false;
		if(order){
			
//			def commodity = commoditys().findOne($$("_id" : order.getProductId()) , $$("_id" : 1 , "is_shelves" : 1));
			//商品不为空 且 商品处于上架状态
			if(commodity != null && 1==commodity["is_shelves"]){
				result = true;
			}
			
		}
		return result;
	}
	
	/**
	 * 校验优惠券可用性
	 * @param discount 优惠券
	 * @return true:可用 false 不可用
	 */
	private boolean check_discount(DiscountUser discountUser){
		boolean result = false;
		if(discountUser){
			//优惠券使用起始时间
			Long beginTime = discountUser.getDiscountStartTime().getTime();
			//优惠券结束时间
			Long endTime = discountUser.getDiscountEndTime().getTime();
			//系统当前时间
			Long now = System.currentTimeMillis();
			
			if(beginTime <= now && now <= endTime && discountUser.getDiscount().getIsStop() == 0){
				result = true;
			}
			
		}
		return result;
	}
	
	/**
	 * 微信循环判断
	 * 订单支付页面-订单购买详情
	 */
	@ResponseBody
	@RequestMapping(value = "getOrderPayType/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "订单支付页面-订单购买详情-微信循环判断", httpMethod = "GET",  notes = "-1:未购买 0.未支付 1.待支付 ,2.支付成功")
	@ApiImplicitParams([
						@ApiImplicitParam(name = "order_id", value = "订单id", required = true, dataType = "String", paramType = "query")
						])
	def getOrderPayType(HttpServletRequest request){
		//-1:未购买 0.未支付 1.待支付 ,2.支付成功

		Integer order_id = ServletRequestUtils.getIntParameter(request , "order_id" , 0);
		if(order_id == null || order_id == 0){
			return getResultParamsError();
		}
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//订单信息
		Orders order = (Orders)sessionFactory.getCurrentSession().createCriteria(Orders.class)
				.add(Restrictions.eq(Orders.PROP_ID, order_id))
				.add(Restrictions.eq(Orders.PROP_STATUS, OrderStatus.有效.ordinal()))
				.uniqueResult();
		if(order){
			return getResultOK(order.getPayStatus());
		}
		return getResultParamsError();
	}
	
	
	
//	/**
//	 * 校验优惠券可用性
//	 * @param discount 优惠券
//	 * @return true:可用 false 不可用
//	 */
//	private boolean check_discount(Discount discount){
//		boolean result = false;
//		if(discount){
//			//优惠券使用起始时间
//			Long beginTime = discount.getStartTime().getTime();
//			//优惠券结束时间
//			Long endTime = discount.getEndTime().getTime();
//			//系统当前时间
//			Long now = System.currentTimeMillis();
//			
//			if(beginTime <= now && now <= endTime && discount.getIsStop() == 0){
//				result = true;
//			}
//			
//		}
//		return result;
//	}
	
	/**
	 * 优惠券优惠的金额
	 * @param discount  优惠券
	 * @param commodity 商品
	 * @return          优惠金额
	 */
	private Double getApplyMoneyByDiscount(Discount discount , Orders order){
		Double discount_money = 0;
		if(check_discount_commodity(discount , order)){
			discount_money = discount.getMoney();
		}
		return discount_money;
	}
	
	/**
	 * 校验:优惠券金额是否在商品限定的金额内
	 * @param discount  优惠券
	 * @param commodity 商品
	 * @return true:符合 false:不符合
	 */
	private boolean check_discount_commodity(Discount discount , Orders order){
		boolean result = false;
		//商品最大金额移植优惠券
		if(discount != null && order != null){
			//优惠券优惠金额
			Double discount_price = discount.getMoney();
			//商品最大优惠金额
			Double max_discount_money = discount.getEnoughMoney();
//			Double max_discount_money = commodity["max_discount_money"] as Double;
			
			if(discount_price <= max_discount_money && discount_price < order.getTotalMoney()){
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 用户是否已经下过该单
	 * @Description: 用户是否已经下过该单，原来购买商品的订单取消了才可以下单
	 * @date 2016年3月18日 下午3:15:34
	 * @param @return 可以创建订单:true 订单已经存在:false
	 */
	def isCreateOrder(String commodity_id , Integer user_id){
		boolean result = true;
		
		if(StringUtils.isNoneBlank(commodity_id)){
			Orders order = (Orders)sessionFactory.getCurrentSession().createCriteria(Orders.class)
				.add(Restrictions.eq(Orders.PROP_PRODUCTID , commodity_id))//商品id
				.add(Restrictions.eq(Orders.PROP_USERID , user_id))//用户id
				.add(Restrictions.eq(Orders.PROP_STATUS , OrderStatus.有效.ordinal()))//是否删除
				.uniqueResult();
				
			if(order != null){
				result = false;
			}
		}
		
		return result;
	}
	
	
	/**
	 * 我的订单
	 * @Description: 我的订单
	 * @date 2016年3月16日 上午10:48:22
	 */
	@ResponseBody
	@RequestMapping(value = "my_order_list/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "我的订单", httpMethod = "GET",  notes = "我的订单", response = OrderListVO.class)
	@ApiImplicitParams([@ApiImplicitParam(name = "size", value = "", required = false, dataType = "long", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "", required = false, dataType = "long", paramType = "query"),
		@ApiImplicitParam(name = "order_type", value = "0.待支付 1.支付成功 ,2.支付失败", required = true, dataType = "int", paramType = "query")])
	@TypeChecked(TypeCheckingMode.SKIP)
	def my_order_list(
		HttpServletRequest request){
		
		Integer user_id = Web.getCurrentUserId();
		int size = ServletRequestUtils.getIntParameter(request , "size" , 15);
		int page = ServletRequestUtils.getIntParameter(request , "page" , 1);
		int order_type = ServletRequestUtils.getIntParameter(request , "order_type" , 0);
		
		
		if(order_type == 0)
		{
		   return  get_fail_order(page,size,user_id);
		}else if(order_type == 2){
			return  get_cancle_order(page,size,user_id);
		}
		else
		{
			return get_success_order(user_id);
		}
		
		
	}
		
	@TypeChecked(TypeCheckingMode.SKIP)
   def get_fail_order(Integer page,Integer size,Integer user_id)
   {
	   
	   List<OrderListVOData> resultlist = new ArrayList();
	   
	   List orderList = sessionFactory
	   .getCurrentSession().createCriteria(Orders.class)
	   .add(Restrictions.eq(Orders.PROP_USERID , user_id))
	   .add(Restrictions.eq(Orders.PROP_STATUS , OrderStatus.有效.ordinal()))
	   .add(Restrictions.or(
		   Restrictions.eq(Orders.PROP_PAYSTATUS , OrderPayStatus.待支付.ordinal()) ,
		   Restrictions.eq(Orders.PROP_PAYSTATUS , OrderPayStatus.未支付.ordinal())
		   ))
	   .addOrder(Order.desc(Orders.PROP_CREATETIME))
	   .setFirstResult((page-1)*size)
	   .setMaxResults(size)
	   .list();
	   
	   int count = (int)sessionFactory
	   .getCurrentSession().createCriteria(Orders.class)
	   .setProjection(Projections.count(Orders.PROP_ID))
	   .add(Restrictions.eq(Orders.PROP_USERID, user_id))
	   .uniqueResult();
	   
	   
	   int allpage = count / size + count% size >0 ? 1 : 0;
	   
	   
	   for(int i=0;i<orderList.size();i++)
	   {
		   OrderListVOData  orderdata = new OrderListVOData();
		   
		   Orders orderitem = orderList.get(i);
		   orderdata._id = orderitem.getId();
		   
		   orderdata.order_id = orderitem.getOrderno();
		   orderdata.item_name = orderitem.getOrderName();
		   orderdata.item_picurl =orderitem.getPic();
		   orderdata.create_time = orderitem.getCreateTime()?.getTime();
		   orderdata.create_time_text = orderdata.GetStringDate();
		   orderdata.pay_type = OrderPayType.在线支付.ordinal();
		   orderdata.pay_status = orderitem.getPayStatus();
		   orderdata.item_id = orderitem.getProductId();
		   if(orderdata.pay_status == OrderPayStatus.待支付.ordinal())
		   {
			  orderdata.pay_status_text = OrderPayStatus.待支付.name();
		   }
		   else
		   {
			   orderdata.pay_status_text = OrderPayStatus.未支付.name();
		   }
		   orderdata.user_id = orderitem.getUserId();
		   orderdata.price = orderitem.getPayMoney();
		   
		   resultlist.add(orderdata);
	   }
	   
	   return getResultOK(resultlist, allpage, count , page , size);
   }
   
	@TypeChecked(TypeCheckingMode.SKIP)
	def get_cancle_order(Integer page,Integer size,Integer user_id)
	{
		
		List<OrderListVOData> resultlist = new ArrayList();
		
		List orderList = sessionFactory
				.getCurrentSession().createCriteria(Orders.class)
				.add(Restrictions.eq(Orders.PROP_USERID , user_id))
				.add(Restrictions.eq(Orders.PROP_STATUS , OrderStatus.取消.ordinal()))
				.addOrder(Order.desc(Orders.PROP_CREATETIME))
				.setFirstResult((page-1)*size)
				.setMaxResults(size)
				.list();
		
		int count = (int)sessionFactory
				.getCurrentSession().createCriteria(Orders.class)
				.setProjection(Projections.count(Orders.PROP_ID))
				.add(Restrictions.eq(Orders.PROP_USERID, user_id))
				.uniqueResult();
		
		
		int allpage = count / size + count% size >0 ? 1 : 0;
		
		
		for(int i=0;i<orderList.size();i++)
		{
			OrderListVOData  orderdata = new OrderListVOData();
			
			Orders orderitem = orderList.get(i);
			orderdata._id = orderitem.getId();
			
			orderdata.order_id = orderitem.getOrderno();
			orderdata.item_name = orderitem.getOrderName();
			orderdata.item_picurl =orderitem.getPic();
			orderdata.create_time = orderitem.getCreateTime()?.getTime();
			orderdata.create_time_text = orderdata.GetStringDate();
			orderdata.item_id = orderitem.getProductId();
			orderdata.pay_type = OrderPayType.在线支付.ordinal();
			 orderdata.pay_status = orderitem.getPayStatus();
			 orderdata.pay_status_text = OrderPayStatus.未支付.name();
			 if(orderdata.pay_status == OrderPayStatus.待支付.ordinal())
			 {
				orderdata.pay_status_text = OrderPayStatus.待支付.name();
			 }
			 else
			 {
				 orderdata.pay_status_text = OrderPayStatus.未支付.name();
			 }
			orderdata.user_id = orderitem.getUserId();
			orderdata.price = orderitem.getPayMoney();
			
			resultlist.add(orderdata);
		}
		
		return getResultOK(resultlist, allpage, count , page , size);
	}
	
   @TypeChecked(TypeCheckingMode.SKIP)
   def get_success_order(Integer user_id){
		List<OrderListVOData> resultlist;
		Map<String , OrderListVOData> map = new HashMap<String , OrderListVOData>();
		//订单list
		List orderList = sessionFactory.getCurrentSession().createCriteria(Orders.class)
				.add(Restrictions.eq(Orders.PROP_USERID , user_id))
				.add(Restrictions.eq(Orders.PROP_PAYSTATUS , OrderPayStatus.支付成功.ordinal()))
				.addOrder(Order.desc(Orders.PROP_CREATETIME))
				.setMaxResults(MAX_LIMIT)
				.list();

		if(orderList){
			orderList.each {Orders oItem->
				OrderListVOData  orderdata = new OrderListVOData();
				String orderNo = oItem.getOrderno();
				orderdata._id = oItem.getId();
				//订单号
				orderdata.order_id = oItem.getOrderno();
				orderdata.item_name = oItem.getOrderName();
				orderdata.item_picurl = oItem.getPic();
				orderdata.create_time = oItem.getCreateTime()?.getTime();
				orderdata.create_time_text = orderdata.GetStringDate();
				orderdata.item_id = oItem.getProductId();
				orderdata.pay_type = oItem.getPayType();
				orderdata.pay_status = oItem.getPayStatus();
				orderdata.pay_status_text = OrderPayStatus.支付成功.name();
				orderdata.price = oItem.getPayMoney();
				orderdata.source = 1;
				
				map.put(orderNo, orderdata);
			}
		}
		
		//报名表list
		def query = $$("user_id" : user_id );
		
		def signList = signs().find(
				query,
				$$("_id" : 1 ,"order_num" : 1 , "order_id" : 1 , "price" : 1 , "user_id" : 1  , "user_name" : 1 , "create_time" : 1)
				).sort($$("create_time" : -1))limit(MAX_LIMIT)?.toArray();

		if(signList){
			signList.each { def dbo ->
				//订单号
				String orderNo = dbo["order_num"];
				if(StringUtils.isNotBlank(orderNo) && map.containsKey(orderNo)){
					OrderListVOData data = map.get(orderNo);
					data.source = 3;
				}else{
					OrderListVOData  orderdata = new OrderListVOData();
					
					orderdata._id = dbo["_id"];
					//订单号
					orderdata.order_id = dbo["order_num"];
					orderdata.item_name = dbo["class_type"];
					orderdata.item_picurl ="";
					orderdata.create_time = dbo["create_time"];
					orderdata.create_time_text = orderdata.GetStringDate();
					orderdata.item_id =dbo["nc_commodity_id"];
					
					orderdata.pay_type = dbo["pay_type"];
					orderdata.pay_status = OrderPayStatus.支付成功.ordinal();
					orderdata.pay_status_text = OrderPayStatus.支付成功.name();
					orderdata.price = dbo["price"];
					orderdata.source = 2;
					
					map.put(orderNo, orderdata);
				}
			}
		}
		
		//map values toArray
		resultlist = map.values().toArray();
		//排序
		if(resultlist){
			Collections.sort(resultlist);
		}
		

		return getResultOK(resultlist);
	}
   
   
	
}
