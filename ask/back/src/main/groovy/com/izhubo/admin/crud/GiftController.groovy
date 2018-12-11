package com.izhubo.admin.crud
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.izhubo.admin.BaseController

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.groovy.CrudClosures.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
/**
 * date: 13-3-28 下午2:31
 * @author: wubinjie@ak.cc
 */
@RestWithSession
class GiftController extends BaseController{

    static final  Logger logger = LoggerFactory.getLogger(GiftController.class)
    @Resource
    KGS    giftKGS
    @Delegate Crud crud = new Crud(adminMongo.getCollection('gifts'),true,
            [_id:{giftKGS.nextId()},name:Str,pic_url:Str,swf_url:Str,pic_pre_url:Str,sale:Ne0,isNew:Ne0,isHot:Ne0,
                    ratio:{String str-> (str == null || str.isEmpty()) ? null : str as Double}, desc:Str,
                    category_id:Int,order:Int,coin_price:Int,star_limit:Int,star:Eq1,status:Ne0],
            new Crud.QueryCondition(){
                public DBObject sortby(HttpServletRequest req) {
                    return new BasicDBObject("order",-1);
                }
            }
    )
    def  add(HttpServletRequest req)
    {
        this.cleanCache()
        return crud.add(req)
    }

    def  edit(HttpServletRequest req)
    {
        this.cleanCache()
        return crud.edit(req)
    }

    private void cleanCache()
    {
        String gifts_key = "all:izhubochang:gifts"
        mainRedis.delete(gifts_key)
    }

}
