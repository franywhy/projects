package com.izhubo.admin

import static com.izhubo.rest.common.doc.MongoKey.*
import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.codec.binary.Base64
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.util.HtmlUtils

import com.hqonline.model.Privs
import com.izhubo.common.util.KeyUtils
import com.izhubo.model.OpType
import com.izhubo.model.PhotoStatusType
import com.izhubo.model.UserType
import com.izhubo.rest.AppProperties
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.IMessageCode
import com.izhubo.rest.common.util.MsgDigestUtil
import com.izhubo.rest.common.util.http.HttpClientUtil
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder

/**
 * date: 13-3-28 下午2:31
 * @author: wubinjie@ak.cc
 */

//@Rest
@RestWithSession
class UserController extends BaseController{

    @Resource
    public StringRedisTemplate liveRedis;
    static final  Logger logger = LoggerFactory.getLogger(UserController.class)
	public static final String USERNAME_TO_ACCESS_TOKEN_KEY = "username_to_access_token";
	public static final String ACCESS_TOKEN_TO_USERNAME_KEY = "access_token_to_username";
	@Resource
	KGS roomKGS;
	
	@Value("#{application['us.domain']}")
	String us_domain ;
	
	private final String public_key = "fYhVueg6JHDie53UWHTb";
	
	private final String inner_key = "0y5aZcfHGJX7QmQF5azB";
	
	
	@Resource
	KGS userKGS;
	DBCollection _rooms(){mainMongo.getCollection('rooms')}
    DBCollection table(){users()}
	DBCollection company(){mainMongo.getCollection('company')}	
	DBCollection qQUser(){qquserMongo.getCollection('qQUser')}

//	code : 1
//	all_page: 10171,
//	count: 152564
//	{
//		_id: 10054614,
//		tuid: "bwxfahnaijgda3gihkus",
//		pic: "http://answerimg.kjcity.com/logo.png",
//		nick_name: "4217",
//		priv: 3,
//		status: true,
//		mission: {
//		register: 0,
//		sign_daily: 0
//		},
//		timestamp: 1469430414826,
//		finance: {
//		coin_count: 0
//		},
//		qd: "izhubo_qq",
//		company_name: null,
//		bag: null,
//		login: {
//		ip: "177.77.77.80",
//		timestamp: 1469430414888
//		},
//		ban_status: 0
//		},
	
	@TypeChecked(TypeCheckingMode.SKIP)
    def list(HttpServletRequest req){
		String phone = req["phone"];
		String tuid = null;
		if(StringUtils.isNotBlank(phone)){
			def qquser = qQUser().findOne($$("username" : phone) , $$("tuid" : 1));
			if(qquser){
				tuid = qquser["tuid"];
			}
		}
		
		def query = Web.fillTimeBetween(req);
		intQuery(query,req,'priv');
		intQuery(query,req,'priv1');
		intQuery(query,req,'priv2');
		stringQuery(query,req,'nick_name');
		stringQuery(query,req,'qd');
		stringQuery(query,req,'tuid');
		if (req['status']){
			query.and('status').is(!'0'.equals(req['status']));
		}
		if (req[_id]){
			query.and(_id).is(req.getInt(_id));
		}
		if(tuid != null){
			query.and("tuid").is(tuid);
		}

		[bean:'finance.bean_count_total',coin:"finance.coin_spend_total"].each {String type,String field->

			def start = req['s'+type]
			def end = req['e'+type]
			if (start || end){
				query.and(field)
				if(start){
					query.greaterThanEquals(start as Long)
				}
				if (end){
					query.lessThan(end as Long)
				}
			}
		}
				
		
		return Crud.list(req,table(),query.get(),ALL_FIELD,SJ_DESC){List<BasicDBObject> data->
			def logins  = logMongo.getCollection('day_login')
			def ret = new BasicDBObject(ip:1,uid:1,timestamp:1)
			for(BasicDBObject obj: data){
				
//				obj.put("company_name", company().findOne(new BasicDBObject("_id":obj['company_id']),new BasicDBObject("company_name",1))?.get("company_name"))
				obj.put("bag", qQUser().findOne(new BasicDBObject("tuid":obj['tuid']),new BasicDBObject("username",1))?.get("username"))
				
				def login = logins.findOne(new BasicDBObject('user_id',obj[_id]),ret,ID_DESC);
				if(login){
					login.removeField(_id)
					obj.put('login',login)
				}
				def flogin = logins.findOne(new BasicDBObject('user_id',obj[_id]),ret,$$("timestamp" : 1));
				if(flogin){
					obj.put("first_login", flogin);
				}
				
				obj.put("ban_status",0)
				def keys = liveRedis.keys(KeyUtils.USER.blackClient("*"))
				def valOp = liveRedis.opsForValue()
				String user_id = obj[_id].toString()
				for(String key : keys){
					String value = (String)valOp.get(key)
					String [] tmp = value.split("_")
					String sUserId =tmp[0]
					if (sUserId.equals(user_id))
						obj.put("ban_status",1)
				}
				
				
				
//				var dl = db.day_login.find({"user_id" : user_id}).sort({"timestamp" : 1}).limit(1).toArray();
//				
//							if(dl != null && dl.length > 0){
//				
//								var dd = dl[0];
//				
//								first_login_time = dd.timestamp;
//				
//							}
				
			}
		}
        
    }
	//冻结
	@TypeChecked(TypeCheckingMode.SKIP)
    def freeze(HttpServletRequest req){
        def id = req.getInt(_id)
        Boolean status = !'0'.equals(req['status'])
        if (table().update(new BasicDBObject(_id,id),new BasicDBObject('$set',[status:status]),false,false,writeConcern).getN() == 1){
            Crud.opLog(OpType.user_freeze,[user_id:id,status:status])
            if (!status){
          
				
				String tuid = mainMongo.getCollection("users").findOne($$("_id":id)).get("tuid").toString();
				String username = qquserMongo.getCollection("qQUser").findOne($$("tuid":tuid)).get("username");
				String token = (String) mainRedis.opsForHash().get(
					USERNAME_TO_ACCESS_TOKEN_KEY, username);
				
                if (token ){
                    mainRedis.delete(KeyUtils.USER.token(id))
                    mainRedis.delete(KeyUtils.accessToken(token))
					mainRedis.opsForHash().delete(ACCESS_TOKEN_TO_USERNAME_KEY, token);
					mainRedis.opsForHash().delete("token:" + token, "priv");
					mainRedis.opsForHash().delete("token:" + token, "_id");
					mainRedis.opsForHash().delete("token:" + token, "nick_name");
					mainRedis.opsForHash().delete("token:" + token, "status");
					mainRedis.opsForHash().delete("token:" + token, "pic");
					mainRedis.opsForHash().delete("token:" + token, "vlevel");
					mainRedis.opsForHash().delete("token:" + token, "nc_id");
					mainRedis.opsForHash().delete("token:" + token, "school_code");
					mainRedis.opsForHash().delete("token:" + token, "privs");
                    publish(KeyUtils.CHANNEL.user(id),'{"action":"sys.freeze"}')
                }
            }
        }
        return OK()
    }
	
	 /**
    * 修改备注
    */
   def editRemark(HttpServletRequest req){
	def id = req.getInt(_id);
	String remark = req["remark"];
	if(null != remark){
	 table().update(new BasicDBObject(_id,id),new BasicDBObject('$set',[remark:remark]));
	}
	return OK();
   }
	


	@TypeChecked(TypeCheckingMode.SKIP)
    def ban(HttpServletRequest req){
        def id = req.getInt(_id)
        String uid = getClientId(req,id)
        def comment = req['comment']
        if(uid){
            Integer hour = Math.max(1, ServletRequestUtils.getIntParameter(req,'hour',48))
            String token =  mainRedis.opsForValue().get(KeyUtils.USER.token(id))
            if (token ){
                mainRedis.delete(KeyUtils.USER.token(id))
                mainRedis.delete(KeyUtils.accessToken(token))
                publish(KeyUtils.CHANNEL.user(id),'{"action":"sys.freeze"}')
            }
            String value = id
            if(StringUtils.isNotBlank(value))
            {
                if(StringUtils.isNotBlank(comment))
                    value = id + "_" +  comment

                liveRedis.opsForValue().set(KeyUtils.USER.blackClient(uid),value,hour,TimeUnit.HOURS)
                Crud.opLog(OpType.user_ban,[user_id: id,client:uid,hour:hour,comment:comment])
                return OK()
            }
        }
        [code:0,msg:'无法获取客户端UID或者IP']
    }

    private final String[] clients =['uid','ip']
    private String getClientId(HttpServletRequest req,Integer id){
        for(String f:clients){
            String val = (String)req[f]
            if(StringUtils.isNotBlank(val)){
                return val
            }
        }
        def login = logMongo.getCollection('day_login').findOne("${new Date().format('yyyyMMdd_')}${id}".toString(),
                new BasicDBObject(ip:1,uid:1))
        if(login){
            for (String f :clients){
                String val = (String)login[f]
                if(StringUtils.isNotBlank(val)){
                    return val
                }
            }
        }
        return null
    }
   /* private final String[] user_cliend_field =['uid','login.ip']
    private String getClientId(BasicDBObject obj,Integer id){
        for(String f:user_cliend_field){
            String val = (String)obj[f]
            if ("login.ip".equals(f))
                val = ((Map)obj.get("login"))?.get("ip")

            if(StringUtils.isNotBlank(val)){
                return val
            }
        }
        def login = logMongo.getCollection('day_login').findOne("${new Date().format('yyyyMMdd_')}${id}".toString(),
                new BasicDBObject(ip:1,uid:1))
        if(login){
            for (String f :clients){
                String val = (String)login[f]
                if(StringUtils.isNotBlank(val)){
                    return val
                }
            }
        }
        return null
    }*/

	@TypeChecked(TypeCheckingMode.SKIP)
    def unban(HttpServletRequest req){
        def id = req.getInt(_id)
        String uid = getClientId(req,id)
        if(uid){
            liveRedis.delete(KeyUtils.USER.blackClient(uid))
            Crud.opLog(OpType.user_unban,[user_id:id,client:uid])
            return OK()
        }
        [code:0,msg:'无法获取客户端UID或者IP']
    }


	@TypeChecked(TypeCheckingMode.SKIP)
    def gm(HttpServletRequest req){
        def id = req.getInt(_id)
        def priv = req.getInt('priv')
        if (priv == UserType.运营人员.ordinal() || priv == UserType.普通用户.ordinal()
                || priv == UserType.经纪人.ordinal()
                || priv == UserType.客服人员.ordinal()){
            //def old = priv == UserType.运营人员.ordinal() ? UserType.普通用户.ordinal() : UserType.运营人员.ordinal()


            def set =new HashMap()
            set.put("priv",priv)
            if(priv == UserType.经纪人.ordinal()){
                set.put('broker.'+timestamp,System.currentTimeMillis())
            }

            if (users().update(new BasicDBObject(_id,id).append("priv",[$ne:UserType.主播.ordinal()]), // 不能修改主播
                new BasicDBObject('$set':set),false,false,writeConcern
            ).getN() == 1){
                Crud.opLog(OpType.user_gm,[user_id:id,priv:priv])
                Web.api('java/flushuser?id1='+id)
            }
        }

        return OK()
    }



    def ban_list(){
        def keys = liveRedis.keys(KeyUtils.USER.blackClient("*"))
        def list = new ArrayList(keys.size())
        def valOp = liveRedis.opsForValue()
        def pre = KeyUtils.USER.blackClient("").length()
        for(String key : keys){
            String value = (String)valOp.get(key)
            String [] tmp = value.split("_")
            String user_id =tmp[0]
            String comment = tmp.length == 2 ? tmp[1]:""
            list.add([client:key.substring(pre),_id:user_id,ttl:liveRedis.getExpire(key),comment:comment])
        }
        [code:1,data:list]
    }


	@TypeChecked(TypeCheckingMode.SKIP)
    def show(HttpServletRequest req)
    {
        def user = table().findOne(req.getInt(_id));
		
		user.put('bag',qQUser().findOne(new BasicDBObject("tuid":user['tuid']),new BasicDBObject("username",1))?.get("username"))
		return user;
    }


    def broker_show(HttpServletRequest req){
        def id=req[_id]
        DBObject query  = Web.fillTimeBetween(req).get()
        if(id){
            query['user_id']=id as Integer
        }
        Crud.list(req,adminMongo.getCollection('stat_brokers'),query,ALL_FIELD,SJ_DESC){List<BasicDBObject> data->
            def users = users()
            for(BasicDBObject obj:data){
                obj.putAll(users.findOne(obj['user_id'],new BasicDBObject(nick_name:1,broker:1)))
            }
        }
    }


    /*  Closure cl = !'send_gift'.equals(req['type']) ? null :{List<BasicDBObject> data ->
        def users = users()
        for(Map obj : data){
            Map gift =((Map) obj["session"])?.get("data") as Map
            if(null == gift){
                continue
            }
            Number uid = gift["xy_star_id"] as Number
            if (uid  == null){
                uid = gift["xy_user_id"] as Number
                if (uid == null){
                    continue
                }
            }
            gift.put("xy_nick",users.findOne(uid,new BasicDBObject('nick_name',1))?.get('nick_name'))
        }
    }*/
    static long zeroMill = new Date().clearTime().getTime()
    def cost_log(HttpServletRequest req){
        def query = Web.fillTimeBetween(req)
        if(req[_id]){
            query.and('session._id').is(req[_id])
        }
        stringQuery(query,req,'type')
        def room_db =   logMongo.getCollection('room_cost')
        Crud.list(req,room_db,query.get(),ALL_FIELD,SJ_DESC,null)
    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def cost_log_history(HttpServletRequest req){
        def query = Web.fillTimeBetween(req)
        if(req[_id]){
            def user_id = req.getInt(_id)
            query.and('user_id').is(user_id)
        }
        stringQuery(query,req,'type')
        def room_db =   logMongo.getCollection('room_cost_day_usr')

        Crud.list(req,room_db,query.get(),ALL_FIELD,SJ_DESC,null)
    }

	
	@TypeChecked(TypeCheckingMode.SKIP)
    def gift_rec(HttpServletRequest req){
        QueryBuilder q = Web.fillTimeBetween(req)
        q.and('type').is("send_gift")
        if(req[_id])
        {
            def user_id = req.getInt(_id)
            q.and('session.data.xy_user_id').is(user_id)
        }
        def room_db =   logMongo.getCollection('room_cost')
        Crud.list(req,room_db,q.get(),ALL_FIELD,SJ_DESC)
    }

    def football_log(HttpServletRequest req){
        QueryBuilder q = Web.fillTimeBetween(req)
        q.and('type').is("football_shoot")
        if(req[_id]){
            q.and('session._id').is(req[_id])
        }
        Crud.list(req,logMongo.getCollection('room_cost'),q.get(),ALL_FIELD,SJ_DESC)
    }

    def egg_log(HttpServletRequest req){
        QueryBuilder q = Web.fillTimeBetween(req)
        q.and('type').is("open_egg")
        if(req[_id]){
            q.and('session._id').is(req[_id])
        }
        Crud.list(req,logMongo.getCollection('room_cost'),q.get(),ALL_FIELD,SJ_DESC)
    }

    def luck_log(HttpServletRequest req){
        def query = Web.fillTimeBetween(req)
        if(req[_id]){
            query.and('session._id').is(req[_id])
        }
        Crud.list(req,logMongo.getCollection('room_luck'),query.get(),ALL_FIELD,SJ_DESC)
    }
	
	@TypeChecked(TypeCheckingMode.SKIP)
    def exchange_log(HttpServletRequest req){
        def query = Web.fillTimeBetween(req)
        if(req[_id]){
            query.and('user_id').is(req.getInt(_id))
        }
        Crud.list(req,logMongo.getCollection('exchange_log'),query.get(),ALL_FIELD,SJ_DESC)
    }

	@TypeChecked(TypeCheckingMode.SKIP)
    def lottery_log(HttpServletRequest req)
    {
        def query = Web.fillTimeBetween(req)
        if(req[_id]){
            query.and('user_id').is(req.getInt(_id))
        }

        String  active_name = req.getParameter("active_name")
        if(StringUtils.isNotBlank(active_name))
            query.and("active_name").is(active_name)

        String  lottery_type = req.getParameter("lottery_type")

        logger.info("lottery_type---------->:" + lottery_type)
        if(StringUtils.isNotBlank(lottery_type))
        {
            Integer iLotteryType = Integer.parseInt(lottery_type)
            query.and("lottery_type").is(iLotteryType)
        }
        Crud.list(req,logMongo.getCollection('lottery_logs'),query.get(),ALL_FIELD,SJ_DESC)
    }

	@TypeChecked(TypeCheckingMode.SKIP)
   def union_photo(HttpServletRequest req){
        Crud.list(req, adminMongo.getCollection("union_photos"),$$("user_id" , req.getInt(_id)), ALL_FIELD, SJ_DESC)
    }
/*
    def uphoto_list(HttpServletRequest req){
        QueryBuilder query = QueryBuilder.start();

        String uid = req[_id]
        if (StringUtils.isNotBlank(uid))
            query.and("user_id").is(Integer.parseInt(uid))

        String status = req['status']
        if (StringUtils.isNotBlank(status))
            query.and("status").is(Integer.parseInt(status))

        Crud.list(req, adminMongo.getCollection("union_photos"), query.get(), ALL_FIELD, SJ_DESC)
    }*/

    static final Long SIX_HUNDRED_SECONDS = 600L

    static final String notify_url = (AppProperties.get('api.domain') + 'upai/notify').replace("/","\\/")

    static final Pattern URL_PATT = Pattern.compile("/\\d+/\\d{4}/\\w{32}.[a-z]{3,4}")

    static final String HTTP_FORM_KEY = "bl37fSAQyZ0ZMcF/cZMGjwWNuQU="

	@TypeChecked(TypeCheckingMode.SKIP)
    def token(HttpServletRequest req){
        def uid = req.getInt(_id);
        def json = "{\"bucket\":\"showphoto\"," +
                "\"expiration\":${System.unixTime() + SIX_HUNDRED_SECONDS}," +
                "\"save-key\":\"/${uid}/{mon}{day}/{filemd5}{.suffix}\"," +
                "\"allow-file-type\":\"jpg,jpeg,gif,png\"," +
                "\"image-width-range\":\"120,2048\","+
                "\"image-height-range\":\"120,8192\","+
                "\"content-length-range\":\"0,3145728\"," + // 0 ~ 3MB
//                    "\"x-gmkerl-type\":\"\","+
                "\"return-url\":\"\"," +
                "\"notify-url\":\"${notify_url}\"}"
        def policy = Base64.encodeBase64String(json.asBytes())
        return [code:1,data:[
                action:'http://v0.api.upyun.com/showphoto/',policy:policy,
                signature:MsgDigestUtil.MD5.digest2HEX("${policy}&${HTTP_FORM_KEY}")
        ]]
    }

    @Value("#{application['pic.domain']}")
    String pic_domain = "http://img.show.izhubo.com/"

	@TypeChecked(TypeCheckingMode.SKIP)
    def add_union(HttpServletRequest req){
        def path = req['path']
        def pic_url = req['pic_url']
        def uid = req.getInt(_id);
        //if(URL_PATT.matcher(path.clean()).matches() && path.startsWith("/"+uid+"/")){
        if(adminMongo.getCollection("union_photos").count($$('user_id', uid)) <= 5){
            if(adminMongo.getCollection("union_photos").save(new BasicDBObject(
                    _id:path,
                    user_id:uid as Integer,
                    pic_url : pic_url,
                    timestamp:System.currentTimeMillis(),
                    status: PhotoStatusType.未处理.ordinal()
            )).getN() > 0)
                return IMessageCode.OK

        }
    }
/*
    //**
     * status 1 : 未处理,2 : 通过,3 : 未通过;
     * @param req
     * @return
     *//*
    def edit_union(HttpServletRequest req){
        def path = req['path']
        def status = req['status'];
        adminMongo.getCollection("union_photos").update($$(_id,path), $$($set, $$('status', Integer.valueOf(status)) ))
        return IMessageCode.OK
    }
*/
    //图片处理
    File pic_folder

    @Value("#{application['pic.folder']}")
    void setPicFolder(String folder){
        pic_folder = new File(folder)
        pic_folder.mkdirs()
        println "初始化图片上传目录 : ${folder}"
    }
/*
    def del_union_photo(HttpServletRequest req){
        def path = req['path'] as String
        Integer uid = req.getInt(_id)
//            users().update($$([_id : Web.currentUserId() as Integer]),
//                    $$($pull, $$('union_pic.stars', $$(["path": path,"status": 0])))
//                ,false,false,writeConcern)
        def obj = adminMongo.getCollection("union_photos").findAndRemove($$(_id,path).append("user_id",uid))
        if(obj != null){
//            obj.put("del_time",System.currentTimeMillis())
//            obj.put("s",0)
//            mainMongo.getCollection("ban_photos").save(obj)
//            return IMessageCode.OK
            File file = new File(pic_folder,path)
            file.delete();
            return  [code: 1]
        }
        return [code: 0]
    }*/

	@TypeChecked(TypeCheckingMode.SKIP)
    def edit(HttpServletRequest req)
    {
        def update = new HashMap()
        Integer userId = req.getInt(_id)
        String v = req.getParameter("pic")
        if (StringUtils.isNotBlank(v))
             update.put('pic',v)

        String nick_name = req.getParameter("nick_name")
        if (StringUtils.isNotBlank(nick_name) && nick_name.length() < 21 && ! nick_name.contains(" "))
        {
            nick_name = HtmlUtils.htmlEscape(nick_name)
            update.put("nick_name",nick_name)
            String token =  mainRedis.opsForValue().get(KeyUtils.USER.token(userId))
            mainRedis.opsForHash().put(KeyUtils.accessToken(token),
                    "nick_name",nick_name)
        }
        if (update.size() > 0)
        {
            if(1==users().update(new BasicDBObject(_id,userId),new BasicDBObject($set,update),false,false,writeConcern).getN())
                return  [code: 1]
        }
        return [code: 0]
    }
	
   /* //封面图审核
    static final BasicDBObject COVER_DESC = new BasicDBObject('audit_pic_status',-1);
    def cover_list(HttpServletRequest req){
        Crud.list(req,rooms(),null,ALL_FIELD,COVER_DESC)
    }


    @Resource
    MessageController messageController

    //封面图审核
    def pic_audit(HttpServletRequest req){
        def status = req.getInt('status')
        def room_id = req[_id] as Integer
        if (status == ApplyType.通过.ordinal() || status == ApplyType.未通过.ordinal()){
            Long time = System.currentTimeMillis()
            def record =  rooms().findAndModify(new BasicDBObject(_id:room_id,audit_pic_status:ApplyType.未处理.ordinal()),
                    new BasicDBObject('$set':[audit_pic_status:status,lastmodif:time]))
            if (record){
                def star_id = record.get('xy_star_id') as Integer
                if (status == ApplyType.通过.ordinal()){
                    rooms().update($$(_id, room_id), $$($set, $$('pic_url': record['audit_pic_url'])))
                }
                //发送消息
                def msg = "审核未通过";
                if(status == ApplyType.通过.ordinal()){
                    msg = "审核通过";
                }
                messageController.sendSingleMsg(room_id, '封面图片审核', "你所上传的封面图${msg}。如有任何疑问欢迎联系客服QQ".toString(), MsgType.系统消息);
                Crud.opLog(OpType.pic_audit,[room_id:room_id,status:status])
            }
        }

        OK()
    }*/
	
	
	//所有公司的编号
	def getAllCompany(HttpServletRequest req){
		List<DBObject>  list =  company().find().toArray();
		return ["code":1,"data":list];
	}
	
	def getCurrentCompanyUser(HttpServletRequest req){
		Map user = (Map)req.getSession().getAttribute("user");
		def company = user.get("company_id");
		List<DBObject> list = table().find(new BasicDBObject("company_id":company),ALL_FIELD).toArray();
		return ["code":1,"data":list];
	} 
	
	//
   def add_user(HttpServletRequest req){

	   def bag = req.getParameter("bag");//账号
	   def nick_name = req.getParameter("nick_name");//用户昵称
	
	   String url =  us_domain+"/register";
	   
	   Map<String, String> userMap = new HashMap<>();
	   userMap.put("nick_name", nick_name);
	   userMap.put("user_name", bag);
	   userMap.put("password", "123456");
	   userMap.put("security", public_key);
	   
	   HttpClientUtil.post(url, userMap, null);
	   
	   return OK();
   }
   
   
      def resetpass(HttpServletRequest req){
	   
			  def tuid = req.getParameter("tuid");//账号

			  String username = qQUser().findOne(new BasicDBObject("tuid":tuid),new BasicDBObject("username",1))?.get("username");
			  String url =  us_domain+"/resetpsw";
			  Map<String, String> userMap = new HashMap<>();
			  userMap.put("k", MsgDigestUtil.MD5.digest2HEX(username+"123456"+inner_key) );
			  userMap.put("user_name", username);
			  userMap.put("password", "123456");
			  userMap.put("security", public_key);
			  
			  HttpClientUtil.post(url, userMap, null);
			  
			  return OK();
		  }
   
   
   def del_user(HttpServletRequest req){	   
	   String id = req[_id];
	   if(StringUtils.isEmpty(id))
		   return [code:0]
	   table().remove(new BasicDBObject(_id,id))
	   return OK();
   }
	//学员权限
	def editPXY(HttpServletRequest req){
		Integer p = req.getInt("p");
		if(p == 1){
			updatePriv( req.getInt(_id), Privs.学员, 1);
		}else if( p == 0){
			updatePriv( req.getInt(_id), Privs.学员, 0);
		}
		return OK();
	}
	//抢答权限
	def editPQD(HttpServletRequest req){
		Integer p = req.getInt("p");
		if(p == 1){
			updatePriv( req.getInt(_id), Privs.抢答, 1);
		}else if( p == 0){
			updatePriv( req.getInt(_id), Privs.抢答, 0);
		}
		return OK();
	}
	//招生权限
	def editPZS(HttpServletRequest req){
		Integer p = req.getInt("p");
		if(p == 1){
			updatePriv( req.getInt(_id), Privs.招生, 1);
		}else if( p == 0){
			updatePriv( req.getInt(_id), Privs.招生, 0);
		}
		return OK();
	}
   /**
    * 修改用户权限
    * @param user_id	用户id
    * @param priv		权限类型
    * @param val		0.关闭 1.开通
    * @return			true:操作成功 false:操作失败
    */
   def updatePriv(Integer user_id , Privs priv , Integer val){
	   if(
		   null != user_id && user_id > 0 && 
		   null != val && (val==0 || val==1)&&
		   (Privs.学员 == priv || Privs.抢答 == priv || Privs.招生 == priv) &&
		   table().update(new BasicDBObject(_id,user_id) , new BasicDBObject('$set', new BasicDBObject(priv.getKey() , val)) , ,false,false,writeConcern).getN()>0
		   ){
		   Crud.opLog("userPrivsEdit",['user_id':user_id , 'priv' : priv.getKey() , "val" : val]);
		   return true;
	   }
	   return false;
   }
	
   // 9：00-21：00 格式转成毫秒格式
   def timeToMillisecond (String time){
	   def str = time.split(":");
	   long newTimes = (str[0] as long)*60*60*1000+(str[1] as long)*60*1000;
	   return newTimes;
   }
   //根据毫秒获取明年今天的毫秒数
   def getNextMillisecond(){
	   Calendar curr = Calendar.getInstance();
	   curr.set(Calendar.YEAR,curr.get(Calendar.YEAR)+1);
	   Date date=curr.getTime();
	   long new_times=date.getTime();
	   println date;
	   return new_times;
   }
   
}
