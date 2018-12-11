package com.elise.singlesignoncenter.config;

import com.elise.singlesignoncenter.interceptor.TokenHandlerInterceptor;
import com.elise.singlesignoncenter.resolver.BusinessIdHandlerMethodArgumentResolver;
import com.hq.common.entity.IpListEntity;
import com.hq.common.interceptor.IPHandlerInterceptor;
import com.hq.common.interceptor.LogHandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * MVC配置
 * Created by Glenn on 2017/5/18 0018.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private static final Logger TRACER = LoggerFactory.getLogger(WebMvcConfig.class);

    @Autowired
    private IPWhiteListEntity entity;
    @Autowired
    private BusinessIdHandlerMethodArgumentResolver businessIdHandlerMethodArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        IpListEntity ipListEntity = new IpListEntity(entity.getFilterSwitch(),entity.getList());
        IPHandlerInterceptor ipHandlerInterceptor = new IPHandlerInterceptor(ipListEntity);
        //IP白名单
        InterceptorRegistration ipHandler = registry.addInterceptor(ipHandlerInterceptor);
        ipHandler.addPathPatterns("/inner/*");
        //token拦截器
        InterceptorRegistration tokenHandler = registry.addInterceptor(new TokenHandlerInterceptor());
        //需要拦截的地址
        tokenHandler.addPathPatterns("/inner/*");
        //不需要拦截的地址
        tokenHandler.excludePathPatterns("/inner/login");
        tokenHandler.excludePathPatterns("/inner/otpSMS");
        tokenHandler.excludePathPatterns("/inner/register");
        tokenHandler.excludePathPatterns("/inner/passWord");
        tokenHandler.excludePathPatterns("/inner/checkSchoolId");
        tokenHandler.excludePathPatterns("/inner/checkMobileNo");
        tokenHandler.excludePathPatterns("/inner/tokenExpired");
        tokenHandler.excludePathPatterns("/inner/checkPassWord");

        if(TRACER.isInfoEnabled()) {
            registry.addInterceptor(new LogHandlerInterceptor());
        }
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(businessIdHandlerMethodArgumentResolver);
    }


}
