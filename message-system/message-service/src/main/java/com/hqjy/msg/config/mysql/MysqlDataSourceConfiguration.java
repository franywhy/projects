package com.hqjy.msg.config.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by baobao on 2017/12/12 0012.
 */
@Configuration
@EnableTransactionManagement
public class MysqlDataSourceConfiguration {
    private static Logger log = LoggerFactory.getLogger(MysqlDataSourceConfiguration.class);

    @Value("${druid.type}")
    private Class<? extends DataSource> dataSourceType;

    @Bean(name = "masterDataSource-01")
    @Primary
    @ConfigurationProperties(prefix = "druid.master-01")
    public DataSource masterDataSourceOne(){

        log.info("-------------------- masterDataSource-01 init ---------------------");
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

}
