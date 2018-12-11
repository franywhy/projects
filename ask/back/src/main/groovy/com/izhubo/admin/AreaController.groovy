package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeCheckingMode
import groovy.transform.TypeChecked;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection

/**
 * 校区列表
 * @author zhengxin
 * 2016-03-09
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class AreaController extends BaseController {


	DBCollection table(){
		return hqmainMongo.getCollection('area');
	}
	DBCollection area(){
		return hqmainMongo.getCollection('area');
	}
	
	static final String DFMT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		//stringQuery(query,req,'nick_name')
		String code = req.getParameter("code");
		String name = req.getParameter("name");
		String address = req.getParameter("address");
		String header = req.getParameter("handler");
		String level = req.getParameter("level");
		String ssyn_time = req.getParameter("ssyn_time");
		String esyn_time = req.getParameter("esyn_time");
		if(ssyn_time != null || esyn_time != null){
			query = Web.fillTimeBetween(query,"syn_time",new SimpleDateFormat(DFMT).parse(ssyn_time),new SimpleDateFormat(DFMT).parse(esyn_time));
		}
		if (StringUtils.isNotBlank(code)){
			Pattern pattern = Pattern.compile("^.*" + code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("code").regex(pattern)
		}
		if (StringUtils.isNotBlank(name)){
			Pattern pattern = Pattern.compile("^.*" + name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("name").regex(pattern)
		}
		/*if (StringUtils.isNotBlank(querysex)){
			if(Integer.valueOf(querysex) == 0){
				query.or(new BasicDBObject("sex", 0), new BasicDBObject("sex", null));
			}else{
				query.and("sex").is(Integer.valueOf(querysex));
			}
		}*/
		if (StringUtils.isNotBlank(address)){
			Pattern pattern = Pattern.compile("^.*" + address + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("address").regex(pattern)
		}
		if (StringUtils.isNotBlank(header)){
			Pattern pattern = Pattern.compile("^.*" + header + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("header").regex(pattern)
		}
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			for(BasicDBObject obj: data){
				obj.put("parent_nc_name", area().findOne(new BasicDBObject("nc_id":obj['parent_nc_id']),new BasicDBObject("name",1))?.get("name"))
				/*def sex = obj.get("sex");
				if(sex && sex==1){
					obj.put("sex","男")
				}else if(sex==2){
					obj.put("sex","女")
				}else{
					obj.put("sex","保密")
				}*/
			}
		};
	}

	/**
	 * 新增
	 */
	/*def add(HttpServletRequest req){
		String birthday = req["birthday"]

		def users = $$("_id" : UUID.randomUUID().toString());
		if(req["sex"] && req["sex"] != ""){
			users.put("sex", req["sex"] as Integer);
		}
		users.put("nick_name", req["nick_name"]);
		users.put("cardnumber", req["cardnumber"]);
		users.put("telephone", req["telephone"]);
		users.put("email", req["email"]);
		users.put("qq_number", req["qq_number"]);
		users.put("school_code", req["school_code"]);
		users.put("status", 0);
		if(birthday){
			users.put("birthday", new SimpleDateFormat(DFMT).parse(birthday).getTime());
		}
		table().save(users);
		Crud.opLog("users",[save:users["_id"]]);
		return OK();
	}*/

	/**
	 * 修改
	 */
	/*def edit(HttpServletRequest req){
		String id = req[_id];
		String nick_name = req.getParameter("nick_name");
		
		if(StringUtils.isEmpty(id))
			return [code:0];
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"nick_name" : nick_name,
						"update_time" : System.currentTimeMillis()
					)
				));
			
			Crud.opLog("users",[edit:id]);
		return OK();
	}*/

	/**
	 * 冻结/解冻
	 */
	/*def freeze(HttpServletRequest req){
		
		String id = req[_id]
		def status = req.getParameter("status")
		if(status && status == "正常"){
			status = 1
		}else{
			status = 0
		}
		
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().update(
				new BasicDBObject("_id":id),
				new BasicDBObject('$set':
					new BasicDBObject(
						"status" : status,
						"update_time" : System.currentTimeMillis()
					)
				));
		Crud.opLog("users",[edit:id]);
		return OK();
	}*/
}
