package com.elise.userinfocenter;

import org.apache.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
//@RemoteApplicationEventScan
@SpringBootApplication
@MapperScan("com.elise.userinfocenter.dao")
@EnableEurekaClient
public class UserInfoCenterApplication extends SpringBootServletInitializer {
    private final static Logger TRACER = Logger.getLogger(UserInfoCenterApplication.class);

    public static void main(String[] args) {
       /* SpringApplication application = new SpringApplication(UserInfoCenterApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.setLogStartupInfo(false);
        ConfigurableApplicationContext context = application.run(args);
        TRACER.info("UserInfoCenter Start Successfully");*/

        SpringApplication.run(UserInfoCenterApplication.class, args);
        TRACER.info("UserInfoCenter Start Successfully");
    }

}
