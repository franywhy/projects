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
 * 总裁语录
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class BossAnaController extends BaseController {


	DBCollection table(){
		return mainMongo.getCollection('boss_ana');
	}
	
	DBCollection boss_record(){
		return mainMongo.getCollection('boss_record');
	}
	
	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		Crud.list(req,table(),null,MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			for(BasicDBObject obj: data){
				if (obj['boss_id'] != null){
					def br = boss_record().findOne($$("_id" : obj['boss_id'] ));
					if(br){
						obj["boss_name"] = br["boss_name"];
						obj["boss_pic"] = br["boss_pic"];
						obj["boss_info"] = br["boss_info"];
					}
				}
			}
		};
	}

	/**
	 * 新增
	 */
	def add(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");

		def ta = $$("_id" : UUID.randomUUID().toString());
		ta.put("boss_ana_title", req["boss_ana_title"]);
		ta.put("boss_ana_info", req["boss_ana_info"]);
		ta.put("boss_id", req["boss_id"]);
		ta.put("create_user_id", user.get("_id") as Integer);
		ta.put("timestamp", System.currentTimeMillis());
		ta.put("update_user_id", user.get("_id") as Integer);
		ta.put("update_time", System.currentTimeMillis());
		table().save(ta);
		Crud.opLog("boss_ana",[save:ta["_id"]]);
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
						"boss_ana_title" : req["boss_ana_title"],
						"boss_ana_info" : req["boss_ana_info"],
						"boss_id" : req["boss_id"],
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis()
					))
				);
		Crud.opLog("boss_ana",[edit:id]);
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
		Crud.opLog("boss_ana",[del:id]);
		return OK();
	}
	
	/**
	 * 总裁列表
	 * @param req
	 * @return
	 */
	def bossList(HttpServletRequest req){
		def bl = boss_record().find().sort(MongoKey.SJ_DESC).limit(100).toArray();
		return [code : 1 , data : bl];
	}
}
