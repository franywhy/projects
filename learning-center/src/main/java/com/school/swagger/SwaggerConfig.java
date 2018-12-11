package com.school.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置
 *
 * @author wq
 * @since 2017-05-16
 */
@EnableWebMvc
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
    	
    	ParameterBuilder tokenPar = new ParameterBuilder();  
        List<Parameter> pars = new ArrayList<Parameter>();  
        tokenPar.name("X-Forward-School").description("businessId").modelRef(new ModelRef("string")).parameterType("header").required(false).build();  
        pars.add(tokenPar.build());  
    	
        return new Docket(DocumentationType.SWAGGER_2)
        		//.host("177.77.77.232:18086")
                .apiInfo(apiInfo())
                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.contract.controller")) // 注意修改此处的包名
                .apis(RequestHandlerSelectors.basePackage("com.school.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("学习中心接口列表 v1.1.0") // 任意，请稍微规范点
                .description("接口测试") // 任意，请稍微规范点
                .termsOfServiceUrl("http://localhost:8081/swagger-ui.html") // 将“url”换成自己的ip:port
                .contact("yaofeng") // 无所谓（这里是作者的别称）
                .version("1.1.0")
                .build();
    }
}