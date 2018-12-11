package com.hqjy.pay.swagger;
import static springfox.documentation.builders.PathSelectors.regex;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {
	@Bean
	public Docket accessToken() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("/")// 定义组
				.select() // 选择那些路径和 api 会生成 document
				.apis(RequestHandlerSelectors.basePackage("com.hqjy.pay.controller")) // 拦截的包
				// 路径
				.paths(regex("/.*"))// 拦截的接口路径
				.build() // 创建
				.apiInfo(apiInfo()); // 配置说明
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()//
				.title("支付系统")// 标题
				.description("支付系统api")// 描述
				.version("1.0")// 版本
				.build();
	}
}
