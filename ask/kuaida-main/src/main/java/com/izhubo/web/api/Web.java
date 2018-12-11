package com.izhubo.web.api;

import static com.izhubo.rest.common.doc.MongoKey.$inc;
import static com.izhubo.rest.common.doc.MongoKey.$set;
import static com.izhubo.rest.common.doc.MongoKey.$setOnInsert;
import static com.izhubo.rest.common.doc.MongoKey.$unset;
import static com.izhubo.rest.common.doc.MongoKey._id;
import static com.izhubo.rest.common.doc.MongoKey.timestamp;
import groovy.transform.CompileStatic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.izhubo.common.doc.Param;
import com.izhubo.common.util.KeyUtils;
import com.izhubo.model.Mission;
import com.izhubo.model.User;
import com.izhubo.model.UserType;
import com.izhubo.rest.common.doc.IMessageCode;
import com.izhubo.rest.common.doc.MongoKey;
import com.izhubo.rest.common.util.WebUtils;
import com.izhubo.rest.ext.RestExtension;
import com.izhubo.rest.web.StaticSpring;
import com.izhubo.web.interceptor.OAuth2SimpleInterceptor;
import com.izhubo.web.interceptor.UserFrom;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.izhubo.model.VlevelType;

@CompileStatic
public abstract class Web  extends WebUtils{


    /**
     * 配合 nginx URL 重写
     * rewrite      /([a-z]+/[a-z_]+)/([a-z0-9\-]+)/(\d+)/?([\d_]+)?\??(.*)
     * @return
     */
    public static Integer roomId(HttpServletRequest request){
        return firstNumber(request);
    }

    public static Integer userId(HttpServletRequest request){
        return secondNumber(request);
    }

    public static Integer secondNumber(HttpServletRequest request)
    {
        Integer secondNumber = 0 ;
        try
        {
            String id2 = request.getParameter(Param.second);
            if(StringUtils.isNotBlank(id2))
                secondNumber = Integer.valueOf(id2);
        }
        catch(Exception ex)
        {
            logger.error("secondNumber String cast Integer Exception", ex);
//            ex.printStackTrace();
        }
        return  secondNumber ;
       // return Integer.valueOf(request.getParameter(Param.second));
    }

    public static Integer firstNumber(HttpServletRequest request)
    {
         Integer firstNumber = 0 ;
         try
         {
             String id1 = request.getParameter(Param.first) ;
             if(StringUtils.isNotBlank(id1))
                 firstNumber = Integer.valueOf(id1);
         }
         catch(Exception ex)
         {
            logger.error("firstNumber String cast Integer Exception", ex);
//            ex.printStackTrace();
         }
        return  firstNumber ;
    }

    public static String firstParam(HttpServletRequest request){
        return request.getParameter(Param.first);
    }

    public static String secondParam(HttpServletRequest request){
        return request.getParameter(Param.second);
    }

    public static List<Integer> getFollowing(Integer userId){
        Set<String> sets =  mainRedis.opsForSet().members(KeyUtils.USER.following(userId));
        if(sets==null){
            return null;
        }
        List<Integer> list = new ArrayList<Integer>(sets.size());
        for(String id : sets){
            list.add(Integer.valueOf(id));
        }
        return list;
    }

    public static Map currentUser(){
        return getSession();
    }

    public static Integer getCurrentSSOUserId(){
        Integer sso_userid = 0 ;
        sso_userid = Integer.valueOf(currentSSOUserId());
        return sso_userid ;
    }

    public static Integer getCurrentUserId(){
        Integer uid = 0 ;
        uid = Integer.valueOf(currentUserId());
        return uid ;
    }
    /** 获取用户的NCid */
	public static String getCurrentUserNcId() {

		Map map = getSession();
		String nc_id = null;
		if (null == map)
			logger.error("currentUserId:OAuth2SimpleInterceptor.getSession is----->: null");
		else
			nc_id = map.get("nc_id").toString();

		return nc_id;

	}
	
	public static JSONArray getCurrentUserPrivs() {

		Map map = getSession();
		String privstr = "[0,0,0]";
		if (null == map)
			logger.error("currentUserId:OAuth2SimpleInterceptor.getSession is----->: null");
		else
			privstr = map.get("privs").toString();
		
		JSONArray ja = null;
		try {
			ja = new JSONArray(privstr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ja;

	}
	
	
	   /** 获取用户的校区编码 */
		public static String getCurrentUserSchoolCode() {

			Map map = getSession();
			String nc_id = null;
			if (null == map)
				logger.error("currentUserId:OAuth2SimpleInterceptor.getSession is----->: null");
			else
				nc_id = map.get("school_code").toString();

			return nc_id;

		}
    final static  Logger logger = LoggerFactory.getLogger(Web.class) ;
    public static String currentSSOUserId()
    {
        Map map = getSession();
        String ssouserId = "0" ;
        if(null == map)
            logger.error("currentSSOUserId:OAuth2SimpleInterceptor.getSession is----->: null");
        else
            ssouserId = map.get("sso_userid") == null ? "0" : map.get("sso_userid").toString() ;

        return ssouserId;
    }
    public static String currentUserId()
    {
        Map map = getSession();
        String userId = "0" ;
        if(null == map)
            logger.error("currentUserId:OAuth2SimpleInterceptor.getSession is----->: null");
        else
            userId = map.get(_id) == null ? "0" : map.get(_id).toString() ;

        return userId;
    }
    
    
    public static Integer currentUserPriv()
    {
        Map map = getSession();
        Integer priv = 3 ;
        if(null == map)
            logger.error("currentUserId:OAuth2SimpleInterceptor.getSession is----->: null");
        else
        	priv = Integer.valueOf(map.get("priv").toString()) ;

        return priv;
    }


    public static String currentUserNick(){
        Map map = getSession();
        String nickName = "----" ;
        if(null == map)
            logger.error("currentUserNick:OAuth2SimpleInterceptor.getSession is---->: null");
        else
            nickName = (String)map.get("nick_name");

        return nickName ;
    }
    public static int currentUserType()
    {
        Integer priv = 3 ;
        try
        {
            Map map = currentUser() ;
            if(null !=map)
                priv = Integer.valueOf(map.get("priv").toString());
        }
        catch(Exception ex)
        {
        	logger.warn("currentUserType reset priv = 3", ex);
//            ex.printStackTrace();
            priv = 3 ;
        }
        return priv ;
    }
    public static int currentUserVLevel()
    {
    	Integer vlevel = VlevelType.V0.ordinal() ;
    	try
    	{
    		Map map = currentUser() ;
    		if(null !=map)
    			vlevel = Integer.valueOf(map.get("vlevel").toString());
    	}
    	catch(Exception ex)
    	{
    		logger.warn("currentUserType reset vlevel = 0", ex);
//            ex.printStackTrace();
    		vlevel = VlevelType.V0.ordinal() ;
    	}
    	return vlevel ;
    }

    public static Map getSession(){

        return OAuth2SimpleInterceptor.getSession();
    }


    private  static Map<String,Object> missParam = new HashMap<String, Object>();
    private  static Map<String,Object> notAllowed = new HashMap<String, Object>();
    private  static Map<String,Object> ok = new HashMap<String, Object>();

    static {
        missParam.put("code",30406);
        missParam.put("msg","丢失必需参数");
        notAllowed.put("code",30413);
        notAllowed.put("msg","权限不足");
        ok.put("code",1);
    }
    public static Map missParam(){
        return missParam;
    }
    public static Map notAllowed(){
        return notAllowed;
    }
    public static final Map OK =  IMessageCode.OK;
   // public static final Map OK =  Collections.unmodifiableMap(ok);



    public static String hexSeconds(){
       return Long.toHexString(System.currentTimeMillis()/1000);
    }

    public static BasicDBObject logCost(String type,Integer cost,Integer roomId,String liveId){
      /*  Map obj = new HashMap();
        obj.put("type", type);
        obj.put("cost", cost);
        if(roomId!=null)
            obj.put("room", roomId);
        if(StringUtils.isNotBlank(liveId))
            obj.put("live", liveId);
        obj.put("session", Web.getSession());
        obj.put("timestamp", System.currentTimeMillis());
        return new BasicDBObject(obj);*/

        return  logCost( type, cost, roomId, liveId, null) ;
    }

    public static BasicDBObject logCost(String type,Integer cost,Integer roomId,String liveId,Integer family_id){
        Map obj = new HashMap();
        obj.put("type", type);
        if("send_feather".equals(type))
            obj.put("num", 1);

        obj.put("cost", cost);
        if(roomId!=null)
            obj.put("room", roomId);
        if(StringUtils.isNotBlank(liveId))
            obj.put("live", liveId);
        obj.put("session", Web.getSession());
        obj.put("timestamp", System.currentTimeMillis());

        if(family_id != null)
            obj.put("family_id", family_id);

        return new BasicDBObject(obj);
    }

    public static void obtainGift(Integer userId,Integer gift_id,Integer count)
    {
        DBObject  gift = getGiftById(gift_id) ;
        String sCategoryId = gift.get("category_id").toString();
        DBObject giftCategory = getGiftCategoriesById(Integer.parseInt(sCategoryId));
        Integer price = gift != null ? Integer.parseInt(gift.get("coin_price").toString()) : 0;

        Double ratio =  giftCategory != null ? Double.parseDouble(giftCategory.get("ratio").toString()) :0.0d ;
        //礼物分成比例
        if(null != gift.get("ratio"))
            ratio =  Double.parseDouble(gift.get("ratio").toString()) ;
        String s =  new java.text.SimpleDateFormat(
                "yyyyMMdd").format(new Date());
        String id = userId +"_" + s ;
        Long bean  = new Double(price * count * ratio).longValue() ;

        logMongo.getCollection("obtain_gifts").findAndModify(new BasicDBObject(_id,id), null, null, false,
                new BasicDBObject($inc,new BasicDBObject("bag."+gift_id,count).append("gifts_bean",bean))
                        .append($set,new BasicDBObject("user_id",userId).append(timestamp,System.currentTimeMillis())) ,true, true) ;

    }


    public static Date getEtime(HttpServletRequest request){
        return getTime(request,"etime");
    }

    public static Date getStime(HttpServletRequest request){
        return getTime(request,"stime");
    }
    public static final String DFMT = "yyyy-MM-dd";
    private static Date getTime(HttpServletRequest request,String key)  {
        String str = request.getParameter(key);
        if(StringUtils.isNotBlank(str)){
            try {
                return new SimpleDateFormat(DFMT).parse(str);
            } catch (ParseException e) {
            	logger.error("", e);
//                e.printStackTrace();
            }
        }
        return null;
    }
    public static QueryBuilder fillTimeBetween(HttpServletRequest req){
        QueryBuilder query = QueryBuilder.start();
        Date stime = getStime(req);
        Date etime = getEtime(req);
        if (stime !=null || etime !=null){
            query.and("timestamp");
            if(stime != null){
                query.greaterThanEquals(stime.getTime());
            }
            if (etime != null){
                query.lessThan(etime.getTime());
            }
        }
        return query;
    }

    public static final StringRedisTemplate liveRedis = (StringRedisTemplate) StaticSpring.get("liveRedis");
    public static final StringRedisTemplate mainRedis = (StringRedisTemplate) StaticSpring.get("mainRedis");
    public static final StringRedisTemplate chatRedis = (StringRedisTemplate) StaticSpring.get("chatRedis");
    public static final StringRedisTemplate picRedis = (StringRedisTemplate) StaticSpring.get("picRedis");
    
    public static final MongoTemplate mainMongo = (MongoTemplate) StaticSpring.get("mainMongo");
    public static final MongoTemplate logMongo = (MongoTemplate) StaticSpring.get("logMongo");
    public static final MongoTemplate adminMongo = (MongoTemplate) StaticSpring.get("adminMongo");

    public static boolean isBanned(HttpServletRequest req){
        return isBanned(getClientId(req));
    }

    public static boolean isBanned(String clientId){
        return liveRedis.hasKey(KeyUtils.USER.blackClient(clientId));
    }
    public static String getClientId(HttpServletRequest req){
        String client_id = req.getParameter(Param.uid);
        if(StringUtils.isNotBlank(client_id)){
            return client_id;
        }
        return getClientIp(req);
    }

    public static String getClientIp(HttpServletRequest req){
        String xff = req.getHeader(Param.XFF);
//        logger.info("X-FORWARDED-FOR: {}", xff);
        if(StringUtils.isNotBlank(xff)){
            return xff;
        }
        return req.getRemoteAddr();
    }
    public static boolean isVIP(Object user_id)
    {
       Boolean bFlag = mainRedis.hasKey(KeyUtils.USER.vip(user_id));//至尊
      /* if(bFlag)
           bFlag =  mainRedis.hasKey(KeyUtils.USER.vip_normal(user_id));  //普通*/

        return bFlag ;
    }
    public static void setNewVIP(DBObject user)
    {
        setVIP(user);
       //setVIPNormal(user);
    }


    public static boolean setVIP(DBObject user)
    { //至尊
        Object vip = user.get(User.VIP.vip);
        if(vip!=null){
            Object userId = user.get(_id);
            long remain_mills  = ((Number)user.get(User.VIP.vip_expires)).longValue()- System.currentTimeMillis();
            if(remain_mills < 1000){
                mainMongo.getCollection("users").update(new BasicDBObject(_id,userId),new BasicDBObject($unset,
                        $$(User.VIP.vip,"").append(User.VIP.vip_expires,"").append(User.VIP.vip_hiding, "")));
                user.removeField(User.VIP.vip);
                user.removeField(User.VIP.vip_expires);
                user.removeField(User.VIP.vip_hiding);
            }else {
            	logger.info("setVIP remain_mills: {}", remain_mills);
                mainRedis.opsForValue().set(KeyUtils.USER.vip(userId),vip.toString(),
                        remain_mills , TimeUnit.MILLISECONDS);
                if( User.VIP.HIGH_LEVEL.equals( vip) && Integer.valueOf(1).equals(user.get(User.VIP.vip_hiding))){
                    mainRedis.opsForValue().set(KeyUtils.USER.vip_hiding(userId),vip.toString(),
                            remain_mills , TimeUnit.MILLISECONDS);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean setVIPNormal(DBObject user){  //普通
        Object vip = user.get(User.VIP.vip_normal);
        if(vip!=null){
            Object userId = user.get(_id);
            long remain_mills  = ((Number)user.get(User.VIP.vip_expires_normal)).longValue()- System.currentTimeMillis();
            if(remain_mills < 1000){
                mainMongo.getCollection("users").update(new BasicDBObject(_id,userId),new BasicDBObject($unset,
                        $$(User.VIP.vip_normal,"").append(User.VIP.vip_expires_normal,"").append(User.VIP.vip_hiding_normal, "")));
                user.removeField(User.VIP.vip_normal);
                user.removeField(User.VIP.vip_expires_normal);
                user.removeField(User.VIP.vip_hiding_normal);
            }else {
                mainRedis.opsForValue().set(KeyUtils.USER.vip_normal(userId),vip.toString(),
                        remain_mills , TimeUnit.MILLISECONDS);
                /*if( User.VIP.HIGH_LEVEL.equals(vip) && Integer.valueOf(1).equals(user.get(User.VIP.vip_hiding_normal))){
                    mainRedis.opsForValue().set(KeyUtils.USER.vip_hiding_normal(userId),vip.toString(),
                            remain_mills , TimeUnit.MILLISECONDS);
                }*/
                return true;
            }
        }
        return false;
    }


    public static void day_login(HttpServletRequest req,Integer uid){
        Date time = new Date();
        Long tmp = time.getTime();
        String id = new SimpleDateFormat("yyyyMMdd_").format(time) + uid;
        Map<String,Object> setOnInsert = new HashMap<>();
        setOnInsert.put("user_id",uid);
        setOnInsert.put(timestamp,tmp);
        String mobileId = req.getParameter(Param.uid);
        if(StringUtils.isNotBlank(mobileId)){
            setOnInsert.put("uid",mobileId);
        }
        String ip = req.getHeader(Param.XFF);
        if(StringUtils.isBlank(ip)){
            ip = req.getRemoteAddr();
        }
        setOnInsert.put("ip",ip);
       //添加qd
        DBObject qdUser = mainMongo.getCollection("users").findOne(new BasicDBObject(_id,uid),new BasicDBObject("qd",1));
        String qd = null ;
        Object oqd = qdUser != null ? qdUser.get("qd") : null ;
        if(null != oqd)
            qd = oqd.toString();
        if(StringUtils.isNotEmpty(qd))
            setOnInsert.put("qd",qd);
        //添加 快播 token
        String id2token = KeyUtils.USER.token(uid);
        String access_token =  mainRedis.opsForValue().get(id2token);
        String namespace = access_token.substring(0, 3);
        if(UserFrom.快播移动.namespace.equals(namespace)&&StringUtils.isNotEmpty(access_token))
        {
            setOnInsert.put("access_token",access_token);
        }

        if(logMongo.getCollection("day_login").findAndModify(new BasicDBObject(_id,id),null,
                null,false,
                new BasicDBObject($setOnInsert,setOnInsert),true,true //upsert
        ).get(timestamp).equals(tmp)){ // 完成每日签到任务  和至尊VIP每日踢人禁言总数
//            DBObject user = mainMongo.getCollection("users").findAndModify($$(_id, uid),
//                        $$(User.VIP.vip,1).append(User.VIP.vip_expires,1),MongoKey.NO_SORT,false,
//                        $$(MongoKey.$set,$$(Mission.每日签到.mongoKey,Mission.Status.完成未领取奖金.ordinal())),false,false);
//            if(User.VIP.HIGH_LEVEL.equals(user.get(User.VIP.vip))){
//                Long ttlMills = (Long) user.get(User.VIP.vip_expires) - System.currentTimeMillis();
//                if(ttlMills > 0){
//                    mainRedis.opsForValue().set(KeyUtils.USER.vip_limit(uid),
//                            User.VIP.MANAGE_LIMIT.toString(),ttlMills,TimeUnit.MILLISECONDS);
//                }
//            }
        }
    }


    public static List<byte[]> redisSort(String key,final SortParameters sortParam){
        final byte[] bytesKey = RestExtension.asBytes(key);
        return mainRedis.execute(new RedisCallback<List<byte[]>>() {
            public List<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.sort(bytesKey, sortParam);
            }
        });
    }


    public static boolean isStar(){
        return UserType.教学老师.ordinal() == currentUserType();
    }

    public static void saveChannel(String qd,String child_qd)
    {
        Map<String,Object> setOnInsert = new HashMap<>();
        setOnInsert.put("client","1");
        setOnInsert.put("comment","");
        setOnInsert.put("name",child_qd);
        setOnInsert.put("parent_qd",qd);
        setOnInsert.put("type","1");
        long tmp = System.currentTimeMillis();
        setOnInsert.put(timestamp,tmp);

        adminMongo.getCollection("channels").findAndModify($$(_id, child_qd), null, null, false,
                new BasicDBObject($setOnInsert, setOnInsert), true, true) ;

    }

    private static final Map<Integer,DBObject>  gifts ;
    private static final  Map<Integer,DBObject>  giftsCategories ;
    private static final Map<Integer,DBObject>  cars ;
    private static String cars_flag = "";
    private static String gifts_flag = "";
    static
    {
        gifts = new HashMap<Integer,DBObject>() ;
        giftsCategories = new HashMap<Integer,DBObject>() ;
        cars = new HashMap<Integer,DBObject>() ;
        init();
        initCar();
    }

    private static void init()
    {
        DBObject status = new BasicDBObject("status", Boolean.TRUE);
        DBObject sort = new BasicDBObject("order",1).append("coin_price",1);
        DBObject fields = new BasicDBObject("coin_price",1).append("ratio",1).append("category_id",1).append("name",1);
        try
        {
            List<DBObject>  giftsLst = adminMongo.getCollection("gifts").find(status,fields).sort(sort)
                    .toArray();
            for(DBObject gift:giftsLst)
            {
                gifts.put(Integer.parseInt(gift.get("_id").toString()),gift);
            }

            List<DBObject>  giftsCategoriesLst =  adminMongo.getCollection("gift_categories").find(status)
                    .sort(new BasicDBObject("order", 1)).toArray() ;
            for(DBObject giftCategory:giftsCategoriesLst)
            {
                giftsCategories.put(Integer.parseInt(giftCategory.get("_id").toString()),giftCategory);
            }
        }
        catch(Exception ex)
        {
            logger.error("init gifts String cast Integer Exception", ex);
//            ex.printStackTrace();
        }
    }

    private static void initCar(){
        try
        {
            DBObject status = new BasicDBObject("status", Boolean.TRUE);
            List<DBObject> car_list = adminMongo.getCollection("cars").find(status).sort(new BasicDBObject("order", -1)).toArray();
            for(DBObject car:car_list)
            {
                cars.put((int)Float.parseFloat(car.get("_id").toString()),car);
            }
        }
        catch(Exception ex)
        {
            logger.error("initCar String cast Integer Exception", ex);
//            ex.printStackTrace();
        }

    }

    public static DBObject getGiftById(Integer giftId)
    {
        String gift_local = KeyUtils.local_gifts_flag();
        String json =  mainRedis.opsForValue().get(gift_local);
        if(StringUtils.isEmpty(json)){
            json = String.valueOf(System.currentTimeMillis());
            mainRedis.opsForValue().setIfAbsent(gift_local, json);
        }
        if(!gifts_flag.equals(json))
        {
            gifts_flag = json;
            init();
        }
        DBObject  gift =  gifts.get(giftId) ;
        return gift ;
    }

    public static DBObject getGiftCategoriesById(Integer categoryId)
    {
        DBObject giftCategory = giftsCategories.get(categoryId);
        return giftCategory ;
    }

    public static DBObject getCarById(Integer carId)
    {
        String local_cars_flag = KeyUtils.local_cars_flag();
        String json =  mainRedis.opsForValue().get(local_cars_flag);
        if(StringUtils.isEmpty(json)){
            json = String.valueOf(System.currentTimeMillis());
            mainRedis.opsForValue().setIfAbsent(local_cars_flag, json);
        }
        if(!cars_flag.equals(json)){
            cars_flag = json;
            initCar();
        }
        DBObject car =  cars.get(carId) ;
        return car ;
    }

}
