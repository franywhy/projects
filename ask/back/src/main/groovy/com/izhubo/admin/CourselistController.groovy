package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeCheckingMode
import groovy.transform.TypeChecked;

import java.util.regex.Pattern

import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.ServletRequestUtils;

import org.apache.commons.lang3.StringUtils

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.common.doc.UnmodifDBObject
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection

/**
 * 课程列表
 * @author zhengjimu
 * 2016-06-06
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class CourselistController extends BaseController {


	DBCollection table(){
		return mainMongo.getCollection('commodity_courses');
	}
	
	static final String DFMT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		def order = new UnmodifDBObject(new BasicDBObject("sort",1));
		String nc_name = req.getParameter("nc_name");
		String show_name = req.getParameter("show_name");
		String course_code = req.getParameter("course_code");
		if (StringUtils.isNotBlank(nc_name)){
			Pattern pattern = Pattern.compile("^.*" + nc_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nc_name").regex(pattern)
		}
		if (StringUtils.isNotBlank(show_name)){
			Pattern pattern = Pattern.compile("^.*" + show_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("show_name").regex(pattern)
		}
		if (StringUtils.isNotBlank(course_code)){
			Pattern pattern = Pattern.compile("^.*" + course_code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("course_code").regex(pattern)
		}
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,order)
	}

//	/**
//	 * 新增
//	 */
//	def add(HttpServletRequest req){
//		Map user = (Map) req.getSession().getAttribute("user");
//
//		def courselist = $$("_id" : UUID.randomUUID().toString());
//		Integer sort = ServletRequestUtils.getIntParameter(req, "sort", 0);
//		
//		courselist.put("create_user_id", user.get("_id") as Integer);
//		courselist.put("nc_id", req["nc_id"]);
//		courselist.put("nc_name", req["nc_name"]);
//		courselist.put("show_name", req["show_name"]);
//		courselist.put("course_code", req["course_code"]);
//		courselist.put("remark", req["remark"]);
//		courselist.put("sort", sort);
//		courselist.put("is_online", 1);
//		courselist.put("try_video", req["try_video"]);
//		courselist.put("tiku_url", req["tiku_url"]);
//		courselist.put("dr", req["dr"]);
//		courselist.put("shixu_url", req["shixu_url"]);
//		table().save(courselist);
//		Crud.opLog("courselist",[save:courselist["_id"]]);
//		return OK();
//	}

	/**
	 * 修改
	 */
	def edit(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req["_id"];
		String show_name = req["show_name"];
		String remark = req["remark"];
		String try_video = req["try_video"];
		String tiku_url = req["tiku_url"];
		String shixun_url = req["shixun_url"];
		
		if(StringUtils.isEmpty(id))
			return [code:0];
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"show_name" : show_name,
						"remark" : remark,
						"try_video" : try_video,
						"tiku_url" : tiku_url,
						"shixun_url" : shixun_url,
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"timestamp" : System.currentTimeMillis()
				)));
			
			Crud.opLog("courselist",[edit:id]);
		return OK();
	}

	/**
	 * 删除
	 */
	def del(HttpServletRequest req){
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().remove(new BasicDBObject(_id,id))
		Crud.opLog("courselist",[del:id]);
		return OK();
	}
}
