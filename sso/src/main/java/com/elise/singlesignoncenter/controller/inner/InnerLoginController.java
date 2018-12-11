package com.elise.singlesignoncenter.controller.inner;

import com.elise.singlesignoncenter.annotation.BusinessId;
import com.elise.singlesignoncenter.config.LocalConfigEntity;
import com.elise.singlesignoncenter.dao.SysBusinessDao;
import com.elise.singlesignoncenter.entity.UserInfoEntity;
import com.elise.singlesignoncenter.pojo.Token;
import com.elise.singlesignoncenter.service.LogService;
import com.elise.singlesignoncenter.service.UserInfoService;
import com.elise.singlesignoncenter.service.impl.UserInfoRedisServiceImpl;
import com.elise.singlesignoncenter.token.UserToken;
import com.elise.singlesignoncenter.token.UserTokenGenerator;
import com.elise.singlesignoncenter.util.PassWordUtil;
import com.hq.common.enumeration.ClientTypeEnum;
import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
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

import javax.servlet.http.HttpServletRequest;


/**
 * Created by Glenn on 2017/4/26 0026.
 */

@Controller
@RequestMapping("/inner")
public class InnerLoginController extends AbstractRestController {

    @Autowired
    protected LocalConfigEntity config;

    @Autowired
    private LogService logService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SysBusinessDao sysBusinessDao;

    @Autowired
    private UserInfoRedisServiceImpl userInfoRedisService;

    @ApiOperation(value = "用户登陆接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobileNo", value = "用户手机号码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "passWord", value = "用户密码(Base64混淆)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "clientType", value = "客户端类型\n(小写ios,android,web)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "versionCode", value = "客户端版本\n(web客户端版本号待定)", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "经度参数", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "latitude", value = "纬度参数", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<Token>> login(@BusinessId String businessId , HttpServletRequest request) {
        try {
            String mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo", "");
            String passWord = ServletRequestUtils.getStringParameter(request, "passWord", "");
            String clientType = ServletRequestUtils.getStringParameter(request, "clientType", "");
            Integer versionCode = ServletRequestUtils.getIntParameter(request, "versionCode", -1);
            String strLongitude = ServletRequestUtils.getStringParameter(request, "longitude", "");
            String strLatitude = ServletRequestUtils.getStringParameter(request, "latitude", "");

            if (sysBusinessDao.checkValidationOfBusiness(businessId) != 1) {
                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
            }
                if (StringUtils.isBlank(mobileNo) || StringUtils.isBlank(passWord)
                    || StringUtils.isBlank(clientType) || versionCode == -1) {
                return this.error("账号密码不可为空");
            }
            if (TRACER.isInfoEnabled()) {
                TRACER.info(getIncomingStack(mobileNo, passWord, businessId, clientType, versionCode));
            }
			
//            if (!PassWordUtil.isValid(passWord)) {
//                TRACER.info("Plain String is " + passWord);
//                return this.error(TransactionStatus.INVALID_PASS_WORD);
//            }

            UserInfoEntity user = userInfoService.getUserInfoItem(mobileNo);
            if (user != null) {
                if (user.getStatus() == 0) {
                    return this.error(TransactionStatus.FROZEN_USER);
                }
                ClientTypeEnum clientTypeEnum = ClientTypeEnum.convertString2ClientType(clientType);
                //验证上传密码正确性
                if (PassWordUtil.isCorrect(passWord, user.getPassWord())) {
                    String oldToken = userInfoRedisService.getTokenByUserId(businessId, clientTypeEnum, user.getUserId());
                    //Kick Out Old Token
                    if (oldToken != null) {
                        userInfoRedisService.removeOldInfo(user.getUserId(), businessId, clientTypeEnum);
                        TRACER.info(String.format("\nOld token %s has been kick out", oldToken));
                    }
                    //Put it in Redis Cache
                    String strToken = UserTokenGenerator.generate(clientTypeEnum.getType(), versionCode, user.getUserId(), false);
                    userInfoRedisService.storeInfo(strToken, businessId, clientTypeEnum, user, config);
                    //Make login record
                    logService.createLoginRecord(this.getIPAddress(request), businessId, user.getUserId());
                    logService.refreshFistActivateLog(user.getUserId(), businessId);
                    // 返回token值
                    Token token = new Token(strToken);
                    return this.success(token);
                } else {
                    TRACER.error(String.format("\nInput password is invalid, mobile number %s", mobileNo));
                    return this.error(TransactionStatus.PASSWORD_ERROR);
                }
            } else {
                TRACER.error(String.format("\nCan't find userId by mobile number %s", mobileNo));
                return this.error(TransactionStatus.USER_NOT_FOUND);
            }
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "根据客户端token获取web token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "客户端token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/webToken", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<Token>> getWebToken(@BusinessId String businessId,HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
           // String businessId = this.getSchoolId(request);
            if (sysBusinessDao.checkValidationOfBusiness(businessId) != 1) {
                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
            }
            UserToken userToken = UserTokenGenerator.parse(token);
            if(userToken.getClientType() == ClientTypeEnum.WEB){
                return this.error("Token 类型错误");
            }
            UserInfoEntity user = userInfoService.getUserInfoItem(userToken.getUserId());
            if (user != null) {
                if (user.getStatus() == 0) {
                    return this.error(TransactionStatus.FROZEN_USER);
                }
                String oldToken = userInfoRedisService.getTokenByUserId(businessId, ClientTypeEnum.WEB, user.getUserId());
                //Kick Out Old Token
                if (oldToken != null) {
                    userInfoRedisService.removeOldInfo(user.getUserId(), businessId, ClientTypeEnum.WEB);
                    TRACER.info(String.format("\nOld token %s has been kick out", oldToken));
                }
                //Put it in Redis Cache
                String strToken = UserTokenGenerator.generate(ClientTypeEnum.WEB.getType(), 1, userToken.getUserId(), false);
                userInfoRedisService.storeInfo(strToken, businessId, ClientTypeEnum.WEB, user, config);
                //Make login record
                logService.createLoginRecord(this.getIPAddress(request), businessId, user.getUserId());
                logService.refreshFistActivateLog(user.getUserId(), businessId);
                // 返回token值
                return this.success(new Token(strToken));
            } else {
                TRACER.error(String.format("\nCan't find userInfo by userId %s", userToken.getUserId()));
                return this.error(TransactionStatus.USER_NOT_FOUND);
            }
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private String getIncomingStack(String mobileNo, String passWord, String schoolId, String clientType, Integer versionCode) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n[Incoming Request] \nmobileNo is ");
        sb.append(mobileNo);
        sb.append("\npassWord is ");
        sb.append(passWord);
        sb.append("\nschoolId is ");
        sb.append(schoolId);
        sb.append("\nclientType is ");
        sb.append(clientType);
        sb.append("\nversionCode is ");
        sb.append(versionCode);
        return sb.toString();
    }

}
