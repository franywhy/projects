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
class GiftcatController extends BaseController{


    @Delegate Crud crud = new Crud(adminMongo.getCollection('gift_categories'),true,
            [_id:IntNotNull,name:Str,order:Int,lucky:Eq1,status:Ne0,vip:Eq1,ratio:{it as Double}],
            new Crud.QueryCondition(){
                public DBObject sortby(HttpServletRequest req) {
                    return new BasicDBObject("order",-1);
                }
            }
    )
}
