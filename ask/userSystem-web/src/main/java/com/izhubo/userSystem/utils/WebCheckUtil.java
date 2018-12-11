package com.izhubo.userSystem.utils;

import javax.servlet.http.HttpServletRequest;

public class WebCheckUtil {
	public static String getIpRemote(HttpServletRequest request){
		String requestIp = request.getHeader("x-forwarded-for");
		if(requestIp == null || requestIp.length() == 0 || "unknown".equalsIgnoreCase(requestIp)) {
			requestIp = request.getHeader("Proxy-Client-IP");
		}
		if(requestIp == null || requestIp.length() == 0 || "unknown".equalsIgnoreCase(requestIp)) {
			requestIp = request.getHeader("WL-Proxy-Client-IP");
		}
		if(requestIp == null || requestIp.length() == 0 || "unknown".equalsIgnoreCase(requestIp)) {
			requestIp = request.getRemoteAddr();
		}
		return requestIp;
	}
}
