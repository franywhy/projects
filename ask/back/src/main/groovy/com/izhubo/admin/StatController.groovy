package com.izhubo.admin
import com.mongodb.DBCollection
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.Crud

import javax.servlet.http.HttpServletRequest
import java.util.regex.Pattern
import static com.izhubo.rest.common.doc.MongoKey.*
import com.izhubo.rest.common.doc.MongoKey
/**
 * date: 13-3-28 下午2:31
 * @author: wubinjie@ak.cc
 */
@RestWithSession
class StatController extends BaseController{
    DBCollection table(){adminMongo.getCollection('stat_daily')}

    def pool_log(HttpServletRequest req){
        super.list(req,Web.fillTimeBetween(req).and('type').is('luck').get())
    }

    def login_log(HttpServletRequest req){
        super.list(req,Web.fillTimeBetween(req).and('type').is('login').get())
    }

    def login_month_log(HttpServletRequest req)
    {
        def query = Web.fillTimeBetween(req).and('type').is('login').get()
        Crud.list(req,adminMongo.getCollection('stat_month') , query, ALL_FIELD, MongoKey.SJ_DESC);
    }

   //

    def cost_log(HttpServletRequest req){
        super.list(req,Web.fillTimeBetween(req).and('type').is('allcost').get())
    }

    def gift_log(HttpServletRequest req){
        def query = Web.fillTimeBetween(req).and('type').is('gift_detail')
        def id = req[_id]
        if(id){
            query.and('gift_id').is(id as Integer)
        }
        String name = req['name']
        if(name){
            query.and('name').regex(Pattern.compile(name))
        }
        super.list(req,query.get())
    }

    def car_log(HttpServletRequest req){
        def query = Web.fillTimeBetween(req).and('type').is('buy_car')
        def id = req[_id]
        if(id){
            query.and('car_id').is(id as Integer)
        }
        super.list(req,query.get())
    }

    def vip_log(HttpServletRequest req){
        super.list(req,Web.fillTimeBetween(req).and('type').is('buy_vip').get())
    }

    def sofa_log(HttpServletRequest req){
        super.list(req,Web.fillTimeBetween(req).and('type').is('grab_sofa').get())
    }

    def egg_log(HttpServletRequest req){
        super.list(req,Web.fillTimeBetween(req).and('type').is('open_egg').get())
    }

    def label_log(HttpServletRequest req){
        super.list(req,Web.fillTimeBetween(req).and('type').is('grab_label').get())
    }

    def finance_log(HttpServletRequest req){
        super.list(req,Web.fillTimeBetween(req).and('type').is('finance').get())
    }




    def buy_car_records(HttpServletRequest req){
        Crud.list(req,logMongo.getCollection('room_cost'),
                Web.fillTimeBetween(req).and('type').is('buy_car').get(),ALL_FIELD,SJ_DESC)
    }


}
