package com.elise.datacenter.config;

import com.bluewhale.common.entity.IpListEntity;
import com.bluewhale.common.interceptor.IPHandlerInterceptor;
import com.bluewhale.common.interceptor.LogHandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Glenn on 2017/5/18 0018.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private static final Logger TRACER = LoggerFactory.getLogger(WebMvcConfig.class);

    @Autowired
    private IPWhiteListEntity entity;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        IpListEntity ipListEntity = new IpListEntity(entity.getFilterSwitch(),entity.getList());
        IPHandlerInterceptor ipHandlerInterceptor = new IPHandlerInterceptor(ipListEntity);

        InterceptorRegistration ipHandler = registry.addInterceptor(ipHandlerInterceptor);
        ipHandler.addPathPatterns("/api/**");
        if(TRACER.isInfoEnabled()) {
            registry.addInterceptor(new LogHandlerInterceptor());
        }
    }
}
