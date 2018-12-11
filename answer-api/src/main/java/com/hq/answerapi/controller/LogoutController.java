package com.hq.answerapi.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.http.HttpConnManager;
import com.hq.http.HttpPlainResult;
import com.hq.http.HttpResultDetail;
import com.hq.http.HttpResultHandler;
import com.hq.answerapi.config.props.LocalConfigProperties;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Created by Glenn on 2017/4/21 0021.
 * @author hq
 */
@Controller
@RequestMapping("/api")
public class LogoutController extends AbstractRestController {
    private static final Logger TRACER = LoggerFactory.getLogger(LogoutController.class);

    @Autowired
    private HttpConnManager httpConnManager;

    @Autowired
    private LocalConfigProperties config;

    @ApiOperation(value = "用户登出")
    @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<String>> logout(HttpServletRequest request,HttpServletResponse response) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            if (StringUtils.isBlank(token)) {
                return this.error("token 不可以为空");
            }
            String schoolId = this.getSchoolId(request);
            TRACER.info(String.format("Incoming request,token is %s", token));
            HashMap<String, Object> map = new HashMap<String, Object>();
            if( token != null) {
                map.put("token", token);
            }
            HttpPlainResult result = httpConnManager.invoke(HttpMethod.POST, config.getSsoHost()+"/inner/logout", map,schoolId);
            TRACER.info(result.getResult());
            HttpResultDetail<String> entry = HttpResultHandler.handle(result,String.class);
            if(entry.isOK()){
                Cookie token_cookie = new Cookie(config.getCookieTokenName(),null);
                token_cookie.setMaxAge(0);
                token_cookie.setDomain(request.getHeader("Host").split(":")[0]);
                response.addCookie(token_cookie);

                return this.success(entry.getResult(), entry.getResponseStatus());
            }else if(entry.isClientError()){
                return this.error(entry.getResponseMessage(), entry.getResponseStatus());
            }else if(entry.isServerError()){
                return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
            }
        } catch (Throwable t) {
            TRACER.error("", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }
}
