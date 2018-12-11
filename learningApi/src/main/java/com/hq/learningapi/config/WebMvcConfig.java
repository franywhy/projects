package com.hq.learningapi.config;

import com.hq.common.entity.IpListEntity;
import com.hq.common.interceptor.IPHandlerInterceptor;
import com.hq.common.interceptor.LogHandlerInterceptor;
import com.hq.learningapi.interceptor.TokenHandlerInterceptor;
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
    private TokenHandlerInterceptor tokenHandlerInterceptor;

    @Autowired
    private IPWhiteListEntity entity;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        IpListEntity ipListEntity = new IpListEntity(entity.getFilterSwitch(), entity.getList());
        IPHandlerInterceptor ipHandlerInterceptor = new IPHandlerInterceptor(ipListEntity);
        InterceptorRegistration ipHandler = registry.addInterceptor(ipHandlerInterceptor);
        ipHandler.addPathPatterns("/api/**");


        InterceptorRegistration tokenHandler = registry.addInterceptor(tokenHandlerInterceptor);
        tokenHandler.addPathPatterns("/api/**");
        tokenHandler.addPathPatterns("/comment/add");
        tokenHandler.addPathPatterns("/like/**");
        tokenHandler.addPathPatterns("/buy/**");
        tokenHandler.excludePathPatterns("/buy/iapConfig");
        tokenHandler.excludePathPatterns("/buy/getMallGoodList");
        tokenHandler.excludePathPatterns("/buy/getMallGoodDetail");
        tokenHandler.excludePathPatterns("/api/constantValue");
        tokenHandler.excludePathPatterns("/api/userChannels");
        tokenHandler.excludePathPatterns("/api/getColdStartingList");
        tokenHandler.excludePathPatterns("/api/getColdStartingLatest");
        tokenHandler.excludePathPatterns("/api/discovery");
        tokenHandler.excludePathPatterns("/api/discovery_V410");
        tokenHandler.excludePathPatterns("/api/pc/marketCourse");
        tokenHandler.excludePathPatterns("/api/app/marketCourse");
        tokenHandler.excludePathPatterns("/api/app/marketCourseMostHot");
        tokenHandler.excludePathPatterns("/api/lastFourSchool");
        tokenHandler.excludePathPatterns("/api/courseBanner");
        tokenHandler.excludePathPatterns("/api/version");
        tokenHandler.excludePathPatterns("/api/getCourseNoList");
        tokenHandler.excludePathPatterns("/api/isAdaptiveUser");
        if (TRACER.isInfoEnabled()) {
            registry.addInterceptor(new LogHandlerInterceptor());
        }
    }
}
