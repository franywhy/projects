package com.elise.userinfocenter.controller;

import com.elise.userinfocenter.entity.LocalConfigProperties;
import com.elise.userinfocenter.pojo.TokenStatus;
import com.elise.userinfocenter.pojo.UserInfoPOJO;
import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.http.HttpConnManager;
import com.hq.http.HttpPlainResult;
import com.hq.http.HttpResultDetail;
import com.hq.http.HttpResultHandler;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Created by Glenn on 2017/4/21 0021.
 */

@Controller
@RequestMapping("/api")
public class UserInfoController extends AbstractRestController {

    @Autowired
    private HttpConnManager httpConnManager;

    @Autowired
    private LocalConfigProperties config;

    @ApiOperation(value = "获取用户信息")
    @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<WrappedResponse<UserInfoPOJO>> getUserInfo(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", null);
            TRACER.info("\nUser Token:" + token);
            HashMap<String, Object> map = new HashMap<String, Object>();
            if(token != null) {
                map.put("token", token);
            }
            String schoolId = this.getSchoolId(request);
            HttpPlainResult result = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost()+"/inner/userInfo", map,schoolId);
            TRACER.info(result.getResult());
            HttpPlainResult result2 = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost()+"/inner/userMobileNo", map,schoolId);
            TRACER.info(result2.getResult());
            HttpResultDetail<UserInfoPOJO> entry = HttpResultHandler.handle(result,UserInfoPOJO.class);
            HttpResultDetail<UserInfoPOJO> entry2 = HttpResultHandler.handle(result2,UserInfoPOJO.class);
            if(entry.isOK() && entry2.isOK()){
                entry.getResult().setMobileNo(entry2.getResult().getMobileNo());
                return this.success(entry.getResult(), entry.getResponseStatus());
            }else if(entry.isClientError()){
                return this.error(entry.getResponseMessage(), entry.getResponseStatus());
            }else if(entry.isServerError()){
                return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
            }
            return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiOperation(value = "更改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "nickName", value = "用户昵称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "邮箱账号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "gender", value = "用户性别(0女，1男，2保密)", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "avatar", value = "用户头像地址", required = false, dataType = "String", paramType = "query")
    })

    @RequestMapping(value = "/userInfo", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<WrappedResponse<String>> modifyUserInfo(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", null);
            String nickName = ServletRequestUtils.getStringParameter(request, "nickName", null);
            String email = ServletRequestUtils.getStringParameter(request, "email", null);
            Integer gender = ServletRequestUtils.getIntParameter(request, "gender", -1);
            String avatar = ServletRequestUtils.getStringParameter(request, "avatar", null);
            String businessId = this.getSchoolId(request);
            if(TRACER.isInfoEnabled()){
                StringBuilder sb = new StringBuilder();
                sb.append("\n[User Info]");
                sb.append("\nToken:").append(token);
                sb.append("\nNike Name:").append(nickName);
                sb.append("\nEmail:").append(email);
                sb.append("\nGender:").append(gender);
                TRACER.info(sb.toString());
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            if(token != null) {
                map.put("token", token);
            }
            if(nickName != null) {
                map.put("nickName", nickName);
            }
            if(email != null) {
                map.put("email", email);
            }
            if(avatar != null) {
                map.put("avatar", avatar);
            }
            if(gender != -1) {
                map.put("gender", gender);
            }
            HttpPlainResult result = httpConnManager.invoke(HttpMethod.PUT, config.getSsoHost()+"/inner/userInfo", map,businessId);
            TRACER.info(result.getResult());
            HttpResultDetail<String> entry = HttpResultHandler.handle(result,String.class);
            if(entry.isOK()){
                return this.success(entry.getResult(), entry.getResponseStatus());
            }else if(entry.isClientError()){
                return this.error(entry.getResponseMessage(), entry.getResponseStatus());
            }else if(entry.isServerError()){
                return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
            }
        } catch (Throwable t) {
            TRACER.error("", t);
        }
        return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "重定向获取用户中心Token")
    @ApiImplicitParam(name = "redirectUrl", value = "回调地址", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/userCenterToken", method = RequestMethod.GET)
    public void userCenterToken(HttpServletRequest request, HttpServletResponse response) {
        String redirectUrl = ServletRequestUtils.getStringParameter(request, "redirectUrl", null);
        String schoolId = this.getSchoolId(request);
        String token = null;
        Cookie[] cookies = request.getCookies();
        if(null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie != null && config.getCookieTokenName().equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        try {
            String effectiveToken = getEffectiveToken(token,schoolId);
            response.sendRedirect(redirectUrl+"?token="+effectiveToken+"&action=callback");
        } catch (Exception e) {
            TRACER.error("", e);
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "校验Token是否失效")
    @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/tokenExpired", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<WrappedResponse<String>> tokenExpired(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", null);
            String schoolId = this.getSchoolId(request);
            String effectiveToken = getEffectiveToken(token,schoolId);
            return this.success(effectiveToken);
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getEffectiveToken(String token, String schoolId) throws Exception {
        String effectiveToken = "";
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("token", token);
        HttpPlainResult result = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost()+"/inner/tokenExpired", map,schoolId);
        TRACER.info(result.getResult());
        HttpResultDetail<TokenStatus> entry = HttpResultHandler.handle(result, TokenStatus.class);
        if (entry.isOK()) {
            TokenStatus tokenStatus = entry.getResult();
            if (!tokenStatus.getExpired()) {
                effectiveToken = token;
            }
        }
        return effectiveToken;
    }

}