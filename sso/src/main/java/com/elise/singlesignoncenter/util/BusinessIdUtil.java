package com.elise.singlesignoncenter.util;

import com.elise.singlesignoncenter.service.GetBusinessIdService;
import com.elise.singlesignoncenter.service.impl.GetBusinessIdServiceImpl;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取业务线ID
 * @author: shihongjie
 * @email: shihongjie@hengqijy.com
 * @date: 2018/1/31 16:51
 */
public class BusinessIdUtil {
    private static Logger TRACER = LoggerFactory.getLogger(BusinessIdUtil.class);
    private static final String TOKEN = "token";
    private static final String HTTP_HEADER_BUSINESSID = "X-Forward-School";
    private static final String HTTP_REQUEST_BUSINESSID = HTTP_HEADER_BUSINESSID;

    /**
     * 获取业务线ID
     * @param request
     * @param getBusinessIdService
     * @return
     */
    public static final String getBusinessId(final HttpServletRequest request ,final GetBusinessIdService getBusinessIdService) {
        //业务线
        String businessId = null;

        //request token
        if(StringUtils.isBlank(businessId)){

            String[] headerParameterValues = request.getParameterValues(TOKEN);
            if(ArrayUtils.isNotEmpty(headerParameterValues)){
                businessId = getBusinessIdService.getBusinessId(headerParameterValues[0]);
                TRACER.info("token businessId={}",businessId);
            }
        }

        //header  X-Forward-School
        if(StringUtils.isBlank(businessId)){
            businessId = request.getHeader(HTTP_HEADER_BUSINESSID);
            TRACER.info("header X-Forward-School businessId={}",businessId);
        }
        //request X-Forward-School
        if(StringUtils.isBlank(businessId)){
            String[] requestParameterValues = request.getParameterValues(HTTP_REQUEST_BUSINESSID);
            if(ArrayUtils.isNotEmpty(requestParameterValues)){
                businessId = requestParameterValues[0];
                TRACER.info("request X-Forward-School businessId={}",businessId);
            }
        }
        return businessId;
    }
}
