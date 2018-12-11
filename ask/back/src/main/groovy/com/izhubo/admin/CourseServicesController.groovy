package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$

import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder


@RestWithSession
class CourseServicesController extends BaseController{
	
	@Resource
	KGS answerTypeKGS;
	DBCollection table(){mainMongo.getCollection('course_services')}
	DBCollection _course(){ mainMongo.getCollection('course')}

	//课程服务列表显示
	def list(HttpServletRequest req){
		def query = QueryBuilder.start()	
        stringQuery(query,req,'course_id');  //课程ID
		intQuery(query,req,'services_type')  //服务类型
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			//id-->name
			for(BasicDBObject obj: data){
				String course_id = obj['course_id'];
				if(course_id){
					obj.put("course_name", _course().findOne(new BasicDBObject("_id" : course_id) , new BasicDBObject("course_name":1))?.get("course_name"));
				}
			}			
		}
	}
	
	
	def add(HttpServletRequest request)
	{
		def course_services=$$("_id" : UUID.randomUUID().toString());
		// 课程服务编号
		course_services.append("services_code", answerTypeKGS.nextId().toString());
		//课程ID
		course_services.append("course_id", request.getParameter("course_id"));
		//服务类型
		course_services.append("services_type", request.getParameter("services_type") as Integer );
		//服务内容
		course_services.append("services_content", request.getParameter("services_content"));
		//序号
		course_services.append("order", request.getParameter("order") as Integer );
		table().save(course_services);
		Crud.opLog("course_services",[save:request["_id"]]);
		return OK();
	}

	def edit(HttpServletRequest request)
	{
		BasicDBObject course_services=new BasicDBObject();
		//课程ID
		course_services.append("course_id", request.getParameter("course_id"));
		//服务类型
		course_services.append("services_type", request.getParameter("services_type") as Integer);
		//服务内容
		course_services.append("services_content", request.getParameter("services_content"));
		//序号
		course_services.append("order", request.getParameter("order") as Integer);
		String _id = request.getParameter("_id");
		table().update(new BasicDBObject("_id":request.getParameter("_id")),new BasicDBObject('$set':course_services));
		Crud.opLog("course_services",[update:request["_id"]]);
		return OK();
	}

	def del(HttpServletRequest req){
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().remove(new BasicDBObject(_id,id))
		Crud.opLog("course_services",[del:id]);
		return OK();
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
}
