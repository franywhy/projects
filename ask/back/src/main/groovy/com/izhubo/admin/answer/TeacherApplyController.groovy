package com.izhubo.admin.answer

import com.izhubo.rest.anno.RestWithSession;
import com.mongodb.QueryBuilder

import javax.servlet.http.HttpServletRequest;
import com.mongodb.BasicDBObject
import com.mongodb.QueryBuilder
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.Crud
import com.izhubo.admin.BaseController
import com.izhubo.model.OpType
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.common.doc.MongoKey.ALL_FIELD
import static com.izhubo.rest.common.doc.MongoKey.SJ_DESC
import com.izhubo.admin.Web

import static com.izhubo.rest.common.doc.MongoKey.*
import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import org.slf4j.Logger;

@RestWithSession
class TeacherApplyController extends BaseController {

	Logger logger = LoggerFactory.getLogger(TeacherApplyController.class)
	
		def list(HttpServletRequest req){
			QueryBuilder query = QueryBuilder.start();
			Crud.list(req,adminMongo.getCollection('teacher_apply'),query.get(),ALL_FIELD,SJ_DESC)
		}
		
		
		/**
		 * 审核
		 */
		def audit(HttpServletRequest request){
			Map user = (Map) request.getSession().getAttribute("user");
			if(user == null){
				return OK();
			}
			table().update(
				new BasicDBObject("_id":request.getParameter("_id") , "manage_info.audit_flag" : false),
				new BasicDBObject('$set':
					new BasicDBObject(
						"manage_info.audit_flag" : true,
						//提交人
						"manage_info.audit_user_id": user.get("_id") as Integer,
						"manage_info.audit_date" : System.currentTimeMillis()
						)
					));
				
			
				//新增账号或者是直接提升权限。
				
			return OK();
		}
	
}
