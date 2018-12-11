package com.izhubo.admin

import static com.izhubo.rest.common.doc.MongoKey.ALL_FIELD
import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils
import com.mongodb.BasicDBObject
import com.izhubo.model.ApplyType
import com.izhubo.model.OpType
import com.izhubo.model.UserType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder
import java.util.regex.Pattern
/**
 * date: 13-3-28 下午2:31
 * @author: hzj
 */
@RestWithSession
class MainTypeController extends BaseController{

	@Resource
	KGS maintypeKGS;
	
    DBCollection table(){
		mainMongo.getCollection('main_type')
	}
	
	DBCollection professional(){
		mainMongo.getCollection('professional')
	}

    // 频道档案查询
    def list(HttpServletRequest req){
    	QueryBuilder query = QueryBuilder.start();
		String main_type_name = req.getParameter("main_type_name");		
		if(StringUtils.isNotBlank(main_type_name)){
			Pattern pattern = Pattern.compile("^.*" + main_type_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("main_type_name").regex(pattern)
		}		
		intQuery(query,req,'main_type_code')
		Crud.list(req,table(),query.get(),ALL_FIELD, MongoKey.SJ_DESC);
	}
	
	def edit(HttpServletRequest req){
		String _id = req.getParameter("_id");
		String main_type_name = req.getParameter("main_type_name");
		def main_type = $$("main_type_name", main_type_name);
		table().update(new BasicDBObject("_id":_id),new BasicDBObject('$set':main_type))
		Crud.opLog(OpType.main_type,[update:_id]);
		return OK();
	}
	//
	def del(HttpServletRequest req){
		String id = req[_id]			
		String main_type_code  = table().findOne(new BasicDBObject("_id":id),new BasicDBObject("main_type_code":1))?.get("main_type_code")		
		def professionalObj = professional().findOne(new BasicDBObject('mainTypeCode',main_type_code));
		if(professionalObj==null){
			table().remove(new BasicDBObject(_id,id))
			Crud.opLog(OpType.main_type,[del:id]);
			return OK();
		}
	}
	//
	def add(HttpServletRequest req){
		String main_type_name = req.getParameter("main_type_name");	
		def main_type = $$("_id":UUID.randomUUID().toString());
		main_type.append("main_type_code", maintypeKGS.nextId());
		main_type.append("main_type_name", main_type_name);
		table().save(main_type);
		Crud.opLog(OpType.main_type,[save:req["_id"]]);
		return OK();
	}
	/**
	 *  db.users.find() select * from users
		db.users.find({"age" : 27}) select * from users where age = 27
	*/	
}
