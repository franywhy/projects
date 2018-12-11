package com.hq.common.util;

/**
 * Created by Glenn on 2017/4/20 0020.
 */


import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于获取schoolId的工具类。
 *
 * @author XingNing OU
 */
public class SchoolIdUtil {

    /**
     * 包含在HTTP头中的网校ID参数。
     */
    private static final String HTTP_HEADER_SCHOOL_ID = "X-Forward-School";

    /**
     * 获取schoolId参数的值，如果无法获取则返回<code>null</code>.
     *
     * @param request
     *            当前HttpServletRequest对象
     * @return schoolId参数的值，如果无法获取则返回<code>null</code>
     */

    public static String getSchoolId(HttpServletRequest request) {
        String schoolId = request.getHeader(HTTP_HEADER_SCHOOL_ID);
        if (StringUtils.isBlank(schoolId)) {
            schoolId = request.getParameter("schoolId");
        }
        return (null == schoolId) ? "test" : schoolId;
    }
}
