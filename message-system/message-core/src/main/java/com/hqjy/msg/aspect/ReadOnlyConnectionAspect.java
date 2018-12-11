package com.hqjy.msg.aspect;


import com.hqjy.msg.annotation.ReadOnlyConnection;
import com.hqjy.msg.enumeration.DbContextHolder;
import com.hqjy.msg.util.DatabaseUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * Created by baobao on 2017/12/12 0012.
 * @author baobao
 * @email liangdongbin@hengqijy.com
 * @date 2017-12-20 14:20:09
 * @descrition 切面容器类
 */
@Aspect
@Component
public class ReadOnlyConnectionAspect implements Ordered {
    private static Logger logger = LoggerFactory.getLogger(ReadOnlyConnectionAspect.class);

    /**
     * 获取注解为readOnlyConnection的方法并进行切面注入行为
     * @param proceedingJoinPoint
     * @param readOnlyConnection
     * @return
     * @throws Throwable
     */
    @Around("@annotation(readOnlyConnection)")
    public Object proceed(ProceedingJoinPoint proceedingJoinPoint, ReadOnlyConnection readOnlyConnection) throws Throwable{
        try {
            /*logger.info("set database connection to read only");
            DbContextHolder.setDbType(DbContextHolder.DbType.SLAVE);
            logger.info(DbContextHolder.getDbType().toString());
           Object obj = proceedingJoinPoint.getTarget();
            Class clz = obj.getClass();
            DatabaseUtils.setSlaveJdbcTemplate(clz);*/
            Object result = proceedingJoinPoint.proceed();
            return result;
        }finally {
            //DbContextHolder.clearDbType();
            //logger.info("restore database connection");
        }
    }



    @Override
    public int getOrder() {
        return 0;
    }
}
