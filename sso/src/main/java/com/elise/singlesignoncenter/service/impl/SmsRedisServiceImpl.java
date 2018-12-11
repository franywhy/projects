package com.elise.singlesignoncenter.service.impl;


import com.elise.singlesignoncenter.util.SmsUtil;
import com.hq.common.enumeration.SmsResultEnum;
import com.hq.common.interfaze.AbstractRedisService;
import com.hq.common.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Created by Glenn on 2017/4/26 0026.
 */

@Service
public class SmsRedisServiceImpl extends AbstractRedisService {

    public void clear(String mobileNo) {
        this.removeCache(SmsUtil.getOtpRedisKey(mobileNo));
        this.removeCache(SmsUtil.getOtpTimeOutRedisKey(mobileNo));
        this.removeCache(SmsUtil.getOtpMaxCountRedisKey(mobileNo));
    }

    public boolean checkFrequency(String mobileNo) {
        String flag = this.getValueCache(SmsUtil.getOtpTimeOutRedisKey(mobileNo), String.class);
        return StringUtils.isBlank(flag) ? false : true;
    }

    public Boolean checkOutOfThreshold(String mobileNo, Integer OtpSmsMaxCount) {
        Integer maxCount = this.getValueCache(SmsUtil.getOtpMaxCountRedisKey(mobileNo), Integer.class);
        return maxCount != null && maxCount >= OtpSmsMaxCount ? true : false;
    }

    public void storeAndUpdateCount(String mobileNo, Long maxGapTime, Long maxExpireTime, String otp) {
        this.pushValueCache(SmsUtil.getOtpRedisKey(mobileNo), otp, maxExpireTime);
        this.pushValueCache(SmsUtil.getOtpTimeOutRedisKey(mobileNo), mobileNo, maxGapTime);
        int count;
        if (this.getValueCache(SmsUtil.getOtpMaxCountRedisKey(mobileNo), Integer.class) == null) {
            count = 0;
        } else {
            count = this.getValueCache(SmsUtil.getOtpMaxCountRedisKey(mobileNo), Integer.class);
        }
        this.pushValueCache(SmsUtil.getOtpMaxCountRedisKey(mobileNo), ++count, CommonUtil.oneDayTimeStamp);
    }

    public SmsResultEnum checkSms(Boolean smsSwitch, String otpPassKey, String mobileNo, String otp) {
        if (smsSwitch) {
            String otpInRedis = this.getValueCache(SmsUtil.getOtpRedisKey(mobileNo), String.class);
            if (otpInRedis == null) {
                return SmsResultEnum.SMS_TIME_OUT;
            } else if (!otpInRedis.equals(otp)) {
                return SmsResultEnum.SMS_ERROR;
            }
        } else {
            if (!otp.equals(otpPassKey)) {
                return SmsResultEnum.SMS_ERROR;
            }
        }
        return SmsResultEnum.OK;
    }

}
