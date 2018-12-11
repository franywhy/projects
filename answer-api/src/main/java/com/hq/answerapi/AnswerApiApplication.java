package com.hq.answerapi;

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
@MapperScan("com.hq.answerapi.dao")
@EnableEurekaClient
public class AnswerApiApplication extends SpringBootServletInitializer {
    private final static Logger TRACER = Logger.getLogger(AnswerApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AnswerApiApplication.class, args);
        TRACER.info("AnswerApi Start Successfully");
    }

}
