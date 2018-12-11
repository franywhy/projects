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
 * 总裁档案
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class BossRecordController extends BaseController {


	DBCollection table(){
		return mainMongo.getCollection('boss_record');
	}
	
	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		Crud.list(req,table(),null,MongoKey.ALL_FIELD,MongoKey.SJ_DESC);
	}

	/**
	 * 新增
	 */
	def add(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");

		def boss_record = $$("_id" : UUID.randomUUID().toString());
		boss_record.put("boss_pic", req["boss_pic"]);
		boss_record.put("boss_name", req["boss_name"]);
		boss_record.put("boss_info", req["boss_info"]);
		boss_record.put("create_user_id", user.get("_id") as Integer);
		boss_record.put("timestamp", System.currentTimeMillis());
		boss_record.put("update_user_id", user.get("_id") as Integer);
		boss_record.put("update_time", System.currentTimeMillis());
		table().save(boss_record);
		Crud.opLog("boss_record",[save:boss_record["_id"]]);
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
						"boss_pic" : req["boss_pic"],
						"boss_name" : req["boss_name"],
						"boss_info" : req["boss_info"],
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis()
					))
				);
		Crud.opLog("boss_record",[edit:id]);
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
		Crud.opLog("boss_record",[del:id]);
		return OK();
	}
}
