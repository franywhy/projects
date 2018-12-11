package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import net.sf.json.JSONObject

import org.apache.commons.lang.StringUtils

import com.izhubo.model.OpType
import com.izhubo.model.UserType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.common.util.JSONUtil
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
/**
 * 
 * @Description: 排课计划管理
 * @author zbm  
 * @date 2015年1月22日 下午4:30:15
 */
@RestWithSession
class CoursePlanController extends BaseController {
	@Resource
	KGS questionnaireTypeKGS;
	DBCollection table(){mainMongo.getCollection('courseplan')}
		
	DBCollection _admins(){ adminMongo.getCollection('admins')}
	DBCollection _users(){	mainMongo.getCollection('users')}

	DBCollection _course(){ mainMongo.getCollection('course')}
	DBCollection _rooms(){ mainMongo.getCollection('rooms')}
	
	DBCollection _pay_order(){mainMongo.getCollection('pay_order')}
	//排课计划显示
	@TypeChecked(TypeCheckingMode.SKIP)
	def list(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String company_id = user.get("company_id");
		def query = QueryBuilder.start()
		//获取公司id进行数据权限处理
		query.and("company_id").is(company_id);
		def plan_code = req.getParameter("plan_code");
		if (StringUtils.isNotBlank(plan_code)) {
			Pattern pattern = Pattern.compile("^.*" + plan_code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("plan_code").regex(pattern)
		}
        stringQuery(query,req,"course_id");
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			//id-->name
			for(BasicDBObject obj: data){
				Integer user_id = obj['manage_info']['create_user_id'] as Integer;
				Integer update_id = obj['manage_info']['update_user_id'] as Integer;
				String course_id = obj['course_id'];
				Integer class_room_id = obj['class_room_id'] as Integer;
				if(user_id){
					obj.put("user_name", _admins().findOne(new BasicDBObject("_id" : user_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
				if(update_id){
					obj.put("update_name", _admins().findOne(new BasicDBObject("_id" : update_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
				if(course_id){
					obj.put("course_name", _course().findOne(new BasicDBObject("_id" : course_id) , new BasicDBObject("course_name":1))?.get("course_name"));
				}
				if(class_room_id){
					obj.put("room_name", _rooms().findOne(new BasicDBObject("_id" : class_room_id as Integer) , new BasicDBObject("room_name":1))?.get("room_name"));
				}
				for (BasicDBObject obj1: obj['course_plan_user']){	
					Integer course_user_id = obj1['user_id']as Integer;
					if (course_user_id){
						obj1.put("course_user_name",_users().findOne(new BasicDBObject("_id" : course_user_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
					}
			    }
			}
			
		}
	}
	
	
	//教师下拉框列表
	def teacher_list(HttpServletRequest req){
		Map user = req.getSession().getAttribute("user") as Map;
		if(user){ //暂时不区分公司权限 "company_id" : user.get("company_id").toString()
			List<DBObject> list = _users().find($$("priv" : UserType.主播.ordinal()),
				$$("_id" : 1 , "nick_name" : 1)).sort($$(_id : 1)).limit(1000).toArray();
			return ["code" : 1, "data" : list];
		}
		return null;

	}
	
		//学员下拉框列表
		@TypeChecked(TypeCheckingMode.SKIP)
		def user_list(HttpServletRequest req){   //TODO 修改UserType类结构
			Map user = req.getSession().getAttribute("user") as Map;
			String course_id=req.getParameter("course_id");
			List<DBObject> list = _pay_order().find($$("item_id" : course_id),$$("user_id" : 1)).toArray();
			List<Integer> list_new=new ArrayList<Integer>();
			for(DBObject dbo :list){
				List<DBObject> list2 = _course().find($$("course_plan_user.user_id" : dbo['user_id'])).toArray();
				if(!list2&&!list_new.contains(dbo['user_id'])){
					list_new.add(dbo['user_id']);
				}
			}
			String search=req.getParameter("search");
			def query = QueryBuilder.start()
				query.and("priv").is(UserType.普通用户.ordinal());
				query.and("_id").in(list_new);
			if (StringUtils.isNotBlank(search)) {
				//启用不区分大小写的匹配。
				Pattern	 pattern = Pattern.compile("^.*" + search + ".*\$", Pattern.CASE_INSENSITIVE)	
				query.and("nick_name").regex(pattern)
			}
			Crud.list(req,_users(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC)
		}
	
	//课程下拉框列表
	def course_list(HttpServletRequest req){
		Map user = req.getSession().getAttribute("user") as Map;
		if(user){
//			List<DBObject> list = _course().find($$("company_id" : user.get("company_id").toString()),$$("_id" : 1 , "course_name" : 1))
//			.sort($$(_id : 1)).limit(1000).toArray();//暂时不区分公司权限
			List<DBObject> list = _course().find(null,$$("_id" : 1 , "course_name" : 1)).sort($$(_id : 1)).limit(1000).toArray();
			return ["code" : 1, "data" : list];
		}
		return null;
	}
	
	//直播间下拉框列表
	def room_list(HttpServletRequest req){
		Map user = req.getSession().getAttribute("user") as Map;
		if(user){
//			List<DBObject> list = _rooms().find($$("company_id" : user.get("company_id").toString()),$$("_id" : 1 , "room_name" : 1))
//			.sort($$(_id : 1)).limit(1000).toArray();  //暂时不区分公司权限
			List<DBObject> list = _rooms().find(null,$$("_id":1,"room_name":1)).sort($$(_id : 1)).limit(1000).toArray();
			return ["code" : 1, "data" : list];
		}
		return null;
	}
	
	//章节下拉框列表
	def chapter_list(HttpServletRequest req){
		Map user = req.getSession().getAttribute("user") as Map;
		String course_id=req.getParameter("course_id");
		if(user){
//			List<DBObject> list = _rooms().find($$("company_id" : user.get("company_id").toString()),$$("_id" : 1 , "room_name" : 1))
//			.sort($$(_id : 1)).limit(1000).toArray();  //暂时不区分公司权限
			List<DBObject> list = _course().find($$("_id":course_id),$$("chapter":1)).sort($$(_id : 1)).limit(1000).toArray();
			return ["code" : 1, "data" : list];
		}
		return null;
	}
	
	@TypeChecked(TypeCheckingMode.SKIP)
	def add(HttpServletRequest request)
	{
		Map user = (Map) request.getSession().getAttribute("user");
		String company_id = user.get("company_id");
		//TODO
		def courseplan=$$("_id" : UUID.randomUUID().toString(), 'company_id' : company_id);
		def container =request.getParameter("json");
		//班级名称
		courseplan.append("class_name", request.getParameter("class_name"));
		//班主任ID
		courseplan.append("class_teach_id", request.getParameter("class_teach_id") as Integer);
		
		//计划编号
		courseplan.append("plan_code", questionnaireTypeKGS.nextId().toString());
		// 课程编号
		courseplan.append("course_id", request.getParameter("course_id"));
		// 直播间Id
		courseplan.append("class_room_id", request.getParameter("class_room_id") as Integer);
		JSONObject jobj = JSONUtil.jsonToMap(container);
		
		def cidlist=jobj.get("cidlist");//章节
		def roomlist=jobj.get("roomlist");//小课直播间
		def teachidlist=jobj.get("teachidlist");// 讲课老师
		def sdatelist=jobj.get("sdatelist");// 上课时间
		def clist=jobj.get("clist");//上课说明
		def beginlist=jobj.get("beginlist");//上课开始日期
		def endlist=jobj.get("endlist");//上课结束日期
		def userlist=jobj.get("userlist");//班组成员
		List courseplan_items =new ArrayList();
		for (int i=0;i<clist.size();i++){
			//BasicDBObject courseplan_item=new BasicDBObject();
			def courseplan_item=$$("_id" : UUID.randomUUID().toString());
			def chapter_id=cidlist.get(i);
			def room_id=roomlist.get(i);
			def teacher_id=teachidlist.get(i);
			def sdate=sdatelist.get(i);
			def coursetitle=clist.get(i);
			def begin_time=beginlist.get(i);
			def end_time=endlist.get(i);
			
		   courseplan_item.put("chapter_id" , chapter_id);
		   courseplan_item.put("room_id" , room_id as Integer);
		   courseplan_item.put("teacher_id" , teacher_id as Integer);
		   courseplan_item.put("sdate" , sdate);
		   courseplan_item.put("coursetitle" , coursetitle);
		   courseplan_item.put("begin_time" , begin_time);
		   courseplan_item.put("end_time" , end_time);
		   courseplan_items.add(courseplan_item);
		}
		courseplan.append("courseplan_item", courseplan_items);
		//获取userid的结果集合
		List course_plan_users =new ArrayList();
		for (int k=0;k<userlist.size();k++){
			BasicDBObject course_plan_user=new BasicDBObject();
			def user_id=userlist.get(k);
			course_plan_user.put("user_id" , user_id as Integer);
			course_plan_users.add(course_plan_user);
		}
		courseplan.append("course_plan_user", course_plan_users);
		
		//管理信息
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
		courseplan.append("manage_info", manage_info);
		table().save(courseplan);
		Crud.opLog(OpType.courseplan,[save:request["_id"]]);
		return OK();
	}
	
	@TypeChecked(TypeCheckingMode.SKIP)
	def edit(HttpServletRequest request)
	{
		Map user = (Map) request.getSession().getAttribute("user");
		BasicDBObject courseplan=new BasicDBObject();
		
		//班级名称
		courseplan.append("class_name", request.getParameter("class_name"));
		//班主任ID
		courseplan.append("class_teach_id", request.getParameter("class_teach_id") as Integer);
		//课程编号
		courseplan.append("course_id", request.getParameter("course_id"));
		// 直播间Id
		courseplan.append("class_room_id", request.getParameter("class_room_id") as Integer);
		def container =request.getParameter("json");
		JSONObject jobj = JSONUtil.jsonToMap(container);
		
	    def cidlist=jobj.get("cidlist");//章节
		def roomlist=jobj.get("roomlist");//小课直播间
		def teachidlist=jobj.get("teachidlist");// 讲课老师
		def sdatelist=jobj.get("sdatelist");// 上课时间
		def clist=jobj.get("clist");//上课说明
		def beginlist=jobj.get("beginlist");//上课开始日期
		def endlist=jobj.get("endlist");//上课结束日期
		def userlist=jobj.get("userlist");//班组成员
		
		List courseplan_items =new ArrayList();
		for (int i=0;i<clist.size();i++){
			//BasicDBObject courseplan_item=new BasicDBObject();
			def courseplan_item=$$("_id" : UUID.randomUUID().toString());
			def chapter_id=cidlist.get(i);
			def room_id=roomlist.get(i);
			def teacher_id=teachidlist.get(i);
			def sdate=sdatelist.get(i);
			def coursetitle=clist.get(i);
			def begin_time=beginlist.get(i);
			def end_time=endlist.get(i);
			
		   courseplan_item.put("chapter_id" , chapter_id);
		   courseplan_item.put("room_id" , room_id as Integer);
		   courseplan_item.put("teacher_id" , teacher_id as Integer);
		   courseplan_item.put("sdate" , sdate);
		   courseplan_item.put("coursetitle" , coursetitle);
		   courseplan_item.put("begin_time" , begin_time);
		   courseplan_item.put("end_time" , end_time);
		   courseplan_items.add(courseplan_item);
		}
		courseplan.append("courseplan_item", courseplan_items);
		
		//获取userid的结果集合
		List course_plan_users =new ArrayList();
		for (int k=0;k<userlist.size();k++){
			BasicDBObject course_plan_user=new BasicDBObject();
			def user_id=userlist.get(k);
			course_plan_user.put("user_id" , user_id as Integer);
			course_plan_users.add(course_plan_user);
		}
		courseplan.append("course_plan_user", course_plan_users);
		
		//管理信息
		Long now = System.currentTimeMillis();
		//修改人Id
		courseplan.put("manage_info.update_user_id",user.get("_id") as Integer);
		//修改日期
		courseplan.put("manage_info.update_date",now);
		String _id = request.getParameter("_id");
		table().update(new BasicDBObject("_id":request.getParameter("_id")),new BasicDBObject('$set':courseplan));
		Crud.opLog(OpType.courseplan,[update:request["_id"]]);
		return OK();
	}

	def del(HttpServletRequest req){
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().remove(new BasicDBObject(_id,id))
		Crud.opLog(OpType.courseplan,[del:id]);
		return OK();
	}
	
	/**
	 * 提交
	 */	
	@TypeChecked(TypeCheckingMode.SKIP)
	def submit(HttpServletRequest request){
		Map user = (Map) request.getSession().getAttribute("user");
		if(user == null){
			return OK();
		}
	   table().update(
		   new BasicDBObject("_id":request.getParameter("_id") , "manage_info.upload_flag" : false),
		   new BasicDBObject('$set':
			   new BasicDBObject(
				   "manage_info.upload_flag" : true,
				   //提交人
				   "manage_info.upload_user_id": user.get("_id") as Integer,
				   "manage_info.upload_date" : System.currentTimeMillis()
				   )
			   ));
	   return OK();
	}
	/**
	 * 收回
	 */
	def recovery(HttpServletRequest request){
		Map user = (Map) request.getSession().getAttribute("user");
		if(user == null){
			return OK();
		}
		table().update(
			new BasicDBObject("_id":request.getParameter("_id") , "manage_info.upload_flag" : true),
			new BasicDBObject('$set':new BasicDBObject("manage_info.upload_flag" : false,)
				));
		return OK();
	}
	/**
	 * 审核
	 */
	def audit(HttpServletRequest request){
		Map user = (Map) request.getSession().getAttribute("user");
		if(user == null){
			return OK();
		}
		table().update(
			new BasicDBObject("_id":request.getParameter("_id") , "manage_info.audit_flag" : false),
			new BasicDBObject('$set':
				new BasicDBObject(
					"manage_info.audit_flag" : true,
					//提交人
					"manage_info.audit_user_id": user.get("_id") as Integer,
					"manage_info.audit_date" : System.currentTimeMillis()
					)
				));
		return OK();
	}
	
	/**
	 * 反审核
	 */
	def reaudit(HttpServletRequest request){
		Map user = (Map) request.getSession().getAttribute("user");
		if(user == null){
			return OK();
		}
		def _id =request.getParameter("_id");
		//TODO
//		def company =  table().findOne(new BasicDBObject("_id":_id),MongoKey.ALL_FIELD);
//		def company_id=company.get("company_id");
//		def adminList =_admins().find($$("company_id" :_id),$$("_id" : 1 , "company_id":1)).toArray();
//		def courseList =_course().find($$("company_id" :_id),$$("_id" : 1 , "company_id":1)).toArray();
//		if(adminList||courseList){
//			return [code : 1 , data :["info":1]];
//		}
		table().update(
			new BasicDBObject("_id":_id, "manage_info.audit_flag" : true),
			new BasicDBObject('$set':
				new BasicDBObject(
					"manage_info.audit_flag" : false,
					)
				));
		return OK();
	}
}
