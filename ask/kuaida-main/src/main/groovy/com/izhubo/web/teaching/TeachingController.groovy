package com.izhubo.web.teaching;
import javax.annotation.Resource;
import static com.izhubo.rest.common.doc.MongoKey.$ne
import static com.izhubo.rest.common.doc.MongoKey.$set
import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.text.SimpleDateFormat

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.hibernate.SessionFactory
import org.json.JSONArray
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.model.Code
import com.izhubo.model.DR
import com.izhubo.model.SignCourseState
import com.izhubo.model.SignsStatus
import com.izhubo.rest.AppProperties
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.web.BaseController
import com.izhubo.web.api.Web
import com.izhubo.web.mq.MessageProductor
import com.izhubo.web.school.SchoolController
import com.izhubo.web.vo.DailyRecommandVO
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation

@RestWithSession
@RequestMapping("/teaching")
public class TeachingController extends BaseController {

	@Resource
	private SessionFactory sessionFactory;

	private String receive_qname =  AppProperties.get("queue.receivenc");
	private String send_qname =  AppProperties.get("queue.sendnc");

	@Resource
	private MessageProductor messageProductorService;
	
	@Resource
	private SchoolController schoolController;
	


	private static Logger logger = LoggerFactory.getLogger(TeachingController.class);


	private MongoTemplate mainMongobyQueues;

	private DBCollection leave(){
		return mainMongo.getCollection("leave");
	}
	private DBCollection leave_detail(){
		return mainMongo.getCollection("leave_detail");
	}
	
	private DBCollection subspend_detail(){
		return mainMongo.getCollection("subspend_detail");
	}
	

	private DBCollection school_notice(){
		return mainMongo.getCollection("school_notice");
	}
	private DBCollection plan_courses(){
		return mainMongo.getCollection("plan_courses");
	}
	private DBCollection school_test(){
		return mainMongo.getCollection("school_test");
	}
	/** 学员信息 */
	public DBCollection plan_student() {
		return mainMongo.getCollection("plan_student");
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

	/** 课程列表 */
	public DBCollection bannerCourse() {
		return mainMongo.getCollection("banner_course");
	}
	
	/** 复课详情 */
	public DBCollection reschool_detail() {
		return mainMongo.getCollection("reschool_detail");
	}
	

	
	/** 休学单列表*/
	public DBCollection subspend() {
		return mainMongo.getCollection("subspend");
	}
	/** 学员商机*/
	public DBCollection hq_student_records() {
		return mainMongo.getCollection("hq_student_records");
	}
	
	
	private DBCollection boss_ana(){
		return mainMongo.getCollection('boss_ana');
	}
	
	DBCollection boss_record(){
		return mainMongo.getCollection('boss_record');
	}
	

	/**
	 * 获取现在时间
	 *
	 * @return 返回短时间字符串格式yyyy-MM-dd
	 */
	public static String getStringDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 总裁语录列表
	 */
	@ResponseBody
	@RequestMapping(value = "bossAnaList/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "总裁语录列表", httpMethod = "GET",  notes = "总裁语录列表")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "分页大小", required = false, dataType = "Integer", paramType = "query")]
	)
	def bossAnaList(HttpServletRequest request){
		//查询结果
		def queryResult = null;
		Integer page  = ServletRequestUtils.getIntParameter(request,"page", 1);
		Integer size  = ServletRequestUtils.getIntParameter(request,"size", 30);
		queryResult = boss_ana().find(null , $$("_id" : 1 , "boss_ana_title" : 1 , "boss_ana_info" : 1 , "boss_id" : 1)).sort($$(timestamp : -1)).skip((page - 1) * size).limit(size).toArray();
		if(queryResult){
			queryResult.each {def obj->
				if (obj['boss_id'] != null){
					def br = boss_record().findOne($$("_id" : obj['boss_id'] ));
					if(br){
						obj["boss_name"] = br["boss_name"];
						obj["boss_pic"] = br["boss_pic"];
						obj["boss_info"] = br["boss_info"];
					}
				}
			}
		}
		return getResultOK(queryResult);
	}
	@ResponseBody
	@RequestMapping(value = "bossAnaDetail/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "总裁语录详情", httpMethod = "GET",  notes = "总裁语录详情")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "boss_ana_id", value = "ID", required = true, dataType = "String", paramType = "query")
	])
	def bossAnaDetail(HttpServletRequest request){
		String _id = request["boss_ana_id"];
		def b = boss_ana().findOne($$("_id" : _id) , $$("_id" : 1 ,"boss_ana_title" : 1 , "boss_ana_info" : 1 ));
		return getResultOK(b);
	}
	
	/**
	 * 待办商机列表
	 */
	@ResponseBody
	@RequestMapping(value = "records_teacher/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "待办商机列表", httpMethod = "GET",  notes = "待办商机列表")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "分页大小", required = false, dataType = "Integer", paramType = "query")]
	)
	@TypeChecked(TypeCheckingMode.SKIP)
	def records_teacher(HttpServletRequest request){
		//用户id
		Integer user_id = Web.currentUserId;
		if(!isZS(user_id)){
			return getResult(Code.招生权限, Code.招生权限_S, Code.招生权限_S);
		}
		//查询结果
		def queryResult = null;
		Integer page  = ServletRequestUtils.getIntParameter(request,"page", 1);
		Integer size  = ServletRequestUtils.getIntParameter(request,"size", 30);
		
		int allpage = 0;
		int count = 0;
		//用户nc_id
		String nc_user_id = Web.currentUserNcId;
		//用户校区code
		def quser = users().findOne($$("_id" : Web.getCurrentUserId()) , $$("school_code" : 1,"sm_user_id":1));
		if(quser){
			String user_school_code = quser["school_code"];
			if(StringUtils.isNotBlank(user_school_code)){
				//校区
				def qarea = area().findOne($$("code" : user_school_code) , $$("nc_id" : 1));
				if(qarea){
					//校区nc_id
					String user_school_nc_id = qarea["nc_id"];
					String smuserid = quser["sm_user_id"];
					BasicDBObject query = $$(//查询条件
							"student_state":1,//1潜在  2无效  3已成交  4关闭
							$or : [
								$$("teacher_id" : smuserid),//属于该老师跟进的
								$$("teacher_id" : "~" , "area_nc_id" : user_school_nc_id)//属于同一个校区,没有指派老师跟进的
							] ,
							"dr" : 0
						);
					
					count = hq_student_records().count(query).intValue();
					allpage = count / size + ((count% size) >0 ? 1 : 0);
					
					if(count > 0){
						
						BasicDBObject show = $$("student_state_name" : 1 , "student_name" : 1 , "student_mobile" : 1 , "teacher_id" : 1);
						
						BasicDBObject qsort = $$( "dbilldate" : -1 , "syn_time" : -1);
						
						queryResult = hq_student_records().find(query,show).sort(qsort).skip((page - 1) * size).limit(size).toArray();
						
					}
				}
			}
		}
		
		return getResultOK(queryResult, allpage, count , page , size);
		
	}
	
	
	/**
	 * 请假单请求nc生成接口
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "addleave/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = " 请假单请求nc生成接口", httpMethod = "GET",  notes = " 请假单请求nc生成接口", response = DailyRecommandVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "course_id", value = "课程科目id", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "plan_bid", value = "排课计划子表id", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "nc_sign_id", value = "报名表主键", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "reason", value = "请假原因", required = false, dataType = "String", paramType = "query")]
	)
	def addleave(
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		String course_id  = ServletRequestUtils.getStringParameter(request, "course_id");
		String plan_bid  = ServletRequestUtils.getStringParameter(request, "plan_bid");
		String reason  = ServletRequestUtils.getStringParameter(request, "reason");
		String nc_sign_id  = ServletRequestUtils.getStringParameter(request, "nc_sign_id");
		
		def plancourse = plan_courses().findOne($$("nc_id",plan_bid));
		
		reason = sqlValidate(reason);
		//判断当天是否请过假
		if(leave_detail().count($$("planid":plan_bid))>0)
		{
			return getResult(-1,"当前课时已经请过假了");
		}
		JSONObject jo = new JSONObject();
	
		jo.put("XYBMH_ID", nc_sign_id);
		jo.put("STUDENTRECORDSID", Web.currentUserNcId);
		
		def signObject = signs().findOne($$("nc_id",nc_sign_id));
		if(signObject)
		{
		   jo.put("COURSESEQUENCEID",signObject.get("nc_commodity_id").toString());	
		   //由于存在转校的情况，因此校区不一定是学员的所属校区，必须以报名表为准
		   jo.put("PK_ORG", getSchoolPKBySchoolcode(signObject.get("school_code").toString()));
		}
		jo.put("BILLTYPE", "HJ08");
		jo.put("REMARK", reason);
		jo.put("ARRANGEDPLANH_ID", plancourse.get("nc_plan_id").toString());
		
		//获取排课主表
		
	


		JSONArray bodys = new JSONArray();
		if(plancourse)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sdf.format(new Date(Long.parseLong(plancourse.get("open_time").toString())));
			JSONObject item = new JSONObject();
			item.put("SESEH_ID", course_id);
			item.put("ARRANGEDPLANB_ID", plan_bid);
			item.put("COURSECON", plancourse.get("content"));
			item.put("SCHOOL_DATE", date);
			item.put("SCHOOL_TIME", plancourse.get("sessions"));
			bodys.put(item);
		}
		jo.put("body", bodys);
		messageProductorService.pushToMessageQueue(send_qname, jo.toString());


		return getResultOK();
	}
	
	/**
	 * 获取请假单列表
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "getleavelist/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "  获取请假单列表", httpMethod = "GET",  notes = "  获取请假单列表", response = DailyRecommandVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "分页大小", required = false, dataType = "Integer", paramType = "query")]
	)
	@TypeChecked(TypeCheckingMode.SKIP)
	def getleavelist(
			HttpServletRequest request
	){
	
			
			Integer page  = ServletRequestUtils.getIntParameter(request,"page", 1);
			Integer size  = ServletRequestUtils.getIntParameter(request,"size", 15);
			
			def liveList = getDataList("leave",page,size,Web.currentUserNcId);
			
			def liveData = liveList.get("data");
			
			liveData.each {item->
				
				def liveDetail  = leave_detail().findOne($$("nc_parent_id",item["nc_id"].toString()));
				def plan_name =   liveDetail.get("course_text")+" "+liveDetail.get("sktime");
				item.put("plan_name",plan_name);
				item.put("sktime",liveDetail.get("sktime"));
			}
						
			return liveList;
			

	}
	
	/**
	 * 获取请假单列表-教师
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "getleavelist_teacher/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取请假单列表-教师", httpMethod = "GET",  notes = "获取请假单列表-教师", response = DailyRecommandVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "分页大小", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "type", value = "0 待处理 1已处理", required = false, dataType = "Integer", paramType = "query")
		]
	)
	@TypeChecked(TypeCheckingMode.SKIP)
	def getleavelist_teacher(
			HttpServletRequest request
	){
	
	Integer user_id = Web.currentUserId;
	if(!isJX(user_id)){
		return getResult(Code.教务权限, Code.教务权限_S, Code.教务权限_S);
	}
			Integer page  = ServletRequestUtils.getIntParameter(request,"page", 1);
			Integer size  = ServletRequestUtils.getIntParameter(request,"size", 15);
			Integer type  = ServletRequestUtils.getIntParameter(request,"type", 0);
			
			def liveList = getTeacherDataList("leave",page,size,Web.currentUserSchoolCode,type);
			
			def liveData = liveList.get("data");
			
			liveData.each {item->
				
				def liveDetail  = leave_detail().findOne($$("nc_parent_id",item["nc_id"].toString()));
				def plan_name =   liveDetail.get("course_text")+" "+liveDetail.get("sktime");
			    def course_name = courses().findOne($$("nc_id",liveDetail.get("course_id").toString()))?.get("name");
				
				item.put("plan_name",plan_name);
				item.put("sktime",liveDetail.get("sktime"));
				item.put("course_name",course_name);
			
			}
						
			return liveList;
			

	}
	
	/**
	 * 获取请假单列表
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "getsubspendlist/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "  获取休学单列表", httpMethod = "GET",  notes = "  获取休学单列表", response = DailyRecommandVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "分页大小", required = false, dataType = "Integer", paramType = "query")]
	)
	@TypeChecked(TypeCheckingMode.SKIP)
	def getsubspendlist(
			HttpServletRequest request
	){
	
			
			Integer page  = ServletRequestUtils.getIntParameter(request,"page", 1);
			Integer size  = ServletRequestUtils.getIntParameter(request,"size", 15);
			
			
			
			def liveList = getDataList("subspend",page,size,Web.currentUserNcId);
			
			def liveData = liveList.get("data");
			
			liveData.each {item->
				
				if(liveData)
				{
				def liveDetail  = subspend_detail().findOne($$("nc_parent_id",item["nc_id"].toString()));
				def courseobj = courses().findOne($$("nc_id",liveDetail.get("course_id").toString()));
				def plan_name =   courseobj.get("name");
				item.put("plan_name",plan_name);
				}
		
			}
						
			return liveList;
			

	}
	
	/**
	 * 获取请假单列表
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "getsubspendlist_teacher/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "  获取休学单列表-教师", httpMethod = "GET",  notes = "  获取休学单列表-教师", response = DailyRecommandVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "分页大小", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "type", value = "0 待处理 1已处理", required = false, dataType = "Integer", paramType = "query")]
	)
	@TypeChecked(TypeCheckingMode.SKIP)
	def getsubspendlist_teacher(
			HttpServletRequest request
	){
	
			
			Integer page  = ServletRequestUtils.getIntParameter(request,"page", 1);
			Integer size  = ServletRequestUtils.getIntParameter(request,"size", 15);
			Integer type  = ServletRequestUtils.getIntParameter(request,"type", 0);
			Integer user_id = Web.currentUserId;
			if(!isJX(user_id)){
				return getResult(Code.教务权限, Code.教务权限_S, Code.教务权限_S);
			}
			
			def liveList = getTeacherDataList("subspend",page,size,Web.currentUserSchoolCode,type);
			
			def liveData = liveList.get("data");
			
			liveData.each {item->
				
				if(liveData)
				{
				def liveDetail  = subspend_detail().findOne($$("nc_parent_id",item["nc_id"].toString()));
				def courseobj = courses().findOne($$("nc_id",liveDetail.get("course_id").toString()));
				def plan_name =   courseobj.get("name");
				item.put("plan_name",plan_name);
				}
		
			}
						
			return liveList;
			

	}
	
	
	/**
	 * 获取请假单列表
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "getreschoollist/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "  获取复课单列表", httpMethod = "GET",  notes = " 获取复课单列表", response = DailyRecommandVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "分页大小", required = false, dataType = "Integer", paramType = "query")]
	)
	@TypeChecked(TypeCheckingMode.SKIP)
	def getreschoollist(
			HttpServletRequest request
	){
	
			
			Integer page  = ServletRequestUtils.getIntParameter(request,"page", 1);
			Integer size  = ServletRequestUtils.getIntParameter(request,"size", 15);
			
			
			
			def liveList = getDataList("reschool",page,size,Web.currentUserNcId);
			
			def liveData = liveList.get("data");
			
			liveData.each {item->
				
				def liveDetail  = reschool_detail().findOne($$("nc_parent_id",item["nc_id"].toString()));
				if(liveDetail)
				{
				  def courseobj = courses().findOne($$("nc_id",liveDetail.get("course_id").toString()));
				  def plan_name =   courseobj.get("name");
				  item.put("plan_name",plan_name);
				}
				else
				{
				   item.put("plan_name","");
				}
		
			}
						
			return liveList;
						
		
			

	}
	
	
	/**
	 * 获取请假单列表
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "getreschoollist_teacher/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "  获取复课单列表-教师", httpMethod = "GET",  notes = " 获取复课单列表-教师", response = DailyRecommandVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "分页大小", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "type", value = "0 待处理 1已处理", required = false, dataType = "Integer", paramType = "query")]
	)
	@TypeChecked(TypeCheckingMode.SKIP)
	def getreschoollist_teacher(
			HttpServletRequest request
	){
	
			
			Integer page  = ServletRequestUtils.getIntParameter(request,"page", 1);
			Integer size  = ServletRequestUtils.getIntParameter(request,"size", 15);
			Integer type  = ServletRequestUtils.getIntParameter(request,"type", 0);
			
			Integer user_id = Web.currentUserId;
			if(!isJX(user_id)){
				return getResult(Code.教务权限, Code.教务权限_S, Code.教务权限_S);
			}
			def liveList = getTeacherDataList("reschool",page,size,Web.currentUserSchoolCode,type);
			
			def liveData = liveList.get("data");
			
			liveData.each {item->
				
				def liveDetail  = reschool_detail().findOne($$("nc_parent_id",item["nc_id"].toString()));
				if(liveDetail)
				{
				  def courseobj = courses().findOne($$("nc_id",liveDetail.get("course_id").toString()));
				  def plan_name =   courseobj.get("name");
				  item.put("plan_name",plan_name);
				}
				else
				{
				   item.put("plan_name","");
				}
		
			}
						
			return liveList;
						
		
			

	}
	
	
	/**
	 * 获取转校单列表
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "gettransschoollist/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "  获取转校单列表", httpMethod = "GET",  notes = "  获取转校单列表", response = DailyRecommandVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "分页大小", required = false, dataType = "Integer", paramType = "query")]
	)
	@TypeChecked(TypeCheckingMode.SKIP)
	def gettransschoollist(
			HttpServletRequest request
	){
	
			

			
			Integer page  = ServletRequestUtils.getIntParameter(request,"page", 1);
			Integer size  = ServletRequestUtils.getIntParameter(request,"size", 15);
			
			
			
			def liveList = getDataList("trans_school",page,size,Web.currentUserNcId);
			
			def liveData = liveList.get("data");
			
			liveData.each {item->
				
				item.put("school_name",getSchoolNameBySchoolCode(item["school_code"].toString()));
				item.put("to_school_name",getSchoolNameBySchoolPK(item["to_school_pk"].toString()));
			}
						
			return liveList;
						

			

	}
	
	
	/**
	 * 获取转校单列表-教师
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "gettransschoollist_teacher/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "  获取转校单列表-教师", httpMethod = "GET",  notes = "  获取转校单列表-教师", response = DailyRecommandVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "分页大小", required = false, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "type", value = "0 待处理 1已处理", required = false, dataType = "Integer", paramType = "query")]
	)
	@TypeChecked(TypeCheckingMode.SKIP)
	def gettransschoollist_teacher(
			HttpServletRequest request
	){
	
			
			Integer page  = ServletRequestUtils.getIntParameter(request,"page", 1);
			Integer size  = ServletRequestUtils.getIntParameter(request,"size", 15);
			Integer type  = ServletRequestUtils.getIntParameter(request,"type", 0);
			Integer user_id = Web.currentUserId;
			if(!isJX(user_id)){
				return getResult(Code.教务权限, Code.教务权限_S, Code.教务权限_S);
			}
			def liveData =  getTeacherDataList("trans_school",page,size,Web.currentUserSchoolCode,type)
			
			liveData.each {item->
				
				if(item.hasProperty("school_code"))
				{
				item.put("school_name",getSchoolNameBySchoolCode(item["school_code"].toString()));
				}
				
				if(item.hasProperty("to_school_pk"))
				{
				item.put("to_school_name",getSchoolNameBySchoolCode(item["to_school_pk"].toString()));
				}
		
			}
			
			return liveData;
			

	}
	
	
	@TypeChecked(TypeCheckingMode.SKIP)
	private Map getDataList(String cname,int page,int size,String nc_user_id )
	{
		BasicDBObject query = new BasicDBObject();
		query.append("nc_user_id" , nc_user_id);
		
		
		//查询字段
		//排序
		def sort = $$("create_time_long" : -1);
	

			int count =mainMongo.getCollection(cname).count(query).intValue();
			int allpage = count / size + ((count% size) >0 ? 1 : 0);
			//查询结果
			def queryResult = null;
			if(count > 0){
				//需要查询的字段
	
				queryResult = mainMongo.getCollection(cname).find(query).sort(sort).skip((page - 1) * size).limit(size).toArray();
			}
			
			
			
			return getResultOK(queryResult, allpage, count , page , size);
	}
	
	
	@TypeChecked(TypeCheckingMode.SKIP)
	private Map getTeacherDataList(String cname,int page,int size,String school_code,int type )
	{
		BasicDBObject query = new BasicDBObject();
		query.append("school_code" , school_code);
		if(type == 0)
		{
			query.append("status" , -1);
		}
		else
		{
			query.append("status" , $$($ne,-1));
		}
		
		
		//查询字段
		//排序
		def sort = $$("create_time_long" : -1);
	

			int count =mainMongo.getCollection(cname).count(query).intValue();
			int allpage = count / size + ((count% size) >0 ? 1 : 0);
			//查询结果
			def queryResult = null;
			if(count > 0){
				//需要查询的字段
	
				queryResult = mainMongo.getCollection(cname).find(query).sort(sort).skip((page - 1) * size).limit(size).toArray();
				
			
				
				
				
				queryResult.each {item->
					
					def user_name = signs().findOne($$("nc_user_id",item["nc_user_id"].toString()))?.get("user_name");
					item.put("user_name",user_name);
				}
			}
			
			
			
			return getResultOK(queryResult, allpage, count , page , size);
	}


	/**
	 * 请假单请求nc生成接口
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "getleavedata/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取请假单单据数据", httpMethod = "GET",  notes = "获取请假单单据数据", response = DailyRecommandVO.class)
	def getleavedata(
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		String nc_user_id = Web.currentUserNcId;
		return getResultOK(signCourseList(nc_user_id));
	}
	
	
	
	/**
	 * 请假单请求nc生成接口
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "getsubspenddata/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取休学单据数据", httpMethod = "GET",  notes = "获取休学单据数据", response = DailyRecommandVO.class)
	def getsubspenddata(
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		String nc_user_id = Web.currentUserNcId;
		return getResultOK(getSignAndCourses(nc_user_id));
	}
	
	
	/**
	 * 休学单生成接口
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "addsubspend/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "休学单生成接口", httpMethod = "GET",  notes = "休学单生成接口", response = DailyRecommandVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "star_date", value = "开始休学时间 yyyy-MM-dd", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "end_date", value = "预计复课时间  yyyy-MM-dd", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "nc_sign_id", value = "报名表主键", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "subspend_reason", value = "休学原因", required = false, dataType = "String", paramType = "query")]
	)
	@TypeChecked(TypeCheckingMode.SKIP)
	def addsubspend(
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		String star_date  = ServletRequestUtils.getStringParameter(request, "star_date");
		String subspend_reason  = ServletRequestUtils.getStringParameter(request, "subspend_reason");
		String nc_sign_id  = ServletRequestUtils.getStringParameter(request, "nc_sign_id");
		
		subspend_reason = sqlValidate(subspend_reason);
		JSONObject jo = new JSONObject();
		
		def signObject = signs().findOne($$("nc_id",nc_sign_id));
		if(signObject)
		{
		   //由于存在转校的情况，因此校区不一定是学员的所属校区，必须以报名表为准
		   jo.put("PK_ORG", getSchoolPKBySchoolcode(signObject.get("school_code").toString()));
		}
		jo.put("PK_ORG", getSchoolPKBySchoolcode(Web.currentUserSchoolCode));
		jo.put("XYBMH_ID", nc_sign_id);
		jo.put("BILLTYPE", "HJ10");
		jo.put("STUDENTRECORDSID", Web.currentUserNcId);
		jo.put("STARTDATE", star_date);
		jo.put("VREASON", subspend_reason);
		
		List courseList = CourseList(Web.currentUserNcId);
		JSONArray bodys = new JSONArray();
		if(courseList)
		{
			courseList.each{sub->
				
				JSONObject item = new JSONObject();
				item.put("SESEH_ID", sub.get("nc_course_id").toString());
				item.put("PKSTATUS",  sub.get("plan_state"));
				bodys.put(item);
			}
			
		}
		jo.put("body", bodys);

		messageProductorService.pushToMessageQueue(send_qname, jo.toString());

		return getResultOK();
	}


	/**
	 * 休学单生成接口
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "addreschool/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "复课单生成接口", httpMethod = "GET",  notes = "复课单生成接口", response = DailyRecommandVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "return_date", value = "计划复课时间 yyyy-MM-dd", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "subspend_school_id", value = "休学单据的ncid", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "reason", value = "复课原因", required = false, dataType = "String", paramType = "query")]
	)
	@TypeChecked(TypeCheckingMode.SKIP)
	def addreschool(
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		String return_date  = ServletRequestUtils.getStringParameter(request, "return_date");
		String subspend_school_id  = ServletRequestUtils.getStringParameter(request, "subspend_school_id");
		String reason  = ServletRequestUtils.getStringParameter(request, "reason");
		reason = sqlValidate(reason);
		
		JSONObject jo = new JSONObject();
		jo.put("PK_ORG", getSchoolPKBySchoolcode(Web.currentUserSchoolCode));
		jo.put("STUDENTRECORDSID", Web.currentUserNcId);
		jo.put("BILLTYPE", "HJ11");
		jo.put("DRESCHOOL_DATE", return_date);
		jo.put("CUNSCHOOL_ID", subspend_school_id);
		
		
		List courseList = CourseList(Web.currentUserNcId);
		JSONArray bodys = new JSONArray();
		if(courseList)
		{
			courseList.each{sub->
				
				JSONObject item = new JSONObject();
				item.put("SESEH_ID", sub.get("nc_course_id").toString());
				bodys.put(item);
			}
			
		}
		jo.put("body", bodys);

		messageProductorService.pushToMessageQueue(send_qname, jo.toString());

		return getResultOK();
	}

	/**
	 * 休学单生成接口
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "getreschooldata/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取复课单据操作所需数据", httpMethod = "GET",  notes = "获取复课单据操作所需数据", response = DailyRecommandVO.class)
	@TypeChecked(TypeCheckingMode.SKIP)
	def getreschooldata(
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO

	def liveData = subspend().find($$("nc_user_id": Web.currentUserNcId,  "dr" : 0,"status":1)).toArray();
	

	
	liveData.each {item->
		
		if(liveData)
		{
		 def liveDetail  = subspend_detail().findOne($$("nc_parent_id",item["nc_id"].toString()));
		 def courseobj = courses().findOne($$("nc_id",liveDetail.get("course_id").toString()));
		 def plan_name =   courseobj.get("name");
		 item.put("plan_name",plan_name);
		}

	}
	
	
		return getResultOK(liveData);
	}

	/**
	 * 休学单生成接口
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "addtransschool/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "转校的生成接口", httpMethod = "GET",  notes = "转校单生成接口", response = DailyRecommandVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "plan_id", value = "报名表的主键id", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "target_school_pkid", value = "目标校区id", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "reason", value = "转校原因", required = false, dataType = "String", paramType = "query")]
	)
	def addtransschool(
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		String plan_id  = ServletRequestUtils.getStringParameter(request, "plan_id");
		String target_school_pkid  = ServletRequestUtils.getStringParameter(request, "target_school_pkid");
		String reason  = ServletRequestUtils.getStringParameter(request, "reason");
		reason = sqlValidate(reason);
		

		JSONObject jo = new JSONObject();
		jo.put("PK_ORG", getSchoolPKBySchoolcode(Web.currentUserSchoolCode));
		jo.put("STUDENTRECORDSID", Web.currentUserNcId);
		jo.put("BILLTYPE", "HJ14");
		jo.put("BILL_DATE", getStringDateShort());
		jo.put("TRANSFER_REASON", reason);
		jo.put("TSCHOOL", target_school_pkid);
		jo.put("XYBMH_ID", plan_id);

		messageProductorService.pushToMessageQueue(send_qname, jo.toString());

		return getResultOK();
	}
	
	/**
	 * 休学单生成接口
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "gettransschooldata/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取转校申请需要的数据", httpMethod = "GET",  notes = "获取转校申请需要的数据", response = DailyRecommandVO.class)
	def gettransschooldata(
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
	    Map data = new HashMap();

		data.put("schooldata", schoolController.get_province_school_list());
		data.put("signdata", getSignAndCourses(Web.currentUserNcId));

		return getResultOK(data);
	}
	//schoolController

	def getSignAndCourses(String nc_user_id)
	{
		def signList = signs().find(
				$$("nc_user_id" : nc_user_id , "status" : $$('$in' : [
					SignsStatus.在读.ordinal() ,
					SignsStatus.休学.ordinal() ,
					SignsStatus.毕业.ordinal()
				]) , "dr" : DR.正常.ordinal()) ,
				$$("nc_id" : 1 , "nc_commodity_id" : 1 , "code" : 1, "status" : 1 , "order_no" : 1,"school_name":1,"ks_name":1,"classtype":1)//NC_id  商品的NCid code  状态
				).sort($$("status" : 1 , "create_time" : 1)).limit(MAX_LIMIT)?.toArray();
	}

	/**
	 * 查询报名表课程-分成两组数据 面授,直播
	 * @param nc_sign_id
	 * @param nc_user_id
	 * @return				map["down"] = 面授 ; map["online"] = 直播
	 */
	private List signCourseList(String nc_user_id){


		//面授
		List course_list = new ArrayList();

		def signList = signs().find(
				$$("nc_user_id" : nc_user_id , "status" : $$('$in' : [
					SignsStatus.在读.ordinal() ,
					SignsStatus.休学.ordinal() ,
					SignsStatus.毕业.ordinal()
				]) , "dr" : DR.正常.ordinal()) ,
				$$("nc_id" : 1 , "nc_commodity_id" : 1 , "code" : 1, "status" : 1 , "school_name":1)//NC_id  商品的NCid code  状态
				).sort($$("status" : 1 , "create_time" : 1)).limit(MAX_LIMIT)?.toArray();


		//初始化
		Map map = new HashMap();
		//面授
		List downList = new ArrayList();
		//直播
		List onlineList = new ArrayList();

		signList.each {def signitem->

			String nc_sign_id = signitem["nc_id"].toString();
			
			//报名表课程科目
			def signCourseList = sign_course().find(
					$$("nc_sign_id" : nc_sign_id,"status":SignCourseState.已排课.ordinal(),"dr":DR.正常.ordinal()),
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
					courseMap["nc_course_id"] = nc_course_id;
					
				
					//面授和线上标志  0.面授 1.线上
					Integer lineType = 0;
					//报名表中的排课状态 1.已排课 2.未排课 3.已完课
					Integer status = Integer.valueOf(item["status"].toString());

				

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
						


					def planCourse = plan_courses().find($$("nc_plan_id" : nc_plan_id , "dr" : DR.正常.ordinal() ) , $$("nc_teacher_name" : 1 , "content" : 1 , "nc_id" : 1 , "open_time" : 1)).sort($$("open_time" : 1)).limit(MAX_LIMIT).toArray();
					if(planCourse){
						
						//课时列表
						List planList = new ArrayList();
						courseMap["planList"] = planList;
						
						planCourse.each {def pi->
							Map planMap = new HashMap();
							Long open_time = planOpenTimeStringToLong(pi["open_time"]);
							
							//大于当天的才显示出来
							if(open_time>now)
							{
								
							
							planMap["plan_body_nc_id"] = pi["nc_id"];
							//课时名称
							planMap["name"] = pi["content"];
							//老师名称
							planMap["teacher_name"] = pi["nc_teacher_name"];
							//状态
							planMap["time"] = planOpenTimeFormate(open_time);
							
							planMap["nc_sign_id"] = nc_sign_id;
							

							//课时放入课时列表
							planList.add(planMap);
							}
						}
						
						if(planList.size()>0)
						{
					    	course_list.add(courseMap);
						}
					}



				}


			}
		} 


		return course_list;
	}
	
	
	/**
	 * 查询报名表课程-分成两组数据 面授,直播
	 * @param nc_sign_id
	 * @param nc_user_id
	 * @return				map["down"] = 面授 ; map["online"] = 直播
	 */
	private List CourseList(String nc_user_id){


		//面授
		List course_list = new ArrayList();

		def signList = signs().find(
				$$("nc_user_id" : nc_user_id , "status" : $$('$in' : [
					SignsStatus.在读.ordinal() ,
					SignsStatus.休学.ordinal() ,
					SignsStatus.毕业.ordinal()
				]) , "dr" : DR.正常.ordinal()) ,
				$$("nc_id" : 1 , "nc_commodity_id" : 1 , "code" : 1, "status" : 1 , "school_name":1)//NC_id  商品的NCid code  状态
				).sort($$("status" : 1 , "create_time" : 1)).limit(MAX_LIMIT)?.toArray();


		//初始化
		Map map = new HashMap();
		//面授
		List downList = new ArrayList();
		//直播
		List onlineList = new ArrayList();

		signList.each {def signitem->

			String nc_sign_id = signitem["nc_id"].toString();

			//报名表课程科目
			def signCourseList = sign_course().find(
					$$("nc_sign_id" : nc_sign_id,"status":$$($ne,SignCourseState.已完课.ordinal()),"dr":DR.正常.ordinal()),
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
					courseMap["nc_course_id"] = nc_course_id;
					courseMap["plan_state"] = Integer.valueOf(item["status"].toString());
					//courseMap["nc_sign_id"] = item["nc_sign_id"].toString();
					course_list.add(courseMap);
			

				}


			}
		}


		return course_list;
	}


	def getSchoolPKBySchoolcode(String schoolcode)
	{
		return area().findOne($$("code":schoolcode))?.get("nc_id");
	}
	def getSchoolNameBySchoolPK(String schoolpk)
	{
		return area().findOne($$("nc_id":schoolpk))?.get("name");
	}
	def getSchoolNameBySchoolCode(String schoolcode)
	{
		return area().findOne($$("code":schoolcode))?.get("name");
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
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
			return formatter.format(new Date(time));
		}
		return "-";
	}
	
	
	//过滤特殊字符串
	protected static String sqlValidate(String str) {
		str = str.toLowerCase();//统一转为小写
		String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|" +
				"char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
				"table|from|grant|use|group_concat|column_name|" +
				"information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|" +
				"chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";//过滤掉的sql关键字，可以手动添加
		String[] badStrs = badStr.split("\\|");
		for (int i = 0; i < badStrs.length; i++) {
			if (str.indexOf(badStrs[i]) >= 0) {
				str = str.replace(badStrs[i], "");
			}
		}
		return str;
		
	}



	


}




