package com.hq.common.enumeration;

/**
 * Created by Glenn on 2017/7/26 1111.
 */

public enum SmsResultEnum {

    OK("Validate Sms Successfully","短信验证通过",1),
    SMS_TIME_OUT("Validate otp key error time out","短信验证码超时",2),
    SMS_ERROR("Validate otp key error","短信验证码错误",3);

    private String logContent;
    private String resultContent;
    private Integer flag;

    SmsResultEnum(String logContent,String resultContent,Integer flag){
          this.logContent = logContent;
          this.resultContent = resultContent;
          this.flag = flag;
    }

    public  boolean isError(){
         return this.flag > SmsResultEnum.OK.flag;
    }

    public String getLogContent(){
        return logContent;
    }

    public String getResultContent(){
        return resultContent;
    }

    public Integer getFlag(){
        return flag;
    }
}
