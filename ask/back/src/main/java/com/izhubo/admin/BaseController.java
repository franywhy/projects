package com.izhubo.admin;

import static com.izhubo.rest.common.doc.MongoKey.$inc;
import static com.izhubo.rest.common.doc.MongoKey.$ne;
import static com.izhubo.rest.common.doc.MongoKey.$pull;
import static com.izhubo.rest.common.doc.MongoKey.$push;
import static com.izhubo.rest.common.doc.MongoKey.ALL_FIELD;
import groovy.lang.GroovyObject;
import groovy.transform.CompileStatic;
import groovy.transform.TypeChecked;
import groovy.transform.TypeCheckingMode;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.izhubo.common.util.KeyUtils;
import com.izhubo.rest.common.doc.MongoKey;
import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.rest.pushmsg.IPushService;
import com.izhubo.rest.pushmsg.XinggePushService;
import com.izhubo.rest.web.Crud;
import com.izhubo.rest.web.StaticSpring;
import com.izhubo.rest.web.support.FreemarkerSupport7;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteConcern;
/**
 * action<br>
 *
 *
 * date: 12-8-20 下午2:58
 *
 * @author: wubinjie@ak.cc
 */
@CompileStatic
public abstract class BaseController extends FreemarkerSupport7 {

    @Resource
    public MongoTemplate mainMongo;
    
    @Resource
    public MongoTemplate hqmainMongo;

    public static final MongoTemplate logMongo = (MongoTemplate) StaticSpring.get("logMongo");

    public static final MongoTemplate adminMongo = (MongoTemplate) StaticSpring.get("adminMongo");

    public static final MongoTemplate topicMongo = (MongoTemplate) StaticSpring.get("topicMongo");

    public static final MongoTemplate activeMongo = (MongoTemplate) StaticSpring.get("activeMongo");
    
    public static final MongoTemplate userMongo = (MongoTemplate) StaticSpring.get("userMongo");
    
    
    public static final MongoTemplate qquserMongo = (MongoTemplate) StaticSpring.get("qquserMongo");
    
    
    protected final BaseController sonObj = sonObj();
    
    public BaseController sonObj(){
    	return this;
    }
    
    
    @Resource
    public StringRedisTemplate mainRedis;
    
    @Resource
	public  StringRedisTemplate chatRedis;
    @Resource
    public MongoTemplate singMongo;

    @Resource
    public WriteConcern writeConcern;

    public DBCollection users(){return mainMongo.getCollection("users");}
    public DBCollection rooms(){return mainMongo.getCollection("rooms");}
    public DBCollection area(){return mainMongo.getCollection("area");}


    public DBCollection table(){
        throw new UnsupportedOperationException("One shuold overried this");
    }

    public Map list(HttpServletRequest req,DBObject query){
        return Crud.list(req, table(), query, ALL_FIELD, MongoKey.SJ_DESC);
    }

    private static final Map OK = new HashMap();
    static {
        OK.put("code",1);
    }
    public Map OK(){
        return OK;
    }
    
	public DBCollection constants() {
		return mainMongo.getCollection("constants");
	}
    
    
    //**
    //获取省份和校区的列表
    //**
    public List<DBObject> GetProvinceAndSchool()
    {
		QueryBuilder query = QueryBuilder.start();
		Pattern pattern = Pattern.compile("^.{5}$", Pattern.CASE_INSENSITIVE);
		query.and("code").regex(pattern);
	
		//db.area.find({"$where":"function(){return this.code.length==5;}"})
		List<DBObject> provincelist = area().find(query.get()).toArray();
		
		for (DBObject row : provincelist) {
			String procode = (String) row.get("code");
			//^JH\w{7,7}$
			Pattern shoolpattern = Pattern.compile("^"+procode+"\\w{6,6}", Pattern.CASE_INSENSITIVE);
			QueryBuilder shoolquery = QueryBuilder.start();
			shoolquery.and("code").regex(shoolpattern);
			shoolquery.and("dr").is(0);
			shoolquery.and("is_school").is("1");
			List<DBObject> schoollist = area().find(shoolquery.get()).toArray();
			row.put("schoollist",schoollist);
		}
	  return provincelist;
    }
    //**
    //获取省份和校区的列表
    //**
    public List<DBObject> GetProvince()
    {
    	QueryBuilder query = QueryBuilder.start();
    	Pattern pattern = Pattern.compile("^.{5}$", Pattern.CASE_INSENSITIVE);
    	query.and("code").regex(pattern);
    	
    	//db.area.find({"$where":"function(){return this.code.length==5;}"})
    	List<DBObject> provincelist = area().find(query.get()).toArray();
    	
    	return provincelist;
    }

	
    
    public IPushService GetPushService()
    {
    	XinggePushService xingePush = new XinggePushService();
   
		return xingePush;
    	
    }
    
    public Map getResultOK(Object data){
		Map map = new HashMap();
		map.put("code",1);
		map.put("msg", "success");
		map.put("data", data);
		return map;
	}
    
    public Map getResultOKS(Object data){
		Map map = new HashMap();
		map.put("code", 1);
		map.put("msg", "success");
		map.put("data", data);
		return map;
	}
    
    public Map getKeyErr(){
    	Map map = new HashMap();
    	map.put("code", 3000);
    	map.put("msg", "验签失败");
    	map.put("data", "验签失败");
    	return map;
    }
    public Map getParamsErr(){
    	Map map = new HashMap();
    	map.put("code", 2000);
    	map.put("msg", "参数校验失败");
    	map.put("data", "参数校验失败");
    	return map;
    }
    
    public Map GetResultOKForList(Object data,int count)
    {
    	Map map = new HashMap();
		map.put("code", 1);
		map.put("msg", "success");
		map.put("count",count);
		map.put("data", data);
		return map;
    }

    public boolean intQuery(QueryBuilder q,HttpServletRequest req,String field){
        String str = req.getParameter(field);
        if (StringUtils.isNotBlank(str)){
            q.and(field).is(Integer.valueOf(str));
            return true;
        }
        return false;
    }

    public boolean stringQuery(QueryBuilder q,HttpServletRequest req,String field){
        String  str = req.getParameter(field);
        if (StringUtils.isNotBlank(str)){
            q.and(field).is(str);
            return true;
        }
        return false;
    }
    public boolean stringParameterQuery(QueryBuilder q,HttpServletRequest req,String field){
    	String  str = req.getParameter(field);
    	if (StringUtils.isNotBlank(str)){
    		Pattern pattern = Pattern.compile("^.*" + str + ".*$", Pattern.CASE_INSENSITIVE);
    		q.and(field).regex(pattern);
    		return true;
    	}
    	return false;
    }
    public static final String timestamp = MongoKey.timestamp;
    public static final String stime = "stime";
    public static final String _id = MongoKey._id;
    static final String finance_coin_count = "finance.coin_count";
    static final String finance_coin_spend_total = "finance.coin_spend_total";
    static final String finance_log="finance_log";
    static final String finance_log_id=finance_log+"."+_id;



    public boolean addCoin(Integer userId,Long coin,BasicDBObject logWithId)
    {
        BasicDBObject obj =   new BasicDBObject(finance_coin_count,coin);
        return  this.addCoin(userId,coin,logWithId,obj);
    }


    public boolean refundCoin(Integer userId,Long coin,BasicDBObject logWithId)
    {
        BasicDBObject obj =  new BasicDBObject(finance_coin_count,coin).append(finance_coin_spend_total,-coin) ;
        return   this.addCoin(userId,coin,logWithId,obj);
    }


    private boolean addCoin(Integer userId,Long coin,BasicDBObject logWithId,BasicDBObject obj){
        String log_id = (String) logWithId.get("_id");
        if( coin < 0 || log_id == null ){
            return false;
        }
        DBCollection users = users();
        DBCollection logColl = adminMongo.getCollection(finance_log);
        if(logColl.count(new BasicDBObject(_id,log_id)) == 0  &&
                users.update(new BasicDBObject(_id,userId).append(finance_log_id,new BasicDBObject($ne,log_id)),
                        new BasicDBObject($inc, obj)
                                .append($push,new BasicDBObject(finance_log,logWithId.append(timestamp,System.currentTimeMillis()))),
                        false,false,writeConcern
                ).getN() == 1){

            logColl.save(logWithId,writeConcern);
            users.update(new BasicDBObject(_id, userId),
                    new BasicDBObject($pull,new BasicDBObject(finance_log,new BasicDBObject(_id,log_id))),
                    false,false,writeConcern);

            return true;
        }
        return false;
    }




    public static final String auth_code="auth_code";
    /**
     * 检查验证码是否输错
     */
    public boolean codeVerifError(HttpServletRequest req,String input){
        HttpSession s=  req.getSession();
        String server = (String) s.getAttribute(auth_code);
        if(input == null ||  !input.equalsIgnoreCase(server) ){
            return true;
        }
        s.removeAttribute(auth_code);
        return false;
    }

    public void publish(final String channel , final String json){
        StaticSpring.execute(
                new Runnable() {
                    public void run() {
                        final byte[] data = KeyUtils.serializer(json);
                        mainRedis.execute(new RedisCallback() {
                            @Override
                            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                                return connection.publish(KeyUtils.serializer(channel), data);
                            }
                        });
                    }
                }
        );
    }
    
    @TypeChecked(TypeCheckingMode.SKIP)
    public void publish(final String channel , Map<String, Object> json){
        publish(channel, JSONUtil.beanToJson(json));
    }

    protected Long strbylon(String str) {
    	if(str==null){
    		return null;
    	}else{
    		Long lon = null;
        	try {
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        		lon = sdf.parse(str).getTime();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        	return lon;
    	}
	}
    
    //
    protected DBCollection getMethodDB(){
    	String property = null;
    	try {
    		Class clzz =  sonObj.getClass();
    		GroovyObject newIns = (GroovyObject)clzz.newInstance();
    		property = (String)newIns.getProperty("property");
    		System.out.println(property);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return mainMongo.getCollection(property);
    }
    
    //提交
    protected Map submit1(HttpServletRequest request){
	   Map user = (Map) request.getSession().getAttribute("user");
	   BasicDBObject company=new BasicDBObject();
	   Long now = System.currentTimeMillis();
	   company.put("manage_info.upload_flag",true);
	   company.put("manage_info.upload_user_id" , user.get("_id"));
	   company.put("manage_info.upload_date" , now);
	   getMethodDB().update(new BasicDBObject("_id",request.getParameter("_id")),new BasicDBObject("$set",company));
	   return OK();
	}

	//收回
	protected Map rollbackSubmit1(HttpServletRequest request){
	   Map user = (Map) request.getSession().getAttribute("user");
	   BasicDBObject company=new BasicDBObject();
	   company.put("manage_info.upload_flag",false);
	   company.put("manage_info.upload_user_id" , user.get("_id"));
	   company.put("manage_info.upload_date" , System.currentTimeMillis());
	   getMethodDB().update(new BasicDBObject("_id",request.getParameter("_id")),new BasicDBObject("$set",company));
	   return OK();
	}
	
	//审核
	protected Map audit1(HttpServletRequest request){
	   Map user = (Map) request.getSession().getAttribute("user");
	   BasicDBObject company=new BasicDBObject();
	   company.put("manage_info.audit_flag",true);
	   company.put("manage_info.upload_user_id" , user.get("_id"));
	   company.put("manage_info.upload_date" , System.currentTimeMillis());
	   getMethodDB().update(new BasicDBObject("_id",request.getParameter("_id")),new BasicDBObject("$set",company));
	   return OK();
	 }
	
	//反审核
	protected Map rollbackAudit1(HttpServletRequest request){
	   Map user = (Map) request.getSession().getAttribute("user");
	   BasicDBObject company=new BasicDBObject();
	   company.put("manage_info.audit_flag",false);
	   company.put("manage_info.upload_user_id" , user.get("_id") );
	   company.put("manage_info.upload_date" , System.currentTimeMillis());
	   getMethodDB().update(new BasicDBObject("_id",request.getParameter("_id")),new BasicDBObject("$set",company));
	   return OK();
	 }
	
	

    
}


