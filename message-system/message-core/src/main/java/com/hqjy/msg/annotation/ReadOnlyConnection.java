package com.hqjy.msg.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by baobao on 2017/12/12 0012.
 * @author baobao
 * @email liangdongbin@hengqijy.com
 * @date 2017-12-20 14:20:09
 * @descrition 注解，用来注解只读的方法
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReadOnlyConnection {

}
