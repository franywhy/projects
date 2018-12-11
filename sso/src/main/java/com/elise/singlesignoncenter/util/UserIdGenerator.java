package com.elise.singlesignoncenter.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;

import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Glenn on 2017/7/5 0005.
 */
public class UserIdGenerator {

    private String nameSpace;
    private StringRedisTemplate kgsRedis;
    private int defaultId ;
    private RedisAtomicInteger nextId;
    private int offset;
    private BitSet prettys ;
    volatile  int threshold;
    final AtomicInteger idGenerator = new AtomicInteger();

    public UserIdGenerator(String nameSpace, StringRedisTemplate kgsRedis, int defaultId, int offset){
        if(offset >  1)
            this.offset = offset;
        this.nameSpace = "kgs:"+nameSpace;
        this.kgsRedis = kgsRedis;
        this.defaultId = defaultId;
    }

    public void setPrettys(BitSet prettys) {
        this.prettys = prettys;
    }

    public void afterPropertiesSet() throws Exception {
        nextId = new RedisAtomicInteger(nameSpace + ":nextId",kgsRedis.getConnectionFactory());
        if(nextId.get() < defaultId){
            nextId.set(defaultId);
        }
        resetGenerator();
    }

    public Integer nextId(String nameSpace) {
        throw new UnsupportedOperationException("nameSpace is not support");
    }

    public int nextId(){
        int i = next();
        if(prettys  == null ){
            return i;
        }
        if(! prettys.get(i) ){
            return i;
        }
        int delta = prettys.nextClearBit(i) - i;
        if(delta>1){
            idGenerator.compareAndSet(i,i + delta -1);
        }
        return nextId();
    }

    private void resetGenerator(){
        int current = idGenerator.get();
        int high = 0;
        while (high <= current){
            high = nextId.getAndAdd(offset);
        }
        if(idGenerator.compareAndSet(current,high)){
            threshold = high + offset;
        }
    }

    private int next(){
        int next  = idGenerator.incrementAndGet();
        if(next >= threshold){
            synchronized (idGenerator){
                if(idGenerator.get()>= threshold){
                    resetGenerator();
                }
                return next();
            }
        }
        return next;
    }
}