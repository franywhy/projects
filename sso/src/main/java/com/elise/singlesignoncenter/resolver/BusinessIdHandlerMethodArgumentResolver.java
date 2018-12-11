package com.elise.singlesignoncenter.resolver;

import com.elise.singlesignoncenter.annotation.BusinessId;
import com.elise.singlesignoncenter.service.impl.GetBusinessIdServiceImpl;
import com.elise.singlesignoncenter.util.BusinessIdUtil;
import com.elise.singlesignoncenter.util.ResponeUtil;
import com.hq.common.enumeration.TransactionStatus;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取业务线ID
 * 临时方法
 * @author shihongjie
 */
@Component
public class BusinessIdHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private Logger TRACER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private GetBusinessIdServiceImpl getBusinessIdServiceImpl;
    /**
     * 用于判定是否需要处理该参数分解，返回true为需要，并会去调用下面的方法resolveArgument
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(String.class) && parameter.hasParameterAnnotation(BusinessId.class);
    }

    /**
     * 真正用于处理参数分解的方法，返回的Object就是controller方法上的形参对象。
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        String businessId = BusinessIdUtil.getBusinessId(request.getNativeRequest(HttpServletRequest.class) , getBusinessIdServiceImpl);
        if(StringUtils.isBlank(businessId)){
            HttpServletResponse respone = request.getNativeResponse(HttpServletResponse.class);
            ResponeUtil.responseWriter(respone , TransactionStatus.UNKNOWN_BUSINESS_ID);
        }
        return businessId;
    }

}
