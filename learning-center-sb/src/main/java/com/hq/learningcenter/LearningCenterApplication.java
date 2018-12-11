package com.hq.learningcenter;

import org.apache.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author hq
 */
@SpringBootApplication
@MapperScan("com.hq.learningcenter.*.dao")
@EnableEurekaClient
public class LearningCenterApplication extends SpringBootServletInitializer {
    private final static Logger TRACER = Logger.getLogger(LearningCenterApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(LearningCenterApplication.class, args);
        TRACER.info("LearningCenter Start Successfully");
    }

}
