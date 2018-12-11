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
class MiddleLiveSumReportController extends BaseController {
	
	DBCollection mid_live_log(){mainMongo.getCollection('mid_live_log')}
	DBCollection middle_daily_plan_users(){mainMongo.getCollection('middle_daily_plan_users')}
	DBCollection qQUser(){qquserMongo.getCollection('qQUser')}
	DBCollection constants(){mainMongo.getCollection('constants')}
	DBCollection class_plan(){mainMongo.getCollection('class_plan')}
	DBCollection plan_courses(){mainMongo.getCollection('plan_courses')}
	DBCollection mid_live_rep_log(){mainMongo.getCollection('mid_live_rep_log')}
	DBCollection live_detail_log(){mainMongo.getCollection('live_detail_log')}
	DBCollection replay_detail_log(){mainMongo.getCollection('replay_detail_log')}
	DBCollection mid_watch_detail_log(){mainMongo.getCollection('mid_watch_detail_log')}


//	live_detail_log().save($$(
//	"ClassNo" : ClassNo , "Operator" : Operator , "Affected" : Affected ,
//	"totalusernum" : totalusernum));




	
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


	def findCount(List<Map> list,String phone)
	{
		int count = 0;
		for(int i=0;i<list.size();i++)
		{
			def item = list.get(i);
			if(phone.equals(item.get("phone")))
			{
				count =(int) item.get("count");
			}
		}
		return  count;
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	def list(HttpServletRequest req){
//		商品id
		String course_id = ServletRequestUtils.getStringParameter(req, "course_id");
		List<Map> list = new ArrayList<Map>();
		List<Map> livelist = new ArrayList<Map>();
		List<Map> replaylist = new ArrayList<Map>();
		if(StringUtils.isNotBlank(course_id) ){
			def result = mid_live_log().aggregate(
					$$('$group',[_id:["phone":'$phone',"pid":'$pid',"nc_course_id":'$nc_course_id'],count:[$sum: 1]] ),
					$$('$group',[_id:["_id.phone":'$_id.phone',"_id.nc_course_id":'$_id.nc_course_id'],count:[$sum: 1]] ),
					$$('$sort', ["count" : -1]),
					$$('$limit', 5000)
			).results().iterator();
			while (result.hasNext()){
				def finaObj = result.next();
				  String phone = finaObj.get("_id").get("_id.phone");
				  String ccode =  finaObj.get("_id").get("_id.nc_course_id");

				  if(course_id.equals(ccode))
				  {
					  def item = $$("course_id",ccode);
					  item["phone"] = phone;
					  item["count"] = finaObj.get("count");
					  livelist.push(item);

				  }

			}
			def result_replay = mid_live_rep_log().aggregate(
					$$('$group',[_id:["phone":'$phone',"pid":'$pid',"nc_course_id":'$nc_course_id'],count:[$sum: 1]] ),
					$$('$group',[_id:["_id.phone":'$_id.phone',"_id.nc_course_id":'$_id.nc_course_id'],count:[$sum: 1]] ),
					$$('$sort', ["count" : -1]),
					$$('$limit', 5000)
			).results().iterator();
			while (result_replay.hasNext()){
				def finaObj = result_replay.next();
				String phone = finaObj.get("_id").get("_id.phone");
				String ccode =  finaObj.get("_id").get("_id.nc_course_id");

				if(course_id.equals(ccode))
				{
					def item = $$("course_id",ccode);
					item["phone"] = phone;
					item["count"] = finaObj.get("count");
					replaylist.push(item);

				}

			}
			//拿最新生成的排课计划的学员为准
			def newobj = middle_daily_plan_users().find().sort($$("create_time_long":-1)).limit(1).toArray();
			if(newobj) {
				def query = $$("pid": newobj[0].get("pid"));

				List<DBObject> classPlanUserList = middle_daily_plan_users().find(query).limit(5000).toArray();
				if (classPlanUserList) {
					for (DBObject obj : classPlanUserList) {
						def item = $$("course_id", course_id);
						String phone = obj.get("phone");
						item.append("phone",phone);
						item.append("live_count",findCount(livelist, phone));
						item.append("reply_count",findCount(replaylist, phone));

						list.push(item);
					}
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
//}
	
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
}
