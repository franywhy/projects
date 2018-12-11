package com.izhubo.admin.crud

import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.izhubo.admin.BaseController

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.groovy.CrudClosures.*

/**
 * date: 13-3-28 下午2:31
 * @author: wubinjie@ak.cc
 */
@RestWithSession
class NoticeController extends BaseController{


    @Resource
    KGS noticeKGS

    @Delegate Crud crud = new Crud(adminMongo.getCollection('notices'),true,
            [_id:{noticeKGS.nextId()},title:Str,content:Str,click_url:Str,order:Int,
                    type:Int,status:Ne0,timestamp:Timestamp],
            new Crud.QueryCondition(){
                public DBObject query(HttpServletRequest req) {
                    def q = QueryBuilder.start()
                    intQuery(q,req,'type')
                    q.get()
                }
                public DBObject sortby(HttpServletRequest req) {
                    return new BasicDBObject(order:-1,timestamp:-1);
                }
            }
    )
}
