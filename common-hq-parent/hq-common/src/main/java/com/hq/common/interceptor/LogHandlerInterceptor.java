package com.hq.common.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Enumeration;


/**
 * Created by Glenn on 2017/7/21 0011.
 */
public class LogHandlerInterceptor implements HandlerInterceptor {

    private static final Logger TRACER = LoggerFactory.getLogger(LogHandlerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        StringBuilder writer = new StringBuilder();
        writer.append("\r\n");
        writer.append("[[Request Received at ").append(new Timestamp(System.currentTimeMillis())).append("]]");
        writer.append("\r\n");
        writer.append("[----request meta info begin----]");
        writer.append("\r\n");
        writer.append("   remoteUser=").append(httpServletRequest.getRemoteUser());
        writer.append("\r\n");
        writer.append("   requestedSessionId=").append(httpServletRequest.getRequestedSessionId());
        writer.append("\r\n");
        writer.append("   characterEncoding=").append(httpServletRequest.getCharacterEncoding());
        writer.append("\r\n");
        writer.append("   remoteHost=").append(httpServletRequest.getRemoteHost());
        writer.append("\r\n");
        writer.append("   isSecure=").append(httpServletRequest.isSecure());
        writer.append("\r\n");
        writer.append("[----request meta info end----]");
        writer.append("\r\n");
        writer.append("\r\n");

        writer.append(httpServletRequest.getMethod());
        writer.append(" ");
        writer.append(httpServletRequest.getRequestURI());
        if(httpServletRequest.getQueryString()!= null
                && (httpServletRequest.getMethod().equals("GET")
                || httpServletRequest.getMethod().equals("DELETE"))){
            writer.append("?");
            writer.append(httpServletRequest.getQueryString());
        }
        writer.append(" ");
        writer.append(httpServletRequest.getProtocol());
        writer.append("\r\n\r\n");

        Enumeration headers = httpServletRequest.getHeaderNames();
        while (headers.hasMoreElements()) {
            String name = (String) headers.nextElement();
            String value = httpServletRequest.getHeader(name);
            writer.append(name);
            writer.append("=");
            writer.append(value);
            writer.append("\r\n");
        }
        writer.append("\r\n");
        if (httpServletRequest.getMethod().equals("POST")
                || httpServletRequest.getMethod().equals("PUT")) {
            writer.append(httpServletRequest.getQueryString());
            writer.append("\r\n");
            if (httpServletResponse.getHeader("Content-Type") != null &&
                    httpServletRequest.getHeader("Content-Type").indexOf("multipart/form-data") != -1) {
                writer.append("REQUEST BOOOOOOODY　TOOOOOO LARGE");
                writer.append("\r\n");
            }
        }

        TRACER.info(writer.toString());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        StringBuilder writer = new StringBuilder();
        writer.append("\r\n");
        writer.append("[[Response Returned at ").append(new Timestamp(System.currentTimeMillis())).append("]]");
        writer.append("\r\n");
        writer.append("[----Response meta info begin----]");
        writer.append("\r\n");
        writer.append("   characterEncoding=").append(httpServletResponse.getCharacterEncoding());
        writer.append("\r\n");
        writer.append("   buffer size=").append(httpServletResponse.getBufferSize());
        writer.append("\r\n");
        writer.append("[----Response meta info end----]");
        writer.append("\r\n");
        writer.append("\r\n");

        writer.append(httpServletRequest.getProtocol());
        writer.append(" ");
        writer.append(httpServletResponse.getStatus());
        writer.append("\r\n");
        Collection<String> headers =  httpServletResponse.getHeaderNames();
        for(String header :headers){
            writer.append(header);
            writer.append(":");
            writer.append(httpServletResponse.getHeader(header));
            writer.append("\r\n");
        }
        if (httpServletResponse.getHeader("Content-Type") != null &&
                httpServletResponse.getHeader("Content-Type").indexOf("multipart/form-data") != -1) {
            writer.append("REQUEST BOOOOOOODY　TOOOOOO LARGE");
        } else {
            // 截取响应流
            // 狗逼框架，连响应流都不暴露
        }
        TRACER.info(writer.toString());
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
