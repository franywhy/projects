package com.izhubo.admin

import static com.izhubo.rest.common.doc.MongoKey.ALL_FIELD
import static com.izhubo.rest.common.util.WebUtils.$$
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest
import org.apache.commons.lang.StringUtils
import com.izhubo.model.OpType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.persistent.KGS;
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
import java.util.regex.Pattern
/**
 * data:2015-01-16
 * @author hzj
 */
@RestWithSession
class ProfessionalController extends BaseController{
	
	@Resource
	KGS professionalKGS;

    DBCollection professional(){mainMongo.getCollection('professional')}
	DBCollection main_type(){mainMongo.getCollection('main_type')}

    //专业档案查询
    def list(HttpServletRequest req){
		QueryBuilder query = QueryBuilder.start();	
		String professional_code = req.getParameter("professional_code");
		if(StringUtils.isNotBlank(professional_code)){
			Pattern pattern = Pattern.compile("^.*" + professional_code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("professional_code").regex(pattern)
		}											
		String professional_name = req.getParameter("professional_name");
		if(StringUtils.isNotBlank(professional_name)){
			Pattern pattern = Pattern.compile("^.*" + professional_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("professional_name").regex(pattern)
		}		
		Crud.list(req,professional(),query.get(),ALL_FIELD,MongoKey.SJ_DESC)      
	}

	//
	def edit(HttpServletRequest req){		
		String _id = req.getParameter("_id");
		String main_type_id = req.getParameter("main_type_id");	
		String parent_id = req.getParameter("parent_id");		
		String professional_code = req.getParameter("professional_code");		
		String professional_name = req.getParameter("professional_name");
		String main_type_code;						
		String main_type_name;
		if(!StringUtils.isBlank(main_type_id)){
			def onlyMainType =  main_type().findOne(new BasicDBObject("_id":main_type_id),new BasicDBObject("_id":1,"main_type_code":1,"main_type_name":1));
			main_type_code =  onlyMainType.get("main_type_code");
			main_type_name =  onlyMainType.get("main_type_name");
		}					
		def editProfessional = $$("main_type_id": main_type_id,"main_type_code":main_type_code,
									"main_type_name":main_type_name,"parent_id":parent_id,
									"professional_code":professional_code,"professional_name":professional_name
						  	        );
		professional().update(new BasicDBObject("_id":_id),new BasicDBObject('$set':editProfessional));	
		Crud.opLog(OpType.professional,[update:_id]);
		return OK();
	}
	//
	def del(HttpServletRequest req){
		String id = req[_id];		
		if(StringUtils.isEmpty(id))
			return [code:0]
		professional().remove(new BasicDBObject(_id,id))
		Crud.opLog(OpType.professional,[del:id]);
		return OK();		
	}
	//
	def add(HttpServletRequest req){	
		def addProfessional = $$("_id":UUID.randomUUID().toString());		
		String main_type_id = req.getParameter("main_type_id");
		if(!StringUtils.isBlank(main_type_id)){
			addProfessional.append("main_type_id",main_type_id);
			def onlyMainType =  main_type().findOne(new BasicDBObject("_id":main_type_id),new BasicDBObject("_id":1,"main_type_code":1,"main_type_name":1));
			String main_type_code =  onlyMainType.get("main_type_code");
			String main_type_name =  onlyMainType.get("main_type_name");
			addProfessional.append("main_type_code",main_type_code);
			addProfessional.append("main_type_name",main_type_name);
		}
		String parent_id = req.getParameter("parent_id");		
		addProfessional.append("parent_id", parent_id)
		addProfessional.append("professional_code", professionalKGS.nextId() as String);
		String professional_name = req.getParameter("professional_name");	
		addProfessional.append("professional_name", professional_name)					
		professional().save(addProfessional);
		Crud.opLog(OpType.professional,[save:req["_id"]]);
		return OK();
	}
	//
	def maintype_list(HttpServletRequest req){
		List<DBObject> list = main_type().find().toArray();
		return ["code":1,"data":list];
	}
}
















