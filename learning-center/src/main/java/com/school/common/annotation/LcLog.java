package com.school.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.school.utils.LogEnum;

/**
 * 学习中心日志注解
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LcLog {
	
	String value() default "";
	
	LogEnum type() default LogEnum.DEFAULT;
}
