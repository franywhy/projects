
package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import net.sf.json.JSONObject
import java.util.regex.Pattern
import org.apache.commons.lang.StringUtils
import static com.izhubo.rest.common.doc.MongoKey.ALL_FIELD
import com.izhubo.model.OpType
import com.izhubo.model.UserType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.common.util.*
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder

	 
@RestWithSession
class CompanySettleRatioController extends BaseController{
	
	
	DBCollection companyDB(){mainMongo.getCollection('company_settle_ratio')}
	DBCollection companyDB2(){mainMongo.getCollection('company')}
	
	def list(HttpServletRequest req){
		QueryBuilder query = QueryBuilder.start();
		stringQuery(query,req,'begin_date');
		stringQuery(query,req,'end_date');
		stringQuery(query,req,'bill_code');
	
		Crud.list(req,companyDB(),query.get(),ALL_FIELD, MongoKey.SJ_DESC){List<BasicDBObject> data->
			for(BasicDBObject obj: data){
				def company_id = obj['company_id'];					
				if(company_id){
					obj.put("company_name", companyDB2().findOne(new BasicDBObject("_id" : company_id) , new BasicDBObject("company_name":1))?.get("company_name"));
				}
				
			}
		
		};	
	}
	
	/**{"ratio":[{"funds_ratio":"132","ratio":"1312"},{"funds_ratio":"54454","ratio":"5555"}]}**/
	def add(HttpServletRequest request){
		def company =$$("_id" : UUID.randomUUID().toString());
		def bill_code = request.getParameter("bill_code");
		def company_id = request.getParameter("company_id");
		def begin_date = request.getParameter("begin_date");
		def end_date = request.getParameter("end_date");
		def month_day = request.getParameter("month_day");
		def type = request.getParameter("type");	
		def month_ratio = request.getParameter("month_ratio");	
		def year_day = request.getParameter("year_day");	
		def memo = request.getParameter("memo");
		def container = request.getParameter("json");
		def  rationMap = JSONUtil.jsonToMap(container);
		List yrs = (ArrayList)rationMap.get("year_ratio");
		
		company.put("bill_code", bill_code);
		company.put("company_id", company_id);
		company.put("begin_date", begin_date);
		company.put("end_date", end_date);
		company.put("month_day", month_day);
		company.put("type", type as Integer);
		company.put("month_ratio", month_ratio as Double);
		company.put("year_day", year_day);
		company.put("memo", memo);
		company.put("year_ratio",yrs);
		def manage_info = getCurrentUserInfo(request,"add");
		company.append("manage_info",manage_info);	
		companyDB().save(company);		
		return OK();
	}
	
	@TypeChecked(TypeCheckingMode.SKIP)
	def edit(HttpServletRequest request){
		def _id = request.getParameter("_id");
		def company =$$("bill_code" : request.getParameter("bill_code"));
		def company_id = request.getParameter("company_id");
		def begin_date = request.getParameter("begin_date");
		def end_date = request.getParameter("end_date");
		def month_day = request.getParameter("month_day");
		def type = request.getParameter("type");
		def month_ratio = request.getParameter("month_ratio");
		def year_day = request.getParameter("year_day");
		def memo = request.getParameter("memo");
		def container = request.getParameter("json");
		def  rationMap = JSONUtil.jsonToMap(container);
		List yrs = (ArrayList)rationMap.get("year_ratio");
	
		company.put("company_id", company_id);
		company.put("begin_date", begin_date);
		company.put("end_date", end_date);
		company.put("month_day", month_day);
		company.put("type", type as Integer);
		company.put("month_ratio", month_ratio as Double);
		company.put("year_day", year_day);
		company.put("memo", memo);
		company.put("year_ratio",yrs);
		
		def manage_info = getCurrentUserInfo(request,"update");
		company.append("manage_info",manage_info);		
		companyDB().update(new BasicDBObject("_id":_id),new BasicDBObject('$set':company));	
		return OK();
	}

	def del(HttpServletRequest req){
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		companyDB().remove(new BasicDBObject(_id,id))
		return OK();
	}
	

	def getCurrentUserInfo(HttpServletRequest req,String flg){
		if(flg.equals("update")){
			Map user = (Map) req.getSession().getAttribute("user");
			Map manage_info = new HashMap();
			Long now = System.currentTimeMillis();
			//创建人id
			//manage_info.put("create_user_id",user.get("_id") as Integer);
			//创建日期
			//manage_info.put("timestamp",now);
			//修改人Id
			manage_info.put("update_user_id",user.get("_id") as Integer);
			//修改日期
			manage_info.put("update_date",now);
			//提交标记
			manage_info.put("upload_flag" , false);
			//审核标记
			manage_info.put("audit_flag" , false);
			return manage_info;
		}
		if(flg.equals("add")){
			Map user = (Map) req.getSession().getAttribute("user");
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
			return manage_info;
		}
	}
	
	def property = "company_settle_ratio"; //定义表名
	
	def submit(HttpServletRequest request){
		submit1(request);
	}
	
	def rollbackSubmit(HttpServletRequest request){
		rollbackSubmit1(request)
	}
	
	def audit(HttpServletRequest request){
		audit1(request);
	}
	
	def rollbackAudit(HttpServletRequest request){
		rollbackAudit1(request);
	}
		
	
	//加盟公司
	def getCompany(HttpServletRequest request){
		List<DBObject> list = companyDB2().find(null, $$("_id":1,"company_name":1)).toArray();
		return ["code":1,"data":list];
	}

}
