package com.school.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 用于获取BusinessId的工具类。
 *
 * @author SHIHONGJIE
 */
@Component
public class BusinessIdUtils {
	
    /**
     * 包含在HTTP头中的网校ID参数。
     */
    private static final String HTTP_HEADER_BUSINESS_ID = "X-Forward-School";

	private static String BUSINESS_ID = "kuaiji";
	
	/**
	 * 获取schoolId参数的值，如果无法获取则返回<code>null</code>.
	 * 
	 * @param request
	 *            当前HttpServletRequest对象
	 * @return schoolId参数的值，如果无法获取则返回<code>null</code>
	 */
	public static String getBusinessId(HttpServletRequest request) {
		String businessId = request.getHeader(HTTP_HEADER_BUSINESS_ID);
		if (StringUtils.isBlank(businessId)) {
			businessId = request.getParameter("businessId");
		}
		return (null == businessId) ? BUSINESS_ID : businessId;
	}
	
	public static String HTTP_HEADER_BUSINESS_ID(){
		return HTTP_HEADER_BUSINESS_ID;
	}
	
}
