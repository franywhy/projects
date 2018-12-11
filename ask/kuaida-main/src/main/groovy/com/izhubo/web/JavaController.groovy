package com.izhubo.web

import com.mongodb.BasicDBObject
import com.izhubo.rest.anno.Rest
import com.izhubo.common.util.ChatExecutor
import com.izhubo.common.util.KeyUtils
import com.izhubo.common.util.StaticNewSpring
import com.izhubo.model.Finance
import com.izhubo.model.UserType
import com.izhubo.web.api.Web
import com.izhubo.web.interceptor.OAuth2SimpleInterceptor
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.servlet.http.HttpServletRequest

/**
 *
 * 程序之间 集成 胶水代码
 *
 * date: 13-2-25 下午4:02
 * @author: wubinjie@ak.cc
 */
@Rest
class JavaController extends BaseController{
    /**
     * 后台变更了用户类型之后 回调
     *
     * @param request
     * @return
     */
    static final  Logger logger = LoggerFactory.getLogger(JavaController.class)
	/**
	 * 更新session中对应用户信息
	 */
     def flushuser(HttpServletRequest request){

        Integer uid = Web.firstNumber(request)

        String id2token = KeyUtils.USER.token(uid)
		//访问令牌
         String access_token = request.getParameter("access_token")

         if(StringUtils.isBlank(access_token))
             access_token =  mainRedis.opsForValue().get(id2token)


        if (access_token!=null){
			//查询更新缓存中用户信息 
			//finance : 用户账户信息
			//nick_name : 用户昵称
			//priv : 用户类型:1运营人员,2主播,3普通用户,4客服人员,5经纪人
			//star.room_id : 用户对应的主播房间ID
			//status :  用户状态 
			
            def user = mainMongo.getCollection("users").findOne(uid,new BasicDBObject(finance:1,nick_name:1,priv:1,"star.room_id":1,status:1))
			//用户类型:1运营人员,2主播,3普通用户,4客服人员,5经纪人
            Integer priv = user.get("priv") as Integer
            def hashOp  = mainRedis.opsForHash()
            String token_key = KeyUtils.accessToken(access_token)
            Map<String, String> session = hashOp.entries(token_key)
            if (session.isEmpty())
               session = new HashMap<String, String>();

            session.put("_id", uid.toString());
            session.put("priv",priv.toString())
            session.put("nick_name",user.get("nick_name").toString())
			//
            session.put("status", Boolean.TRUE.equals(user.get("status"))?"1":"0")
			//账户信息
            Map finance = (Map) user.get(Finance.finance);
            if (null != finance) {
				//消费金币总数
                Number coin_spend = (Number) finance.get(Finance.coin_spend_total);
                if (null != coin_spend) {
                    session.put("spend", coin_spend.toString());
                }
            }
			//用户类型:1运营人员,2主播,3普通用户,4客服人员,5经纪人
			//初始化主播房间id
            if (priv==UserType.教学老师.ordinal()){
                Number room_id = ((Map) user.get("star"))?.get("room_id") as Number
                if (room_id!=null)
                    session.put("room_id",String.valueOf(room_id.intValue()))
            }
			
            hashOp.putAll(token_key,(Map)session)

            chatRedis.opsForHash().putAll(token_key, (Map)session);
            OAuth2SimpleInterceptor.setSession(session)
            /*def userId = Web.getCurrentUserId();
            logger.info("userId----->:{}",userId)*/
            return  Web.OK
        }
        [code: 0]
    }

    def prizepool(){
        [code:1,data:mainRedis.opsForValue().get(KeyUtils.LUCK.prizePool())]
    }


    //监控转发消息线程池数
    def msg_pool_size()
    {
        def poolSize =   ChatExecutor.poolSize()
        def activeCount =  ChatExecutor.activeCount()
        [code: 1,data:[poolSize:poolSize,activeCount:activeCount,info:ChatExecutor.threadPoolInfoDetail()]]
    }
    //监控相关业务并行处理时线程池数
    def business_pool_size()
    {
        def poolSize =   StaticNewSpring.poolSize()
        def activeCount =  StaticNewSpring.activeCount()
        [code: 1,data:[poolSize:poolSize,activeCount:activeCount,info:StaticNewSpring.threadPoolInfoDetail()]]
    }

    //监控redis指令执行时间
    def redis_time()
    {
        Long l = System.nanoTime()

        String gift_key = KeyUtils.all_gifts()
        mainRedis.opsForValue().get(gift_key)
        double gift_time = (System.nanoTime() - l)/1000d

        l = System.nanoTime()
        Integer room_id = 2518308
        mainRedis.opsForSet().members(KeyUtils.ROOM.users(room_id))
        double view_time = (System.nanoTime() - l)/1000d

        [code: 1,data: [redis_gift_time:gift_time,redis_view_time:view_time,unit:'us']]
    }

    //监控定时执行时间 crontab 的执行时间
    def timer_info()
    {
       def timerLogs =  rankMongo.getCollection("timer_logs")
               .find(new BasicDBObject(timestamp:[$gt:new Date().clearTime().getTime()]),
                     new BasicDBObject(_id:0,timer_name:1,cat:1,cost_total:1,unit:1))
               .toArray()

      // def threshold = [cat_minute:10*1000L,cat_hour:1*60*1000L,cat_day:8*60L*1000]

       [code: 1,data:timerLogs]
    }
	
	def flush_user(Integer _user_id , String _access_token){
		
		
				Integer uid = _user_id;
		
				String id2token = KeyUtils.USER.token(uid)
				//访问令牌
				 String access_token = _access_token;
		
				 if(StringUtils.isBlank(access_token))
					 access_token =  mainRedis.opsForValue().get(id2token)
		
		
				if (access_token!=null){
					//查询更新缓存中用户信息
					//finance : 用户账户信息
					//nick_name : 用户昵称
					//priv : 用户类型:1运营人员,2主播,3普通用户,4客服人员,5经纪人
					//star.room_id : 用户对应的主播房间ID
					//status :  用户状态
					
					def user = mainMongo.getCollection("users").findOne(uid,new BasicDBObject(finance:1,nick_name:1,priv:1,"star.room_id":1,status:1))
					//用户类型:1运营人员,2主播,3普通用户,4客服人员,5经纪人
					Integer priv = user.get("priv") as Integer
					def hashOp  = mainRedis.opsForHash()
					String token_key = KeyUtils.accessToken(access_token)
					Map<String, String> session = hashOp.entries(token_key)
					if (session.isEmpty())
					   session = new HashMap<String, String>();
		
					session.put("_id", uid.toString());
					session.put("priv",priv.toString())
					session.put("nick_name",user.get("nick_name").toString())
					//
					session.put("status", Boolean.TRUE.equals(user.get("status"))?"1":"0")
					//账户信息
					Map finance = (Map) user.get(Finance.finance);
					if (null != finance) {
						//消费金币总数
						Number coin_spend = (Number) finance.get(Finance.coin_spend_total);
						if (null != coin_spend) {
							session.put("spend", coin_spend.toString());
						}
					}
					//用户类型:1运营人员,2主播,3普通用户,4客服人员,5经纪人
					//初始化主播房间id
					if (priv==UserType.教学老师.ordinal()){
						Number room_id = ((Map) user.get("star"))?.get("room_id") as Number
						if (room_id!=null)
							session.put("room_id",String.valueOf(room_id.intValue()))
					}
					
					hashOp.putAll(token_key,(Map)session)
		
					chatRedis.opsForHash().putAll(token_key, (Map)session);
					OAuth2SimpleInterceptor.setSession(session)
					/*def userId = Web.getCurrentUserId();
					logger.info("userId----->:{}",userId)*/
					return  Web.OK
				}
				[code: 0]
			
	}

}