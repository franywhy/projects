package com.elise.datacenter.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glenn on 2017/6/1 0001.
 */
@Configuration
public class CacheConfig {

    public static final String FILE_CACHE_KEY = "FileCache";

    @Value("${guava.cache.maximum-weight:#{null}}")
    private Long maximumWeight;
    @Value("${guava.cache.concurrency-level:#{null}}")
    private Integer concurrencyLevel;

    private GuavaCache buildHotelPositionCache() {
        LoadingCache<Object, Object> graphs = CacheBuilder.newBuilder()
                .maximumWeight(maximumWeight * 1024 * 1024)
                .concurrencyLevel(concurrencyLevel)
                .weigher(new Weigher<Object, Object>() {
                    @Override
                    public int weigh(Object o, Object o2) {
                        ByteBuffer b = (ByteBuffer) o2;
                        return b.capacity();
                    }
                })
                .build(new CacheLoader<Object, Object>() {
                    @Override
                    public Object load(Object o) throws Exception {
                        return ByteBuffer.allocate(0);
                    }
                });
        return new GuavaCache(FILE_CACHE_KEY, graphs);
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();
        List list = new ArrayList();
        list.add(buildHotelPositionCache());
        manager.setCaches(list);
        return manager;
    }
}
