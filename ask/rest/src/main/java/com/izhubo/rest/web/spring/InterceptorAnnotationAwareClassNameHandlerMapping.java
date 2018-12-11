package com.izhubo.rest.web.spring;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping;

/**
 *
 * 自动处理标记的拦截器
 *
 * date: 13-3-7 上午10:39
 *
 * @author: wubinjie@ak.cc
 */
public class InterceptorAnnotationAwareClassNameHandlerMapping extends ControllerClassNameHandlerMapping {
	private static Logger logger = LoggerFactory
			.getLogger(InterceptorAnnotationAwareClassNameHandlerMapping.class);
	
    protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request) {
        HandlerExecutionChain chain =super.getHandlerExecutionChain(handler,request);
        HandlerInterceptor[] interceptors = detectInterceptors(chain.getHandler().getClass());
        if(null != interceptors)
        chain.addInterceptors(interceptors);
        return chain;
    }

    protected HandlerInterceptor[] detectInterceptors(Class handlerClass) {

//    	logger.info("class: {}", handlerClass);
        Interceptors interceptorAnnot = AnnotationUtils.findAnnotation(handlerClass, Interceptors.class);
        //logger.info("interceptorAnnot: {}", interceptorAnnot);
        if (interceptorAnnot != null) {
            String[] beanNames = interceptorAnnot.value();
//            logger.info("beanNames: {}", beanNames);
            if (beanNames != null) {
                HandlerInterceptor[] result = new HandlerInterceptor[beanNames.length];
                ApplicationContext ctx  = getApplicationContext();
                int i = 0;
                for (String beanName : beanNames) {
//                	logger.info("beanname: {}, i: {}", beanName, i);
                    result[i++] = (HandlerInterceptor)ctx.getBean(beanName);
                }
                return result;
            }
        }
        return null;
    }
}
