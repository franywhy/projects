package com.izhubo.admin.crud
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.izhubo.admin.BaseController

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.common.util.WebUtils.$$
import static com.izhubo.rest.groovy.CrudClosures.*
import com.izhubo.common.util.KeyUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
/**
 * date: 13-3-28 下午2:31
 * @author: wubinjie@ak.cc
 * 座驾管理
 */
//@Rest
@RestWithSession
class CarController extends BaseController{

//    @Resource
//    KGS    giftKGS

    // 车 id 自己指定，大于0 ， 越贵越大
    static final  Logger logger = LoggerFactory.getLogger(CarController.class)
    @Delegate Crud crud = new Crud(adminMongo.getCollection('cars'),true,

            [_id:IntNotNull,name:Str,pic_url:Str,swf_url:Str,pic_pre_url:Str,
                    cat:Str,order:Int,coin_price:Int,status:Ne0,type:Int],
            new Crud.QueryCondition(){
                public DBObject query(HttpServletRequest req) {
                    def q = QueryBuilder.start()
                    stringQuery(q,req,'cat')
                    intQuery(q,req,'type')
                    q.get()
                }
                public DBObject sortby(HttpServletRequest req) {new BasicDBObject("order",-1)}
            }
    )

    def add(HttpServletRequest req)
    {
        def id = req[_id] as Integer
        if(adminMongo.getCollection('cars').count($$(_id, id)) > 0){
            return [code: 30442]
        }
       this.cleanCache()
       return crud.add(req)
    }

    def edit(HttpServletRequest req)
    {
        this.cleanCache()
        return crud.edit(req)
    }

    private void cleanCache()
    {
        String cars_key = "all:izhubochang:cars"
        mainRedis.delete(cars_key)
    }

}
