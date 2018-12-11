package com.hqjy.msg.util;

import com.hqjy.msg.enumeration.RedisType;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by baobao on 2017/12/21 0021.
 */
public class RedisUtils {

    public synchronized  static RedisTemplate setRedisConnection(String key ,Object object){
        if (null==key || key.equals("")) {
                return null;
        }
        String redis = RedisType.USER_REDIS.getValue();
        if (key.endsWith(Constant.WAIT_REDIS)) redis = RedisType.WAIT_REDIS.getValue();
        if (key.endsWith(Constant.MESSAGE_REDIS)) redis = RedisType.MESSAGE_REDIS.getValue();
        if (key.endsWith(Constant.CHANNEL_REDIS)) redis = RedisType.CHANNEL_REDIS.getValue();
        Class clz = object.getClass();
        Field [] fields = clz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.getType() == RedisTemplate.class) {
                RedisTemplate template = (RedisTemplate) SpringUtils.getBean(redis);
                try {
                    invoke(object,field.getName(),template);
                    return template;
                }catch (Exception e){

                }
            }
        }
        return null;
    }
    public synchronized  static void removeRedisConnection(Object object) throws Exception {
        Class clz = object.getClass();
        Field [] fields = clz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.getType() == RedisTemplate.class) {

                try {
                    RedisTemplate template = (RedisTemplate) getValue(object, field.getName());
                    template.getConnectionFactory().getConnection().close();

                } catch (Exception e) {

                }
            }
        }

    }

    /**
     * 用反射设置对象的属性值
     * @param obj 需要設置值的對象
     * @param fieldName 需要設置值的屬性
     * @param value 需要设置的值
     * @return 设置值后的对象
     */
    private static Object invoke(Object obj, String fieldName, Object value) throws Exception {
        String firstWord = fieldName.substring(0, 1).toUpperCase();
        String methodName = String.format("set%s%s", firstWord, fieldName.substring(1));
        Method method = obj.getClass().getMethod(methodName, value.getClass());
        method.invoke(obj, value);
        return obj;
    }

    /**
     * 用反射获取对象的属性值
     * @param obj 需要設置值的對象
     * @param fieldName 需要設置值的屬性
     * @return 对象的属性值
     */
    private static Object getValue(Object obj, String fieldName) throws Exception {
        String firstWord = fieldName.substring(0, 1).toUpperCase();
        String methodName = String.format("get%s%s", firstWord, fieldName.substring(1));
        Method method = obj.getClass().getMethod(methodName);
        return method.invoke(obj);
        //return obj;
    }

}
