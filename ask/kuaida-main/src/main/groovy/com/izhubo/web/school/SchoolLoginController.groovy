package com.izhubo.web.school;
import javax.annotation.Resource
import java.nio.charset.Charset;

import static com.izhubo.rest.common.doc.MongoKey.$set
import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.commons.lang3.StringUtils
import org.hibernate.SessionFactory
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

import com.hqonline.model.HK
import com.izhubo.model.Code
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.util.MsgDigestUtil
import com.izhubo.utils.DataUtils
import com.izhubo.web.BaseController
import com.izhubo.web.CommoditysController
import com.izhubo.web.api.Web
import com.izhubo.web.vo.BaseResultVO
import com.izhubo.web.vo.DailyRecommandVO
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation
import com.izhubo.rest.common.util.http.HttpClientUtil4_3;
import com.izhubo.rest.common.util.JSONUtil
import org.springframework.beans.factory.annotation.Value
@RestWithSession
@RequestMapping("/schoollogin")
public class SchoolLoginController extends BaseController {

	@Resource
	private SessionFactory sessionFactory;


	private DBCollection school_pic(){
		return mainMongo.getCollection("school_pic");
	}

	private DBCollection school_notice(){
		return mainMongo.getCollection("school_notice");
	}
	private DBCollection commodity_schools(){
		return mainMongo.getCollection("commodity_schools");
	}
	//	private DBCollection school_test(){
	//		return mainMongo.getCollection("school_test");
	//	}
	private DBCollection province_exam(){
		return mainMongo.getCollection("province_exam_url");
	}
	/** 商机 */
	private DBCollection hq_student_records(){
		return mainMongo.getCollection("hq_student_records");
	}
	/** 课程列表 */
	public DBCollection bannerCourse() {
		return mainMongo.getCollection("banner_course");
	}
	/** 上门服务 */
	public DBCollection service_home() {
		return mainMongo.getCollection("service_home");
	}
	private DBCollection school_menu(){
		return mainMongo.getCollection("school_menu");
	}
	@Resource
	private CommoditysController commoditysController;

	//注意距离要除以111.2（1度=111.2km）
	private float maxDistance = 0.009;              //    0.5/111.12  500米范围内，如果有目标校区，则有效


	private String ALL_SCHOOL_KEY= "all";


	public DBCollection area() {
		return mainMongo.getCollection("area");
	}

	private static final String SECRET_AESKEY = "%^\$AF>.12*******";
	private static final String EXAMWORK_MD5KEY = "hqzxtk";

	@ResponseBody
	@RequestMapping(value = "serviceHome/*-*", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "上门服务", httpMethod = "POST",  notes = "上门服务")
	@ApiImplicitParams([
		//		1.财务信息
		@ApiImplicitParam(name = "company_name", value = "企业名称", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "company_addres", value = "企业地址", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "company_type", value = "企业性质", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "company_industry", value = "所属行业", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "company_peoples", value = "企业人数-非必填", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "company_filiale", value = "是否有分公司", required = true, dataType = "int", paramType = "query"),
		//		2.财务状况
		@ApiImplicitParam(name = "finance_peoples", value = "财务人数", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "finance_job_max", value = "财务最高岗位", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "finance_use_sofe", value = "是否使用财务软件", required = true, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "finance_basic_system", value = "是否有财务基本制度", required = true, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "finance_full", value = "是否完整有财务", required = true, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "finance_report", value = "是否有财务报表", required = true, dataType = "int", paramType = "query"),
		//		3.服务需求
		@ApiImplicitParam(name = "finance_demand", value = "财务需求", required = true, dataType = "String", paramType = "query"),
		//		4.个人信息你
		@ApiImplicitParam(name = "contacts_name", value = "姓名", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "contacts_sex", value = "性别", required = true, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "contacts_identity", value = "身份", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "contacts_phone", value = "手机号码", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "contacts_email", value = "邮箱-非必填", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "auth_code", value = "验证码", required = true, dataType = "String", paramType = "query")
	])
	def serviceHome(HttpServletRequest request , HttpServletResponse response){
		//手机号码
		String contacts_phone = ServletRequestUtils.getStringParameter(request, "contacts_phone");
		//验证码
		String auth_code = ServletRequestUtils.getStringParameter(request, "auth_code");
		boolean isSecurity = isRegisterController(contacts_phone, auth_code);
		if (!isSecurity) {
			return getResult(0, "验证码错误", "验证码错误");
		}

		//企业名称
		String company_name = ServletRequestUtils.getStringParameter(request, "company_name");
		//企业地址
		String company_addres = ServletRequestUtils.getStringParameter(request, "company_addres");
		//企业性质
		String company_type = ServletRequestUtils.getStringParameter(request, "company_type");
		//所属行业
		String company_industry = ServletRequestUtils.getStringParameter(request, "company_industry");
		//企业人数-非必填
		String company_peoples = ServletRequestUtils.getStringParameter(request, "company_peoples");
		//是否有分公司
		String company_filiale = ServletRequestUtils.getStringParameter(request, "company_filiale");

		//财务人数
		String finance_peoples = ServletRequestUtils.getStringParameter(request, "finance_peoples");
		//财务最高岗位
		String finance_job_max = ServletRequestUtils.getStringParameter(request, "finance_job_max");
		//是否使用财务软件
		Integer finance_use_sofe = ServletRequestUtils.getIntParameter(request, "finance_use_sofe" , 0);
		//是否有财务基本制度
		Integer finance_basic_system = ServletRequestUtils.getIntParameter(request, "finance_basic_system" , 0);
		//是否完整有财务
		Integer finance_full = ServletRequestUtils.getIntParameter(request, "finance_full" , 0);
		//是否有财务报表
		Integer finance_report = ServletRequestUtils.getIntParameter(request, "finance_report" , 0);

		//财务需求
		String finance_demand = ServletRequestUtils.getStringParameter(request, "finance_demand");

		//姓名
		String contacts_name = ServletRequestUtils.getStringParameter(request, "contacts_name");
		//性别
		Integer contacts_sex = ServletRequestUtils.getIntParameter(request, "contacts_sex" , 0);
		//身份
		String contacts_identity = ServletRequestUtils.getStringParameter(request, "contacts_identity");
		//		//手机号码
		//		String contacts_phone = ServletRequestUtils.getStringParameter(request, "contacts_phone");
		//邮箱-非必填
		String contacts_email = ServletRequestUtils.getStringParameter(request, "contacts_email");
		//		//验证码
		//		String auth_code = ServletRequestUtils.getStringParameter(request, "auth_code");
		//操作用户id
		Integer user_id = Web.currentUserId;
		//参数非空校验
		if(
		StringUtils.isNotBlank(company_name) &&
		StringUtils.isNotBlank(company_addres) &&
		StringUtils.isNotBlank(company_type) &&
		StringUtils.isNotBlank(company_industry) &&
		StringUtils.isNotBlank(company_filiale) &&

		StringUtils.isNotBlank(finance_peoples) &&

		StringUtils.isNotBlank(finance_demand) &&

		StringUtils.isNotBlank(contacts_name) &&
		StringUtils.isNotBlank(contacts_identity) &&
		StringUtils.isNotBlank(contacts_phone)
		){

			service_home().save($$(
					"_id" : UUID.randomUUID().toString(),

					"company_name" : company_name ,
					"company_addres" : company_addres ,
					"company_type" : company_type ,
					"company_industry" : company_industry ,
					"company_peoples" : company_peoples ,
					"company_filiale" : company_filiale ,

					"finance_peoples" : finance_peoples ,
					"finance_job_max" : finance_job_max ,
					"finance_use_sofe" : finance_use_sofe ,
					"finance_basic_system" : finance_basic_system ,
					"finance_full" : finance_full ,
					"finance_report" : finance_report ,

					"finance_demand" : finance_demand ,

					"contacts_name" : contacts_name ,
					"contacts_sex" : contacts_sex ,
					"contacts_identity" : contacts_identity ,
					"contacts_phone" : contacts_phone ,
					"contacts_email" : contacts_email ,

					"user_id" : user_id ,
					"timestamp" : System.currentTimeMillis(),
					"state" : 0,
					"dr" : 0,
					"IP" : getIpAddr(request)
					));

			return getResultOK();
		}
		return getResultParamsError();
	}

	// 校验验证码
	public boolean isRegisterController(String phone, String security) {
		boolean res = false;
		if (StringUtils.isNotBlank(security) && StringUtils.isNotBlank(phone)) {
			String rs = mainRedis.opsForValue().get(HK.SECURITY.getSecurityKey(phone));
			res = security.equals(rs);
		}
		return res;
	}

	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	@ResponseBody
	@RequestMapping(value = "toExamWork/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "考试作业-跳转地址-重定向", httpMethod = "GET",  notes = "考试作业-跳转地址-重定向")
	def	toExamWork(HttpServletRequest request , HttpServletResponse response){
		//		http://123.57.39.129:8000/ErrLogin.aspx?sso_date=13541905982&sign_cipher=ceb40ea85ade9d8715e41f3796c541b0
		//		参数：
		//		loginCode + md5Key   =“hqzxtk”
		//用户手机号码
		String username = getUserPhoneByUserId();
		//加密后的结果
		String  md5result = MsgDigestUtil.MD5.digest2HEX(username+EXAMWORK_MD5KEY);

		response.sendRedirect("http://123.57.39.129:8000/ErrLogin.aspx?sso_date=${username}&sign_cipher=${md5result}");
	}

	@ResponseBody
	@RequestMapping(value = "toCourseWarp/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "备课中心-跳转地址", httpMethod = "GET",  notes = "备课中心-跳转地址")
	def	toCourseWarp(HttpServletRequest request , HttpServletResponse response){
		//		http://nx.kjcity.com/Web/CourseByWap/List?phone=13620535450&k=B3C8BC4427429E807720AD49DCB3FC97
		//		参数：
		//		var phone = "13620535450";
		//		var k = (phone + "%^$AF>.12*******").MD5();
		//		k是MD5加密后的字符串

		//用户手机号码
		String username = getUserPhoneByUserId();
		//加密后的结果
		String  md5result = MsgDigestUtil.MD5.digest2HEX(username+SECRET_AESKEY).toUpperCase();

		response.sendRedirect("http://nx.kjcity.com/Web/CourseByWap/List?phone=${username}&k=${md5result}");
	}


	@ResponseBody
	@RequestMapping(value = "recordList/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "商机列表", httpMethod = "GET",response = BaseResultVO.class,  notes = "获取最近两天的上级列表.当code=Integer(60111)时,错误消息为:教师账号未正确关联校区")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "size", value = "每页size条数据,默认20条", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "第page页,默认第1页", required = false, dataType = "int", paramType = "query"),
	])
	def recordList(HttpServletRequest request){
		//用户id
		Integer user_id = Web.getCurrentUserId();

		def user = users().findOne($$("_id" : user_id , "priv" : 2) , $$("school_code" : 1 , "nc_id" : 1));

		if(null != user && null != user["nc_id"] && null != user["school_code"]){
			//教师NC_ID
			String nc_id = user["nc_id"];
			//校区NC_CODE
			String school_code = user["school_code"];
			//校区NC_ID
			String school_nc_id = null;
			//校区
			def school = area().findOne("code" : school_code , $$("nc_id" : 1));
			if(null != school && null != school["nc_id"]){
				//校区NC_ID
				school_nc_id = school["nc_id"];
				//分页
				int size = ServletRequestUtils.getIntParameter(request, "size", 20);
				int page = ServletRequestUtils.getIntParameter(request, "page", 1);

				def recordData = hq_student_records().find(
						$$("pk_org" : school_nc_id) ,
						$$("student_mobile" : 1 ,"student_name" : 1 , "student_state_name" : 1 , "remark" : 1)
						).sort($$("type" : 1, "update_at" : -1 , "timestamp" : -1)).skip((page - 1) * size).limit(size)?.toArray();
				return getResultOK(recordData);
			}

		}
		return getResult(Code.教师账号未正确关联校区, Code.教师账号未正确关联校区_S);
	}




	def get_school_list(String citycode)
	{
		def companylist =  area().find($$("parent_nc_code":citycode));
		def schoollist =[];

		companylist.each {BasicDBObject row ->

			def school = mainMongo.getCollection("area").findOne($$(["parent_nc_code" : row.get("code"),"dr":0]));


			schoollist.add(school);

		}


		return schoollist;
	}


	@ResponseBody
	@RequestMapping(value = "get_school_detail/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取校区详情 ", httpMethod = "GET",response = BaseResultVO.class,  notes = "获取校区详情")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "school_code", value = "校区编码", required = false, dataType = "String", paramType = "query")
	])
	def get_school_detail(HttpServletRequest request){


		String schoolcode  = ServletRequestUtils.getStringParameter(request, "school_code");

		Map<String, Object> result = new HashMap<String, Object>();


		def school = mainMongo.getCollection("area").findOne($$(["code" : schoolcode]));

		result.put("school", school);


		return getResultOK(result);

	}


	@ResponseBody
	@RequestMapping(value = "get_school_index/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "移动端校区首页接口", httpMethod = "GET",response = BaseResultVO.class,  notes = "移动端校区首页接口")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "school_code", value = "校区编码", required = false, dataType = "String", paramType = "query")
	])
	def get_school_index(HttpServletRequest request){


		String schoolcode  = ServletRequestUtils.getStringParameter(request, "school_code");

		Map<String, Object> result = new HashMap<String, Object>();
		//获取校区图片
		result.put("pic_list", get_school_activity310(schoolcode));
		//获取校区活动
		result.put("notice_list", get_school_activity(schoolcode));

		return getResultOK(result);

	}

	@ResponseBody
	@RequestMapping(value = "get_school_all", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取校区详情", httpMethod = "GET",response = BaseResultVO.class,  notes = "获取校区详情")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "school_code", value = "校区编码", required = false, dataType = "String", paramType = "query")
	])
	def get_school_all(HttpServletRequest request){
		//menu_type 0:表示跳转网页 1:表示直接弹出提示 2:前往校区 3:在线咨询 4:致电校区 5:投诉反馈
		String schoolcode  = ServletRequestUtils.getStringParameter(request, "school_code");
		String access_token  = ServletRequestUtils.getStringParameter(request, "access_token");
		Integer android_code  = ServletRequestUtils.getIntParameter(request, "android_code");
		Integer ios_code  = ServletRequestUtils.getIntParameter(request, "iOS_code");
		Integer is_student  = ServletRequestUtils.getIntParameter(request, "is_student");
		Map<String, Object> result = new HashMap<String, Object>();
		def MenuList = [];
		//def	query = ;
		MenuList = school_menu().find($$($or : [
			$$( "school_code" ,   schoolcode),
			$$( "school_code" ,   "all")
		])).sort($$("menu_order",1)).toArray();
		Integer k = 0;
		Integer f = 0;
		if(is_student != null){
			if(MenuList){
				MenuList.each { def dbo ->
					
					if("学习中心".equals(dbo["menu_title"])){
						f = k;
					}
					k++;
					//判断版本号，低于最低所需的版本号则提示必须升级
					if(dbo["need_student"] == 1 && is_student == 0){
						dbo["menu_type"] = 1;
						dbo["menu_action"] = "您暂无权限";
					}
					
				}
			}
		}
		if((android_code == null && ios_code==null) || (is_student == null)){
			return getResult(Code.校区未上传版本号, Code.校区未上传版本号_S,null);
		}
		if(android_code >= 50 || ios_code >= 320){
			MenuList.remove(f);
		}
		
		if(android_code != null){
			if(MenuList){
				MenuList.each { def dbo ->
					//判断版本号，低于最低所需的版本号则提示必须升级
					if(android_code < dbo["android_minCode"] ){
						dbo["menu_type"] = 1;
						dbo["menu_action"] = "请升级最新版本";
					}else{
						String menu_action = dbo["menu_action"];
						if(menu_action.contains("http")){
							menu_action =  menu_action.replace("{token}",access_token);
							menu_action =  menu_action.replace("{school_code}",schoolcode);
							dbo["menu_action"] = menu_action;
						}
					}
				}
			}
		}
		if(ios_code != null){
			if(MenuList){
				MenuList.each { def dbo ->
					//判断版本号，低于最低所需的版本号则提示必须升级
					if(ios_code < dbo["ios_minCode"] ){
						dbo["menu_type"] = 1;
						dbo["menu_action"] = "请升级最新版本";					
						}else{
						String menu_action = dbo["menu_action"];
						if(menu_action.contains("http")){
							menu_action =  menu_action.replace("{token}",access_token);
							menu_action =  menu_action.replace("{school_code}",schoolcode);
							dbo["menu_action"] = menu_action;
						}
					}
				}
			}
		}
		result.put("menu_list", MenuList)
		//获取校区图片
		result.put("pic_list", get_school_activity310(schoolcode));
		//获取校区活动  放弃
		//result.put("notice_list", get_school_activity(schoolcode));

		return getResultOK(result);

	}


	@ResponseBody
	@RequestMapping(value = "get_school_index_notstudent/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "移动端校区首页接口-非学员", httpMethod = "GET",response = BaseResultVO.class,  notes = "移动端校区首页接口-非学员")
	//	@ApiImplicitParams([@ApiImplicitParam(name = "school_code", value = "校区编码", required = false, dataType = "String", paramType = "query")])
	def get_school_index_notstudent(HttpServletRequest request){



		Map<String, Object> result = new HashMap<String, Object>();
		//获取校区图片
		result.put("pic_list", get_school_activity310(ALL_SCHOOL_KEY));
		result.put("course_list", get_school_course(ALL_SCHOOL_KEY));

		return getResultOK(result);

	}

	@ResponseBody
	@RequestMapping(value = "get_school_test/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "获取会计考试报名列表", httpMethod = "GET",response = BaseResultVO.class,  notes = "获取会计考试报名列表")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "school_code", value = "校区编码", required = false, dataType = "String", paramType = "query")
	])
	def get_school_test(HttpServletRequest request){
		//校区编码
		String schoolcode  = ServletRequestUtils.getStringParameter(request, "school_code");
		//校验参数非空
		if(StringUtils.isBlank(schoolcode)){
			schoolcode = "JH104080101"
		}
		List uList = new ArrayList();
		//通过校区code获取省code
		String province_code = getProvinceCode(schoolcode);
		//显示的字段
		def pshow = $$("url" : 1 , "code" : 1);
		//排序
		def psort = $$("order" : 1 , "code" : 1 , "_id" : 1);

		//会计从业考试
		def urlList0 = province_exam().find(
				$$("code" : $$('$in' : [province_code , "all"]) , "type" : 0),
				pshow
				).sort(psort).limit(1)?.toArray();
		if(null != urlList0 && urlList0.size() == 1){
			def item = urlList0.get(0);
			Map map = new HashMap();
			map.put("name", "会计从业考试")
			map.put("url", item["url"]);
			map.put("code", item["code"]);
			uList.add(map);
		}
		//初级会计师考试
		def urlList1 = province_exam().find(
				$$("code" : $$('$in' : [province_code , "all"]) , "type" : 1),
				pshow
				).sort(psort).limit(1)?.toArray();
		if(null != urlList1 && urlList1.size() == 1){
			def item = urlList1.get(0);
			Map map = new HashMap();
			map.put("name", "初级会计师考试")
			map.put("url", item["url"]);
			map.put("code", item["code"]);
			uList.add(map);
		}
		//中级会计师考试
		def urlList2 = province_exam().find(
				$$("code" : $$('$in' : [province_code , "all"]) , "type" : 2),
				pshow
				).sort(psort).limit(1)?.toArray();
		if(null != urlList2 && urlList2.size() == 1){
			def item = urlList2.get(0);
			Map map = new HashMap();
			map.put("name", "中级会计师考试")
			map.put("url", item["url"]);
			map.put("code", item["code"]);
			uList.add(map);
		}
		//高级会计师考试
		def urlList3 = province_exam().find(
				$$("code" : $$('$in' : [province_code , "all"]) , "type" : 3),
				pshow
				).sort(psort).limit(1)?.toArray();
		if(null != urlList3 && urlList3.size() == 1){
			def item = urlList3.get(0);
			Map map = new HashMap();
			map.put("name", "注册会计师");
			//			map.put("name", "高级会计师考试")
			map.put("url", item["url"]);
			map.put("code", item["code"]);
			uList.add(map);
		}
		return getResultOK(uList);
	}


	@ResponseBody
	@RequestMapping(value = "get_school_notice_list/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "移动端公告列表", httpMethod = "GET",response = BaseResultVO.class,  notes = "移动端公告列表")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "school_code", value = "校区编码", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "第page页", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页size条数据", required = false, dataType = "int", paramType = "query")
	])
	@TypeChecked(TypeCheckingMode.SKIP)
	def get_school_notice_list(HttpServletRequest request){


		String schoolcode  = ServletRequestUtils.getStringParameter(request, "school_code");
		Integer page  = ServletRequestUtils.getIntParameter(request, "page",1);
		Integer size  = ServletRequestUtils.getIntParameter(request, "size",20);
		def sort = $$("timestamp" : -1);
		def	query = $$(
				$or : [
					$$( "school_code" ,   schoolcode),
					$$( "school_code" ,   "all")
				]
				);
		long count =school_notice().count(query);
		Integer allpage = count / size + ((count% size) >0 ? 1 : 0);
		//查询结果
		def queryResult = null;
		if(count > 0){
			//需要查询的字段
			BasicDBObject show = new BasicDBObject();
			show.append("activity_info" , 0);
			queryResult = school_notice().find(query,show).sort(sort).skip((page - 1) * size).limit(size).toArray();
		}





		return getResultOK(queryResult, allpage, count , page , size);





	}





	def get_school_pic(String schoolcode)
	{
		def	query = $$(
				$or : [
						$$( "school_code" ,   schoolcode),
						$$( "school_code" ,   "all")
				]
		);
		return  school_pic().find(query).sort($$("timestamp",1)).limit(4).toArray();

	}

	def get_school_activity(String schoolcode)
	{

		def activlist = [];
		def	query = $$(
				$or : [
					$$( "school_code" ,   schoolcode),
					$$( "school_code" ,   "all")
				]
				);

		BasicDBObject show = new BasicDBObject();
		show.append("activity_info" , 0)
		activlist = school_notice().find(query,show).sort($$("timestamp",-1)).limit(4).toArray();

		return activlist;
	}

	def get_school_activity310(String schoolcode)
	{

		def activlist = [];
		def	query = $$(
				$or : [
						$$( "school_code" ,   schoolcode),
						$$( "school_code" ,   "all")
				]
		);
		query.append("is_recommand",1);
		query.append("dr",0);

		BasicDBObject show = new BasicDBObject();
		show.append("activity_info" , 0)
		def resultList = new ArrayList();


//		{
//			"_id" : "23c166a6-304a-461b-8574-5aad91b7b815",
//			"create_user_id" : 12799,
//			"school_code" : "all",
//			"school_name" : "全部校区",
//			"pic_url" : "http://answerimg.kjcity.com/56/0/1481510230136.jpg",
//			"create_time" : NumberLong(1481508875748),
//			"timestamp" : NumberLong(1481510230973),
//			"update_time" : NumberLong(1481510230973)
//		}

		activlist = school_notice().find(query,show).sort($$("timestamp",-1)).limit(8).toArray();
		activlist.each {BasicDBObject x->
			def	item = $$("_id",x.get("_id"));
			item.append("school_code",x.get("school_code"));
			item.append("school_name",x.get("school_name"));
			item.append("pic_url",x.get("activity_picurl"));
			item.append("url",x.get("activity_infourl"));
			resultList.add(item);
		}


		return resultList;
	}


	def get_school_course(String schoolcode)
	{
		def list = bannerCourse().find(null , $$("url" : 1 , "pic" : 1 , "_id" : 0)).sort($$("sort" : 1)).limit(4).toArray();

		return list;
	}


	/**
	 * 会答首页-每日推送api
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "notice_detail/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "公告详情", httpMethod = "GET",  notes = "公告详情接口", response = DailyRecommandVO.class)
	@ApiImplicitParams([
		@ApiImplicitParam(name = "notice_id", value = "公告id", required = false, dataType = "String", paramType = "query")
	])
	def notice_detail(
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		String notice_id  = ServletRequestUtils.getStringParameter(request, "notice_id");
		BasicDBObject query = new BasicDBObject();


		//query.append("recommend_type" , DailyRecommendType.文章.ordinal());
		query.append("_id" , notice_id);


		def queryResult = school_notice().findOne(query);

		Integer count = 0;

		count = (queryResult["read_count"] ==null?0:(Integer)queryResult["read_count"]);

		count++;


		BasicDBObject up = new BasicDBObject("read_count", count);
		school_notice().update(query, new BasicDBObject($set, up));


		queryResult["timestamp"] = DataUtils.dateToString(Long.valueOf(queryResult["timestamp"].toString()));

		queryResult["read_count"] = count;

		return getResultOK(queryResult);
	}

	@Value("#{application['admin.domain']}")
	private String admin_domain ="http://183.63.120.222:8015/";

	@Value("#{application['hqjy.domain']}")
	private String hqjydomain ="http://test.hqjy.com/";


	private String credit_url="credit/getMyCredit.json";

	private String credit_cash_url="/credit/getCashCoupon.json";

	private String md5key = '6512bd43d9caa6e02c990b$AF>.12**';

	/**
	 * 会答首页-每日推送api
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "credit/*-*", method = RequestMethod.GET, produces = "application/html; charset=utf-8")
	@ApiOperation(value = "获取积分详情", httpMethod = "GET",  notes = "获取积分详情", response = DailyRecommandVO.class)
	def credit(
			HttpServletRequest request,HttpServletResponse res
	){
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		String nc_id = Web.getCurrentUserNcId();


		///credit/getMyCredit.json?studentid=&secretKey=
		String MYMD5Result = MsgDigestUtil.MD5.digest2HEX(nc_id+md5key);
		String reqResult = HttpClientUtil4_3.get(admin_domain+credit_url+"?studentid="+nc_id+"&secretKey="+MYMD5Result,null, Charset.forName("UTF-8"));

		res.setContentType("text/html; charset=utf-8")
		def out = res.getWriter()
		out.println(reqResult)
		out.close()


	}


	public static String replacer(String outBuffer) {
		String data = outBuffer.toString();
		try {
			data = data.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
			data = data.replaceAll("\\+", "%2B");
			data = URLDecoder.decode(data, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	@ResponseBody
	@RequestMapping(value = "credit2cash/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "兑换积分接口", httpMethod = "GET",  notes = "获取积分详情", response = DailyRecommandVO.class)
	def credit2cash(
			HttpServletRequest request
	){
		//TODO 待测试 消息返回结构VO
		//查询条件 已上架 套餐商品
		String nc_id = Web.getCurrentUserNcId();
		String phone = getUserPhoneByUserId();

		///credit/getMyCredit.json?studentid=&secretKey=
		String MYMD5Result = MsgDigestUtil.MD5.digest2HEX(nc_id+md5key);

		String class_id = request["class_id"];

		String reqResult = HttpClientUtil4_3.post(admin_domain+credit_cash_url+"?studentid="+nc_id+"&secretKey="+MYMD5Result+"&classId="+class_id,null,null);

		Map _jsonMap = JSONUtil.jsonToMap(reqResult);

		int number = (int)_jsonMap.get("number");
		String reqResult2 = "0";
		if(number>0) {

			 reqResult2 = HttpClientUtil4_3.post(hqjydomain + "api/credit.ashx?name=" + phone + "&classId=" + class_id + "&number=" + number, null, null);
		}



		 return  getResultOK(reqResult2);
	}


	public static void main(String[] args) throws Exception {



	}
}

