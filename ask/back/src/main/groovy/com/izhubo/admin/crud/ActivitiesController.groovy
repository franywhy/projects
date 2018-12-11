package com.izhubo.admin.crud

import com.mongodb.BasicDBObject
import com.mongodb.QueryBuilder
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.Crud
import com.izhubo.admin.BaseController
import com.izhubo.model.OpType
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.common.doc.MongoKey.ALL_FIELD
import static com.izhubo.rest.common.doc.MongoKey.SJ_DESC
import com.izhubo.admin.Web

/**
 * date: 13-10-11 下午2:31
 * 活动相关的内容
 * @author: wubinjie@ak.cc
 */
@RestWithSession
class ActivitiesController extends BaseController{
    Logger logger = LoggerFactory.getLogger(ActivitiesController.class)
    def list(HttpServletRequest req){

        QueryBuilder query = QueryBuilder.start();

        String id = req[_id]
        if (StringUtils.isNotBlank(id))
            query.and("_id").is(id)

        Crud.list(req,adminMongo.getCollection('activities'),query.get(),ALL_FIELD,SJ_DESC)

    }

    def add(HttpServletRequest req)
    {
        String base = req['base']
        String rate = req['rate']
        String type = req['type']
        long begin = Web.getStime(req).getTime()
        long end = Web.getEtime(req).getTime()
        def tmp = System.currentTimeMillis()
        def prop = [
                _id:tmp,
                base:Long.parseLong(base),
                rate:Double.parseDouble(rate),
                type:type,
                stime:begin,
                etime:end,
                status:1,
                timestamp: tmp
        ]

        adminMongo.getCollection('activities').save(new BasicDBObject((Map)prop))
        Crud.opLog(OpType.actives_add,prop)
        [code:1]
    }


    def edit(HttpServletRequest req){

        String id = req[_id]
        def  update  =  new BasicDBObject()
        String base =  req.getParameter("base")
        if(StringUtils.isNotBlank(base))
            update.put("base",base)
        String rate = req.getParameter("rate")
        if(StringUtils.isNotBlank(rate))
            update.put("rate",rate)

        String status =  req.getParameter("status")
        if(StringUtils.isNotBlank(status))
            update.put("status",status)


        adminMongo.getCollection('activities').update(new BasicDBObject(_id,Long.parseLong(id).longValue()),new BasicDBObject('$set',update))


        Crud.opLog(OpType.actives_edit,[edit:id])
        OK()
    }

    /*def del(HttpServletRequest req){

        String id = req[_id]

        adminMongo.getCollection('activities').remove(new BasicDBObject(_id,id))

        Crud.opLog(OpType.voting_del,[del:id])
        OK()
    }*/








}
