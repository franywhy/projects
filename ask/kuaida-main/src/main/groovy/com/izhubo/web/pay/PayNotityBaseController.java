package com.izhubo.web.pay;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.izhubo.model.AccScoreGainType;
import com.izhubo.model.OrderPayStatus;
import com.izhubo.model.OrderPreferenceScheme;
import com.izhubo.web.BaseController;
import com.izhubo.web.mq.MessageProductor;
import com.izhubo.web.score.ScoreBase;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mysqldb.model.Discount;
import com.mysqldb.model.DiscountUser;
import com.mysqldb.model.Orders;

/**
 * 支付回调
 * @author Administrator
 *
 */
public abstract class PayNotityBaseController extends BaseController {
	
	@Resource
	private SessionFactory sessionFactory;

	//锁
	private Lock lock = new ReentrantLock();
	
	/** 订单号 */
	protected String out_trade_no;
	/** 第三方交易号 */
	protected String trade_no;
	/** 参数备注信息 */
	protected String payCallBlackRemark;
	
	public String nodity(HttpServletRequest request , HttpServletResponse response){
		//锁
		lock.lock();
		try {
			if(nodityMethod(request , response)){
				paySuccessNodity();
				return successString();
			}else{
				payFailNodity();
				return failString();
			}
		} catch (Exception e) {
			payErr(e.getMessage());
			return failString();
		}finally{
			//显示释放锁
			lock.unlock();
		}
	}
	/** 回调处理业务 */
	public abstract boolean nodityMethod(HttpServletRequest request , HttpServletResponse response);
	
	/** 成功返回的信息 */
	public abstract String successString();
	/** 失败返回的信息 */
	public abstract String failString();
	/** 第三方回调名称 */
	public abstract String nodityName();
	
	/**
	 * 支付回调改写订单状态
	 * @param session				session
	 * @param out_trade_no			订单号
	 * @param trade_no				第三方交易号
	 * @param payCallBlackRemark 	参数备注信息
	 * @return
	 */
	protected void paySuccessNodity(){
		Session session = sessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		Orders order = (Orders) session.createCriteria(Orders.class).add(Restrictions.eq(Orders.PROP_ORDERNO, out_trade_no)).uniqueResult();
		//订单等待支付
		if(order.getPayStatus() != OrderPayStatus.支付成功.ordinal()){
			
			
			//支付状态修改
			order.setPayStatus(OrderPayStatus.支付成功.ordinal());
			//支付时间
			order.setPayCallblackTime(new Timestamp(System.currentTimeMillis()));
			//TODO 支付回调备注
			order.setPayCallblackRemark(payCallBlackRemark);
			//支付宝交易号
			order.setAlipayTradeNo(trade_no);
			//赠送积分
			try {
				DBObject commodity = commoditys().findOne(
						new BasicDBObject("_id" , order.getProductId()) , 
						new BasicDBObject("points" , 1) 
						);
				if(commodity != null && commodity.get("points") != null){
//					Double score = (Double) commodity.get("points");
					Double score = 0d;
					Object obj_points = commodity.get("points");
					if(obj_points != null){
						score = Double.valueOf(obj_points.toString());
					}
					order.setAddScore(score);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			savePayLogs("notify", new String[] {nodityName() , "支付成功" , out_trade_no , trade_no ,payCallBlackRemark});
			
			
			
			//开启事务
			session.update(order);
			
			//提交事务
			tran.commit();
			//刷新session缓存
			session.flush();
			//TODO 购买商品赠送积分
			addScore(order.getId());
			//TODO 订单同步NC
			pushOrders(order.getId());
			
		}else{
			savePayLogs("notify", new String[]{ nodityName() ,"支付成功失败,订单已经支付过了"  , out_trade_no , trade_no , payCallBlackRemark});
		}
	}
	/** 错误日志 */
	protected void payFailNodity(){
		savePayLogs("notify", new String[] {nodityName()  , "回调失败" , out_trade_no , trade_no  , payCallBlackRemark});
	}
	
	/** 异常日志 */
	protected void payErr(String msg){
		savePayLogs("notify", new String[] {nodityName()  , "回调异常" , out_trade_no , trade_no  , payCallBlackRemark , msg});
		
	}
	
	/**
	 * 赠送积分
	 * @param order_id 订单id
	 */
	protected void addScore(Integer order_id){
		Session session = sessionFactory.getCurrentSession();
		Orders order = (Orders) session.createCriteria(Orders.class)
						.add(Restrictions.eq(Orders.PROP_ID, order_id))
						.add(Restrictions.eq(Orders.PROP_PAYSTATUS, OrderPayStatus.支付成功.ordinal()))
						.uniqueResult();
		if(order != null && order.getAddScore() > 0){
			Integer user_id = order.getUserId();
			String nick_name = (String) users().findOne(new BasicDBObject("_id" , user_id) , new BasicDBObject("nick_name" , 1)).get("nick_name");
			//scoreBase.PushScoreMsg(order.getAddScore(), AccScoreGainType.购买商品赠送积分, user_id, nick_name);
		}
	}
	
	
	
	/**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓   订单同步到NC ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ ↓*/
	
	/**
	 * 订单支付成功后推送到消息队列
	 * @param order_id	订单id
	 */
	public void pushOrders(int order_id){
		//session
		Session session = sessionFactory.getCurrentSession();
		//订单
		Orders order = (Orders) session.createCriteria(Orders.class)
			.add(Restrictions.eq(Orders.PROP_ID, order_id))	//用户id
			.add(Restrictions.eq(Orders.PROP_PAYSTATUS, OrderPayStatus.支付成功.ordinal()))	//订单状态
			.uniqueResult();
		//订单非空
		if(order != null){
			//用户id
			Integer user_id = order.getUserId();
			//用户表需要查询的字段
			DBObject userShow = new BasicDBObject("nc_id" , 1);
			userShow.put("nick_name", 1);
			userShow.put("tuid", 1);
			//用户信息
			DBObject user = users().findOne(new BasicDBObject("_id" , user_id) , userShow);
			if(user != null){
				//优惠方式
				Integer preferencescheme = order.getPreferenceScheme();
				
				//用户nc_id 潜在学员编号
				String studentid = (String) user.get("nc_id");
				//学员名称
				String studentname = (String) user.get("nick_name");
				//报读班型
				String coursesequenceid = order.getNcProductId();
				//电话
				String dh = (String) qquserMongo.getCollection("qQUser").findOne(new BasicDBObject("tuid" , user.get("tuid")), new BasicDBObject("username" , 1)).get("username");
				//订单ID
				String orderid = order.getOrderno();
				//上课校区
//				String campusno = order.getSchoolcode();
//				String campusno = area().findOne(new BasicDBObject("code" , order.getSchoolcode()), new BasicDBObject("nc_id" , 1)).get("nc_id").toString();
				String campusno = order.getNcSchoolId();
//				String campusno = order.getSchoolcode();
				//报读院校
				String bdyx = "";
				//是否学历  Ｙ／Ｎ
				String isxueli = "N";
				
				
				//用户可用积分
				String userkyjf ="0";
				//积分可兑换金额
				String jfkdhje = "0";
				//本次使用积分
				String bcsyjf = order.getUseIntegral().toString();
				//本次积分兑换金额
				String bcjfdhje = order.getFavorableMoney().toString();
//				//积分兑换金额
//				String jfdhje = order.getFavorableMoney().toString();
				
//				结算方式（settlement） 1.支付宝PC 2.微信PC 3.快钱PC 4.支付宝网银PC 11.支付宝H5 12.微信H5 13.块钱H5
				String settlement = String.valueOf(order.getPayOnLineType());
//				收款账号（collectaccount）
				String collectaccount = PayConfig.getAccount(order.getPayOnLineType());
				
				
//				discountamount 		本次优惠金额 
				String discountamount = valueToString(order.getFavorableMoney());
//				promocode      		优惠码
				String promocode = "";
				if(preferencescheme == OrderPreferenceScheme.优惠券.ordinal() && order.getCouponId() != null){
//					Discount discount = (Discount) session.createCriteria(Discount.class)
//							.add(Restrictions.eq(Discount.PROP_ID, order.getCouponId()))	//用户id
//							.uniqueResult();
					Discount discount = null;
					Integer discountUserId = order.getCouponId();
					if(discountUserId != null){
						DiscountUser discountUser = (DiscountUser) session.createCriteria(DiscountUser.class)
								.add(Restrictions.eq(DiscountUser.PROP_ID, order.getCouponId()))	//用户id
								.uniqueResult();
						if(discountUser != null){
							discount = discountUser.getDiscount();
						}
					}
					if(discount != null){
						promocode = valueToString(discount.getCode());
					}
				}
//				preferentialtype        优惠类型	字段值：(优惠卷 、积分)
//				coupons			优惠券
				String preferentialtype = "";
				String coupons = "";
				if(preferencescheme == OrderPreferenceScheme.优惠券.ordinal()){
					preferentialtype = "优惠卷";
					coupons = valueToString(order.getFavorableMoney());
				}else if(preferencescheme == OrderPreferenceScheme.积分.ordinal()){
					preferentialtype = "积分";
				}
				
				try {
					//拼接json
					JSONObject jo = new JSONObject();
//					潜在学员编号（studentid）  			
					jo.put("studentid", studentid);
//					学员名称（studentname）                      
					jo.put("studentname", studentname);
//					报读班型（coursesequenceid）   
					jo.put("coursesequenceid", coursesequenceid);
//					电话(dh)					
					jo.put("dh", dh);
//					订单ID（orderid）			        
					jo.put("orderid", orderid);
//					上课校区（campusno）			        
					jo.put("campusno", campusno);
//					报读院校 （bdyx）
					jo.put("bdyx", bdyx);
//					是否学历 （isxueli）
					jo.put("isxueli", isxueli);
//
//					用户可用积分（userkyjf）
					jo.put("userkyjf", userkyjf);
//					积分可兑换金额（jfkdhje）
					jo.put("jfkdhje", jfkdhje);
//					本次使用积分（bcsyjf）
					jo.put("bcsyjf", bcsyjf);
//					本次积分兑换金额（bcjfdhje）
					jo.put("bcjfdhje", bcjfdhje);
//					//积分兑换金额（jfdhje）-暂时不要
//				报名表增加字段如下：
//					discountamount 		本次优惠金额 
					jo.put("discountamount", discountamount);
//					promocode      		优惠码
					jo.put("promocode", promocode);
//					preferentialtype        优惠类型	字段值：(优惠卷 、积分)
					jo.put("preferentialtype", preferentialtype);
//					coupons			优惠券
					jo.put("coupons", coupons);
//					结算方式（settlement） 1.支付宝PC 2.微信PC 3.快钱PC 4.支付宝网银PC 11.支付宝H5 12.微信H5 13.块钱H5
					jo.put("settlement", settlement);
//					收款账号（collectaccount）
					jo.put("collectaccount", collectaccount);
					//明细
					jo.put("details", priceDetails(order_id));
					
					//messageProductorService.pushToMessageQueue("rabbit_queue_hqonline_orders",  jo.toString());
					
				} catch (JSONException e) {
//					payErr(msg);
//					e.printStackTrace();
					ncJson(e.getMessage());
				}
				
			}

		}
	}
	
	
	/** 异常日志 */
	protected void ncJson(String msg){
		savePayLogs("rabbit_queue_hqonline_orders", new String[] {"create_json"  , msg});
	}
	
	/*报名表消息队列的json字段：

	潜在学员编号（studentid）  			
	学员名称（studentname）                      
	报读班型（coursesequenceid）   
	电话(dh)					
	订单ID（orderid）			        
	上课校区（campusno）			        
	报读院校 （bdyx）
	是否学历 （isxueli）

	用户可用积分（userkyjf）
	积分可兑换金额（jfkdhje）
	本次使用积分（bcsyjf）
	本次积分兑换金额（bcjfdhje）
	//积分兑换金额（jfdhje）-暂时不要
报名表增加字段如下：
	discountamount 		本次优惠金额 
	promocode      		优惠码
	preferentialtype        优惠类型	字段值：(优惠卷 、积分)
	coupons			优惠券


	明细字段：
	收支项目（pk_subjcode）
	标准价格（dcost）
	应收结算金额（dnshoulddcost）
	积分兑换金额（jfdhje）
	明细字段增加如下字段：
	xn			学年 		字段值：(无、第一学年、第二学年、第三学年、第四学年、)
	def14                   标准折扣额   

	格式：
	{
	  studentid: 		"OG20090805001",
	  studentname : 	"test",
	  coursesequenceid: 	"OG20090805001",
	  dh: 			"OG20090805001",
	  orderid : 		"test",
	  campusno: 		"OG20090805001",
	  bdyx :		 "test",
	  isxueli:		 "OG20090805001",
	  userkyjf :		 "test",
	  jfkdhje:		 "OG20090805001",
	  bcsyjf :		 "test",
	  bcjfdhje: 		  "OG20090805001",
	  jfdhje :		  "test"
	  details : [{pk_subjcode:"1",dcost:"aa",dnshoulddcost:"100"，jfdhje："100"},{pk_subjcode:"1",dcost:"aa",dnshoulddcost:"100"，jfdhje："100"}] // 明细
	};*/
	
	
	/**
	 * 获取价格明细
	 * @param order_id 订单id
	 */
	public List<Map<String , String>> priceDetails(Integer order_id){
		//结果集
		List<Map<String , String>> list = new ArrayList<Map<String , String>>();
		List<DBObject> itemList = null;
		
		//session
		Session session = sessionFactory.getCurrentSession();
		//订单
		Orders order = (Orders) session.createCriteria(Orders.class)
			.add(Restrictions.eq(Orders.PROP_ID, order_id))	//用户id
			.add(Restrictions.eq(Orders.PROP_PAYSTATUS, OrderPayStatus.支付成功.ordinal()))	//订单状态
			.uniqueResult();
		//校区nc_id
		String nc_school_id = order.getNcSchoolId();
		//商品NCid
		String nc_commodity_id = order.getNcProductId();
		
		//查询条件
		DBObject commodity_prices_query = new BasicDBObject("nc_school_id" , nc_school_id);
		commodity_prices_query.put("nc_commodity_id", nc_commodity_id);
		//执行查询
		itemList = find_commodity_area_price(nc_commodity_id, nc_school_id);
//		DBObject commodity_prices = mainMongo.getCollection("commodity_prices").findOne(commodity_prices_query , commodity_prices_show);
		
		if(itemList != null){
			
//			优惠方案0.不使用优惠 1.积分 2.优惠券
			int preference_scheme = order.getPreferenceScheme();
			//优惠金额
			Double favorable_money = order.getFavorableMoney();
			switch (preference_scheme) {
			case 0:
				for (int i = 0; i < itemList.size(); i++) {
					DBObject item = itemList.get(i);
					//优惠金额
					item.put("s_money", 0d);
					//使用积分
//					item.put("s_score", use_integral);
					//支付金额
					Double normalPrice = Double.valueOf(item.get("normalPrice").toString()) ;
					item.put("s_paymoney", normalPrice);
				}
				break;
			case 1:
				//使用积分
				Integer use_integral = order.getUseIntegral();
				//使用了积分和优惠金额
				if(favorable_money > 0 && use_integral > 0){
					for (int i = 0; i < itemList.size(); i++) {
						DBObject item = itemList.get(i);
						
						Double normalPrice = Double.valueOf(item.get("normalPrice").toString()) ;
						//价格item的价格大于优惠价格
						if(normalPrice >= favorable_money){
							//优惠金额
							item.put("s_money", favorable_money);
							//使用积分
							item.put("s_score", use_integral);
							//支付金额
							item.put("s_paymoney", sub(normalPrice , favorable_money));
							break;
						}else{//如果优惠价格大于价格item
							//优惠价格抵消item的价格后剩余的优惠价格
							favorable_money = sub(favorable_money , normalPrice);
							//本次优惠的积分
							Integer item_use_integral = 0;
							
							use_integral = use_integral - item_use_integral;
							//优惠金额
							item.put("s_money", normalPrice);
							//使用积分
							item.put("s_score", item_use_integral);
							//支付金额
							item.put("s_paymoney", 0);
						}
					}
				}
				
				break;
				
			case 2:
				for (int i = 0; i < itemList.size(); i++) {
					DBObject item = itemList.get(i);
					Double normalPrice = (Double) item.get("normalPrice");
					//价格item的价格大于优惠价格
					if(normalPrice >= favorable_money){
						//优惠金额
						item.put("s_money", favorable_money);
						//支付金额
						item.put("s_paymoney", sub(normalPrice , favorable_money));
						break;
					}else{//如果优惠价格大于价格item
						//优惠价格抵消item的价格后剩余的优惠价格
						favorable_money = sub(favorable_money , normalPrice);
						//优惠金额
						item.put("s_money", normalPrice);
						//支付金额
						item.put("s_paymoney", 0);
					}
				}
				
				break;
				
			default:
				break;
			}
		}
		//优惠条目
		if(itemList != null){
			for (int i = 0; i < itemList.size(); i++) {
				DBObject item = itemList.get(i);
				Map<String , String> map = new HashMap<String , String>();
//				收支项目（pk_subjcode）
				map.put("pk_subjcode", valueToString(item.get("code")));
//				标准价格（dcost）
				map.put("dcost", valueToString(item.get("normalPrice")));
//				应收结算金额（dnshoulddcost）
				map.put("dnshoulddcost", valueToString(item.get("s_paymoney") , "0"));
//				积分兑换金额（jfdhje）
				map.put("jfdhje", valueToString(item.get("s_score") , "0"));
//				明细字段增加如下字段：
//				xn			学年 		字段值：(无、第一学年、第二学年、第三学年、第四学年、)
				map.put("xn", "无");
//				def14                   标准折扣额   
//				map.put("def14", valueToString(item.get("s_money") , "0"));
				list.add(map);
			}
		}
		
		return list;
	}
	
	
	/**↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑   订单同步到NC ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ ↑*/
	
	
	
	/**
	 * 获取商品校区明细
	 * @date 2016年3月10日 下午3:06:53 
	 * @param @param nc_commodity_id	商品NCid
	 * @param @param nc_school_pk		校区NCid	
	 */
	private List<DBObject> find_commodity_area_price(String nc_commodity_id , String nc_school_pk){
		List<DBObject> list =  null;
		if(StringUtils.isNotBlank(nc_commodity_id) && StringUtils.isNotBlank(nc_school_pk)){
			//校区的nc_id
			String nc_id = nc_school_pk;
			
			//价格获取最多有5层 从底层往上找价格 顺序分别是:校区-城市-省-大区-上海恒企
			for(int i = 0 ; i < 5 ; i++){
				BasicDBObject query = new BasicDBObject("nc_school_pk" , nc_id);
				query.append("nc_commodity_id" , nc_commodity_id);
				//商品价格信息
				DBObject commodity_prices = mainMongo.getCollection("commodity_prices").findOne(query , new BasicDBObject("itemList" , 1));
				//判断是否拿到价格
				if(commodity_prices != null){
					list = (List<DBObject>) commodity_prices.get("itemList");
					break;
				}
				//如果没有获取到价格,获取父级nc_id
				
				nc_id = getAreaParentNcId(nc_id);
				//如果i=0 上面查到的是公司的层级 在查询一次获取到市的nc_id
				if(i == 0){
					nc_id = getAreaParentNcId(nc_id);
				}
			}
			
			
		}
		return list;
	}	
	
	/**
	 * 获取业务单元父NCid
	 * @param nc_id	业务单元NCid
	 * @return		父业务单元NCid
	 */
	private String getAreaParentNcId(String nc_id){
		String parent_nc_id = null;
		DBObject area = area().findOne(new BasicDBObject("nc_id" , nc_id),new BasicDBObject("parent_nc_id" , 1));
		if(area != null){
			parent_nc_id = (String) area.get("parent_nc_id");
		}
		return parent_nc_id;		
	}

	public String valueToString(Object val){
		return valueToString(val , "");
	}
	
	public String valueToString(Object val,String defaultVal){
		if(val == null){
			return defaultVal;
		}
		return String.valueOf(val);
	}
	
	 /**
	  * 提供精确的减法运算。
	  * 
	  * @param v1
	  *            被减数
	  * @param v2
	  *            减数
	  * @return 两个参数的差
	  */
	 public static double sub(double v1, double v2) {
	  BigDecimal b1 = new BigDecimal(Double.toString(v1));
	  BigDecimal b2 = new BigDecimal(Double.toString(v2));
	  return b1.subtract(b2).doubleValue();
	 }
	
	
	
}
