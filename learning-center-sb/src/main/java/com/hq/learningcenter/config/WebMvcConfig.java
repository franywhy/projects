package com.hq.learningcenter.config;

import com.hq.learningcenter.config.props.IPWhiteListProperties;
import com.hq.learningcenter.interceptor.TokenHandlerInterceptor;
import com.hq.common.entity.IpListEntity;
import com.hq.common.interceptor.IPHandlerInterceptor;
import com.hq.common.interceptor.LogHandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Glenn on 2017/5/18 0018.
 * @author hq
 */
@Configuration
@AutoConfigureAfter(HttpMessageConvertersAutoConfiguration.class)
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private static final Logger TRACER = LoggerFactory.getLogger(WebMvcConfig.class);

    @Autowired
    private IPWhiteListProperties entity;

    @Autowired
    private TokenHandlerInterceptor tokenHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        IpListEntity ipListEntity = new IpListEntity(entity.getFilterSwitch(),entity.getList());
        IPHandlerInterceptor ipHandlerInterceptor = new IPHandlerInterceptor(ipListEntity);
        InterceptorRegistration ipHandler = registry.addInterceptor(ipHandlerInterceptor);
        ipHandler.addPathPatterns("/api/**");

        InterceptorRegistration tokenHandler = registry.addInterceptor(tokenHandlerInterceptor);
        tokenHandler.addPathPatterns("/api/**");
        tokenHandler.excludePathPatterns("/api/login", "/api/tLogin");
        tokenHandler.excludePathPatterns("/api/otpSMS");
        tokenHandler.excludePathPatterns("/api/resetPassword");
        tokenHandler.excludePathPatterns("/api/captcha-image");
        tokenHandler.excludePathPatterns("/api/appconfig/constantValue");
        tokenHandler.excludePathPatterns("/api/myself/version");
        if(TRACER.isInfoEnabled()) {
            registry.addInterceptor(new LogHandlerInterceptor());
        }
    }

}
