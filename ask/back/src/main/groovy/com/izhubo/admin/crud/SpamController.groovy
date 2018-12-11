package com.izhubo.admin.crud

import static com.izhubo.rest.common.doc.MongoKey.$setOnInsert
import static com.izhubo.rest.common.doc.MongoKey.$unset
import static com.izhubo.rest.common.doc.MongoKey.ALL_FIELD
import static com.izhubo.rest.common.doc.MongoKey.SJ_DESC
import static com.izhubo.rest.common.doc.MongoKey._id
import static com.izhubo.rest.common.doc.MongoKey.timestamp
import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.servlet.http.HttpServletRequest

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.mongodb.BasicDBObject
import com.mongodb.QueryBuilder
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.Crud
import com.izhubo.admin.BaseController
import com.izhubo.model.OpType

/**
 * 垃圾信息相关内容
 * date: 13-10-11 下午2:31
 * @author: wubinjie@ak.cc
 */
@RestWithSession
class SpamController extends BaseController
{
    Logger logger = LoggerFactory.getLogger(SpamController.class)

    def list(HttpServletRequest req){

        QueryBuilder query = QueryBuilder.start();

        Crud.list(req,adminMongo.getCollection('spam_info'),query.get(),ALL_FIELD,SJ_DESC)

    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def add(HttpServletRequest req)
    {
         def field = req['field_id']

         def field_content = req['field_content']

         def tmp =   System.currentTimeMillis()
         def update = new BasicDBObject(field,field_content).append("modify",tmp)

        adminMongo.getCollection('spam_info').update(new BasicDBObject(_id, 'field_id_field_content_20131121'),
             new BasicDBObject($set:update))

        Crud.opLog(OpType.spam_add,update)

        [code:1]
    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def edit(HttpServletRequest req){

        def field = req['field_id']
        adminMongo.getCollection('spam_info').update(new BasicDBObject(_id, 'field_id_field_content_20131121'),
                new BasicDBObject($unset,new BasicDBObject(field,1)))

        Crud.opLog(OpType.spam_del,[del:field])
        OK()
    }










}
