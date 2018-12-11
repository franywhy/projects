package com.elise.datacenter.service;

import com.elise.datacenter.config.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

/**
 * Created by Glenn on 2017/6/1 0001.
 */
@Service
public class FileCacheService {

    @CachePut(cacheNames = CacheConfig.FILE_CACHE_KEY, key = "{#fileId}")
    public ByteBuffer putFile(String fileId, ByteBuffer buffer){
         return buffer;
    }

    @Cacheable(cacheNames = CacheConfig.FILE_CACHE_KEY, key = "{#fileId}")
    public ByteBuffer getFile(String fileId){
        return ByteBuffer.allocate(0);
    }

}
