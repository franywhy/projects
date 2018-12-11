package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeCheckingMode
import groovy.transform.TypeChecked;

import java.util.HashMap;
import java.util.Map;
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
 * 校区图片管理
 * @author zhengxin
 * 2016-05-10
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class SchoolpicController extends BaseController {


	DBCollection table(){
		return mainMongo.getCollection('school_pic');
	}
	DBCollection area(){
		return mainMongo.getCollection('area');
	}

	static final String DFMT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		String province = req.getParameter("province");
		String school = req.getParameter("school");
		if (StringUtils.isNotBlank(province)){
			Pattern pattern = Pattern.compile("^.*" + province + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("school_code").regex(pattern)
		}
		if (StringUtils.isNotBlank(school)){
			Pattern pattern = Pattern.compile("^.*" + school + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("school_name").regex(pattern)
		}
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC)
	}

	/**
	 * 获取校区省份下拉列表
	 */
	def getprovince(HttpServletRequest req){

		Map map = new HashMap();
		map.put("data", GetProvinceAndSchool());
		return map;
	}

	/**
	 * 新增
	 */
	def add(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
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
		def schoolpic = $$("_id" : UUID.randomUUID().toString());
		schoolpic.put("create_user_id", user.get("_id") as Integer);
		schoolpic.put("school_code", req["school_code"]);
		schoolpic.put("school_name", school_name);
		schoolpic.put("pic_url", req["pic_url"]);
		schoolpic.put("create_time", System.currentTimeMillis());
		schoolpic.put("timestamp", System.currentTimeMillis());
		table().save(schoolpic);
		Crud.opLog("schoolpic",[save:schoolpic["_id"]]);
		return OK();
	}

	/**
	 * 修改
	 */
	def edit(HttpServletRequest req){
		String id = req["_id"];
		String school_code = req["school_code"];

		def school_name = "";
		if(school_code == "all") {
			school_name = "全部校区";
		}
		else {
			QueryBuilder query = QueryBuilder.start();
			Pattern pattern = Pattern.compile("^.*" + school_code + ".*\$", Pattern.CASE_INSENSITIVE);
			query.and("code").regex(pattern)
			school_name = area().findOne(query.get() , new BasicDBObject("name":1))?.get("name");
			if(school_name == null || school_code.length() != 11){
				Map fail = new HashMap();
				fail.put("msg","抱歉，您填写的校区编号为无效编号");
				fail.put("code",0);
				return fail;
			}
		}
		String pic_url = req["pic_url"];

		if(StringUtils.isEmpty(id))
			return [code:0];
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
				new BasicDBObject(
				"school_code" : school_code,
				"school_name" : school_name,
				"pic_url" : pic_url,
				"update_time" : System.currentTimeMillis(),
				"timestamp" : System.currentTimeMillis()
				)
				));

		Crud.opLog("schoolpic",[edit:id]);
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
		Crud.opLog("schoolpic",[del:id]);
		return OK();
	}
}
