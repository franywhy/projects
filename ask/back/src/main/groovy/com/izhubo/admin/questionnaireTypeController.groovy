
package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$

import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils

import com.izhubo.model.OpType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder

	  /**
	   * @Description: 问卷调查类型档案管理
	   * @author zbm  
	   * @date 2015年1月19日 上午11:21:37
	   */

@RestWithSession
class questionnaireTypeController extends BaseController{
	@Resource
	KGS questionnaireTypeKGS;
	DBCollection table(){mainMongo.getCollection('questionnaire_type')}

	//答疑类型列表显示
	def list(HttpServletRequest req){
		def query = QueryBuilder.start()
		def type_code = req.getParameter("type_code");
		if (StringUtils.isNotBlank(type_code)) {
			Pattern pattern = Pattern.compile("^.*" + type_code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("type_code").regex(pattern)
		}
		  def type_name = req.getParameter("type_name");
		  if (StringUtils.isNotBlank(type_name)) {
			  Pattern pattern = Pattern.compile("^.*" + type_name + ".*\$", Pattern.CASE_INSENSITIVE)
			  query.and("type_name").regex(pattern)
		  }
		 intQuery(query,req,'class')
		Crud.list(req,table(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC)
	}
	
	
	def add(HttpServletRequest request)
	{
		def questionnaire_type=$$("_id" : UUID.randomUUID().toString());
		Long now = System.currentTimeMillis();
		// 类型编号
		questionnaire_type.append("type_code", questionnaireTypeKGS.nextId().toString());
		// 类型名称
		questionnaire_type.append("type_name", request.getParameter("type_name"));
		// 种类
		questionnaire_type.append("class", request.getParameter("class") as Integer);
		// 描述
		questionnaire_type.append("memo", request.getParameter("memo"));
		//创建时间
		questionnaire_type.append("timestamp", now);
		//删除标记
		questionnaire_type.append("del", false);
		table().save(questionnaire_type);
		Crud.opLog(OpType.questionnaire_type,[save:request["_id"]]);
		return OK();
	}

	def edit(HttpServletRequest request)
	{
		BasicDBObject questionnaire_type=new BasicDBObject();
		questionnaire_type.append("type_name", request.getParameter("type_name"));
		questionnaire_type.append("class", request.getParameter("class") as Integer);
		questionnaire_type.append("memo", request.getParameter("memo"));
		String _id = request.getParameter("_id");
		table().update(new BasicDBObject("_id":request.getParameter("_id")),new BasicDBObject('$set':questionnaire_type));
		Crud.opLog(OpType.questionnaire_type,[update:request["_id"]]);
		return OK();
	}

	def del(HttpServletRequest req){
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		table().remove(new BasicDBObject(_id,id))
		Crud.opLog(OpType.questionnaire_type,[del:id]);
		return OK();
	}
 }


