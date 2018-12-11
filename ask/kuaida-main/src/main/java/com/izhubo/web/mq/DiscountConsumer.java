package com.izhubo.web.mq;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.izhubo.model.DiscountTimeType;
import com.izhubo.model.DiscountUserIsUseType;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mysqldb.model.Discount;
import com.mysqldb.model.DiscountUser;
import com.rabbitmq.client.Channel;



public class DiscountConsumer extends BaseConsumer{


	com.izhubo.web.DiscountController discountController;
	
	public DiscountConsumer(MongoTemplate mainMongo, MongoTemplate qquserMongo,MongoTemplate logMongo, SessionFactory sessionFactory) {
		super(mainMongo , qquserMongo , logMongo , sessionFactory);
//		discountController = new com.izhubo.web.DiscountController();
//		discountController.mainMongo = mainMongo;
//		discountController.logMongo = logMongo;
//		discountController.sessionFactory = sessionFactory;
//		discountController.qquserMongo = qquserMongo;
		
	}

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("收到队列信息" + " [x] Received '" + message + "'");

		try {

			String body = new String(message.getBody(), "UTF-8");
			JSONObject JO = new JSONObject(body);
			
			Integer user_id = JO.getInt("user_id");
			Long time = JO.getLong("time");
			
//			discountController.registerDiscount(user_id, time);
			
			discount(user_id, time);

			channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
		} catch (Exception e) {
			System.out.println(e.toString());
			// 记录报错日志
			_logMongo.getCollection("discount_log_err").save(
					new BasicDBObject("msg", e.toString())
					.append("msgEro", e.toString())
					.append("timestamp", System.currentTimeMillis())
					.append("messages", message.getBody())
					);
//			 channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
		}

		// 发送应答

		// channel.basicReject(message.getMessageProperties().getDeliveryTag(),
		// true);

	}
	
	private void discount(Integer user_id , Long time){
		if(user_id != null && user_id > 0){
			DBObject users = _mainMongo.getCollection("users").findOne(new BasicDBObject("_id", user_id));
			if(users != null){
				String tuid = (String) users.get("tuid");
				DBObject qQUser = _qquserMongo.getCollection("qQUser").findOne(new BasicDBObject("tuid", tuid));
				if(qQUser != null){
					String phone = (String) qQUser.get("username");
					
					//领取优惠券
					getDiscountNotDays(phone );
						
					
				}
			}
			
			
		}
	}
	
	/**
	 * 获取系统优惠券id
	 * @param discount_map_id discount_map表的id
	 * @return 优惠券id
	 */
	private Integer getSysDiscountId(String discount_map_id){
		Integer discount_id = null;
		DBObject discount_map = _logMongo.getCollection("discount_map").findOne(new BasicDBObject("_id" , discount_map_id) , new BasicDBObject("discount_id" , 1));
		if(discount_map != null){
			Object obj = discount_map.get("discount_id");
			if(obj != null){
				discount_id = (Integer) obj ;
			}
		}
		return discount_id;
	}
	
	/**
	 * 领取用户优惠券 (没有每日领取数量限制)
	 * @param phone			电话号码
	 * @param discount_id	优惠券id
	 * @return				map[code: 70201 data:"优惠券已领取过"] ["code" : 1 ,"data" : "success"]
	 */
	private void getDiscountNotDays(String phone ){
		
		Integer discount_id = getSysDiscountId("A0001");
		Session session =  _sessionFactory.openSession();
		try {
			Long discount_count = (Long) session.createCriteria(DiscountUser.class)
					.setProjection(Projections.count(DiscountUser.PROP_ID))//统计数量
					.add(Restrictions.eq(DiscountUser.PROP_PHONE , phone))//电话号码
					.add(Restrictions.eq(DiscountUser.PROP_DISCOUNTID , discount_id))//优惠券id
					.uniqueResult();
			
			//如果用户没有领取过优惠券
			if(discount_count > 0){
				return;
			}
			
			//优惠券
			Discount di = (Discount)session.createCriteria(Discount.class)
								.add(Restrictions.eq(Discount.PROP_ID , discount_id))//优惠券id
								.add(Restrictions.eq(Discount.PROP_ISSTOP , 0))//是否停用 0.否 1.是
								.uniqueResult();
			
			if(di != null){
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
				ds.setDiscountEndTime(discountEndTime);
				//领取优惠券的生效日期
				ds.setDiscountStartTime(discountStartTime);
				
				//优惠券领取数量加一
				if(di.getGetNum() == null)
				{
					di.setGetNum(1);
				}
				else
				{
					Integer getNum = di.getGetNum();
			 		di.setGetNum(getNum++);
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
				
				//日志
				_logMongo.getCollection("discount_logs").save(
						new BasicDBObject("_id" , UUID.randomUUID().toString())
						.append( "phone" , phone )
						.append( "discount_id" , discount_id )
						.append( "timestamp" , System.currentTimeMillis())
						);
			}
		} catch (HibernateException e) {
			throw new RuntimeException(e);
		}finally{
			if(session != null && session.isConnected()){
				session.close();
			}
		}
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
}