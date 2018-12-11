package com.izhubo.rest.anno;

import com.izhubo.rest.web.spring.Interceptors;
import org.codehaus.groovy.transform.GroovyASTTransformationClass;
import org.springframework.stereotype.Controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * date: 13-6-4 下午5:25
 *
 * @author: wubinjie@ak.cc
 */
@java.lang.annotation.Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
@GroovyASTTransformationClass("com.izhubo.rest.anno.RestStaticCompileProcessor")
public @interface RestWithSession {
    Class[] value = {Controller.class, Interceptors.class};
}
