package com.hqjy.msg.config.mysql;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by baobao on 2017/12/12 0012.
 */
@Configuration
@MapperScan(basePackages = "com.hqjy.msg.mapper" /*,sqlSessionTemplateRef  = "sqlSessionFactory"*/)
public class MybatisConfiguration  {

    private static Log log = LogFactory.getLog(MybatisConfiguration.class);

    @Resource(name = "masterDataSource-01")
    private DataSource masterDataSourceOne;

    @Value("${druid.mapperMXLLoc}")
    private String mapperMXLLoc;
    /*@Value("${druid.pojoPackage}")
    private String pojoPackage;*/
    @Value("${druid.configLocations}")
    private String configLocations;
    @Bean(name="sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        log.info("--------------------  sqlSessionFactory init ---------------------");
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        sqlSessionFactoryBean.setDataSource(roundRobinDataSouceProxy());

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(mapperMXLLoc));
        //sqlSessionFactoryBean.setTypeAliasesPackage(pojoPackage);
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource(configLocations));
        //添加分页插件、打印sql插件
        //Interceptor[] plugins = new Interceptor[]{new SqlPrintInterceptor()};
        sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);

        return sqlSessionFactoryBean.getObject();

    }
    @Bean(name="roundRobinDataSouceProxy")
    public AbstractRoutingDataSource roundRobinDataSouceProxy(){
        AbstractRoutingDataSource  proxy = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return null;
            }
        };
        Map<Object,Object> targetDataResources = new HashMap();

        targetDataResources.put("masterDataSource-01",masterDataSourceOne);
        proxy.setDefaultTargetDataSource(masterDataSourceOne);//默认源
        proxy.setTargetDataSources(targetDataResources);

        return proxy;
    }
}
