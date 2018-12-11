package com.hqjy.msg.provide;

import com.hqjy.msg.annotation.RedisIdxConnection;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
public interface RedisMsgService<T> {
    public void removeKey(String key);

    public boolean setExpire(String key,long time);

    /**
     * 是否存在key
     * @param key
     * @return true:存在 false:不存在
     */
    public boolean hasKey(String key);

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key
     *            缓存的键值
     * @param value
     *            缓存的值
     * @return 缓存的对象
     */
    @RedisIdxConnection
    public <T> ValueOperations<String, T> setCacheObject(String key, T value);

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key
     *            缓存的键值
     * @param value
     *            缓存的值
     * @param time
     *            过期时间
     * @return 缓存的对象
     */
    @RedisIdxConnection
    public <T> ValueOperations<String, T> setCacheObject(String key, T value,long time);

    /**
     * 获得缓存的基本对象。
     *
     * @param key
     *            缓存键值
     * @param operation
     * @return 缓存键值对应的数据
     */
    @RedisIdxConnection
    public <T> T getCacheObject( String key);

    /**
     * 缓存List数据
     *
     * @param key
     *            缓存的键值
     * @param dataList
     *            待缓存的List数据
     * @return 缓存的对象
     */
    @RedisIdxConnection
    public <T> ListOperations<String, T> setCacheList(String key, List<T> dataList);

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key
     *            缓存的键值
     * @param value
     *            缓存的值
     * @param time
     *            过期时间 单位分钟
     * @return 缓存的对象
     */
    @RedisIdxConnection
    public <T> ListOperations<String, T> setCacheList(String key, List<T> dataList, long time);

    /**
     * 获得缓存的list对象
     *
     * @param key
     *            缓存的键值
     * @return 缓存键值对应的数据
     */
    @RedisIdxConnection
    public <T> List<T> getCacheList(String key);

    /**
     * 缓存Set
     *
     * @param key
     *            缓存键值
     * @param dataSet
     *            缓存的数据
     * @return 缓存数据的对象
     */
    @RedisIdxConnection
    public <T> BoundZSetOperations<String, T> setCacheZSet(String key, T value, double time);

    /**
     * 缓存Set
     *
     * @param key
     *            缓存键值
     * @param sets
     *            缓存的数据
     * @return 缓存数据的对象
     */
    @RedisIdxConnection
    public <T> BoundZSetOperations<String, T> setCacheZSet(String key,Set<ZSetOperations.TypedTuple<T>> sets);

    /**
     *
     * @param key
     * @param list
     */
    @RedisIdxConnection
    public void pipeline(String key,List list);

    /**
     *
     * @param key
     * @param list
     */
    @RedisIdxConnection
    public void remove(String key,String msgId,List list);

    /**
     * 获得缓存的set
     *
     * @param key
     * @param operation
     * @return
     */
    @RedisIdxConnection
    public Set<T> getCacheZSet( String key);

    /**
     * 缓存Set
     *
     * @param key
     *            缓存键值
     * @param dataSet
     *            缓存的数据
     * @return 缓存数据的对象
     */
    @RedisIdxConnection
    public <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet);

    /**
     * 获得缓存的set
     *
     * @param key
     * @param operation
     * @return
     */
    @RedisIdxConnection
    public Set<T> getCacheSet(String key);

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     * @return
     */
    @RedisIdxConnection
    public <T> HashOperations<String, String, T> setCacheMap(String key, Map<String, T> dataMap,long time);


    /**
     * 获得缓存的Map
     *
     * @param key
     * @param hashOperation
     * @return
     */
    @RedisIdxConnection
    public <T> Map<String, T> getCacheMap(String key);

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     * @return
     */
    @RedisIdxConnection
    public <T> HashOperations<String, Integer, T> setCacheIntegerMap(String key, Map<Integer, T> dataMap);

    /**
     * 获得缓存的Map
     *
     * @param key
     * @param hashOperation
     * @return
     */
    @RedisIdxConnection
    public <T> Map<Integer, T> getCacheIntegerMap(String key);

    /**
     * 获得Zset范围内的值
     *
     * @param key
     * @param startTime
     * @param endTime
     * @return
     */
    @RedisIdxConnection
    public Object getZsetCache(String key,long startTime,long endTime,int size);

    /**
     * 获得Zset范围内的值
     *
     * @param key
     * @param msgId
     * @return
     */
    @RedisIdxConnection
    public Object setZsetCacheUpdate(String key,String msgId);

    /**
     * 删除缓存的set
     *
     * @param key
     * @param objects
     * @return
     */
    @RedisIdxConnection
    public void removeCacheZSet(
            String key/* ,BoundSetOperations<String,T> operation */,Object [] objects);

    /**
     * 移除缓存的Map
     *
     * @param key
     * @param hashOperation
     * @return
     */
    @RedisIdxConnection
    public Long removeCacheMap(
            String key,Object column/* ,HashOperations<String,String,T> hashOperation */);

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    @RedisIdxConnection
    public Set setsIntoCacheZSet(String key, List strs);

    public Object execute(String str);


}
