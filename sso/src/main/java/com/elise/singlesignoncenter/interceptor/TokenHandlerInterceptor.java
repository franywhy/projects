package com.elise.singlesignoncenter.interceptor;

import com.elise.singlesignoncenter.service.GetBusinessIdService;
import com.elise.singlesignoncenter.service.impl.GetBusinessIdServiceImpl;
import com.elise.singlesignoncenter.service.impl.UserInfoRedisServiceImpl;
import com.elise.singlesignoncenter.token.UserToken;
import com.elise.singlesignoncenter.token.UserTokenGenerator;
import com.elise.singlesignoncenter.util.BusinessIdUtil;
import com.elise.singlesignoncenter.util.ResponeUtil;
import com.elise.singlesignoncenter.util.SpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hq.common.enumeration.TransactionStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * token拦截器
 * 非白名单的:校验token合法性
 * @author shihongjie
 */
public class TokenHandlerInterceptor implements HandlerInterceptor {

    private static final Logger TRACER = LoggerFactory.getLogger(TokenHandlerInterceptor.class);
    @Autowired
    private UserInfoRedisServiceImpl userInfoRedisService;
    @Autowired
    private GetBusinessIdService getBusinessIdService;

    public TokenHandlerInterceptor() {
        super();
    }

    private void initBean() {
        if(this.userInfoRedisService == null){
            this.userInfoRedisService = SpringUtil.getBean(UserInfoRedisServiceImpl.class);
        }
        if(this.getBusinessIdService == null){
            this.getBusinessIdService = SpringUtil.getBean(GetBusinessIdServiceImpl.class);
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        initBean();
        String path = httpServletRequest.getServletPath();
        String method = httpServletRequest.getMethod();
        if (path.equals("/api/passWord") && method.equals("POST")) {
            return true;
        }
        String token = ServletRequestUtils.getStringParameter(httpServletRequest, "token", "");
        if (StringUtils.isBlank(token)) {
            ResponeUtil.responseWriter(httpServletResponse , TransactionStatus.BAD_REQUEST.value(), "Token不能为空");
            return false;
        } else {
            //add by shihongjie
            UserToken userToken = UserTokenGenerator.parse(token);
            if(null != userToken){
                String businessId = BusinessIdUtil.getBusinessId(httpServletRequest , getBusinessIdService);
                if(StringUtils.isNotBlank(businessId)){
                    String tokenInRedis = userInfoRedisService.getTokenByUserId(businessId,userToken.getClientType(),userToken.getUserId());
                    if(StringUtils.isNotBlank(tokenInRedis) && tokenInRedis.equals(token)){
                        return true;
                    }
                }
            }
            ResponeUtil.responseWriter(httpServletResponse ,TransactionStatus.USER_TOKEN_NOT_FOUND);
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
