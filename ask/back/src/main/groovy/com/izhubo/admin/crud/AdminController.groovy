package com.izhubo.admin.crud

import static com.izhubo.rest.groovy.CrudClosures.*
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import static com.izhubo.rest.common.doc.MongoKey.ALL_FIELD
import java.util.regex.Pattern
import com.izhubo.rest.common.doc.MongoKey
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection;
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.util.JSONUtil
import com.izhubo.rest.common.util.MsgDigestUtil
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.izhubo.admin.BaseController

/**
 * date: 13-3-28 下午2:31
 * @author: wubinjie@ak.cc
 */
@RestWithSession
class AdminController extends BaseController{

    @Resource
    KGS    seqKGS;
	
	 DBCollection company(){mainMongo.getCollection('company')};
	 //DBCollection admins(){mainMongo.getCollection('admins')};
	
    @Delegate Crud crud = new Crud(adminMongo.getCollection('admins'),true,
            [_id:{seqKGS.nextId()},
				nick_name:Str,name:Str,company_id:Str,post_name:Str,menus:{
					if(it){JSONUtil.jsonToMap(it as String) }else{null}
				},
                modules: {if(it){JSONUtil.jsonToMap(it as String) }else{null}}, password:{MsgDigestUtil.SHA.digest2HEX(it as String)},timestamp:Timestamp],
           				
			 new Crud.QueryCondition(){
                public DBObject query(HttpServletRequest req) {
                    if (req['nick_name']){
                       return QueryBuilder.start().and('nick_name').regex(Pattern.compile("/"+req['nick_name']+"/")).get()
                    }
					 
					return super.query(req);;
                }
				
                public DBObject field(HttpServletRequest req) {
					 return new BasicDBObject("password",0);
				}
            }
    )
	
	def list(HttpServletRequest req){				
		Crud.list(
			req,adminMongo.getCollection('admins'),
			null,
			new BasicDBObject("password",0),MongoKey.SJ_DESC)
		{List<BasicDBObject> data->	
			for(BasicDBObject obj: data){
				
				obj.put("company_name", company().findOne(new BasicDBObject("_id":obj['company_id']),new BasicDBObject("company_name",1))?.get("company_name") )
			}
		}
	}
	

	@TypeChecked(TypeCheckingMode.SKIP)
    def show(HttpServletRequest req){
        table().findOne(req.getInt(_id),new BasicDBObject("password",0))
    }

}
