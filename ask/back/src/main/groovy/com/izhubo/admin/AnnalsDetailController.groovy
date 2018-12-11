package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$

import java.text.SimpleDateFormat;

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils
import org.springframework.web.bind.ServletRequestUtils

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection

/**
 * 恒企志文章档案
 * @author shihongjie
 *
 */
@RestWithSession
class AnnalsDetailController extends BaseController {
	
	private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	DBCollection table(){
		return mainMongo.getCollection('annals_detail');
	}
	DBCollection annals(){
		return mainMongo.getCollection('annals');
	}
	//专题列表
	def annalsList(HttpServletRequest req){
		def order = $$("status" : -1 ,"sort" : 1 , "create_time" : 1);
		def aList = annals().find(null ,$$("_id" : 1 , "title" : 1 , "status" : 1) ).sort(order).toArray();
		return [code : 1 , data : aList];
	}
	
	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		def q = Web.fillTimeBetween(req);
		//专题分类
		stringQuery(q,req,'annals_id');
		//封面标题
		stringQuery(q,req,'cover_title');
		intQuery(q,req,'status');
		def order = $$("status" : -1 , "create_time" : 1);
		Crud.list(req,table(),q.get(),MongoKey.ALL_FIELD,order){List<BasicDBObject> data->
			//id-->name
			for(BasicDBObject obj: data){ 				
				obj.put("annals_title", annals().findOne(new BasicDBObject("_id" : obj.get("annals_id")) , new BasicDBObject("title":1))?.get("title"));
			}
		}
	}
	
	/**
	 * 新增
	 */
	def add(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");

		def obj = $$("_id" : UUID.randomUUID().toString());
		//状态
		obj.put("status", ServletRequestUtils.getIntParameter(req, "status", 1));
		
		//封面图片
		obj.put("cover_pic", req["cover_pic"]);
		//封面分类
		obj.put("cover_type", req["cover_type"]);
		//封面标题
		obj.put("cover_title", req["cover_title"]);
		//封面作者
		obj.put("cover_author", req["cover_author"]);
		//封面简介
		obj.put("cover_content", req["cover_content"]);
		//专题ID
		obj.put("annals_id", req["annals_id"]);
		
		Long now = System.currentTimeMillis();
		//内容
		obj.put("content_html", req["content_html"]);
		//阅读人数
		obj.put("read_count", 0);
		obj.put("create_user_id", user.get("_id") as Integer);
		obj.put("timestamp_format", TIMESTAMP_FORMAT.format(new Date(now)));
		obj.put("timestamp", now);
		obj.put("create_time", now);
		table().save(obj);
		Crud.opLog("annals_detail",[save:obj["_id"]]);
		return OK();
	}
	
	/**
	 * 修改
	 */
	def edit(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		def id = req["_id"];
		if(StringUtils.isEmpty(id))return [code:0];
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"cover_pic" : req["cover_pic"],
						"cover_type" : req["cover_type"],
						"cover_title" : req["cover_title"],
						"cover_author" : req["cover_author"],
						"cover_content" : req["cover_content"],
						
						"annals_id" : req["annals_id"],
						"status" : ServletRequestUtils.getIntParameter(req, "status", 1),
						"content_html" : req["content_html"],
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis()
					))
				);
		Crud.opLog("annals_detail",[edit:id]);
		return OK();
	}
	
	/**
	 * 禁用
	 * @param req
	 * @return
	 */
	def to_unable(HttpServletRequest req){
		return updateStatus(req , 0);
	}
	
	/**
	 * 启用
	 * @param req
	 * @return
	 */
	def to_able(HttpServletRequest req){
		return updateStatus(req , 1);
	}
	
	def updateStatus(HttpServletRequest req , Integer status){
		Map user = (Map) req.getSession().getAttribute("user");
		def id = req["_id"];
		if(StringUtils.isEmpty(id))return [code:0];
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"status" : status,
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis()
					))
				);
		return OK();
	}
	
	/**
	 * 删除
	 */
	def del(HttpServletRequest req){
		def id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().remove(new BasicDBObject(_id,id))
		Crud.opLog("annals_detail",[del:id]);
		return OK();
	}
	
}
