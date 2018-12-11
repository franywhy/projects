package com.izhubo.admin.crud
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.Crud
import com.izhubo.admin.BaseController

import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.groovy.CrudClosures.*
/**
 * date: 13-3-28 下午2:31
 * @author: wubinjie@ak.cc
 */
@RestWithSession
class TkArticleTypeController extends BaseController{

    @Delegate Crud crud = new Crud(userMongo.getCollection('tk_article_type'),
            [_id:Str,type_name:Str],
            new Crud.QueryCondition(){
                public DBObject sortby(HttpServletRequest req) {
                    return new BasicDBObject("type_name",-1);
                }
            }
    )
}
