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
 * 课程列表banner管理
 * @author zhengxin
 * 2016-05-25
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class CoursebannerController extends BaseController {


	DBCollection table(){
		return mainMongo.getCollection('course_banner');
	}
	
	static final String DFMT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		query.and("dr").is(0);
		def order = new UnmodifDBObject(new BasicDBObject("order",1));
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,order)
	}

	/**
	 * 新增
	 */
	def add(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");

		def coursebanner = $$("_id" : UUID.randomUUID().toString());
	    Integer is_default = ServletRequestUtils.getIntParameter(req, "is_default", 0);
		BasicDBObject defaultbanner = table().findOne(new BasicDBObject("is_default" : 1))
		if(defaultbanner != null && is_default == 1){
			Map fail = new HashMap();
			fail.put("fail",1);
			fail.put("code",1);
			return fail;
		}
		if(is_default == 2){
			is_default = 1;
		}
		coursebanner.put("create_user_id", user.get("_id") as Integer);
		coursebanner.put("order", req["order"] as Integer);
		coursebanner.put("name", req["name"]);
		coursebanner.put("pc_icon", req["pc_icon"]);
		coursebanner.put("pc_pic", req["pc_pic"]);
		coursebanner.put("pc_url", req["pc_url"]);
		coursebanner.put("pc_is_show", req["pc_is_show"] as Integer);
		coursebanner.put("is_default", is_default);
		coursebanner.put("app_pic", req["app_pic"]);
		coursebanner.put("app_url", req["app_url"]);
		coursebanner.put("app_is_show", req["app_is_show"] as Integer);
		coursebanner.put("remark", req["remark"]);
		coursebanner.put("create_time", System.currentTimeMillis());
		coursebanner.put("timestamp", System.currentTimeMillis());
		coursebanner.put("dr", 0);
		table().save(coursebanner);
		if(defaultbanner != null){
			table().update(
				new BasicDBObject("_id":defaultbanner.get("_id")),
				new BasicDBObject('$set':
					new BasicDBObject(
						"is_default" : 0
					)
				));
		}
		Crud.opLog("coursebanner",[save:coursebanner["_id"]]);
		return OK();
	}

	/**
	 * 修改
	 */
	def edit(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req["_id"];
		String order = req["order"] as Integer;
		String name = req["name"];
		String pc_icon = req["pc_icon"];
		String pc_pic = req["pc_pic"];
		String pc_url = req["pc_url"];
		Integer pc_is_show = req["pc_is_show"] as Integer;
		String app_pic = req["app_pic"];
		String app_url = req["app_url"];
		Integer app_is_show = req["app_is_show"] as Integer;
		String remark = req["remark"];
		Integer is_default = ServletRequestUtils.getIntParameter(req, "is_default", 0);
		BasicDBObject defaultbanner = table().findOne(new BasicDBObject("is_default" : 1))
		if(defaultbanner != null && is_default == 1 && !id.equals(defaultbanner.get("_id"))){
			Map fail = new HashMap();
			fail.put("fail",1);
			fail.put("code",1);
			return fail;
		}
		if(is_default == 2){
			is_default = 1;
		}
		if(StringUtils.isEmpty(id))
			return [code:0];
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"order" : order,
						"name" : name,
						"pc_icon" : pc_icon,
						"pc_pic" : pc_pic,
						"pc_url" : pc_url,
						"pc_is_show" : pc_is_show,
						"app_pic" : app_pic,
						"app_url" : app_url,
						"app_is_show" : app_is_show,
						"remark" : remark,
						"is_default" : is_default,
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"timestamp" : System.currentTimeMillis()
					)
				));
		if(defaultbanner != null && !id.equals(defaultbanner.get("_id"))){
			table().update(
				new BasicDBObject("_id":defaultbanner.get("_id")),
				new BasicDBObject('$set':
					new BasicDBObject(
						"is_default" : 0
					)
				));
		}
			
			Crud.opLog("coursebanner",[edit:id]);
		return OK();
	}

	/**
	 * 删除
	 */
	def del(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"dr" : 1
					)
				));
		Crud.opLog("coursebanner",[del:id]);
		return OK();
	}
}
