package com.elise.singlesignoncenter.controller;


import com.elise.singlesignoncenter.annotation.BusinessId;
import com.elise.singlesignoncenter.config.LocalConfigEntity;
import com.elise.singlesignoncenter.dao.SysBusinessDao;
import com.elise.singlesignoncenter.entity.UserInfoEntity;
import com.elise.singlesignoncenter.service.LogService;
import com.elise.singlesignoncenter.service.UserInfoService;
import com.elise.singlesignoncenter.service.impl.SmsRedisServiceImpl;
import com.elise.singlesignoncenter.service.impl.UserInfoRedisServiceImpl;
import com.elise.singlesignoncenter.token.UserToken;
import com.elise.singlesignoncenter.token.UserTokenGenerator;
import com.elise.singlesignoncenter.util.PassWordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hq.common.enumeration.ClientTypeEnum;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * Created by Glenn on 2017/4/26 0026.
 */


@Controller
@RequestMapping(value = "/inner")
public class PassWordController extends AbstractRestController {

    @Autowired
    protected LocalConfigEntity config;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private LogService logService;

    @Autowired
    private UserInfoRedisServiceImpl userInfoRedisService;

    @Autowired
    private SmsRedisServiceImpl smsRedisService;

    @Autowired
    private SysBusinessDao sysBusinessDao;

    @Autowired
    protected RedisTemplate redisTemplate;

    private final static String realPassWord = "realPassWord:";

    @ApiOperation(value = "密码修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "activePassWord", value = "用户新输入密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "inactivePassWord", value = "用户原本密码", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/passWord", method = RequestMethod.PUT)
    public ResponseEntity<WrappedResponse<String>> modifyPassword(@BusinessId String businessId , HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", null);
            String activePassWord = ServletRequestUtils.getStringParameter(request, "activePassWord", null);
            String inactivePassWord = ServletRequestUtils.getStringParameter(request, "inactivePassWord", null);
            if (TRACER.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("\nToken:").append(token);
                sb.append("\nActive PassWord:").append(activePassWord);
                sb.append("\nInactive PassWord:").append(inactivePassWord);
                TRACER.info(sb.toString());
            }
            if (sysBusinessDao.checkValidationOfBusiness(businessId) != 1) {
                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
            }
            if (StringUtils.isBlank(activePassWord) || StringUtils.isBlank(inactivePassWord)) {
                return this.error("必要参数不可以为空");
            }
            if (!PassWordUtil.isValid(activePassWord)) {
                TRACER.info("PassWord is " + activePassWord);
                return this.error(TransactionStatus.INVALID_PASS_WORD);
            }
            UserToken userToken = UserTokenGenerator.parse(token);
            String tokenInRedis = userInfoRedisService.getTokenByUserId(businessId,userToken.getClientType(),userToken.getUserId());
            if (tokenInRedis == null || !tokenInRedis.equals(token)) {
                return this.error("登录信息失效，请重新登陆", TransactionStatus.USER_TOKEN_NOT_FOUND);
            }
            String passWordInDatabase = userInfoService.queryUserPassWord(userToken.getUserId());
            if (PassWordUtil.isCorrect(inactivePassWord, passWordInDatabase)) {
                String modifiedPassWord = PassWordUtil.encrypted(activePassWord);
                int result = userInfoService.modifyUserPassWord(userToken.getUserId(), modifiedPassWord);
                if (result == 1) {
                    //Make a operation record
                    UserInfoEntity entity = new UserInfoEntity();
                    entity.setPassWord(modifiedPassWord);
                    entity.setUserId(userToken.getUserId());
                    String method = this.getRequestMethod(request);
                    String path = this.getRequestPath(request);
                    String ip = this.getIPAddress(request);
                    logService.createOpsRecord(userToken.getUserId(), new ObjectMapper().writeValueAsString(entity), ip, businessId, method, path);

                    TRACER.info("Update PassWord OK");
                    return this.success("密码重置成功");
                } else {
                    TRACER.info("Update PassWord Failed");
                    return this.error("密码写入出错");
                }
            } else {
                TRACER.info("Validate PassWord Error");
                return this.error("原密码输入错误", TransactionStatus.PASSWORD_ERROR);
            }
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "密码重置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobileNo", value = "用户手机号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "passWord", value = "新用户密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "otp", value = "短信验证码", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/passWord", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<String>> resetPassword(@BusinessId String businessId , HttpServletRequest request) {
        try {
            String mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo", null);
            String passWord = ServletRequestUtils.getStringParameter(request, "passWord", null);
            String otp = ServletRequestUtils.getStringParameter(request, "otp", null);
            if (TRACER.isInfoEnabled()) {
                StringBuilder sbs = new StringBuilder();
                sbs.append("\nmobileNo:").append(mobileNo);
                sbs.append("\nPassWord:").append(passWord);
                sbs.append("\nOTP SMS:").append(otp);
                TRACER.info(sbs.toString());
            }
            if (sysBusinessDao.checkValidationOfBusiness(businessId) != 1) {
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
                    return this.error(TransactionStatus.USER_NOT_FOUND);
                }
                if (userInfoEntity.getStatus() == 0) {
                    return this.error(TransactionStatus.FROZEN_USER);
                }
                SmsResultEnum resultEnum = smsRedisService.checkSms(config.getOtpSmsSwitch(), config.getOtpPassKey(), mobileNo, otp);
                if (resultEnum.isError()) {
                    TRACER.error(resultEnum.getLogContent());
                    return this.error(resultEnum.getResultContent());
                }
                // Validate Sms Successfully,clear all redis cache associated with mobile number
                smsRedisService.clear(mobileNo);
                String modifiedPassWord = PassWordUtil.encrypted(passWord);
                int result = userInfoService.modifyUserPassWord(userInfoEntity.getUserId(), modifiedPassWord);
                if (result == 1) {
                    //Make a operation record
                    UserInfoEntity entity = new UserInfoEntity();
                    entity.setPassWord(modifiedPassWord);
                    entity.setUserId(userInfoEntity.getUserId());
                    String method = this.getRequestMethod(request);
                    String path = this.getRequestPath(request);
                    String ip = this.getIPAddress(request);
                    logService.createOpsRecord(userInfoEntity.getUserId(), new ObjectMapper().writeValueAsString(entity), ip, businessId, method, path);

                    TRACER.info("Reset PassWord Successful");
                    return this.success("密码重置成功");
                } else {
                    TRACER.error("Write password into database error");
                    return this.fail("密码写入出错");
                }
            } else {
                TRACER.error("Invalid mobile number");
                return this.error(TransactionStatus.INVALID_MOBILE_NUMBER);
            }
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiOperation(value = "检查密码是否正确")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobileNo", value = "用户手机", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "passWord", value = "用户密码", required = true, dataType = "String", paramType = "query")

    })
    @RequestMapping(value = "/checkPassWord", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<String>> checkPassWord( HttpServletRequest request) {
        try {
            String mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo", null);
            String passWord = ServletRequestUtils.getStringParameter(request, "passWord", null);

            if (StringUtils.isBlank(mobileNo)) {
                return this.error("手机号码不可为空");
            }
            if (StringUtils.isBlank(passWord)) {
                return this.error("密码不可为空");
            }
            //获取用户真实密码
            UserInfoEntity user = userInfoService.getUserInfoItem(mobileNo);
            int count = 0;
            Object object = redisTemplate.opsForValue().get(realPassWord + user.getUserId() + ":count:");
            if (object != null ){
                count = (int) object;
            }
            if (count == config.getCheckPasswordMaxCount()){
                return this.success("请求频繁,"+config.getPasswordMaxTimeout()/(60*1000)+"分钟后重试!",TransactionStatus.OK);
            }
            //把用户真实密码存redis;
            redisTemplate.opsForValue().set(realPassWord+user.getUserId()+":count:",++count);
            redisTemplate.expire(realPassWord+user.getUserId()+":count:",config.getPasswordMaxTimeout(), TimeUnit.MILLISECONDS);
            if (user != null) {
                    if (user.getStatus() == 0) {
                        return this.success("用户已经被冻结!",TransactionStatus.OK);
                    }
                    if (!PassWordUtil.isCorrect(passWord, user.getPassWord())) {
                        return this.success("密码输入错误!",TransactionStatus.OK);
                    }

                    return this.success("true",TransactionStatus.OK);
                }
                return this.success("用户不存在!",TransactionStatus.OK);
        } catch (Throwable t) {
            TRACER.error("", t.getMessage());
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
