package com.bluewhale.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Glenn on 2017/4/24 0024.
 */

@Configuration
@ConditionalOnProperty(name = "swagger.enable",havingValue = "true")
@EnableConfigurationProperties(Swagger2Repository.class)
@EnableSwagger2
public class Swagger2Configuration {


    private final  Swagger2Repository SWAGGER_2_REPOSITORY;

    public Swagger2Configuration(Swagger2Repository swagger2Repository){
        SWAGGER_2_REPOSITORY = swagger2Repository;
    }


    @Bean
    @ConditionalOnMissingBean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(SWAGGER_2_REPOSITORY.getEnable())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_2_REPOSITORY.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(SWAGGER_2_REPOSITORY.getTitleName())
                .contact(SWAGGER_2_REPOSITORY.getAuthor())
                .version(SWAGGER_2_REPOSITORY.getVersion())
                .description(
                                "    OK(200, \"操作成功\"),\n" +
                                "    LATEST_VERSION(201, \"当前客户端最新版本\"),\n" +
                                "    NO_CONTENT(204, \"无内容\"),\n" +
                                "\n"+ "\n"+
                                "    BAD_REQUEST(400, \"错误的请求\"),\n" +
                                "    USER_TOKEN_NOT_FOUND(401, \"登录信息失效，请重新登录\"),\n" +
                                "    USER_NOT_FOUND(402, \"用户不存在\"),\n" +
                                "    PASSWORD_ERROR(403, \"密码输入错误\"),\n" +
                                "    INVALID_MOBILE_NUMBER(404, \"错误手机号码\"),\n" +
                                "    INVALID_PASS_WORD(405, \"密码格式非法\"),\n" +
                                "    NOT_FOUND(406, \"请求资源未找到\"),\n" +
                                "    REQUEST_REJECT(407, \"服务被拒绝\"),\n" +
                                "    UPLOAD_FAILED(408, \"上传文件失败\"),\n" +
                                "    DAMAGE_FILE(409, \"文件已经被损坏\"),\n" +
                                "    FILE_TOO_LARGE(410, \"文件过大\"),\n" +
                                "    FROZEN_USER(411, \"用户被冻结\"),\n" +
                                "    DUPLICATE_MOBILE_NUMBER(412, \"号码重复注册\"),\n" +
                                        "    UNKNOWN_BUSINESS_ID(413,\"未知的业务ID\"),\n"+
                                "\n" + "\n" +
                                "    INTERNAL_SERVER_ERROR(500, \"服务器内部错误\"),\n" +
                                "    CHECK_VERSION_FAILED(501, \"更新检查失败\")")

                .build();
    }
}
