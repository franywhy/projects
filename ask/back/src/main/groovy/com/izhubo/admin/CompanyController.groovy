package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder

/**
 * @date date: 15-1-14 下午2:31
 * @author zbm
 * 
 */
@RestWithSession
class CompanyController extends BaseController{
	
	@Resource
	KGS companyKGS;
    DBCollection table(){mainMongo.getCollection('company')}
	
	DBCollection _admins(){adminMongo.getCollection('admins')}
	DBCollection _course(){mainMongo.getCollection('course')}
    //档案管理查询
    def list(HttpServletRequest req){
		//登录用户信息
		Map<String,String> sMap = (Map<String,String>)req.getSession().getAttribute("user");
		//默认是公司人员的管理人员操作
		String company_id = sMap.get("company_id");
        def query = QueryBuilder.start()
		//获取公司id进行数据权限处理
		//query.and("_id").is(company_id);
		def company_code = req.getParameter("company_code");
		if (StringUtils.isNotBlank(company_code)) {
			Pattern pattern = Pattern.compile("^.*" + company_code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("company_code").regex(pattern)
		}
		  def company_name = req.getParameter("company_name");
		  if (StringUtils.isNotBlank(company_name)) {
			  Pattern pattern = Pattern.compile("^.*" + company_name + ".*\$", Pattern.CASE_INSENSITIVE)
			  query.and("company_name").regex(pattern)
		  }
		  intQuery(query,req,'company_type')	  
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			//id-->name
			for(BasicDBObject obj: data){ 				
				Integer user_id = obj['manage_info']['create_user_id'] as Integer;
				Integer update_id = obj['manage_info']['update_user_id'] as Integer;
				if(user_id){
					obj.put("user_name", _admins().findOne(new BasicDBObject("_id" : user_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
				if(update_id){
					obj.put("update_name", _admins().findOne(new BasicDBObject("_id" : update_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
			}
			
		}
    }
	def add(HttpServletRequest request)
	{   
	    Map user = (Map) request.getSession().getAttribute("user");
		def company=$$("_id" : UUID.randomUUID().toString());
		//通讯地址
		company.append("address", request.getParameter("address"));
		//公司图片 
		company.append("pic_url", request.getParameter("pic_url"));
		//公司编号
		company.append("company_code", companyKGS.nextId().toString());
		//公司名称
		company.append("company_name", request.getParameter("company_name"));
		//类型
		company.append("company_type", request.getParameter("company_type") as Integer);
		//公司URL
		company.append("company_url", request.getParameter("company_url"));
		//联系人
		company.append("contact", request.getParameter("contact"));
		//电子邮箱
		company.append("email", request.getParameter("email"));
		//传真
		company.append("fax", request.getParameter("fax"));
		//法人身份证号码
		company.append("legal_code", request.getParameter("legal_code"));
		//法人代表名称
		company.append("legal_name", request.getParameter("legal_name"));
		//电话
		company.append("phone", request.getParameter("phone"));
		//邮编
		company.append("zip_code", request.getParameter("zip_code"));
		
		//管理信息
		Map manage_info = new HashMap();
		Long now = System.currentTimeMillis();
		//创建人id
		manage_info.put("create_user_id",user.get("_id") as Integer);
		//创建日期
		manage_info.put("timestamp",now);
		//修改人Id
		manage_info.put("update_user_id",user.get("_id") as Integer);
		//修改日期
		manage_info.put("update_date",now);
		//提交标记
		manage_info.put("upload_flag" , false);
		//审核标记
		manage_info.put("audit_flag" , false);
		company.append("manage_info", manage_info);
		table().save(company);
		Crud.opLog("company",[save:request["_id"]]);
		return OK();
	}

	def edit(HttpServletRequest request)
	{  
		Map user = (Map) request.getSession().getAttribute("user");
		BasicDBObject company=new BasicDBObject();
		//通讯地址
		company.append("address", request.getParameter("address"));
		//公司图片
		company.append("pic_url", request.getParameter("pic_url"));
		//公司名称
		company.append("company_name", request.getParameter("company_name"));
		//类型
		company.append("company_type", request.getParameter("company_type") as Integer);
		//公司URL
		company.append("company_url", request.getParameter("company_url"));
		//联系人
		company.append("contact", request.getParameter("contact"));
		//电子邮箱
		company.append("email", request.getParameter("email"));
		//传真
		company.append("fax", request.getParameter("fax"));
		//法人身份证号码
		company.append("legal_code", request.getParameter("legal_code"));
		//法人代表名称
		company.append("legal_name", request.getParameter("legal_name"));
		//电话
		company.append("phone", request.getParameter("phone"));
		//邮编
		company.append("zip_code", request.getParameter("zip_code"));
		
		//管理信息
		Long now = System.currentTimeMillis();		
		//修改人Id
		company.put("manage_info.update_user_id",user.get("_id") as Integer);
		//修改日期
		company.put("manage_info.update_date",now);
	    String _id = request.getParameter("_id");
		table().update(new BasicDBObject("_id":request.getParameter("_id")),new BasicDBObject('$set':company));
		Crud.opLog("company",[update:request["_id"]]);
		return OK();
	}
    
	
	/**
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
//		def company =  table().findOne(new BasicDBObject("_id":_id),MongoKey.ALL_FIELD);
//		def company_id=company.get("company_id");
		//反审核暂时关闭
//		def adminList =_admins().find($$("company_id" :_id),$$("_id" : 1 , "company_id":1)).toArray();
//		def courseList =_course().find($$("company_id" :_id),$$("_id" : 1 , "company_id":1)).toArray();
//		if(adminList||courseList){
//			//return [code : 1 , data :["info":1]];
//			return [code : 0 , msg : '数据已被引用，无法反审核！取消引用才可操作！'];
//		}
		table().update(
			new BasicDBObject("_id":_id, "manage_info.audit_flag" : true),
			new BasicDBObject('$set':
				new BasicDBObject(
					"manage_info.audit_flag" : false,
					)
				));
		return OK();
	}
		
	def del(HttpServletRequest req){
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		mainMongo.getCollection('company').remove(new BasicDBObject(_id,id))
		Crud.opLog("company",[del:id]);
		return OK();
	}
}
