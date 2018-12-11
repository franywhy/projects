package com.izhubo.admin

import static com.izhubo.rest.common.doc.MongoKey.*
import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Value

import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.Crud
import com.izhubo.rest.web.StaticSpring
import com.izhubo.model.ApplyType
import com.izhubo.model.OpType
import com.izhubo.model.UserType

/**
 * date: 13-3-28 下午2:31
 * @author: wubinjie@ak.cc
 */
//@Rest
@RestWithSession
class StarController extends BaseController{

    DBCollection table(){users()}

    static long DAY_MILLON = 24 * 3600 * 1000L

    static long zeroMill = new Date().clearTime().getTime()

    def list(HttpServletRequest req){
        def query = Web.fillTimeBetween(req).and('priv').is(UserType.主播.ordinal())

        if (! intQuery(query,req,_id) ){ //没有id处理下面内容
//            if (req['nick_name')!=null]{
//                query.and('nick_name').regex(Pattern.compile("/"+Pattern.quote(req['nick_name'))+"/")]
//            }
            stringQuery(query,req,'nick_name')
            if (req['status']){
                query.and('status').is(!'0'.equals(req['status']))
            }
            intQuery(query,req,"star.broker")
            def start = req['sbean']
            def end = req['ebean']
            if (start || end){
                query.and('finance.bean_count_total')
                if(start){
                    query.greaterThanEquals(start as Long)
                }
                if (end){
                    query.lessThan(end as Long)
                }
            }

        }
        def q  =  query.get()
        def timeQuery = q.removeField(timestamp)
        if (timeQuery){
            q.put('star.timestamp',timeQuery)
        }

        Crud.list(req,table(),q,ALL_FIELD, new BasicDBObject('star.timestamp',-1)){List<BasicDBObject> data->
            def rooms = rooms()
            def applys = adminMongo.getCollection('applys')
            for(BasicDBObject user : data){
                def myroom =  rooms.findOne(new BasicDBObject('xy_star_id',user.get(_id)))
                def apply =  applys.findOne(new BasicDBObject('xy_user_id',user.get(_id)))
                user.put('last_live',myroom?.get(timestamp))
                user.put('baidu_active',myroom?.get("baidu_active"))
                user.put('time_slot',myroom?.get("time_slot"))
                user.put('real_sex',myroom?.get("real_sex"))
                user.put('test',myroom?.get("test"))
                user.put('tel',apply?.get("tel"))
                user.put('qq',apply?.get("qq"))
            }
        }
    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def terminate(HttpServletRequest req){
        def id = req.getInt(_id)
        def user = table().findAndModify(new BasicDBObject(_id:id,priv:UserType.主播.ordinal()),
                new BasicDBObject('$unset':[star:1],'$set':[priv:UserType.普通用户.ordinal()])
        )
        if (user){
            Map star =(Map) user.get('star')
            if (star){
                Integer roomId = star.get("room_id") as Integer
                rooms().remove(new BasicDBObject(_id,roomId))
                Crud.opLog(OpType.star_terminate,[user_id:id,roomId:roomId])

                Integer broker = star.get("broker") as Integer
                if(broker){
                    users().update(new BasicDBObject(_id:broker,'broker.star_total':[$gt:0]),
                            new BasicDBObject('$inc',['broker.star_total':-1]))
                }

                def applys = adminMongo.getCollection('applys')
                def apply = applys.findOne(new BasicDBObject('xy_user_id':id,status:ApplyType.通过.ordinal()),new BasicDBObject('status',1),SJ_DESC)
                if(apply){
                    def tmp = System.currentTimeMillis()
                    applys.update(apply,new BasicDBObject('$set',
                            [status:ApplyType.解约.ordinal(),lastModif:tmp]))
                    logMongo.getCollection('member_applys').findAndModify(new BasicDBObject(xy_user_id:id,status:ApplyType.通过.ordinal()),
                            new BasicDBObject('$set':[status:ApplyType.关闭.ordinal(),lastmodif:tmp,msg: 'terminate']))
                }

                Web.api('java/flushuser?id1='+id)
            }
        }
        return OK()
    }


    def live_log(HttpServletRequest req){
        def query = Web.fillTimeBetween(req).and('type').is('live_on')
        if(req[_id]){
            query.and('session._id').is(req[_id])
        }
        Crud.list(req,logMongo.getCollection('room_edit'),query.get(),ALL_FIELD,SJ_DESC)
    }

    def gift_log(HttpServletRequest req){
        stars_log(req,'send_gift')
    }

    def song_log(HttpServletRequest req){
        stars_log(req,'song')
    }

    //抢沙发记录
    def sofa_log(HttpServletRequest req) {
        stars_log(req, 'grab_sofa')
    }
    //接生记录
    def level_up_log(HttpServletRequest req) {
        stars_log(req, 'level_up')
    }

    def vip_log(HttpServletRequest req){
        stars_log(req,'buy_vip')
    }

	@TypeChecked(TypeCheckingMode.SKIP)
    private stars_log(HttpServletRequest req,String type){
        QueryBuilder q = Web.fillTimeBetween(req)
        q.and('type').is(type)
        stringQuery(q,req,'live')
        if(req[_id]){
            def room_id = req.getInt(_id)
            q.and('room').is(room_id)
            if ('send_gift'.equals(type)) {
                q.and('session.data.xy_star_id').is(room_id)
            }
        }

        def room_db =   logMongo.getCollection('room_cost')
         Crud.list(req,room_db,q.get(),ALL_FIELD,SJ_DESC)
    }
	
	@TypeChecked(TypeCheckingMode.SKIP)
    def stars_log_history(HttpServletRequest req){
        QueryBuilder q = Web.fillTimeBetween(req)
        stringQuery(q,req,'type')

        if(req[_id]){
            def room_id = req.getInt(_id)
            q.and('star_id').is(room_id)

        }
        def room_db =   logMongo.getCollection('room_cost_day_star')
        Crud.list(req,room_db,q.get(),ALL_FIELD,SJ_DESC)
    }

    static String[] apply_props = [
            'real_name',"bank","bank_location","bank_name","bank_id","bank_user_name",
            "tel","qq","address",'sfz',"sex","baidu_active","time_slot"
    ]

	@TypeChecked(TypeCheckingMode.SKIP)
    def edit(HttpServletRequest req){
        def query = new BasicDBObject('xy_user_id',req.getInt(_id))
        def update = new BasicDBObject()

        for(String prop:apply_props){
            String v =  req.getParameter(prop)
            if(StringUtils.isNotEmpty(v)){
                update.put(prop,v)
            }
        }
        def update_romm = new BasicDBObject()
        String sex =  req.getParameter("sex")
        if(StringUtils.isNotEmpty(sex)){
            update_romm.put("real_sex",Integer.parseInt(sex))
        }
        String baidu_active =  req.getParameter("baidu_active")
        if(StringUtils.isNotEmpty(baidu_active)){
            update_romm.put("baidu_active",Integer.parseInt(baidu_active))
        }
        String time_slot =  req.getParameter("time_slot")
        if(StringUtils.isNotEmpty(time_slot)){
            update_romm.put("time_slot",time_slot)
        }
        String test = req.getParameter("test")
        if(StringUtils.isNotEmpty(test)){
            update_romm.put("test",'1'.equals(test))
        }

        if(! update.isEmpty())
        {
            def res = adminMongo.getCollection('applys').findAndModify(query,SJ_DESC,new BasicDBObject('$set',update))
            if(res != null)
            {
               mainMongo.getCollection("rooms").update(new BasicDBObject(_id,req.getInt(_id)),new BasicDBObject('$set',update_romm))
            }
            return [code:res == null ? 0 : 1]
        }
        [code:0,msg:'apply not found']
    }

   // String pic_domain = "http://img.show.izhubo.com/"
	@TypeChecked(TypeCheckingMode.SKIP)
    def add_union_pic(HttpServletRequest req)
    {
        def map = new HashMap()

        String v = req.getParameter("union_pic")
        String unionKey = req.getParameter("union_key")
        String field = "union_pic"

       if("km".equals(unionKey))
           field = field +".km"
       else if ("bd".equals(unionKey))
           field = field +".bd"

        if (StringUtils.isNotBlank(v))
        {
            map.put(field,v)
           // if(v.startsWith(pic_domain))
        }
        if (map.size() > 0)
            users().update(new BasicDBObject(_id,req.getInt(_id)),new BasicDBObject($set,map),false,false,writeConcern)
        [code:1]
    }


    //多张图片
	@TypeChecked(TypeCheckingMode.SKIP)
    def add_union_pics(HttpServletRequest req)
    {
        def map = new HashMap()

        String v = req.getParameter("union_pic")
        String unionKey = req.getParameter("union_key")
        String field = "union_pic"

        if("km".equals(unionKey))
            field = field +".kms"
        else if ("bd".equals(unionKey))
            field = field +".bds"

        if (StringUtils.isNotBlank(v))
        {
            map.put(field,v)
            // if(v.startsWith(pic_domain))
        }
        if (map.size() > 0)
            users().update(new BasicDBObject(_id,req.getInt(_id)),new BasicDBObject($addToSet,map),false,false,writeConcern)
        [code:1]
    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def del_union_pic(HttpServletRequest req)
    {
        def map = new HashMap()
        String v = req.getParameter("union_pic")
        String unionKey = req.getParameter("union_key")
        String field = "union_pic"

        if("km".equals(unionKey))
            field = field +".kms"
        else if ("bd".equals(unionKey))
            field = field +".bds"
        if (StringUtils.isNotBlank(v))
        {
            map.put(field,v)
        }
        if (map.size() > 0)
            users().update(new BasicDBObject(_id,req.getInt(_id)),new BasicDBObject($pull,map),false,false,writeConcern)
        [code:1]
    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def change_broker(HttpServletRequest req){
        String input = req[auth_code]
        if (codeVerifError(req,input)){
            return [code: 30419,msg:'验证码错误']
        }
        def star_id = req.getInt(_id)
        def broker_id = req.getInt('broker')
        def star_query = new BasicDBObject(_id:star_id,priv:UserType.主播.ordinal(),'star.broker':[$ne:broker_id])
        def broker_query  = new BasicDBObject(_id:broker_id,priv:UserType.经纪人.ordinal())
        def users = users()
        if(users.count(star_query)+users.count(broker_query) !=2){
            return [code:0,msg:'star or broker NOT exist,OR star.broker = current']
        }
        adminMongo.getCollection('applys').findAndModify (new BasicDBObject('xy_user_id':star_id,status:ApplyType.通过.ordinal()),SJ_DESC,
                new BasicDBObject('$set':[broker:broker_id.toString()]))
        def old_broker =  users.findAndModify(new BasicDBObject(_id,star_id),new BasicDBObject('$set',['star.broker':broker_id]))
        .get('star')?.getAt('broker')
        if(old_broker){
            users.update(new BasicDBObject(_id:old_broker,'broker.star_total':[$gt:0]),new BasicDBObject('$inc',['broker.star_total':-1]))
        }
        users.update(new BasicDBObject(_id,broker_id),
                new BasicDBObject($addToSet:['broker.stars':star_id],$inc:['broker.star_total':1]))
        Crud.opLog(OpType.star_change_broker,[star:star_id,old_broker:old_broker,broker:broker_id])
        OK()
    }


	@TypeChecked(TypeCheckingMode.SKIP)
  def history_special_gift(HttpServletRequest req)
  {
      final users = users()

      QueryBuilder q  = QueryBuilder.start();
      Date stime = Web.getStime(req);
      Date etime = Web.getEtime(req);
      if (stime !=null || etime !=null){
          q.and("star1.bonus_time");
          if(stime != null){
              q.greaterThanEquals(stime.getTime());
          }
          if (etime != null){
              q.lessThan(etime.getTime());
          }
      }

        def room_id = req.getParameter("_id")
        if (req[_id])
            q.and('star1._id').is(req.getInt(_id))

        Crud.list(req, logMongo.getCollection('special_gifts'), q.get(), ALL_FIELD, ID_DESC) {List<BasicDBObject> data ->
            def rooms = rooms()
            def liveField = new BasicDBObject("live", 1)
            for (BasicDBObject giftLog : data) {
                fillBasicInfo(users, giftLog)
                def star1 = (Map) giftLog.get('star1')
                if (null != star1) { // 增加主播是否开播字段
                    star1.put("live", Boolean.TRUE.equals(rooms.findOne(star1.get(_id), liveField)?.get("live")))
                }
            }
        }
    }

    static final String[] gift_log_props = ['star1', 'fan1']

    private static DBObject fillBasicInfo(DBCollection users, DBObject giftLog)
    {
        for (String field : gift_log_props)
        {
            def user = (Map) giftLog.get(field)
            if (user != null && user.size() > 0)
            {
              Map map =  (Map)users.findOne(user.get("_id") as Integer ,
                        new BasicDBObject('finance.coin_spend_total': 1, 'finance.bean_count_total': 1, nick_name: 1, pic: 1))
                user.putAll(map)
            }
        }
        return giftLog
    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def promotion_list(HttpServletRequest req)
    {
        def query  = Web.fillTimeBetween(req).and('via').is('System')
        def room_id = req.getParameter("_id")
        if (room_id)
           query.and("star_id").is(req.getInt(_id))
        def field = $$(bean:1,coin:1,nick_name:1,star_id:1,timestamp:1,user_id:1)
        return  Crud.list(req,adminMongo.getCollection('bean_ops'),query.get(),field, SJ_DESC)
    }


    @Value("#{application['pic.domain']}")
    String pic_domain = "http://img.show.izhubo.com/"

    File pic_folder

    public static final BasicDBObject ROOM_QUERY = new BasicDBObject(pic_url: [$exists: true])

    @Value("#{application['pic.folder']}")
    void setPicFolder(String folder){
        pic_folder = new File(folder)
        pic_folder.mkdirs()
        println "初始化图片上传目录 : ${folder}"
    }

    @Resource
    UnionPicController unionPicController
    /**
     * 批量切割为百度推广图片要求格式
     * @param request
     * @return
     */
    def batchPic(HttpServletRequest request){
        StaticSpring.execute(
                new Runnable() {
                    public void run() {
                        def users = mainMongo.getCollection("users");
                        def rooms =  mainMongo.getCollection("rooms");
                        Long b = System.currentTimeMillis()
                        rooms.find(ROOM_QUERY, new BasicDBObject("pic_url",1)).toArray().each {
                            String url =  it["pic_url"] as String
                            Integer _id = it["_id"] as Integer
                            unionPicController.generateUnionPic(_id, url)
                        }
                        println "batchPic cost time ${System.currentTimeMillis() - b}"
                    }
                }
        );
        Crud.opLog(OpType.batch_uion_pic,[])
        return [code : 1]
    }

}
