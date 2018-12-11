package com.bluewhale.common.interceptor;

import com.bluewhale.common.entity.IpListEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Glenn on 2017/5/18 0018.
 */

public class IPHandlerInterceptor implements HandlerInterceptor {

    private static final Logger TRACER = LoggerFactory.getLogger(IPHandlerInterceptor.class);

    private IpListEntity entity;

    public IPHandlerInterceptor(IpListEntity entity){
        this.entity=entity;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if(entity.getFilterSwitch()){
            String ip = httpServletRequest.getRemoteAddr();
            if(entity.getList().contains(ip)){

                return true;
            }else{
                httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                TRACER.info(String.format("Host %s try to access service",ip));
                return false;
            }
        }else{
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
