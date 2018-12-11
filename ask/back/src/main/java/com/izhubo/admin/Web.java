package com.izhubo.admin;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.izhubo.rest.AppProperties;
import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.rest.common.util.WebUtils;
import com.izhubo.rest.common.util.http.HttpClientUtil;
import com.izhubo.rest.web.StaticSpring;
import com.izhubo.web.interceptor.SessionInterceptor;
import groovy.transform.CompileStatic;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.izhubo.rest.common.doc.MongoKey.*;

@CompileStatic
public abstract class Web extends WebUtils{

    public static final MongoTemplate logMongo = (MongoTemplate) StaticSpring.get("logMongo");

   static Logger logger = LoggerFactory.getLogger(Web.class) ;
    public static Date getEtime(HttpServletRequest request){
        return getTime(request,"etime");
    }

    public static Date getStime(HttpServletRequest request){
        return getTime(request,"stime");
    }
    static final String DFMT = "yyyy-MM-dd HH:mm:ss";
    public static Date getTime(HttpServletRequest request,String key)  {
        String str = request.getParameter(key);
        if(StringUtils.isNotBlank(str)){
            try {
                return new SimpleDateFormat(DFMT).parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static Date getTime(String dateStr)  {
        if(StringUtils.isNotBlank(dateStr)){
            try {
                return new SimpleDateFormat(DFMT).parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static Map currentUser(){
        return getSession();
    }

    public static Integer getCurrentUserId(){
        return Integer.valueOf(currentUserId());
    }

    public static String currentUserId(){
        return getSession().get("_id").toString();
    }

    public static String currentUserNick(){
        return (String)getSession().get("nick_name");
    }

    public static int currentUserType(){
        return Integer.valueOf(currentUser().get("priv").toString());
    }


    private static  final SessionInterceptor ss = StaticSpring.get(SessionInterceptor.class);

    public static Map getSession(){
        return ss.getSession();
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

    public static String hexSeconds(){
       return Long.toHexString(System.currentTimeMillis()/1000);
    }

    /**
     * 数据填充到 request 中
     * @param request
     * @param map
     */
    public static void populate(HttpServletRequest request,Map<String,Object> map){
        for (Map.Entry<String,Object> entry: map.entrySet()){
            request.setAttribute(entry.getKey(),entry.getValue());
        }
    }



    static final String API_DOMAIN = AppProperties.get("api.domain", "http://ttapi.app100699141.twsapp.com/");
    static final Charset UTF8= Charset.forName("utf8");
    public static Object api(String url) throws IOException{
        Object obj = null ;

        try
        {

            String sUrl = API_DOMAIN + url;

            String json =  HttpClientUtil.get(sUrl, null, UTF8);

            obj = JSONUtil.jsonToMap(json).get("data");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return obj ;

    }
    

    public static Object api(String url,Boolean bFlag) throws IOException
    {
        Map<String,Object> map = new HashMap<String,Object>();
        String json =  HttpClientUtil.get(API_DOMAIN + url, null, UTF8);
        Map content = JSONUtil.jsonToMap(json) ;
        map.put("data",content.get("data"));
        if(bFlag)
        {    map.put("count",content.get("count")) ;
        }

        return map ;

    }




    public static  QueryBuilder fillTimeBetween(HttpServletRequest req){
        return fillTimeBetween(getStime(req),getEtime(req));
    }


    public static  QueryBuilder fillTimeBetween(QueryBuilder query, String field, HttpServletRequest req){
        return fillTimeBetween(query, field, getTime(req, "lstime"),getTime(req, "letime"));
    }

    public static  QueryBuilder fillTimeBetween(QueryBuilder query, String field, Date stime,Date etime){
        if (stime !=null || etime !=null){
            query.and(field);
            if(stime != null){
                query.greaterThanEquals(stime.getTime());
            }
            if (etime != null){
                query.lessThan(etime.getTime());
            }
        }
        return query;
    }


    public static  QueryBuilder fillTimeBetween(Date stime,Date etime){
        QueryBuilder query = QueryBuilder.start();
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

    public static final MongoTemplate adminMongo = (MongoTemplate) StaticSpring.get("adminMongo");
    public static void obtainGift(Integer userId,Integer gift_id,Integer count)
    {
        DBObject obj = new BasicDBObject("coin_price",1).append("ratio",1).append("category_id",1);
        DBObject  gift = adminMongo.getCollection("gifts").findOne(gift_id,obj) ;
        DBObject giftCategory = null ;
        String sCategoryId = gift.get("category_id").toString();
        if(StringUtils.isNotBlank(sCategoryId))
        {
            Integer categoryId = Integer.parseInt(sCategoryId) ;
            giftCategory = adminMongo.getCollection("gift_categories").findOne(categoryId);
        }
        Integer price = gift != null ? Integer.parseInt(gift.get("coin_price").toString()) : 0;
        Double ratio =  giftCategory != null ? Double.parseDouble(giftCategory.get("ratio").toString()) :0.0d ;
        //礼物分成比例
        if(null != gift.get("ratio"))
            ratio =  Double.parseDouble(gift.get("ratio").toString()) ;

        String s =  new java.text.SimpleDateFormat(
                "yyyyMMdd").format(new Date());
        String id = userId +"_" + s ;
        Long bean  =  new Double(price *count*ratio).longValue() ;

        logMongo.getCollection("obtain_gifts").findAndModify(new BasicDBObject(_id,id), null, null, false,
                new BasicDBObject($inc,new BasicDBObject("bag."+gift_id,count).append("gifts_value",bean))
                        .append($set,new BasicDBObject("user_id",userId).append(timestamp,System.currentTimeMillis())) ,true, true) ;

    }
    
    
    
    static final String UPLOAD_DOMAIN = AppProperties.get("upload.domain", "http://127.0.0.1:9000/");
    public static Object upload(String url) throws IOException{
        Object obj = null ;

        try
        {

            String sUrl = UPLOAD_DOMAIN + url;

            String json =  HttpClientUtil.get(sUrl, null, UTF8);

            obj = JSONUtil.jsonToMap(json);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return obj ;

    }
}
