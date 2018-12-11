package com.school.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 获取当前请求的request对象工具类
 *
 */
public class HttpContextUtils {
	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	public static HttpServletResponse getHttpServletResponse() {
		
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}
}
