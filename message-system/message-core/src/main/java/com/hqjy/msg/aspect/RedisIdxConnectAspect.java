package com.hqjy.msg.aspect;

import com.hqjy.msg.annotation.RedisIdxConnection;
import com.hqjy.msg.util.RedisUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/12/20 0020.
 */
@Aspect
@Component
public class RedisIdxConnectAspect implements Ordered {

    @Around("@annotation(redisIdxConnection)")
    public Object proceed(ProceedingJoinPoint proceedingJoinPoint, RedisIdxConnection redisIdxConnection) throws Throwable{

        Object object = proceedingJoinPoint.getTarget();
        String key = proceedingJoinPoint.getArgs()[0].toString();
        Object template = RedisUtils.setRedisConnection(key,object);
        Object result = proceedingJoinPoint.proceed();
        //RedisUtils.removeRedisConnection(object);
        return result;
    }

   @Override
    public int getOrder() {
        return 0;
   }



}
