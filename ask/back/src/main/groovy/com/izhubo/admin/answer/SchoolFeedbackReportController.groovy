package com.izhubo.admin.answer



import static com.izhubo.rest.common.doc.MongoKey.$setOnInsert
import static com.izhubo.rest.common.doc.MongoKey._id
import static com.izhubo.rest.common.doc.MongoKey.timestamp
import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.servlet.http.HttpServletRequest

import com.izhubo.admin.BaseController
import com.izhubo.rest.anno.RestWithSession
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject


/**
 * 校区反馈报表
 * @author 
 *
 */
@RestWithSession
class SchoolFeedbackReportController extends BaseController {

	public DBCollection school_feedback(){
		return mainMongo.getCollection("school_feedback");
	}

	/**
	 * 反馈列表
	 * @param req
	 * @return
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def list(HttpServletRequest req){
		def result = school_feedback().find().toArray();
		return getResultOKS(result);
	}
	
}

