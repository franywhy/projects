package com.hq.learningcenter.config;

import com.hq.learningcenter.school.rest.persistent.HighLowKGS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by longduyuan on 2018/11/28 0028.
 * @author hq
 */
@Configuration
public class KGSConfig {

    @Autowired
    private StringRedisTemplate adminRedisTemplate;

    @Bean
    public HighLowKGS courseAbnormalOrderNoKGS() {
        HighLowKGS kgs = new HighLowKGS();
        kgs.setNameSpace("courseAbnormalOrderNo");
        kgs.setKgsRedis(adminRedisTemplate);
        kgs.setStep(10);
        kgs.setOffset(100000);
        return kgs;
    }

    @Bean
    public HighLowKGS courseAbnormalFreeAssessmentNoKGS() {
        HighLowKGS kgs = new HighLowKGS();
        kgs.setNameSpace("courseAbnormalFreeAssessmentNo");
        kgs.setKgsRedis(adminRedisTemplate);
        kgs.setStep(10);
        kgs.setOffset(100000);
        return kgs;
    }

    @Bean
    public HighLowKGS courseAbnormallRegisterationPKKGS() {
        HighLowKGS kgs = new HighLowKGS();
        kgs.setNameSpace("baoKaoNo");
        kgs.setKgsRedis(adminRedisTemplate);
        kgs.setStep(10);
        kgs.setOffset(100000);
        return kgs;
    }

    @Bean
    public HighLowKGS invoicesNumberKGS() {
        HighLowKGS kgs = new HighLowKGS();
        kgs.setNameSpace("invoicesNumberKGS");
        kgs.setKgsRedis(adminRedisTemplate);
        kgs.setStep(10);
        kgs.setOffset(100000);
        return kgs;
    }

}
