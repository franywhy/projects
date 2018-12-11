package com.izhubo.web.checkin;


import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import java.text.SimpleDateFormat
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.commons.lang.StringUtils
import org.hibernate.SessionFactory
import org.json.JSONObject
import org.springframework.stereotype.Controller
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.model.CheckStatus
import com.izhubo.model.Code
import com.izhubo.model.DR
import com.izhubo.rest.AppProperties
import com.izhubo.web.BaseController
import com.izhubo.web.api.Web
import com.izhubo.web.mq.MessageProductor
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection


/**
 * 打卡考勤接口（不需要登录）
 * @ClassName: CheckInNoLoginController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 赵琨
 * @date 2016年4月12日 上午9:47:16
 *
 */
@Controller
class CheckInNoLoginController extends BaseController {

	public DBCollection attendance_students() {
		return mainMongo.getCollection("attendance_students");
	}
	public DBCollection attendances() {
		return mainMongo.getCollection("attendances");
	}
	public DBCollection class_plan() {
		return mainMongo.getCollection("class_plan");
	}
	public DBCollection plan_student() {
		return mainMongo.getCollection("plan_student");
	}

	public DBCollection student_checkin_log() {
		return logMongo.getCollection("student_checkin_log");
	}
	public DBCollection users() {
		return mainMongo.getCollection("users");
	}
	public DBCollection plan_courses() {
		return mainMongo.getCollection("plan_courses");
	}
	public DBCollection mid_live_rep_log() {
		return mainMongo.getCollection("mid_live_rep_log");
	}
	
	public DBCollection mid_live_log() {
		return mainMongo.getCollection("mid_live_log");
	}
	public DBCollection live_detail_log() {
		return mainMongo.getCollection("live_detail_log");
	}

	public DBCollection replay_detail_log() {
		return mainMongo.getCollection("replay_detail_log");
	}
	

	
	DBCollection qQUser(){qquserMongo.getCollection('qQUser')}
	@Resource
	private MessageProductor messageProductorService;




	@Resource
	private SessionFactory sessionFactory;

	private String DakaqueueName = AppProperties.get("queue.daka").toString();


	private static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static String formateyyyyMMddhhmmss(){
		return yyyyMMdd.format(new Date());
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private String formate(){
		return sdf.format(new Date());
	}
	private Date Dateformate(){

		Date date = sdf.parse(formate());
		return   date ;
	}

	/**
	 * 当天的开始时间
	 * @return
	 */
	public static long startOfTodDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date=calendar.getTime();
		return date.getTime();
	}
	/**
	 * 当天的结束时间
	 * @return
	 */
	public static long endOfTodDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		Date date=calendar.getTime();
		return date.getTime();
	}
	/**
	 * 昨天的开始时间
	 * @return
	 */
	public static long startOfyesterday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date=calendar.getTime();
		return date.getTime();
	}
	/**
	 * 昨天的结束时间
	 * @return
	 */
	public static long endOfyesterday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		calendar.add(Calendar.DATE, -1);
		Date date=calendar.getTime();
		return date.getTime();
	}


	public static long startOfDayLong(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DATE, day);
		Date date=calendar.getTime();
		return date.getTime();
	}



	public Map getResultOKDefine(Object data , Integer all_page , Long count , Integer page ,Integer size,int checked_cout, Long all_check_count ){
		Map map = new HashMap();
		map.put("code", Code.OK);
		map.put("msg", Code.OK_S);
		map.put("data", data);
		map.put("all_page", all_page);
		map.put("count", count);
		map.put("page", page);
		map.put("size", size);
		map.put("checked_cout", checked_cout);
		map.put("all_check_count", all_check_count);
		return map;
	}









	def getClassData(String nc_attendance_id) {
		def main = attendances().findOne($$("nc_id":nc_attendance_id));

		String planid =main.get("nc_class_plan");


		//校区 教室  开班时间 时间说明 教师姓名 课程名称 章节 说明

		BasicDBObject result = $$("school_name":main.get("nc_school_name"));
		result.put("classroom", main.get("room"));
		result.put("open_time", main.get("attendance_time"));
		result.put("attendance_time_long", main.get("attendance_time_long"));

		def plan = class_plan().findOne($$("nc_id":planid));
		if(plan !=null)
		{
			result.put("open_time_text", plan.get("name"));

		}

		result.put("teacher_name", main.get("nc_teacher_name"));
		result.put("course_name", main.get("nc_course_name"));
		result.put("chapter", main.get("content"));

		return result;
	}





	def save_student_checkin_log(String uc_user_id,String id,float longitude,float latitude,String shoolname,String classroom,String coures_name,int resultState,String resultstring)
	{
		def query = $$("uc_user_id":uc_user_id);
		query.append("id", id);
		query.append("longitude", longitude);
		query.append("latitude", latitude);
		query.append("shoolname", shoolname);
		query.append("classroom", classroom);
		query.append("coures_name", coures_name);
		query.append("check_in_time", System.currentTimeMillis());

		query.append("resultState", resultState);
		query.append("resultstring", resultstring);

		student_checkin_log().save(query);

	}
	
	

	//type = 0 今天 type = -1 昨天
	def getTeacherTodayOrYestodayCheck(String teacherid,int type)
	{


		def query;

		if(type == 0) {
			query =	$$("nc_teacher_id":teacherid,"dr" : DR.正常.ordinal());
			query.put("attendance_time_long", $$('$gte' : startOfTodDay() , '$lte' : endOfTodDay()));
		}
		else
		{
			query =	$$("nc_teacher_id":teacherid,"dr" : DR.正常.ordinal());
			query.put("attendance_time_long", $$('$gte' : startOfyesterday() , '$lte' : endOfyesterday()));
		}


		def attendlist = attendances().find(query).sort($$("attendance_time_long":-1)).toArray();


		attendlist.each {BasicDBObject item->

			String planid =item.get("nc_class_plan");
			def plan = class_plan().findOne($$("nc_id":planid));

			BasicDBObject result = new BasicDBObject();
			result.put("nc_school_name", item.get("nc_school_name"));
			result.put("room", item.get("room"));
			result.put("open_time", item.get("attendance_time"));
			result.put("open_time_text", plan.get("name"));
			result.put("teacher_name", item.get("nc_teacher_name"));
			result.put("nc_course_name", item.get("nc_course_name"));
			result.put("content", item.get("content"));
			result.put("nc_id", item.get("nc_id"));


		}

		return 	attendlist;
	}
	
	
	//http://SiteUrl?ClassNo=*&Operator=*&Action=*& Affected=*&totalusernum=*
	
	@ResponseBody
	@TypeChecked(TypeCheckingMode.SKIP)
	def live_sync(
			HttpServletRequest request,HttpServletResponse res){
			
			String ClassNo = ServletRequestUtils.getStringParameter(request, "ClassNo");
			String Operator = ServletRequestUtils.getStringParameter(request, "Operator");
			String Affected = ServletRequestUtils.getStringParameter(request, "Affected");
		    String Action = ServletRequestUtils.getStringParameter(request, "Action");
			String totalusernum = ServletRequestUtils.getStringParameter(request, "totalusernum");
			live_detail_log().save($$(
					"ClassNo" : ClassNo , "Operator" : Operator , "Affected" : Affected ,"Action":Action,
					"totalusernum" : totalusernum,"timelong":System.currentTimeMillis()));
			
	}
	/**
	 *
	 * @param request
	 * @param res
	 * @return
	 */

	@ResponseBody
	@TypeChecked(TypeCheckingMode.SKIP)
	def replay_sync(
			HttpServletRequest request,HttpServletResponse res){
		String ClassNo = ServletRequestUtils.getStringParameter(request, "ClassNo");
		String Operator = ServletRequestUtils.getStringParameter(request, "Operator");
		String Action = ServletRequestUtils.getStringParameter(request, "Action");
		String totalusernum = ServletRequestUtils.getStringParameter(request, "totalusernum");
		replay_detail_log().save($$(
				"ClassNo" : ClassNo , "Operator" : Operator , "Action" : Action ,
				"totalusernum" : totalusernum,"timelong":System.currentTimeMillis()));

	}
	
	/**
	 * 学生打卡
	 * @Description: 我的订单
	 * @date 2016年3月16日 上午10:48:22
	 */

	@ResponseBody
	@TypeChecked(TypeCheckingMode.SKIP)
	def student_checkin_bytoken(
			HttpServletRequest request,HttpServletResponse res){


		String pid = ServletRequestUtils.getStringParameter(request, "pid");
		String access_token = ServletRequestUtils.getStringParameter(request, "access_token");
		String nc_user_id = "";
		
		Integer user_id = getUserIdByToken(access_token);
		String phone = getUserPhoneByUserId(user_id);
		
		save_live_log(phone,pid);

		def main = attendances().findOne($$("nc_plan_course":pid,"dr":0));
		String nc_id = "";
		if(main)
		{
			def item = attendance_students().findOne($$("phone":phone,"nc_attendance_id":main.get("nc_id")));
			if(item)
			{
				nc_id = item.get("nc_id");
				nc_user_id = item.get("nc_student_id");
			}

			String planid =main.get("nc_class_plan");
			String nc_school_code =main.get("nc_school_code");
			def plan = class_plan().findOne($$("nc_id":planid));


			//先判断时间是否符合要求.必须在开课时间的前后30分钟

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			//记录日志
			save_student_checkin_log(nc_user_id,nc_id,0,0,main.get("nc_school_name"),main.get("room"),main.get("nc_course_name"),CheckStatus.已打卡.ordinal(),"打卡成功");

			//更改签到状态

			if(attendance_students().count(new BasicDBObject("nc_id":nc_id).append("is_sign", CheckStatus.未打卡.ordinal()))>0)
			{

				attendance_students().update(
						new BasicDBObject("nc_id":nc_id),
						new BasicDBObject('$set':
						new BasicDBObject(
						"is_sign" : CheckStatus.已打卡.ordinal(),
						"sign_time":formateyyyyMMddhhmmss()
						)
						));


				JSONObject jsonrequest = new JSONObject();

				jsonrequest.put("ccheckbid", nc_id);
				jsonrequest.put("ccheckinid", item.get("nc_attendance_id"));
				jsonrequest.put("isarriv", "Y");
				jsonrequest.put("isarrivDate", formateyyyyMMddhhmmss());



				messageProductorService.pushToMessageQueue(DakaqueueName, jsonrequest.toString());
				return getResult(1,"打卡成功");
			}
			else
			{
				return getResult(-1,"已经打过卡了");
			}




		}



	}
			
			
			/**
			 * 学生打卡
			 * @Description: 我的订单
			 * @date 2016年3月16日 上午10:48:22
			 */
		
			@ResponseBody
			@TypeChecked(TypeCheckingMode.SKIP)
			def student_checkin_resend(
					HttpServletRequest request,HttpServletResponse res){
		
		
				String pid = ServletRequestUtils.getStringParameter(request, "pid");
	
				
				def main = attendances().findOne($$("nc_plan_course":pid,"dr":0));
				
				if(main)
				{
					def attendanceslist = attendance_students().find($$("is_sign":1,"nc_attendance_id":main.get("nc_id"))).toArray();
					if(attendanceslist)
					{
						attendanceslist.each {BasicDBObject item ->
							String nc_user_id = "";
							String nc_id = "";
							nc_id = item.get("nc_id");
							nc_user_id = item.get("nc_student_id");
							JSONObject jsonrequest = new JSONObject();
							
						 jsonrequest.put("ccheckbid", nc_id);
						 jsonrequest.put("ccheckinid", item.get("nc_attendance_id"));
						 jsonrequest.put("isarriv", "Y");
						 jsonrequest.put("isarrivDate",item.get("sign_time"));

						 messageProductorService.pushToMessageQueue(DakaqueueName, jsonrequest.toString());
						}
						
					}
		

		
					
						
					
					
		
		
					return getResult(1,"打卡成功");
		
				}
		
		
		
			}

	/**
	 * 学生打卡
	 * @Description: 我的订单
	 * @date 2016年3月16日 上午10:48:22
	 */

	@ResponseBody
	@TypeChecked(TypeCheckingMode.SKIP)
	def student_checkin(
			HttpServletRequest request,HttpServletResponse res){


		String pid = ServletRequestUtils.getStringParameter(request, "pid");
		String phone = ServletRequestUtils.getStringParameter(request, "phone");
		String nc_user_id = "";
		
		
		save_live_log(phone,pid);

		def main = attendances().findOne($$("nc_plan_course":pid,"dr":0));
		String nc_id = "";
		if(main)
		{
			def item = attendance_students().findOne($$("phone":phone,"nc_attendance_id":main.get("nc_id")));
			if(item)
			{
				nc_id = item.get("nc_id");
				nc_user_id = item.get("nc_student_id");
			}

			String planid =main.get("nc_class_plan");
			String nc_school_code =main.get("nc_school_code");
			def plan = class_plan().findOne($$("nc_id":planid));


			//先判断时间是否符合要求.必须在开课时间的前后30分钟

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			//记录日志
			save_student_checkin_log(nc_user_id,nc_id,0,0,main.get("nc_school_name"),main.get("room"),main.get("nc_course_name"),CheckStatus.已打卡.ordinal(),"打卡成功");

			//更改签到状态

			if(attendance_students().count(new BasicDBObject("nc_id":nc_id).append("is_sign", CheckStatus.未打卡.ordinal()))>0)
			{

				attendance_students().update(
						new BasicDBObject("nc_id":nc_id),
						new BasicDBObject('$set':
						new BasicDBObject(
						"is_sign" : CheckStatus.已打卡.ordinal(),
						"sign_time":formateyyyyMMddhhmmss()
						)
						));


				JSONObject jsonrequest = new JSONObject();

				jsonrequest.put("ccheckbid", nc_id);
				jsonrequest.put("ccheckinid", item.get("nc_attendance_id"));
				jsonrequest.put("isarriv", "Y");
				jsonrequest.put("isarrivDate", formateyyyyMMddhhmmss());



				messageProductorService.pushToMessageQueue(DakaqueueName, jsonrequest.toString());
				return getResult(1,"打卡成功");
			}
			else
			{
				return getResult(-1,"已经打过卡了");
			}




		}



	}
			
	 def save_live_log( String phone , String pid)
	 {
		 if(
			(
			 StringUtils.isNotBlank(phone)) &&
			 StringUtils.isNotBlank(pid)
			 ){
			 //用户昵称
			 String user_name = null;
			 //用户信息
			 Integer  user_id = users().findOne($$("tuid" : qQUser().findOne($$("username" : phone))?.get("tuid")))?.get("_id");
			 def user = users().findOne($$("_id" : user_id) , $$("nick_name" : 1));
			 if(user){
				 user_name = user.get("nick_name");
			 }
			 
			 //课时名称
			 String course_time_name = null;
			 //课程名称
			 String course_name = null;
			 //nc课程iD
			 String nc_course_id = null;
			 //排课ID
			 String nc_plan_id = null;
			 
			 def planCourse = plan_courses().findOne($$("nc_id" : pid) , $$("nc_plan_id" : 1 , "content" : 1));
			 if(planCourse){
				 course_time_name = planCourse.get("content");
				 nc_plan_id = planCourse.get("nc_plan_id");
			 }
			 
			 if(StringUtils.isNotBlank(nc_plan_id)){
				 def classPlan = class_plan().findOne($$("nc_id" : nc_plan_id) , $$("course_name" : 1 , "nc_course_id" : 1));
				 if(classPlan){
					 nc_course_id = classPlan.get("nc_course_id");
					 course_name = classPlan.get("course_name");
				 }
			 }
			 
			 
			 String nick_name = null;
			 def us = users().findOne($$("_id" : user_id) , $$("nick_name" : 1));
			 if(us){
				 nick_name = us.get("nick_name");
			 }
			 
			 if(
				 StringUtils.isNotBlank(course_time_name) && StringUtils.isNotBlank(user_name) &&
				 StringUtils.isNotBlank(nc_course_id) && StringUtils.isNotBlank(nc_plan_id)
				 ){
				 
				 //当前时间
				 Long timestamp = System.currentTimeMillis();
				 
				 
				 
				 mid_live_log().save($$(
						 "user_id" : user_id , "user_name" : user_name , "phone" : phone ,
						 "pid" : pid,
						 "course_time_name" : course_time_name ,
						 "nc_course_id" : nc_course_id,"course_name" : course_name,
						 "nc_plan_id" : nc_plan_id , "timestamp" : timestamp
						 ));
				 
				
			 }
			 
		 }
		 
		 
	 }
	
	/**
	 * 中级直播回放记录
	 * @param request  pid:课时ID  access_token:token  phone:手机号码
	 * @param res
	 * @return
	 */
	@ResponseBody
	def mid_live_rep_login_t(HttpServletRequest request,HttpServletResponse res){
		String pid = ServletRequestUtils.getStringParameter(request, "pid");
		String access_token = ServletRequestUtils.getStringParameter(request, "access_token");
		String phone = ServletRequestUtils.getStringParameter(request, "phone");
		if(StringUtils.isNotBlank(pid) &&(StringUtils.isNotBlank(access_token) || StringUtils.isNotBlank(phone))){
			//用户id
			Integer user_id = 0;
			
			if(StringUtils.isNotBlank(phone)){
				user_id = users().findOne($$("tuid" : qQUser().findOne($$("username" : phone.trim()))?.get("tuid")))?.get("_id");
			}
			
			if(user_id == 0 && StringUtils.isNotBlank(access_token)){
				user_id = getUserIdByToken(access_token);
				
				if(user_id > 0){
					phone = qQUser().findOne(
								$$("tuid" : users().findOne($$("_id" : user_id) , $$("tuid" : 1))?.get("tuid")),
								$$("username" : 1)
							)?.get("username");
				}
			}
			
			if(user_id > 0 && StringUtils.isNotBlank(phone.trim())){
				return mid_live_rep_log(user_id, phone.trim(), pid);
			}
		}
		return getResultParamsError();
	}
	
	/**
	 * 中级直播回放记录
	 * @param user_id	用户ID
	 * @param phone		用户手机号码
	 * @param pid		课时ID
	 * @return
	 */
	def Map mid_live_rep_log(Integer user_id , String phone , String pid){
//		//用户ID
//		Integer user_id = ServletRequestUtils.getIntParameter(request, "user_id");
//		//用户昵称
//		String user_name = ServletRequestUtils.getStringParameter(request, "user_name");
//		//手机号码
//		String mobile = ServletRequestUtils.getStringParameter(request, "mobile");
//		//课程ID
//		String course_id = ServletRequestUtils.getStringParameter(request, "course_id");
//		//PID
//		String pid = ServletRequestUtils.getStringParameter(request, "pid");
		
		if(
			null != user_id &&
//			StringUtils.isNotBlank(user_name) &&
			StringUtils.isNotBlank(phone) &&
			StringUtils.isNotBlank(pid)
			){
			//用户昵称
			String user_name = null;
			//用户信息
			def user = users().findOne($$("_id" : user_id) , $$("nick_name" : 1));
			if(user){
				user_name = user.get("nick_name");
			}
			
			//课时名称
			String course_time_name = null;
			//课程名称
			String course_name = null;
			//nc课程iD
			String nc_course_id = null;
			//排课ID
			String nc_plan_id = null;
			
			def planCourse = plan_courses().findOne($$("nc_id" : pid) , $$("nc_plan_id" : 1 , "content" : 1));
			if(planCourse){
				course_time_name = planCourse.get("content");
				nc_plan_id = planCourse.get("nc_plan_id");
			}
			
			if(StringUtils.isNotBlank(nc_plan_id)){
				def classPlan = class_plan().findOne($$("nc_id" : nc_plan_id) , $$("course_name" : 1 , "nc_course_id" : 1));
				if(classPlan){
					nc_course_id = classPlan.get("nc_course_id");
					course_name = classPlan.get("course_name");
				}
			}
			
			
			String nick_name = null;
			def us = users().findOne($$("_id" : user_id) , $$("nick_name" : 1));
			if(us){
				nick_name = us.get("nick_name");
			}
			
			if(
				StringUtils.isNotBlank(course_time_name) && StringUtils.isNotBlank(user_name) &&
				StringUtils.isNotBlank(nc_course_id) && StringUtils.isNotBlank(nc_plan_id)
				){
				
				//当前时间
				Long timestamp = System.currentTimeMillis();
				
				
				
				mid_live_rep_log().save($$(
						"user_id" : user_id , "user_name" : user_name , "phone" : phone ,
						"pid" : pid,
						"course_time_name" : course_time_name , 
						"nc_course_id" : nc_course_id,"course_name" : course_name,
						"nc_plan_id" : nc_plan_id , "timestamp" : timestamp 
						));
				
				return getResultOK();
			}
			
		}
		return getResultParamsError();
	}
			
			
			
			



}
