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
class PosterController extends BaseController{

    @Resource
    KGS posterKGS

    @Delegate Crud crud = new Crud(adminMongo.getCollection('posters'),true,
            [_id:{posterKGS.nextId()},title:Str,pic_url:Str,small_pic_url:Str,click_url:Str,back_ground_color:Str,
                    type:Int,status:Ne0,timestamp:Timestamp,order:Int],
            new Crud.QueryCondition(){
                public DBObject query(HttpServletRequest req) {
                    def query = QueryBuilder.start()
                    intQuery(query,req,'type')
                    return query.get()
                }
                public DBObject sortby(HttpServletRequest req) {new BasicDBObject(order:1,timestamp:-1)}
            }
    )
}
