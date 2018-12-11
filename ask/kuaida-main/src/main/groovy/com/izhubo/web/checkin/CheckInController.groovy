package com.izhubo.web.checkin;


import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.text.SimpleDateFormat
import java.util.TreeMap.PrivateEntryIterator;

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.hibernate.SessionFactory
import org.json.JSONObject
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.model.AccScoreGainType
import com.izhubo.model.CheckStatus
import com.izhubo.model.Code
import com.izhubo.model.DR
import com.izhubo.rest.AppProperties;
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.web.BaseController
import com.izhubo.web.api.Web
import com.izhubo.web.mq.MessageProductor
import com.izhubo.web.school.SchoolController
import com.izhubo.web.score.ScoreBase
import com.izhubo.web.vo.UserWalletDetailListVO
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation


/**
 * 钱包api
 * @ClassName: UserController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 赵琨
 * @date 2016年4月12日 上午9:47:16
 *
 */
@RestWithSession
@RequestMapping("/checkin")
class CheckInController extends BaseController {

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

	@Resource
	private MessageProductor messageProductorService;


	@Resource
	private SchoolController schoolController;

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



	/**
	 * 获取学员考勤
	 * @Description: 我的订单
	 * @date 2016年3月16日 上午10:48:22
	 */

	@ResponseBody
	@RequestMapping(value = "get_student_checkdata/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取学员考勤", httpMethod = "GET",  notes = "获取学员考勤列表",response =UserWalletDetailListVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "size", value = "页大小", required = false, dataType = "long", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "long", paramType = "query")
	])
	@TypeChecked(TypeCheckingMode.SKIP)
	def get_student_checkdata(
			HttpServletRequest request){
		String nc_user_id = Web.getCurrentUserNcId();

		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		def query = $$("nc_student_id":nc_user_id);
		query.append("dr", DR.正常.ordinal());
		//-1是因为昨天的要过滤出来
		query.append("attendance_time_long", $$($gt:startOfyesterday()-1));

		def count = attendance_students().count(query);

		int allpage = count / size + ((count% size) >0 ? 1 : 0);
		//优化提醒：这里必须加上时间的排序规则，时间上进行排序。目前还缺少字段getTopicsInfo_v200。
		// def checklist = attendance_students().find(query,$$("is_back": 1 , "is_leave" : 1 , "class_type" : 1 , "sign_time" : 1,"is_sign":1,"nc_attendance_id":1,"nc_id":1)).toArray();

		def checklist = attendance_students().find(query,$$("is_back": 1 , "is_leave" : 1 , "class_type" : 1 , "sign_time" : 1,"is_sign":1,"nc_attendance_id":1,"nc_id":1) ).sort($$("attendance_time_long",1)).skip((page - 1) * size).limit(size).toArray();

		checklist.each {BasicDBObject row ->

			def classdata = getClassData(row["nc_attendance_id"])
			row.put("classinfo",classdata);

			//如果未打卡，并且日期小于今天凌晨，则表示已经漏打卡了。attendance_time
			//		   if(Integer.valueOf(row["is_sign"].toString()) == CheckStatus.未打卡.ordinal()&&(Long.valueOf(classdata.get("attendance_time_long").toString())<startOfyesterday()))
			//		   {
			//			   row.put("is_sign",CheckStatus.漏打卡.ordinal());
			//		   }

			//if(Integer.value row["is_sign"]

		}

		int checked_cout = attendance_students().count(query.append("is_sign", CheckStatus.已打卡.ordinal()));

		return getResultOKDefine(checklist, allpage, count , page , size,checked_cout,count);


	}

	/**
	 * 获取教师端考勤
	 * @Description: 我的订单
	 * @date 2016年3月16日 上午10:48:22
	 */

	@ResponseBody
	@RequestMapping(value = "get_teacher_checkdata/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取教师端考勤", httpMethod = "GET",  notes = "获取教师端考勤",response =UserWalletDetailListVO.class)
	@TypeChecked(TypeCheckingMode.SKIP)
	def get_teacher_checkdata(
			HttpServletRequest request){
		String nc_user_id = Web.getCurrentUserNcId();
		Integer user_id = Web.getCurrentUserId();
		String sm_user_id = users().findOne($$("_id":user_id)).get("sm_user_id")?.toString();


		def otherlist = [];
		def query = $$("nc_teacher_id":sm_user_id,"dr" : DR.正常.ordinal(),"attendance_time_long":$$($gt:startOfyesterday()-1));
		def attendlist = attendances().find(query).sort($$("attendance_time_long":-1)).limit(200).toArray();
		attendlist.each {BasicDBObject item->

			String planid =item.get("nc_class_plan");

			BasicDBObject result = new BasicDBObject();
			result.put("nc_school_name", item.get("nc_school_name"));
			result.put("room", item.get("room"));
			result.put("attendance_time", item.get("attendance_time"));
			def plan = class_plan().findOne($$("nc_id":planid));
			if(plan!=null)
			{
				result.put("open_time_text", plan.get("name"));
			}
			result.put("nc_teacher_name", item.get("nc_teacher_name"));
			result.put("nc_course_name", item.get("nc_course_name"));
			result.put("content", item.get("content"));
			result.put("nc_id", item.get("nc_id"));

			otherlist.push(result);
		}

		def resultlist = new  BasicDBObject();


		resultlist.put("today", getTeacherTodayOrYestodayCheck(sm_user_id,0));
		resultlist.put("yesterday", getTeacherTodayOrYestodayCheck(sm_user_id,-1));
		resultlist.put("other", otherlist);








		return getResultOK(resultlist);


	}



	/**
	 * 根据考勤表id获取班级考勤详情
	 * @Description: 我的订单
	 * @date 2016年3月16日 上午10:48:22
	 */

	@ResponseBody
	@RequestMapping(value = "get_class_checkdata_byid/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "根据考勤表id获取班级考勤详情", httpMethod = "GET",  notes = "根据考勤表id获取班级考勤详情",response =UserWalletDetailListVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "nc_id", value = "考勤表id", required = false, dataType = "long", paramType = "query")
	])
	@TypeChecked(TypeCheckingMode.SKIP)
	def get_class_checkdata_byid(
			HttpServletRequest request){
		//String nc_user_id = Web.getCurrentUserNcId();


		//db.class_plan.find({"nc_out_teacher_id" : "0001A510000000000Y31"})

		String nc_id = ServletRequestUtils.getStringParameter(request, "nc_id", "");

		def result = new BasicDBObject();

		def query = $$("nc_attendance_id":nc_id);


		def studentchecklist = attendance_students().find(query,$$("is_back": 1 , "is_leave" : 1 , "class_type" : 1 , "sign_time" : 1,"is_sign":1,"nc_attendance_id":1,"nc_student_name":1)).toArray();

		def checked_count =attendance_students().count($$("nc_attendance_id":nc_id,"is_sign":CheckStatus.已打卡.ordinal()));

		def all_count = attendance_students().count( $$("nc_attendance_id":nc_id));


		result.put("studentchecklist", studentchecklist);
		result.put("all_count", all_count);
		result.put("checked_count", checked_count);
		result.put("unchecked_count", all_count-checked_count);
		result.put("class_info", getClassData(nc_id));



		return getResultOK(result);


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

	/**
	 * 学生打卡
	 * @Description: 我的订单
	 * @date 2016年3月16日 上午10:48:22
	 */

	@ResponseBody
	@RequestMapping(value = "student_checkin/*-*", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "学生打卡", httpMethod = "POST",  notes = "学生打卡",response =UserWalletDetailListVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "nc_id", value = "打卡记录的ncid", required = false, dataType = "long", paramType = "query"),
		@ApiImplicitParam(name = "longitude", value = "经度", required = false, dataType = "float", paramType = "query"),
		@ApiImplicitParam(name = "latitude", value = "纬度", required = false, dataType = "float", paramType = "query")
	])
	@TypeChecked(TypeCheckingMode.SKIP)
	def student_checkin(
			HttpServletRequest request){
		String nc_user_id = Web.getCurrentUserNcId();
		Integer user_id = Web.getCurrentUserId();
		String nick_name = Web.currentUserNick();

		String nc_id = ServletRequestUtils.getStringParameter(request, "nc_id");
		float longitude  = ServletRequestUtils.getFloatParameter(request, "longitude", 0);
		float latitude = ServletRequestUtils.getFloatParameter(request, "latitude", 0);

		def item = attendance_students().findOne($$("nc_id":nc_id));

		def main = attendances().findOne($$("nc_id":item.get("nc_attendance_id")));
		String planid =main.get("nc_class_plan");
		String nc_school_code =main.get("nc_school_code");
		def plan = class_plan().findOne($$("nc_id":planid));


		//先判断时间是否符合要求.必须在开课时间的前后30分钟

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");




		//        if(df.format(openTime) != df.format(new Date()))
		//		{
		//			save_student_checkin_log(nc_user_id,nc_id,longitude,latitude,main.get("nc_school_name"),main.get("room"),main.get("nc_course_name"),CheckStatus.未打卡.ordinal(),"超出打卡时间范围，开课时间为："+main.get("attendance_time"));
		//			return getResult(-1,"超出打卡时间范围，开课时间为："+main.get("attendance_time"));
		//		}

		//		//日期判断，7天之前的或者未来不能打卡
		//		Long signtime = Long.valueOf(main.get("attendance_time_long").toString());
		//		if(signtime<startOfDayLong(-7)||
		//			signtime>=endOfTodDay())
		//		{
		//				save_student_checkin_log(nc_user_id,nc_id,longitude,latitude,main.get("nc_school_name"),main.get("room"),main.get("nc_course_name"),CheckStatus.未打卡.ordinal(),"超出打卡时间范围，开课时间为："+main.get("attendance_time"));
		//				return getResult(-1,"超出打卡时间范围，开课时间为："+main.get("attendance_time"));
		//		}

		//if(schoolController.check_school_area(nc_school_code, longitude, latitude))
		if(true)
		{
			//记录日志
			save_student_checkin_log(nc_user_id,nc_id,longitude,latitude,main.get("nc_school_name"),main.get("room"),main.get("nc_course_name"),CheckStatus.已打卡.ordinal(),"打卡成功");

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
		else
		{
			save_student_checkin_log(nc_user_id,nc_id,longitude,latitude,main.get("nc_school_name"),main.get("room"),main.get("nc_course_name"),CheckStatus.未打卡.ordinal(),"超出校区范围，打卡失败");
			return getResult(-1,"超出校区范围，打卡失败");
		}






	}
	public static void main(String[] args) throws Exception {


		System.out.println(formateyyyyMMddhhmmss());
	}



}
