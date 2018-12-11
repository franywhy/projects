package com.izhubo.admin

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import static com.izhubo.rest.common.util.WebUtils.$$;
import static com.izhubo.rest.common.doc.MongoKey.ALL_FIELD
import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import java.util.regex.Pattern
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import net.sf.json.JSONArray
import net.sf.json.JSONObject
import org.apache.commons.lang.StringUtils
import com.izhubo.common.util.JSONHelper
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.common.util.JSONUtil
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
import com.izhubo.model.CoursewareStates

/**
 * 2015-01-19
 * @author hzj
 */
@RestWithSession
class CourseController extends BaseController{
		
	@Resource
	KGS maintypeKGS;

	DBCollection courseDB(){
		mainMongo.getCollection('course')
	}
	DBCollection usersDB(){
		mainMongo.getCollection('users')
	}
	
	DBCollection roomsDB(){
		mainMongo.getCollection('rooms')
	}
	DBCollection coursewareDB(){
		mainMongo.getCollection('courseware')
	}
	DBCollection professionalDB(){
		mainMongo.getCollection('professional')
	}
	DBCollection maintypeDB(){
		mainMongo.getCollection('main_type')
	}
	
	DBCollection company(){
		mainMongo.getCollection('company')};
	

	// 课程档案Pattern pattern = Pattern.compile("^.*" + company_code + ".*\$", Pattern.CASE_INSENSITIVE)
	//		query.and("company_code").regex(pattern)
	def list(HttpServletRequest req){
		QueryBuilder query = QueryBuilder.start();
		def course_name = req.getParameter("course_name");
		if(StringUtils.isNotBlank(course_name)){
			Pattern pattern = Pattern.compile("^.*" + course_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("course_name").regex(pattern)
		}
		def course_code = req.getParameter("course_code");
		if(StringUtils.isNotBlank(course_code)){
			Pattern pattern = Pattern.compile("^.*" + course_code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("course_code").regex(pattern)
		}
		Map user = (Map)req.getSession().getAttribute("user");
	//	def company_id = user.get("company_id");  待用，根据当前自己公司操作自己公司的数据
	//	query.and("company_id").is(company_id);
		Crud.list(req,courseDB(),query.get(),ALL_FIELD, MongoKey.SJ_DESC);
	}
	
	@TypeChecked(TypeCheckingMode.SKIP)
	def edit(HttpServletRequest req){
 		String _id = req.getParameter("_id");
		String course_code = req.getParameter("course_code");
		def container =req.getParameter("json");
		// 课程编号
		def addCourse = $$("course_code":course_code);
		// 一个name多个值转换
		JSONObject jobj = JSONUtil.jsonToMap(container);
		JSONArray chapter = jobj.getJSONArray("chapter");
		JSONArray course_teacher = jobj.getJSONArray("course_teacher");
		JSONArray course_price = jobj.getJSONArray("course_price");
				 
		def  chapterList= JSONHelper.JsonToList(chapter);
		def  course_teacherList=JSONHelper.JsonToList(course_teacher);
		def  course_priceList=JSONHelper.JsonToList(course_price);
		 
		if(StringUtils.isNotBlank(container)){
			List chapterObjLists = new ArrayList();
			List course_teacherObjLists = new ArrayList();
			List course_priceObjLists = new ArrayList();
			//1章节
			for(int i=0;i<chapterList.size();i++){
				BasicDBObject chapterObjList = new BasicDBObject();
				 Map m = chapterList.get(i);
				 String chapter_name =  m.get("chapter_name");
				 String chapter_introduction =  m.get("chapter_introduction");
				 String parameters =  m.get("parameters");
				 //1.1章节包含课件
				 List<Map> coursewareList = (List<Map>)m.get("products");
				 if(coursewareList){
					 for(Map ps:coursewareList){
						Boolean flg = ps.get("is_free").equals("true")?true:false;
						ps.put("is_free",flg);
					
						ps.put("sepNum",ps.get("sepNum")==null?maintypeKGS.nextId():ps.get("sepNum") as Integer);
					 }
				 }			 
				 chapterObjList.put("chapter_courseware",coursewareList);			 
				 chapterObjList.put("_id", m.get("_id")==null?UUID.randomUUID().toString():m.get("chapter_id"));//
				 chapterObjList.put("chapter_code",m.get("chapter_code")==null?maintypeKGS.nextId():m.get("chapter_code"));//
				 chapterObjList.put("chapter_name", chapter_name);
				 chapterObjList.put("chapter_introduction", chapter_introduction);
				 chapterObjList.put("parameters", parameters as Integer);
				 chapterObjLists.add(chapterObjList);
			}
			//2.课程对应老师
			for(int i=0;i<course_teacherList.size();i++){
				BasicDBObject course_teacherObjList = new BasicDBObject();
				Map m = course_teacherList.get(i);
				def answer_name =  m.get("answer_name");
				def teach_id =  m.get("teach_id");							
				course_teacherObjList.put("answer_id",m.get("answer_id")==null?UUID.randomUUID().toString():m.get("answer_id"));//							
				course_teacherObjList.put("answer_name", answer_name);
				course_teacherObjList.put("teach_id", teach_id as Integer);
				course_teacherObjLists.add(course_teacherObjList);
			}
			
			//3.课程对应价格
			for(int i=0;i<course_priceList.size();i++){
				BasicDBObject course_priceObjList = new BasicDBObject();
				Map m = course_priceList.get(i);
				def price =  m.get("price");
				def item_type =  m.get("item_type");
				course_priceObjList.put("price_id",m.get("price_id")==null?UUID.randomUUID().toString():m.get("price_id"));
				
				course_priceObjList.put("price", price as Double);
				course_priceObjList.put("item_type", item_type as Integer);
				course_priceObjLists.add(course_priceObjList);
			}
			addCourse.append("chapter", chapterObjLists);
			addCourse.append("course_teacher", course_teacherObjLists);
			addCourse.append("course_price", course_priceObjLists);
		}
			addCourse.append("course_name", req.getParameter("course_name"));//课名称
			addCourse.append("class_hour", req.getParameter("class_hour") as Integer);//课时
			addCourse.append("main_type_id", req.getParameter("main_type_id"));//频道档案
			addCourse.append("course_url", req.getParameter("course_url"));//图片
			addCourse.append("is_vip", req.getParameter("is_vip").equals("true")?true:false);//是否vip
			addCourse.append("courseware_id", req.getParameter("courseware_id"));//课件
			addCourse.append("company_id", req.getParameter("company_id"));//公司
			addCourse.append("indusetry_id", req.getParameter("indusetry_id") as Integer);//行业
			addCourse.append("professional_id", req.getParameter("professional_id"));//专业
			addCourse.append("rooms_id", req.getParameter("rooms_id"));//直播
			addCourse.append("teacher_id", req.getParameter("teacher_id") as Integer);//老师
			addCourse.append("introduction", req.getParameter("introduction") );//简介
			addCourse.append("introduction_html", jobj.get("introduction_html"));//详情
					
		def manage_info = getCurrentUserInfo(req,"update");
		addCourse.append("manage_info",manage_info);
		courseDB().update(new BasicDBObject("_id":_id),new BasicDBObject('$set':addCourse));			
		return OK();
	}	
	
	//
	def del(HttpServletRequest req){		
		String id = req[_id]
		courseDB().remove(new BasicDBObject(_id,id))
		return OK();
	}
	
	//	
	@TypeChecked(TypeCheckingMode.SKIP)
	def add(HttpServletRequest req){
		def container =req.getParameter("json");
		// 课程主键
		def addCourse = $$("_id":UUID.randomUUID().toString());      		
		// 课程编号
		addCourse.append("course_code", maintypeKGS.nextId().toString());
		// 一个name多个值转换
		JSONObject jobj = JSONUtil.jsonToMap(container);
		JSONArray chapter = jobj.getJSONArray("chapter");
		JSONArray course_teacher = jobj.getJSONArray("course_teacher");
		JSONArray course_price = jobj.getJSONArray("course_price");			 		
		List<Map>  chapterList = (List<Map>)JSONHelper.JsonToList(chapter);
		def  course_teacherList=JSONHelper.JsonToList(course_teacher);
		def  course_priceList=JSONHelper.JsonToList(course_price);				 
		if(StringUtils.isNotBlank(container)){		
			List chapterObjLists = new ArrayList();
			List course_teacherObjLists = new ArrayList();
			List course_priceObjLists = new ArrayList();		
			for(Map m: chapterList){		
				BasicDBObject chapterObjList = new BasicDBObject();
				String chapter_name =  m.get("chapter_name");
				String chapter_introduction =  m.get("chapter_introduction");				
				String parameters =  m.get("parameters");
				 //1.1章节包含课件
				List<Map> products = (List<Map>)m.get("products");
				if(products){
					for(Map ps : products){
						Boolean flg = ps.get("is_free").equals("true")?true:false;
						ps.put("is_free",flg);
						ps.put("sepNum", maintypeKGS.nextId());
					}
				}	
				 chapterObjList.put("chapter_courseware",products);	//章节包含的课件					 
				 chapterObjList.put("_id",UUID.randomUUID().toString());
				 chapterObjList.put("chapter_code",maintypeKGS.nextId());
				 chapterObjList.put("chapter_name", chapter_name);
				 chapterObjList.put("chapter_introduction", chapter_introduction);				 
				 chapterObjList.put("parameters", parameters as Integer);				 
				 chapterObjLists.add(chapterObjList);
			 }
			//2.课程对应老师
			for(int i=0;i<course_teacherList.size();i++){	
				BasicDBObject course_teacherObjList = new BasicDBObject();
				Map m = course_teacherList.get(i);
				def answer_name =  m.get("answer_name");
				def teach_id =  m.get("teach_id");
				course_teacherObjList.put("answer_id",UUID.randomUUID().toString());
				course_teacherObjList.put("answer_name", answer_name);
				course_teacherObjList.put("teach_id", teach_id as Integer);
				course_teacherObjLists.add(course_teacherObjList);
			}
			//3.课程对应价格
			for(int i=0;i<course_priceList.size();i++){	
				BasicDBObject course_priceObjList = new BasicDBObject();
				Map m = course_priceList.get(i);
				def price =  m.get("price");
				def item_type =  m.get("item_type");
				course_priceObjList.put("price_id", UUID.randomUUID().toString());
				course_priceObjList.put("price", price as Double);
				course_priceObjList.put("item_type", item_type as Integer);
				course_priceObjList.put("order",maintypeKGS.nextId())
				course_priceObjLists.add(course_priceObjList);
			}
			addCourse.append("chapter", chapterObjLists);
			addCourse.append("course_teacher", course_teacherObjLists);
			addCourse.append("course_price", course_priceObjLists);
		}
			addCourse.append("course_name", req.getParameter("course_name"));//课名称
			addCourse.append("class_hour", req.getParameter("class_hour") as Integer);//课时
			addCourse.append("main_type_id", req.getParameter("main_type_id"));//频道档案
			
			addCourse.append("course_url", req.getParameter("course_url"));//图片
			addCourse.append("is_vip", req.getParameter("is_vip").equals("true")?true:false);//是否vip
			addCourse.append("courseware_id", req.getParameter("courseware_id"));//课件
			addCourse.append("company_id", req.getParameter("company_id"));//公司
			addCourse.append("indusetry_id", req.getParameter("indusetry_id") as Integer);//行业
			addCourse.append("professional_id", req.getParameter("professional_id"));//专业
			addCourse.append("rooms_id", req.getParameter("rooms_id"));//直播
			addCourse.append("teacher_id", req.getParameter("teacher_id") as Integer);//老师
			addCourse.append("introduction", req.getParameter("introduction") );//简介			
			addCourse.append("introduction_html", jobj.get("introduction_html"));//详情
			
		
		def manage_info = getCurrentUserInfo(req,"add");
		addCourse.append("manage_info",manage_info);
		courseDB().save(addCourse);
		return OK();
	}

	
	//获得users的公司绑定信息
	def users_company(HttpServletRequest req){
		Map user = req.getSession().getAttribute("user") as Map;
		def company_id = user.get("company_id");	
		def company_name =  company().findOne(new BasicDBObject("_id":company_id),new BasicDBObject("company_name",1))?.get("company_name")
		return ["code" : 1, data:["company_name" : company_name,"company_id":company_id]];
	}
	
	//获得users的老师的绑定信息
	def users_priv(HttpServletRequest req){
		List<DBObject> list = usersDB().find(new BasicDBObject("priv":6), $$("_id":1,"priv":1,"nick_name":1)).toArray();
		return ["code":1,"data":list];
	}
	
	//获得users的答疑老师的绑定信息
	def users_priv2(HttpServletRequest req){
		List<DBObject> list = usersDB().find(new BasicDBObject("priv":2), $$("_id":1,"priv":1,"nick_name":1)).toArray();
		return ["code":1,"data":list];
	}
	
	//所以不分页课件
	def chapter_courseware(HttpServletRequest req){
		def query = QueryBuilder.start()
		Map user = req.getSession().getAttribute("user") as Map;
		def company_id = user.get("company_id");
		query.and("company_id").is(company_id);
		query.and("manage_info.audit_flag").is(true);
		query.and("states").is(2);	
		List<DBObject> list = coursewareDB().find(query.get()).toArray();	
		return ["code":1,"data":list];
	}
	
	//获取直播间
	def rooms1(HttpServletRequest req){
		List<DBObject> list = roomsDB().find(null,$$("_id":1,"room_name":1)).toArray();
		return ["code":1,"data":list];
	}
	
	//所有分页课件
	def courseware(HttpServletRequest req){
		
		String search=req.getParameter("search");	
		def query = QueryBuilder.start()
		if (StringUtils.isNotBlank(search)) {
			Pattern pattern = Pattern.compile("^.*" + search + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("courseware_name").regex(pattern)			
		}
		
		Map user = req.getSession().getAttribute("user") as Map;
		def company_id = user.get("company_id");
		query.and("company_id").is(company_id);
		query.and("manage_info.audit_flag").is(true);
		query.and("states").is(2);
		def xx = query.get();
		Crud.list(req,coursewareDB(),query.get(),ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			int j=0;
			for(BasicDBObject obj: data){
				int i=j++;
				obj.put("company_name", company().findOne(new BasicDBObject("_id":obj['company_id']),new BasicDBObject("company_name",1))?.get("company_name"))	
				obj.put("teacher_name", usersDB().findOne(new BasicDBObject("_id":obj['teacher_id']),new BasicDBObject("nick_name",1))?.get("nick_name"))
				obj.put("is_free", "<input type="+'radio'+" name="+'is_free'+i+" value="+'true'+" checked="+'checked'+"/>免费<br><input type="+'radio'+" name="+'is_free'+i+" value="+'false'+" /> 收费")
			}	
		}	
	}
		
	//专业
	def professional(HttpServletRequest req){
		List<DBObject> list = professionalDB().find().toArray();
		return ["code":1,"data":list];
	}
	//频道档案
	def maintype(HttpServletRequest req){
		List<DBObject> list = maintypeDB().find().toArray();
		return ["code":1,"data":list];
	}
	
	
	
	def getCurrentUserInfo(HttpServletRequest req,String flg){
		if(flg.equals("update")){
			Map user = (Map) req.getSession().getAttribute("user");
			Map manage_info = new HashMap();
			Long now = System.currentTimeMillis();
			//创建人id
			//manage_info.put("create_user_id",user.get("_id") as Integer);
			//创建日期
			//manage_info.put("timestamp",now);
			//修改人Id
			manage_info.put("update_user_id",user.get("_id") as Integer);
			//修改日期
			manage_info.put("update_date",now);
			//提交标记
			manage_info.put("upload_flag" , false);
			//审核标记
			manage_info.put("audit_flag" , false);
			return manage_info;
		}
		if(flg.equals("add")){
			Map user = (Map) req.getSession().getAttribute("user");
			Map manage_info = new HashMap();
			Long now = System.currentTimeMillis();
			//创建人id
			manage_info.put("create_user_id",user.get("_id") as Integer);
			//创建日期
			manage_info.put("timestamp",now);
			//修改人Id
			manage_info.put("update_user_id",user.get("_id") as Integer);
			//修改日期
			manage_info.put("update_date",now);
			//提交标记
			manage_info.put("upload_flag" , false);
			//审核标记
			manage_info.put("audit_flag" , false);
			return manage_info;
		}		
	}
	
	def property = "course"; //定义表名
		
	def submit(HttpServletRequest request){
		submit1(request);
	}

	def rollbackSubmit(HttpServletRequest request){
		rollbackSubmit1(request)
	}

	def audit(HttpServletRequest request){
		audit1(request);
	}

	def rollbackAudit(HttpServletRequest request){
		rollbackAudit1(request);
	}
}
