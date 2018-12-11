package com.hq.learningcenter.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hq.learningcenter.config.props.LocalConfigProperties;
import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.prototype.WrappedResponse;
import com.hq.common.util.SchoolIdUtil;
import com.hq.http.HttpConnManager;
import com.hq.http.HttpPlainResult;
import com.hq.http.HttpResultDetail;
import com.hq.http.HttpResultHandler;
import com.hq.learningcenter.pojo.TokenStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by Glenn on 2017/5/18 0018.
 * @author hq
 */
@Component
public class TokenHandlerInterceptor implements HandlerInterceptor {

    private static final Logger TRACER = LoggerFactory.getLogger(TokenHandlerInterceptor.class);

    @Autowired
    private LocalConfigProperties config;

    @Autowired
    private HttpConnManager httpConnManager;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String token = ServletRequestUtils.getStringParameter(httpServletRequest, "token", "");
        Boolean result = null;
        WrappedResponse response = null;
        String schoolId= SchoolIdUtil.getSchoolId(httpServletRequest);
        if (StringUtils.isBlank(token)) {
            response = WrappedResponse.generate(TransactionStatus.BAD_REQUEST.value(), "Token不能为空", null);
            result = false;
        } else {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("token", token);
            HttpPlainResult httpResult = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost() + "/inner/tokenExpired", map, schoolId);
            HttpResultDetail<TokenStatus> entry = HttpResultHandler.handle(httpResult, TokenStatus.class);
            if (entry.isOK()) {
                TRACER.info(httpResult.getResult());
                TokenStatus tokenStatus = entry.getResult();
                if (tokenStatus.getExpired()) {
                    response = WrappedResponse.generate(TransactionStatus.USER_TOKEN_NOT_FOUND.value(),
                            TransactionStatus.USER_TOKEN_NOT_FOUND.getReasonPhrase(), null);
                    result = false;
                } else {
                    result = true;
                }
            } else if (entry.isClientError()) {
                TRACER.error(httpResult.getResult());
                response = WrappedResponse.generate(TransactionStatus.BAD_REQUEST.value(),
                        TransactionStatus.BAD_REQUEST.getReasonPhrase(), null);
                result = false;
            } else if (entry.isServerError()) {
                TRACER.error(httpResult.getResult());
                response = WrappedResponse.generate(TransactionStatus.INTERNAL_SERVER_ERROR.value(),
                        TransactionStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
                result = false;
            }
        }

        if (!result) {
            PrintWriter out = null;
            try {
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.setContentType("application/json; charset=utf-8");
                out = httpServletResponse.getWriter();
                out.append(objectMapper.writeValueAsString(response));
            } catch (IOException e) {
                TRACER.error("写入流失败", e);
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
        return result;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
