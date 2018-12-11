package io.renren.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 用于获取schoolId的工具类。
 *
 * @author XingNing OU
 */
@Component
public class SchoolIdUtils {
	
    /**
     * 包含在HTTP头中的网校ID参数。
     */
    private static final String HTTP_HEADER_SCHOOL_ID = "X-Forward-School";

//	private static final String DEF_SCHOOL_ID = "test";
//	private static final String DEF_SCHOOL_ID = "zikao";
	private static String SCHOOL_ID = "test";
	@Value("#{application['schoolId']}")
	public void initSchoolId(String url) {
		SCHOOL_ID = url;  
	}  
	
    /**
     * 获取schoolId参数的值，如果无法获取则返回<code>null</code>.
     * 
     * @param request
     *            当前HttpServletRequest对象
     * @return schoolId参数的值，如果无法获取则返回<code>null</code>
     */
   public static String getSchoolId(HttpServletRequest request) {
//        String schoolId = request.getHeader(HTTP_HEADER_SCHOOL_ID);
//        if (StringUtils.isBlank(schoolId)) {
//            schoolId = request.getParameter("schoolId");
//        }
//        return (null == schoolId) ? SCHOOL_ID : schoolId;
	   return SCHOOL_ID;
    }

}
