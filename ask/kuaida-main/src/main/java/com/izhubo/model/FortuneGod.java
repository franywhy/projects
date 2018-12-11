package com.izhubo.model;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.izhubo.common.util.KeyUtils;
import com.izhubo.web.api.Web;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;
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
public enum FortuneGod
{
    FirstCoin2000(2000),

    SecondCoin1000(1000),

    AverageCoin(null);

    private final Integer coin;

    private FortuneGod(Integer coin)
    {
        this.coin = coin ;
    }

    public Integer getCoin()
    {
         return this.coin ;
    }

    public boolean awardOnly(Integer  userId,DBCollection users)
    {
        return  this.awardOnly(userId,users,this.coin);
    }

    public boolean awardOnly(Integer  userId,DBCollection users,Integer coin)
    {
        if(null != coin)
        {
            return 1 == users.update(new BasicDBObject(_id,userId),
                    new BasicDBObject($inc,new BasicDBObject(Finance.finance$coin_count,coin))).getN();
        }
        return false;
    }

   public Integer[] remove(int index,Integer[] userAllIDs)
   {
       List<Integer> my = new ArrayList<Integer>();
       for(Integer temp :userAllIDs)
           my.add(temp) ;

       my.remove(index);

       Integer[] userIds1 = my.toArray(new Integer[my.size()]);

       return userIds1 ;
   }
}
