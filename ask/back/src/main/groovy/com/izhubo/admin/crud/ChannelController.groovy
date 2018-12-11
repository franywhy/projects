package com.izhubo.admin.crud

import groovy.transform.TypeChecked;
import groovy.transform.TypeCheckingMode;

import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import com.izhubo.rest.common.util.MsgDigestUtil
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.Crud
import com.izhubo.admin.BaseController
import com.izhubo.admin.Web
import com.izhubo.model.OpType

import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.common.doc.MongoKey.ALL_FIELD
import static com.izhubo.rest.common.doc.MongoKey._id
import static com.izhubo.rest.groovy.CrudClosures.*
import static com.izhubo.rest.common.doc.MongoKey.SJ_DESC

import org.apache.commons.lang.StringUtils

/**
 * date: 13-4-19 下午2:31
 * @author: wubinjie@ak.cc
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class ChannelController extends BaseController{

    @Delegate Crud crud = new Crud(adminMongo.getCollection('channels'),
            [_id:Str,name:Str,comment:Str,client:Str,type:Str,
                    reg_discount:Str,active_discount:Str,timestamp:Timestamp],
            new Crud.QueryCondition(){
                public DBObject query(HttpServletRequest req) {
                    def query = new BasicDBObject()
                    def id = req[_id]
                    if(id.isNotBlank()){
                        query.put(_id,id)
                    }
                    def client = req['client']
                    if(client.isNotBlank()){
                        query.put("client",client)
                    }

                    def name = req['name']
                    if(name.isNotBlank()){
                        query.put("name",name)
                    }

                    def parent_flag = req['parent_flag']
                    if(StringUtils.isNotBlank(parent_flag))
                    {
                        if("1".equals(parent_flag))
                            query.append("parent_qd",new BasicDBObject($nin:[null]))
                         else if ("-1".equals(parent_flag))
                            query.append("parent_qd",null)
                    }
                    return query
                }
            })

    def add_user(HttpServletRequest req){
        String pwd = req['password']
        def prop = [
                //_id:seqKGS.nextId(),
                nick_name:req['nick_name'],
                _id:req['name'],
                timestamp:System.currentTimeMillis(),
                qd:req['qd']
        ]
        if(pwd){
            prop.put('password', MsgDigestUtil.SHA.digest2HEX(pwd.toString()))
        }
        adminMongo.getCollection('channel_users').save(new BasicDBObject((Map)prop))
        Crud.opLog(OpType.channel_add_user,prop)
        [code:1]
    }


    def list_user(HttpServletRequest req){
        int p = Web.getPage(req)
        int size = Web.getPageSize(req)
        def query = new BasicDBObject()
        if (req['qd']){
            query['qd'] = req['qd']
        }
        Crud.list(req,adminMongo.getCollection('channel_users'),query,ALL_FIELD,SJ_DESC)
    }


    def del_user(HttpServletRequest req){
        adminMongo.getCollection('channel_users').remove(new BasicDBObject(_id,req[_id]))
        Crud.opLog(OpType.channel_del_user,[_id:req[_id]])
        [code:1]
    }
}
