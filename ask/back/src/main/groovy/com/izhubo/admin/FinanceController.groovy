package com.izhubo.admin

import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.common.doc.IMessageCode
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.Crud
import com.izhubo.common.util.KeyUtils
import com.izhubo.model.OpType
import com.izhubo.model.PayType
import com.izhubo.model.User

import org.apache.commons.lang.StringUtils

import groovy.transform.TypeChecked;
import groovy.transform.TypeCheckingMode;

import javax.servlet.http.HttpServletRequest

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

import static com.izhubo.rest.common.doc.MongoKey.*
import static com.izhubo.rest.common.util.WebUtils.$$

import com.izhubo.model.ApplyType

/**
 * date: 13-3-28 下午2:31
 * @author: wubinjie@ak.cc
 */
//@Rest
@RestWithSession
class FinanceController extends BaseController{
    DBCollection table(){adminMongo.getCollection('finance_log')}

    def list(HttpServletRequest req){

        def q = Web.fillTimeBetween(req)
        intQuery(q,req,"user_id")
        stringQuery(q,req,_id)
        stringQuery(q,req,'via')
        stringQuery(q,req,'to_id')
		def dq = q.get();
		dq.putAll([$or:[['cny':[$ne:null]], ['via':[$eq:'Admin']]]])
        Crud.list(req,table(),dq,ALL_FIELD,SJ_DESC){List<BasicDBObject> data->
            def users = users()
            for(BasicDBObject obj: data){ // 更新昵称 http://192.168.1.181/redmine/issues/4086
                def user = users.findOne(obj['user_id'],new BasicDBObject(nick_name:1,finance:1))
                if(user){
                    user.removeField(_id)
                    obj.putAll(user)
                }
            }
        }
    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def add(HttpServletRequest req){
        String input = req[auth_code]
        if (codeVerifError(req,input)){
            return [code: 30419,msg:'验证码错误']
        }
        Integer id = req.getInt(_id)
        Long num = req['num'] as Long
        def logWithId = new BasicDBObject(
                _id:"${id}_${num}_Admin_${System.currentTimeMillis()}".toString(),
                user_id:id,
                coin:num,
                via:'Admin',
                session:Web.getSession()
        )
        if (addCoin(id,num,logWithId)){
            Crud.opLog(OpType.finance_add,[user_id:id,coin:num])
        }
        [code:1]
    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def cut_coin(HttpServletRequest req){
        String input = req[auth_code]
        if (codeVerifError(req,input)){
            return [code: 30419,msg:'验证码错误']
        }
        Integer id = req.getInt(_id)
        Long num = req['num'] as Long
        if(num<=0){
            return [code:0,msg:'num must > 0']
        }
        if(users().update(new BasicDBObject(_id,id).append('finance.coin_count',[$gte:num])
                ,new BasicDBObject('$inc',['finance.coin_count':0 - num]),false,false,writeConcern).getN()==1){
            Crud.opLog(OpType.finance_cut_coin,[user_id:id,coin:num])
            return [code:1]
        }
        [code:0,msg: '再扣就负了']
    }




    def stat_list(HttpServletRequest req){

        if (req['stime'] == null){
            return [code: 0]
        }
        def timeQuery = Web.fillTimeBetween(req).get()
        def q = new BasicDBObject()
        String sid = req[_id]
        if (StringUtils.isNotBlank(sid)){
            q.put('user_id',sid as Integer)
        }else{
            def brokerId = req['broker']
            if(brokerId){
               q.put('user_id',[$in: users().find(new BasicDBObject("star.broker",brokerId as Integer),new BasicDBObject('nick_name',1))
                .toArray().collect { it.getAt(_id) }])//*.get(_id)]) //.collect {it[_id]}])
            }
        }
        q.putAll(timeQuery)
        Iterator records  = adminMongo.getCollection("stat_lives").aggregate(
                new BasicDBObject('$match',q),
                new BasicDBObject('$project', [_id: '$user_id',second: '$second',day:'$value',earned:'$earned']),
                new BasicDBObject('$group', [_id: '$_id',second:[$sum: '$second'],earned:[$sum: '$earned'],days :[$sum :'$day']
                ]),
                new BasicDBObject('$sort', [second:-1])
        ).results().iterator()
        def dataList  = new ArrayList(50)
        def applys = adminMongo.getCollection('applys')
        def users = users()
        while (records.hasNext()){
            def obj = records.next()
            def uid = obj.get(_id) as Integer
            def  apply =  applys.findOne(new BasicDBObject('xy_user_id':uid,status:ApplyType.通过.ordinal()), new BasicDBObject(
                    tel:1,real_name:1,bank_id:1,bank:1,bank_location:1,bank_name:1,bank_user_name:1,sfz:1
            ))
            if (apply){
                apply.removeField(_id)
                obj.putAll(apply)
            }
            obj.putAll(users.findOne(new BasicDBObject(_id,uid),new BasicDBObject('star.timestamp':1,'star.broker':1,nick_name:1,'finance.bean_count_total':1)))
//            obj.put("days",obj.get('days').collect(new HashSet(30)) {new Date(((Number)it).longValue()).format("yyyy-MM-dd")})

            dataList.add(obj)
        }
        [code: 1,data:dataList]

    }

    static class PayStat{
        final Set user = new HashSet(2000)
        final AtomicInteger count = new AtomicInteger()
        final AtomicLong coin = new AtomicLong()
        def BigDecimal cny = new BigDecimal(0)
        def toMap(){[user: user.size(),count: count.get(),coin:coin.get(),cny:cny.doubleValue()]}

        def add(def user_id,BigDecimal deltaCny,Long deltaCoin){
            count.incrementAndGet()
            user.add(user_id)
            cny = cny.add(deltaCny)
            coin.addAndGet(deltaCoin)
        }
    }

    def pay_all_delta(HttpServletRequest req){
        if (StringUtils.isBlank((String)req['stime']) ){
            return [code: 0 , msg:'stime is must.']
        }
        def timeQuery = (Map)Web.fillTimeBetween(req).get()
        def oldQuery = new BasicDBObject(timestamp,[$lt:Web.getStime(req).getTime()]).append('via',[$ne:'Admin'])
        def coll = table()
        def old_ids = new HashSet(coll.distinct('user_id',oldQuery))

        def data = new HashMap()
        //[pc:PayType.PC,moblie:PayType.MOBILE]
        [pc:PayType.PC_LIST*.id.toArray(new String[0]),
         moblie:PayType.MOBILE_LIST*.id.toArray(new String[0]),
         qd:PayType.QD_LIST*.id.toArray(new String[0])
        ].each {String k,String[] v->
            def all = new PayStat()
            def delta = new PayStat()
            def cursor = coll.find(new BasicDBObject(timeQuery).append('via',[$in:v]),
                    new BasicDBObject(user_id:1,cny:1,coin:1)).batchSize(5000)
            while(cursor.hasNext()){
                def obj = cursor.next()
                def user_id = obj['user_id']
                def cny = new BigDecimal(((Number)obj.get('cny')).doubleValue())
                def coin = obj.get('coin') as Long
                all.add(user_id,cny,coin)
                if(!old_ids.contains(user_id)){
                    delta.add(user_id,cny,coin)
                }
            }
            cursor.close()
            data.put(k,[all:all.toMap(),delta:delta.toMap()])
        }


        [code: 1,data: data]
    }

    def pay_types(){
        [code: 1,data: [pc:PayType.PC_LIST, moblie:PayType.MOBILE_LIST, qd:PayType.QD_LIST]]
    }

    static final Long DAY_MILL = 24*3600*1000L

	@TypeChecked(TypeCheckingMode.SKIP)
    def change_vip(HttpServletRequest req){
        def day =  req.getInt('day')
        def userId = req.getInt(_id)
        Long delta_mills = day * DAY_MILL

        Integer vip_type = "2".equals(req['type']) ? 2 : 1

        def users = users()
        def query  = new BasicDBObject(_id,userId)
        def user = users.findOne(query,$$("vip_expires",1).append("vip",1))
        if(null == user){
            return [code: 0,msg: "user with id ${userId} not found." ]
        }else{
            Integer oldVip = (Integer)user.get("vip")
            if(null != oldVip && oldVip != vip_type){
                users.update(query,$$($unset,(Map)[vip:1,vip_expires:1,vip_hiding:1]))
                user.removeField("vip")
                user.removeField("vip_expires")
            }
        }
        def vip_expires = user.get("vip_expires") as Long
        Long now = System.currentTimeMillis()
        if(vip_expires == null){
            if(delta_mills <= 0){ //nothing to do.
                return IMessageCode.OK
            }
            vip_expires = now
        }
        def total = vip_expires + delta_mills

        def key = KeyUtils.USER.vip(userId)
        def vipLimitKey = KeyUtils.USER.vip_limit(userId)
        if(total > now){
            users.update(query,new BasicDBObject($set,(Map)[vip:vip_type,vip_expires:total]))
            // vip .toString
            def second = (total - now).intdiv(1000).longValue()
            def valOp = mainRedis.opsForValue()
            valOp.set(key,vip_type.toString(),second,TimeUnit.SECONDS)
            if( vip_type == 2 &&  ! mainRedis.hasKey(vipLimitKey)){ // 10 times  to shutup or forbid erverday.
                valOp.set(vipLimitKey,"10",second,TimeUnit.SECONDS)
            }
        }else {
            users.update(query,new BasicDBObject($unset,(Map)[vip:1,vip_expires:1,vip_hiding:1]))
            mainRedis.delete([key,vipLimitKey])
        }
        Crud.opLog(OpType.finance_change_vip,[user_id:userId,day:day,vip:vip_type])
        IMessageCode.OK
    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def donate_car(HttpServletRequest req){
        def day =  req.getInt('day')
        def userId = req.getInt(_id)
        def carId = req['car_id']
        def carIdInt = Integer.valueOf(carId)
        Long delta_mills = day * DAY_MILL

        String entryKey = "car." + carId
        def users = users()
        Long now = System.currentTimeMillis()
        if(users.update($$(_id,userId).append(entryKey,[$not: [$gte:now]]),
                $$($set,$$(entryKey,now+delta_mills)) ).getN() == 1
            || 1 == users.update($$(_id,userId).append(entryKey,[$gt:now]),$$($inc,$$(entryKey,delta_mills))).getN() ){
            def valOp = mainRedis.opsForValue()
            String key = KeyUtils.USER.car(userId)
            String currRedis = valOp.get(key)
            if(currRedis.isBlank()){
                valOp.set(key,carId,delta_mills,TimeUnit.MILLISECONDS)
                users.update($$(_id,userId),$$($set,$$("car.curr",carIdInt)))
            }else if ( carId.equals(currRedis) ){
                Long expSeconds = mainRedis.getExpire(key) + delta_mills.intdiv(1000)
                mainRedis.expire(key,expSeconds,TimeUnit.SECONDS)
            }
        }

        Crud.opLog(OpType.finance_donate_car,[user_id:userId,day:day,car_id:carIdInt])
        IMessageCode.OK
    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def donate_medal(HttpServletRequest req){
        def medal_award_logs = logMongo.getCollection('medal_award_logs')
        def day =  req.getInt('day')
        def userIds = req.getParameter('ids')
        def medalId = Integer.valueOf(req['medal_id'])
        Long delta_mills = day * DAY_MILL
        String entryKey = "medals." + medalId
        def users = users()
        Long now = System.currentTimeMillis()
        userIds.split(',').collect {
           def userId = it as Integer
            if(users.update($$(_id,userId).append(entryKey,[$not: [$gte:now]]),
                    $$($set,$$(entryKey,now+delta_mills)) ).getN() == 1
                    || 1 == users.update($$(_id,userId).append(entryKey,[$gt:now]),$$($inc,$$(entryKey,delta_mills))).getN() ){
                String medalsListkey = KeyUtils.MEDAL.medalsList(userId)
                mainRedis.opsForSet().add(medalsListkey, medalId.toString())
                //徽章日志
                medal_award_logs.insert($$([_id: userId+"_"+ now,
                        mid:medalId,
                        uid:userId,
                        timestamp:now]
                ))
            }
        }
        Crud.opLog(OpType.finance_donate_medal,[userIds:userIds,day:day,medal_id:medalId])
        IMessageCode.OK
    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def donate_horn(HttpServletRequest req){
        def num =  req.getInt('num')
        def userId = req.getInt(_id)
        def row =  users().update($$(_id,userId),$$($inc,$$('horn',num)),false,false,writeConcern).getN()
        if( 1 == row){
            Crud.opLog(OpType.finance_donate_horn,[user_id:userId,num:num])
        }
        [code:row]
    }

}
