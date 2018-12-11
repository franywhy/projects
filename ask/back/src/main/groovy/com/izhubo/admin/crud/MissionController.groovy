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
class MissionController extends BaseController{

    @Delegate Crud crud = new Crud(adminMongo.getCollection('missions'),
            [_id:Str,title:Str,pic_url:Str,icon_url:Str,coin_count:Int,order:Int,status:Ne0,
                    type:{it.toString().split(',').collect{it as Integer}}],
            new Crud.QueryCondition(){
                public DBObject sortby(HttpServletRequest req) {
                    return new BasicDBObject("order",-1);
                }
            }
    )
}
