package com.izhubo.common.util;

import com.izhubo.rest.web.StaticSpring;
import groovy.transform.CompileStatic;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 护士活动礼物抽奖
 * date: 14-4-30 下午6:12
 */
@CompileStatic
public class PrizeNumber {

    static StringRedisTemplate mainRedis = (StringRedisTemplate) StaticSpring.get("mainRedis");

    public static Integer getRandomGift(Integer star_id){
        Integer gift_id = null;
        final String gift_key = "hushi:"+star_id+":list_gift";
        final byte[] gift_keys =  KeyUtils.serializer(gift_key);
        RedisCallback<String> LIST_GIFT = new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                return KeyUtils.decode(connection.lPop(gift_keys));
            }
        };
        String gift = mainRedis.execute(LIST_GIFT);
        if(gift != null)
            gift_id = Integer.parseInt(gift);

        return gift_id;
    }

    public static Integer getGiftCount(Integer star_id){
        Integer gift_count = 0;
        final String gift_key = "hushi:"+star_id+":list_gift";
        final byte[] gift_keys =  KeyUtils.serializer(gift_key);
        RedisCallback<String> COUNT_GIFT = new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.lLen(gift_keys).toString();
            }
        };
        String gift = mainRedis.execute(COUNT_GIFT);
        if(gift != null)
            gift_count = Integer.parseInt(gift);

        return gift_count;
    }

    public static void pushGiftList(final Integer star_id){
        String[][] ranPower =  new String[][]{bulidPower()};
        final List<byte[]> seq = new ArrayList<byte[]>(300);
        for(String[] pseq : ranPower){
            for (String i : pseq){
                seq.add(KeyUtils.serializer(i));
            }
        }

        mainRedis.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                final String gift_key = "hushi:"+star_id+":list_gift";
                final byte[] gift_keys =  KeyUtils.serializer(gift_key);
                connection.watch(gift_keys);
                connection.multi();
                for(byte[] value:seq)
                    connection.rPush(gift_keys,value);
                connection.exec();
                return null;
            }
        });
    }

    static final Random RANDOM =  new Random();
    //
    private static String[] bulidPower(){
        String[] gifts = {"21","225","6","259","336","289","268","262","265","7"};
        int i=gifts.length;
        for (; i>0; i--) // shuffle
            swap(gifts, i-1, RANDOM.nextInt(i));
        return gifts;
    }

    private static void swap(String[] arr, int i, int j) {
        String tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }


    public static void main(String[] args) {
        System.out.println(Arrays.toString(bulidPower()));
        System.out.println(Arrays.toString(bulidPower()));
        System.out.println(Arrays.toString(bulidPower()));
    }

}
