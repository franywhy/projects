package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.hqonline.model.UserIncomType
import com.izhubo.rest.anno.RestWithSession
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection

@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class UserIncomController extends BaseController {
	
	private static Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	/**	收支明细 */
	public DBCollection user_incom_log() {
		return logMongo.getCollection("user_incom_log");
	}
	
	public DBCollection topic_bunus() {
		return mainMongo.getCollection("topic_bunus");
	}
	
	/**
	 * 同步红包
	 * @return
	 */
	def syn_bunus(){
		Integer count = topic_bunus().count($$("open_type" : $$('$in' : [1 ,2]))).intValue();
		int size = 2000;
		def allpage = count / size + ((count% size) >0 ? 1 : 0);
		Integer type = UserIncomType.红包.ordinal();
		for(int page = 1 ; page <= allpage ; page++){
			def queryResult = topic_bunus().find($$("open_type" : $$('$in' : [1 ,2]))).sort($$("_id" : 1)).skip((page - 1) * size).limit(size).toArray();
			if(queryResult){
				queryResult.each {def dbo->
					Integer user_id = dbo["user_id"] as Integer;
					double money = dbo["mmoney"] as Double;
					String topic_id = dbo["topic_id"] as String;
					Long timestamp = dbo["open_time"] as Long;
					save(user_id, money, type, topic_id, timestamp , 0);
				}
			}
		}
	}
	
	
	/**
	 * 用户总收益
	 * @param user_id
	 * @return
	 */
	def myIncomAll(int user_id){
		Double money = 0d;
		Iterator iter  = user_incom_log().aggregate(
				$$('$match',["user_id" : user_id , "type" : $$('$in' : [
						UserIncomType.红包.ordinal() ,
						UserIncomType.打赏.ordinal()
					])]),
				$$('$project', [_id: '$user_id',money: '$money']),
				$$('$group', [_id: '$_id',money:[$sum: '$money']])
				).results().iterator();

		if(iter.hasNext()){
			def obj = iter.next()
			money = obj.get("money") as Double;
		}
		return new BigDecimal(money).setScale(0, BigDecimal.ROUND_HALF_UP);
	}
	
//	/**
//	 * 保存-业务新增
//	 * <b>
//	 * 调用场景:
//	 * 红包到账接口
//	 * 打赏到账接口
//	 * 批量支付到账接口
//	 * </b>
//	 * @param user_id	用户id
//	 * @param money		金额
//	 * @param type		类型
//	 * @param topic_id	问题id
//	 */
//	def save(Integer user_id , Double money , Integer type , String topic_id){
//		if(
//			null != user_id && user_id > 0 &&
//			null != money &&
//			null != type && type > -1 
//			){
//				user_incom_log().save($$(
//					"user_id" : user_id , "money" : money , 
//					 "type" : type , "topic_id" : topic_id,
//					 "timestamp" : System.currentTimeMillis()
//					));
//		}
//	}
	
	/**
	 * 保存-用于保存历史记录
	 * @param user_id	用户id
	 * @param money		金额
	 * @param timestamp	时间戳
	 * @param type		类型
	 * @param topic_id	问题id
	 */
	def save(Integer user_id , Double money  , Integer type , String topic_id , Long timestamp , Integer apply_id){
		if(
				null != user_id && user_id > 0 &&
				null != money &&
				null != timestamp && timestamp > 0 &&
				null != type && type > -1 &&
				user_incom_log().count($$("timestamp" : timestamp , "type" : type , "user_id" : user_id)) == 0
			){
				
				def m = getSaveMap(user_id , money  , type , topic_id , timestamp , apply_id);
				user_incom_log().save(m);
		}
		return true;
	}
	
	/**
	 * 获取保存收支明细的MAP
	 * @param user_id	用户id
	 * @param money		金额
	 * @param type		类型 @see com.hqonline.model.UserIncomType
	 * @param topic_id	问题ID
	 * @param timestamp 时间戳
	 * @param apply_id	申请ID
	 * @return			Map<String , Object>
	 */
	public BasicDBObject getSaveMap(Integer user_id , Double money  , Integer type , String topic_id , Long timestamp , Integer apply_id){
//		new BasicDBObject
		BasicDBObject map = new BasicDBObject();
		String id = null;
		if(type == UserIncomType.红包.ordinal()){
			id = UserIncomType.红包.ordinal() + "_" + topic_id + "_" + user_id;
		}else if(type == UserIncomType.打赏.ordinal()){
			id = UserIncomType.打赏.ordinal() + "_" + topic_id + "_" + user_id;
		}else if(type == UserIncomType.申请提现.ordinal()){
			id = UserIncomType.申请提现.ordinal() + "_" + apply_id + "_" + user_id;
		}else if(type == UserIncomType.提现失败.ordinal()){
			id = UserIncomType.提现失败.ordinal() + "_" + apply_id + "_" + user_id;
		}else{
			id = System.currentTimeMillis() + "";
		}
		
		map.put("_id", id);
		map.put("user_id", user_id);
		map.put("money", money);
		map.put("timestamp", timestamp);
		map.put("type", type);
		map.put("topic_id", topic_id);
		map.put("apply_id", apply_id);
		map.put("ts", System.currentTimeMillis());
		
		return map;
	}
	
}
