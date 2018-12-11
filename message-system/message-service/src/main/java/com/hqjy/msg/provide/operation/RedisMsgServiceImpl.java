package com.hqjy.msg.provide.operation;

import com.hqjy.msg.annotation.RedisIdxConnection;
import com.hqjy.msg.provide.RedisMsgService;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.ListUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Transaction;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("redisMsgServiceImpl")
@Scope("prototype")
public class RedisMsgServiceImpl<T> implements RedisMsgService<T> {

    private RedisTemplate redisTemplate;

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    @Override
    @RedisIdxConnection
    public boolean hasKey(String key) {

        return redisTemplate.hasKey(key);
    }

    @Override
    @RedisIdxConnection
    public void removeKey(String key) {
        redisTemplate.delete(key);

    }

    @Override
    @RedisIdxConnection
    public Object execute(String str) {
        return redisTemplate.getConnectionFactory().getConnection().execute(str, null);
    }

    @Override
    @RedisIdxConnection
    public boolean setExpire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.MINUTES);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     * @return 缓存的对象
     */
    @RedisIdxConnection
    public <T> ValueOperations<String, T> setCacheObject(String key, T value) {

        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        operation.set(key, value);
        return operation;
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     * @param time  过期时间
     * @return 缓存的对象
     */
    @RedisIdxConnection
    public <T> ValueOperations<String, T> setCacheObject(String key, T value, long time) {

        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        operation.set(key, value);
        redisTemplate.expire(key, time, TimeUnit.MINUTES);
        return operation;
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key       缓存键值
     * @param operation
     * @return 缓存键值对应的数据
     */
    @RedisIdxConnection
    public <T> T getCacheObject(
            String key/* ,ValueOperations<String,T> operation */) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    @RedisIdxConnection
    public <T> ListOperations<String, T> setCacheList(String key, List<T> dataList) {
        ListOperations listOperation = redisTemplate.opsForList();
        if (null != dataList) {
            int size = dataList.size();
            for (int i = 0; i < size; i++) {

                listOperation.rightPush(key, dataList.get(i));
            }
        }

        return listOperation;
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     * @param time  过期时间 单位分钟
     * @return 缓存的对象
     */
    @RedisIdxConnection
    public <T> ListOperations<String, T> setCacheList(String key, List<T> dataList, long time) {
        ListOperations listOperation = redisTemplate.opsForList();
        if (null != dataList) {
            int size = dataList.size();
            for (int i = 0; i < size; i++) {
                listOperation.rightPush(key, dataList.get(i));
            }
        }

        redisTemplate.expire(key, time, TimeUnit.MINUTES);
        return listOperation;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    @RedisIdxConnection
    public <T> List<T> getCacheList(String key) {
        List<T> dataList = new ArrayList<T>();
        ListOperations<String, T> listOperation = redisTemplate.opsForList();
        Long size = listOperation.size(key);

        for (int i = 0; i < size; i++) {
            dataList.add((T) listOperation.leftPop(key));
        }

        return dataList;
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    @RedisIdxConnection
    public <T> BoundZSetOperations<String, T> setCacheZSet(String key, T value, double time) {
        BoundZSetOperations<String, T> setOperation = redisTemplate.boundZSetOps(key);
        setOperation.add(value, time);
        return setOperation;
    }

    /**
     * 缓存Set
     *
     * @param key  缓存键值
     * @param sets 缓存的数据
     * @return 缓存数据的对象
     */
    @RedisIdxConnection
    public <T> BoundZSetOperations<String, T> setCacheZSet(String key, Set<ZSetOperations.TypedTuple<T>> sets) {
        BoundZSetOperations<String, T> setOperation = redisTemplate.boundZSetOps(key);
        /*
         * T[] t = (T[]) dataSet.toArray(); setOperation.add(t);
         */
        setOperation.add(sets);
        return setOperation;
    }

    /**
     * @param key
     * @param list
     */
    @RedisIdxConnection
    public synchronized void pipeline(String key, List list) {
        //pipeline
		/*RedisCallback<Object> pipelineCallback = new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				RedisConnection stringRedisConn = (RedisConnection)connection;
				byte[] rawValue = null;byte[] rawKey = null;

				for(int i=0; i< list.size(); i++) {
					Map map = (Map) list.get(i);
					String msg = (String) map.get("msg");
					long time = (long) map.get("time");
					String key = (String) map.get("user_id");
					rawKey = redisTemplate.getKeySerializer().serialize(key);
					rawValue = redisTemplate.getValueSerializer().serialize(msg);
					stringRedisConn.set(rawKey,time,rawValue);
				}
				return null;
			}
		};
 	 	Object results = redisTemplate.executePipelined(pipelineCallback);*/
        //redisTemplate.multi();//返回一个事务控制对象


        SessionCallback sessionCallback = new SessionCallback<List<Object>>() {

            @Override
            public List<Object> execute(RedisOperations operations)
                    throws DataAccessException {
                operations.multi();
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    String msg = (String) map.get("value");
                    long time = (long) map.get("score");
                    String key = (String) map.get(Constant.REDIS_KEY);
                    operations.opsForZSet().add(key, msg, time);
                }
                return operations.exec();
            }

        };
        redisTemplate.executePipelined(sessionCallback);
        //redisTemplate.execute(sessionCallback);
        //transaction.exec(sessionCallback);//执行
    }

    @Override
    @RedisIdxConnection
    public void remove(String key, String msgId, List list) {

        SessionCallback sessionCallback = new SessionCallback<Object>() {

            @Override
            public Object execute(RedisOperations operations)
                    throws DataAccessException {
                operations.multi();
                String value = "";
                String keys = "";
                String readedValue = "";
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    value = (String) map.get("value");
//                  value = msgId;
                    keys = (String) map.get(Constant.REDIS_KEY);
                    operations.opsForZSet().remove(keys, value);
                    readedValue = Constant.READED_START_KEY + msgId;
                    operations.opsForZSet().remove(keys, readedValue);
                }
                //operations.opsForZSet().remove(key,msgId);
                return operations.exec();
            }

        };
        //redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.executePipelined(sessionCallback);
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @param operation
     * @return
     */
    @RedisIdxConnection
    public Set<T> getCacheZSet(
            String key/* ,BoundSetOperations<String,T> operation */) {
        /* Set<T> dataSet = new HashSet<T>();*/
        BoundZSetOperations<String, T> operation = redisTemplate.boundZSetOps(key);

        return operation.range(0, -1);
    }

    /**
     * 删除缓存的set
     *
     * @param key
     * @param operation
     * @return
     */
    @RedisIdxConnection
    public void removeCacheZSet(
            String key/* ,BoundSetOperations<String,T> operation */, Object[] objects) {
        /* Set<T> dataSet = new HashSet<T>();*/
        BoundZSetOperations<String, T> operation = redisTemplate.boundZSetOps(key);

        operation.remove(objects);

    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    @RedisIdxConnection
    public <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        /*
         * T[] t = (T[]) dataSet.toArray(); setOperation.add(t);
         */
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            setOperation.add(it.next());
        }

        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @param operation
     * @return
     */
    @RedisIdxConnection
    public Set<T> getCacheSet(
            String key/* ,BoundSetOperations<String,T> operation */) {
        Set<T> dataSet = new HashSet<T>();
        BoundSetOperations<String, T> operation = redisTemplate.boundSetOps(key);
        Long size = operation.size();
        for (int i = 0; i < size; i++) {
            dataSet.add(operation.pop());
        }
        return dataSet;
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    @RedisIdxConnection
    public Set setsIntoCacheZSet(String key, List strs) {
        BoundZSetOperations<String, T> setOperation = redisTemplate.boundZSetOps(key);
        strs.stream().forEach(p -> {
            setOperation.unionAndStore((String) p, key);
        });
        return setOperation.range(0, -1);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     * @return
     */
    @RedisIdxConnection
    public <T> HashOperations<String, String, T> setCacheMap(String key, Map<String, T> dataMap, long time) {

        HashOperations hashOperations = redisTemplate.opsForHash();
        if (null != dataMap) {

            for (Map.Entry<String, T> entry : dataMap.entrySet()) {

                /*
                 * System.out.println("Key = " + entry.getKey() + ", Value = " +
                 * entry.getValue());
                 */
                hashOperations.put(key, entry.getKey(), entry.getValue());
            }
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.MINUTES);
            }


        }

        return hashOperations;
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @param hashOperation
     * @return
     */
    @RedisIdxConnection
    public <T> Map<String, T> getCacheMap(
            String key/* ,HashOperations<String,String,T> hashOperation */) {
        Map<String, T> map = redisTemplate.opsForHash().entries(key);
        /* Map<String, T> map = hashOperation.entries(key); */
        return map;
    }

    /**
     * 移除缓存的Map
     *
     * @param key
     * @param hashOperation
     * @return
     */
    @RedisIdxConnection
    public Long removeCacheMap(
            String key, Object column/* ,HashOperations<String,String,T> hashOperation */) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations.delete(key, column);

    }


    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     * @return
     */
    @RedisIdxConnection
    public <T> HashOperations<String, Integer, T> setCacheIntegerMap(String key, Map<Integer, T> dataMap) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        if (null != dataMap) {

            for (Map.Entry<Integer, T> entry : dataMap.entrySet()) {

                /*
                 * System.out.println("Key = " + entry.getKey() + ", Value = " +
                 * entry.getValue());
                 */
                hashOperations.put(key, entry.getKey(), entry.getValue());
            }

        }

        return hashOperations;
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @param hashOperation
     * @return
     */
    @RedisIdxConnection
    public <T> Map<Integer, T> getCacheIntegerMap(
            String key/* ,HashOperations<String,String,T> hashOperation */) {
        Map<Integer, T> map = redisTemplate.opsForHash().entries(key);
        /* Map<String, T> map = hashOperation.entries(key); */
        return map;
    }

    @Override
    @RedisIdxConnection
    public Object getZsetCache(String key, long startTime, long endTime, int size) {
        long lsize = -1;
        if (size < 0) {
            lsize = Integer.valueOf(size).longValue();
        }
        return redisTemplate.opsForZSet().rangeByScore(key, startTime, endTime, 0, lsize);
        /*SessionCallback sessionCallback = new SessionCallback<Object>() {

            @Override
            public Object execute(RedisOperations operations)
                    throws DataAccessException {
                operations.multi();

                operations.opsForZSet().rangeByScore(key, startTime, endTime);

                return operations.exec();
            }

        };

        Object list = redisTemplate.execute(sessionCallback);*/

        //return ListUtils.linkedHashSetsToStrings((List<LinkedHashSet>) list, size);
    }

    @Override
    @RedisIdxConnection
    public Object setZsetCacheUpdate(String key, String msgId) {
        Double score = redisTemplate.opsForZSet().score(key, msgId);
        if (null == score) return null;
        String readedValue = Constant.READED_START_KEY + msgId;
        Double score1 = redisTemplate.opsForZSet().score(key, readedValue);
        if (null != score1) return null;

        //System.out.println("score:" + score);
        SessionCallback sessionCallback = new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();

                operations.opsForZSet().add(key, readedValue, score);
                operations.opsForZSet().remove(key, msgId);
                return operations.exec();
            }
        };

        return this.redisTemplate.execute(sessionCallback);
    }

}