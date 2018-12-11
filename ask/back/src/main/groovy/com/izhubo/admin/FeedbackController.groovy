package com.izhubo.admin

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.servlet.http.HttpServletRequest

import com.izhubo.common.util.KeyUtils
import com.izhubo.rest.anno.RestWithSession;
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject

import static com.izhubo.rest.common.util.WebUtils.$$;
import static com.izhubo.rest.common.doc.MongoKey.*;
/**
 * 反馈信息
 * @author Administrator
 *
 */
@RestWithSession
class FeedbackController extends BaseController {

	def feedback(){
		return mainMongo.getCollection('feedback');
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	def list(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		Crud.list(req , feedback() , query.get() , null , SJ_DESC){List<BasicDBObject> bdata->
			for(BasicDBObject data: bdata){
				def user = users().findOne($$("_id" : data["user_id"] ), $$("nick_name" : 1 , "priv" : 1));
				if(user){
					data["nick_name"] = user["nick_name"];
					data["priv"] = user["priv"];
				}
				
			}
				
		}
	}
	
}
