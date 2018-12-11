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
class StarStudentController extends BaseController{
	
	@Resource
	KGS roomKGS;
	KGS answerTypeKGS;
	DBCollection table(){mainMongo.getCollection('star_student')}
	DBCollection _course(){ mainMongo.getCollection('course')}

	//明星学员列表显示
	def list(HttpServletRequest req){
		def query = QueryBuilder.start()
		  def student_name = req.getParameter("student_name");
		  if (StringUtils.isNotBlank(student_name)) {
			  Pattern pattern = Pattern.compile("^.*" + student_name + ".*\$", Pattern.CASE_INSENSITIVE)
			  query.and("student_name").regex(pattern)
		  }
		 stringQuery(query,req,'course_id');  //课程ID
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
		// 明星学员编号
		Integer _id = roomKGS.nextId();
		def star_student=$$("_id" : _id);
		//所属课程
		star_student.append("course_id", request.getParameter("course_id"));
		//学员名称
		star_student.append("student_name", request.getParameter("student_name"));
		//头像
		star_student.append("student_pic", request.getParameter("student_pic"));
		//工作单位
		star_student.append("work_unit", request.getParameter("work_unit"));
		//薪资
		star_student.append("salary", request.getParameter("salary") as double);
		//学员评价
		star_student.append("student_memo", request.getParameter("student_memo"));
		table().save(star_student);
		Crud.opLog("star_student",[save:request["_id"]]);
		return OK();
	}

	def edit(HttpServletRequest request)
	{
		BasicDBObject star_student=new BasicDBObject();
		//所属课程
		star_student.append("course_id", request.getParameter("course_id"));
		//学员名称
		star_student.append("student_name", request.getParameter("student_name"));
		//头像
		star_student.append("student_pic", request.getParameter("student_pic"));
		//工作单位
		star_student.append("work_unit", request.getParameter("work_unit"));
		//薪资
		star_student.append("salary", request.getParameter("salary") as double);
		//学员评价
		star_student.append("student_memo", request.getParameter("student_memo"));
		Integer _id = request.getParameter("_id") as Integer;
		table().update(new BasicDBObject("_id":_id),new BasicDBObject('$set':star_student));
		Crud.opLog("star_student",[update:request["_id"]]);
		return OK();
	}

	def del(HttpServletRequest req){
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().remove(new BasicDBObject(_id,id as Integer))
		Crud.opLog("star_student",[del:id]);
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
