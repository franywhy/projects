package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeCheckingMode
import groovy.transform.TypeChecked;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder

/**
 * 排课计划
 * @author zhengxin
 * 2016-05-05
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class ClassplanController extends BaseController {


	DBCollection table(){
		return mainMongo.getCollection('class_plan');
	}
	DBCollection areatable(){
		return mainMongo.getCollection('area');
	}
	DBCollection coursetable(){
		return mainMongo.getCollection('plan_courses');
	}
	DBCollection studenttable(){
		return mainMongo.getCollection('plan_student');
	}
	
	static final String DFMT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		//stringQuery(query,req,'nick_name')
		String nc_area_id = req.getParameter("nc_area_id");
		String class_time = req.getParameter("class_time");
		String name = req.getParameter("name");
		String nc_course_id = req.getParameter("nc_course_id");
		String sopen_time = req.getParameter("sopen_time");
		String eopen_time = req.getParameter("eopen_time");
		String ssyn_time = req.getParameter("ssyn_time");
		String esyn_time = req.getParameter("esyn_time");
		if(ssyn_time != null || esyn_time != null){
			query = Web.fillTimeBetween(query,"syn_time",new SimpleDateFormat(DFMT).parse(ssyn_time),new SimpleDateFormat(DFMT).parse(ssyn_time));
		}
		if(sopen_time != null || eopen_time != null){
			query = Web.fillTimeBetween(query,"openTimeLong",new SimpleDateFormat(DFMT).parse(sopen_time),new SimpleDateFormat(DFMT).parse(eopen_time));
		}
		if (StringUtils.isNotBlank(nc_area_id)){
			Pattern pattern = Pattern.compile("^.*" + nc_area_id + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nc_area_id").regex(pattern)
		}
		if (StringUtils.isNotBlank(name)){
			Pattern pattern = Pattern.compile("^.*" + name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("name").regex(pattern)
		}
		if (StringUtils.isNotBlank(class_time)){
			Pattern pattern = Pattern.compile("^.*" + class_time + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("class_time").regex(pattern)
		}
		if (StringUtils.isNotBlank(nc_course_id)){
			Pattern pattern = Pattern.compile("^.*" + nc_course_id + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nc_course_id").regex(pattern)
		}
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			for(BasicDBObject obj: data){
				obj.put("nc_area_name", areatable().findOne(new BasicDBObject("nc_id":obj['nc_area_id']),new BasicDBObject("name",1))?.get("name"))
			}
		};
	}
	
	/**
	 * 课程信息
	 */
	def course(HttpServletRequest req){
		def sort_sj = $$("sort" : 1);
		def query = Web.fillTimeBetween(req);
		String nc_id = req.getParameter("nc_id");
		Pattern pattern = Pattern.compile("^.*" + nc_id + ".*\$", Pattern.CASE_INSENSITIVE)
		query.and("nc_plan_id").regex(pattern);
		query.and("dr").is(0);
		String name = table().findOne(new BasicDBObject("nc_id":nc_id),new BasicDBObject("name",1))?.get("name")
		Crud.list(req,coursetable(),query.get(),MongoKey.ALL_FIELD,sort_sj){List<BasicDBObject> data->
			for(BasicDBObject obj: data){
				if (StringUtils.isNotBlank(name)){
					obj.put("class_name", name)
				}else{
					obj.put("class_name", "无")
				}
			}
		};
	}
	
	/**
	 * 学生信息
	 */
	def student(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		String nc_id = req.getParameter("nc_id");
		Pattern pattern = Pattern.compile("^.*" + nc_id + ".*\$", Pattern.CASE_INSENSITIVE)
		query.and("nc_plan_id").regex(pattern);
		query.and("dr").is(0);
		String name = table().findOne(new BasicDBObject("nc_id":nc_id),new BasicDBObject("name",1))?.get("name")
		Crud.list(req,studenttable(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			for(BasicDBObject obj: data){
				obj.put("class_name", name)
			}
		};
	}
	
	/**
	 * 获取校区下拉列表
	 */
	def getarea(HttpServletRequest req){
		QueryBuilder query = QueryBuilder.start();
		query.and("is_school").is("1");
		Pattern pattern = Pattern.compile("^\\w{11,12}\$", Pattern.CASE_INSENSITIVE)
		query.and("code").regex(pattern)
		Map map = Crud.list(req,areatable(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC)
		return map;
	}

}
