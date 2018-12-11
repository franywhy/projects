package com.izhubo.admin

import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.common.doc.ParamKey
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.UnmodifDBObject
import com.izhubo.rest.web.Crud

import org.apache.commons.lang.StringUtils

import groovy.transform.TypeChecked;
import groovy.transform.TypeCheckingMode;

import javax.servlet.http.HttpServletRequest

import java.text.SimpleDateFormat
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

import static com.izhubo.rest.common.doc.MongoKey.*
import static com.izhubo.rest.common.util.WebUtils.$$

/**
 * date: 13-4-22 10:40
 * @author: wubinjie@ak.cc
 */
//@Rest
@RestWithSession
class QdController extends BaseController{
    DBCollection table(){adminMongo.getCollection('stat_channels')}


	@TypeChecked(TypeCheckingMode.SKIP)
    def set_cpa(HttpServletRequest req){
        [code: table().update($$(_id,req[_id]),
            $$($set,$$('cpa1',new Integer(req['cpa1'])).append(
                    'cpa2',new Integer(req['cpa2'])
            ))
        ).getN()
        ]
    }

    Map reg_pay_list_service(DBObject query,HttpServletRequest req)
    {
        reg_pay_list_service(query, null, req)
    }

    Map reg_pay_list_service(DBObject query,BasicDBObject desc,HttpServletRequest req)
    {
        if(desc == null){
            desc = $$(reg:-1)
        }
          Crud.list(req,table(),query,$$(pays:0,regs:0), desc){List<BasicDBObject> qd_list->
            def channel = adminMongo.getCollection('channels')
            for (BasicDBObject obj:qd_list)
            {
                def id = obj['qd']
                obj.put('name',channel.findOne(id,$$('name',1))?.get('name'))
            }
        }
    }

    def reg_pay_list(HttpServletRequest req){

        def channel = adminMongo.getCollection('channels')

        def qid= req[_id]
        def child_qd  =  req["child_qd"]
        def query = new BasicDBObject('qd',[$in:channel.find($$('client',req['client'])).toArray()
                .collect {it.getAt(_id)}])
        BasicDBObject desc = $$(reg:-1)
        if(qid)
        { // 按照id查询  日期排序
           def   my_query = Web.fillTimeBetween(req).and('qd').is(qid)
           if(child_qd)
              my_query.and('child_qd').is(child_qd)
           query = my_query.get()
            desc = $$(timestamp,-1);
        }
        else
        {
            //按注册数排序
         /*   query = new BasicDBObject('qd',[$in:channel.find($$('client',req['client'])).toArray()
                    .collect {it.getAt(_id)}])*/
            def day = req[stime]
            if(day){
                query[timestamp] = new SimpleDateFormat('yyyy-MM-dd HH:mm:ss').parse(day as String).clearTime().getTime()
            }
        }
        reg_pay_list_service(query,desc,req)
    }



    Map reg_list_service(String qdId ,HttpServletRequest req){
        def query = Web.fillTimeBetween(req).and('qd').is(qdId).get()
        Crud.list(req,users(),query,new BasicDBObject(timestamp:1,nick_name:1), SJ_DESC)
    }

    def reg_list(HttpServletRequest req){
        reg_list_service(req[_id] as String,req)
    }


    def pay_list(HttpServletRequest req){
        pay_list_service(req[_id] as String,req)

    }
    //已优化
    Map pay_list_service(String qid,HttpServletRequest req){
        QueryBuilder query  = Web.fillTimeBetween(req)
        query  = query.and('via').notEquals('Admin').and('qd').is(qid)
        //优化后
        Crud.list(req,adminMongo.getCollection('finance_log'),query.get(),ALL_FIELD, SJ_DESC){List<BasicDBObject> list->
            def users = users()
            //待优化
            for (BasicDBObject obj:list){
                obj.put('nick_name',users.findOne(obj['user_id'],$$('nick_name',1))?.get('nick_name'))
            }
        }
    }

    final Long DAY_MILLON = 24 * 3600 * 1000L
    static final BasicDBObject REG_DESC =new BasicDBObject('reg',-1);
    def pay_rate(HttpServletRequest req){
        def channel = adminMongo.getCollection('channels')

        def qid= req[_id]
        def query
        BasicDBObject desc = REG_DESC
        if(qid)
        { // 按照id查询
             query = Web.fillTimeBetween(req).and('qd').is(qid).get()
             desc  = new BasicDBObject('timestamp',-1);
        }
        else
        {
            query = new BasicDBObject('qd',[$in:channel.find($$('client',req['client'])).toArray()
                    .collect {it.getAt(_id)}])
            def day = req[stime]
            if(day){
                query[timestamp] = new SimpleDateFormat('yyyy-MM-dd HH:mm:ss').parse(day as String).clearTime().getTime()
            }
        }
        pay_rate_service(query,desc,req)

    }
   //待优化

    Map pay_rate_service(DBObject query, BasicDBObject desc,HttpServletRequest req){
        Crud.list(req,table(),query,new BasicDBObject(pays:0,cny:0,count:0), desc){List<BasicDBObject> qd_list->
            def finance_log = adminMongo.getCollection('finance_log')
            def channel = adminMongo.getCollection('channels')
            for (BasicDBObject obj:qd_list){
                long begin = obj[timestamp] as long
                def allUids = obj.remove('regs') as Collection
                obj.put('name',channel.findOne(obj['qd'],$$('name',1))?.get('name'))
                if(allUids && allUids.size()>0)
                    [1,3,7,30].each {Integer d->
                        def timeBetween =  [$gte: begin, $lt: begin+d*DAY_MILLON]
                        def iter = finance_log.aggregate(
                                $$('$match', [via:[$ne:'Admin'],user_id:[$in:allUids],timestamp:timeBetween]),
                                $$('$project', [cny:'$cny',user_id:'$user_id']),
                                $$('$group', [_id: null,cny: [$sum: '$cny'],count:[$sum: 1],pays:[$addToSet :'$user_id']])
                        ).results().iterator()
                        if(iter.hasNext()){
                            def finaObj = iter.next()
                            finaObj['pay'] = (finaObj.removeField('pays') as Collection).size()
                            finaObj.removeField(_id)
                            obj.put("${d}_day".toString(),finaObj)
                        }
                    }
            }
        }
    }

     //
    def login_rate(HttpServletRequest req){
        def channel = adminMongo.getCollection('channels')
        def query
        def qid= req[_id]
        BasicDBObject desc = REG_DESC
        if(qid)
        { // 按照id查询
            query = Web.fillTimeBetween(req).and('qd').is(qid).get()
            desc  = new BasicDBObject('timestamp',-1);
        }
        else
        {
            query = $$('qd',[$in:channel.find($$('client',req['client'])).toArray().collect {it[_id]}])
            def day = req[stime]
            if(day){
                query[timestamp] = new SimpleDateFormat('yyyy-MM-dd HH:mm:ss').parse(day as String).clearTime().getTime()
            }
        }
        login_rate_service(query,desc,req)

    }

     //待优化
    Map login_rate_service(DBObject query, BasicDBObject desc,HttpServletRequest req){
        Crud.list(req,table(),query,new BasicDBObject(pays:0,cny:0,count:0), desc){List<BasicDBObject> qd_list->

            def day_login = logMongo.getCollection('day_login')
            def channel = adminMongo.getCollection('channels')
            for (BasicDBObject obj:qd_list){
                long begin = obj[timestamp] as long
                def allUids = obj.remove('regs') as Collection
                obj.put('name',channel.findOne(obj['qd'],$$('name',1))?.get('name'))
                if(allUids && allUids.size()>0)
                    [1,3,7,30].each {Integer d->
                        Long gt = begin+d*DAY_MILLON
                        def count = day_login.count($$(user_id:[$in:allUids],timestamp:
                                [$gte: gt, $lt: gt+DAY_MILLON]
                        ))
                        obj.put("${d}_day".toString(),count)
                    }
            }
        }
    }



    def login_total(HttpServletRequest req){
        login_total_service((String)req[_id],req)
    }


     //已优化
    Map login_total_service(String qdId,HttpServletRequest req)
    {
        Map<Integer,Integer> map
        def qb = new BasicDBObject()
        if(StringUtils.isNotBlank(qdId))
        { // _id client 互斥
          map = new HashMap<Integer,Integer>(100000)
          qb.put(_id,qdId)
          def res =  Crud.list(req,adminMongo.getCollection('channels'),qb,ALL_FIELD, SJ_DESC)
          {List<BasicDBObject> list->
                long time = (Web.getStime(req)?:new Date().clearTime()).getTime()
                def days = [1,7,30]
                def day_login = logMongo.getCollection('day_login')
                for (BasicDBObject obj:list)
                {
                    days.each {Integer d->
                        Long begin = time - d*DAY_MILLON
                        def logins = day_login.find(new BasicDBObject('qd':obj[_id],timestamp:[$gte:begin , $lt: time]),new BasicDBObject("user_id":1)).toArray()
                        for (DBObject login :logins)
                        {
                           Integer uid  =  login.get("user_id") as Integer
                           map.put(uid,uid)
                        }
                        obj.put("${d}_day".toString(),map.size())
                    }
                }
          }
          map = null ;
          return res
        }
        else
        {//待优化
            map = new HashMap<Integer,Integer>(2000000)
            def client = req['client']
            if(null != client)
                qb.put("client",client)
            def qds = adminMongo.getCollection('channels').find(qb,
                    $$(_id,1)).toArray().collect{DBObject it->it.get(_id)}
            def days = [1,7,30]
            def day_login = logMongo.getCollection('day_login')
            if( qds.size() > 0 )
            { //待优化
                def time = (Web.getStime(req)?:new Date().clearTime()).getTime()
                days.each{Integer d->
                    def logins = day_login.find(new BasicDBObject(qd:[$in:qds],timestamp:[$gte: time - d*DAY_MILLON, $lt: time]),new BasicDBObject("user_id":1)).toArray()
                    for (DBObject login :logins)
                    {
                        Integer uid  =  login.get("user_id") as Integer
                        map.put(uid,uid)
                    }
                    qb.put("${d}_day".toString(),map.size())
                }
            }
            map = null
            return [code:1,data:[qb]]
        }
    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def reg_pay_period(HttpServletRequest req){

        def uQ = Web.fillTimeBetween(req)
        def qd  = req[_id]

        if(qd.isNotBlank() && qd.equals("f101")){
           return km_reg_pay_period(req)
        }
        if(qd.isNotBlank()){
            uQ.and('qd').is(qd)
        }else{
            def client = req['client']
            if(client.isNotBlank()){
                def qds = adminMongo.getCollection('channels').find($$('client',client),
                        $$(_id,1)).toArray().collect{DBObject it->it.get(_id)}
                //TODO 快播用户特殊处理
                qds.remove("f101")
                uQ.and('qd').in(qds)
            }
        }
        def userQuery = (BasicDBObject)uQ.get()
        if(userQuery.isEmpty()){
            return [code:0,msg:'user_query  empty']
        }

        def uids = users().find(userQuery,$$(_id,1)).collect(new HashSet(10240)){it[_id]}

        def payQ = Web.fillTimeBetween(Web.getTime(req,'stime1'),Web.getTime(req,'etime1')).and('via').notEquals('Admin')

        def  payQuery = ((BasicDBObject)payQ.get()).append($or,[ [user_id: [$in:uids]] ,[to_id:[$in:uids]] ])
        //TODO 快播用户特殊处理
 /*       if(qd.isNotBlank() && qd.equals("f101")){
            payQuery = ((BasicDBObject)payQ.get()).append($or,[ [via: 'km-'] ])
        }*/
        //.append('user_id',[$in:uids])//      忽略代理冲值

        def cur = adminMongo.getCollection('finance_log').find(payQuery).batchSize(5000)
        def uset = new HashSet(uids.size())
        def coin = new AtomicLong()
        def cny = new BigDecimal(0)
        def times = new AtomicInteger()
        while (cur.hasNext()){
            def row = cur.next()
            uset.add(row['to_id'] ?: row['user_id'])
            coin.addAndGet((Long)row['coin'])
            cny = cny.add(new BigDecimal(((Double)row['cny']).doubleValue()))
            times.incrementAndGet()
        }
        [code: 1,data:[reg_user:uids.size(),pay_user:uset.size(),pay_cny:cny.doubleValue(),pay_coin:coin.longValue(),pay_times:times.get()]]
    }

	
	@TypeChecked(TypeCheckingMode.SKIP)
    private km_reg_pay_period(HttpServletRequest req){

        def uQ = Web.fillTimeBetween(req)
        def qd  = req[_id]
        if(qd.isNotBlank()){
            uQ.and('qd').is(qd)
        }else{
            def client = req['client']
            if(client.isNotBlank()){
                def qds = adminMongo.getCollection('channels').find($$('client',client),
                        $$(_id,1)).toArray().collect{DBObject it->it.get(_id)}
                uQ.and('qd').in(qds)
            }
        }
        def userQuery = (BasicDBObject)uQ.get()
        if(userQuery.isEmpty()){
            return [code:0,msg:'user_query  empty']
        }

        def payQ = Web.fillTimeBetween(Web.getTime(req,'stime1'),Web.getTime(req,'etime1')).and('via').notEquals('Admin')

        Long reg_users = 0
        Long pay_users = 0
        Long pay_cnys = 0
        Long pay_coins = 0
        Long pay_times = 0

        def count = users().count(userQuery)
        def size = 100000
        def allPage = (int)((count + size - 1) / size);
        while(allPage > 0){
            List uids = users().find(userQuery, $$(_id ,1)).skip((allPage - 1) * size).limit(size).toArray()*._id;
            def payQuery = ((BasicDBObject)payQ.get()).append($or,[ [user_id: [$in:uids]] ,[to_id:[$in:uids]] ])
            def result = reduceRegPay(uids, payQuery)
            reg_users += result["reg_user"] as Long
            pay_users += result["pay_user"] as Long
            pay_cnys += result["pay_cny"] as Long
            pay_coins += result["pay_coin"] as Long
            pay_times += result["pay_times"] as Long
            allPage--
        }
        [code: 1,data:[reg_user:reg_users,pay_user:pay_users,pay_cny:pay_cnys,pay_coin:pay_coins,pay_times:pay_times]]
    }

    private Map reduceRegPay(List uids, BasicDBObject payQuery){
        def cur = adminMongo.getCollection('finance_log').find(payQuery).batchSize(5000)
        def uset = new HashSet(uids.size())
        def coin = new AtomicLong()
        def cny = new BigDecimal(0)
        def times = new AtomicInteger()
        while (cur.hasNext()){
            def row = cur.next()
            uset.add(row['to_id'] ?: row['user_id'])
            coin.addAndGet((Long)row['coin'])
            cny = cny.add(new BigDecimal(((Double)row['cny']).doubleValue()))
            times.incrementAndGet()
        }

        return [reg_user:uids.size(),pay_user:uset.size(),pay_cny:cny.doubleValue(),pay_coin:coin.longValue(),pay_times:times.get()]
    }
}
