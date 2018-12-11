package com.hq.answerapi.config;

import com.hq.answerapi.model.HighLowKGS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by Administrator on 2018/9/19 0019.
 * @author hq
 */
@Configuration
public class KGSConfig {

    @Autowired
    private StringRedisTemplate mainRedis;

    @Bean
    public HighLowKGS userKGS() {
        HighLowKGS kgs = new HighLowKGS();
        kgs.setNameSpace("users");
        kgs.setKgsRedis(mainRedis);
        kgs.setStep(10);
        kgs.setOffset(10010032);
        return kgs;
    }
}
