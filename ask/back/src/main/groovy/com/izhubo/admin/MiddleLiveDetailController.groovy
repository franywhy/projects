package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$

import java.text.DateFormat
import java.text.SimpleDateFormat

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils
import org.springframework.web.bind.ServletRequestUtils
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import com.izhubo.rest.anno.RestWithSession
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject

/**
 * 中级直播课出勤明细
 * @author shihongjie
 *
 */
@RestWithSession
class MiddleLiveDetailController extends BaseController {
	
	DBCollection mid_live_log(){mainMongo.getCollection('mid_live_log')}
	DBCollection middle_daily_plan_users(){mainMongo.getCollection('middle_daily_plan_users')}
	DBCollection qQUser(){qquserMongo.getCollection('qQUser')}
	DBCollection users(){mainMongo.getCollection('users')}
	DBCollection constants(){mainMongo.getCollection('constants')}
	DBCollection class_plan(){mainMongo.getCollection('class_plan')}
	DBCollection plan_courses(){mainMongo.getCollection('plan_courses')}
	DBCollection mid_live_rep_log(){mainMongo.getCollection('mid_live_rep_log')}
	DBCollection live_detail_log(){mainMongo.getCollection('live_detail_log')}
	DBCollection replay_detail_log(){mainMongo.getCollection('replay_detail_log')}
	DBCollection mid_watch_detail_log(){mainMongo.getCollection('mid_watch_detail_log')}
	DBCollection mid_watch_sum_log(){mainMongo.getCollection('mid_watch_sum_log')}

//	live_detail_log().save($$(
//	"ClassNo" : ClassNo , "Operator" : Operator , "Affected" : Affected ,
//	"totalusernum" : totalusernum));


	def camidrecord(HttpServletRequest req){
		//String com_id = ServletRequestUtils.getStringParameter(req, "com_id");
		//String course_time_id = ServletRequestUtils.getStringParameter(req, "course_time_id");
		//caMidCoursePlayRecord(com_id,course_time_id);
		
		println "test3";
		//sumPlayRecord("1001A5100000002ARPCS");

	}



	/**
	 *
	 * @param com_id 课程id
	 * @param course_time_id  课时id
	 * @return
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def caMidCoursePlayRecord(String com_id,String course_time_id)
	{
		String live_id = "";
		List<DBObject> ls = constants().findOne().get("middle_course_liveinfo");
			ls.each {DBObject cc->
				if(cc.get("course_id").equals(com_id))
				{
					 live_id =cc.get("liveid");

				}
			}
		//写汇总计算逻辑 1，生成到新的一张表，记录课时，用户，观看时长  2，报表查出的时候，按照这个逻辑汇总，汇总的时候
		def planobj = plan_courses().findOne($$("nc_id":course_time_id));
		if(planobj)
		{
		long live_end_time_long =  planobj.get("live_end_time_long");
		long live_start_time_long = planobj.get("live_start_time_long");

		def ids = live_detail_log().distinct("Operator",$$("timelong":$$($gt:live_start_time_long,$lt:live_end_time_long)));
		//统计每一个学员观看的时长
		ids.each { item ->
			//1、找出所有进入的数据
			long watchtime = 0;
			def startlist = live_detail_log().find($$("timelong": $$($gt: live_start_time_long, $lt: live_end_time_long), "Operator": item, "Action": "101")).toArray();
			//2、针对每条进入的数据，找出对应退出的记录（）
			if (startlist) {
				startlist.each { sitem ->
					ArrayList recordlist = live_detail_log().find($$("timelong": $$($gt: sitem.get("timelong"), $lt: live_end_time_long), "Operator": item, "Action": $$($in: ["107", "110"]))).sort($$("timelong": 1)).toArray();
					if(recordlist.size()>0)
				    {
						//当前进入点和最近一次退出的点，当中的间隔时间，就是观看时长
						long wtime = recordlist.get(0).get("timelong") - sitem.get("timelong");
						watchtime += wtime;
					}

				}
			}
			//3、//存入数据库，记录课时，用户 总共观看时长

			Integer user_id = Integer.valueOf(item);
			if(user_id>1000000000)
			{
				user_id = user_id - 1000000000;
			}
			String phone = findUserPhoneById(user_id);
			mid_watch_detail_log().save($$("_id": user_id + course_time_id, "live_id": live_id, "course_time_id": course_time_id, "course_id": com_id, "watchtime": watchtime, "user_id": user_id,"phone":phone));

			//mid_watch_sum_log().save($$("_id": item + com_id, "live_id": live_id, "course_time_id": course_time_id, "course_id": com_id, "watchtime": watchtime, "user_id": item));
		}


		}






	}


	/**
	 *
	 * @param com_id 课程id
	 * @param course_time_id  课时id
	 * @return
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def sumPlayRecord(String com_id)
	{
		   //计算中级直播的观看时长
		   long alltime = 100000;



		def iter = mid_watch_detail_log().aggregate(
				$$('$match', [course_id:com_id]),
				new BasicDBObject('$project', [course_id: '$course_id',user_id:'$user_id',phone:'$phone']),
				$$('$group', [_id: [course_id: '$course_id',user_id:'$user_id',phone:'$phone'],count:[$sum: 1]]),
				new BasicDBObject('$sort', ["_id.user_id":-1])
		).results().iterator();

		while(iter.hasNext()){
			def finaObj = iter.next();
			def _id = finaObj.get("_id").get("course_id")+finaObj.get("_id").get("user_id");
			def course_id = finaObj.get("_id").get("course_id");
			def user_id = finaObj.get("_id").get("user_id");
			def phone = finaObj.get("_id").get("phone");
			def count = finaObj.get("_id").get("count");

			mid_watch_sum_log().save($$("_id": _id,  "course_id": course_id, "phone": phone, "watchtime": count, "user_id": user_id,"alltime":alltime));




		}

		//mid_watch_sum_log().save($$("_id": item + com_id, "live_id": live_id, "course_time_id": course_time_id, "course_id": com_id, "watchtime": watchtime, "user_id": item));



	}







	
	/**
	 * 中介排课计划列表
	 */
	def midCourseList(HttpServletRequest req){
		def middle_course_ids = constants().findOne()?.get("middle_course_ids");
		List list = new ArrayList();
		if(middle_course_ids){
			def planList = class_plan().find(
				$$("nc_course_id" : $$('$in' : middle_course_ids)) , 
				$$("nc_id" : 1 , "name" : 1 , "course_name" : 1 , "class_time" : 1 , "nc_teacher_name" : 1)
				);
			if(planList){
				planList.each {def plan->
					def pcList = plan_courses().find(
						$$("nc_plan_id" : plan["nc_id"]) ,
						$$("content" : 1 , "nc_id" : 1 , "live_start_time" : 1)).sort($$("open_time": 1)
						).limit(1000).toArray();
					
					if(pcList){
						plan["courseList"] = pcList;
						list.add(plan);
					}
				}
			}
		}
		return ["code" : 1 , "data" : list];
	}
//	def midCourseList(HttpServletRequest req){
//		def middle_course_ids = constants().findOne()?.get("middle_course_ids");
//		List list = new ArrayList();
//		if(middle_course_ids){
//			middle_course_ids.each {String mid_course_id->
//			def plan = class_plan().findOne(
//					$$("nc_course_id" : mid_course_id) , 
//					$$("nc_id" : 1 , "name" : 1 , "course_name" : 1 , "class_time" : 1 , "nc_teacher_name" : 1)
//					);
//			if(plan){
//				def pcList = plan_courses().find(
//						$$("nc_plan_id" : plan["nc_id"]) , 
//						$$("content" : 1 , "nc_id" : 1)).sort($$("sort": 1)
//								).limit(1000).toArray();
//				
//				if(pcList){
//					plan["courseList"] = pcList;
//					list.add(plan);
//				}
//			}
//			}
//		}
//		return ["code" : 1 , "data" : list];
//	}
	
	def list(HttpServletRequest req){
//		用户手机
		String q_phone = ServletRequestUtils.getStringParameter(req, "phone");
		//日期(天)
//		String q_create_day = ServletRequestUtils.getStringParameter(req, "create_day");
		//课时ID
		String q_pid = ServletRequestUtils.getStringParameter(req, "pid");
		
		//最总返回的结果集
		List<Map> list = new ArrayList<Map>();
		
		if(StringUtils.isNotBlank(q_pid) ){
//			//今天凌晨
//			Long startDate = getStartDay(q_create_day);
//			//昨天凌晨
//			Long endDate = getEndDay(q_create_day);
			
			//查询条件 课时ID和天
			def query = $$("pid" : q_pid );
			//如果输入手机号码
			if(StringUtils.isNotBlank(q_phone)){
				query.append("phone", q_phone);
			}
			//总的出席记录
			List<String> phones = new ArrayList<String>();
			
			//-----------------------------------   排课计划出席记录   -----------------------------------------------------------------------
			//排课计划查询出来的用户
			List<DBObject> classPlanUserList = middle_daily_plan_users().find(query).limit(3000).toArray();
			if(classPlanUserList){
				for(DBObject obj : classPlanUserList){
					//手机号码
					String phone = obj["phone"];
					phones.add(phone);
					//课时ID
					String pid = obj["pid"];
					//第一次进入直播间时间
					Long timestamp = null;
					//第一次进入直播间记录
					DBObject dboLog=null;
					
					//最近一次回放的时间
					Long replytimestamp = null;
					//最近一次回放记录
					DBObject dborepyLog=null;
					def dboLogList = mid_live_log().find(
															$$(
																"phone" : phone , "pid" : pid //, 
//																"timestamp" : $$( '$gte' : startDate , '$lte' : endDate )
																)
															).sort($$("timestamp" : 1)
															).limit(1).toArray();
														
				
														
					def dbomidRepList = mid_live_rep_log().find(
															$$(
																"phone" : phone , "pid" : pid //,
//																"timestamp" : $$( '$gte' : startDate , '$lte' : endDate )
																)
															).sort($$("timestamp" : -1)
															).limit(1).toArray();
														
					if(dboLogList){
						dboLog = dboLogList.get(0);
					}
					if(dboLog){
//						第一次进入直播间时间
						timestamp = (Long)dboLog["timestamp"];
					}
					
					
					if(dbomidRepList){
						dborepyLog = dbomidRepList.get(0);
					}
					if(dborepyLog){
//						第一次进入直播间时间
						replytimestamp = (Long)dborepyLog["timestamp"];
					}
					
					Map map = new HashMap();
					map["phone"] = phone;
					map["pid"] = pid;
					map["timestamp"] = timestamp;
					map["replytimestamp"] = replytimestamp;
					map["socure"] = "排课";
					//根据用户手机号码查询用户昵称等信息
					def user = findUserByPhone(phone);
					if(user){
						map["nick_name"] = user["nick_name"];
						map["user_id"] = user["_id"];
						//map["watch_time"] = mid_watch_detail_log().findOne($$("phone":phone))?.get("watchtime");
						//增加观看时长watch_time的处理
						String diff_time_string = mid_watch_detail_log().findOne($$("phone":phone))?.get("watchtime")
						if(StringUtils.isNotBlank(diff_time_string)){
							long diff_time = Long.parseLong(diff_time_string)
							if(diff_time<60000){
								map["watch_time"] = "1分钟"
							}else{
								Integer mm_except = (Integer)(diff_time / 60000)
								long mm = mm_except * 60000
								map["watch_time"] = String.format("%s分钟", mm / 60000)
							}
						}else{
							map["watch_time"] = "-"
						}
					}
					list.add(map);
				}
			}
			
			//-----------------------------------   白名单出席记录   -----------------------------------------------------------------------
			//搜索条件没有指定某一个手机号码  或者 指定的手机号码不在排课计划中
			if(StringUtils.isBlank(q_phone)|| (StringUtils.isNotBlank(q_phone)&& phones.size() == 0)){
				BasicDBObject match = null;
				//搜索条件指定手机号码
				if(StringUtils.isBlank(q_phone)){
					match = $$('$match', [ "pid" : q_pid ,  "phone" : ['$nin' : phones] ]);
				}else if(StringUtils.isNotBlank(q_phone) && phones.size() == 0){//搜索条件指定了手机号码,但是没有在排课计划中
					match = $$('$match', [ "pid" : q_pid ,  "phone" : q_phone ]);
				}
				def midLiveList = mid_live_log().aggregate(
					match,
//					$$('$match', [ "pid" : q_pid , "phone" : ['$nin' : phones] ]),
					$$('$project', ["phone": '$phone',timestamp: '$timestamp']),
					$$('$group', [_id: '$phone',timestamp:[$min: '$timestamp']]),
					$$('$sort', ["timestamp" : 1]),
					$$('$limit', 1000)
					).results().iterator();
				while (midLiveList.hasNext()){
					def obj = midLiveList.next();
					//进入直播间时间
					Long timestamp = (Long)obj["timestamp"];
					//手机号码
					String phone = obj["_id"];
					
					Map map = new HashMap();
					map["phone"] = phone;
					map["pid"] = q_pid;
					map["timestamp"] = timestamp;
					map["socure"] = "白名单";
					
					//根据用户手机号码查询用户昵称等信息
					def user = findUserByPhone(phone);
					if(user){
						map["nick_name"] = user["nick_name"];
						map["user_id"] = user["_id"];
					}
					list.add(map);
				}
			}
			
			
			
			
			
		}
		return ["code" : 1 , "data" : list];
	}
//	def list(HttpServletRequest req){
////		用户手机
//		String q_phone = ServletRequestUtils.getStringParameter(req, "phone");
//		//日期(天)
//		String q_create_day = ServletRequestUtils.getStringParameter(req, "create_day");
//		//课时ID
//		String q_pid = ServletRequestUtils.getStringParameter(req, "pid");
//		
//		//最总返回的结果集
//		List<Map> list = new ArrayList<Map>();
//		
//		if(StringUtils.isNotBlank(q_pid) && StringUtils.isNotBlank(q_create_day) ){
//			//今天凌晨
//			Long startDate = getStartDay(q_create_day);
//			//昨天凌晨
//			Long endDate = getEndDay(q_create_day);
//			
//			//查询条件 课时ID和天
//			def query = $$("pid" : q_pid , "create_day" : q_create_day);
//			//如果输入手机号码
//			if(StringUtils.isNotBlank(q_phone)){
//				query.append("phone", q_phone);
//			}
//			//总的出席记录
//			List<String> phones = new ArrayList<String>();
//			
//			//-----------------------------------   排课计划出席记录   -----------------------------------------------------------------------
//			//排课计划查询出来的用户
//			List<DBObject> classPlanUserList = middle_daily_plan_users().find(query).limit(1000).toArray();
//			if(classPlanUserList){
//				for(DBObject obj : classPlanUserList){
//					//手机号码
//					String phone = obj["phone"];
//					phones.add(phone);
//					//课时ID
//					String pid = obj["pid"];
//					//第一次进入直播间时间
//					Long timestamp = null;
//					//第一次进入直播间记录
//					DBObject dboLog=null;
//					def dboLogList = mid_live_log().find(
//							$$(
//									"phone" : phone , "pid" : pid , 
//									"timestamp" : $$( '$gte' : startDate , '$lte' : endDate )
//									)
//							).sort($$("timestamp" : 1)
//									).limit(1).toArray();
//					
//					if(dboLogList){
//						dboLog = dboLogList.get(0);
//					}
//					if(dboLog){
////						第一次进入直播间时间
//						timestamp = (Long)dboLog["timestamp"];
//					}
//					
//					Map map = new HashMap();
//					map["phone"] = phone;
//					map["pid"] = pid;
//					map["timestamp"] = timestamp;
//					map["socure"] = "排课";
//					//根据用户手机号码查询用户昵称等信息
//					def user = findUserByPhone(phone);
//					if(user){
//						map["nick_name"] = user["nick_name"];
//						map["user_id"] = user["_id"];
//					}
//					list.add(map);
//				}
//			}
//			
//			//-----------------------------------   白名单出席记录   -----------------------------------------------------------------------
//			//搜索条件没有指定某一个手机号码  或者 指定的手机号码不在排课计划中
//			if(StringUtils.isBlank(q_phone)|| (StringUtils.isNotBlank(q_phone)&& phones.size() == 0)){
//				def match = null;
//				//搜索条件指定手机号码
//				if(StringUtils.isBlank(q_phone)){
//					match = $$('$match', [ "pid" : q_pid , "timestamp" : [ '$gte': startDate, '$lt': endDate ] , "phone" : ['$nin' : phones] ]);
//				}else if(StringUtils.isNotBlank(q_phone) && phones.size() == 0){//搜索条件指定了手机号码,但是没有在排课计划中
//					match = $$('$match', [ "pid" : q_pid , "timestamp" : [ '$gte': startDate, '$lt': endDate ] , "phone" : q_phone ]);
//				}
//				def midLiveList = mid_live_log().aggregate(
//						$$('$match', [ "pid" : q_pid , "timestamp" : [ '$gte': startDate, '$lt': endDate ] , "phone" : ['$nin' : phones] ]),
//						$$('$project', ["phone": '$phone',timestamp: '$timestamp']),
//						$$('$group', [_id: '$phone',timestamp:[$min: '$timestamp']]),
//						$$('$sort', ["timestamp" : 1]),
//						$$('$limit', 1000)
//						).results().iterator();
//				while (midLiveList.hasNext()){
//					def obj = midLiveList.next();
//					//进入直播间时间
//					Long timestamp = (Long)obj["timestamp"];
//					//手机号码
//					String phone = obj["phone"];
//					
//					Map map = new HashMap();
//					map["phone"] = phone;
//					map["pid"] = q_pid;
//					map["timestamp"] = timestamp;
//					map["socure"] = "白名单";
//					
//					//根据用户手机号码查询用户昵称等信息
//					def user = findUserByPhone(phone);
//					if(user){
//						map["nick_name"] = user["nick_name"];
//						map["user_id"] = user["_id"];
//					}
//					list.add(map);
//				}
//			}
//			
//			
//			
//			
//			
//		}
//		return ["code" : 1 , "data" : list];
//	}
	
	DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
	
	private Long getStartDay(String d){
		return fmt.parse(d).getTime();
	}
	private Long getEndDay(String d){
		Date date = fmt.parse(d);//取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
		return calendar.getTime().getTime();
	}
	
	
	private DBObject findUserByPhone(String phone){
		String tuid = qQUser().findOne($$("username" : phone))?.get("tuid");
		if(StringUtils.isNotBlank(tuid)){
			return users().findOne($$("tuid" : tuid));
		}	
		//return users().findOne($$("tuid" : qQUser().findOne($$("username" : phone))?.get("tuid")));
	}

	private String findUserPhoneById(Integer user_id){
		String tuid = users().findOne($$("_id" : user_id))?.get("tuid").toString();
		String phone = "";
		if(StringUtils.isNotBlank(tuid)){
			DBObject quser = qQUser().findOne($$("tuid" : tuid));
			if(quser)
			{
				phone = qQUser().findOne($$("tuid" : tuid)).get("username");
			}
		}
		return  phone;
		//return users().findOne($$("tuid" : qQUser().findOne($$("username" : phone))?.get("tuid")));
	}
}
