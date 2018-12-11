package com.hqjy.pay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@EnableSwagger2
@SpringBootApplication
@ComponentScan({ "com.hqjy.pay.controller","com.hqjy.pay.impl", "com.hqjy.pay.pojo" })
@MapperScan(basePackages = "com.hqjy.pay.mapper")
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
