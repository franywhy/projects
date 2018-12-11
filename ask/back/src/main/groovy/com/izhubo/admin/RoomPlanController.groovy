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
import com.izhubo.rest.common.util.*
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder

/**
 * 
 * @Description: 直播间排课计划管理
 * @author zbm  
 * @date 2015年4月24日 上午10:25:27
 */

@RestWithSession
class RoomPlanController extends BaseController{
	@Resource
	KGS questionnaireTypeKGS;
	DBCollection table(){mainMongo.getCollection('room_plan')}
	
	DBCollection _admins(){
		return adminMongo.getCollection('admins');
	}
	
	DBCollection _users(){ mainMongo.getCollection('users')}
	DBCollection _rooms(){ mainMongo.getCollection('rooms')}
	DBCollection _course(){ mainMongo.getCollection('course')}
	
	//直播间排课计划列表显示
	def list(HttpServletRequest req){
		def query = QueryBuilder.start()
		def plan_code = req.getParameter("plan_code");
		if (StringUtils.isNotBlank(plan_code)) {
			Pattern pattern = Pattern.compile("^.*" + plan_code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("plan_code").regex(pattern)
		}
		 intQuery(query,req,'teach_room_id') 
		 intQuery(query,req,'room_type')
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			//id-->name
			for(BasicDBObject obj: data){
				Integer user_id = obj['manage_info']['create_user_id'] as Integer;
				Integer update_id = obj['manage_info']['update_user_id'] as Integer;
				Integer teach_room_id = obj['teach_room_id'] as Integer;
				Integer teach_id = obj['teach_id'] as Integer;
				if(user_id){
					obj.put("user_name", _admins().findOne(new BasicDBObject("_id" : user_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
				if(update_id){
					obj.put("update_name", _admins().findOne(new BasicDBObject("_id" : update_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
				if(teach_room_id){
					obj.put("teach_room_name", _rooms().findOne(new BasicDBObject("_id" : teach_room_id as Integer) , new BasicDBObject("room_name":1))?.get("room_name"));
				}
				if(teach_id){
					obj.put("teach_name", _users().findOne(new BasicDBObject("_id" : teach_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
			}
			
		}
	}
	

	
	@TypeChecked(TypeCheckingMode.SKIP)
	def add(HttpServletRequest request)
	{
		Map user = (Map) request.getSession().getAttribute("user");
		def container =request.getParameter("json");
		//TODO
		def room_plan=$$("_id" : UUID.randomUUID().toString());
		// 直播间计划编号
		room_plan.append("plan_code", questionnaireTypeKGS.nextId().toString());
		// 老师直播间ID
		room_plan.append("teach_room_id", request.getParameter("teach_room_id") as Integer);
		// 老师ID
		room_plan.append("teach_id", request.getParameter("teach_id") as Integer);
		// 直播间类型
		room_plan.append("room_type", request.getParameter("room_type") as Integer);
		// 直播间说明
		room_plan.append("room_memo", request.getParameter("room_memo"));
		JSONObject jobj = JSONUtil.jsonToMap(container);
		def clist=jobj.get("clist");
		def beginlist=jobj.get("beginlist");
		def endlist=jobj.get("endlist");
		def slist=jobj.get("slist");
		BasicDBList room_plan_items =new ArrayList();
		for (int i=0;i<clist.size();i++){
			BasicDBObject room_plan_item=new BasicDBObject();
			def course_title=clist.get(i);
			def begin_time=beginlist.get(i);
			def end_time=endlist.get(i);
			def sdate=slist.get(i);
		   room_plan_item.put("course_title" , course_title);
		   room_plan_item.put("begin_time" , begin_time);
		   room_plan_item.put("end_time" , end_time);
		   room_plan_item.put("sdate" , sdate);
		   room_plan_items.add(room_plan_item);
		}
		room_plan.append("room_plan_item", room_plan_items);
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
		room_plan.append("manage_info", manage_info);
		table().save(room_plan);
		Crud.opLog("room_plan",[save:request["_id"]]);
		return OK();
	}
	
	@TypeChecked(TypeCheckingMode.SKIP)
	def edit(HttpServletRequest request)
	{
		Map user = (Map) request.getSession().getAttribute("user");
		BasicDBObject room_plan=new BasicDBObject();
	    // 老师直播间ID
		room_plan.append("teach_room_id", request.getParameter("teach_room_id") as Integer);
		// 老师ID
		room_plan.append("teach_id", request.getParameter("teach_id") as Integer);
		// 直播间类型
		room_plan.append("room_type", request.getParameter("room_type") as Integer);
		// 直播间说明
		room_plan.append("room_memo", request.getParameter("room_memo"));
		//项目标题内容
		def container =request.getParameter("json");
		JSONObject jobj = JSONUtil.jsonToMap(container);
		def clist=jobj.get("clist");
		def beginlist=jobj.get("beginlist");
		def endlist=jobj.get("endlist");
		def slist=jobj.get("slist");
		BasicDBList room_plan_items =new ArrayList();
		for (int i=0;i<clist.size();i++){
			BasicDBObject room_plan_item=new BasicDBObject();
			def course_title=clist.get(i);
			def begin_time=beginlist.get(i);
			def end_time=endlist.get(i);
			def sdate=slist.get(i);
		   room_plan_item.put("course_title" , course_title);
		   room_plan_item.put("begin_time" , begin_time);
		   room_plan_item.put("end_time" , end_time);
		   room_plan_item.put("sdate" , sdate);
		   room_plan_items.add(room_plan_item);
		}
		room_plan.append("room_plan_item", room_plan_items);
		//管理信息
		Long now = System.currentTimeMillis();
		//修改人Id
		room_plan.put("manage_info.update_user_id",user.get("_id") as Integer);
		//修改日期
		room_plan.put("manage_info.update_date",now);
		String _id = request.getParameter("_id");
		table().update(new BasicDBObject("_id":request.getParameter("_id")),new BasicDBObject('$set':room_plan));
		Crud.opLog("room_plan",[update:request["_id"]]);
		return OK();
	}

	def del(HttpServletRequest req){
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().remove(new BasicDBObject(_id,id))
		Crud.opLog("room_plan",[del:id]);
		return OK();
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
	
	//直播间下拉框列表
	def room_list(HttpServletRequest req){
		Map user = req.getSession().getAttribute("user") as Map;
		if(user){
			List<DBObject> list = _rooms().find(null,$$("_id":1,"room_name":1)).sort($$(_id : 1)).limit(1000).toArray();
			return ["code" : 1, "data" : list];
		}
		return null;
	}
	
	def property = "room_plan"; //定义表名
	/**
	 * 提交
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def submit(HttpServletRequest request){
		submit1(request);
	}
	/**
	 * 收回
	 */
	def recovery(HttpServletRequest request){
		rollbackSubmit1(request);
	}
	/**
	 * 审核
	 */
	def audit(HttpServletRequest request){
		audit1(request);
	}
	
	/**
	 * 反审核
	 */
	def reaudit(HttpServletRequest request){
		rollbackAudit1(request);
	}
 }
