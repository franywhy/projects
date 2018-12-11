package com.elise.singlesignoncenter.config;

import com.elise.singlesignoncenter.util.UserIdGenerator;
import com.hq.starter.AbstractRedisServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by Glenn on 2017/7/4 0004.
 */
@AutoConfigureAfter(AbstractRedisServiceConfiguration.class)
@Configuration
public class UIGConfig {

    @Value("${uig.name-space:users}")
    private String nameSpace;
    @Value("${uig.offset:10020000}")
    protected int offset;
    @Value("${uig.step:10}")
    private int step;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Bean
    public UserIdGenerator getUserIdGenerator(){
        UserIdGenerator uig = new UserIdGenerator(nameSpace,redisTemplate,offset,step);
        try {
            uig.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uig;
    }

}
