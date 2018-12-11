package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import java.text.SimpleDateFormat
import javax.servlet.http.HttpServletRequest;

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode


import com.izhubo.rest.anno.RestWithSession
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection;

import com.izhubo.rest.web.Crud
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.common.doc.UnmodifDBObject

import org.apache.commons.lang3.StringUtils


/**
 * @ClassName: AdvertisingController
 * @Description: 广告位
 * @author vince
 * @date 2017年8月21日
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class AdvertisingController extends BaseController{
	
	DBCollection advertising(){
		return mainMongo.getCollection('advertising');
	}
	
	
	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		query.and("dr").is(0);
		def order = $$("update_time" : -1);
		Crud.list(req,advertising(),query.get(),MongoKey.ALL_FIELD,order){List<BasicDBObject> data->
		};
	}

	
	/**
	 * 新增
	 */
	def add(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		def now= new Date()
		def longTime= now.time
		def advertisingitem = $$("_id" : UUID.randomUUID().toString());
		advertisingitem.put("title", req["title"]);
		advertisingitem.put("pic", req["pic"]);
		advertisingitem.put("url", req["url"]);
		advertisingitem.put("countdown", Integer.parseInt(req["countdown"]));
		advertisingitem.put("create_time", longTime);
		advertisingitem.put("update_time", longTime);
		
		def num = advertising().find($$("dr":0),$$("_id" : 1,"version" : 1)).sort($$("version":-1))?.toArray();
		Integer version = num.get(0).get("version")
		advertisingitem.put("version", version+1);
		advertisingitem.put("is_show", 1);
		advertisingitem.put("dr", 0);
		advertising().save(advertisingitem);
		Crud.opLog("advertising",[save:advertisingitem["_id"]]);
		return OK();
	}
	
	

	/**
	 * 修改
	 */
	def edit(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id];
		String advertising_title = req.getParameter("title");
		String advertising_pic = req.getParameter("pic");
		String advertising_url = req.getParameter("url");
		String advertising_countdown = req.getParameter("countdown");
		Integer advertising_is_show = req.getParameter("is_show")?req.getParameter("is_show"):1;
		
		def num = advertising().find($$("dr":0),$$("_id" : 1,"version" : 1)).sort($$("version":-1))?.toArray();
		Integer advertising_version = num.get(0).get("version")+1
		
		if(StringUtils.isEmpty(id))
			return [code:0];
		advertising().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
						new BasicDBObject(
								"title":advertising_title,
								"pic":advertising_pic,
								"url":advertising_url,
								"countdown" : advertising_countdown,
								"is_show" : advertising_is_show,
								"update_time" : System.currentTimeMillis(),
								"version" : advertising_version
						)
				));
		Crud.opLog("advertising",[edit:id]);
		return OK();
	}

	
	/**
	 * 删除
	 
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
		Crud.opLog("liveclass",[del:id]);
		return OK();
	}
	*/
	
	
}
