package com.izhubo.web;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.izhubo.rest.common.util.MsgDigestUtil;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;
import java.util.Random;

import static com.izhubo.web.api.Web.hexSeconds;

/**
 * Created with IntelliJ IDEA.
 * User: haigen.xiong
 * Date: 13-11-13
 * Time: 上午11:54
 * To change this template use File | Settings | File Templates.
 */
public class SysConfig
{

   public static final String CDN_WANG_SU = "wangsu" ;
   public static final String CDN_DI_LIAN = "dilian" ;
    private static final String[] cnd = {CDN_WANG_SU, CDN_DI_LIAN};

    public static  String getCDName(MongoTemplate adminMongo)
    {
        String cdn = ""  ;
        Random random = new Random();
        int inum = random.nextInt(100);
        int wangsuRate = getRateByName(cnd[0],adminMongo) ;
        int dilianRate =   getRateByName(cnd[1],adminMongo) ;
        if (inum >= 0 && inum < wangsuRate)
            cdn = cnd[0] ;
        else if(inum>= wangsuRate && inum < dilianRate + wangsuRate)
            cdn = cnd[1] ;
        else
            cdn = cnd[0] ;

        return cdn ;
    }

    private static  int getRateByName(String cdn,MongoTemplate adminMongo)
    {
        int rate = 0 ;

        DBObject obj = adminMongo.getCollection("sys_config").findOne(new BasicDBObject("name", cdn)) ;

         rate = (obj != null ? ((Long)obj.get("rate")).intValue()  : 50) ;

         return rate ;
    }

    static String getRtmpPullURLForAndroid(String cdnName,Integer roomId)
    {
        String time = hexSeconds();
        String  key_pull = "f4_d0s3gp_zfir5jr3qwxv19" ;
        String wsSecret = MsgDigestUtil.MD5.digest2HEX(key_pull+"/ttshow/"+roomId+".flv"+time) ;
       // String wsSecret = MsgDigestUtil.MD5.digest2HEX("f4_d0s3gp_zfir5jr3qwxv19/ttshow/${roomId}.flv${time}")
        String cdn = "ws" ;
        if(SysConfig.CDN_WANG_SU.equals(cdnName))
            cdn = "ws" ;
        else if(SysConfig.CDN_DI_LIAN.equals(cdnName))
            cdn = "dl" ;
     /*   String wsSecret = MsgDigestUtil.MD5.digest2HEX("f4_d0s3gp_zfir5jr3qwxv19/ttshow/${roomId}.flv${time}")

        return "http://pull.ws.izhubo.com/ttshow/${roomId}.flv?k=${wsSecret}&t=${time}"*/

        return "http://pull."+cdn+".izhubo.com/ttshow/"+roomId+".flv?k="+wsSecret+"&t="+time ;
    }


    //pc
    public static String getRtmpPullURL(String cdnName,Integer roomId)
    {
        String sURL = "" ;

        String t = hexSeconds() ;
        String k = "" ;
       String  key_pull = "f4_d0s3gp_zfir5jr3qwxv19" ;
        if(SysConfig.CDN_WANG_SU.equals(cdnName))
        {
             k = createKey(roomId, t,key_pull) ;
            sURL = "rtmp://live.ws.izhubo.com/ttshow/"+roomId+"?k="+k+"&t="+t  ;  //测试
            //sURL = "rtmp://pull.ws.izhubo.com/ttshow/"+roomId+"?k="+k+"&t="+t  ;   //product
        }
        else if(SysConfig.CDN_DI_LIAN.equals(cdnName))
        {

            k = createKey(roomId, t, key_pull) ;
            sURL = "rtmp://pull.dl.izhubo.com/ttshow/"+roomId+"?k="+k+"&t="+t  ;
        }

        return sURL ;
    }

    public static String getRtmpPushURL(String cdnName,Integer roomId)
    {
        String sURL = "" ;

        String t = hexSeconds() ;
        String k = "" ;
        String key_push =  "jel0_d3_uwpzq7e1_3q4vkdyeig" ;
        if(SysConfig.CDN_WANG_SU.equals(cdnName))
        {
            k = createKey(roomId, t,key_push) ;
            sURL = "rtmp://show.ws.izhubo.com/ttshow/"+roomId+"?k="+k+"&t="+t  ; //测试
           // sURL = "rtmp://push.ws.izhubo.com/ttshow/"+roomId+"?k="+k+"&t="+t  ;
        }
        else if(SysConfig.CDN_DI_LIAN.equals(cdnName))
        {
            k = createKey(roomId, t, key_push) ;
            sURL = "rtmp://push.dl.izhubo.com/ttshow/"+roomId+"?k="+k+"&t="+t  ;
        }


        return sURL ;
    }


    private static String createKey(Integer roomId, String time,String key)
    {
        return MsgDigestUtil.MD5.digest2HEX(key+"/ttshow/"+roomId+time) ;
    }


    public static void main(String[] ars)
    {
        long begin = new Date().getTime() - 7 * 24L * 3600 * 1000 ;
        System.out.println("begin------>:"+begin);

    }

}
