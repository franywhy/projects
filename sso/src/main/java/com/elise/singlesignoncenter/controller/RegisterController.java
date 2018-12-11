package com.elise.singlesignoncenter.controller;


import com.elise.singlesignoncenter.annotation.BusinessId;
import com.elise.singlesignoncenter.config.LocalConfigEntity;
import com.elise.singlesignoncenter.dao.SysBusinessDao;
import com.elise.singlesignoncenter.entity.AppUserChannelsEntity;
import com.elise.singlesignoncenter.entity.UserInfoEntity;
import com.elise.singlesignoncenter.service.AppUserChannelsService;
import com.elise.singlesignoncenter.service.LogService;
import com.elise.singlesignoncenter.service.UserInfoService;
import com.elise.singlesignoncenter.service.impl.SmsRedisServiceImpl;
import com.elise.singlesignoncenter.util.PassWordUtil;
import com.elise.singlesignoncenter.util.SendChannelIdUtil;
import com.elise.singlesignoncenter.util.UserIdGenerator;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Glenn on 2017/4/26 0026.
 */


@Controller
@RequestMapping(value = "/inner")
public class RegisterController extends AbstractRestController {

    @Autowired
    protected LocalConfigEntity config;

    @Autowired
    private LogService logService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SysBusinessDao sysBusinessDao;

    @Autowired
    private SmsRedisServiceImpl smsRedisService;

    @Autowired
    private UserIdGenerator userIdGenerator;
    
    @Autowired
    private AppUserChannelsService appUserChannelsService;

    @Value("${domain.msg-host}")
    private String msgHost;

    @ApiOperation(value = "新用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobileNo", value = "新用户手机号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "passWord", value = "新用户密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "otp", value = "短信验证码", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public  ResponseEntity<WrappedResponse<String>> register(@BusinessId String businessId , HttpServletRequest request) {
        try {
            String mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo", null);
            String passWord = ServletRequestUtils.getStringParameter(request, "passWord", null);
            String otp = ServletRequestUtils.getStringParameter(request, "otp", null);

            if(sysBusinessDao.checkValidationOfBusiness(businessId) != 1){
                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
            }
            if (StringUtils.isBlank(mobileNo) || StringUtils.isBlank(passWord) || StringUtils.isBlank(otp)) {
                return this.error("请求参数不可为空");
            }
            if (!PassWordUtil.isValid(passWord)) {
                TRACER.info("\nPassWord is " + passWord);
                return this.error(TransactionStatus.INVALID_PASS_WORD);
            }
            MobileNoRegular mobileNumber = new MobileNoRegular(mobileNo);
            if (mobileNumber.isValidMobileNo()) {
                UserInfoEntity userInfoEntity = userInfoService.getUserInfoItem(mobileNo);
                if (userInfoEntity == null) {
                    SmsResultEnum resultEnum = smsRedisService.checkSms(config.getOtpSmsSwitch(),config.getOtpPassKey(),mobileNo,otp);
                    if(resultEnum.isError()){
                        TRACER.error(resultEnum.getLogContent());
                        return this.error(resultEnum.getResultContent());
                    }
                    // Validate Sms Successfully,clear all redis cache associated with mobile number
                    smsRedisService.clear(mobileNo);
                    Integer userId = userIdGenerator.nextId();
                    String avatar = config.getDefaultAvatarUrl();
                    String nickName = mobileNumber.generateDefaultNickName();
                    Integer result = userInfoService.insertUserInfo(userId, businessId, mobileNumber.getValue(), nickName, PassWordUtil.encrypted(passWord), avatar);
                    if (result > 0) {
                        //Make a record for first activated account
                        logService.createFistActivateRecord(userId,businessId);
                        try {
                        	List<String> list = new ArrayList<String>();
                        	list.add(userId.toString());
                        	list.add("public");
                        	for (String channel : list) {
								
                        		AppUserChannelsEntity entity = new AppUserChannelsEntity();
                        		entity.setUserId(Long.valueOf(userId.toString()));
                        		entity.setChannelId(channel);
                        		this.appUserChannelsService.save(entity);
                        		TRACER.error("消息系统地址"+msgHost);
                        		SendChannelIdUtil.sendChannelId(msgHost,userId, channel, 1);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
                        return this.success("注册成功");
                    } else {
                        return this.fail("注册失败，请稍后重试");
                    }
                } else {
                    return this.error("此手机号已注册，请登录",TransactionStatus.DUPLICATE_MOBILE_NUMBER);
                }
            } else {
                TRACER.error("Invalid mobile number");
                return this.error("请输入正确的手机号格式",TransactionStatus.INVALID_MOBILE_NUMBER);
            }
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
