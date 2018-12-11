package com.izhubo.common.doc;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.izhubo.rest.common.util.JSONUtil;
import com.izhubo.common.util.ChatExecutor;
import com.izhubo.common.util.KeyUtils;
import com.izhubo.web.api.MessageBulider;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.izhubo.web.api.Web.chatRedis;
import static com.izhubo.web.api.Web.mainMongo;

/**
 * date: 13-6-5 下午2:52
 *
 * @author: wubinjie@ak.cc
 */
public abstract class DelayMessage {

    static final BasicDBObject user_field = new BasicDBObject();
    static {
        user_field.put("nick_name", 1);
        user_field.put("finance", 1);
        user_field.put("pic", 1);
        user_field.put("priv", 1);
    }



    public static void pushUserInfo(final Integer userId){
        delayPublish(KeyUtils.CHANNEL.user(userId),2500L,new MessageBulider() {
            @Override
            public String build() {
                Map map = new HashMap<>();
                map.put("action","user.info");
                map.put("data_d", mainMongo.getCollection("users").findOne(userId,user_field));
                return JSONUtil.beanToJson(map);
            }
        });
    }

    public static final BasicDBObject star_field = new BasicDBObject();
    static {
        star_field.put("pic", 1);
        star_field.put("nick_name", 1);
        star_field.put("finance.bean_count_total", 1);
        star_field.put("finance.coin_spend_total", 1);
        star_field.put("finance.feather_receive_total", 1);
        star_field.put("vip", 1);
        star_field.put("tag", 1);
        star_field.put("star.room_id", 1);
        star_field.put("star.gift_week", 1);
    }

    public static void pushStarInfo(final Integer userId){
        delayPublish(KeyUtils.CHANNEL.user(userId),2500L,new MessageBulider() {
            @Override
            public String build() {
                Map map = new HashMap<>();
                map.put("action","room.star");
                DBObject user =  mainMongo.getCollection("users").findOne(userId, star_field);
                if(null == user){
                    return null;
                }

                map.put("data_d",user);

                //rankStar
//                Map starField =  (Map)user.get("star");
//                if(starField != null){
//                    starField.put("day_rank",)
//                }
//                ()?.put(,
//                        rankMongo.getCollection("star").findOne("day_"+user.get("_id"),new BasicDBObject("rank",1))?.get("rank")
//                )
                return JSONUtil.beanToJson(map);
            }
        });
    }


    private static final ConcurrentHashMap<String,Boolean> delayCached = new ConcurrentHashMap<>(4096);
    private static void delayPublish(final String key, final long millis, final MessageBulider bulider) {
        if( null ==  delayCached.putIfAbsent(key,Boolean.TRUE)) ChatExecutor.execute(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(millis);
                    String buildMsg = bulider.build();
                    if (null == buildMsg) {
                        return;
                    }
                    final byte[] msg = KeyUtils.serializer(buildMsg);
                    chatRedis.execute(new RedisCallback() {
                        @Override
                        public Object doInRedis(RedisConnection connection) throws DataAccessException {
                            connection.publish(KeyUtils.serializer(key), msg);
                            return null;
                        }
                    });
                } catch (InterruptedException ignored) {
                } finally {
                    delayCached.remove(key);
                }
            }
        });
    }
}
