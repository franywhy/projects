package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$

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
import com.mongodb.QueryBuilder


@RestWithSession
class AnswerTypeController extends BaseController{
	
	@Resource
	KGS answerTypeKGS;
	DBCollection table(){mainMongo.getCollection('answer_type')}

	//答疑类型列表显示
	def list(HttpServletRequest req){
		def query = QueryBuilder.start()
		  //stringQuery(query,req,'answer_type_code')
		 // stringQuery(query,req,'answer_type_name')
		
		def answer_type_code = req.getParameter("answer_type_code");
		if (StringUtils.isNotBlank(answer_type_code)) {
			Pattern pattern = Pattern.compile("^.*" + answer_type_code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("answer_type_code").regex(pattern)
		}
		  def type_name = req.getParameter("answer_type_name");
		  if (StringUtils.isNotBlank(type_name)) {
			  Pattern pattern = Pattern.compile("^.*" + type_name + ".*\$", Pattern.CASE_INSENSITIVE)
			  query.and("answer_type_name").regex(pattern)
		  }
		 intQuery(query,req,'map_info') 
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC)
	}
	
	
	def add(HttpServletRequest request)
	{
		def answer_type=$$("_id" : UUID.randomUUID().toString());
		// 类型编号
		answer_type.append("answer_type_code", answerTypeKGS.nextId().toString());
		//  类型名称
		answer_type.append("answer_type_name", request.getParameter("answer_type_name"));
		//  关联信息
		answer_type.append("map_info", request.getParameter("map_info"));
		table().save(answer_type);
		Crud.opLog("answer_type",[save:request["_id"]]);
		return OK();
	}

	def edit(HttpServletRequest request)
	{
		BasicDBObject answer_type=new BasicDBObject();		
		answer_type.append("answer_type_name", request.getParameter("answer_type_name"));
		answer_type.append("map_info", request.getParameter("map_info"));
		String _id = request.getParameter("_id");
		table().update(new BasicDBObject("_id":request.getParameter("_id")),new BasicDBObject('$set':answer_type));
		Crud.opLog("answer_type",[update:request["_id"]]);
		return OK();
	}

	def del(HttpServletRequest req){
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().remove(new BasicDBObject(_id,id))
		Crud.opLog("answer_type",[del:id]);
		return OK();
	}
}
