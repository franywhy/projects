package com.elise.userinfocenter.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.http.HttpConnManager;
import com.hq.http.HttpPlainResult;
import com.hq.http.HttpResultDetail;
import com.hq.http.HttpResultHandler;
import com.elise.userinfocenter.entity.LocalConfigProperties;
import com.elise.userinfocenter.pojo.SchoolId;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Created by Glenn on 2017/4/22 0022.
 */


@Controller
@RequestMapping("/api")
public class PassWordController extends AbstractRestController {

    @Autowired
    private HttpConnManager httpConnManager;

    @Autowired
    private LocalConfigProperties config;

    @ApiOperation(value = "密码修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "activePassWord", value = "用户新输入密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "inactivePassWord", value = "用户原本密码", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/passWord", method = RequestMethod.PUT)
    public ResponseEntity<WrappedResponse<String>> modifyPassword(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", null);
            String activePassWord = ServletRequestUtils.getStringParameter(request, "activePassWord", null);
            String inactivePassWord = ServletRequestUtils.getStringParameter(request, "inactivePassWord", null);
            String schoolId = this.getSchoolId(request);
            if (TRACER.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("\nToken:");
                sb.append(token);
                sb.append("\nActive PassWord:");
                sb.append(activePassWord);
                sb.append("\nInactive PassWord:");
                sb.append(inactivePassWord);
                TRACER.info(sb.toString());
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("token", token);
            map.put("activePassWord", activePassWord);
            map.put("inactivePassWord", inactivePassWord);
            HttpPlainResult result = httpConnManager.invoke(HttpMethod.PUT, config.getSsoHost() + "/inner/passWord", map, schoolId);
            TRACER.info(result.getResult());
            HttpResultDetail<String> entry = HttpResultHandler.handle(result, String.class);
            if (entry.isOK()) {
                return this.success(entry.getResult(), entry.getResponseStatus());
            } else if (entry.isClientError()) {
                return this.error(entry.getResponseMessage(), entry.getResponseStatus());
            } else if (entry.isServerError()) {
                return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
            }
        } catch (Throwable t) {
            TRACER.error("", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "密码重置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobileNo", value = "用户手机号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "passWord", value = "新用户密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "otp", value = "短信验证码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "redirectUrl", value = "重定向资源(Web需要传)", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/passWord", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<String>> resetPassword(HttpServletRequest request, HttpServletResponse response) {
        try {
            String mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo", null);
            String passWord = ServletRequestUtils.getStringParameter(request, "passWord", null);
            String otp = ServletRequestUtils.getStringParameter(request, "otp", null);
            String schoolId = this.getSchoolId(request);
            String redirectUrl = null;
            if (TRACER.isInfoEnabled()) {
                StringBuilder sbs = new StringBuilder();
                sbs.append("\nmobileNo:");
                sbs.append(mobileNo);
                sbs.append("\nPassWord:");
                sbs.append(passWord);
                sbs.append("\nOTP SMS:");
                sbs.append(otp);
                TRACER.info(sbs.toString());
            }
            HttpPlainResult schoolIdResult = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost() + "/inner/checkSchoolId", new HashMap<>(), schoolId);
            TRACER.info(schoolIdResult.getResult());
            HttpResultDetail<SchoolId> schoolIdEntry = HttpResultHandler.handle(schoolIdResult, SchoolId.class);
            if (schoolIdEntry.isOK()) {
                Boolean isMobile = schoolIdEntry.getResult().getMobile();
                if (!isMobile) {
                    redirectUrl = ServletRequestUtils.getStringParameter(request, "redirectUrl", null);
                    if (StringUtils.isBlank(redirectUrl)) {
                        return this.error("重定向地址不能为空");
                    }
                }
                HashMap<String, Object> map = new HashMap<String, Object>();
                if (mobileNo != null) {
                    map.put("mobileNo", mobileNo);
                }
                if (passWord != null) {
                    map.put("passWord", passWord);
                }
                if (otp != null) {
                    map.put("otp", otp);
                }
                HttpPlainResult result = httpConnManager.invoke(HttpMethod.POST, config.getSsoHost() + "/inner/passWord", map, schoolId);
                TRACER.info(result.getResult());
                HttpResultDetail<String> entry = HttpResultHandler.handle(result, String.class);
                if (entry.isOK()) {
                    if (isMobile) {
                        return this.success(entry.getResult(), entry.getResponseStatus());
                    } else {
                        return this.success(redirectUrl);
                    }
                } else if (entry.isClientError()) {
                    return this.error(entry.getResponseMessage(), entry.getResponseStatus());
                } else {
                    return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
                }
            } else if (schoolIdEntry.isClientError()) {
                return this.error(schoolIdEntry.getResponseMessage(), schoolIdEntry.getResponseStatus());
            } else {
                return this.fail(schoolIdEntry.getResponseMessage(), schoolIdEntry.getResponseStatus());
            }
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiOperation(value = "app4.0密码重置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobileNo", value = "用户手机号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "passWord", value = "新用户密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "otp", value = "短信验证码", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/passWord_v410", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<String>> resetPassword_v410(HttpServletRequest request, HttpServletResponse response) {
        try {
            String mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo", null);
            String passWord = ServletRequestUtils.getStringParameter(request, "passWord", null);
            String otp = ServletRequestUtils.getStringParameter(request, "otp", null);
            String schoolId = this.getSchoolId(request);
            if (TRACER.isInfoEnabled()) {
                StringBuilder sbs = new StringBuilder();
                sbs.append("\nmobileNo:");
                sbs.append(mobileNo);
                sbs.append("\nPassWord:");
                sbs.append(passWord);
                sbs.append("\nOTP SMS:");
                sbs.append(otp);
                TRACER.info(sbs.toString());
            }

                HashMap<String, Object> map = new HashMap<String, Object>();
                if (mobileNo != null) {
                    map.put("mobileNo", mobileNo);
                }
                if (passWord != null) {
                    map.put("passWord", passWord);
                }
                if (otp != null) {
                    map.put("otp", otp);
                }
                HttpPlainResult result = httpConnManager.invoke(HttpMethod.POST, config.getSsoHost() + "/inner/passWord", map, schoolId);
                TRACER.info(result.getResult());
                HttpResultDetail<String> entry = HttpResultHandler.handle(result, String.class);
                if (entry.isOK()) {
                        return this.success(entry.getResult(), entry.getResponseStatus());
                } else if (entry.isClientError()) {
                    return this.error(entry.getResponseMessage(), entry.getResponseStatus());
                } else {
                    return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
                }
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}