package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.util.regex.Pattern

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.springframework.web.bind.ServletRequestUtils

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder

/**
 * 省考试查询地址
 * @author Administrator
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class ProvinceexamController extends BaseController {
	
	/*
	 * _id
	 * type
	 * url
	 * area_code
	 * area_nc_id
	 * 操作信息 
	 */
	
	DBCollection table(){
		return mainMongo.getCollection('province_exam_url');
	}
	
	def list(HttpServletRequest req){
		QueryBuilder query = QueryBuilder.start();
		//省编码
		String code = req.getParameter("qcode");
		//省
		String name = req.getParameter("qname");
		//考试地址类型
		Integer type =  ServletRequestUtils.getIntParameter(req, "qtype" , -1);
		
		if (StringUtils.isNotBlank(code)){
			query.and("code").is(code);
		}
		if (StringUtils.isNotBlank(name)){
			Pattern pattern = Pattern.compile("^.*" + name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("name").regex(pattern)
		}
		if(type>-1){
			query.and("type").is(type);
		}
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC);
	}
	
	/**
	 * 获取省份下拉列表
	 */
	def getprovince(HttpServletRequest req){
		
		Map map = new HashMap();
		List<DBObject> list = GetProvince();
		if(null != list){
			DBObject db = new BasicDBObject();
			db.put("code" , "all");
			db.put("name" , "全部校区");
			list.add(db);
		}
		map.put("code", 1);
		map.put("data", list);
		return map;
	}
	
	def add(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		//省编码
		String code = req["code"];
//		//省nc_id
//		String nc_id = req["nc_id"];
		//考试查询跳转地址
		String url = req["url"];
		//考试地址类型
		Integer type =  ServletRequestUtils.getIntParameter(req, "type" , 0);
		//省名称
		def name = "";
		Integer order = 1;
		if(code == "all") {
			name = "全部校区";
			order = 9999;
		}else{
			QueryBuilder query = QueryBuilder.start();
			query.and("code").is(code);
			name = area().findOne(query.get() , new BasicDBObject("name":1))?.get("name");
			//省份校验
			if(StringUtils.isBlank(name) || code.length() != 5){
				Map fail = new HashMap();
				fail.put("msg","抱歉，您填写的省份编号为无效编号");
				fail.put("code",0);
				return fail;
			}
			//重复添加校验
			if(table().count($$("code" : code , "type" : type))>0){
				Map fail = new HashMap();
				fail.put("msg","已经存在该地区下的考试类型的数据了!");
				fail.put("code",0);
				return fail;
			}
		}
		//新增操作
		def pe = $$("_id" : UUID.randomUUID().toString());
		pe.put("create_user_id", user.get("_id") as Integer);
		pe.put("code", code);
		pe.put("name", name);
//		pe.put("nc_id", nc_id);
		pe.put("url", url);
		pe.put("type", type);
		pe.put("timestamp", System.currentTimeMillis());
		pe.put("order", order);//用于排序
		table().save(pe);
		Crud.opLog("province_exam_url",[save:pe["_id"]]);
		return OK();
	}

	
	
	/**
	 * 修改
	 */
	def edit(HttpServletRequest req){
		String id = req["_id"];
		//考试查询跳转地址
		String url = req["url"];
		Map user = (Map) req.getSession().getAttribute("user");
		
		if(StringUtils.isEmpty(id))
			return [code:0];
		if(StringUtils.isEmpty(url)){
			return [code:0 , "data" : "地址不能为空!"];
		}	
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
				new BasicDBObject(
				"url" : url,
				"update_time" : System.currentTimeMillis(),
				"update_user_id" : user.get("_id") as Integer
				)
				));

		Crud.opLog("province_exam_url",[edit:id]);
		return OK();
	}
	/**
	 * 删除
	 */
	def del(HttpServletRequest req){
		String id = req[_id];
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().remove(new BasicDBObject(_id,id))
		Crud.opLog("province_exam_url",[del:id]);
		return OK();
	}
	
}
