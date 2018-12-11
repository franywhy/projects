package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeCheckingMode
import groovy.transform.TypeChecked;

import java.text.SimpleDateFormat
import java.util.regex.Pattern

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection

/**
 * 教师列表
 * @author zhengxin
 * 2016-03-02
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class NCTeacherController extends BaseController {


	DBCollection table(){
		return mainMongo.getCollection('teachers');
	}
	
	static final String DFMT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		//stringQuery(query,req,'nick_name')
		String nick_name = req.getParameter("nick_name");
		String nc_id = req.getParameter("nc_id");
		String querysex = req.getParameter("sex");
		String name = req.getParameter("name");
		String telephone = req.getParameter("telephone");
		String email = req.getParameter("email");
		String school_code = req.getParameter("school_code");
		String querystatus = req.getParameter("status");
		String ssyn_time = req.getParameter("ssyn_time");
		String esyn_time = req.getParameter("esyn_time");
		String slast_login = req.getParameter("slast_login");
		String elast_login = req.getParameter("elast_login");
		String screate_time = req.getParameter("screate_time");
		String ecreate_time = req.getParameter("ecreate_time");
		if(ssyn_time != null || esyn_time != null){
			query = Web.fillTimeBetween(query,"syn_time",new SimpleDateFormat(DFMT).parse(ssyn_time),new SimpleDateFormat(DFMT).parse(esyn_time));
		}
		if(slast_login != null || elast_login != null){
			query = Web.fillTimeBetween(query,"last_login",new SimpleDateFormat(DFMT).parse(slast_login),new SimpleDateFormat(DFMT).parse(elast_login));
		}
		if(screate_time != null || ecreate_time != null){
			query = Web.fillTimeBetween(query,"create_time",new SimpleDateFormat(DFMT).parse(screate_time),new SimpleDateFormat(DFMT).parse(ecreate_time));
		}
		if (StringUtils.isNotBlank(nick_name)){
			Pattern pattern = Pattern.compile("^.*" + nick_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nick_name").regex(pattern)
		}
		if (StringUtils.isNotBlank(nc_id)){
			Pattern pattern = Pattern.compile("^.*" + nc_id + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nc_id").regex(pattern)
		}
		if (StringUtils.isNotBlank(querysex)){
			if(Integer.valueOf(querysex) == 0){
				query.or(new BasicDBObject("sex", 0), new BasicDBObject("sex", null));
			}else{
				query.and("sex").is(Integer.valueOf(querysex));
			}
		}
		if (StringUtils.isNotBlank(name)){
			Pattern pattern = Pattern.compile("^.*" + name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("name").regex(pattern)
		}
		if (StringUtils.isNotBlank(telephone)){
			Pattern pattern = Pattern.compile("^.*" + telephone + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("telephone").regex(pattern)
		}
		if (StringUtils.isNotBlank(email)){
			Pattern pattern = Pattern.compile("^.*" + email + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("email").regex(pattern)
		}
		if (StringUtils.isNotBlank(school_code)){
			Pattern pattern = Pattern.compile("^.*" + school_code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("school_code").regex(pattern)
		}
		if (StringUtils.isNotBlank(querystatus)){
			query.and("status").is(Integer.valueOf(querystatus));
		}
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC)
	}

	/**
	 * 新增
	 */
	/*def add(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");

		def demo = $$("_id" : UUID.randomUUID().toString());
		demo.put("user_id", user.get("_id") as Integer);
		demo.put("content", req["content"]);
		demo.put("timestamp", System.currentTimeMillis());
		table().save(demo);
		Crud.opLog("demo",[save:demo["_id"]]);
		return OK();
	}*/

	/**
	 * 修改
	 */
	def edit(HttpServletRequest req){
		String id = req[_id];
		String nick_name = req.getParameter("nick_name");
		
		if(StringUtils.isEmpty(id))
			return [code:0];
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"nick_name" : nick_name,
						"update_time" : System.currentTimeMillis()
					)
				));
			
			Crud.opLog("teacher",[edit:id]);
		return OK();
	}

	/**
	 * 冻结/解冻
	 */
	def freeze(HttpServletRequest req){
		
		String id = req[_id]
		def status = req.getParameter("status")
		if(StringUtils.isBlank(status)){
			status = 0
		}
		status = status as Integer
		if(status == 0){
			status = 1
		}else{
			status = 0
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"status" : status,
						"update_time" : System.currentTimeMillis()
					)
				));
		Crud.opLog("teacher",[edit:id]);
		return OK();
	}
}
