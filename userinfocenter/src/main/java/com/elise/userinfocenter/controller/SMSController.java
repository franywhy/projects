package com.elise.userinfocenter.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.http.HttpConnManager;
import com.hq.http.HttpPlainResult;
import com.hq.http.HttpResultDetail;
import com.hq.http.HttpResultHandler;
import com.elise.userinfocenter.entity.CaptchaProperties;
import com.elise.userinfocenter.entity.LocalConfigProperties;
import com.elise.userinfocenter.pojo.MobileNo;
import com.elise.userinfocenter.service.impl.CaptchaServiceImpl;
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
import java.util.HashMap;

/**
 * Created by Glenn on 2017/5/11 0011.
 */

@Controller
@RequestMapping("/api")
public class SMSController extends AbstractRestController {

    @Autowired
    private HttpConnManager httpConnManager;

    @Autowired
    private LocalConfigProperties config;

    @Autowired
    private CaptchaServiceImpl captchaService;

    @Autowired
    private CaptchaProperties prop;

    @ApiOperation(value = "根据手机号下发一次性口令")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobileNo", value = "用户手机号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "captchaCode", value = "验证码(Web需要传)", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "验证码类型(1.注册 2.找回密码)", required = false, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value = "/otpSMS", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<String>> getOtpSMS(HttpServletRequest request) {
        try {
            String mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo", null);
            String captchaCode = ServletRequestUtils.getStringParameter(request, "captchaCode", null);
            Integer type = ServletRequestUtils.getIntParameter(request, "type", 2);
            String schoolId = this.getSchoolId(request);
            //测试配置为false不对图形验证码进行验证
            if (prop.isValidate()) {
                if (StringUtils.isBlank(captchaCode)) {
                    return this.error("web端验证码不能为空");
                }
                String codeInSession = captchaService.loadCaptchaCode(schoolId, request.getSession().getId());
                TRACER.info(String.format("\r\nJSESSIONID is %s \r\nMobile Number is %s\r\n captcha code is %s\r\n Sms type is %s", request.getSession().getId(), mobileNo, codeInSession, type));
                if (codeInSession == null || !codeInSession.equalsIgnoreCase(captchaCode)) {
                    return this.error("验证码错误");
                } else {
                    captchaService.clear(schoolId, request.getSession().getId());
                }
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            if (mobileNo != null) {
                map.put("mobileNo", mobileNo);
            }
            HttpPlainResult checkMobileResult = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost() + "/inner/checkMobileNo", map, schoolId);
            TRACER.info(checkMobileResult.getResult());
            HttpResultDetail<MobileNo> checkMobileEntry = HttpResultHandler.handle(checkMobileResult, MobileNo.class);
            if (checkMobileEntry.isOK()) {
                switch (type) {
                    case 1:
                        if (checkMobileEntry.getResult().getIsMobileNumber()) {
                            return this.error(TransactionStatus.DUPLICATE_MOBILE_NUMBER);
                        }
                        break;
                    case 2:
                        if (!checkMobileEntry.getResult().getIsMobileNumber()) {
                            return this.error("此手机号未注册过，请返回注册");
                        }
                        break;
                    default:
                        return this.error("不支持的类型");
                }
                HttpPlainResult optSmsResult = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost() + "/inner/otpSMS", map, schoolId);
                TRACER.info(optSmsResult.getResult());
                HttpResultDetail<String> otpSmsEntry = HttpResultHandler.handle(optSmsResult, String.class);
                if (otpSmsEntry.isOK()) {
                    return this.success(otpSmsEntry.getResult(), otpSmsEntry.getResponseStatus());
                } else if (otpSmsEntry.isClientError()) {
                    return this.error(otpSmsEntry.getResponseMessage(), otpSmsEntry.getResponseStatus());
                } else {
                    return this.fail(otpSmsEntry.getResponseMessage(), otpSmsEntry.getResponseStatus());
                }
            } else if (checkMobileEntry.isClientError()) {
                return this.error(checkMobileEntry.getResponseMessage(), checkMobileEntry.getResponseStatus());
            } else {
                return this.fail(checkMobileEntry.getResponseMessage(), checkMobileEntry.getResponseStatus());
            }
        } catch (Throwable t) {
            TRACER.error("\nUnknown Error", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "根据手机号验证一次性口令")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobileNo", value = "用户手机号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "otp", value = "手机验证码", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/otpSMS", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<String>> checkOtpSMS(HttpServletRequest request) {
        try {
            String mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo", null);
            String otp = ServletRequestUtils.getStringParameter(request, "otp", null);
            if (TRACER.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("\nmobileNo:");
                sb.append(mobileNo);
                sb.append("\nOtp Sms:");
                sb.append(otp);
                TRACER.info(sb.toString());
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            if (mobileNo != null) {
                map.put("mobileNo", mobileNo);
            }
            if (otp != null) {
                map.put("otp", otp);
            }
            String schoolId = this.getSchoolId(request);
            HttpPlainResult result = httpConnManager.invoke(HttpMethod.POST, config.getSsoHost() + "/inner/otpSMS", map, schoolId);
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
            TRACER.error("\nUnknown Error", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

