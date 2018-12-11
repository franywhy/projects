package com.izhubo.admin
import static com.izhubo.rest.common.util.WebUtils.$$
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
/** * @Description: 学员就业登记管理 * @author zbm   * @date 2015年1月28日 下午2:34:32 */@RestWithSession
class EmploymentRegistrationController extends BaseController{
	@Resource	KGS baseKGS;	DBCollection table(){mainMongo.getCollection('employment_registration')}		DBCollection _admins(){		return adminMongo.getCollection('admins');	}     
	DBCollection _payOrder(){
		return mainMongo.getCollection('pay_order');
	}
	
	DBCollection _users(){
		return mainMongo.getCollection('users');
	}	//学员就业登记列表显示	def list(HttpServletRequest req){		def query = QueryBuilder.start()		def bill_code = req.getParameter("bill_code");		if (StringUtils.isNotBlank(bill_code)) {			Pattern pattern = Pattern.compile("^.*" + bill_code + ".*\$", Pattern.CASE_INSENSITIVE)			query.and("bill_code").regex(pattern)		}		intQuery(query,req,"teacher_id");
		stringQuery(query,req,"statement_state");
		def statement_state=req.getParameter("statement_state");
		if (StringUtils.isNotBlank(statement_state)){ 
			query.and("statement_state").is(statement_state.equals("true")?true:false);
		}		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->			//id-->name			for(BasicDBObject obj: data){
				Integer employment_user_id = obj['user_id']as Integer;
				Integer teacher_id = obj['teacher_id']as Integer;				Integer user_id = obj['manage_info']['create_user_id'] as Integer;				Integer update_id = obj['manage_info']['update_user_id'] as Integer;
				if(employment_user_id){
					obj.put("employment_user_name", _users().findOne(new BasicDBObject("_id" : employment_user_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
								if(teacher_id){
					obj.put("teacher_name", _users().findOne(new BasicDBObject("_id" : teacher_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
								if(user_id){					obj.put("user_name", _admins().findOne(new BasicDBObject("_id" : user_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));				}
								if(update_id){					obj.put("update_name", _admins().findOne(new BasicDBObject("_id" : update_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}			}					}	}			@TypeChecked(TypeCheckingMode.SKIP)	def add(HttpServletRequest request)	{		Map user = (Map) request.getSession().getAttribute("user");
		Long now = System.currentTimeMillis();		def container =request.getParameter("json");		//TODO		def employment_registration=$$("_id" : UUID.randomUUID().toString());		// 附件		employment_registration.append("attachment", request.getParameter("attachment"));		// 单据编号		employment_registration.append("bill_code", baseKGS.nextId().toString());		// 单据日期		employment_registration.append("bill_date", now);		// 订单编号		employment_registration.append("order_no", request.getParameter("order_no"));		// 结算状态		employment_registration.append("statement_state", false);		// 所属就业老师		employment_registration.append("teacher_id", request.getParameter("teacher_id") as Integer);		// 选择学员		employment_registration.append("user_id", request.getParameter("user_id") as Integer);		//获取前台传过来的数据集合		JSONObject jobj = JSONUtil.jsonToMap(container);		def emplist=jobj.get("emplist");		def teachidlist=jobj.get("teachidlist");		def positionlist=jobj.get("positionlist");		def salarylist=jobj.get("salarylist");		def memolist=jobj.get("memolist");		List employment_details =new ArrayList();		for (int i=0;i<emplist.size();i++){			BasicDBObject employment_detail=new BasicDBObject();			def employers=emplist.get(i);			def teacher_id=teachidlist.get(i);			def position=positionlist.get(i);			def salary=salarylist.get(i);			def memo=memolist.get(i);					   employment_detail.put("employers" , employers);		   employment_detail.put("teacher_id" , teacher_id as Integer);		   employment_detail.put("position" , position);		   employment_detail.put("salary" , salary);		   employment_detail.put("memo" , memo);		   employment_details.add(employment_detail);		}		employment_registration.append("employment_details", employment_details);		//管理信息		Map manage_info = new HashMap();		//创建人id		manage_info.put("create_user_id",user.get("_id") as Integer);		//创建日期		manage_info.put("timestamp",now);		//修改人Id		manage_info.put("update_user_id",user.get("_id") as Integer);		//修改日期		manage_info.put("update_date",now);		//提交标记		manage_info.put("upload_flag" , false);		//审核标记		manage_info.put("audit_flag" , false);		employment_registration.append("manage_info", manage_info);		table().save(employment_registration);		Crud.opLog("employment_registration",[save:request["_id"]]);		return OK();	}	
	
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
	}	@TypeChecked(TypeCheckingMode.SKIP)	def edit(HttpServletRequest request)	{		Map user = (Map) request.getSession().getAttribute("user");		def container =request.getParameter("json");		BasicDBObject employment_registration=new BasicDBObject();		// 附件		employment_registration.append("attachment", request.getParameter("attachment"));		// 订单编号		employment_registration.append("order_no", request.getParameter("order_no"));		// 所属就业老师		employment_registration.append("teacher_id", request.getParameter("teacher_id") as Integer);		// 选择学员		employment_registration.append("user_id", request.getParameter("user_id") as Integer);		//获取前台传过来的数据集合		JSONObject jobj = JSONUtil.jsonToMap(container);		def emplist=jobj.get("emplist");		def teachidlist=jobj.get("teachidlist");		def positionlist=jobj.get("positionlist");		def salarylist=jobj.get("salarylist");		def memolist=jobj.get("memolist");		List employment_details =new ArrayList();		for (int i=0;i<emplist.size();i++){			BasicDBObject employment_detail=new BasicDBObject();			def employers=emplist.get(i);			def teacher_id=teachidlist.get(i);			def position=positionlist.get(i);			def salary=salarylist.get(i);			def memo=memolist.get(i);					   employment_detail.put("employers" , employers);		   employment_detail.put("teacher_id" , teacher_id as Integer);		   employment_detail.put("position" , position);		   employment_detail.put("salary" , salary);		   employment_detail.put("memo" , memo);		   employment_details.add(employment_detail);		}		employment_registration.append("employment_details", employment_details);		//管理信息		Long now = System.currentTimeMillis();		//修改人Id		employment_registration.put("manage_info.update_user_id",user.get("_id") as Integer);		//修改日期		employment_registration.put("manage_info.update_date",now);		String _id = request.getParameter("_id");		table().update(new BasicDBObject("_id":request.getParameter("_id")),new BasicDBObject('$set':employment_registration));		Crud.opLog("employment_registration",[update:request["_id"]]);		return OK();	}	def del(HttpServletRequest req){		String id = req[_id]		if(StringUtils.isEmpty(id))			return [code:0]		table().remove(new BasicDBObject(_id,id))		Crud.opLog("employment_registration",[del:id]);		return OK();	}		/**
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
