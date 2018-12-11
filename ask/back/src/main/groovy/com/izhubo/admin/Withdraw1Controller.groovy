package com.izhubo.admin
import static com.izhubo.rest.common.doc.MongoKey.*
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.servlet.http.HttpServletRequest

import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.Crud
import com.izhubo.model.ApplyType
import com.izhubo.model.OpType

/**
 * date: 13-3-28 下午2:31
 * @author: wubinjie@ak.cc
 */
@RestWithSession
class Withdraw1Controller extends BaseController{
    DBCollection table(){adminMongo.getCollection('withdrawl_log')}

    static final Integer WITH_DRAW_BROKER_TYPE = 2  //代理提现

    def list(HttpServletRequest req){
        def query = Web.fillTimeBetween(req)
        ['status','user_id'].each {String field->
            intQuery(query,req,field)
        }
        def exchange_total = 0

        def q = query.and("type").notIn([WITH_DRAW_BROKER_TYPE]).get()
        def map = Crud.list(req,table(),q,ALL_FIELD,SJ_DESC){List<BasicDBObject> data ->
            def applys = adminMongo.getCollection('applys')
            def users = users()
            for(BasicDBObject obj: data){
                def  apply =  applys.findOne(new BasicDBObject('xy_user_id',obj.get('user_id') as Integer).append("status",ApplyType.通过.ordinal()),
                        new BasicDBObject(tel:1,real_name:1,bank_id:1,bank:1,bank_location:1,bank_name:1,bank_user_name:1,sfz:1
                ))
                if (apply){
                    apply.removeField(_id)
                    obj.putAll(apply)
                }
                def user = users.findOne(new BasicDBObject(_id,obj.get('user_id') as Integer),new BasicDBObject(['star.broker':1,'nick_name':1]))
                obj.put('nick_name',user?.get('nick_name'))
                Map star  = user?.get('star') as Map
                obj.put('broker',star?.get('broker'))
            }
            exchange_total = table().aggregate(
                    new BasicDBObject('$match',q),
                    new BasicDBObject('$project', [exchange:'$exchange']),
                    new BasicDBObject('$group', [_id:null, exchange: [$sum: '$exchange']])
            ).results().first().get('exchange')
        }
        map.put('exchange_total',exchange_total)
        return map
    }


	@TypeChecked(TypeCheckingMode.SKIP)
    def edit(HttpServletRequest req){
        Integer  status = req.getInt("status")
        def record = table().findAndModify(new BasicDBObject(_id,req[_id]),
                new BasicDBObject('$set':[status:status,modif:System.currentTimeMillis()]))
        if (record){
            Crud.opLog(OpType.withdraw_edit,[status: status,withdrawl_log_id:req[_id]])
            if ( status == 2 ){ //拒绝
                users().update(new BasicDBObject(_id,record.get('user_id')),
                    new BasicDBObject('$inc',['finance.bean_count' : 100 * ((Number) record.get('exchange')).intValue()])
                )
            }
        }
        OK()
    }

    def list_broker(HttpServletRequest req){
        def query = Web.fillTimeBetween(req)
        ['status','user_id'].each {String field->
            intQuery(query,req,field)
        }
        def exchange_total = 0
        def q = query.and("type").is(WITH_DRAW_BROKER_TYPE).get()
        def map = Crud.list(req,table(),q,ALL_FIELD,SJ_DESC){List<BasicDBObject> data ->

            def users = users()
            for(BasicDBObject obj: data)
            {
                obj.put('nick_name',users.findOne(new BasicDBObject(_id,obj.get('user_id')),new BasicDBObject('nick_name',1))
                        ?.get('nick_name'))
            }

            exchange_total = table().aggregate(
                    new BasicDBObject('$match',q),
                    new BasicDBObject('$project', [exchange:'$exchange']),
                    new BasicDBObject('$group', [_id:null, exchange: [$sum: '$exchange']])
            ).results().first().get('exchange')
        }
        map.put('exchange_total',exchange_total)
        return map
    }

}
