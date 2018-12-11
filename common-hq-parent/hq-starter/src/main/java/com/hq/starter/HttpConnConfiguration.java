package com.hq.starter;

import com.hq.http.HttpConnManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Glenn on 2017/8/9 0024.
 */


@Configuration
@EnableConfigurationProperties(HttpConnRepository.class)
@ConditionalOnClass(HttpConnManager.class)
public class HttpConnConfiguration {

    private final HttpConnRepository repository;

    public HttpConnConfiguration(HttpConnRepository repository){
             this.repository = repository;
    }

    @Bean
    @ConditionalOnMissingBean(HttpConnManager.class)
    public HttpConnManager getHttpConnManager(){
        HttpConnManager manager = new HttpConnManager();
        manager.init(repository.getTimeout().getConn(),
                repository.getTimeout().getRequest(),
                repository.getTimeout().getSocket(),
                repository.getPool().getMaxTotal(),
                repository.getPool().getRouteTotal(),
                repository.getRetryCount(),
                repository.getExpectContinueEnable());
        return manager;
    }


}
