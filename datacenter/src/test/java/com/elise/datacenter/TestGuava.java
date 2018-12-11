package com.elise.datacenter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;
import org.springframework.cache.guava.GuavaCache;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2017/6/1 0001.
 */
public class TestGuava {

    public static void main(String args[]){
        LoadingCache<Object, Object> graphs = CacheBuilder.newBuilder()
                .maximumWeight(104857600L)
                .weigher(new Weigher<Object, Object>() {
                    @Override
                    public int weigh(Object o, Object o2) {
                        ByteBuffer b = (ByteBuffer)o2;
                        return b.capacity();
                    }
                })
                .build(new CacheLoader<Object, Object>() {
                    @Override
                    public Object load(Object o) throws Exception {
                        return null;
                    }
                });
    }
}
