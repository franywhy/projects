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
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder;

/**
 * 考前冲刺
 * @author zhengxin
 * 2016-05-24
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class LiveclassController extends BaseController {


	DBCollection table(){
		return mainMongo.getCollection('live_class');
	}
	DBCollection itemtable(){
		return mainMongo.getCollection('live_class_item');
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
		def liveclass = $$("_id" : UUID.randomUUID().toString());
		liveclass.put("name", req["name"]);
		liveclass.put("order", req["order"]);
		liveclass.put("content", req["content"]);
		liveclass.put("create_user_id", user.get("_id") as Integer);
		liveclass.put("create_time", System.currentTimeMillis());
		liveclass.put("dr", 0);
		table().save(liveclass);
		Crud.opLog("liveclass",[save:liveclass["_id"]]);
		return OK();
	}

	/**
	 * 修改
	 */
	def edit(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id];
		String name = req.getParameter("name");
		String order = req.getParameter("order");
		String content = req.getParameter("content");
		
		if(StringUtils.isEmpty(id))
			return [code:0];
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"name" : name,
						"content" : content,
						"order" : order,
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis()
					)
				));
			
			Crud.opLog("liveclass",[edit:id]);
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
		itemtable().update(
			new BasicDBObject("live_class_id":id),
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
	
	/**
	 * 查询冲刺课
	 */
	def item(HttpServletRequest req){
		SimpleDateFormat ddtf = new SimpleDateFormat(DFMT);
		def query = Web.fillTimeBetween(req);
		String live_class_id = req.getParameter("live_class_id");
		String name = req.getParameter("name");
		String teahcer_name = req.getParameter("teahcer_name");
		String sstart_time = req.getParameter("sstart_time");
		String estart_time = req.getParameter("estart_time");
		String send_time = req.getParameter("send_time");
		String eend_time = req.getParameter("eend_time");
		if(sstart_time != null || estart_time != null){
			query = Web.fillTimeBetween(query,"start_time",new SimpleDateFormat(DFMT).parse(sstart_time),new SimpleDateFormat(DFMT).parse(estart_time));
		}
		if(send_time != null || eend_time != null){
			query = Web.fillTimeBetween(query,"end_time",new SimpleDateFormat(DFMT).parse(send_time),new SimpleDateFormat(DFMT).parse(eend_time));
		}
		if (StringUtils.isNotBlank(live_class_id)){
			Pattern pattern = Pattern.compile("^.*" + live_class_id + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("live_class_id").regex(pattern)
		}
		if (StringUtils.isNotBlank(name)){
			Pattern pattern = Pattern.compile("^.*" + name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("name").regex(pattern)
		}
		if (StringUtils.isNotBlank(teahcer_name)){
			Pattern pattern = Pattern.compile("^.*" + teahcer_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("teahcer_name").regex(pattern)
		}
		query.and("dr").is(0);
		Crud.list(req,itemtable(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			for(BasicDBObject obj: data){
				if (obj['start_time'] != null){
					obj.put("start_time", ddtf.format(new Date(obj['start_time'])));
				}
				if (obj['end_time'] != null){
					obj.put("end_time", ddtf.format(new Date(obj['end_time'])));
				}
			}
		};
	}
	
	/**
	 * 新增
	 */
	def additem(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		def liveclassitem = $$("_id" : UUID.randomUUID().toString());
		String querystart_time = req.getParameter("start_time");
		Long start_time = null;
		if(StringUtils.isNotBlank(querystart_time)){
			start_time = new SimpleDateFormat(DFMT).parse(querystart_time).getTime();
			liveclassitem.put("start_time", start_time);
		}
		String queryend_time = req.getParameter("end_time");
		Long end_time = null;
		if(StringUtils.isNotBlank(queryend_time)){
			end_time = new SimpleDateFormat(DFMT).parse(queryend_time).getTime();
			liveclassitem.put("end_time", end_time);
		}
		liveclassitem.put("live_class_id", req["live_class_id"]);
		liveclassitem.put("name", req["name"]);
		liveclassitem.put("teacher_name", req["teacher_name"]);
		liveclassitem.put("pc_live_param", req["pc_live_param"]);
		liveclassitem.put("pc_playback_param", req["pc_playback_param"]);
		liveclassitem.put("app_live_param", req["app_live_param"]);
		liveclassitem.put("app_playback_param", req["app_playback_param"]);
		liveclassitem.put("content", req["content"]);
		liveclassitem.put("create_user_id", user.get("_id") as Integer);
		liveclassitem.put("create_time", System.currentTimeMillis());
		liveclassitem.put("dr", 0);
		itemtable().save(liveclassitem);
		Crud.opLog("liveclassitem",[save:liveclassitem["_id"]]);
		return OK();
	}

	/**
	 * 修改
	 */
	def edititem(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id];
		String live_class_id = req.getParameter("live_class_id");
		String name = req.getParameter("name");
		String teacher_name = req.getParameter("teacher_name");
		String querystart_time = req.getParameter("start_time");
		String queryend_time = req.getParameter("end_time");
		String pc_live_param = req.getParameter("pc_live_param");
		String pc_playback_param = req.getParameter("pc_playback_param");
		String app_live_param = req.getParameter("app_live_param");
		String app_playback_param = req.getParameter("app_playback_param");
		String content = req.getParameter("content");
		Long start_time = null;
		Long end_time = null;
		
		if(StringUtils.isNotBlank(querystart_time)){
			start_time = new SimpleDateFormat(DFMT).parse(querystart_time).getTime();
			itemtable().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"start_time" : start_time,
					)
				));
		}
		if(StringUtils.isNotBlank(queryend_time)){
			end_time = new SimpleDateFormat(DFMT).parse(queryend_time).getTime();
			itemtable().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"end_time" : end_time,
					)
				));
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0];
		itemtable().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"live_class_id" : live_class_id,
						"name" : name,
						"content" : content,
						"teacher_name" : teacher_name,
						"pc_live_param" : pc_live_param,
						"pc_playback_param" : pc_playback_param,
						"app_live_param" : app_live_param,
						"app_playback_param" : app_playback_param,
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis()
					)
				));
			
			Crud.opLog("liveclassitem",[edit:id]);
		return OK();
	}
	
	/**
	 * 删除
	 */
	def delitem(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		itemtable().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"update_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"dr" : 1
					)
				));
		Crud.opLog("liveclassitem",[del:id]);
		return OK();
	}
	

	
	/**
	 * 提交
	 */
	def submit(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id]
		def upload_flag = req.getParameter("upload_flag")
		if(StringUtils.isBlank(upload_flag)){
			upload_flag = 0
		}
		upload_flag = upload_flag as Integer
		if(upload_flag == 0){
			upload_flag = 1
		}else if(upload_flag == 1){
			return [code:500,msg:"您已经提交了！无需重复提交"]
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"upload_flag" : upload_flag,
						"update_user_id" : user.get("_id") as Integer,
						"upload_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"upload_time" : System.currentTimeMillis()
					)
				));
		Crud.opLog("liveclass",[edit:id]);
		return OK();
	}
	
	/**
	 * 反提交
	 */
	def unsubmit(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id]
		def upload_flag = req.getParameter("upload_flag")
		if(StringUtils.isBlank(upload_flag)){
			upload_flag = 1
		}
		upload_flag = upload_flag as Integer
		if(upload_flag == 1){
			upload_flag = 0
		}else if(upload_flag == 0){
			return [code:500,msg:"您已经反提交了！无需重复反提交"]
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"upload_flag" : upload_flag,
						"update_user_id" : user.get("_id") as Integer,
						"upload_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"upload_time" : System.currentTimeMillis()
					)
				));
		Crud.opLog("liveclass",[edit:id]);
		return OK();
	}
	
	/**
	 * 审核
	 */
	def audit(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id]
		def audit_flag = req.getParameter("audit_flag")
		if(StringUtils.isBlank(audit_flag)){
			audit_flag = 0
		}
		audit_flag = audit_flag as Integer
		if(audit_flag == 0){
			audit_flag = 1
		}else if(audit_flag == 1){
			return [code:500,msg:"您已经审核了！无需重复审核"]
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"audit_flag" : audit_flag,
						"update_user_id" : user.get("_id") as Integer,
						"audit_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"audit_time" : System.currentTimeMillis()
					)
				));
		Crud.opLog("liveclass",[edit:id]);
		return OK();
	}
	
	/**
	 * 反审核
	 */
	def unaudit(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");
		String id = req[_id]
		def audit_flag = req.getParameter("audit_flag")
		if(StringUtils.isBlank(audit_flag)){
			audit_flag = 1
		}
		audit_flag = audit_flag as Integer
		if(audit_flag == 1){
			audit_flag = 0
		}else if(audit_flag == 0){
			return [code:500,msg:"您已经反审核了！无需重复反审核"]
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"audit_flag" : audit_flag,
						"update_user_id" : user.get("_id") as Integer,
						"audit_user_id" : user.get("_id") as Integer,
						"update_time" : System.currentTimeMillis(),
						"audit_time" : System.currentTimeMillis()
					)
				));
		Crud.opLog("liveclass",[edit:id]);
		return OK();
	}
}
