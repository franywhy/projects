package com.school.accountant.util;

import java.io.UnsupportedEncodingException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
/**
 * 
 * RedisHelper
 * 
 */
@Component
public class RedisUtil{

	private static Logger log = LoggerFactory.getLogger(RedisUtil.class);
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	public static final String DEFAULT_KEY_CHARSET = "utf-8";
	private static final Long DEFAULT_EXPIRE_SECONDS = 30 * 60L;
	private static final Long PERMANENT = -1L;

	/**
	 * 批量删除
	 * 
	 * @param key
	 *            可变参数
	 */
	public long del(final String... keys) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				long result = 0;
				for (int i = 0; i < keys.length; i++) {
					result = connection.del(keys[i].getBytes());
				}
				return result;
			}
		});
	}
	
	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void set(final byte[] keys, final byte[] values, final long liveTime) {
		redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(keys, values);
				if (liveTime > 0) {
					connection.expire(keys, liveTime);
				}
				return 1L;
			}
		});
	}

	/**
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	public void set(String key, String value, long liveTime) {
		this.set(key.getBytes(), value.getBytes(), liveTime);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		this.set(key, value, RedisUtil.DEFAULT_EXPIRE_SECONDS);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public Long lpush(final String key, final String value,final long liveTime) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				Long number = connection.lPush(key.getBytes(), value.getBytes());
				if (liveTime > 0) {
					connection.expire(key.getBytes(), liveTime);
				}
				return number;
			}
		});
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public Long lpush(final String key, final String value) {
		return this.lpush(key, value, RedisUtil.PERMANENT);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void set(byte[] key, byte[] value) {
		this.set(key, value, 0L);
	}

	/**
	 * @param key
	 * @return
	 */
	public String get(final String key) {
		return redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] value = connection.get(key.getBytes());
				if (null == value) {
					return null;
				} else {
					try {
						return new String(value, RedisUtil.DEFAULT_KEY_CHARSET);
					} catch (UnsupportedEncodingException e) {
						log.error("UnsupportedEncodingException:{}", RedisUtil.DEFAULT_KEY_CHARSET, e);
						return null;
					}
				}
			}
		});
	}

	/**
	 * 通过正则匹配keys
	 * 
	 * @param pattern
	 * @param isPatternSerializer
	 * @return
	 */
	public Set keys(final String pattern, boolean isPatternSerializer) {
		if (isPatternSerializer) {
			return redisTemplate.keys(pattern);
		} else {
			return this.redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
				public Set<byte[]> doInRedis(RedisConnection connection) {
					return connection.keys(pattern.getBytes());
				}
			}, true);
		}
	}

	/**
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.exists(key.getBytes());
			}
		});
	}

	/**
	 * Delete all keys of the currently selected database。 this command never
	 * fails
	 * <p>
	 * See http://redis.io/commands/flushdb
	 * 
	 * @return ok
	 */
	public String flushDB() {
		return redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return "ok";
			}
		});
	}

	/**
	 * 查看redis里有多少数据
	 * 
	 * @return
	 */
	public long dbSize() {
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.dbSize();
			}
		});
	}

	/**
	 * 检测连接是否正常
	 * 
	 * @return
	 */
	public String ping() {
		return redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.ping();
			}
		});
	}

	public RedisUtil() {
	}

	public RedisUtil(RedisTemplate<String, String> rt) {
		redisTemplate = rt;
	}

	/**
	 * 获取过期时间
	 * 
	 * @param key
	 * @param isKeySerializer
	 * @return
	 */
	public Long getExpire(final String key, boolean isKeySerializer) {
		if (isKeySerializer) {
			return this.redisTemplate.getExpire(key);
		} else {
			return this.redisTemplate.execute(new RedisCallback<Long>() {
				@Override
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					return connection.ttl(key.getBytes());
				}
			});
		}
	}

	/**
	 * if given item exists in the set
	 * @param key
	 * @param item
	 * void
	 * @exception
	*/
	public boolean existsInSet(final String key, final String item) {
		return this.redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.sIsMember(key.getBytes(), item.getBytes());
			}
		});
	}

	/**
	 * hget 
	 * @param key
	 * @param field
	 * @return
	 * String
	 * @exception
	*/
	public String hget(final String key, final String field) {
		return this.redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] value = connection.hGet(key.getBytes(), field.getBytes());
				try {
					return value == null? null: new String(value,DEFAULT_KEY_CHARSET);
				} catch (UnsupportedEncodingException e) {
					log.error("UnsupportedEncodingException:{}", RedisUtil.DEFAULT_KEY_CHARSET, e);
					return null;
				}
			}
		});
	}

	/**
	 * hset(这里用一句话描述这个方法的作用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param key
	 * @param field
	 * @param value
	 * @return Boolean
	*/
	public Boolean hset(final String key, final String field,final String value) {
		return this.redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.hSet(key.getBytes(), field.getBytes(), value.getBytes());
			}
		});
	}
}
