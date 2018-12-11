package com.izhubo.admin

import static com.izhubo.rest.common.doc.MongoKey.*
import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.servlet.http.HttpServletRequest

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
/**
 * 上门服务
 * @author Administrator
 *
 */
@RestWithSession
class ServiceHomeController extends BaseController {

	DBCollection table(){
		return mainMongo.getCollection('service_home');
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	def list(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		//公司名称
//		stringQuery(query,req,'company_name');
		stringParameterQuery(query, req, "company_name");
		//联系人名称
//		stringQuery(query,req,'contacts_name');
		stringParameterQuery(query, req, "contacts_name");
		//联系电话
//		stringQuery(query,req,'contacts_phone');
		stringParameterQuery(query, req, "contacts_phone");
		
		//提交用户ID
		stringQuery(query,req,'user_id');
		Crud.list(req , table() , query.get() , null , SJ_DESC){List<BasicDBObject> bdata->
			for(BasicDBObject data: bdata){
				data["nick_name"] = users().findOne($$("_id" : data["user_id"] ), $$("nick_name" : 1 ))?.get("nick_name");
			}
				
		}
	}
	
}
