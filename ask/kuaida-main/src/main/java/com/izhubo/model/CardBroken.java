package com.izhubo.model;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.izhubo.common.util.KeyUtils;
import com.izhubo.web.api.Web;
import org.apache.commons.lang.StringUtils;
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
 * Egg. Broken.
 *
 * http://redmine.izhubo.com//redmine/projects/xinyuan/wiki/%E6%95%B0%E5%80%BC%E8%AE%BE%E5%AE%9A#砸金蛋
 *
 * date: 13-8-13 下午4:28
 *
 * @author: wubinjie@ak.cc
 */
public enum CardBroken {
/*

    其它文字：多福多寿、恭喜发财、财运亨通、幸福美满、吉祥如意
    中奖文字：龙马精神、骏马迎春、万马奔腾、马到成功、金马纳福
*/
    DuoFuDuoShou(null),
    GongXiFaCai(null),
    CaiYunHengTong(null),
    XingFuMeiMan(null),
    JiXiangRuYi(null),

    LongMaJingShen,
    JunMaYingChun,
    WanMaBenTeng,
    MaDaoChengGong,
    JinMaNaFu;
    private final Integer  coin ;


    private CardBroken() {
        this(1000);
    }

    private CardBroken(Integer coin) {
        this.coin = coin;

    }

    public boolean awardOnly(Integer userId,DBCollection users,Integer a_coin)
    {
        if(null != coin)
        {
            Integer award_coin = coin ;
            if(null != a_coin)
                award_coin = a_coin ;
            return 1 == users.update(new BasicDBObject(_id,userId),
                    new BasicDBObject($inc,new BasicDBObject(Finance.finance$coin_count,award_coin))).getN();
        }
        return false ;

    }

    static final CardBroken[] RAN1 = new CardBroken[1000];
    static final CardBroken[] RAN2 = new CardBroken[1000];
    static final CardBroken[] RAN3 = new CardBroken[1000];

    static {//init RAN1
        int i=0;
        for(;i<150;i++){
            RAN1[i] = DuoFuDuoShou;
        }
        for(;i<300;i++){
            RAN1[i] = GongXiFaCai;
        }
        for(;i<450;i++){
            RAN1[i] = CaiYunHengTong;
        }
        for(;i<600;i++){
            RAN1[i] = XingFuMeiMan;
        }
        for(;i<750;i++){
            RAN1[i] = JiXiangRuYi;
        }
        for(;i<800;i++){
            RAN1[i] = LongMaJingShen;
        }
        for(;i<850;i++){
            RAN1[i] = JunMaYingChun;
        }
        for(;i<900;i++){
            RAN1[i] = WanMaBenTeng;
        }
        for(;i<950;i++){
            RAN1[i] = MaDaoChengGong;
        }
        for(;i<999;i++){
            RAN1[i] = JinMaNaFu;
        }
        RAN1[999] = JinMaNaFu;
        shuffle(RAN1);
    }


    static {//init RAN2
        int i=0;
        for(;i<160;i++){
            RAN2[i] = DuoFuDuoShou;
        }
        for(;i<320;i++){
            RAN2[i] = GongXiFaCai;
        }
        for(;i<480;i++){
            RAN2[i] = CaiYunHengTong;
        }
        for(;i<640;i++){
            RAN2[i] = XingFuMeiMan;
        }
        for(;i<800;i++){
            RAN2[i] = JiXiangRuYi;
        }
       for(;i<840;i++){
          RAN2[i] = LongMaJingShen;
       }
       for(;i<880;i++){
          RAN2[i] = JunMaYingChun;
       }
      for(;i<920;i++){
          RAN2[i] = WanMaBenTeng;
      }
      for(;i<960;i++){
          RAN2[i] = MaDaoChengGong;
      }
      for(;i<999;i++){
          RAN2[i] = JinMaNaFu;
      }
      RAN2[999] = JinMaNaFu;
      shuffle(RAN2);
    }

    static {//init RAN3
        int i=0;
        for(;i<180;i++){
            RAN3[i] = DuoFuDuoShou;
        }
        for(;i<360;i++){
            RAN3[i] = GongXiFaCai;
        }
        for(;i<540;i++){
            RAN3[i] = CaiYunHengTong;
        }
        for(;i<720;i++){
            RAN3[i] = XingFuMeiMan;
        }
        for(;i<900;i++){
            RAN3[i] = JiXiangRuYi;
        }
        for(;i<920;i++){
            RAN3[i] = LongMaJingShen;
        }
        for(;i<940;i++){
            RAN3[i] = JunMaYingChun;
        }
        for(;i<960;i++){
            RAN3[i] = WanMaBenTeng;
        }
        for(;i<980;i++){
            RAN3[i] = MaDaoChengGong;
        }
        for(;i<999;i++){
            RAN3[i] = JinMaNaFu;
        }
        RAN3[999] = JinMaNaFu;
        shuffle(RAN3);
    }


    public static CardBroken ran1(){
        return RAN1[new Random().nextInt(1000)];
    }
    public static CardBroken ran2(){
        return RAN2[new Random().nextInt(1000)];
    }
    public static CardBroken ran3(){
        return RAN3[new Random().nextInt(1000)];
    }

    static void shuffle(CardBroken[] ar){
        Random rnd = new Random();
        for (int i = ar.length - 1; i >= 0; i--){
            int index = rnd.nextInt(i + 1);
            // Simple swap
            CardBroken a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
