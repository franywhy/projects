  package com.hqjy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.logging.Logger;
@EnableEurekaClient
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
/*@MapperScan(basePackages = "com.antsoft.mapper")*/
  //@EnableScheduling
public class MessageApplication {
 	private static Logger logger = Logger.getLogger("MessageApplication");
	public static void main(String[] args) {
		SpringApplication.run(MessageApplication.class, args);
		logger.info("web-msg is running..");
 		logger.info("web-msg is start finished..");
 		System.out.println("web-msg is start finished..");
	}
}