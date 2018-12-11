package com.izhubo.admin.answer



import static com.izhubo.rest.common.doc.MongoKey.$setOnInsert
import static com.izhubo.rest.common.doc.MongoKey._id
import static com.izhubo.rest.common.doc.MongoKey.timestamp
import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.text.SimpleDateFormat

import javax.servlet.http.HttpServletRequest

import org.springframework.web.bind.ServletRequestUtils

import com.izhubo.admin.BaseController
import com.izhubo.admin.Web
import com.izhubo.model.NCUserState
import com.izhubo.model.TopicEvaluationType
import com.izhubo.model.TopicsType
import com.izhubo.model.UserType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.common.util.NumberUtil
import com.izhubo.rest.web.StaticSpring
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject



/**
 * 教师排序报表
 * @author 
 *
 */
@RestWithSession
class TeacherOrderReportController extends BaseController {

	public DBCollection users(){
		return mainMongo.getCollection("users");
	}
	
	public DBCollection attention(){
		return mainMongo.getCollection("attention");
	}

	public DBCollection topics(){
		return mainMongo.getCollection("topics");
	}

	public DBCollection reportdata_teacher_groupbytopicscount(){
		return logMongo.getCollection("reportdata_teacher_groupbytopicscount");
	}
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//一天的毫秒数
	private static final Long DAYM = 86400000L; //24*60*60*1000;
	
	/**
	 * 教师天排行
	 * @param req
	 * @return
	 */
	def teacherOrderDay(HttpServletRequest req){
//		生成渠道 0.定时器 1.管理后台
		int channle = ServletRequestUtils.getIntParameter(req , "channle" , 0);
		//操作用户id
		int create_user_id = Web.getCurrentUserId();
		
		asyncTeacherReportDay(channle, create_user_id);
		return OK();
		
	}
	
	/**
	 * 教师天排行
	 * @param req
	 * @return
	 */
	def teacherOrderDayTime(HttpServletRequest req){
//		生成渠道 0.定时器 1.管理后台
		int channle = ServletRequestUtils.getIntParameter(req , "channle" , 0);
		//操作用户id
		int create_user_id = 0;
		
		asyncTeacherReportDay(channle, create_user_id);
		return OK();
		
	}

	/**
	 * 开启线程-按天抢答问题报表
	 * @author Administrator
	 * channle:         生成渠道 0.定时器 1.管理后台
	 * create_user_id : 创建用户
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	private def asyncTeacherReportDay(final Integer channle ,final Integer create_user_id){
		StaticSpring.execute(
				new Runnable() {
					public void run() {

						teacherReportDay(channle , create_user_id);
					}
				}
				);
	}
	
	/**
	 * 按天抢答问题报表
	 * @author Administrator
	 * channle:         生成渠道 0.定时器 1.管理后台
	 * create_user_id : 创建用户
	 */
	private void teacherReportDay(Integer channle , Integer create_user_id){
		//当前时间
		Long now = System.currentTimeMillis();
		//当前时间
		String data_string=sdf.format(new java.util.Date(now));
		//操作用户id
//		int create_user_id = Web.getCurrentUserId();
		
		//时间
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		//今天零点时间
		Long maxTime = cal.getTimeInMillis();
		cal.add(Calendar.DATE , -1);
		//前一天零点时间
		Long minTime = cal.getTimeInMillis();
		
		
		//统计昨天抢答排名前10位教师
		def topic_report = topics().aggregate(
			$$('$match', [ "type":TopicsType.问题已结束.ordinal() , "race_time" : [ '$gte': minTime, '$lt': maxTime ] ]),
			$$('$project', ["teach_id": '$teach_id',count: '$count']),
			$$('$group', [_id: '$teach_id',count:[$sum: 1]]),
			$$('$sort', ["count" : -1]),
			$$('$limit', 10)
			).results().iterator();
		while (topic_report.hasNext()){
			def obj = topic_report.next();
			//教师id
			Integer teach_id = obj["_id"] as Integer;
			//教师基础信息
			def teacher = users().findOne(
											$$("_id" : teach_id , "priv" : UserType.主播.ordinal() , "status" : true) ,
											$$(
												"_id" : 1 , "nick_name" : 1 , "pic" : 1 ,
												"topic_evaluation_count" : 1 , "topic_count" : 1 , "vlevel" : 1
											   )
										  );
		    if(teacher){
		    	//被关注数量
		    	Long attention_count =  attention().count($$("source_tuid" : teach_id));
		    	//满意度
		    	String satisfaction = teacher_replay_end_percent(teach_id);
		    	def item = $$(
		    			"_id" : UUID.randomUUID().toString() , "user_id" : teach_id , "user_name" : teacher["nick_name"]?.toString(),
		    					"topic_total" : teacher["topic_evaluation_count"] , "topic_day_count" : obj["count"] , "timesatmp" : now,
		    					"channel" : channle , "create_user_id" : create_user_id , "satisfaction" : satisfaction ,
		    					"attention_count" : attention_count , "user_pic" : teacher["pic"]?.toString() , "vlevel" : teacher["vlevel"],
		    							"dr" : 0
		    			);
		    	//保存
		    	reportdata_teacher_groupbytopicscount().save(item);
			}
		}
	}
	
	
	//好评百分比
	@TypeChecked(TypeCheckingMode.SKIP)
	def teacher_replay_end_percent(Integer teacher_id){
		long y = teacher_replay_end_with_evaluation(teacher_id);
		Double z = 0;
		if(y != 0){
			z = teacher_replay_end_well(teacher_id) / y * 100;
		}
		return NumberUtil.formatDouble3(z , 0)  + "%";
	}
	
	//老师回答的问题-已结束-满意的数量
	def teacher_replay_end_well(Integer teacher_id){
		return topics().count($$("teach_id" : teacher_id ,"type" : TopicsType.问题已结束.ordinal()  ,"deleted" : false, "evaluation_type" : $$('$in' : [TopicEvaluationType.满意.ordinal() , TopicEvaluationType.非常满意.ordinal()]) ));
	}
	
	
	//老师回答的问题-已结束-并且已经评价的（未评价的不计入满意计算中）
	def teacher_replay_end_with_evaluation(Integer teacher_id){
		return topics().count($$("teach_id" : teacher_id , "type" : TopicsType.问题已结束.ordinal() , "deleted" : false, "evaluation_type": $$($ne : TopicEvaluationType.未评价.ordinal()) ));
	}
	

}

