package com.izhubo.admin.answer

import static com.izhubo.rest.common.doc.MongoKey.$setOnInsert
import static com.izhubo.rest.common.doc.MongoKey._id
import static com.izhubo.rest.common.doc.MongoKey.timestamp
import static com.izhubo.rest.common.util.WebUtils.$$

import javax.servlet.http.HttpServletRequest;

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import com.izhubo.admin.BaseController
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.StaticSpring
import com.mongodb.DBCollection

/**
 * 问答红包每天数量和总金额报表 定时生成
 * @author Administrator
 *
 */
@RestWithSession
class TopicBunusReportController extends BaseController {
	//红包表
	public DBCollection topic_bunus(){
		return mainMongo.getCollection("topic_bunus");
	}
	public DBCollection topic_bunus_report(){
		return logMongo.getCollection("reportdata_topic_bunus");
	}
	public DBCollection topic_tip(){
		return mainMongo.getCollection("topic_tip");
	}
	
	
	
	def topicBunusReportDay(HttpServletRequest req){
		asyncTopicBunusReport();
		return OK();
		
	}
	
	
	
	@TypeChecked(TypeCheckingMode.SKIP)
	private def asyncTopicBunusReport(){
		StaticSpring.execute(
				new Runnable() {
					public void run() {

						topicBunusReport();
					}
				}
				);
	}
	
	private void topicBunusReport(){
		Long yesterday = getDate(-1);
		Long today = getDate(0);
		
		Iterator records = topic_bunus().aggregate(
													$$('$match' , ["create_time":[$gte:yesterday , $lt : today]]),
													$$('$group' , [_id : null , count:[$sum : 1] , money : [$sum : '$mmoney']])
													).results().iterator();
		Iterator tip_records = topic_tip().aggregate(
													$$('$match' , ["create_time":[$gte:yesterday , $lt : today]]),
													$$('$group' , [_id : null , count:[$sum : 1] , money : [$sum : '$kd_money']])
													).results().iterator();
		Integer bunus_count = 0;
		Double bunus_money = 0d;										
		if(records.hasNext()){
			def obj = records.next();
			if(obj){
				bunus_count = (obj.get("count") as Double).intValue();
				bunus_money = obj.get("money") as Double;
			}
		}
		
		Integer tip_count = 0;
		Double tip_money = 0d;
		if(tip_records.hasNext()){
			def obj = tip_records.next();
			if(obj){
				tip_count = (obj.get("count") as Double).intValue();
				tip_money = obj.get("money") as Double;
			}
		}
		
		topic_bunus_report().save($$("_id" : yesterday , "timestamp" : yesterday , "create_time" : System.currentTimeMillis() , "bunus_count" : bunus_count , "bunus_money" : bunus_money,"tip_count" : tip_count,"tip_money" : tip_money));
	}
	
	public Long getDate(int day){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		cal.add(Calendar.DATE , day);
		return cal.getTimeInMillis();
	}
}
