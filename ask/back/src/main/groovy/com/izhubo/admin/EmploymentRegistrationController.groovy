package com.izhubo.admin

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import net.sf.json.JSONObject

import org.apache.commons.lang.StringUtils

import com.izhubo.model.UserType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.common.util.JSONUtil
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder

class EmploymentRegistrationController extends BaseController{
	@Resource
	DBCollection _payOrder(){
		return mainMongo.getCollection('pay_order');
	}
	
	DBCollection _users(){
		return mainMongo.getCollection('users');
	}
		stringQuery(query,req,"statement_state");
		def statement_state=req.getParameter("statement_state");
		if (StringUtils.isNotBlank(statement_state)){ 
			query.and("statement_state").is(statement_state.equals("true")?true:false);
		}
				Integer employment_user_id = obj['user_id']as Integer;
				Integer teacher_id = obj['teacher_id']as Integer;
				if(employment_user_id){
					obj.put("employment_user_name", _users().findOne(new BasicDBObject("_id" : employment_user_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
				
					obj.put("teacher_name", _users().findOne(new BasicDBObject("_id" : teacher_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
				
				
				}
		Long now = System.currentTimeMillis();
	
	//教师下拉框列表
	def teacher_list(HttpServletRequest req){
		Map user = req.getSession().getAttribute("user") as Map;
		if(user){ //暂时不区分公司权限 "company_id" : user.get("company_id").toString()
			List<DBObject> list = _users().find($$("priv" : UserType.主播.ordinal()),
				$$("_id" : 1 , "nick_name" : 1)).sort($$(_id : 1)).limit(1000).toArray();
			return ["code" : 1, "data" : list];
		}
		return null;

	}
	
	//订单列表
	def order_list(HttpServletRequest req){   //
		String search=req.getParameter("search");
		Pattern pattern;
		def query;
		def user_id =req.getParameter("user_id");
		if (StringUtils.isNotBlank(search)) {
			//启用不区分大小写的匹配。
			 pattern = Pattern.compile("^.*" + search + ".*\$", Pattern.CASE_INSENSITIVE)
			 //查询条件
			  query = $$(
					 $or : [
							$$( "item_name" ,   pattern),
							$$( "order_no" ,   pattern)
							]
					 );
		   }else{
		      query= new BasicDBObject()
		    }
				 query.put("user_id",user_id as Integer);
			Crud.list(req,_payOrder(),query,MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
				for(BasicDBObject obj: data){
					Integer user_ids = obj['user_id']as Integer;
					if(user_ids){
						obj.put("user_name", _users().findOne(new BasicDBObject("_id" : user_ids as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
					}
				 }
				
			}
//		} else {
//			def query = QueryBuilder.start()
//			query.and("user_id").is(user_id);
//			Crud.list(req,_payOrder(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC)
//		}
	}
		
	//学员下拉框列表
	@TypeChecked(TypeCheckingMode.SKIP)
	def user_list(HttpServletRequest req){   //TODO 修改UserType类结构
		Map user = req.getSession().getAttribute("user") as Map;
		String course_id=req.getParameter("course_id");
		List<DBObject> list = _payOrder().find(null,$$("user_id" : 1)).toArray();
		List<Integer> list_new=new ArrayList<Integer>();
		for(DBObject dbo :list){
			if(!list_new.contains(dbo['user_id'])){
				list_new.add(dbo['user_id']);
			}
		}
		String search=req.getParameter("search");
		def query = QueryBuilder.start()
			query.and("priv").is(UserType.普通用户.ordinal());
			query.and("_id").in(list_new);
		if (StringUtils.isNotBlank(search)) {
			//启用不区分大小写的匹配。
			Pattern	 pattern = Pattern.compile("^.*" + search + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nick_name").regex(pattern)
		}
		Crud.list(req,_users(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC)
	}
	 * 提交
	 */	
	@TypeChecked(TypeCheckingMode.SKIP)
	def submit(HttpServletRequest request){
		Map user = (Map) request.getSession().getAttribute("user");
		if(user == null){
			return OK();
		}
	   table().update(
		   new BasicDBObject("_id":request.getParameter("_id") , "manage_info.upload_flag" : false),
		   new BasicDBObject('$set':
			   new BasicDBObject(
				   "manage_info.upload_flag" : true,
				   //提交人
				   "manage_info.upload_user_id": user.get("_id") as Integer,
				   "manage_info.upload_date" : System.currentTimeMillis()
				   )
			   ));
	   return OK();
	}
	/**
	 * 收回
	 */
	def recovery(HttpServletRequest request){
		Map user = (Map) request.getSession().getAttribute("user");
		if(user == null){
			return OK();
		}
		table().update(
			new BasicDBObject("_id":request.getParameter("_id") , "manage_info.upload_flag" : true),
			new BasicDBObject('$set':new BasicDBObject("manage_info.upload_flag" : false,)
				));
		return OK();
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
		return OK();
	}
	
	/**
	 * 反审核
	 */
	def reaudit(HttpServletRequest request){
		Map user = (Map) request.getSession().getAttribute("user");
		if(user == null){
			return OK();
		}
		
		def _id =request.getParameter("_id");
		if(isUse(_id)){
			return [code : 0 , msg : '数据已被引用，无法反审核！取消引用才可操作！'];
		}
		
		//TODO
		table().update(
			new BasicDBObject("_id":_id, "manage_info.audit_flag" : true),
			new BasicDBObject('$set':
				new BasicDBObject( 
					"manage_info.audit_flag" : false,
					)
				));
		return OK();
	}
	
	//是否被引用
	private Boolean isUse(String _id){
		Boolean isUse = false;
		DBObject employment = table().findOne(new BasicDBObject("_id" : _id));
		boolean statement_state=employment.get("statement_state");
		if(statement_state){
			isUse = true;
		}
		return isUse;
	}
}