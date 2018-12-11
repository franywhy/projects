package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeCheckingMode
import groovy.transform.TypeChecked;

import java.util.HashMap;
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
 * 首页banner管理
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class BannerController extends BaseController {


	DBCollection table(){
		return mainMongo.getCollection('banner');
	}
	
	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		def order = $$("sort" : 1 , "_id" : 1);
		Crud.list(req,table(),null,MongoKey.ALL_FIELD,order)
	}

	/**
	 * 新增
	 */
	def add(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");

		def banner = $$("_id" : UUID.randomUUID().toString());
		banner.put("sort", ServletRequestUtils.getIntParameter(req, "sort", 1));
		banner.put("pic", req["pic"]);
		banner.put("url", req["url"]);
		banner.put("create_user_id", user.get("_id") as Integer);
		banner.put("timestamp", System.currentTimeMillis());
		banner.put("update_user_id", user.get("_id") as Integer);
		banner.put("update_time", System.currentTimeMillis());
//		banner.put("new_field", req["new_field"]);
		
		table().save(banner);
		Crud.opLog("banner",[save:banner["_id"]]);
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
						"url" : req["url"],
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
//						"new_field":req["new_field"]
					))
				);
		Crud.opLog("banner",[edit:id]);
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
		Crud.opLog("banner",[del:id]);
		return OK();
	}
}
