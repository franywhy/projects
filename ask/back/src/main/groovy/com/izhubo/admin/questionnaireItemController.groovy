
package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import net.sf.json.JSONObject

import org.apache.commons.lang.StringUtils

import com.izhubo.model.OpType
import com.izhubo.model.UserType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.common.util.*
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder

      /**
       * @Description: 问卷调查项目管理
       * @author zbm  
       * @date 2015年1月22日 上午10:30:14
       */
@RestWithSession
class questionnaireItemController extends BaseController{
	@Resource
	KGS questionnaireTypeKGS;
	DBCollection table(){mainMongo.getCollection('questionnaire_item')}
	
	DBCollection _admins(){
		return adminMongo.getCollection('admins');
	}

	//问卷调查项目列表显示
	def list(HttpServletRequest req){
		def query = QueryBuilder.start()
		def item_code = req.getParameter("item_code");
		if (StringUtils.isNotBlank(item_code)) {
			Pattern pattern = Pattern.compile("^.*" + item_code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("item_code").regex(pattern)
		}
		  def item_name = req.getParameter("item_name");
		  if (StringUtils.isNotBlank(item_name)) {
			  Pattern pattern = Pattern.compile("^.*" + item_name + ".*\$", Pattern.CASE_INSENSITIVE)
			  query.and("item_name").regex(pattern)
		  }
		 intQuery(query,req,'class')
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
	
	//科目下拉框列表
	def questionnaire_list(HttpServletRequest req){
		Map user = req.getSession().getAttribute("user") as Map;
		if(user){
			List<DBObject> list = mainMongo.getCollection('questionnaire_type')
			.find(null,$$("_id" : 1 ,"type_code" : 1 , "type_name" : 1))
			.sort($$(_id : 1)).limit(1000).toArray();
			return ["code" : 1, "data" : list];
		}
		return null;
	}
	
	@TypeChecked(TypeCheckingMode.SKIP)
	def add(HttpServletRequest request)
	{   
		Map user = (Map) request.getSession().getAttribute("user");
		def container =request.getParameter("json");
		//TODO
		def questionnaire_item=$$("_id" : UUID.randomUUID().toString());
		// 编号
		questionnaire_item.append("item_code", questionnaireTypeKGS.nextId().toString());
		// 名称
		questionnaire_item.append("item_name", request.getParameter("item_name"));
		// 评价类型Id
		questionnaire_item.append("type_id", request.getParameter("type_id"));
		//{"codelist":[["123"],["123"]],"namelist":[["123"],["123"]]}
		JSONObject jobj = JSONUtil.jsonToMap(container);
		def orderlist=jobj.get("orderlist");
		def codelist=jobj.get("codelist");
		def namelist=jobj.get("namelist");
		BasicDBList questionnaire_options =new ArrayList();
        for (int i=0;i<codelist.size();i++){
			BasicDBObject questionnaire_option=new BasicDBObject();
			def order=orderlist.get(i);
		    def options_code=codelist.get(i);
		    def options_name=namelist.get(i);
		   questionnaire_option.put("order" , order as Integer);
		   questionnaire_option.put("options_code" , options_code);
		   questionnaire_option.put("options_name" , options_name);
		   questionnaire_options.add(questionnaire_option);
		}
		questionnaire_item.append("questionnaire_option", chapter_sort(questionnaire_options));	
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
		questionnaire_item.append("manage_info", manage_info);
		table().save(questionnaire_item);
		Crud.opLog(OpType.questionnaire_item,[save:request["_id"]]);
		return OK();
	}
	
	@TypeChecked(TypeCheckingMode.SKIP)
	def edit(HttpServletRequest request)
	{   
		Map user = (Map) request.getSession().getAttribute("user");
		BasicDBObject questionnaire_item=new BasicDBObject();
		// 名称
		questionnaire_item.append("item_name", request.getParameter("item_name"));
		// 评价类型Id
		questionnaire_item.append("type_id", request.getParameter("type_id"));
		//项目标题内容
		def container =request.getParameter("json");
		JSONObject jobj = JSONUtil.jsonToMap(container);
		def orderlist=jobj.get("orderlist");
		def codelist=jobj.get("codelist");
		def namelist=jobj.get("namelist");
		BasicDBList questionnaire_options =new ArrayList();
		for (int i=0;i<codelist.size();i++){
			BasicDBObject questionnaire_option=new BasicDBObject();
			def options_code=codelist.get(i);
			def options_name=namelist.get(i);
			def order =orderlist.get(i);
			questionnaire_option.put("order" , order as Integer);
		   questionnaire_option.put("options_code" , options_code);
		   questionnaire_option.put("options_name" , options_name);
		   questionnaire_options.add(questionnaire_option);
		}
		questionnaire_item.append("questionnaire_option", chapter_sort(questionnaire_options));
			
		//管理信息
		Long now = System.currentTimeMillis();
		//修改人Id
		questionnaire_item.put("manage_info.update_user_id",user.get("_id") as Integer);
		//修改日期
		questionnaire_item.put("manage_info.update_date",now);
		String _id = request.getParameter("_id");
		table().update(new BasicDBObject("_id":request.getParameter("_id")),new BasicDBObject('$set':questionnaire_item));
		Crud.opLog(OpType.questionnaire_item,[update:request["_id"]]);
		return OK();
	}

	def del(HttpServletRequest req){
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().remove(new BasicDBObject(_id,id))
		Crud.opLog(OpType.questionnaire_item,[del:id]);
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
	
	@TypeChecked(TypeCheckingMode.SKIP)
	def chapter_sort(BasicDBList obj){
		if(obj){
			//章节 排序
			obj.sort { a, b ->
				a.order.compareTo(b.order);
			}

		}
	}	
 }


