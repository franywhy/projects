package com.izhubo.admin


import static com.izhubo.rest.common.util.WebUtils.$$

import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils

import com.izhubo.model.UserType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder

/**
 * @Description:直播间档案管理
 * @author zbm  
 * @date 2015年1月26日 上午9:31:44
 */
@RestWithSession
class RoomsController  extends BaseController{
	
	@Resource
	KGS roomKGS;
	DBCollection _rooms(){mainMongo.getCollection('rooms')}
	DBCollection _professional(){mainMongo.getCollection('professional')}
	DBCollection _users(){	mainMongo.getCollection('users')}
	//直播间列表
	def list(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String company_id = user.get("company_id");
		def query = QueryBuilder.start()
		//获取公司id进行数据权限处理
		query.and("company_id").is(company_id);
		def _id = req.getParameter("_id");
		if (StringUtils.isNotBlank(_id)) {
			Pattern pattern = Pattern.compile("^.*" + _id + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("_id").regex(pattern)
		}
		  def room_name = req.getParameter("room_name");
		  if (StringUtils.isNotBlank(room_name)) {
			  Pattern pattern = Pattern.compile("^.*" + room_name + ".*\$", Pattern.CASE_INSENSITIVE)
			  query.and("room_name").regex(pattern)
		  }
		intQuery(query,req,'room_type')
		Crud.list(req,_rooms(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			//id-->name
			for(BasicDBObject obj: data){
				String professional_id = obj['professional_id'];
				if(professional_id){
					obj.put("professional_name", _professional().findOne(new BasicDBObject("_id" : professional_id) , new BasicDBObject("professional_name":1))?.get("professional_name"));
				}
			}
			
		}
	}
		
	//专业信息下拉列表
	def professional_list(HttpServletRequest req){   //
	   Map user = req.getSession().getAttribute("user") as Map;
	   if(user){
		   List<DBObject> list = _professional().find(null,$$("_id" : 1 , "professional_name" : 1)).sort($$(_id : 1))
		   .limit(1000).toArray();
		   return ["code" : 1, "data" : list];
	   }
	   return null;
   }
	
	//教师下拉框列表
	def teacher_list(HttpServletRequest req){
		Map user = req.getSession().getAttribute("user") as Map;
		String userid=req.getParameter("teacher_id");
		if(StringUtils.isNotBlank(userid)){
			DBObject teacher = _users().findOne($$("priv" : UserType.主播.ordinal(),"_id":userid as Integer),null);
			return ["code" : 1, "data" : teacher];
		}else{
		if(user){ //暂时不区分公司权限 "company_id" : user.get("company_id").toString()
			List<DBObject> list = _users().find($$("priv" : UserType.主播.ordinal()),
				$$("_id" : 1 , "nick_name" : 1)).sort($$(_id : 1)).limit(1000).toArray();
			return ["code" : 1, "data" : list];
		  }
		}
		return null;

	}
		
	//新增直播间
	def add(HttpServletRequest request)
	{   
		Map user = (Map) request.getSession().getAttribute("user");
		String company_id = user.get("company_id");
		Long now = System.currentTimeMillis();
		Integer room_id = roomKGS.nextId();
		def rooms=$$("_id" : room_id,'company_id' : company_id);
		// 房间编号 room_ids
		rooms.append("room_ids", room_id.toString());	
		//主播封面审核状态  audit_pic_status 
		rooms.append("audit_pic_status",true);
		// 主播封面  audit_pic_url
		rooms.append("audit_pic_url", request.getParameter("audit_pic_url"));
		// 直播间生效日期  date
		rooms.append("begin_date", request.getParameter("begin_date") as long);
		// 直播间结束日期 end_date
		rooms.append("end_date", request.getParameter("end_date") as long);
		// 每天开播时间   found_time
		rooms.append("found_time", request.getParameter("found_time") as long);
		// 是否开播   live
		rooms.append("live", false);
		// 收费类型 cost_type
		rooms.append("cost_type", request.getParameter("cost_type") as Integer);
		// 每天直播结束时间  live_end_time
		rooms.append("live_end_time", request.getParameter("live_end_time") as long);
		//开播唯一id   live_id 暂时留空
		rooms.append("live_id", "");
		// 主播留言   message
		rooms.append("message", request.getParameter("message"));
		// 主播头像   pic_url
		rooms.append("pic_url", request.getParameter("pic_url"));
		// 是否使用插件 plugin
		rooms.append("plugin", request.getParameter("plugin"));
		// 大讲堂的价格  price
		rooms.append("price", request.getParameter("price") as Double);
		//直播间名称  room_name  
		rooms.append("room_name", request.getParameter("room_name"));
		// 直播间类型  room_type
		rooms.append("room_type", request.getParameter("room_type") as Integer);
		//直播时间说明 timedescription
		rooms.append("timedescription", request.getParameter("timedescription"));
		//创建时间    timestamp
		rooms.append("timestamp", now);
		// 最后修改时间  lastmodif
		rooms.append("lastmodif", now);		
		// 获取主播信息表数据
		Integer xy_star_id =request.getParameter("xy_star_id") as Integer
		def  nick_name; 
		def users =  _users().findOne(new BasicDBObject("_id":xy_star_id),MongoKey.ALL_FIELD);
		nick_name =  users.get("nick_name");
        //主播_ID   xy_star_id
		rooms.append("xy_star_id", xy_star_id);
		// 主播昵称  nick_name
		rooms.append("nick_name", nick_name);
		// 获取专业信息表数据
		String professional_id =request.getParameter("professional_id"); 
		def  main_type_id;
		if(!StringUtils.isBlank(professional_id)){
			def professional =  _professional().findOne(new BasicDBObject("_id":professional_id),MongoKey.ALL_FIELD);
			//频道信息
			 main_type_id =  professional.get("main_type_id");
		}
		// 专业编号   professional_id
		rooms.append("professional_id", request.getParameter("professional_id"));
		// 	频道PK   main_type_id
		rooms.append("main_type_id", main_type_id);
		
		_rooms().save(rooms);
		Crud.opLog("rooms",[save:request["_id"]]);
		return OK();
	}
    
	//编辑直播间
	def edit(HttpServletRequest request)
	{
		BasicDBObject rooms=new BasicDBObject();
		Long now = System.currentTimeMillis();
		// 主播封面  audit_pic_url
		rooms.append("audit_pic_url", request.getParameter("audit_pic_url"));
		// 直播间生效日期  date
		rooms.append("begin_date", request.getParameter("begin_date") as long);
		// 直播间结束日期 end_date
		rooms.append("end_date", request.getParameter("end_date") as long);
		// 每天开播时间   found_time
		rooms.append("found_time", request.getParameter("found_time") as long);
		// 是否开播   live
		rooms.append("live", false);
		// 收费类型 cost_type
		rooms.append("cost_type", request.getParameter("cost_type") as Integer);
		// 每天直播结束时间  live_end_time
		rooms.append("live_end_time", request.getParameter("live_end_time") as long);
		//开播唯一id   live_id 暂时留空
		rooms.append("live_id", request.getParameter("live_id"));
		// 主播留言   message
		rooms.append("message", request.getParameter("message"));
		// 主播头像   pic_url
		rooms.append("pic_url", request.getParameter("pic_url"));
		// 是否使用插件 plugin
		rooms.append("plugin", request.getParameter("plugin"));
		// 大讲堂的价格  price
		rooms.append("price", request.getParameter("price") as Double);
		//直播间名称  room_name  
		rooms.append("room_name", request.getParameter("room_name"));
		// 直播间类型  room_type
		rooms.append("room_type", request.getParameter("room_type") as Integer);
		//直播时间说明 timedescription
		rooms.append("timedescription", request.getParameter("timedescription"));
		// 最后修改时间  lastmodif
		rooms.append("lastmodif", now);		
		// 获取主播信息表数据
		Integer xy_star_id =request.getParameter("xy_star_id") as Integer
		def  nick_name; 
		def users =  _users().findOne(new BasicDBObject("_id":xy_star_id),MongoKey.ALL_FIELD);
		nick_name =  users.get("nick_name");
        //主播_ID   xy_star_id
		rooms.append("xy_star_id", xy_star_id);
		// 主播昵称  nick_name
		rooms.append("nick_name", nick_name);
		// 获取专业信息表数据
		String professional_id =request.getParameter("professional_id"); 
		def  main_type_id;
		if(!StringUtils.isBlank(professional_id)){
			def professional =  _professional().findOne(new BasicDBObject("_id":professional_id),MongoKey.ALL_FIELD);
			//频道信息
			 main_type_id =  professional.get("main_type_id");
		}
		// 专业编号   professional_id
		rooms.append("professional_id", request.getParameter("professional_id"));
		// 	频道PK   main_type_id
		rooms.append("main_type_id", main_type_id);
		//String _id = request.getParameter("_id");
		_rooms().update(new BasicDBObject("_id":request.getParameter("_id") as Integer),new BasicDBObject('$set':rooms));
		Crud.opLog("rooms",[update:request["_id"]]);
		return OK();
	}
    
	//删除
	def del(HttpServletRequest req){
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		_rooms().remove(new BasicDBObject(_id,id as Integer))
		Crud.opLog("rooms",[del:id]);
		return OK();
	}
}
