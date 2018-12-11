package com.elise.singlesignoncenter;

import com.elise.singlesignoncenter.util.SpringUtil;
import org.apache.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by Glenn on 2017/4/26 0026.
 */


@MapperScan("com.elise.singlesignoncenter.dao")
@SpringBootApplication
@EnableEurekaClient
public class SingleSignOnCenterApplication {

	private final static Logger TRACER = Logger.getLogger(SingleSignOnCenterApplication.class);

	public static void main(String[] args) {
		SpringApplication application= new SpringApplication(SingleSignOnCenterApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		application.setLogStartupInfo(false);
		ConfigurableApplicationContext context = application.run(args);
		SpringUtil.setApplicationContext(context);
		TRACER.info("Service Start Successfully");
	}
}
