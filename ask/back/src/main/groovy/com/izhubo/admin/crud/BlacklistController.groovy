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
 * date: 13-10-11 下午2:31
 * @author: wubinjie@ak.cc
 */
@RestWithSession
class BlacklistController extends BaseController{

    def list(HttpServletRequest req){

        QueryBuilder query = QueryBuilder.start();

        String uid = req[_id]
        if (StringUtils.isNotBlank(uid))
            query.and("user_id").is(Integer.parseInt(uid))


        String type = req['type']
        if (StringUtils.isNotBlank(type))
            query.and("type").is(Integer.parseInt(type))

        Crud.list(req,adminMongo.getCollection('blacklist'),query.get(),ALL_FIELD,SJ_DESC){List<BasicDBObject> list ->
            for (BasicDBObject blk : list) {
                def user = users().findOne(blk.get('user_id') as Integer, $$(_id: 0, nick_name: 1, finance:1, priv:1))
                if(user)
                    blk.putAll(user)
            }
        }

    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def add(HttpServletRequest req)
    {

         def uid = Integer.parseInt(req[_id])

         def type =  Integer.parseInt(req['type'])

         def id = uid +"_" + type

        def prop = [
                _id:id,
                type:type,
                user_id:uid,
                timestamp:System.currentTimeMillis()
        ]

        adminMongo.getCollection('blacklist').save(new BasicDBObject((Map)prop))
        Crud.opLog(OpType.blacklist_add,prop)
        [code:1]
    }
    Logger logger = LoggerFactory.getLogger(BlacklistController.class)
    def del(HttpServletRequest req){

        String id = req[_id]

        adminMongo.getCollection('blacklist').remove(new BasicDBObject(_id,id))

        Crud.opLog(OpType.blacklist_del,[del:id])
        OK()
    }








}
