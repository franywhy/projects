package com.izhubo.model;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.izhubo.common.util.KeyUtils;
import com.izhubo.web.api.Web;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.izhubo.rest.common.doc.MongoKey.*;
import static com.izhubo.rest.common.util.WebUtils.$$;

/**
 * big Wheel 大转盘
 *
 *
 *
 * date: 13-8-13 下午4:28
 *
 * @author: wubinjie@ak.cc
 */
public enum GiftBox
{
    /* 礼盒一 	3000星币       玫瑰5个 	    30星币
      礼盒二 	5000星币 	   喉宝10个 	100星币
      礼盒三 	8000星币 	   麦克风16个 	320星币
      礼盒四 	10000星币 	   掌声50个 	500星币
      礼盒五 	30000星币 	   西瓜100个 	1000星币
      礼盒六 	50000星币 	   笑一个1个 	2000星币
      礼盒七 	80000星币 	   砸屏1个 	    4000星币
      礼盒八 	100000星币 	   浪漫烟花1个 	5000星币*/

    //圣诞
    MeiGui5(6,5),
    HouBao10(263,10),
    MaiKeFeng16(9,16),
    ZhangSheng50(7,50),
    XiGua100(265,100),
    Xiao1(224,1),
    ZaPing(226,1),
    YanHua1(21,1),
    CaiShen(283,1);

   static Logger logger = LoggerFactory.getLogger(GiftBox.class) ;
   private final Integer giftId,giftCount;

    private GiftBox(Integer giftId, Integer giftCount) {
        this.giftId = giftId;
        this.giftCount = giftCount;
    }



   public Integer getGiftsId()
   {
       return  this.giftId ;
   }



    public boolean awardOnly(Integer roomId,DBCollection users){

        if( null != giftId)
        {
            return 1 ==  users.update(new BasicDBObject(_id,roomId),
                    new BasicDBObject($inc,new BasicDBObject("bag."+giftId,giftCount))).getN();
        }

        return false;
    }


    public static GiftBox getGiftBox(Long pcoin,Integer num)
    {
        GiftBox giftBox = null ;
        Long coin = pcoin ;
        logger.info("coin1------>:{}",coin);
        if(num>=2)
            coin = pcoin -((num-1)*100000L) ;
        logger.info("coin2------>:{}",coin);

       if(coin>=3000L && coin <5000L)
           giftBox = MeiGui5 ;

       else if(coin >= 5000L && coin<8000L)
           giftBox = HouBao10 ;
       else if(coin >= 8000L && coin<10000L)
           giftBox = MaiKeFeng16 ;
       else if(coin >= 10000L && coin<30000L)
           giftBox = ZhangSheng50 ;
       else if(coin >= 30000L && coin< 50000L)
           giftBox = XiGua100 ;
       else if(coin >= 50000L && coin< 80000L)
                giftBox = Xiao1 ;
       else if(coin >= 80000L && coin< 100000L)
           giftBox = ZaPing ;

        return giftBox ;
    }

    public static int getStep(Long pcoin,Integer num)
    {
        int  step = 1 ;
        Long coin = pcoin ;
        logger.info("coin1------>:{}",coin);
        if(num>=2)
            coin = pcoin -((num-1)*100000L) ;
        logger.info("coin2------>:{}",coin);

        if(coin>=3000L && coin <5000L)
            step = 1;
        else if(coin >= 5000L && coin<8000L)
            step = 2 ;
        else if(coin >= 8000L && coin<10000L)
            step = 3 ;
        else if(coin >= 10000L && coin<30000L)
            step = 4 ;
        else if(coin >= 30000L && coin< 50000L)
            step = 5;
        else if(coin >= 50000L && coin< 80000L)
            step = 6;
        else if(coin >= 80000L && coin< 100000L)
            step = 7;
        return step ;
    }



}
