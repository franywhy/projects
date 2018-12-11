package com.school.service.impl;

import com.school.dao.SysConfigDao;
import com.school.service.SysConfigService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by DL on 2018/11/22.
 */
@Service("sysConfigService")
public class SysConfigServiceImpl implements SysConfigService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysConfigDao sysConfigDao;
    @Resource
    protected StringRedisTemplate mainRedis;

    @Override
    public String getValue(String key, String defaultValue) {
        String value = "";
        String redisKey = "sysConfig:"+key;
        String valueStr = mainRedis.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(valueStr)){
           value = valueStr;
           logger.info("获取直播间地址,从缓存获取sysConfig:{}={}",key,value);
        }else {
            value = sysConfigDao.queryByKey(key);
            value = StringUtils.isBlank(value) ? defaultValue : value;
            mainRedis.opsForValue().set(redisKey,value);
            mainRedis.expire(redisKey,35*60, TimeUnit.SECONDS);
            logger.info("获取直播间地址,从数据库获取sysConfig:{}={}",key,value);
        }
        return value;
    }
}
