package com.school.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.school.exception.SchoolNotFoundException;
import com.school.service.BusinessDiscoverer;

/**
 * {@link SchoolDiscoverer}接口的实现。
 * @author XingNing OU
 */
@Service
public class BusinessDiscovererImpl implements BusinessDiscoverer {

    /**
     * 包含在HTTP头中的网校ID参数。
     */
    private static final String HTTP_HEADER_SCHOOL_ID = "X-Forward-School";

    /**
     * 包含在HTTP头中的REQUEST_HOST参数。
     */
    private static final String HTTP_HREADER_REQUEST_HOST = "X-Domain";

    private static String SCHOOL_ID = "zikao";
   	/*@Value("#{application['schoolId']}")
   	public void initSchoolId(String url) {
   		SCHOOL_ID = url;
   	} */ 
    
    /*@Autowired
    private SchoolManager schoolManager;*/

    @Override
    public String getBusinessId(HttpServletRequest request) throws SchoolNotFoundException {

        // 先检查HTTP头中是否带有SCHOOL_ID参数
        String businessId = request.getHeader(HTTP_HEADER_SCHOOL_ID);

        /*if (StringUtils.isBlank(schoolId)) {
            String domain = request.getHeader(HTTP_HREADER_REQUEST_HOST);
            if (StringUtils.isNotBlank(domain)) {
                School school = schoolManager.getSchoolByDomain(domain);
                if (null != school) {
                    schoolId = school.getSchoolId();
                }
            }
        }*/

        if (null != businessId && !"zikao".equals(businessId)) {
            return businessId;
        }

        // throw new SchoolNotFoundException("找不到网校信息，无法定位您要访问的资源。");
        return SCHOOL_ID; // 用于在localhost中测试
    }
}
