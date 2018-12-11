  package com.hqjy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.logging.Logger;

  //@EnableEurekaClient
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
/*@MapperScan(basePackages = "com.antsoft.mapper")*/
  @EnableScheduling
public class BackMessageApplication {
 	private static Logger logger = Logger.getLogger("MessageApplication");
	public static void main(String[] args) {
		SpringApplication.run(BackMessageApplication.class, args);
		logger.info("back-msg is running..");
 		logger.info("back-msg is start finished..");
		System.out.println("back-msg is start finished..");

	}
}