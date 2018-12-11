package com.elise.singlesignoncenter.util;


import tim.SmsSingleSender;
import tim.SmsSingleSenderResult;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Glenn on 2017/5/11 0011.
 */
public class SmsUtil {

    /** 腾讯云相关配置，请先确保模板审核通过*/
    private static final int APP_ID = 1400025077;
    private static final String APP_KEY = "fe515ffd807c365c32bb81da48f841e3";
    /** 注意这个模板id，我们的模板为: 您的短信验证码为{1},本验证{2}分钟内有效。*/
    private static final int OTP_TEMPLATE_ID = 11549;
    private static final String MESSAGE_KEY = "message:";
    private static final String SECURITY_KEY = "security:";

    private static final String TIMES_KEY = "times:";
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

    public static String getOtpRedisKey(String mobileNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append(MESSAGE_KEY);
        sb.append(SECURITY_KEY);
        sb.append(mobileNumber);
        return sb.toString();
    }

    public static String getOtpTimeOutRedisKey(String mobileNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append(MESSAGE_KEY);
        sb.append(SECURITY_KEY);
        sb.append(mobileNumber);
        sb.append(":time");
        return sb.toString();
    }

    public static String getOtpMaxCountRedisKey(String mobileNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append(MESSAGE_KEY);
        sb.append(TIMES_KEY);
        sb.append(SDF.format(new Date()));
        sb.append(":");
        sb.append(mobileNumber);
        return sb.toString();
    }

    public static String generateOtp() {
        return (new DecimalFormat("0")).format(Math.random() * 9000.0D + 1000.0D);
    }

    public static SmsSingleSenderResult sendOtp(String otp, String mobileNo, Integer expireTime,String sign) throws Exception {
        SmsSingleSender singleSender = new SmsSingleSender(APP_ID, APP_KEY);
        SmsSingleSenderResult singleSenderResult;
        ArrayList<String> params = new ArrayList<>();
        params.add(otp);
        params.add(String.valueOf(expireTime));
        try {
            singleSenderResult = singleSender.sendWithParam("86", mobileNo, OTP_TEMPLATE_ID, params, sign,"","");
            return singleSenderResult;
        } catch (Exception e) {
            throw e;
        }
    }
}
