package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeCheckingMode
import groovy.transform.TypeChecked;

import java.text.DateFormat
import java.text.SimpleDateFormat;
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
 * 报名表
 * @author zhengxin
 * 2016-05-12
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class SignsController extends BaseController {


	DBCollection table(){
		return mainMongo.getCollection('signs');
	}
	
	static final String DFMT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		SimpleDateFormat ddtf = new SimpleDateFormat(DFMT);
		def query = Web.fillTimeBetween(req);
		//stringQuery(query,req,'nick_name')
		def user_name = req.getParameter("user_name");
		def sex = req.getParameter("sex");
		def code = req.getParameter("code");
		def school_code = req.getParameter("school_code");
		def school_name = req.getParameter("school_name");
		def class_type = req.getParameter("class_type");
		def order_num = req.getParameter("order_num");
		def pay_type = req.getParameter("pay_type");
		def sign_status = req.getParameter("sign_status");
		def status = req.getParameter("status");
		def spredict_open_time = req.getParameter("spredict_open_time");
		def epredict_open_time = req.getParameter("epredict_open_time");
		def screate_time = req.getParameter("screate_time");
		def ecreate_time = req.getParameter("ecreate_time");
		if(spredict_open_time != null || epredict_open_time != null){
			query = Web.fillTimeBetween(query,"predict_open_time",new SimpleDateFormat(DFMT).parse(spredict_open_time),new SimpleDateFormat(DFMT).parse(epredict_open_time));
		}
		if(screate_time != null || ecreate_time != null){
			query = Web.fillTimeBetween(query,"create_time",new SimpleDateFormat(DFMT).parse(screate_time),new SimpleDateFormat(DFMT).parse(ecreate_time));
		}
		if (StringUtils.isNotBlank(user_name)){
			Pattern pattern = Pattern.compile("^.*" + user_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("user_name").regex(pattern)
		}
		if (StringUtils.isNotBlank(sex)){
			sex = sex as Integer;
			query.and("sex").is(sex);
		}
		if (StringUtils.isNotBlank(code)){
			Pattern pattern = Pattern.compile("^.*" + code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("code").regex(pattern)
		}
		if (StringUtils.isNotBlank(school_code)){
			Pattern pattern = Pattern.compile("^.*" + school_code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("school_code").regex(pattern)
		}
		if (StringUtils.isNotBlank(school_name)){
			Pattern pattern = Pattern.compile("^.*" + school_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("school_name").regex(pattern)
		}
		if (StringUtils.isNotBlank(class_type)){
			class_type = class_type as Integer;
			query.and("class_type").is(class_type);
		}
		if (StringUtils.isNotBlank(order_num)){
			Pattern pattern = Pattern.compile("^.*" + order_num + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("order_num").regex(pattern)
		}
		if (StringUtils.isNotBlank(pay_type)){
			pay_type = pay_type as Integer;
			query.and("pay_type").is(pay_type);
		}
		if (StringUtils.isNotBlank(sign_status)){
			sign_status = sign_status as Integer;
			query.and("sign_status").is(sign_status);
		}
		if (StringUtils.isNotBlank(status)){
			status = status as Integer;
			query.and("status").is(status);
		}
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			for(BasicDBObject obj: data){
				if (obj['syn_time'] != null){
					obj.put("syn_time", ddtf.format(new Date(obj['syn_time'])));
				}
				if (obj['create_time'] != null){
					obj.put("create_time", ddtf.format(new Date(obj['create_time'])));
				}
				if (obj['predict_open_time'] != null){
					obj.put("predict_open_time", ddtf.format(new Date(obj['predict_open_time'])));
				}
				if (obj['status'] != null){
					status = Integer.valueOf(obj['status']);
					switch(status){
						case 1:obj.put("status", "在读");break; 
						case 2:obj.put("status", "休学");break; 
						case 3:obj.put("status", "毕业");break; 
						case 4:obj.put("status", "暂停");break; 
						case 6:obj.put("status", "坏账");break; 
						default:obj.put("status", "无");
					}
				}
				if (obj['sex'] != null){
					sex = Integer.valueOf(obj['sex']);
					switch(sex){
						case 1:obj.put("sex", "男");break; 
						case 2:obj.put("sex", "女");break; 
						default:obj.put("sex", "无");
					}
				}
			}
		};
	}
	

}
