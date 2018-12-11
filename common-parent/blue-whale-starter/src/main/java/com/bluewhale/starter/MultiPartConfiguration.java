package com.bluewhale.starter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.boot.autoconfigure.web.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;

/**
 * Created by Glenn on 2017/8/9 0024.
 */


@Configuration
@ConditionalOnClass({ WebMvcProperties.Servlet.class, StandardServletMultipartResolver.class,
        MultipartConfigElement.class })
@ConditionalOnProperty(prefix = "multipart", name = "enabled", havingValue = "true")
@EnableConfigurationProperties({MultipartProperties.class,MultiPartRepository.class})

public class MultiPartConfiguration {

    @Autowired
    private MultipartProperties multipartProperties;


    private final  MultiPartRepository multiPartRepository;

    public MultiPartConfiguration(MultiPartRepository multiPartRepository){
         this.multiPartRepository = multiPartRepository;
    }

    @Bean
    @ConditionalOnMissingBean
    public MultipartConfigElement multipartConfigElement() {

        multipartProperties.setMaxRequestSize(multiPartRepository.getMaxRequestSize());
        multipartProperties.setEnabled(multiPartRepository.getEnabled());
        multipartProperties.setMaxFileSize(multiPartRepository.getMaxFileSize());
        return this.multipartProperties.createMultipartConfig();
    }

    @Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
    @ConditionalOnMissingBean(MultipartResolver.class)
    public StandardServletMultipartResolver multipartResolver() {
        StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
        multipartResolver.setResolveLazily(false);
        return multipartResolver;
    }


}
