package com.hq.bi.offline.task.factory;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * @author chenzeke
 * @date 2018/12/10 16:32
 */
public class MybatisSessionFactory {
    private static SqlSessionFactory sqlSessionFactory;

    private MybatisSessionFactory() {

    }

    synchronized public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {
            String resources = "mybatis-config.xml";
            InputStream inputStream = null;
            try {
                inputStream = Resources.getResourceAsStream(resources);
            } catch (Exception e) {
                e.printStackTrace();
            }
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }
        return sqlSessionFactory;

    }
}
