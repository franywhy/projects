package com.hqjy.msg.interceptor;

import com.hqjy.msg.model.IpListEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by baobao on 2017/12/19 0019.
 */
public class IPHandlerInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(IPHandlerInterceptor.class);


    private IpListEntity entity;

    public IPHandlerInterceptor(IpListEntity entity){
        this.entity=entity;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (entity.getFilterSwitch()){

            //
            String ip = request.getRemoteAddr();
           /* logger.info("host:"+request.getRemoteHost());
            logger.info("headnames:"+request.getHeaderNames());
            logger.info("addr:"+request.getRemoteAddr());
            logger.info("url:"+request.getRequestURL());*/
            if(entity.getList().contains(ip)){
                return true;
            }else{
                response.setStatus(HttpStatus.FORBIDDEN.value());
                logger.warn(String.format("Host %s try to access service",ip));
            }


        }

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
