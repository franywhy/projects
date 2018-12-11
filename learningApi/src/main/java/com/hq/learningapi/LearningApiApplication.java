package com.hq.learningapi;

import org.apache.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication

@EnableSwagger2
@EnableTransactionManagement
@MapperScan("com.hq.learningapi.dao")
@EnableEurekaClient
public class LearningApiApplication {

	private final static Logger TRACER = Logger.getLogger(LearningApiApplication.class);

	public static void main(String[] args) {

		SpringApplication application= new SpringApplication(LearningApiApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		application.setLogStartupInfo(false);
		
		ConfigurableApplicationContext context = application.run(args);
		TRACER.info("Service Start Successfully");
	}
}
