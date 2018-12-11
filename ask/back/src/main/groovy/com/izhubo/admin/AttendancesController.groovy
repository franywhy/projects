package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeCheckingMode
import groovy.transform.TypeChecked;

import java.text.DateFormat
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
 * 考勤表
 * @author zhengxin
 * 2016-05-12
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class AttendancesController extends BaseController {


	DBCollection table(){
		return mainMongo.getCollection('attendances');
	}
	DBCollection student(){
		return mainMongo.getCollection('attendance_students');
	}
	
	static final String DFMT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		SimpleDateFormat ddtf = new SimpleDateFormat(DFMT);
		def query = Web.fillTimeBetween(req);
		//stringQuery(query,req,'nick_name')
		def nc_code = req.getParameter("nc_code");
		def nc_class_plan = req.getParameter("nc_class_plan");
		def nc_school_code = req.getParameter("nc_school_code");
		def nc_school_name = req.getParameter("nc_school_name");
		def nc_course_name = req.getParameter("nc_course_name");
		def nc_teacher_name = req.getParameter("nc_teacher_name");
		def room = req.getParameter("room");
		def sessions = req.getParameter("sessions");
		def sattendance_time = req.getParameter("sattendance_time");
		def eattendance_time = req.getParameter("eattendance_time");
		def ssyn_time = req.getParameter("ssyn_time");
		def esyn_time = req.getParameter("esyn_time");
		if(sattendance_time != null || eattendance_time != null){
			query = Web.fillTimeBetween(query,"attendance_time",new SimpleDateFormat(DFMT).parse(sattendance_time),new SimpleDateFormat(DFMT).parse(eattendance_time));
		}
		if(ssyn_time != null || esyn_time != null){
			query = Web.fillTimeBetween(query,"syn_time",new SimpleDateFormat(DFMT).parse(ssyn_time),new SimpleDateFormat(DFMT).parse(esyn_time));
		}
		if (StringUtils.isNotBlank(nc_code)){
			Pattern pattern = Pattern.compile("^.*" + nc_code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nc_code").regex(pattern)
		}
		if (StringUtils.isNotBlank(nc_class_plan)){
			Pattern pattern = Pattern.compile("^.*" + nc_class_plan + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nc_class_plan").regex(pattern)
		}
		if (StringUtils.isNotBlank(nc_school_code)){
			Pattern pattern = Pattern.compile("^.*" + nc_school_code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nc_school_code").regex(pattern)
		}
		if (StringUtils.isNotBlank(nc_school_name)){
			Pattern pattern = Pattern.compile("^.*" + nc_school_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nc_school_name").regex(pattern)
		}
		if (StringUtils.isNotBlank(nc_course_name)){
			Pattern pattern = Pattern.compile("^.*" + nc_course_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nc_course_name").regex(pattern)
		}
		if (StringUtils.isNotBlank(nc_teacher_name)){
			Pattern pattern = Pattern.compile("^.*" + nc_teacher_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nc_teacher_name").regex(pattern)
		}
		if (StringUtils.isNotBlank(room)){
			Pattern pattern = Pattern.compile("^.*" + room + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("room").regex(pattern)
		}
		if (StringUtils.isNotBlank(sessions)){
			sessions = sessions as Integer;
			query.and("sessions").is(sessions);
		}
		query.and("dr").is(0);
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			for(BasicDBObject obj: data){
				if (obj['attendance_time_long'] != null){
					obj.put("attendance_time", ddtf.format(new Date(obj['attendance_time_long'])));
				}
				if (obj['syn_time'] != null){
					obj.put("syn_time", ddtf.format(new Date(obj['syn_time'])));
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
		query.and("nc_attendance_id").regex(pattern);
		pattern = Pattern.compile("^.*0.*\$", Pattern.CASE_INSENSITIVE)
		query.and("dr").regex(pattern);
		String name = table().findOne(new BasicDBObject("nc_id":nc_id),new BasicDBObject("nc_course_name",1))?.get("nc_course_name")
		Crud.list(req,student(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			for(BasicDBObject obj: data){
				if (StringUtils.isNotBlank(name)){
					obj.put("nc_course_name", name)
				}else{
					obj.put("nc_course_name", "无")
				}
			}
		};
	}
	

}
