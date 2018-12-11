package com.elise.datacenter;

import org.apache.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by Glenn on 2017/5/19 0019.
 */

@MapperScan("com.elise.datacenter.dao")
@SpringBootApplication
public class DataCenterApplication {


	private final static Logger TRACER = Logger.getLogger(DataCenterApplication.class);

	public static void main(String[] args) {
		SpringApplication application= new SpringApplication(DataCenterApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		application.setLogStartupInfo(false);
		ConfigurableApplicationContext context = application.run(args);
		TRACER.info("Service Start Successfully");
	}
}
