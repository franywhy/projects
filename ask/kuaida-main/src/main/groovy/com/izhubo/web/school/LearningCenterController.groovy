package com.izhubo.web.school;
import javax.annotation.Resource;

import static com.izhubo.rest.common.util.WebUtils.$$
import static com.izhubo.rest.common.doc.MongoKey.$in
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.text.SimpleDateFormat
import java.util.Date;
import java.util.TreeMap.PrivateEntryIterator;

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.common.util.KeyUtils.PUBLIC;
import com.izhubo.model.DR
import com.izhubo.model.SignCourseState
import com.izhubo.model.SignsStatus
import com.izhubo.rest.common.util.NumberUtil;
import com.izhubo.web.BaseController
import com.izhubo.web.vo.UserinfoMySignListVO
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.wordnik.swagger.annotations.ApiOperation



@Controller
@RequestMapping("/learningcenter")
public class LearningCenterController extends BaseController {


	/** pc考前冲刺 */
	@Value("#{application['pc.liveclass.domain']}")
	String PC_LIVECLASS_DOMAIN = null;
	/** app考前冲刺 */
	@Value("#{application['app.liveclass.domain']}")
	String APP_LIVECLASS_DOMAIN = null;


	@Value("#{application['hqjy.domain']}")
	String HQJY_DOMAIN = "http://test.hqjy.com/";

	public DBCollection attention() {
		return mainMongo.getCollection("attention");
	}
	public DBCollection collection() {
		return mainMongo.getCollection("collection");
	}
	public DBCollection topics() {
		return mainMongo.getCollection("topics");
	}
	public DBCollection industry() {
		return mainMongo.getCollection("industry");
	}
	public DBCollection tip_content() {
		return mainMongo.getCollection("tip_content");
	}
	/** 课程科目 */
	public DBCollection commodity_courses() {
		return mainMongo.getCollection("commodity_courses");
	}
	/** 排课计划 */
	public DBCollection class_plan() {
		return mainMongo.getCollection("class_plan");
	}
	/** 学员信息 */
	public DBCollection plan_student() {
		return mainMongo.getCollection("plan_student");
	}
	/** 课程信息 */
	public DBCollection plan_courses() {
		return mainMongo.getCollection("plan_courses");
	}
	/** 报名表 */
	public DBCollection signs() {
		return mainMongo.getCollection("signs");
	}
	/** 报名表课程科目 */
	public DBCollection sign_course() {
		return mainMongo.getCollection("sign_course");
	}
	/** 校区 */
	public DBCollection area() {
		return mainMongo.getCollection("area");
	}
	/** 课程科目 */
	public DBCollection courses() {
		return mainMongo.getCollection("courses");
	}
	/** 课程 节 */
	public DBCollection courses_chapter_section() {
		return mainMongo.getCollection("courses_chapter_section");
	}
	/** 课程 课时 */
	public DBCollection courses_content() {
		return mainMongo.getCollection("courses_content");
	}
	/** 我的课程浏览记录 */
	public DBCollection sign_logs() {
		return logMongo.getCollection("sign_logs");
	}
	/**	介绍 */
	public DBCollection shares() {
		return logMongo.getCollection("shares_log");
	}
	/**	邀请信息 */
	public DBCollection invite() {
		return mainMongo.getCollection("invite");
	}
	/**	考前冲刺 */
	public DBCollection live_class() {
		return mainMongo.getCollection("live_class");
	}
	/**	考前冲刺 */
	public DBCollection live_class_item() {
		return mainMongo.getCollection("live_class_item");
	}
	public DBCollection middle_course_plan() {
		return mainMongo.getCollection("middle_course_plan");
	}

	//	/**	收支明细 */
	//	public DBCollection user_incom_log() {
	//		return logMongo.getCollection("user_incom_log");
	//	}

	public DBCollection c(){
		return logMongo.getCollection("shares_log");
	}
	
	public DBCollection middle_daily_plan_users(){
		return mainMongo.getCollection("middle_daily_plan_users");
	}
	
	
	//自动回放视频功能
	public DBCollection live_debris_record(){
		return mainMongo.getCollection("live_debris_record");
	}
	
	
	

	//中级职称相关配置项初始化
	private String[] getmiddle_commodity_ids()
	{
		return constants().findOne().get("middle_commodity_id");
	}

	private String[] getmiddle_course_ids()
	{
		return constants().findOne().get("middle_course_ids");
	}

	private String[] getmiddle_white_list()
	{
		return constants().findOne().get("middle_white_list");
	}




	@ResponseBody
	@RequestMapping(value = "liveClassListH5", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "H5考前冲刺", httpMethod = "GET" , notes = "H5考前冲刺")
	@TypeChecked(TypeCheckingMode.SKIP)
	def liveClassListH5(HttpServletRequest request){
		Map result = new HashMap();
		result["liveList"] = liveClassList(1);
		result["endliveList"] = endliveClassList(1);
		return getResultOK(result);
	}

	@ResponseBody
	@RequestMapping(value = "liveClassList_API", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "api考前冲刺", httpMethod = "GET" , notes = "H5考前冲刺")
	@TypeChecked(TypeCheckingMode.SKIP)
	def liveClassList_API(HttpServletRequest request){
		Map result = new HashMap();
		String phone = request["phone"].toString();
		def signList = signs().count(
				$$("phone" : phone , "status" : $$('$in' : [
					SignsStatus.在读.ordinal() ,
					SignsStatus.休学.ordinal() ,
					SignsStatus.毕业.ordinal()
				]) ,"dr" : DR.正常.ordinal()) ,
				);

		if(signList>0)
		{


			List liveList = liveClassList(0);
			List endliveList = endliveClassList(0);

			int liveSize = liveList.size();
			int endliveSize = endliveList.size();

			result["liveList"] =liveList;
			result["endliveList"] = endliveList;

			result["name"] = "2017会计从业考前冲刺";
			result["progress"] = (endliveSize/(liveSize+endliveSize))*100;
			result["course_num"] =liveSize+ endliveSize;
			return getResultOK(result);

		}
		else
		{
			return ["code":101,"data":null,"msg":""];
		}



	}

	@ResponseBody
	@RequestMapping(value = "NCSignList_API", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "NC报名表列表（将自动过滤中级职称）", httpMethod = "GET" , notes = "H5考前冲刺")
	@TypeChecked(TypeCheckingMode.SKIP)
	def NCSignList_API(HttpServletRequest request){
		Map result = new HashMap();
		String phone = request["phone"].toString();
		//TODO
		//报名表集合
		List signResultList = new ArrayList();
		//只有订单没有报名表集合
		List orderResultList = new ArrayList();


		//订单号集合
		List<String> order_no_list = new ArrayList<String>();

		//学员报名表集合
		def signList = signs().find(
				$$("phone" : phone , "status" : $$('$in' : [
					SignsStatus.在读.ordinal() ,
					SignsStatus.休学.ordinal() ,
					SignsStatus.毕业.ordinal()
				]) , "dr" : DR.正常.ordinal()) ,
				$$("nc_id" : 1 , "nc_commodity_id" : 1 , "code" : 1, "status" : 1 , "order_no" : 1,"school_name":1,"nc_user_id":1)//NC_id  商品的NCid code  状态
				).sort($$("status" : 1 , "create_time" : 1)).limit(MAX_LIMIT)?.toArray();

		if(signList){
			String nc_user_id = signList.get(0).get("nc_user_id").toString();
			signList.each {def item ->
				//报名表NCid
				String nc_sign_id = item["nc_id"];
				if(!Arrays.asList(getmiddle_commodity_ids()).contains(item.get("nc_commodity_id")))
				{
					//报名表基础信息
					Map map = signTitleInfo(nc_sign_id,nc_user_id);
					if(map){
						Map scMap = signCourseList(nc_sign_id, nc_user_id);
						if(scMap){
							//面授
							map["downList"] = scMap["downList"];

						}

					}
					signResultList.add(map);
				}
			}
			result["signData"] = signResultList;
			return getResultOK(result);
		}
		else
		{

			return ["code":101,"data":null,"msg":""];
		}



	}


	@ResponseBody
	@RequestMapping(value = "NCMiddleSignBuild", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@TypeChecked(TypeCheckingMode.SKIP)
	def NCMiddleSignBuild(HttpServletRequest request){
		def userList = signs().find(
				$$( "nc_commodity_id" : $$('$in':getmiddle_commodity_ids()), "status" : $$('$in' : [
					SignsStatus.在读.ordinal() ,
					SignsStatus.休学.ordinal() ,
					SignsStatus.毕业.ordinal()
				]) , "dr" : DR.正常.ordinal()) ,
				$$("nc_id" : 1 , "nc_commodity_id" : 1 , "code" : 1, "status" : 1 , "order_no" : 1,"school_name":1,"nc_user_id":1,"phone":1)//NC_id  商品的NCid code  状态
				).sort($$("status" : 1 , "create_time" : 1))?.toArray();

		middle_course_plan().remove($$("timestart_long":$$($lt:System.currentTimeMillis()-1000*60*60*6)));
		userList.each {def cp->

			DBObject result = new BasicDBObject();
			result.put("_id", UUID.randomUUID());
			result.put("phone", cp.get("phone"));
			result.put("data",  NCMiddleSignList_API_Async( cp.get("phone")));
			result.put("timestart",  nomorlFormate( System.currentTimeMillis()));
			result.put("timestart_long",   System.currentTimeMillis());
			middle_course_plan().save(result);

		}

	}
	@ResponseBody
	@RequestMapping(value = "NCMiddleSignUsersBuild", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@TypeChecked(TypeCheckingMode.SKIP)
	def NCMiddleSignUsersBuild(HttpServletRequest request){
		
		Long timelong  = ServletRequestUtils.getLongParameter(request, "timelong", System.currentTimeMillis());
		
		def class_planList = class_plan().find(
				$$("nc_course_id" : [$in:getmiddle_course_ids()],"dr":DR.正常.ordinal())
				).sort($$("open_time" : -1)).limit(MAX_LIMIT).toArray();	
		if(class_planList)
		{
			class_planList.each {def cp->
				def plan_coursesList = plan_courses().find(
						$$("nc_plan_id" : cp.get("nc_id"),"dr":DR.正常.ordinal())
						).sort($$("open_time" : -1)).limit(MAX_LIMIT).toArray();
				Long now = System.currentTimeMillis();
				if(plan_coursesList){
					plan_coursesList.each {def pi->

						//查找当天的直播，生成学员列表
						Map planMap = new HashMap();
						Long starttime = planOpenTimeStringToLong(pi["live_start_time_long"]);
						Long endtime = planOpenTimeStringToLong(pi["live_end_time_long"]);
						
						SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
						Long StartOfDateTime = formatter.parse(dateFormate(timelong) +" 00:00:00").getTime();
						Long EndOfDateTime = formatter.parse(dateFormate(timelong) +" 23:59:59").getTime();
									
						if(StartOfDateTime<=starttime&&EndOfDateTime>=endtime)
						{
							//先删除当天的该pid的记录
							middle_daily_plan_users().remove($$("create_day":dateFormate(timelong),"pid":pi.get("nc_id")));
							
							def studenlist = plan_student().find($$("nc_plan_id": cp.get("nc_id"),"dr":DR.正常.ordinal()));
							studenlist.each {def stu->
								Long timestamp = System.currentTimeMillis();
								middle_daily_plan_users().save($$(
									"_id":UUID.randomUUID(),
									"create_day" : dateFormate(timelong),
									"phone" : stu.get("telephone"),
									"pid" : pi.get("nc_id"),
									"create_time_long" : timestamp ,
									"create_time_text" : nomorlFormate(timestamp),
									"nc_plan_id" : cp.get("nc_id")
									));
							}
							
						}
						

						
					}
				}

			}
		}
	}




	def NCMiddleSignList_API_Async(String phone){
		Map result = new HashMap();
		//TODO
		//报名表集合
		List signResultList = new ArrayList();
		//只有订单没有报名表集合
		List orderResultList = new ArrayList();


		//订单号集合
		List<String> order_no_list = new ArrayList<String>();
		def signList;
		//如果是白名单，则自动去搜索匹配的学员
		if(Arrays.asList(getmiddle_white_list()).contains(phone))
		{
			//学员报名表集合
			signList = signs().find(
					$$( "nc_commodity_id" : $$('$in':getmiddle_commodity_ids()), "status" : $$('$in' : [
						SignsStatus.在读.ordinal() ,
						SignsStatus.休学.ordinal() ,
						SignsStatus.毕业.ordinal()
					]) , "dr" : DR.正常.ordinal()) ,
					$$("nc_id" : 1 , "nc_commodity_id" : 1 , "code" : 1, "status" : 1 , "order_no" : 1,"school_name":1,"nc_user_id":1)//NC_id  商品的NCid code  状态
					).sort($$("status" : 1 , "create_time" : 1)).limit(1)?.toArray();
		}
		else
		{
			//学员报名表集合
			signList = signs().find(
					$$("phone" : phone, "nc_commodity_id" : $$('$in':getmiddle_commodity_ids()), "status" : $$('$in' : [
						SignsStatus.在读.ordinal() ,
						SignsStatus.休学.ordinal() ,
						SignsStatus.毕业.ordinal()
					]) , "dr" : DR.正常.ordinal()) ,
					$$("nc_id" : 1 , "nc_commodity_id" : 1 , "code" : 1, "status" : 1 , "order_no" : 1,"school_name":1,"nc_user_id":1)//NC_id  商品的NCid code  状态
					).sort($$("status" : 1 , "create_time" : 1)).limit(1)?.toArray();
		}


		if(signList){

			String nc_user_id = signList.get(0).get("nc_user_id").toString();
			signList.each {def item ->
				//报名表NCid
				String nc_sign_id = item["nc_id"];
				//报名表基础信息
				Map map = signTitleInfo(nc_sign_id,nc_user_id);



				if(map){
					Map scMap = signMiddleCourseList(nc_sign_id, nc_user_id);
					if(scMap){



						int num = 0;

						//开始状态
						scMap["downList"].each{def subitem->

							List liveList = new ArrayList();
							List EndliveList = new ArrayList();
							List NOTliveList = new ArrayList();

							Map signMap = clone(map);


							subitem["planList"].each{def sitem->

								sitem["course_name"] = subitem["course_name"].toString();
								signMap.put("course_name", subitem["course_name"].toString());


								if("已结束".equals(sitem["state"].toString()))
								{
									EndliveList.add(sitem);
								}
								else if("未排课".equals(sitem["state"].toString()))
								{
									NOTliveList.add(sitem);
								}
								else
								{
									liveList.add(sitem);
								}
							}

							NOTliveList.each{def sitem->
								liveList.add(sitem);
							}
							signMap["liveList"] =liveList;
							signMap["endliveList"] =EndliveList.sort {  a, b->
								a.get("time_long") < b.get("time_long")?0:1
							};
							int allcount = liveList.size()+EndliveList.size();
							signMap["course_num"] = allcount;
							if(allcount>0)
							{
								signMap["progress"] = (int)((EndliveList.size()  / allcount)*100);
							}
							signMap.put("nc_sign_id", signMap["nc_sign_id"].toString()+num.toString());
							signMap.put("commodity_name",signMap.get("commodity_name").toString()+"-" +subitem["course_name"].toString());
							signResultList.add(signMap);
							num++;
						}



						//					  liveList.sort {  a, b->
						//						    、、a.get("time").toString().equals("未排课")?-1:0
						//						 // Long.valueOf(a.get("time_long").toString()) < Long.valueOf(b.get("time_long").toString())?0:1
						//						  //a.get("time_long") < b.get("time_long")?1:0
						//						};


						//结束状态

					}

				}

			}
			result["signData"] = signResultList;
			return getResultOK(result);
		}
		else
		{

			return ["code":101,"data":null,"msg":""];
		}





	}

	
	def NCMiddleSignList_API_Async_Real(String phone){
		Map result = new HashMap();
		//TODO
		//报名表集合
		List signResultList = new ArrayList();
		//只有订单没有报名表集合
		List orderResultList = new ArrayList();
		//订单号集合
		List<String> order_no_list = new ArrayList<String>();
		def signList;
		//如果是白名单，则自动去搜索匹配的学员
		
			//学员报名表集合
			signList = signs().find(
					$$("phone" : phone, "nc_commodity_id" : $$('$in':getmiddle_commodity_ids()), "status" : $$('$in' : [
						SignsStatus.在读.ordinal() ,
						SignsStatus.休学.ordinal() ,
						SignsStatus.毕业.ordinal()
					]) , "dr" : DR.正常.ordinal()) ,
					$$("nc_id" : 1 , "nc_commodity_id" : 1 , "code" : 1, "status" : 1 , "order_no" : 1,"school_name":1,"nc_user_id":1)//NC_id  商品的NCid code  状态
					).sort($$("status" : 1 , "create_time" : 1)).limit(1)?.toArray();
		if(signList){

			String nc_user_id = signList.get(0).get("nc_user_id").toString();
			signList.each {def item ->
				//报名表NCid
				String nc_sign_id = item["nc_id"];
				//报名表基础信息
				Map map = signTitleInfo(nc_sign_id,nc_user_id);



				if(map){
					Map scMap = signMiddleCourseList(nc_sign_id, nc_user_id);
					if(scMap){



						int num = 0;

						//开始状态
						scMap["downList"].each{def subitem->

							List liveList = new ArrayList();
							List EndliveList = new ArrayList();
							List NOTliveList = new ArrayList();

							Map signMap = clone(map);


							subitem["planList"].each{def sitem->

								sitem["course_name"] = subitem["course_name"].toString();
								signMap.put("course_name", subitem["course_name"].toString());


								if("已结束".equals(sitem["state"].toString()))
								{
									EndliveList.add(sitem);
								}
								else if("未排课".equals(sitem["state"].toString()))
								{
									NOTliveList.add(sitem);
								}
								else
								{
									liveList.add(sitem);
								}
							}

							NOTliveList.each{def sitem->
								liveList.add(sitem);
							}
							signMap["liveList"] =liveList;
							signMap["endliveList"] =EndliveList.sort {  a, b->
								a.get("time_long") < b.get("time_long")?0:1
							};
							int allcount = liveList.size()+EndliveList.size();
							signMap["course_num"] = allcount;
							if(allcount>0)
							{
								signMap["progress"] = (int)((EndliveList.size()  / allcount)*100);
							}
							signMap.put("nc_sign_id", signMap["nc_sign_id"].toString()+num.toString());
							signMap.put("commodity_name",signMap.get("commodity_name").toString()+"-" +subitem["course_name"].toString());
							signResultList.add(signMap);
							num++;
						}



						//					  liveList.sort {  a, b->
						//						    、、a.get("time").toString().equals("未排课")?-1:0
						//						 // Long.valueOf(a.get("time_long").toString()) < Long.valueOf(b.get("time_long").toString())?0:1
						//						  //a.get("time_long") < b.get("time_long")?1:0
						//						};


						//结束状态

					}

				}

			}
			result["signData"] = signResultList;
			return getResultOK(result);
		}
		else
		{

			return ["code":101,"data":null,"msg":""];
		}





	}


	@ResponseBody
	@RequestMapping(value = "NCMiddleSignList_API", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "只包含中级职称", httpMethod = "GET" , notes = "H5考前冲刺")
	@TypeChecked(TypeCheckingMode.SKIP)
	def NCMiddleSignList_API(HttpServletRequest request){
		Map result = new HashMap();
		String phone = request["phone"].toString();
		//TODO
		//报名表集合
		List signResultList = new ArrayList();
		//只有订单没有报名表集合
		List orderResultList = new ArrayList();


		//订单号集合
		List<String> order_no_list = new ArrayList<String>();
		def signList;
		//如果是白名单，则自动去搜索匹配的学员
		if(Arrays.asList(getmiddle_white_list()).contains(phone))
		{
			result = NCMiddleSignList_API_Async(phone);
		}
		else
		{
			result = middle_course_plan().find($$("phone":phone)).sort($$("timestart_long":-1))?.limit(1)[0]?.get("data");
		}

		if(result)
		{
			return result;
		}
		else
		{
			return ["code":101,"data":null,"msg":""];
		}



	}


	@ResponseBody
	@RequestMapping(value = "NCMiddleSignList_API_Test", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "只包含中级职称", httpMethod = "GET" , notes = "H5考前冲刺")
	@TypeChecked(TypeCheckingMode.SKIP)
	def NCMiddleSignList_API_Test(HttpServletRequest request){
		Map result = new HashMap();
		String phone = request["phone"].toString();
		//TODO

		return NCMiddleSignList_API_Async(phone);



	}
	
	
	@ResponseBody
	@RequestMapping(value = "NCMiddleSignList_API_Test_Real", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "只包含中级职称", httpMethod = "GET" , notes = "H5考前冲刺")
	@TypeChecked(TypeCheckingMode.SKIP)
	def NCMiddleSignList_API_Test_Real(HttpServletRequest request){
		Map result = new HashMap();
		String phone = request["phone"].toString();
		//TODO

		return NCMiddleSignList_API_Async_Real(phone);



	}


	@ResponseBody
	@RequestMapping(value = "NCMiddlePlan_API", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "只包含中级职称的排课计划", httpMethod = "GET" , notes = "H5考前冲刺")
	@TypeChecked(TypeCheckingMode.SKIP)
	def NCMiddlePlan_API(HttpServletRequest request){
		Map result = new HashMap();

		//TODO
		//报名表集合
		List signResultList = new ArrayList();

		//初始化
		Map map = new HashMap();
		//直播
		List planList = new ArrayList();
		//报名表课程科目


		def class_planList = class_plan().find(
				$$("nc_course_id" : [$in:getmiddle_course_ids()],"dr":DR.正常.ordinal())
				).sort($$("open_time" : -1)).limit(MAX_LIMIT).toArray();
		if(class_planList)
		{
			class_planList.each {def cp->
				def plan_coursesList = plan_courses().find(
						$$("nc_plan_id" : cp.get("nc_id"),"dr":DR.正常.ordinal())
						).sort($$("open_time" : -1)).limit(MAX_LIMIT).toArray();
				Long now = System.currentTimeMillis();
				if(plan_coursesList){
					plan_coursesList.each {def pi->

						Map planMap = new HashMap();
						Long starttime = planOpenTimeStringToLong(pi["live_start_time_long"]);
						Long endtime = planOpenTimeStringToLong(pi["live_end_time_long"]);
						//课时名称
						planMap["name"] = pi["content"];
						//老师名称
						planMap["teacher_name"] = pi["nc_teacher_name"];
						//状态
						planMap["state"] = liveClassStateString(starttime,endtime);
						//状态
						planMap["time"] = planOpenTimeFormate(starttime);

						planMap["pid"] = pi["nc_id"];

						//课时放入课时列表
						planList.add(planMap);
					}
				}

			}
		}


		//系统当前时间


		return getResultOK(planList);
	}


	/**
	 * 学习中心-报名表列表-web
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "mySignList", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "学习中心-报名表列表-web", httpMethod = "GET" , response = UserinfoMySignListVO.class , notes = "学习中心-报名表列表-web")
	@TypeChecked(TypeCheckingMode.SKIP)
	def mySignList(HttpServletRequest request){
		//用户id
		String nc_user_id = request["nc_user_id"].toString();


		Map result = new HashMap();
		//TODO
		//报名表集合
		List signResultList = new ArrayList();
		//只有订单没有报名表集合
		List orderResultList = new ArrayList();


		//订单号集合
		List<String> order_no_list = new ArrayList<String>();

		//学员报名表集合
		def signList = signs().find(
				$$("nc_user_id" : nc_user_id , "status" : $$('$in' : [
					SignsStatus.在读.ordinal() ,
					SignsStatus.休学.ordinal() ,
					SignsStatus.毕业.ordinal()
				]) , "dr" : DR.正常.ordinal()) ,
				$$("nc_id" : 1 , "nc_commodity_id" : 1 , "code" : 1, "status" : 1 , "order_no" : 1,"school_name":1)//NC_id  商品的NCid code  状态
				).sort($$("status" : 1 , "create_time" : 1)).limit(MAX_LIMIT)?.toArray();

		if(signList){
			signList.each {def item ->
				//报名表NCid
				String nc_sign_id = item["nc_id"];
				//报名表基础信息
				Map map = signTitleInfo(nc_sign_id,nc_user_id);

				if(map){
					Map scMap = signCourseList(nc_sign_id, nc_user_id);
					if(scMap){
						//面授
						map["downList"] = scMap["downList"];

					}

				}
				signResultList.add(map);
				//put直播
				//里面的课时

				//订单号
				String order_no = item["order_no"];
				if(StringUtils.isNotBlank(order_no)){
					order_no_list.add(order_no);
				}
			}
		}


		result["signData"] = signResultList;
		result["liveList"] = liveClassList(0);
		result["endliveList"] = endliveClassList(0);




		return getResultOK(result);
	}

	/**
	 * 查询报名表课程-分成两组数据 面授,直播
	 * @param nc_sign_id
	 * @param nc_user_id
	 * @return				map["down"] = 面授 ; map["online"] = 直播
	 */
	private Map signCourseList(String nc_sign_id , String nc_user_id){
		//初始化
		Map map = new HashMap();

		//面授
		List downList = new ArrayList();
		//直播
		List onlineList = new ArrayList();

		//面授
		map["downList"] = downList;


		//报名表课程科目
		def signCourseList = sign_course().find(
				$$("nc_sign_id" : nc_sign_id,"dr":DR.正常.ordinal()) ,
				$$("course_code" : 1  , "status" : 1 , "course_name" : 1)
				).sort($$("course_code" : 1)).limit(MAX_LIMIT).toArray();
		//系统当前时间
		Long now = System.currentTimeMillis();
		if(signCourseList){
			signCourseList.each {def item->
				//				课程Map
				Map courseMap = new HashMap();
				//课程名称
				courseMap["course_name"] = item["course_name"];
				//课程NCid
				String nc_course_id = courses().findOne($$("code" : item["course_code"].toString()) , $$("nc_id" : 1))?.get("nc_id");
				//面授和线上标志  0.面授 1.线上
				Integer lineType = couresIsOnline(nc_course_id)

				//报名表中的排课状态 1.已排课 2.未排课 3.已完课
				Integer status = Integer.valueOf(item["status"].toString());

				//课时列表
				List planList = new ArrayList();
				courseMap["planList"] = planList;

				if(item["course_code"].toString().equals("kckm236"))
				{
					String DDLFormatterImpl = "";
				}
				if(status == SignCourseState.已排课.ordinal() || status == SignCourseState.已完课.ordinal()){
					//排课计划id
					String nc_plan_id = "";
					def plans = plan_student().find(
							$$("nc_sign_id" : nc_sign_id , "nc_user_id" : nc_user_id , "nc_course_id" : nc_course_id , "dr" : DR.正常.ordinal()),
							$$("nc_plan_id" : 1)
							).sort($$("into_class_time":-1)).toArray();

					if(plans)
					{
						nc_plan_id =   plans[0]?.get("nc_plan_id");
					}


					def planCourse = plan_courses().find($$("nc_plan_id" : nc_plan_id , "dr" : DR.正常.ordinal() ) , $$("nc_teacher_name" : 1 , "content" : 1 , "open_time" : 1,"nc_id":1)).sort($$("open_time" : 1)).limit(MAX_LIMIT).toArray();
					if(planCourse){
						planCourse.each {def pi->
							Map planMap = new HashMap();
							Long open_time = planOpenTimeStringToLong(pi["open_time"]);

							//课时名称
							planMap["name"] = pi["content"];
							//老师名称
							planMap["teacher_name"] = pi["nc_teacher_name"];
							//状态
							planMap["state"] = planStateString(open_time);
							//状态
							planMap["time"] = planOpenTimeFormate(open_time);

							planMap["time_long"] = open_time;

							planMap["pid"] = pi["nc_id"];

							//processPro(planMap,courseMap["course_name"].toString());
							//题库
							//planMap["tiku_url"] = tikuUrl(nc_course_id);
							//实训
							//planMap["shixun_url"] = shixuUrl(nc_course_id);
							//直播
							//planMap["live_url"] = zhiboUrl(nc_course_id);

							//课时放入课时列表
							planList.add(planMap);
						}
					}

				}else if(status == SignCourseState.未排课.ordinal() ){
					//课程课时
					def coursesContentList = courses_content().find($$("nc_course_id" : nc_course_id) , $$("name" : 1 , "nc_name" : 1)).sort($$("sessions" : 1)).limit(MAX_LIMIT).toArray();
					if(coursesContentList){
						coursesContentList.each {DBObject cc->
							Map planMap = new HashMap();

							//课时名称
							planMap["name"] = coursesContentName(cc);
							//老师名称
							planMap["teacher_name"] = "-";
							//状态
							planMap["state"] = planStateString(null);
							//状态
							planMap["time"] = "-";

							planMap["pid"] ="";

							planMap["time_long"] = 1803200400000L;
							//课时放入课时列表
							planList.add(planMap);
						}
					}

				}
				//面授和线上标志  0.面授 1.线上

				downList.add(courseMap);


			}

		}
		return map;
	}


	/**
	 * 查询报名表课程-分成两组数据 面授,直播
	 * @param nc_sign_id
	 * @param nc_user_id
	 * @return				map["down"] = 面授 ; map["online"] = 直播
	 */
	private Map signMiddleCourseList(String nc_sign_id , String nc_user_id){
		//初始化
		Map map = new HashMap();

		//面授
		List downList = new ArrayList();
		//直播
		List onlineList = new ArrayList();

		//面授
		map["downList"] = downList;


		//报名表课程科目
		def signCourseList = sign_course().find(
				$$("nc_sign_id" : nc_sign_id,"dr":DR.正常.ordinal()) ,
				$$("course_code" : 1  , "status" : 1 , "course_name" : 1)
				).sort($$("course_code" : 1)).limit(MAX_LIMIT).toArray();
		//系统当前时间
		Long now = System.currentTimeMillis();
		if(signCourseList){
			signCourseList.each {def item->
				//				课程Map
				Map courseMap = new HashMap();
				//课程名称
				courseMap["course_name"] = item["course_name"];
				//课程NCid
				String nc_course_id = courses().findOne($$("code" : item["course_code"].toString()) , $$("nc_id" : 1))?.get("nc_id");
				//面授和线上标志  0.面授 1.线上
				Integer lineType = couresIsOnline(nc_course_id)

				//报名表中的排课状态 1.已排课 2.未排课 3.已完课
				Integer status = Integer.valueOf(item["status"].toString());

				//课时列表
				List planList = new ArrayList();
				courseMap["planList"] = planList;

				if(item["course_code"].toString().equals("kckm236"))
				{
					String DDLFormatterImpl = "";
				}
				if(status == SignCourseState.已排课.ordinal() || status == SignCourseState.已完课.ordinal()){
					//排课计划id
					String nc_plan_id = "";
					def plans = plan_student().find(
							$$("nc_sign_id" : nc_sign_id , "nc_user_id" : nc_user_id , "nc_course_id" : nc_course_id , "dr" : DR.正常.ordinal()),
							$$("nc_plan_id" : 1)
							).sort($$("into_class_time":-1)).toArray();

					if(plans)
					{
						nc_plan_id =   plans[0]?.get("nc_plan_id");
					}


					def planCourse = plan_courses().find($$("nc_plan_id" : nc_plan_id , "dr" : DR.正常.ordinal() ) , $$("nc_teacher_name" : 1 , "content" : 1 , "open_time" : 1,"nc_id":1,"live_start_time_long":1,"live_end_time_long":1)).sort($$("open_time" : 1)).limit(MAX_LIMIT).toArray();
					if(planCourse){
						planCourse.each {def pi->
							Map planMap = new HashMap();
							Long open_time = planOpenTimeStringToLong(pi["open_time"]);

							Long starttime = planOpenTimeStringToLong(pi["live_start_time_long"]);
							Long endtime = planOpenTimeStringToLong(pi["live_end_time_long"]);
							//课时名称
							planMap["name"] = pi["content"];
							//老师名称
							planMap["teacher_name"] = pi["nc_teacher_name"];
							//状态
							planMap["state"] = liveClassStateString(starttime, endtime);
							//状态
							planMap["time"] = liveOpenTimeFormate(starttime,endtime);

							planMap["time_long"] = starttime;

							planMap["pid"] = pi["nc_id"];

							processPro(planMap,nc_course_id);
							//题库
							//planMap["tiku_url"] = tikuUrl(nc_course_id);
							//实训
							//planMap["shixun_url"] = shixuUrl(nc_course_id);
							//直播
							//planMap["live_url"] = zhiboUrl(nc_course_id);
							planMap["reply_url"] = processReplay(pi["nc_id"].toString());
							

							//课时放入课时列表
							planList.add(planMap);
						}
					}

				}else if(status == SignCourseState.未排课.ordinal() ){
					//课程课时
					def coursesContentList = courses_content().find($$("nc_course_id" : nc_course_id) , $$("name" : 1 , "nc_name" : 1)).sort($$("sessions" : 1)).limit(MAX_LIMIT).toArray();
					if(coursesContentList){
						coursesContentList.each {DBObject cc->
							Map planMap = new HashMap();

							//课时名称
							planMap["name"] = coursesContentName(cc);
							//老师名称
							planMap["teacher_name"] = "-";
							//状态
							planMap["state"] = planStateString(null);
							//状态
							planMap["time"] = "-";

							planMap["pid"] ="";

							planMap["time_long"] = 1803200400000L;
							//课时放入课时列表
							planList.add(planMap);
						}
					}

				}
				//面授和线上标志  0.面授 1.线上

				downList.add(courseMap);


			}

		}
		return map;
	}



	private  void processPro(Map planMap,String nc_course_id)
	{
		//这里配置中级课程的直播间id
		//课时名称
		List<DBObject> ls = constants().findOne().get("middle_course_liveinfo");
		if("直播中".equals(planMap.get("state")))
		{

			ls.each {DBObject cc->
				if(nc_course_id.equals(cc.get("course_id").toString()))
				{
					planMap.put("live_id", cc.get("liveid"));
					planMap.put("live_num", cc.get("livenum"));
				}
			}
		}


	}
	
	//获取自动抓取的录播视频
	private  String processReplay(String pid)
	{
		//这里配置中级课程的直播间id
		//课时名称
		def replay = live_debris_record().findOne($$("course_time_id":pid))
		if(replay)
		{

			return replay["url"].toString();
		}


	}


	@TypeChecked(TypeCheckingMode.SKIP)
	private Map signTitleInfo(String nc_sign_id,nc_user_id){
		Map map = new HashMap();
		//排课计划nc_id
		map["nc_sign_id"] = nc_sign_id;
		//报名表信息
		def sign = signs().findOne($$("nc_id" : nc_sign_id) , $$("nc_commodity_id" : 1 , "status":1 , "order_no" : 1,"school_name":1));
		if(sign){
			//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 基础信息  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
			//订单号
			String order_no = sign.get("order_no");
			//订单信息
			//商品信息
			def commodity = commoditys().findOne($$("nc_id" : sign["nc_commodity_id"]) , $$("name" : 1 , "_id" : 1));
			//商品名称
			String commodity_name = "-";
			//商品id
			String commodity_id = "";
			if(commodity){
				//商品名称
				commodity_name = commodity["name"];
				//商品id
				commodity_id = commodity["_id"];
			}
			//商品信息

			//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 基础信息  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

			//进度 面授课程数量 在线课程数量 总课程数量

			//面授课程数量
			//总数
			Integer crouse_down_num = 0;
			//已完成
			Integer crouse_down_fin_num = 0;

			//在线课程数量
			//总数
			Integer crouse_online_num = 0;
			//已完成
			Integer crouse_online_fin_num = 0;

			//报名表课程科目
			def signCourseList = sign_course().find(
					$$("nc_sign_id" : nc_sign_id,"dr":DR.正常.ordinal()) ,
					$$("course_code" : 1  , "status" : 1)
					).sort($$("course_code" : 1)).limit(MAX_LIMIT).toArray();
			if(signCourseList){
				signCourseList.each {def item->
					//状态
					Integer status = Integer.valueOf(item["status"].toString());

					String nc_course_id = courses().findOne($$("code" : item["course_code"].toString()) , $$("nc_id" : 1))?.get("nc_id");
					if(SignCourseState.已完课.ordinal() == status){
						//已排课数量
						Integer ks = classPlanFinishNum(nc_course_id ,nc_sign_id, nc_user_id);

						crouse_down_num     += ks;
						crouse_down_fin_num += ks;

					}else if(SignCourseState.未排课.ordinal() == status){
						Integer ks = coursesContentNum(nc_course_id);
						crouse_down_num   += ks;

					}else if(SignCourseState.已排课.ordinal() == status){
						//未完成
						Integer[] array = classPlanClassing(nc_course_id ,nc_sign_id, nc_user_id);

						crouse_down_fin_num += array[0];
						crouse_down_num     += array[0] + array[1];

					}
				}
			}

			//总课程数
			//Integer live_num = liveClassNum();
			Integer crouse_num = crouse_down_num;
			map["course_num"] = crouse_num;
			//面授总课程数
			map["course_down_num"] = crouse_down_num;
			//学习进度
			if(crouse_num > 0){


				map["progress"] = (int)(((crouse_down_fin_num+crouse_online_fin_num ) / crouse_num)*100);
			}else{
				map["progress"] = 0d;
			}

			//考前冲刺数量
			//map["liveclass_num"] = liveClassNum();
			map["school_name"] = sign.get("school_name");
			map["commodity_name"] = commodity_name;


		}
		return map;
	}


	/**
	 * 考前冲刺数量
	 * @return
	 */
	private Integer liveClassNum(){
		return live_class_item().count($$( "end_time" : $$('$gte' : System.currentTimeMillis()) , "dr" : 0)).intValue();
	}

	/**
	 * 线上线下标志
	 * @param course_code 课程code
	 * @return 是否是线上 0.否 1.是
	 */
	private Integer couresIsOnline(String nc_course_id){
		Integer is_online = commodity_courses().findOne($$("nc_id" : nc_course_id) , $$("is_online" : 1))?.get("is_online") as Integer;
		return is_online;
	}


	/**
	 * 已排课(已完课)-课时
	 * @param nc_course_id	nc商品id
	 * @param nc_sign_id	nc报名表id
	 * @param nc_user_id	nc用户id
	 * @return				排课数量
	 */
	private Integer classPlanFinishNum(String nc_course_id , String nc_sign_id , String  nc_user_id){
		//排课计划列表
		Long num = 0;
		if(StringUtils.isNotBlank(nc_course_id) && StringUtils.isNotBlank(nc_sign_id) && StringUtils.isNotBlank(nc_user_id)){
			//排课计划id
			String nc_plan_id = plan_student().findOne(
					$$("nc_sign_id" : nc_sign_id , "nc_user_id" : nc_user_id , "nc_course_id" : nc_course_id , "dr" : DR.正常.ordinal()),
					$$("nc_plan_id" : 1)
					)?.get("nc_plan_id");
			//排课计划列表
			num = plan_courses().count($$("nc_plan_id" : nc_plan_id , "dr" : DR.正常.ordinal() ));

		}
		return num.intValue();
	}

	/**
	 * 未排课-课时
	 * @param nc_course_id	nc商品id
	 * @return				排课数量
	 */
	private Integer coursesContentNum(String nc_course_id){
		Long num = 0;
		if(StringUtils.isNotBlank(nc_course_id)){
			num = courses_content().count($$("nc_course_id" : nc_course_id));
		}
		return num.intValue();
	}

	/**
	 * 未排课-课时
	 * @param nc_course_id	nc商品id
	 * @return				数组:num[0] 已完成   num[1] 未完成
	 */
	private Integer[] classPlanClassing(String nc_course_id , String nc_sign_id , String  nc_user_id){
		Integer[] num = [0, 0];
		if(StringUtils.isNotBlank(nc_course_id) && StringUtils.isNotBlank(nc_sign_id) && StringUtils.isNotBlank(nc_user_id)){
			//排课计划id
			String nc_plan_id = plan_student().findOne(
					$$("nc_sign_id" : nc_sign_id , "nc_user_id" : nc_user_id , "nc_course_id" : nc_course_id , "dr" : DR.正常.ordinal()),
					$$("nc_plan_id" : 1)
					)?.get("nc_plan_id");

			//排课计划列表
			Long now = System.currentTimeMillis();

			//
			num[0] = plan_courses().count($$("nc_plan_id" : nc_plan_id , "dr" : DR.正常.ordinal() , "open_time" : $$('$lte': now))).intValue();

			num[1] = plan_courses().count($$("nc_plan_id" : nc_plan_id , "dr" : DR.正常.ordinal() , "open_time" : $$('$gt': now))).intValue();
		}
		return num;
	}

	/**
	 * 考前冲刺
	 * @param liveType 0.PC  1.APP
	 * @return	考前冲刺结果集
	 */
	private List liveClassList(int liveType){
		//系统当前时间
		Long now = System.currentTimeMillis();
		List itemList = new ArrayList();
		//查出 直播只要未结束
		def liveClassItemList = live_class_item().find(
				$$("end_time" : $$('$gte' : now), "dr" : 0),
				$$("start_time" : 1 , "end_time" : 1 , "name" : 1 , "pc_live_param" : 1 ,"app_live_param" : 1, "teacher_name" : 1)
				).sort($$("start_time" : 1)).limit(MAX_LIMIT).toArray();
		if(liveClassItemList){
			liveClassItemList.each {def item->
				Map map = new HashMap();
				//直播名称
				map["name"] = item["name"];
				//直播老师名称
				map["teacher_name"] = item["teacher_name"];
				//直播开始时间
				Long start_time = planOpenTimeStringToLong(item["start_time"]);
				Long end_time = planOpenTimeStringToLong(item["end_time"]);
				map["time"] = planOpenTimeFormate(start_time);
				//状态
				map["state"] = liveClassStateString(start_time,end_time);
				//直播间地址
				if(liveType == 0){
					map["live_url"] = liveClassUrlPC(item["pc_live_param"]?.toString());
				}else{
					map["live_url"] = liveClassUrlAPP(item["app_live_param"]?.toString());
				}

				itemList.add(map);
			}
		}



		return itemList;
	}

	/**
	 * 考前冲刺-结束 "end_time" : $$('$lt' : now) 
	 * @param liveType 0.PC  1.APP
	 * @return	考前冲刺结果集
	 */
	private List endliveClassList(int liveType){
		Long now = System.currentTimeMillis();
		List itemList = new ArrayList();
		//查出 直播只要未结束
		def liveClassItemList = live_class_item().find(
				$$( "end_time" : $$('$lt' : now), "dr" : 0),
				$$("start_time" : 1 , "end_time" : 1 , "name" : 1 , "pc_live_param" : 1 , "teacher_name" : 1 , "app_playback_param" : 1 , "pc_playback_param" : 1)
				).sort($$("start_time" : -1)).limit(MAX_LIMIT).toArray();
		if(liveClassItemList){
			liveClassItemList.each {def item->
				Map map = new HashMap();
				//直播名称
				map["name"] = item["name"];
				//直播老师名称
				map["teacher_name"] = item["teacher_name"];
				//直播开始时间
				Long start_time = planOpenTimeStringToLong(item["start_time"]);
				Long end_time = planOpenTimeStringToLong(item["end_time"]);
				map["time"] = planOpenTimeFormate(start_time);
				//状态
				map["state"] = liveClassStateString(start_time,end_time);
				//直播间地址
				if(liveType == 0){
					map["live_url"] = liveClassUrlPC(item["pc_playback_param"]?.toString());
				}else{
					map["live_url"] = liveClassUrlAPP(item["app_playback_param"]?.toString());
				}
				itemList.add(map);
			}
		}
		return itemList;
	}
	/**
	 * 根据时间判断课程状态
	 * @param time
	 * @return 已结束/未开始
	 */
	private String planStateString(Long time){
		if(time != null && time < System.currentTimeMillis()){
			return "已结束";
		}
		else if(time == null)
		{
			return "未排课";
		}
		else
		{
			return "未开始";
		}
	}

	/**
	 * 考前冲刺状态
	 * @param startTime		开始时间
	 * @param endTime		结束时间
	 * @return				未开始/已开始
	 */
	private String liveClassStateString(Long startTime,Long endTime){
		Long now = System.currentTimeMillis();
		if(startTime > now){
			return "未开始";
		}else if(startTime<now && endTime>now){
			return "直播中"
		}
		else
		{
			return "已结束"
		}
	}

	/**
	 * PC直播地址
	 * @param param
	 * @return
	 */
	private String liveClassUrlPC(String param){
		return  param;
	}

	/**
	 * APP直播地址
	 * @param param
	 * @return
	 */
	private String liveClassUrlAPP(String param){
		return  param;
	}


	private Long planOpenTimeStringToLong(Object obj){
		if(obj != null){
			return Long.valueOf(obj.toString());
		}
		return 0;
	}

	/**
	 * 学习中心日期格式化 yyyy-MM-dd HH:mm
	 * @param time Long类型的时间
	 * @return yyyy-MM-dd HH:mm
	 */
	private String planOpenTimeFormate(Long time){
		if(time > 0){
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd HH:mm");
			return formatter.format(new Date(time));
		}
		return "-";
	}

	/**
	 * 学习中心日期格式化 yyyy-MM-dd HH:mm
	 * @param time Long类型的时间
	 * @return yyyy-MM-dd HH:mm
	 */
	private String nomorlFormate(Long time){
		if(time > 0){
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
			return formatter.format(new Date(time));
		}
		return "-";
	}
	
	private String dateFormate(Long time){
		if(time > 0){
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
			return formatter.format(new Date(time));
		}
		return "-";
	}

	private  String liveOpenTimeFormate(Long start,Long end){
		if(start > 0&&end>0){
			SimpleDateFormat Startformatter = new SimpleDateFormat ("yyyy/MM/dd HH:mm");
			SimpleDateFormat Endformatter = new SimpleDateFormat ("HH:mm");
			return Startformatter.format(new Date(start))+"~"+Endformatter.format(new Date(end));
		}
		return "-";
	}

	/**
	 * 获取课时名称
	 * @param map
	 * @return		优先取name , 如果name为空则取nc_name。(name是网页展示内容，nc_name为NC的名称)
	 */
	private String coursesContentName(DBObject map){
		String name = "-";
		if(map != null){
			if(map["name"] != null){
				return map["name"] ;
			}else if(map["nc_name"] != null){
				return map["nc_name"] ;
			}
		}
		return name;
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

}

