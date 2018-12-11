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
 * @Description: 首页展示档案
 * @author zbm  
 * @date 2015年1月26日 上午9:32:43
 */
@RestWithSession
class RoomInPageController extends BaseController{
	
	@Resource
	KGS baseKGS;
	DBCollection table(){mainMongo.getCollection('room_in_page')}
	DBCollection professional(){mainMongo.getCollection('professional')}
	DBCollection _course(){ mainMongo.getCollection('course')}
	DBCollection _rooms(){ mainMongo.getCollection('rooms')}
	

	//答疑类型列表显示
	def list(HttpServletRequest req){
		def query = QueryBuilder.start()
		def code = req.getParameter("code");
		if (StringUtils.isNotBlank(code)) {
			Pattern pattern = Pattern.compile("^.*" + code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("code").regex(pattern)
		}
		  def professional_name = req.getParameter("professional_name");
		  if (StringUtils.isNotBlank(professional_name)) {
			  Pattern pattern = Pattern.compile("^.*" + professional_name + ".*\$", Pattern.CASE_INSENSITIVE)
			  query.and("professional_name").regex(pattern)
		  }
		 intQuery(query,req,'type')
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC)
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			//id-->name
			for(BasicDBObject obj: data){
				Integer value = obj['type'] as Integer;
				if(value==0){
					obj.put("room_name", _rooms().findOne(new BasicDBObject("_id" : obj['code'] as Integer) , new BasicDBObject("room_name":1))?.get("room_name"));
					obj.put("course_name","");
				}else{
				    obj.put("room_name","");
				    obj.put("course_name", _course().findOne(new BasicDBObject("_id" : obj['code'] as String) , new BasicDBObject("course_name":1))?.get("course_name"));
				 }
			}
			
		}
	}
	
	
	 //专业信息下拉列表	
	 def professional_list(HttpServletRequest req){   //
		Map user = req.getSession().getAttribute("user") as Map;
		if(user){
			List<DBObject> list = professional().find(null,$$("_id" : 1 , "professional_name" : 1)).sort($$(_id : 1))
			.limit(1000).toArray();
			return ["code" : 1, "data" : list];
		}
		return null;
	}
    
	//课程下拉框列表
	def course_list(HttpServletRequest req){
		Map user = req.getSession().getAttribute("user") as Map;
		Integer indusetry_id=req.getParameter("type") as Integer;
		if(user){  //暂时不区分公司权限
			List<DBObject> list = _course().find($$("indusetry_id" : indusetry_id),$$("_id" : 1 , "course_name" : 1))
			.sort($$(_id : 1)).limit(1000).toArray();
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
	
	def add(HttpServletRequest request)
	{
		def room_in_page=$$("_id" : UUID.randomUUID().toString());
		// 名称类型
		
		def type=request.getParameter("type") as Integer;
		room_in_page.append("type", type);
		
		//顺序号
		room_in_page.append("order", request.getParameter("order") as Integer);
		
		// 课程或直播间编号
		def code;
		String professional_id;
		if (type==0){
			code=request.getParameter("room_id") as Integer;
	       professional_id = _rooms().findOne(new BasicDBObject("_id":code),new BasicDBObject("professional_id":1))?.get("professional_id");
		} else{
		   String value ="course_id"+type;
		   code=request.getParameter(value);
		   professional_id = _course().findOne(new BasicDBObject("_id":code),new BasicDBObject("professional_id":1))?.get("professional_id");
		}
		     
		room_in_page.append("code", code.toString());
		//通过 professional_id 来查询出专业档案的信息
		def  main_type_id;
		def  main_type_code;
		def  main_type_name;
		def  professional_code;
		def  professional_name;
		if(!StringUtils.isBlank(professional_id)){
			def professional =  professional().findOne(new BasicDBObject("_id":professional_id),MongoKey.ALL_FIELD);
			//频道信息
			 main_type_id =  professional.get("main_type_id");
			 main_type_code =  professional.get("main_type_code");
			 main_type_name =  professional.get("main_type_name");
			//专业信息
			//def professional_id =  professional.get("professional_id");
			 professional_code =  professional.get("professional_code");
			 professional_name=  professional.get("professional_name");
		}

		room_in_page.append("main_type_id", main_type_id);
		room_in_page.append("main_type_code", main_type_code);
		room_in_page.append("main_type_name", main_type_name);
		room_in_page.append("professional_id", professional_id);
		room_in_page.append("professional_code", professional_code);
		room_in_page.append("professional_name", professional_name);

		table().save(room_in_page);
		Crud.opLog("room_in_page",[save:request["_id"]]);
		return OK();
	}

	def edit(HttpServletRequest request)
	{
		BasicDBObject room_in_page=new BasicDBObject();
	    // 名称类型
		def type=request.getParameter("type") as Integer;
		room_in_page.append("type", type);
		
		//顺序号
		room_in_page.append("order", request.getParameter("order") as Integer);
		
		// 课程或直播间编号
		def code;
		String professional_id;
		if (type==0){
			code=request.getParameter("room_id") as Integer;
	       professional_id = _rooms().findOne(new BasicDBObject("_id":code),new BasicDBObject("professional_id":1))?.get("professional_id");
		} else{
		   code=request.getParameter("course_id");
		   professional_id = _course().findOne(new BasicDBObject("_id":code),new BasicDBObject("professional_id":1))?.get("professional_id");
		}
		room_in_page.append("code", code.toString());
		
		//通过 professional_id 来查询出专业档案的信息
		def  main_type_id;
		def  main_type_code;
		def  main_type_name;
		def  professional_code;
		def  professional_name;
		if(!StringUtils.isBlank(professional_id)){
			def professional =  professional().findOne(new BasicDBObject("_id":professional_id),MongoKey.ALL_FIELD);
			//频道信息
			 main_type_id =  professional.get("main_type_id");
			 main_type_code =  professional.get("main_type_code");
			 main_type_name =  professional.get("main_type_name");
			//专业信息
			//def professional_id =  professional.get("professional_id");
			 professional_code =  professional.get("professional_code");
			 professional_name=  professional.get("professional_name");
		}

		room_in_page.append("main_type_id", main_type_id);
		room_in_page.append("main_type_code", main_type_code);
		room_in_page.append("main_type_name", main_type_name);
		room_in_page.append("professional_id", professional_id);
		room_in_page.append("professional_code", professional_code);
		room_in_page.append("professional_name", professional_name);
		String _id = request.getParameter("_id");
		table().update(new BasicDBObject("_id":request.getParameter("_id")),new BasicDBObject('$set':room_in_page));
		Crud.opLog("room_in_page",[update:request["_id"]]);
		return OK();
	}

	def del(HttpServletRequest req){
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().remove(new BasicDBObject(_id,id))
		Crud.opLog("room_in_page",[del:id]);
		return OK();
	}
}
