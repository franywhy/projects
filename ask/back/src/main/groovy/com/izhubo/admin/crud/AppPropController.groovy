package com.izhubo.admin.crud

import static com.izhubo.rest.common.doc.MongoKey.$setOnInsert
import static com.izhubo.rest.common.doc.MongoKey.ALL_FIELD
import static com.izhubo.rest.common.doc.MongoKey.SJ_DESC
import static com.izhubo.rest.common.doc.MongoKey._id
import static com.izhubo.rest.common.doc.MongoKey.timestamp
import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.mongodb.BasicDBObject
import com.mongodb.QueryBuilder
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.Crud
import com.izhubo.admin.BaseController
import com.izhubo.model.OpType

/**
 * date: 14-05-30 下午2:31
 */
//@Rest
@RestWithSession
class AppPropController extends BaseController{

    Logger logger = LoggerFactory.getLogger(AppPropController.class)

    def list(HttpServletRequest req){
        QueryBuilder query = QueryBuilder.start();
        Crud.list(req,adminMongo.getCollection('properties'),query.get(),ALL_FIELD,SJ_DESC)
    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def add(HttpServletRequest req){
        def id = req[_id]
        if(StringUtils.isEmpty(id))
            return [code:0]

        def content =  req['content']
        def prop = [
                _id:id,
                content:content,
                timestamp:System.currentTimeMillis()
        ]
        adminMongo.getCollection('properties').save(new BasicDBObject((Map)prop))
        Crud.opLog(OpType.properties_add,prop)
        cleanCache();
        [code:1]
    }

    def del(HttpServletRequest req){

        String id = req[_id]
        if(StringUtils.isEmpty(id))
            return [code:0]

        adminMongo.getCollection('properties').remove(new BasicDBObject(_id,id))
        Crud.opLog(OpType.properties_del,[del:id])
        cleanCache();
        OK()
    }

    private void cleanCache()
    {
        String props_key = "all:izhuboapp:props"
        mainRedis.delete(props_key)
    }
}
