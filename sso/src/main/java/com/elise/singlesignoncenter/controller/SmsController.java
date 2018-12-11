package com.elise.singlesignoncenter.controller;


import com.elise.singlesignoncenter.annotation.BusinessId;
import com.elise.singlesignoncenter.config.LocalConfigEntity;
import com.elise.singlesignoncenter.dao.SysBusinessDao;
import com.elise.singlesignoncenter.service.UserInfoService;
import com.elise.singlesignoncenter.service.impl.SmsRedisServiceImpl;
import com.elise.singlesignoncenter.util.SmsUtil;
import com.hq.common.enumeration.SmsResultEnum;
import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.common.util.MobileNoRegular;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tim.SmsSingleSenderResult;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by Glenn on 2017/4/26 0026.
 */

@Controller
@RequestMapping(value = "/inner")
public class SmsController extends AbstractRestController {

    @Autowired
    private SmsRedisServiceImpl smsRedisService;

    @Autowired
    protected LocalConfigEntity config;

//    @Autowired
//    private UserInfoService userInfoService;

    @Autowired
    private SysBusinessDao sysBusinessDao;

    @ApiOperation(value = "根据手机号下发一次性口令")
    @ApiImplicitParam(name = "mobileNo", value = "用户手机号", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/otpSMS", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<String>> getOtpSMS(@BusinessId String businessId , HttpServletRequest request) {
        String mobileNo = null;
        try {
//            if (sysBusinessDao.checkValidationOfBusiness(businessId) != 1) {
//                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
//            }

            mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo", null);
            if (StringUtils.isBlank(mobileNo)) {
                TRACER.error("Mobile number is null");
                return this.error("手机号码不可为空");
            }
            MobileNoRegular mobileNumber = new MobileNoRegular(mobileNo);
            if (mobileNumber.isValidMobileNo()) {
//                /*------临时逻辑--------*/
//                if(!userInfoService.checkMobileNo(mobileNo)){
//                    TRACER.info(String.format("Mobile number %s is not registered", mobileNo));
//                    return this.error("未注册的手机号");
//                }
//                /*------临时逻辑--------*/
                if (smsRedisService.checkFrequency(mobileNo)) {
                    TRACER.info(String.format("Mobile number %s get OTP too frequently", mobileNo));
                    StringBuilder sb = new StringBuilder();
                    sb.append("请求过于频繁,请");
                    sb.append(config.getOtpSmsMaxGapTime() / 1000);
                    sb.append("秒之后再试");
                    return this.fail(sb.toString());
                }
                if (smsRedisService.checkOutOfThreshold(mobileNo, config.getOptSmsMaxCount())) {
                    TRACER.info(String.format("Mobile number %s get OTP too frequently", mobileNo));
                    return this.fail("当日短信次数已超出，请勿频繁操作");
                }
                if (config.getOtpSmsSwitch()) {
                    String otp = SmsUtil.generateOtp();
                    smsRedisService.storeAndUpdateCount(mobileNo, config.getOtpSmsMaxGapTime(), config.getOtpSmsMaxExpireTime(), otp);
                    SmsSingleSenderResult singleSenderResult = SmsUtil.sendOtp(otp, mobileNo, (int) (config.getOtpSmsMaxExpireTime() / (60 * 1000)),sysBusinessDao.querySmsSign(businessId));
                    if (singleSenderResult.result == 0) {
                        TRACER.info(String.format("\nMobile Number is %s\n%s",mobileNumber.getValue(),singleSenderResult.toString()));
                        return this.success();
                    } else {
                        smsRedisService.clear(mobileNo);
                        TRACER.error(String.format("\nSend otp sms failed\nMobile Number is %s\n%s",mobileNumber.getValue(),singleSenderResult.toString()));
                        return this.fail("短信发送失败");
                    }
                } else {
                    return this.success();
                }
            } else {
                TRACER.error(String.format("\nMobile Number %s is invalid", mobileNo));
                smsRedisService.clear(mobileNo);
                return this.error("请输入正确的手机号格式", TransactionStatus.INVALID_MOBILE_NUMBER);
            }
        } catch (Throwable t) {
            TRACER.error("\nUnknown Error", t);
            smsRedisService.clear(mobileNo);
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
            if (StringUtils.isBlank(mobileNo) || StringUtils.isBlank(otp)) {
                TRACER.error("Mobile number or Otp SMS is null");
                return this.error("手机号码或短信验证码不可为空");
            } else {
                MobileNoRegular mobileNumber = new MobileNoRegular(mobileNo);
                if (mobileNumber.isValidMobileNo()) {
                    SmsResultEnum resultEnum = smsRedisService.checkSms(config.getOtpSmsSwitch(), config.getOtpPassKey(), mobileNo, otp);
                    if (resultEnum.isError()) {
                        TRACER.error(resultEnum.getLogContent());
                        return this.error(resultEnum.getResultContent());
                    } else {
                        // Validate Sms Successfully,clear all redis cache associated with mobile number
                        //单独验证手机验证码的时候不清除,注册完成的时候才清除
                        /*smsRedisService.clear(mobileNo);*/
                        return this.success();
                    }
                } else {
                    TRACER.error("Invalid mobile number");
                    return this.error("请输入正确的手机号格式", TransactionStatus.INVALID_MOBILE_NUMBER);
                }
            }
        } catch (Throwable t) {
            TRACER.error("\nUnknown Error", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
