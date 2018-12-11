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
 * date: 13-3-4 下午6:12
 * @author: wubinjie@ak.cc
 */
@CompileStatic
public class LuckNumber {
    final static int rate = 100;
    final int number;
    public LuckNumber(int number) {
        this.number = number;
    }
    final AtomicInteger last=new AtomicInteger();
    final AtomicInteger lastLuck = new AtomicInteger(bulidLuckNum(0));
    public int time(int coin){
        int must = coin/rate;
        int may = coin%rate;
        if (may==0){
            return must;
        }
        int now =  last.addAndGet(may);
        int luck = lastLuck.get();
        if (luck < now &&  now <= luck + may ){
            must ++;
        }
        if (now>=rate){
            //lastLuck.weakCompareAndSet(luck,bulidLuckNum(last.addAndGet(-50)))
            lastLuck.set(bulidLuckNum(last.addAndGet(-rate)));
        }

        return must;
    }

    static StringRedisTemplate mainRedis = (StringRedisTemplate) StaticSpring.get("mainRedis");

    public static int power(){
        String power =  mainRedis.execute(LIST_POWER);
        if(power == null){
            pushPowerList();
            return 5;
        }
        return Integer.parseInt(power);
    }

    static final byte[] luckPower =  KeyUtils.serializer(KeyUtils.LUCK.powerList());
    static final RedisCallback<String> LIST_POWER = new RedisCallback<String>() {
        public String doInRedis(RedisConnection connection) throws DataAccessException {
            return KeyUtils.decode(connection.lPop(luckPower));
        }
    };
    private static void pushPowerList(){
        StaticNewSpring.execute(
                new Runnable() {
                    public void run() {
                        String[][] ranPower =  new String[][]{bulidPower(),bulidPower(),bulidPower()};
                        final List<byte[]> seq = new ArrayList<byte[]>(300);
                        for(String[] pseq : ranPower){
                            for (String i : pseq){
                                seq.add(KeyUtils.serializer(i));
                            }
                        }
                        mainRedis.execute(new RedisCallback<Object>() {
                            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                                connection.watch(luckPower);
                                connection.multi();
                                for(byte[] value:seq)
                                    connection.rPush(luckPower,value);
                                connection.exec();
                                return null;
                            }
                        });
                    }
                }
        );
    }

    static final Random RANDOM =  new Random();
    private static int bulidLuckNum(int last){
        return  last+ RANDOM.nextInt(rate -last);
    }

    // 每100 ，power 分片洗牌
    private static String[] bulidPower(){
        String[] powers = new String[100];
        int i=0;
        for(;i<40;i++){
            powers[i]="5";
        }
        for(;i<70;i++){
            powers[i]="10";
        }
        for(;i<85;i++){
            powers[i]="15";
        }
        for(;i<95;i++){
            powers[i]="20";
        }
        for(;i<100;i++){
            powers[i]="200";
        }
        for (; i>1; i--) // shuffle
            swap(powers, i-1, RANDOM.nextInt(i));
        return powers;
    }

    private static void swap(String[] arr, int i, int j) {
        String tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }





    public static void main(String[] args) {
        System.out.println(
                Arrays.toString(bulidPower())
        );
    }

}
