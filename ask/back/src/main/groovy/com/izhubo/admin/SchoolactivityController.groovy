package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeCheckingMode
import groovy.transform.TypeChecked;
import groovy.json.*

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern

import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.ServletRequestUtils;

import org.apache.commons.lang3.StringUtils

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.common.doc.ParamKey;
import com.izhubo.rest.common.doc.UnmodifDBObject
import com.izhubo.rest.common.util.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder;

/**
 * 校区活动管理
 * @author zhengjimu
 * 2016-06-22
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class SchoolactivityController extends BaseController {


	DBCollection table(){
		return mainMongo.getCollection('school_notice');
	}
	static final String DFMT = "yyyy-MM-dd HH:mm:ss";

	@Value("#{application['h5.domain']}")
	private String h5domain;
	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC)
	}
	def buildinfourl(String id)
	{
		h5domain+"notice_info.html?notice_id="+id;
	}
   /**
	 * 新增
	 */
	def add(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		def schoolactivity = $$("_id" : UUID.randomUUID().toString());
		String school_code = req["school_code"];
		def school_name = "";
		if(school_code == "all") {
			school_name = "全部校区";
		}
		else {
			QueryBuilder query = QueryBuilder.start();
			Pattern pattern = Pattern.compile("^.*" + req["school_code"] + ".*\$", Pattern.CASE_INSENSITIVE);
			query.and("code").regex(pattern)
			school_name = area().findOne(query.get() , new BasicDBObject("name":1))?.get("name");
			if(school_name == null || school_code.length() != 11){
				Map fail = new HashMap();
				fail.put("msg","抱歉，您填写的校区编号为无效编号");
				fail.put("code",0);
				return fail;
			}
		}
		String id = UUID.randomUUID().toString();
		schoolactivity.put("_id", id);
		schoolactivity.put("activity_title", req["activity_title"]);
		schoolactivity.put("activity_author", req["activity_author"]);
		schoolactivity.put("activity_picurl", req["activity_picurl"]);
		schoolactivity.put("url", req["activity_infourl"]);
		schoolactivity.put("is_recommand", Integer.valueOf(req.getParameter("is_recommand")));
		schoolactivity.put("activity_infourl", buildinfourl(id));
		schoolactivity.put("summary", req["summary"]);
		schoolactivity.put("activity_info", req["activity_info"]);
		schoolactivity.put("default_info_url",buildinfourl(id));
		schoolactivity.put("activity_big_picurl", req["activity_big_picurl"]);
		schoolactivity.put("school_name", school_name);
		schoolactivity.put("school_code", school_code);
		schoolactivity.put("create_time", System.currentTimeMillis());
		schoolactivity.put("timestamp", System.currentTimeMillis());
		schoolactivity.put("dr", 1);
		table().save(schoolactivity);
		Crud.opLog("schoolactivity",[save:schoolactivity["_id"]]);
		return OK();
	}

	/**
	 * 修改
	 */
	def edit(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id];
		
		String school_code = req["school_code"];
		def school_name = "";
		if(school_code == "all") {
			school_name = "全部校区";
		}
		else {
			QueryBuilder query = QueryBuilder.start();
			Pattern pattern = Pattern.compile("^.*" + req["school_code"] + ".*\$", Pattern.CASE_INSENSITIVE);
			query.and("code").regex(pattern)
			school_name = area().findOne(query.get() , new BasicDBObject("name":1))?.get("name");
			if(school_name == null || school_code.length() != 11){
				Map fail = new HashMap();
				fail.put("msg","抱歉，您填写的校区编号为无效编号");
				fail.put("code",0);
				return fail;
			}
		}
		
		
		String activity_title = req.getParameter("activity_title");
		String activity_author = req.getParameter("activity_author");
		String activity_picurl = req.getParameter("activity_picurl");
		String activity_infourl = req.getParameter("activity_infourl");
		String activity_info = req.getParameter("activity_info");
	    String activity_big_picurl = req.getParameter("activity_big_picurl");
		String summary = req.getParameter("summary");

        Integer is_recommand = Integer.valueOf(req.getParameter("is_recommand"));

	
		if(StringUtils.isEmpty(id))
			return [code:0];
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"activity_title" : activity_title,
						"activity_author" : activity_author,
						"activity_picurl" : activity_picurl,
							"url" : activity_infourl,
						"activity_infourl" : buildinfourl(id),
						"is_recommand":is_recommand,
							"default_info_url":buildinfourl(id),
							"activity_big_picurl":activity_big_picurl,
						"activity_info" : activity_info,
						"summary" : summary,
						"school_code" : school_code,
						"school_name":school_name,
						"timestamp": System.currentTimeMillis()
					)
				));
			Crud.opLog("schoolactivity",[edit:id]);
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
		Crud.opLog("schoolactivity",[del:id]);
		return OK();
	}

	def disable(HttpServletRequest req){
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
						new BasicDBObject(
								"dr" : 1,
								"timestamp": System.currentTimeMillis()
						)
				));

		return OK();
	}

	def enable(HttpServletRequest req){
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
						new BasicDBObject(
								"dr" : 0,
								"timestamp": System.currentTimeMillis()
						)
				));

		return OK();
	}

}
	
	
	

