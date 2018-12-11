package com.hqjy.msg.interceptor;


import com.hqjy.msg.model.IpListEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7 0007.
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Value("${ip.filterSwitch}")
    private boolean filterSwitch;

    @Value("${ip.urls}")
    private String urls;
    //@Bean
   /** public RemoteIpFilter remoteIpFilter() {
        return new RemoteIpFilter();
    }

    //@Bean
    public FilterRegistrationBean testFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
       /* registration.setFilter(new SSOClientFilter());
        registration.addUrlPatterns("*//*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("MyFilter");
        registration.setOrder(1);
        return registration;
    }
    */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截

        if(filterSwitch) {
            List<String> iplist = new ArrayList<String>();
            if (null != urls && !urls.trim().equals("")) {
                String[] strs = urls.split(",");
                for (int i = 0; i < strs.length; i++) {
                    iplist.add(strs[i]);
                }
            }
            IpListEntity ips = new IpListEntity();
            ips.setFilterSwitch(filterSwitch);
            ips.setList(iplist);
            registry.addInterceptor(new IPHandlerInterceptor(ips)).addPathPatterns("/**");
        }
        //registry.addInterceptor(new MyInterceptor2()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}