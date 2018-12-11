package com.izhubo.admin.crud
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.izhubo.rest.web.StaticSpring
import com.izhubo.admin.BaseController
import com.izhubo.admin.Web
import com.izhubo.model.MadelType
import com.izhubo.model.Medal
import com.izhubo.rest.common.doc.MongoKey
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.common.util.WebUtils.$$
import static com.izhubo.rest.groovy.CrudClosures.*
import static com.izhubo.rest.common.doc.MongoKey.*
import com.izhubo.common.util.KeyUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
/**
 * @author: wubinjie@ak.cc
 * Date: 14-2-17 上午10:23
 */
//@Rest
@RestWithSession
class MedalController extends BaseController{

    static final  Logger logger = LoggerFactory.getLogger(MedalController.class)

    private final static Long YEAR_MILLON = 10 * 365 * 24 * 3600 * 1000L
    /**
     * type:1用户 2主播
     * medal_type: 1:礼物 2:活动 3:系统
     * status: 1 上线 0 下线
     * expiry_days:有效天数
     * sum_days :累计天数
     */
    @Delegate Crud crud = new Crud(adminMongo.getCollection('medals'),true,

            [_id:IntNotNull,medal_type:Int,type:Int,name:Str,grey_pic:Str,pic_url:Str,small_pic:Str,expiry_days:Int,
                    sum_days:Int,desc:Str,
                    gift_ids:{String str-> (str == null || str.isEmpty()) ? null : str.split(',').collect {Integer.valueOf(it.toString())}},
                    order:Int,coins:Int,status:Ne0,timestamp:Timestamp,
                    stime:{
                        String str->  (str == null || str.isEmpty()) ? null : Web.getTime(str).getTime()
                    },
                    etime:{String str->  (str == null || str.isEmpty()) ? null : Web.getTime(str).getTime()}
            ],
            new Crud.QueryCondition(){
                public DBObject query(HttpServletRequest req) {
                    def q = QueryBuilder.start()
                    stringQuery(q,req,'status')
                    intQuery(q,req,'type')
                    q.get()
                }
                public DBObject sortby(HttpServletRequest req) {new BasicDBObject("order",-1)}
            }
    )
	
	def list(HttpServletRequest req){
		Crud.list(req,mainMongo.getCollection("users"),null,null,MongoKey.SJ_DESC){
			List<BasicDBObject> data->
			for(BasicDBObject obj: data){
				obj.put("company_name", mainMongo.getCollection('company').findOne(new BasicDBObject("_id":obj['company_id']),new BasicDBObject("company_name",1))?.get("company_name") )
			}
		}
	}
	

    def add(HttpServletRequest req)
    {
        def id = req[_id] as Integer
        if(adminMongo.getCollection('medals').count($$(_id, id)) > 0){
            return [code: 30442]
        }
        this.cleanCache()
        Map result =  crud.add(req)
        if(result.get("code") == 1){
            setSysMedals(req);
        }
        return result
    }

    def edit(HttpServletRequest req)
    {
        //this.unsetMedal(req)
        this.cleanCache()
        Map data = crud.edit(req) as Map
        if(data.get("code") == 1){
            setSysMedals(req);
        }
        return data
    }


    private static final Long SYS_MEDAL_MILLS = 5 * 365 * 24 * 3600 * 1000L

    //设置系统徽章
    private void setSysMedals(HttpServletRequest req){
        final Integer medal_type = req['medal_type'] as Integer
        final Integer mid = req['_id'] as Integer
        if(MadelType.系统.ordinal() == medal_type){
            StaticSpring.execute(
                    new Runnable() {
                        public void run() {
                            def users = mainMongo.getCollection('users')
                            Long now = System.currentTimeMillis()
                            String entryKey = "medals." + mid
                            Long l = System.currentTimeMillis()
                            BasicDBObject query = $$(entryKey,[$not: [$gte:now]]);
                            Boolean flag = Boolean.FALSE
                            if(Medal.羽毛徽章.getId() == mid){
                                query.append('finance.feather_send_total', $$($gte:365))
                                flag = Boolean.TRUE
                            }
                            /*else if(Medal.签到徽章.getId() == mid){
                                query.append('finance.sign_daily_total', $$($gte:100))
                                flag = Boolean.TRUE
                            }*/
                            if(flag){
                                users.update(query, $$($set,$$(entryKey,now+SYS_MEDAL_MILLS)), false, true)
                            }

                            println "setSysMedals cost time: ${System.currentTimeMillis() - l}"
                        }
                    }
            );
        }

    }

    private void unsetMedal(HttpServletRequest req){
        def id = req.getParameter('_id')
        def status = !'0'.equals(req.getParameter('status'))
        def medal = adminMongo.getCollection('medals').findOne($$(_id, id as Integer))
        if(medal == null) return
        Boolean old_status = medal.get('status') as Boolean
        if(old_status != status){
            def entryKey = 'medals.'+id
            if(status)
                users().update($$(entryKey,$$($gt,0)), $$($inc,$$(entryKey,YEAR_MILLON)), false , true)
            else
                users().update($$(entryKey,$$($gt,0)), $$($inc,$$(entryKey,-YEAR_MILLON)), false , true)
        }
    }

    private void cleanCache()
    {
        String medals_key = "all:izhubochang:medals"
        mainRedis.delete(medals_key)

    }
}