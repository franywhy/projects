package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils
import org.springframework.web.bind.ServletRequestUtils

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection

/**
 * 恒企志专题档案
 * @author shihongjie
 *
 */
@RestWithSession
class AnnalsController extends BaseController {
	
	DBCollection table(){
		return mainMongo.getCollection('annals');
	}
	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		def order = $$("status" : -1 ,"sort" : -1 , "create_time" : 1);
		Crud.list(req,table(),null,MongoKey.ALL_FIELD,order)
	}
	
	/**
	 * 新增
	 */
	def add(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");

		def obj = $$("_id" : UUID.randomUUID().toString());
		obj.put("sort", ServletRequestUtils.getIntParameter(req, "sort", 1));
		obj.put("status", ServletRequestUtils.getIntParameter(req, "status", 1));
		obj.put("pic", req["pic"]);
		obj.put("title", req["title"]);
		
		obj.put("content", req["content"]);
		obj.put("create_user_id", user.get("_id") as Integer);
		obj.put("timestamp", System.currentTimeMillis());
//		banner.put("update_user_id", user.get("_id") as Integer);
//		banner.put("update_time", System.currentTimeMillis());
		table().save(obj);
		Crud.opLog("annals",[save:obj["_id"]]);
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
						"sort" : ServletRequestUtils.getIntParameter(req, "sort", 1),
						"pic" : req["pic"],
						"status" : ServletRequestUtils.getIntParameter(req, "status", 1),
						"title" : req["title"],
						"content" : req["content"],
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis()
					))
				);
		Crud.opLog("annals",[edit:id]);
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
		Crud.opLog("annals",[del:id]);
		return OK();
	}
	
}
