package com.izhubo.web.server

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.hqonline.model.Privs
import com.izhubo.common.util.KeyUtils
import com.izhubo.model.*
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.util.JSONUtil
import com.izhubo.rest.common.util.NumberUtil
import com.izhubo.rest.common.util.http.HttpClientUtil4_3
import com.izhubo.utils.DataUtils
import com.izhubo.web.BaseController
import com.izhubo.web.DiscountController
import com.izhubo.web.api.Web
import com.izhubo.web.mobile.TeacherController
import com.izhubo.web.vo.*
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mysqldb.model.Orders
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.apache.commons.lang3.StringUtils
import org.hibernate.Query
import org.hibernate.SessionFactory
import org.hibernate.criterion.Restrictions
import org.hibernate.transform.Transformers
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import java.text.SimpleDateFormat

import static com.izhubo.rest.common.util.WebUtils.$$

@RestWithSession
@RequestMapping("/userinfo")
class UserInfoController extends BaseController {

	@Resource
	private SessionFactory sessionFactory;

	/** pc考前冲刺 */
	@Value("#{application['pc.liveclass.domain']}")
	String PC_LIVECLASS_DOMAIN = null;
	/** app考前冲刺 */
	@Value("#{application['app.liveclass.domain']}")
	String APP_LIVECLASS_DOMAIN = null;
	/** 根据学员获取课程 */
	@Value("#{application['school.api.domain']}")
    private String SCHOOL_API_DOMAIN = null;

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
	//	/**	收支明细 */
	//	public DBCollection user_incom_log() {
	//		return logMongo.getCollection("user_incom_log");
	//	}

	public DBCollection c(){
		return logMongo.getCollection("shares_log");
	}


	@Resource
	private TeacherController teacherController;
	@Resource
	private ScoreController scoreController;
	@Resource
	private DiscountController discountController;
	@Resource
	private UserIncomController userincomController;


	/**
	 * 我的班级-班级列表
	 */
	@ResponseBody
	@RequestMapping(value = "myClassInfoList/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "我的班级-班级详细信息列表-pc", httpMethod = "GET" , response = UserinfomyClassInfoListVO.class , notes = "我的班级-班级列表")
	@TypeChecked(TypeCheckingMode.SKIP)
	def myClassInfoList(HttpServletRequest request){
		//用户id
		String nc_user_id = Web.getCurrentUserNcId();

		//nc_plan_id集合
		def ncPlanIdArray = plan_student().find($$("nc_user_id" : nc_user_id , "dr" : DR.正常.ordinal()), $$("nc_plan_id" : 1))?.toArray()?.collect{DBObject it->it.get("nc_plan_id")}
		def classPlanList = null;
		if(ncPlanIdArray){
			classPlanList = class_plan().find(
					$$("nc_id" : $$('$in' : ncPlanIdArray) , "dr" : DR.正常.ordinal()) ,
					$$(
					"_id" : 1 	,"name" : 1 		, "class_time" : 1 		, "classroom" : 1  , "open_time" : 1,
					"nc_id" : 1 , "nc_area_id" : 1	, "nc_course_id" : 1    , "status" : 1,
					"course_name" : 1 , "nc_teacher_name" : 1
					)
					).sort($$("open_time" : 1 , "_id" : 1)).limit(MAX_LIMIT)?.toArray();

			if(classPlanList){
				classPlanList.each {def item->
					//订单图片
					String pic = null;
					//课程code:排课计划中课程id->关联课程表查出课程code
					String course_code = commodity_courses().findOne($$("nc_id" : item["nc_course_id"]) , $$("course_code" : 1))?.get("course_code");
					if(StringUtils.isNotBlank(course_code)){
						//nc报名表id
						String nc_sign_id = sign_course().findOne($$("course_code" : course_code) , $$("nc_sign_id" : 1))?.get("nc_sign_id");
						if(StringUtils.isNotBlank(nc_sign_id)){
							String order_no = signs().findOne($$("nc_id" : nc_sign_id) , $$("order_no" : 1))?.get("order_no");
							if(order_no){
								//订单信息
								Orders order = (Orders)sessionFactory.getCurrentSession().createCriteria(Orders.class)
										.add(Restrictions.eq(Orders.PROP_ORDERNO , order_no))//订单号
										.uniqueResult();
								if(order){
									pic = order.getPic();
								}//		end if(order){
							}//			end if(order_id != null){
						}//				end if(StringUtils.isNotBlank(nc_sign_id)){
					}//					end if(StringUtils.isNotBlank(course_code)){
					//图片
					item["pic"] = pic;

					//学员信息
					def studentArray = plan_student().find($$("nc_plan_id" : item["nc_id"] , "dr" : DR.正常.ordinal()) , $$("name" : 1)).sort($$("nc_user_id" : 1)).limit(MAX_LIMIT)?.toArray()?.collect{DBObject it->it.get("name")}
					item["studetList"] = studentArray;

					//校区
					item["school_name"] = area().findOne($$("nc_id" : item["nc_area_id"]) , $$("name" : 1))?.get("name");

					item.removeField("nc_id");
					//					item.removeField("nc_out_teacher_id");
					item.removeField("nc_area_id");
				}
			}
		}
		return getResultOK(classPlanList);
	}



	/**
	 * 学习中心报名表列表-h5
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "mySignListH5/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "学习中心报名表列表-h5", httpMethod = "GET" , response = UserinfoMySignListH5VO.class,notes = "学习中心报名表列表-h5")
	@TypeChecked(TypeCheckingMode.SKIP)
	def mySignListH5(HttpServletRequest request){
		return getResultOK(signTitleInfos(request));
		//		return mySignList(request);
	}


	/**
	 * 学习中心-报名表详情-h5
	 */
	@ResponseBody
	@RequestMapping(value = "mySignInfoByNcIdH5/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "学习中心-报名表详情-h5", httpMethod = "GET" , response = UserinfoMySignInfoByNcIdH5VO.class , notes = "学习中心-报名表详情-h5")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "nc_id", value = "报名表id-如果为空,先获取上次浏览历史;浏览历史为空,则从取最新的报名表", required = false, dataType = "String", paramType = "query")
	])
	@TypeChecked(TypeCheckingMode.SKIP)
	def mySignInfoByNcIdH5(HttpServletRequest request){
		return mySignInfoByNcId(request, 1);
	}

	/**
	 * 学习中心-报名表详情-web
	 */
	@ResponseBody
	@RequestMapping(value = "mySignInfoByNcId/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "学习中心-报名表详情-web", httpMethod = "GET" , response = UserinfoMySignInfoByNcIdVO.class , notes = "学习中心-报名表详情-web")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "nc_id", value = "报名表id-如果为空,先获取上次浏览历史;浏览历史为空,则从取最新的报名表", required = false, dataType = "String", paramType = "query")
	])
	@TypeChecked(TypeCheckingMode.SKIP)
	def mySignInfoByNcId(HttpServletRequest request){
		return mySignInfoByNcId(request, 0);
	}

	def mySignInfoByNcId(HttpServletRequest request , int liveType){
		//用户id
		String nc_user_id = Web.getCurrentUserNcId();
		Integer user_id = Web.getCurrentUserId();
		//NC报名表id 如果没有排课计划id-从最近浏览中获取,如果最近浏览记录没有,获取报名表最近记录
		String nc_sign_id = request["nc_id"];


		//如果为空-尝试从浏览历史表中获取最近浏览的课程
		if(StringUtils.isBlank(nc_sign_id)){
			def signLog = sign_logs().find($$("user_id" : user_id), $$("nc_sign_id" : 1)).sort($$("timestamp" : -1)).limit(1)?.toArray();
			if(signLog){
				nc_sign_id = signLog.get(0).get("nc_sign_id");

				if(StringUtils.isNotBlank(nc_sign_id)){
					if(signs().findOne($$("nc_id" : nc_sign_id)) == null)nc_sign_id = null;
				}
			}
		}
		//如果为空-尝试从报名表中获取最近的
		if(StringUtils.isBlank(nc_sign_id)){
			def signItem = signs().find($$("nc_user_id" : nc_user_id ) , $$("nc_id" : 1)).sort($$("create_time" : -1)).limit(1)?.toArray();
			if(signItem){
				nc_sign_id = signItem.get(0).get("nc_id");
			}
		}


		Map map = signTitleInfo(nc_sign_id);//signTitleInfo(nc_sign_id);

		if(map){
			//			map[""] = 1;
			Map scMap = signCourseList(nc_sign_id, nc_user_id);
			if(scMap){
				//面授
				map["downList"] = scMap["downList"];
				//直播
				map["onlineList"] = scMap["onlineList"];


			}
			//记录用户浏览报名表的记录 下次进入报名表 默认获取上次浏览的最近记录
			saveSignLog(user_id, nc_sign_id);

		}
		return getResultOK(map);
	}

	/**
	 * 记录用户浏览报名表的记录 下次进入报名表 默认获取上次浏览的最近记录
	 * @param user_id		用户id
	 * @param nc_sign_id	报名表NCid
	 */
	private void saveSignLog(int user_id , String nc_sign_id){
		if(user_id > 0 && StringUtils.isNotBlank(nc_sign_id)){
			sign_logs().save($$("_id" : user_id + "_" + nc_sign_id  , "user_id" : user_id , "nc_sign_id" : nc_sign_id , "timestamp" : System.currentTimeMillis()));
		}
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
		//直播
		map["onlineList"] = onlineList;

		//报名表课程科目
		def signCourseList = sign_course().find(
				$$("nc_sign_id" : nc_sign_id,"dr":DR.正常.ordinal())  ,
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
				if(status == SignCourseState.已排课.ordinal() || status == SignCourseState.已完课.ordinal()){
					//排课计划id


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

					def planCourse = plan_courses().find($$("nc_plan_id" : nc_plan_id , "dr" : DR.正常.ordinal() ) , $$("nc_teacher_name" : 1 , "content" : 1 , "open_time" : 1)).sort($$("open_time" : 1)).limit(MAX_LIMIT).toArray();
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

							//题库
							planMap["tiku_url"] = tikuUrl(nc_course_id);
							//实训
							planMap["shixun_url"] = shixuUrl(nc_course_id);
							//直播
							planMap["live_url"] = zhiboUrl(nc_course_id);

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

							//课时放入课时列表
							planList.add(planMap);
						}
					}

				}
				//面授和线上标志  0.面授 1.线上
				if(lineType == 0){
					downList.add(courseMap);
				}else if(lineType == 1){
					onlineList.add(courseMap);
				}

			}

		}
		return map;
	}

	/**
	 * 考前冲刺
	 * @param liveType 0.PC  1.APP
	 * @return	考前冲刺结果集
	 */
	private List liveClassList(int liveType){
		List list = new ArrayList();
		//系统当前时间
		Long now = System.currentTimeMillis();

		def liveClassList = live_class().find($$("dr" : 0) , $$("_id" : 1 , "name" : 1)).sort($$("order" : 1)).limit(MAX_LIMIT).toArray();
		if(liveClassList){
			liveClassList.each {def live->
				Map liveMap = new HashMap();
				liveMap["name"] = live["name"]
				List itemList = new ArrayList();
				liveMap["item"] = itemList;
				//查出 直播只要未结束
				def liveClassItemList = live_class_item().find(
						$$("live_class_id" : live["_id"] , "end_time" : $$('$gte' : now) , "dr" : 0),
						$$("start_time" : 1 , "end_time" : 1 , "name" : 1 , "pc_live_param" : 1 , "teacher_name" : 1)
						).sort($$("start_time" : 1)).limit(MAX_LIMIT).toArray();
				if(liveClassItemList){
					liveClassItemList.each {def item->
						Map map = new HashMap();
						itemList.add(map);
						//直播名称
						map["name"] = item["name"];
						//直播老师名称
						map["teacher_name"] = item["teacher_name"];
						//直播开始时间
						Long start_time = planOpenTimeStringToLong(item["start_time"]);
						map["time"] = planOpenTimeFormate(start_time);
						//状态
						map["state"] = liveClassStateString(start_time);
						//直播间地址
						if(liveType == 0){
							map["live_url"] = liveClassUrlPC(item["pc_live_param"]?.toString());
						}else{
							map["live_url"] = liveClassUrlAPP(item["pc_live_param"]?.toString());
						}
					}
				}
				list.add(liveMap);
			}
		}
		return list;
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
		return "未开始";
	}

	/**
	 * 考前冲刺状态
	 * @param startTime		开始时间
	 * @param endTime		结束时间
	 * @return				未开始/已开始
	 */
	private String liveClassStateString(Long startTime){
		Long now = System.currentTimeMillis();
		if(startTime > now){
			return "未开始";
		}else{
			return "已开始"
		}
	}

	/**
	 * PC直播地址
	 * @param param
	 * @return
	 */
	private String liveClassUrlPC(String param){
		return PC_LIVECLASS_DOMAIN + param;
	}

	/**
	 * APP直播地址
	 * @param param
	 * @return
	 */
	private String liveClassUrlAPP(String param){
		return APP_LIVECLASS_DOMAIN + param;
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
	 * 学习中心-报名表题头信息
	 * @param nc_sign_id	 报名表NCid
	 * @return				Map{"nc_id" : "报名表NCid" , "commodity_name" : 商品名称" , "commodity_id" : "商品id", "crouse_num" : "总课程数", "crouse_down_num" : "面授总课程数" , "crouse_online_num" : "在线总课程数" , "progress" : "学习进度"}
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	private List signTitleInfos(HttpServletRequest request){

		String nc_user_id = Web.getCurrentUserNcId();
		Integer user_id = Web.getCurrentUserId();
		//报名表集合
		List signResultList = new ArrayList();
		//学员报名表集合
		def signList = signs().find(
				$$("nc_user_id" : nc_user_id , "status" : $$('$in' : [
					SignsStatus.在读.ordinal() ,
					SignsStatus.休学.ordinal() ,
					SignsStatus.毕业.ordinal()
				]) , "dr" : DR.正常.ordinal()) ,
				$$("nc_id" : 1 , "nc_commodity_id" : 1 , "code" : 1, "status" : 1 , "order_no" : 1)//NC_id  商品的NCid code  状态
				).sort($$("status" : 1 , "create_time" : 1)).limit(MAX_LIMIT)?.toArray();

		if(signList){
			signList.each {def item ->
				//报名表NCid
				String nc_sign_id = item["nc_id"];

				Map map = new HashMap();
				//排课计划nc_id
				map["nc_sign_id"] = nc_sign_id;

				if(item){
					//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 基础信息  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

					//商品信息
					def commodity = commoditys().findOne($$("nc_id" : item["nc_commodity_id"]) , $$("name" : 1 , "_id" : 1));
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
					//商品名称
					map["commodity_name"] = commodity_name;
					//商品id
					map["commodity_id"] = commodity_id;

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
							$$("nc_sign_id" : nc_sign_id,"dr":DR.正常.ordinal())  ,
							$$("course_code" : 1  , "status" : 1)
							).sort($$("course_code" : 1)).limit(MAX_LIMIT).toArray();
					if(signCourseList){
						signCourseList.each {def sc_item->
							//状态
							Integer status = Integer.valueOf(sc_item["status"].toString());

							String nc_course_id = courses().findOne($$("code" : sc_item["course_code"].toString()) , $$("nc_id" : 1))?.get("nc_id");

							//					是否是线上 0.否 1.是
							Integer is_online = couresIsOnline(nc_course_id );
							if(SignCourseState.已完课.ordinal() == status){
								//已排课数量
								Integer ks = classPlanFinishNum(nc_course_id ,nc_sign_id, nc_user_id);
								if(is_online == 0){
									crouse_down_num     += ks;
									crouse_down_fin_num += ks;
								}else{
									crouse_online_num     += ks;
									crouse_online_fin_num += ks;
								}
							}else if(SignCourseState.未排课.ordinal() == status){
								Integer ks = coursesContentNum(nc_course_id);
								if(is_online == 0){
									crouse_down_num   += ks;
								}else{
									crouse_online_num += ks;
								}
							}else if(SignCourseState.已排课.ordinal() == status){
								//未完成
								Integer[] array = classPlanClassing(nc_course_id ,nc_sign_id, nc_user_id);
								if(is_online == 0){
									crouse_down_fin_num += array[0];
									crouse_down_num     += array[0] + array[1];
								}else{
									crouse_online_fin_num += array[0];
									crouse_online_num     += array[0] + array[1];
								}
							}
						}
					}

					//总课程数
					Integer crouse_num = crouse_down_num + crouse_online_num
					map["crouse_num"] = crouse_num;
					//面授总课程数
					map["crouse_down_num"] = crouse_down_num;
					//在线总课程数
					map["crouse_online_num"] = crouse_online_num;
					//学习进度
					if(crouse_num > 0){
						map["progress"] = new BigDecimal(crouse_down_fin_num + crouse_online_fin_num).divide(new BigDecimal(crouse_num) , 2);;
					}else{
						map["progress"] = 0d;
					}

					//					//考前冲刺数量
					//					map["liveclass_num"] = liveClassNum();
					signResultList.add(map);
				}
			}
		}

		return signResultList;
	}
	/**
	 * 学习中心-报名表题头信息
	 * @param nc_sign_id	 报名表NCid
	 * @return				Map{"nc_id" : "报名表NCid" , "commodity_name" : 商品名称" , "commodity_id" : "商品id", "crouse_num" : "总课程数", "crouse_down_num" : "面授总课程数" , "crouse_online_num" : "在线总课程数" , "progress" : "学习进度"}
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	private Map signTitleInfo(String nc_sign_id){
		Map result = null;

		String nc_user_id = Web.getCurrentUserNcId();
		Integer user_id = Web.getCurrentUserId();
		//报名表集合
		//		List signResultList = new ArrayList();
		//学员报名表集合
		def signItem = signs().findOne(
				$$("nc_id" : nc_sign_id ,"nc_user_id" : nc_user_id , "status" : $$('$in' : [
					SignsStatus.在读.ordinal() ,
					SignsStatus.休学.ordinal() ,
					SignsStatus.毕业.ordinal()
				]) , "dr" : DR.正常.ordinal()) ,
				$$("nc_id" : 1 , "nc_commodity_id" : 1 , "code" : 1, "status" : 1 , "order_no" : 1)//NC_id  商品的NCid code  状态
				);
		if(signItem){
			//报名表NCid
			//			String nc_sign_id = item["nc_id"];
			result = new HashMap();


			//排课计划nc_id
			result["nc_sign_id"] = nc_sign_id;

			//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 基础信息  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

			//商品信息
			def commodity = commoditys().findOne($$("nc_id" : signItem["nc_commodity_id"]) , $$("name" : 1 , "_id" : 1));
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
			//商品名称
			result["commodity_name"] = commodity_name;
			//商品id
			result["commodity_id"] = commodity_id;

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
					$$("nc_sign_id" : nc_sign_id,"dr":DR.正常.ordinal())  ,
					$$("course_code" : 1  , "status" : 1)
					).sort($$("course_code" : 1)).limit(MAX_LIMIT).toArray();
			if(signCourseList){
				signCourseList.each {def sc_item->
					//状态
					Integer status = Integer.valueOf(sc_item["status"].toString());

					String nc_course_id = courses().findOne($$("code" : sc_item["course_code"].toString()) , $$("nc_id" : 1))?.get("nc_id");

					//					是否是线上 0.否 1.是
					Integer is_online = couresIsOnline(nc_course_id );
					if(SignCourseState.已完课.ordinal() == status){
						//已排课数量
						Integer ks = classPlanFinishNum(nc_course_id ,nc_sign_id, nc_user_id);
						if(is_online == 0){
							crouse_down_num     += ks;
							crouse_down_fin_num += ks;
						}else{
							crouse_online_num     += ks;
							crouse_online_fin_num += ks;
						}
					}else if(SignCourseState.未排课.ordinal() == status){
						Integer ks = coursesContentNum(nc_course_id);
						if(is_online == 0){
							crouse_down_num   += ks;
						}else{
							crouse_online_num += ks;
						}
					}else if(SignCourseState.已排课.ordinal() == status){
						//未完成
						Integer[] array = classPlanClassing(nc_course_id ,nc_sign_id, nc_user_id);
						if(is_online == 0){
							crouse_down_fin_num += array[0];
							crouse_down_num     += array[0] + array[1];
						}else{
							crouse_online_fin_num += array[0];
							crouse_online_num     += array[0] + array[1];
						}
					}
				}
			}

			//总课程数
			Integer crouse_num = crouse_down_num + crouse_online_num;
			//			result["crouse_num"] = crouse_num;
			//			//面授总课程数
			//			result["crouse_down_num"] = crouse_down_num;
			//			//在线总课程数
			//			result["crouse_online_num"] = crouse_online_num;
			//学习进度
			if(crouse_num > 0){
				result["progress"] = new BigDecimal(crouse_down_fin_num + crouse_online_fin_num).divide(new BigDecimal(crouse_num) , 2);;
			}else{
				result["progress"] = 0d;
			}
		}

		return result;
	}

	/**
	 * 根据订单id获取未生成报名表的信息
	 * @param order_id
	 * @return
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	private Map unSign(Integer order_id){
		if(order_id != null && order_id > 0){

			//用户id
			Integer user_id = Web.getCurrentUserId();
			String nc_user_id = Web.getCurrentUserNcId();
			//订单信息
			Orders order = (Orders)sessionFactory.getCurrentSession().createCriteria(Orders.class)
					.add(Restrictions.eq(Orders.PROP_ID , order_id))//订单号
					.add(Restrictions.eq(Orders.PROP_USERID , user_id))//用户id
					.add(Restrictions.eq(Orders.PROP_STATUS , OrderStatus.有效.ordinal()))//是否删除
					.uniqueResult();
			//订单存在-线上购买
			if(order){
				Map map = new HashMap();
				//排课计划nc_id
				map["order_id"] = order_id;

				//商品名称
				map["commodity_name"] = order.getOrderName();
				//商品id
				map["commodity_id"] = order.getProductId();

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

				def course_ids = commoditys().findOne($$("_id" : order.getProductId()) , $$("course_ids" : 1))?.get("course_ids");
				if(course_ids && course_ids.size() > 0){
					course_ids.each {String nc_course_id->
						Integer ks = coursesContentNum(nc_course_id);
						//					是否是线上 0.否 1.是
						Integer is_online = couresIsOnline(nc_course_id );
						if(is_online == 0){
							crouse_down_num   += ks;
						}else{
							crouse_online_num += ks;
						}
					}
				}
				//面授总课程数
				map["crouse_down_num"] = crouse_down_num;
				//在线总课程数
				map["crouse_online_num"] = crouse_online_num;
				//学习进度
				map["progress"] = 0d;

				//考前冲刺数量
				map["liveclass_num"] = liveClassNum();

				return map;
			}
		}
		return null;
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
	 * 题库跳转地址
	 * @param nc_course_id
	 * @return
	 */
	private String tikuUrl(String nc_course_id){
		String url = null;
		url = commodity_courses().findOne($$("nc_id" : nc_course_id) , $$("tiku_url" : 1))?.get("tiku_url");
		return url;
	}
	/**
	 * 实训跳转地址
	 * @param nc_course_id
	 * @return
	 */
	private String shixuUrl(String nc_course_id){
		String url = null;
		url = commodity_courses().findOne($$("nc_id" : nc_course_id) , $$("shixun_url" : 1))?.get("shixun_url");
		return url;
	}
	/**
	 * 直播跳转地址
	 * @param nc_course_id
	 * @return
	 */
	private String zhiboUrl(String nc_course_id){
		String url = null;
		url = commodity_courses().findOne($$("nc_id" : nc_course_id) , $$("zhibo_url" : 1))?.get("zhibo_url");
		return url;
	}



	/**
	 * 课程课时列表-未排课
	 * @param nc_course_id 课程NCid
	 * @return	课程课时列表
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def courseContentList(String nc_course_id){
		def course_content_list = courses_content().find($$("nc_course_id" : nc_course_id) , $$("nc_name" : 1)).sort($$("sessions" : 1)).limit(MAX_LIMIT).toArray();
		if(course_content_list){
			course_content_list.each {def item->
				item["name"] = item["nc_name"];
				item.removeField("nc_name");
			}
		}
		return course_content_list;
	}
	/**
	 * 我的优惠券列表-未使用
	 */
	@ResponseBody
	@RequestMapping(value = "myDiscountList/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "我的优惠券列表-包含未使用,已使用,过期,三部分数据", httpMethod = "GET" , response = UserInfomyDiscountUnUseListDto.class, notes = "我的优惠券列表-包含未使用,已使用,过期,三部分数据")
	@TypeChecked(TypeCheckingMode.SKIP)
	def myDiscountList(HttpServletRequest request){
		Map map = new HashMap();
		map["unUseList"] = myDiscountUnUseList(request).data;
		map["overDueList"] = myDiscountOverDueList(request).data;
		map["useList"] = myDiscountUseList(request).data;

		return getResultOKS(map);
	}

	/**
	 * 我的优惠券列表-未使用
	 */
	@ResponseBody
	@RequestMapping(value = "myDiscountUnUseList/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "我的优惠券列表-未使用", httpMethod = "GET" , response = UserInfomyDiscountUnUseListDto.class, notes = "我的优惠券列表-未使用")
	@TypeChecked(TypeCheckingMode.SKIP)
	def myDiscountUnUseList(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();

		String username = qquserMongo.getCollection("qQUser").findOne(
				$$("tuid" : users().findOne($$("_id" : user_id , "priv" : UserType.普通用户.ordinal()) , $$("tuid" : 1))?.get("tuid") ),
				$$("username" : 1)
				)?.get("username")?.toString();

		Long now = System.currentTimeMillis();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append("        di.id AS 'id', ");//id
		sql.append("        di.NAME AS 'name', ");//名称
		sql.append("        ds.discount_start_time AS 'start_time',");//起始时间
		sql.append("        ds.discount_end_time AS 'end_time',");//结束时间
		sql.append("        ds.discount_code AS 'discount_code',");//优惠券code
		sql.append("        di.money AS 'money', ");//金额
		sql.append("        di.enough_money AS 'discount_price',  ");//消费限制 消费满多少元以上
		sql.append("        di.work_type AS 'work_type'  ");//使用品台 0.不限制 1.恒企在线 2.会答APP
		sql.append("   FROM    ");
		sql.append("        discount_user AS ds ");
		sql.append("        INNER JOIN discount AS di ON ds.discount_id = di.id ");
		sql.append("  WHERE 1 = 1");
		sql.append("    AND ds.discount_end_time > ?"   );//活动结束日期
		sql.append("    AND ds.discount_start_time <= ?");//活动开始日期
		sql.append("    AND ds.is_use = 0");//未使用
		sql.append("    AND di.is_stop = 0 ");//未停用
		sql.append("    AND ds.phone = ?");//电话号码
		sql.append("  ORDER BY get_time ");//获取时间

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql.toString()).setResultTransformer(Transformers.aliasToBean(UserInfomyDiscountUnUseListDto.class));
		//时间参数赋值
		query.setParameter(0, DataUtils.dateToStringNormal(now));
		query.setParameter(1, DataUtils.dateToStringNormal(now));
		query.setParameter(2, username);

		List<UserInfomyDiscountUnUseListDto> list = query.list();

		return getResultOK(list);
	}

	/**
	 * 订单中可使用的优惠券
	 */
	@ResponseBody
	@RequestMapping(value = "orderDiscountUnUseList/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "订单中可使用的优惠券", httpMethod = "GET" , response = OrderDiscountUnUseListDto.class, notes = "订单中可使用的优惠券")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "order_no", value = "订单号", required = false, dataType = "int", paramType = "query")
	])
	@TypeChecked(TypeCheckingMode.SKIP)
	def orderDiscountUnUseList(HttpServletRequest request){
		//商品id
		Integer order_id = ServletRequestUtils.getIntParameter(request , "order_id" , 0);
		//订单号
		String order_no = request["order_no"];
		if(StringUtils.isBlank(order_no) && order_id == 0 ){
			return getResultParamsError();
		}

		//用户id
		Integer user_id = Web.getCurrentUserId();

		//订单信息
		Orders order = (Orders)sessionFactory.getCurrentSession().createCriteria(Orders.class)
				.add(Restrictions.or(
				Restrictions.eq(Orders.PROP_ID, order_id),
				Restrictions.eq(Orders.PROP_ORDERNO, order_no)
				))//id
				.add(Restrictions.eq(Orders.PROP_USERID , user_id))//用户id
				.add(Restrictions.eq(Orders.PROP_STATUS , OrderStatus.有效.ordinal()))//是否删除
				.uniqueResult();

		//商品id
		String commodity_id = order.getProductId();
		if(StringUtils.isBlank(commodity_id)){
			return getResultParamsError();
		}
		//商品价格
		Double price = order.getTotalMoney();
		//		Double price = commoditys().findOne($$("_id" : commodity_id) , $$("price" : 1))?.get("price");

		String username = qquserMongo.getCollection("qQUser").findOne(
				$$("tuid" : users().findOne($$("_id" : user_id , "priv" : UserType.普通用户.ordinal()) , $$("tuid" : 1))?.get("tuid") ),
				$$("username" : 1)
				)?.get("username")?.toString();

		Long now = System.currentTimeMillis();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append("        ds.id AS 'id', ");//id
		sql.append("        di.NAME AS 'name', ");//名称
		sql.append("        di.money AS 'money', ");//金额
		sql.append("        di.enough_money AS 'discount_price' ");//消费限制 消费满多少元以上
		sql.append("   FROM    ");
		sql.append("        discount_user AS ds ");
		sql.append("        INNER JOIN discount AS di ON ds.discount_id = di.id ");
		sql.append("  WHERE 1 = 1");
		sql.append("    AND ds.discount_end_time > ?"   );//活动结束日期
		sql.append("    AND ds.discount_start_time <= ?");//活动开始日期
		sql.append("    AND ds.is_use = 0");//未使用
		sql.append("    AND di.is_stop = 0 ");//未停用
		sql.append("    AND ds.phone = ?");//电话号码
		sql.append("    AND di.enough_money <= ?");//最低消费限制
		sql.append("  ORDER BY get_time ");//获取时间

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql.toString()).setResultTransformer(Transformers.aliasToBean(OrderDiscountUnUseListDto.class));
		//时间参数赋值
		query.setParameter(0, DataUtils.dateToStringNormal(now));
		query.setParameter(1, DataUtils.dateToStringNormal(now));
		query.setParameter(2, username);
		query.setParameter(3, price);

		List<OrderDiscountUnUseListDto> list = query.list();

		return getResultOK(list);
	}
	/**
	 * 我的优惠券列表-过期
	 */
	@ResponseBody
	@RequestMapping(value = "myDiscountOverDueList/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "我的优惠券列表-过期", httpMethod = "GET" , response = UserInfomyDiscountUnUseListDto.class, notes = "我的优惠券列表-过期")
	def myDiscountOverDueList(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();

		String username = qquserMongo.getCollection("qQUser").findOne(
				$$("tuid" : users().findOne($$("_id" : user_id , "priv" : UserType.普通用户.ordinal()) , $$("tuid" : 1))?.get("tuid") ),
				$$("username" : 1)
				)?.get("username")?.toString();

		Long now = System.currentTimeMillis();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append("        di.id AS 'id', ");//id
		sql.append("        di. NAME AS 'name', ");//名称
		sql.append("        ds.discount_start_time AS 'start_time',");//起始时间
		sql.append("        ds.discount_end_time AS 'end_time',");//结束时间
		sql.append("        ds.discount_code AS 'discount_code',");//优惠券code
		sql.append("        di.money AS 'money', ");//金额
		sql.append("        di.enough_money AS 'discount_price',  ");//消费限制 消费满多少元以上
		sql.append("        di.work_type AS 'work_type'  ");//使用品台 0.不限制 1.恒企在线 2.会答APP
		sql.append("   FROM    ");
		sql.append("        discount_user AS ds ");
		sql.append("        INNER JOIN discount AS di ON ds.discount_id = di.id ");
		sql.append("  WHERE 1 = 1");
		sql.append("    AND ds.discount_end_time < ?"   );//活动结束日期
		sql.append("    AND ds.is_use = 0");//未使用
		sql.append("    AND ds.phone = ?");//电话号码
		sql.append("  ORDER BY get_time ");//获取时间



		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql.toString()).setResultTransformer(Transformers.aliasToBean(UserInfomyDiscountUnUseListDto.class));
		//时间参数赋值
		query.setParameter(0, DataUtils.dateToStringNormal(now));
		query.setParameter(1, username);

		List<UserInfomyDiscountUnUseListDto> list = query.list();

		return getResultOK(list);
	}
	/**
	 * 我的优惠券列表-已使用
	 */
	@ResponseBody
	@RequestMapping(value = "myDiscountUseList/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "我的优惠券列表-已使用", httpMethod = "GET" , response = UserInfomyDiscountUnUseListDto.class, notes = "我的优惠券列表-已使用")
	def myDiscountUseList(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();

		String username = qquserMongo.getCollection("qQUser").findOne(
				$$("tuid" : users().findOne($$("_id" : user_id , "priv" : UserType.普通用户.ordinal()) , $$("tuid" : 1))?.get("tuid") ),
				$$("username" : 1)
				)?.get("username")?.toString();

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append("        di.id AS 'id', ");//id
		sql.append("        di. NAME AS 'name', ");//名称
		sql.append("        ds.discount_start_time AS 'start_time',");//起始时间
		sql.append("        ds.discount_end_time AS 'end_time',");//结束时间
		sql.append("        ds.discount_code AS 'discount_code',");//优惠券code
		sql.append("        di.money AS 'money', ");//金额
		sql.append("        di.enough_money AS 'discount_price',  ");//消费限制 消费满多少元以上
		sql.append("        di.work_type AS 'work_type'  ");//使用品台 0.不限制 1.恒企在线 2.会答APP
		sql.append("   FROM    ");
		sql.append("        discount_user AS ds ");
		sql.append("        INNER JOIN discount AS di ON ds.discount_id = di.id ");
		sql.append("  WHERE 1 = 1");
		sql.append("    AND ds.is_use = 1");//未使用
		sql.append("    AND ds.phone = ?");//电话号码
		sql.append(" ORDER BY get_time ");//获取时间

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql.toString()).setResultTransformer(Transformers.aliasToBean(UserInfomyDiscountUnUseListDto.class));
		//时间参数赋值
		query.setParameter(0, username);

		List<UserInfomyDiscountUnUseListDto> list = query.list();

		return getResultOK(list);
	}

	/**
	 * 我的学习-学习报告
	 * @param request
	 * @return
	 */
	def myClassPlans(HttpServletRequest request){
		//用户id
		String nc_user_id = Web.getCurrentUserNcId();

		def commodityList = new ArrayList();

		//报名表
		def sList = signs().find(
				$$("nc_user_id" : nc_user_id) ,
				$$("_id" : 1 , "nc_commodity_id" : 1)
				).sort($$("predict_open_time" : 1)).limit(MAX_LIMIT)?.toArray();

		if(sList){
			sList.each {def sitem->
				//商品nc_id
				String nc_commodity_id = sitem["nc_commodity_id"];
				//商品
				def commodity = commoditys().findOne($$("nc_id" : nc_commodity_id) , $$("_id" : 1 , "nc_id" : 1 , "name" : 1));
				if(commodity){
					//装入集合
					commodityList.add(commodity);
				}
			}
		}
	}


	/**
	 * 教师端-设置标签-查询标签
	 * @Description: 教师端-设置标签-查询标签
	 * @date 2015年7月21日 上午11:51:58
	 * @param @param request
	 * @param @return
	 * @throws
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def topic_input_tips(HttpServletRequest request){

		Map map = new HashMap();
		def industryList = _industryList();
		//两组行业
		def industry0 , industry1 = null;
		if(industryList){
			if(industryList.size() > 0){
				industry0 = industryList.get(0);
			}
			if(industryList.size() > 1){
				industry1 = industryList.get(1);
			}
		}

		int MAX = 12;

		if(industry0 != null){
			def it0 = industry0["industry_tip"];
			if(it0 != null && it0.size() > 0){
				for(int size0 = it0.size() ; size0 > MAX ; size0 = it0.size()){
					it0.remove(size0-1);
				}
			}
		}
		if(industry1 != null){
			def it1 = industry1["industry_tip"];
			if(it1 != null && it1.size() > 0){
				for(int size0 = it1.size() ; size0 > MAX ; size0 = it1.size()){
					it1.remove(size0-1);
				}
			}
		}


		//学习领域
		map["industry0"] = industry0;
		//工作领域
		map["industry1"] = industry1;
		map["userIndustry0"] = null;
		map["userIndustry1"] = null;

		return getResultOKS(map);
	}

	/**
	 * 提问获取标签接口 V2.00
	 */
	@ResponseBody
	@RequestMapping(value = "topic_input_tips_v200/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "提问获取标签接口 V2.00", httpMethod = "GET", response = UserInfoTopicInputTipsV200.class , notes = "提问获取标签接口 V2.00")
	@TypeChecked(TypeCheckingMode.SKIP)
	def topic_input_tips_v200(HttpServletRequest request){

		Integer userid = Web.getCurrentUserId();
		def tipList;
		//判断是不是中级的学员
		if(getIsZJStudent(userid)){
			tipList = tip_content().find($$("parent_tip_id":$$('$ne':0)), $$("_id" : 1 , "tip_name":1)).limit(100).sort($$("order": 1))?.toArray();
		}else{
			tipList = tip_content().find($$(
					"_id" : $$( '$gt': 0, '$lt': 9999),//普通标签的id范围  所有用户都可见
					), $$("_id" : 1 , "tip_name":1)).limit(100).sort($$("order": 1))?.toArray();
		}
		return getResultOKS(tipList);
	}


	def settingTips(HttpServletRequest request){

		java.net.URLDecoder urlDecoder = new java.net.URLDecoder();
		String json = urlDecoder.decode(request["json"], "UTF-8");
		//用户id
		Integer user_id = Web.getCurrentUserId();
		if (isPrivs(user_id, Privs.特殊标签老师)){//是中级老师
			return getResult(60101, "您暂无修改标签权限");
		}
		if(StringUtils.isNotBlank(json)){
			//参数
			//			JSONArray js = new JSONArray(json);
			List js = JSONUtil.jsonToBean(json, List.class);



			if(js != null && js.size() > 0){

				for(int i = 0 ; i < js.size() ; i ++){
					if(js.get(i)["industry_id"] == null){
						js.remove(i);
					}
				}

				if(js.size() > 0){
					users().update(
							$$("_id" : Web.getCurrentUserId()),
							$$($set : $$("user_industry" : js))
							);
					return getResultOKS();
				}
			}
		}

		return getResultParamsError();
	}

	@ResponseBody
	@RequestMapping(value = "settingTips_v200/*-*", produces = "application/json; charset=utf-8")
	@ApiOperation(value = "设置用户标签",  notes = "设置用户标签")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "tips", value = "tipId0,tipId1", required = true, dataType = "int", paramType = "query")
	])
	@TypeChecked(TypeCheckingMode.SKIP)
	def settingTips_v200(HttpServletRequest request){
		return getResult(60101, "您暂无修改标签权限");
/*		
		String tips = request["tips"];
		//用户id
		Integer user_id = Web.getCurrentUserId();

		if (isPrivs(user_id, Privs.特殊标签老师)){//是中级老师
			return getResult(60101, "您暂无修改标签权限");
		}

		//标签
		if(StringUtils.isNotBlank(tips)){
			String[] tipArray = tips.split(",");

			if(tipArray.length > 5){
				return getResult(60101, "标签数量超出限制");
			}

			"user_industry" : [
			 {
			 "industry_id" : 1111,
			 "users_industry_tip" : [
			 {
			 "industry_tip_id" : 1000
			 },
			 {
			 "industry_tip_id" : 2000
			 },
			 {
			 "industry_tip_id" : 3000
			 }
			 ]
			 }
			 ],
			//拼接数据结构
			List user_industryList = new ArrayList();

			Map user_industry = new HashMap();
			user_industryList.add(user_industry);
			user_industry["industry_id"] = 1111;

			List industry_tip_id = new ArrayList();
			user_industry["users_industry_tip"] = industry_tip_id;

			tipArray.each {String row->
				Map imap = new HashMap();
				imap["industry_tip_id"] = Integer.valueOf(row);
				industry_tip_id.add(imap);
			}
			users().update($$("_id" : user_id) , $$('$set' : $$("user_industry" : user_industryList)));
		}else{
			users().update($$("_id" : user_id) , $$('$set' : $$("user_industry" : null)));
		}

		return getResultOK();
*/
	}

	/**
	 * 教师端-设置标签-查询标签
	 * @Description: 教师端-设置标签-查询标签
	 * @date 2015年7月21日 上午11:51:58
	 * @param @param request
	 * @param @return
	 * @throws
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def tips(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//		//用户基本信息
		//		def user = users().findOne($$("_id" : user_id) , $$("_id" : 1 , "user_industry" : 1));

		Map map = new HashMap();
		def industryList = _industryList();
		def user_industry_list = null;
		//两组行业
		def industry0 , industry1 = null;
		//两组选择的行业
		def userIndustry0 , userIndustry1 = null;
		if(industryList){
			//用户基本信息
			user_industry_list = users().findOne($$("_id" : user_id) , $$("user_industry" : 1))?.get("user_industry");
			int industryListSize = industryList.size();
			if(industryListSize > 0){
				industry0 = industryList.get(0);
				if(
				user_industry_list &&
				user_industry_list.size() > 0 &&
				industry0["_id"] == user_industry_list.get(0)["industry_id"]
				){
					userIndustry0 = user_industry_list.get(0);
				}

			}

			if(industryListSize > 1){
				industry1 = industryList.get(1);

				if(
				user_industry_list &&
				user_industry_list.size() > 1 &&
				industry1["_id"] == user_industry_list.get(1)["industry_id"]
				){
					userIndustry1 = user_industry_list.get(1);
				}

			}

		}
		map["industry0"] = industry0;
		map["industry1"] = industry1;
		map["userIndustry0"] = userIndustry0;
		map["userIndustry1"] = userIndustry1;

		return getResultOKS(map);
	}

	def _industryList(){
		return industry().find().limit(2).toArray();
	}

	def mytips(HttpServletRequest request)
	{
		Integer user_id = Web.getCurrentUserId();
		//用户基本信息
		def user_industry = null;
		user_industry = users().findOne($$("_id" : user_id) , $$("user_industry" : 1))?.get("user_industry");		//行业
		if(user_industry){
			//行业名称

			user_industry.each { def tdbo ->
				//标签名称

				tdbo["industry_name"] = industry().findOne(
						$$("_id" : tdbo["industry_id"]),
						$$("industry_name" : 1)
						)?.get("industry_name");

				def users_industry_tip = tdbo["users_industry_tip"];

				if(users_industry_tip){
					users_industry_tip.each { def tdbosub ->
						//标签名称
						def tip_ids_obj = tdbosub["industry_tip_id"].toString()
						int tip_id=tip_ids_obj.toInteger()
						tdbosub["tip_name"] = tip_content().findOne($$("_id" :tip_id), $$("tip_name" : 1))?.get("tip_name");
						}
				}

			}

		}
		return getResultOKS(user_industry);
	}

	def mytips_v200(HttpServletRequest request)
	{
		List list = new ArrayList();
		Integer user_id = Web.getCurrentUserId();
		//用户基本信息
		def user_industry = null;
		user_industry = users().findOne($$("_id" : user_id) , $$("user_industry" : 1))?.get("user_industry");		//行业
		if(user_industry){
			//行业名称

			user_industry.each { def tdbo ->
				//标签名称

				//				tdbo["industry_name"] = industry().findOne(
				//						$$("_id" : tdbo["industry_id"]),
				//						$$("industry_name" : 1)
				//						)?.get("industry_name");

				def users_industry_tip = tdbo["users_industry_tip"];

				if(users_industry_tip){
					users_industry_tip.each { def tdbosub ->
						//标签名称
						def tip_ids_obj = tdbosub["industry_tip_id"].toString()
						int tip_id=tip_ids_obj.toInteger()
						String tip_name = tip_content().findOne($$("_id" :  tip_id), $$("tip_name" : 1))?.get("tip_name");
						Map map = new HashMap();
						map["industry_tip_id"] = tdbosub["industry_tip_id"];
						map["industry_tip_id"] = tip_name;
						list.add(map);
					}
				}

			}

		}
		return getResultOKS(list);
	}


	//老师-用户基本信息
	def mytInfo(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//用户基本信息


		def user = users().findOne($$("_id" : user_id) , $$("_id" : 1 , "nick_name" : 1 , "pic" : 1 ));
		if(user){
			//抢答数
			user["replay_num"] = teacherController.replay_num(user_id);
			//好评比率
			user["evaluation_percent"] = teacherController.teacher_replay_end_percent(user_id);
			//被关注数量
			user["target_num"] = teacherController.attention_num(user_id);


		}
		return getResultOKS(user);
	}


	//老师-用户基本信息
	def mytInfo_android_v150(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//用户基本信息


		def user = users().findOne($$("_id" : user_id) , $$("_id" : 1 , "nick_name" : 1 , "pic" : 1 , "vlevel" : 1));
		if(user){
			//抢答数
			user["replay_num"] = teacherController.replay_num(user_id);
			//好评比率
			user["evaluation_percent"] = teacherController.teacher_replay_end_percent(user_id);
			//被关注数量
			user["target_num"] = teacherController.attention_num(user_id);

			user["vip_icon"] = VlevelType.vipIcon(user["vlevel"]);

			//add by shihongjie 2015-12-22 VIP_ICON
			user["vip_icon"] = VlevelType.vipIcon(user["vlevel"]);
			user.removeField("vlevel");
		}
		return getResultOKS(user);
	}

	//老师-用户基本信息
	@TypeChecked(TypeCheckingMode.SKIP)
	def mytInfo_android_v200(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//用户基本信息


		def user = users().findOne($$("_id" : user_id) , $$("_id" : 1 , "nick_name" : 1 , "pic" : 1 , "user_industry" : 1 ));
		if(user){
			//抢答数
			user["replay_num"] = teacherController.replay_num(user_id);
			//好评比率
			user["evaluation_percent"] = teacherController.teacher_replay_end_percent(user_id);
			//被关注数量 我的粉丝
			user["target_num"] = teacherController.attention_num(user_id);
			//标签
			def user_industry = user["user_industry"];
			user["tips"] = tipToString("",user_industry);
			if("null".equals(user["tips"])){
				user["tips"]="";
			}
			//我的收益
			def incom = userincomController.myIncomAll(user_id);
			user["incom"] = incom;
			user["incoms"] = incom + "元";
			//我的关注
			user["attention_num"] = attention().count($$("source_tuid" : user_id)).intValue();
		}
		return getResultOKS(user);
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	public String tipToString(String head, def user_industryList){
		StringBuffer tipStr = new StringBuffer(head);

		if(user_industryList){
			user_industryList.each { def industry ->
				def tipList = industry["users_industry_tip"];
				if(tipList){
					tipList.each {def tip ->
						tipStr.append(tip_content().findOne($$("_id" : tip["industry_tip_id"]) , $$("tip_name" : 1))?.get("tip_name") + " ");
					}
				}
			}
		}
		return tipStr.length() > head.length() ? tipStr.substring(0, tipStr.length() - 1) : head;
	}



	//学生-用户基本信息
	def mysInfo(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//用户基本信息
		def user = users().findOne($$("_id" : user_id) , $$("_id" : 1 , "nick_name" : 1 , "pic" : 1));
		if(user){
			//提问数量
			user["topic_num"] = topics().count($$("author_id" : user_id));
			//已完成提问的数量
			Long all = topics().count($$("author_id" : user_id , "type" : TopicsType.问题已结束.ordinal()));
			//已评价的数量
			Long evaluation_num = topics().count($$("author_id" : user_id , "evaluation_type" : $$($ne:TopicEvaluationType.未评价.ordinal())));
			//点评率
			if(all > 0){
				double devaluation_num = Double.valueOf(NumberUtil.formatDouble3((double)(evaluation_num / all) , 2));
				int ievaluation_num = (int) (devaluation_num * 100) ;
				user["evaluation_num"] = ievaluation_num + "%";
			}else{
				user["evaluation_num"] = "0%";
			}

			//关注数量
			user["attention_num"] = mainMongo.getCollection("attention").count($$("source_tuid" : user_id));


		}
		return getResultOKS(user);
	}


	//学生-用户基本信息
	def mysInfo_android_v150(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//用户基本信息
		def user = users().findOne($$("_id" : user_id) , $$("_id" : 1 , "nick_name" : 1 , "pic" : 1 , "vlevel" : 1));
		if(user){
			//提问数量
			user["topic_num"] = topics().count($$("author_id" : user_id));
			//已完成提问的数量
			Long all = topics().count($$("author_id" : user_id , "type" : TopicsType.问题已结束.ordinal()));
			//已评价的数量
			Long evaluation_num = topics().count($$("author_id" : user_id , "evaluation_type" : $$($ne:TopicEvaluationType.未评价.ordinal())));
			//点评率
			if(all > 0){
				double devaluation_num = Double.valueOf(NumberUtil.formatDouble3((double)(evaluation_num / all) , 2));
				int ievaluation_num = (int) (devaluation_num * 100) ;
				user["evaluation_num"] = ievaluation_num + "%";
			}else{
				user["evaluation_num"] = "0%";
			}

			//关注数量
			user["attention_num"] = mainMongo.getCollection("attention").count($$("source_tuid" : user_id));

			//add by shihongjie 2015-12-22 VIP_ICON
			user["s_vip_icon"] = VlevelType.vipIcon(user["vlevel"]);
			user.removeField("vlevel");
		}
		return getResultOKS(user);
	}
	//学生-用户基本信息
	def mysInfo_android_v200(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//用户基本信息
		def user = users().findOne($$("_id" : user_id) , $$("_id" : 1 , "nick_name" : 1 , "pic" : 1));
		if(user){
			//提问数量
			user["topic_num"] = topics().count($$("author_id" : user_id));

			//关注数量
			user["attention_num"] = mainMongo.getCollection("attention").count($$("source_tuid" : user_id));

			//关注数量
			user["score"] = scoreController.myScore(user_id);
		}
		return getResultOKS(user);
	}


	//评价的百分比
	def evaluation_percent(HttpServletRequest request){
		Integer user_id = ServletRequestUtils.getIntParameter(request , "user_id");
		//已完成提问的数量
		Long all = topics().count($$("author_id" : user_id , "type" : TopicsType.问题已结束.ordinal()));
		//点评率
		String ep = "0%"
		if(all > 0){
			//已评价的数量
			Long evaluation_num = topics().count($$("author_id" : user_id , "type" : TopicsType.问题已结束.ordinal() , "evaluation_type" : $$($ne:TopicEvaluationType.未评价.ordinal())));
			double devaluation_num = Double.valueOf(NumberUtil.formatDouble3((double)(evaluation_num / all) , 2));
			int ievaluation_num = (int) (devaluation_num * 100) ;
			ep = ievaluation_num + "%";
		}
		return getResultOKS(ep);
	}

	/**
	 * 删除收藏
	 * @Description: 删除收藏
	 */
	def deleteCollection(HttpServletRequest request){
		String _id = request["_id"];
		if(_id){
			collection().remove($$("_id" : _id));
			return getResultOKS();
		}
		return getResultParamsError();
	}
	/**
	 * 批量删除收藏
	 */
	def deleteCollectionBatch(HttpServletRequest request){
		String ids = request["_ids"];
		if(StringUtils.isNotBlank(ids)){
			List idList = ids.split(",").toList();
			collection().remove($$("_id" : $$('$in' : idList)));
			return getResultOKS();
		}
		return getResultParamsError();
	}

	/**
	 * 删除关注
	 * @Description: 删除关注
	 */
	def deleteAttention(HttpServletRequest request){
		String _id = request["_id"];
		if(_id){
			attention().remove($$("_id" : _id));
			return getResultOKS();
		}
		return getResultParamsError();
	}
	/**
	 * 批量删除关注
	 */
	def deleteAttentionBatch(HttpServletRequest request){
		String ids = request["_ids"];
		if(StringUtils.isNotBlank(ids)){
			List idList = ids.split(",").toList();
			attention().remove($$("_id" : $$('$in' : idList)));
			return getResultOKS();
		}
		return getResultParamsError();
	}

	//我的收藏列表
	def myCollectionList(HttpServletRequest request){
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//用户收藏列表
		def collectionList = collection().find(
				$$("tuid" : user_id) ,
				$$("tuid" : 0)).sort($$("timestamp" : -1)
				).skip((page - 1) * size).limit(size)?.toArray();
		if(collectionList){
			collectionList.each { def dbo ->
				def topic = topics().findOne($$("_id" : dbo["topics_id"]) ,$$("_id" : 1 , "content" : 1 , "industry_id" : 1 , "tips" : 1 , "author_id" : 1))
				if(topic){
					topic["industry_name"] = industry().findOne($$("_id" : topic["industry_id"]) , $$("industry_name" : 1))?.get("industry_name");
					topic["pic"] = users().findOne($$("_id" : topic["author_id"]) , $$("pic" : 1))?.get("pic");
				}
				dbo["topic"] = topic;
			}
		}
		return getResultOKS(collectionList);

	}

	//我的关注列表
	def myAttentionList(HttpServletRequest request){
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		//用户id
		Integer user_id = Web.getCurrentUserId();

		def attentionList = attention().find(
				$$("source_tuid" : user_id),
				$$("source_tuid" : 0 , "timestamp" : 0)).sort($$("timestamp" : -1)).skip((page - 1) * size).limit(size)?.toArray();

		if(attentionList){
			attentionList.each { def dbo ->

				Integer teacher_id = dbo["target_tuid"] as Integer;

				//老师基本信息
				def user = users().findOne(
						$$("_id" : teacher_id ),
						$$("pic" : 1 , "_id" : 1 ,"nick_name" : 1 , "user_industry" : 1)
						);
				//老师图片
				dbo["teach_pic"] = user["pic"];
				dbo["teach_id"] = user["_id"];
				//老师昵称
				dbo["nick_name"] = user["nick_name"];

				//关注数量
				dbo["target_num"] = teacherController.attention_num(teacher_id);
				//好评比率
				dbo["satisfaction"] = teacherController.teacher_replay_end_percent(teacher_id);
				//抢答数
				dbo["qiangda_num"] = teacherController.replay_num(teacher_id);

				//行业
				def user_industry = user["user_industry"];
				dbo["tips"] = teacherController.tipToString(user_industry);

			}
		}
		return getResultOKS(attentionList);
	}

	/**
	 * 被关注的数量
	 * @Description: 被关注的数量
	 */
	public long target_num(Integer target_tuid){
		return attention().count($$("target_tuid" : target_tuid));
	}


	def updateUserInfo(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();

		BasicDBObject updater =  new BasicDBObject();

		//0.显示 1.不显示
		Integer isShowUpdate = -1;
		boolean name = request["nick_name"]!=null && request["nick_name"]!="";
		boolean url = request["avatar_url"]!=null && request["avatar_url"]!="";
		if(name)
		{
			String nick_name = request["nick_name"].toString();
			updater.append("nick_name" , nick_name);

			//			isShowUpdate = UserIsShowUpdate.不显示.ordinal();
		}
		if(url)
		{
			String avatar_url = request["avatar_url"].toString();
			updater.append("pic" , avatar_url);
			//			isShowUpdate = UserIsShowUpdate.不显示.ordinal();
		}

		if(name || url){
			isShowUpdate = UserIsShowUpdate.不显示.ordinal();
		}else{
			isShowUpdate = UserIsShowUpdate.显示.ordinal();
		}

		if(isShowUpdate == UserIsShowUpdate.不显示.ordinal()){
			updater.append("is_show_update" , isShowUpdate);
		}

		users().update(
				$$("_id" : user_id),
				$$( $set : updater)
				);

		return getResultOKS();
	}




	@ResponseBody
	@RequestMapping(value = "update_userinfo_pc/*-*", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "修改用户信息", httpMethod = "POST", response = UserInfoTopicInputTipsV200.class , notes = "修改用户信息,修改的时候，可以提供个别字段即可，其他字段名不能提供")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "nick_name", value = "昵称", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "avatar_url", value = "头像的url地址", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "mail", value = "邮箱", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "sex", value = "性别 0：男 1：女", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "province", value = "省份", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "city", value = "城市", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "area", value = "地区", required = false, dataType = "String", paramType = "query")
	])
	def update_userinfo_pc(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();

		BasicDBObject updater =  new BasicDBObject();

		//0.显示 1.不显示
		Integer isShowUpdate = -1;

		if(request["nick_name"]!=null)
		{
			String nick_name = request["nick_name"].toString();
			updater.append("nick_name" , nick_name);

			isShowUpdate = UserIsShowUpdate.不显示.ordinal();
		}
		if(request["avatar_url"]!=null)
		{
			String avatar_url = request["avatar_url"].toString();
			updater.append("pic" , avatar_url);
			isShowUpdate = UserIsShowUpdate.不显示.ordinal();
		}

		if(isShowUpdate == UserIsShowUpdate.不显示.ordinal()){
			updater.append("is_show_update" , isShowUpdate);
		}


		//更新性别
		if(request["sex"]!=null)
		{
			String user_sex = request["sex"].toString();
			updater.append("user_sex" , user_sex);
		}
		//更新省份
		if(request["province"]!=null)
		{
			String province = request["province"].toString();
			updater.append("province" , province);
		}
		//更新城市
		if(request["city"]!=null)
		{
			String city = request["city"].toString();
			updater.append("city" , city);
		}
		//更新地区
		if(request["area"]!=null)
		{
			String area = request["area"].toString();
			updater.append("area" , area);
		}
		if(request["mail"]!=null)
		{
			String mail = request["mail"].toString();
			updater.append("mail" , mail);
		}


		users().update(
				$$("_id" : user_id),
				$$($set : updater)
				);

		return getResultOK();
	}


	//学生-用户基本信息
	@RequestMapping(value = "mysInfo_pc/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取用户基本信息", httpMethod = "GET", response = BaseResultVO.class , notes = "")
	def mysInfo_pc(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//用户基本信息
		def user = users().findOne($$("_id" : user_id) , $$("_id" : 1 , "nick_name" : 1 , "pic" : 1,"user_sex":1,"province":1,"city":1,"area":1,"mail":1));


		if(user){
			//积分
			//user["score"] = scoreController.myScore(user_id);
			user["score"] = 0;

			//用户手机号码
			String username = super.getUserPhoneByUserId(user_id);
			//当前时间
			Long now = System.currentTimeMillis();
			//查询语句
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT ");
			sql.append("        count(di.id) ");//id
			sql.append("   FROM    ");
			sql.append("        discount_user AS ds ");
			sql.append("        INNER JOIN discount AS di ON ds.discount_id = di.id ");
			sql.append("  WHERE 1 = 1");
			sql.append("    AND ds.discount_end_time > ?"   );//活动结束日期
			sql.append("    AND ds.discount_start_time <= ?");//活动开始日期
			sql.append("    AND ds.is_use = 0");//未使用
			sql.append("    AND di.is_stop = 0 ");//未停用
			sql.append("    AND ds.phone = ?");//电话号码
			sql.append("  ORDER BY get_time ");//获取时间

			Query query = sessionFactory.getCurrentSession().createSQLQuery(sql.toString());
			//时间参数赋值
			query.setParameter(0, DataUtils.dateToStringNormal(now));
			query.setParameter(1, DataUtils.dateToStringNormal(now));
			query.setParameter(2, username);
			//执行查询 优惠券数量
			user["discount_num"] = query.uniqueResult();
			user["username"] = username;

		}
		return getResultOKS(user);
	}


	public void updateUserInfo(String real_name,String alipay_account)
	{
		Integer user_id = Web.getCurrentUserId();
		BasicDBObject updater =  new BasicDBObject();
		updater.append("real_name" , real_name);
		updater.append("alipay_account" , alipay_account);
		users().update(
				$$("_id" : user_id),
				$$( $set : updater)
				);

	}

	def updateUserInComeInfo(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();

		BasicDBObject updater =  new BasicDBObject();

		if(request["real_name"]!="")
		{
			String real_name = request["real_name"].toString();
			updater.append("real_name" , real_name);

		}
		if(request["alipay_account"]!="")
		{
			String alipay_account = request["alipay_account"].toString();
			updater.append("alipay_account" , alipay_account);
		}

		users().update(
				$$("_id" : user_id),
				$$( $set : updater)
				);

		return getResultOKS();
	}





	/**
	 * 获取标签列表
	 */
	@ResponseBody
	@RequestMapping(value = "tip_list/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "会答2.0 标签列表API", httpMethod = "GET", response = UserinfoTipListVO.class, notes = "会答2.0 标签列表API")
	def tip_list(HttpServletRequest request){
		//标签列表
		def tipList;
		Integer userid = Web.getCurrentUserId();
		//判断是不是中级的学员
		if(getIsZJStudent(userid)){
			tipList = tip_content().find(null, $$("_id" : 1 , "tip_name":1, "pic":1, "bigpic":1)).limit(100).sort($$("order": 1))?.toArray();
		}else{
			tipList = tip_content().find($$(
					"_id" : $$( '$gt': 0, '$lt': 9999),//普通标签的id范围  所有用户都可见
					), $$("_id" : 1 , "tip_name":1, "pic":1, "bigpic":1)).limit(100).sort($$("order": 1))?.toArray();
		}
		if(tipList){
			//用户id
			Integer user_id = Web.getCurrentUserId();
			//用户关注的标签
			def user_tip_contents = users().findOne($$("_id" : user_id) , $$("tip_contents" : 1))?.get("tip_contents");
			//循环
			tipList.each {def row ->
				//判断标签是否需要选中
				if(user_tip_contents){
					user_tip_contents.each {def uRow->
						if(uRow["_id"]!= null && row["_id"] == uRow["_id"]){
							row["is_select"] = true;
							def topicListSize = 0;
							if(uRow["refresh_time"] != null && uRow["refresh_time"] != 0){
								//推荐问题列表更新了多少
								topicListSize = topics().find(
										$$(
										"type" : TopicsType.问题已结束.ordinal() , "evaluation_type":  $$('$in' :[
											TopicEvaluationType.满意.ordinal() ,
											TopicEvaluationType.很满意.ordinal()
										]) ,
										"deleted" : false ,"tips._id" :(int)uRow["_id"],
										"author_id" :$$('$nin' : [userid]),
										"update_at" :$$('$gt': (long)uRow["refresh_time"], '$lt': System.currentTimeMillis())
										)).size();
							}
							row["topicListSize"] = topicListSize;
						}
					}
				}
				//不选中
				if(row["is_select"] == null){
					row["is_select"] = false;
					row["topicListSize"] = 0;
				}
			
			}
		}
		return getResultOK(tipList);
	}
	
	
	
	/**
	 * 获取标签列表3.00
	 */
	@ResponseBody
	@RequestMapping(value = "tip_list_300/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "会答3.00标签列表API(分类)", httpMethod = "GET", response = UserinfoTipListVO.class, notes = "会答3.00标签列表API(分类)")
	def tip_list_300(HttpServletRequest request){
		Integer userid = Web.getCurrentUserId();
		//标签列表
		def tipList = []
		def second_tip = [:]

		def parent_tip_list = tip_content().find($$("parent_tip_id":0), $$("_id" : 1, "tip_name":1)).limit(100).sort($$("order": 1))?.toArray();
		for (var in parent_tip_list) {
			second_tip = [:]
			def sub_tip_list = tip_content().find($$("parent_tip_id":var.get("_id")), $$("_id" : 1 , "tip_name":1 )).limit(100).sort($$("order": 1))?.toArray();
			if(null != var.get("_id")){
				second_tip.put("id", var.get("_id"))
				second_tip.put("tip_name", var.get("tip_name"))
				second_tip.put("second_tip",sub_tip_list)
				tipList<<second_tip
			}
		}
		
		if(tipList){
			//用户id
			Integer user_id = Web.getCurrentUserId();
			//用户关注的标签
			def user_tip_contents = users().findOne($$("_id" : user_id) , $$("tip_contents" : 1))?.get("tip_contents");
			//用户选中的识别
			def user_tip_contents1 = users().findOne($$("_id" : user_id) , $$("user_industry" : 1))?.get("user_industry");
			def user_my_tipsL 
			if(user_tip_contents1){
				user_tip_contents1.each {def industry_tip->
					user_my_tipsL = industry_tip["users_industry_tip"]
				}
			}
			//循环
			tipList.each {def tRow ->
				def tipsubList = tRow["second_tip"]
				tipsubList.each{def row ->
				//判断标签是否需要选中
				if(user_tip_contents){
					user_tip_contents.each {def uRow->
						if(uRow["_id"]!= null && row["_id"] == uRow["_id"]){
							row["is_select"] = true;
							def topicListSize = 0;
							if(uRow["refresh_time"] != null && uRow["refresh_time"] != 0){
								//推荐问题列表更新了多少
								topicListSize = topics().find(
										$$(
										"type" : TopicsType.问题已结束.ordinal() , "evaluation_type":  $$('$in' :[
											TopicEvaluationType.满意.ordinal() ,
											TopicEvaluationType.很满意.ordinal()
										]) ,
										"deleted" : false ,"tips._id" :(int)uRow["_id"],
										"author_id" :$$('$nin' : [userid]),
										"update_at" :$$('$gt': (long)uRow["refresh_time"], '$lt': System.currentTimeMillis())
										)).size();
							}
							row["topicListSize"] = topicListSize;
						}
					}
				}
				if(user_my_tipsL){
//					println user_my_tipsL
//					println row
//					user_my_tipsL.each {def mRow->
					for(mRow in user_my_tipsL){
					if(row["_id"] == mRow["industry_tip_id"]){
						row["is_select"] = true;
						break;
					}else{
						row["is_select"] = false;
					}
					
					}
				}
				//不选中
				if(row["is_select"] == null){
					row["is_select"] = false;
					row["topicListSize"] = 0;
				}
			}
			}
		}
		return getResultOK(tipList);
	}



	/**
	 * 会答3.1 设置标签API(单个提交)
	 */
	@ResponseBody
	@RequestMapping(value = "tip_list_setting_v310/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "会答3.1标签列表提交", httpMethod = "GET", response = UserinfoTipListVO.class, notes = "会答3.1标签列表提交")
	def tip_list_setting_v310(HttpServletRequest request){
		Integer user_id = Web.getCurrentUserId();
		String tip = request["tip"];
		if(StringUtils.isNotBlank(tip)){
			Integer tid = Integer.valueOf(tip);
			//判断是不是中级的学员
			if(getIsZJStudent(user_id)){//是中级学员

			}else{//普通用户
				if(tid>=10000 && tid<=10010){//中级学员的标签范围
					return getResult(Code.学员设置关注标签数量超出权限, Code.学员设置关注标签数量超出权限_S)
				}
			}
			def tipContent = tip_content().findOne($$("_id" : tid) , $$("_id" : 1 , "tip_name" : 1));
			def userTipList = (List)(users().findOne($$("_id" : user_id) , $$("tip_contents" : 1))?.get("tip_contents"));
			if(userTipList){
				for(int i = 0;i<userTipList.size();i++){
					if(userTipList.get(i)["_id"] == tipContent["_id"]){
						return getResultOK("订阅成功");
					}
				}
			}else{
				userTipList = new ArrayList();
			}
			tipContent["refresh_time"] = System.currentTimeMillis();
			userTipList.add(tipContent);
			users().update($$("_id" : user_id) , $$('$set' : $$("tip_contents" : userTipList)));
			return getResultOK("订阅成功");
		}else{
			return getResult(60103, "请选择您需要设置的标签");
		}
	}
	/**
	 * 会答3.1 设置标签API(单个取消)
	 */
	@ResponseBody
	@RequestMapping(value = "tip_list_unsetting_v310/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "会答3.1标签列表取消", httpMethod = "GET", response = UserinfoTipListVO.class, notes = "会答3.1标签列表取消")
	def tip_list_unsetting_v310(HttpServletRequest request){
		Integer user_id = Web.getCurrentUserId();
		String tip = request["tip"];
		if(StringUtils.isNotBlank(tip)){
			Integer tid = Integer.valueOf(tip);
			//判断是不是中级的学员
			if(getIsZJStudent(user_id)){//是中级学员

			}else{//普通用户
				if(tid>=10000 && tid<=10010){//中级学员的标签范围
					return getResult(Code.学员设置关注标签数量超出权限, Code.学员设置关注标签数量超出权限_S)
				}
			}
			def tipContent = tip_content().findOne($$("_id" : tid) , $$("_id" : 1 , "tip_name" : 1));
			List userTipList = new ArrayList();
			userTipList = (List)(users().findOne($$("_id" : user_id) , $$("tip_contents" : 1))?.get("tip_contents"));
			for(int i = 0;i<userTipList.size();i++){
				if(userTipList.get(i)["_id"] == tipContent["_id"]){
					userTipList.remove(i);
					users().update($$("_id" : user_id) , $$('$set' : $$("tip_contents" : userTipList)));
					return getResultOK("取消成功");
				}
			}
			return getResultOK("取消成功");
		}else{
			return getResult(60103, "请选择您需要设置的标签");
		}
	}


	/**
	 * 会答2.0 设置标签API
	 */
	@ResponseBody
	@RequestMapping(value = "tip_list_setting/*-*", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "会答2.0 设置标签API", httpMethod = "POST", response = BaseSimpleResultVO.class ,notes = "code=60100 , msg=标签数量超出限制")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "tips", value = "标签用逗号隔开,非空!例如:1000,2000,3000", required = true, dataType = "String", paramType = "query" , defaultValue="1000,2000")
	])
	def tip_list_setting(HttpServletRequest request){
		String tips = request["tips"];
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//标签
		if(StringUtils.isNotBlank(tips)){
			List userTipList = new ArrayList();
			String[] tipArray = tips.split(",");

			if(tipArray.length > 5){
				return getResult(Code.学员设置关注标签数量超出限制, Code.学员设置关注标签数量超出限制_S)
			}
			for(String row:tipArray){
				Integer tid = Integer.valueOf(row);
				//判断是不是中级的学员
				if(getIsZJStudent(user_id)){//是中级学员

				}else{//普通用户
					if(tid>=10000 && tid<=10010){//中级学员的标签范围
						return getResult(Code.学员设置关注标签数量超出权限, Code.学员设置关注标签数量超出权限_S)
					}
				}
			}

			tipArray.each {String row->
				Integer tid = Integer.valueOf(row);
				def tipContent = tip_content().findOne($$("_id" : tid) , $$("_id" : 1 , "tip_name" : 1));
				if(tipContent){
					userTipList.add(tipContent);
				}

				if(userTipList.size() > 0){
					//更新用户关注标签列表
					users().update($$("_id" : user_id) , $$('$set' : $$("tip_contents" : userTipList)));
				}
			}
		}else{
			//更新用户关注标签列表
			users().update($$("_id" : user_id) , $$('$set' : $$("tip_contents" : null)));
		}

		return getResultOK();
	}

	/**
	 * 转介绍分享
	 */
	@ResponseBody
	@RequestMapping(value = "introduceShare/*-*", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "会答2.0 转介绍分享", httpMethod = "POST" ,notes = "转介绍分享 [code : 1 , data:true/false] true第一次分享")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "share_url", value = "分享路径", required = true, dataType = "String", paramType = "query" , defaultValue="分享路径")
	])
	def introduceShare(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();
		//分享路径
		String share_url = request["share_url"];

		Boolean firstShare = shares().count($$("user_id" : user_id , "type" : 0)) == 0;

		//分享
		shares().save($$("_id" : UUID.randomUUID().toString() , "user_id" : user_id , "share_url" : share_url , "timestamp" : System.currentTimeMillis() , "type" : 0))

		//第一次分享赠送优惠券
		//		if(firstShare){
		//			discountController.getSysDiscountFromFirstShare(super.getUserPhoneByUserId());
		//		}

		return getResultOK(firstShare);
	}


	/**
	 * 转介绍分享
	 */
	@ResponseBody
	@RequestMapping(value = "introduceInfo/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "转介绍详情", httpMethod = "GET" ,notes = "转介绍详情")
	def introduceInfo(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();

		Integer inviteNum = invite().count($$("invite_user_id" : user_id)).intValue();

		//		没有就是6个月 没邀请一个人+1个月 最多+6
		Integer monthNum = 6 + (inviteNum > 6 ? 6 :inviteNum);

		Map map = new HashMap();
		map["invite_count"] = inviteNum;
		map["month_count"] = monthNum;

		return getResultOK(map);
	}

	/**
	 * 提问获取标签接口 V3.00
	 */
	@ResponseBody
	@RequestMapping(value = "topic_input_tips_v300/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "提问获取标签接口 V3.00(分类)", httpMethod = "GET", response = UserInfoTopicInputTipsV200.class , notes = "提问获取标签接口 V3.00(分类)")
	@TypeChecked(TypeCheckingMode.SKIP)
	def topic_input_tips_v300(HttpServletRequest request){
		Integer ssoUserId = Web.getCurrentSSOUserId()
		if(0 == ssoUserId) {
			return getResultToken()
		}
		def result = []
		def second_tip = [:]

		String tip_key = KeyUtils.kuaijiTip(ssoUserId)
		String jsonList = mainRedis.opsForValue().get(tip_key)
		List<String> courseNoList = JSONArray.parseArray(jsonList,String.class)
		if(null == courseNoList) {
			courseNoList = new ArrayList<>()
		}

		def parent_tip_list = tip_content().find($$("parent_tip_id":0,"product":0,"dr":0), $$("_id" : 1, "tip_name":1)).sort($$("order": 1))?.toArray()
		for (var in parent_tip_list) {
			second_tip = [:]
			def sub_tip_list = tip_content().find($$($$($and:[
				$$("parent_tip_id":var.get("_id"),"product":0,"dr":0),
				$$($or:[$$("course_no":[$in: courseNoList]),$$("is_common":1)])
			]
			)), $$("_id" : 1 , "tip_name":1 )).sort($$("order": 1))?.toArray()
			if(null != var.get("_id") && null != sub_tip_list && sub_tip_list.size() > 0){
				second_tip.put("id", var.get("_id"))
				second_tip.put("tip_name", var.get("tip_name"))
				second_tip.put("second_tip",sub_tip_list)
				result<<second_tip
			}
		}
		return getResultOKS(result)
	}

	def topic_input_tips_zikao(HttpServletRequest request){
		Integer ssoUserId = Web.getCurrentSSOUserId()
		if(0 == ssoUserId) {
			return getResultToken()
		}
		def result = []
		def second_tip = [:]

		String tip_key = KeyUtils.zikaoTip(ssoUserId)
        String jsonList = mainRedis.opsForValue().get(tip_key)
        List<String> courseNoList = JSONArray.parseArray(jsonList,String.class)
		if(null == courseNoList) {
			courseNoList = new ArrayList<>()
		}

		def parent_tip_list = tip_content().find($$("parent_tip_id":0,"product":1,"dr":0), $$("_id" : 1, "tip_name":1)).sort($$("order": 1))?.toArray()
		for (var in parent_tip_list) {
			second_tip = [:]
			def sub_tip_list = tip_content().find($$("parent_tip_id":var.get("_id"),"product":1,"dr":0,"course_no":[$in: courseNoList]), $$("_id" : 1 , "tip_name":1 )).sort($$("order": 1))?.toArray()
			if(null != var.get("_id") && null != sub_tip_list && sub_tip_list.size() > 0){
				second_tip.put("id", var.get("_id"))
				second_tip.put("tip_name", var.get("tip_name"))
				second_tip.put("second_tip",sub_tip_list)
				result<<second_tip
			}
		}
		return getResultOKS(result)
	}

	/**
	 * 提问获取标签接口 V4.00
	 */
	@ResponseBody
	@RequestMapping(value = "topic_input_tips_v400/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "提问获取标签接口 V4.00(分类)", httpMethod = "GET", response = UserInfoTopicInputTipsV200.class , notes = "提问获取标签接口 V4.00(分类)")
	@ApiImplicitParams([
			@ApiImplicitParam(name = "product", value = "产品类型（0：会计，1：自考）", required = true, dataType = "int", paramType = "query" , defaultValue="0")
	])
	@TypeChecked(TypeCheckingMode.SKIP)
	def topic_input_tips_v400(HttpServletRequest request){
		//产品类型
		int product = request["product"] as int
		if(0 == product) { //会计
			return topic_input_tips_v300(request)
		} else if(1 == product) { //自考
			return topic_input_tips_zikao(request)
		}
		return getResult(60103, "参数异常：产品类型（0：会计，1：自考）")
	}

	/**
	 * 初始化自考问题标签
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value = "init_zikao_tips", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@TypeChecked(TypeCheckingMode.SKIP)
	def init_zikao_tips(HttpServletRequest request) {

		int parent_id = 100110
		int order = 1
		try {
			String courseResult = HttpClientUtil4_3.get(SCHOOL_API_DOMAIN+"api/getCourses4LB",null)
			if(StringUtils.isNotBlank(courseResult)){
				JSONObject object = JSONObject.parseObject(courseResult)
				if(200 == object.getIntValue("code")) {
					JSONArray parentArray = object.getJSONArray("data")
					for (int i=0; i<parentArray.size(); i++) {
                        /**
                         * courses:[{
                             courseId: 633,
                             courseName: "03657学前教育研究方法",
                             courseNo: "03657"}],
                         lb: "专业课"
                         */
                        int child_id = (parent_id % 1000) * 100

						JSONObject ob = parentArray.getJSONObject(i)
						String lb = ob.getString("lb")

						def parent = new HashMap()
						parent["_id"] = parent_id
						parent["tip_name"] = lb
						parent["parent_tip_id"] = 0
						parent["product"] = 1
						parent["order"] = order
						tip_content().save($$(parent))

						JSONArray childArray = ob.getJSONArray("courses")
						for(int j=0; j<childArray.size(); j++) {
							JSONObject tip = childArray.getJSONObject(j)
							String courseName = tip.getString("courseName")
                            String courseNo = tip.getString("courseNo")

							long countTip = tip_content().count($$("course_no" : courseNo,"parent_tip_id" : parent_id))
							if(countTip == 0) {
								def child = new HashMap()
								child["_id"] = child_id
								child["course_no"] = courseNo
								child["tip_name"] = courseName
								child["parent_tip_id"] = parent_id
								child["product"] = 1
								tip_content().save($$(child))

								child_id += 1
							}
						}
                        parent_id += 10
						order++
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace()
		}
		return ["parent_tip_id" : parent_id - 10]
	}
	
	
	



	/** 2018-05-25新增提供给蓝鲸后台的接口
	 * 所有(会计标签)
	 */
	@ResponseBody
	@RequestMapping(value = "topic_input_tips_v500/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "提问获取标签接口 V5.00(分类)", httpMethod = "GET", response = UserInfoTopicInputTipsV200.class , notes = "提问获取标签接口 V5.00(分类)")
	@TypeChecked(TypeCheckingMode.SKIP)
	def topic_input_tips_v500(HttpServletRequest request){
		def result = []
		def second_tip = [:]

		def parent_tip_list = tip_content().find($$("parent_tip_id":0,"product":0), $$("_id" : 1, "tip_name":1)).limit(1000).sort($$("order": 1))?.toArray();
		for (var in parent_tip_list) {
			second_tip = [:]
			def sub_tip_list = tip_content().find($$("parent_tip_id":var.get("_id"),"product":0), $$("_id" : 1 , "tip_name":1, "parent_tip_id":1)).limit(1000).sort($$("order": 1))?.toArray();
			if(null != var.get("_id")){
				second_tip.put("_id", var.get("_id"))
				second_tip.put("tip_name", var.get("tip_name"))
				second_tip.put("parent_tip_id", -1)
				if(null != sub_tip_list){
					for (var1 in sub_tip_list) {
						result<<var1
					}
				}
				result<<second_tip
			}
		}
		return getResultOKS(result);
	}
	
	/** 2018-05-25新增提供给蓝鲸后台的接口
	 * 所有(自考标签)
	 */
	def topic_input_tips_zikao_v500(HttpServletRequest request){
		def result = []
		def second_tip = [:]

		def parent_tip_list = tip_content().find($$("parent_tip_id":0,"product":1), $$("_id" : 1, "tip_name":1)).limit(1000).sort($$("order": 1))?.toArray()
		for (var in parent_tip_list) {
			second_tip = [:]
			def sub_tip_list = tip_content().find($$("parent_tip_id":var.get("_id"),"product":1), $$("_id" : 1 , "tip_name":1, "parent_tip_id":1)).limit(1000).sort($$("order": 1))?.toArray();
			if(null != var.get("_id")){
				second_tip.put("_id", var.get("_id"))
				second_tip.put("tip_name", var.get("tip_name"))
				second_tip.put("parent_tip_id", -2)
				if(null != sub_tip_list){
					for (var1 in sub_tip_list) {
						result<<var1
					}
				}
				result<<second_tip
			}
		}
		return getResultOKS(result)
	}

	/** 2018-05-25新增提供给蓝鲸后台的接口
	 * 提问获取标签接口 V5.00
	 */
	@ResponseBody
	@RequestMapping(value = "topic_input_tips_lanjing/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "提问获取标签接口 V5.00(分类)", httpMethod = "GET", response = UserInfoTopicInputTipsV200.class , notes = "提问获取标签接口 V5.00(分类)")
	@ApiImplicitParams([
			@ApiImplicitParam(name = "product", value = "产品类型（0：会计，1：自考）", required = true, dataType = "int", paramType = "query" , defaultValue="0")
	])
	@TypeChecked(TypeCheckingMode.SKIP)
	def topic_input_tips_lanjing(HttpServletRequest request){
		//产品类型
		int product = request["product"] as int
		if(0 == product) { //会计
			return topic_input_tips_v500(request)
		} else if(1 == product) { //自考
			return topic_input_tips_zikao_v500(request)
		}
		return getResult(60103, "参数异常：产品类型product（0：会计，1：自考）")
	}
	
	
	
	
	/** 2018-05-30新增提供给蓝鲸后台的接口
	 * 所有(会计标签)
	 */
	@ResponseBody
	@RequestMapping(value = "topic_input_tips_v600/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "提问获取标签接口 V6.00(分类)", httpMethod = "GET", response = UserInfoTopicInputTipsV200.class , notes = "提问获取标签接口 V6.00(分类)")
	@TypeChecked(TypeCheckingMode.SKIP)
	def topic_input_tips_v600(HttpServletRequest request){
		def parent_tip_list = tip_content().find($$("parent_tip_id":0,"product":0), $$("_id" : 1, "tip_name":1,"parent_tip_id":1)).limit(1000).sort($$("order": 1))?.toArray();
		for (var in parent_tip_list) {
			var.put("parent_tip_id", -1);
		}
		return getResultOKS(parent_tip_list);
	}
	
	/** 2018-05-30新增提供给蓝鲸后台的接口
	 * 所有(自考标签)
	 */
	def topic_input_tips_zikao_v600(HttpServletRequest request){
		def parent_tip_list = tip_content().find($$("parent_tip_id":0,"product":1), $$("_id" : 1, "tip_name":1,"parent_tip_id":1)).limit(1000).sort($$("order": 1))?.toArray()
		for (var in parent_tip_list) {
			var.put("parent_tip_id", -2);
		}
		return getResultOKS(parent_tip_list)
	}

	/** 2018-05-30新增提供给蓝鲸后台的接口
	 * 提问获取标签接口 V6.00
	 */
	@ResponseBody
	@RequestMapping(value = "topic_input_tips_lanjing_v1/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "提问获取标签接口 V6.00(一级分类)", httpMethod = "GET", response = UserInfoTopicInputTipsV200.class , notes = "提问获取标签接口 V6.00(分类)")
	@ApiImplicitParams([
			@ApiImplicitParam(name = "product", value = "产品类型（0：会计，1：自考）", required = true, dataType = "int", paramType = "query" , defaultValue="0")
	])
	@TypeChecked(TypeCheckingMode.SKIP)
	def topic_input_tips_lanjing_v1(HttpServletRequest request){
		//产品类型
		int product = request["product"] as int
		if(0 == product) { //会计
			return topic_input_tips_v600(request)
		} else if(1 == product) { //自考
			return topic_input_tips_zikao_v600(request)
		}
		return getResult(60103, "参数异常：产品类型product（0：会计，1：自考）")
	}
}
