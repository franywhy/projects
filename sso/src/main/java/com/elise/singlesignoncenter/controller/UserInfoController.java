package com.elise.singlesignoncenter.controller;


import com.elise.singlesignoncenter.annotation.BusinessId;
import com.elise.singlesignoncenter.config.LocalConfigEntity;
import com.elise.singlesignoncenter.dao.SysBusinessDao;
import com.elise.singlesignoncenter.entity.UserInfoEntity;
import com.elise.singlesignoncenter.pojo.UserInfoPOJO;
import com.elise.singlesignoncenter.service.LogService;
import com.elise.singlesignoncenter.service.UserInfoService;
import com.elise.singlesignoncenter.service.impl.UserInfoRedisServiceImpl;
import com.elise.singlesignoncenter.token.UserToken;
import com.elise.singlesignoncenter.token.UserTokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@RequestMapping(value = "/inner")
public class UserInfoController extends AbstractRestController {

    @Autowired
    private UserInfoRedisServiceImpl userInfoRedisService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private LogService logService;

    @Autowired
    private SysBusinessDao sysBusinessDao;

    @Autowired
    protected LocalConfigEntity config;

    @ApiOperation(value = "获取用户信息")
    @ApiImplicitParam(name = "token", value = "用户 Token", required = false, dataType = "String", paramType = "query")
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<UserInfoPOJO>> getUserInfo(@BusinessId String businessId , HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", null);
            TRACER.info("\nUser Token:" + token);

            if (sysBusinessDao.checkValidationOfBusiness(businessId) != 1) {
                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
            }
            UserToken userToken = UserTokenGenerator.parse(token);
            String tokenInRedis = userInfoRedisService.getTokenByUserId(businessId,userToken.getClientType(),userToken.getUserId());
            if (tokenInRedis == null || !tokenInRedis.equals(token)) {
                TRACER.info(String.format("Token %s is timeout", token));
                return this.error("登录信息失效，请重新登陆", TransactionStatus.USER_TOKEN_NOT_FOUND);
            } else {
                UserInfoPOJO userInfo = new UserInfoPOJO();
                UserInfoEntity entity = userInfoService.getUserInfoItem(userToken.getUserId());
                userInfo.setNickName(userInfoRedisService.getNickName(userToken.getUserId(),businessId));
                userInfo.setAvatar(userInfoRedisService.getAvatar(userToken.getUserId(),businessId));
                userInfo.setEmail(userInfoRedisService.getEmail(userToken.getUserId(),businessId));
                userInfo.setGender(userInfoRedisService.getGender(userToken.getUserId(),businessId));
                userInfo.setUid(entity.getUserId());
                TRACER.info(String.format("\nUserId:%d\nUserInfo:\n%s", userToken.getUserId(), userInfo));
                return this.success(userInfo);
            }
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
    public ResponseEntity<WrappedResponse<String>> modifyUserInfo(@BusinessId String businessId,HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", null);
            String nickName = ServletRequestUtils.getStringParameter(request, "nickName", null);
            String email = ServletRequestUtils.getStringParameter(request, "email", null);
            String avatar = ServletRequestUtils.getStringParameter(request, "avatar", null);
            Integer gender = ServletRequestUtils.getIntParameter(request, "gender", -1);
            //String businessId = this.getSchoolId(request);
            if (TRACER.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("\n[User Info]");
                sb.append("\nToken:").append(token);
                sb.append("\nNike Name:").append(nickName);
                sb.append("\nEmail:").append(email);
                sb.append("\nGender:").append(gender);
                sb.append("\nAvatar:").append(avatar);
                TRACER.info(sb.toString());
            }
            if (sysBusinessDao.checkValidationOfBusiness(businessId) != 1) {
                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
            }
            Boolean isBlank = (StringUtils.isBlank(nickName) && StringUtils.isBlank(email) && gender == -1 &&StringUtils.isBlank(avatar));
            if (isBlank) {
                return this.error("用户信息不能为空");
            } else {
                UserToken userToken = UserTokenGenerator.parse(token);
                String tokenInRedis = userInfoRedisService.getTokenByUserId(businessId,userToken.getClientType(),userToken.getUserId());
                if (tokenInRedis == null || !tokenInRedis.equals(token)) {
                    TRACER.info(String.format("Token %s is timeout", token));
                    return this.error(TransactionStatus.USER_TOKEN_NOT_FOUND);
                } else {
                    int result = userInfoService.updateUserInfo(avatar,nickName, email, gender, userToken.getUserId());
                    if (result == 1) {
                        userInfoRedisService.UpdateUserInfo(userToken.getUserId(),businessId,avatar,nickName, email, gender, this.config);
                        // Make a operation record;
                        UserInfoEntity entity = new UserInfoEntity();
                        entity.setNickName(nickName);
                        entity.setEmail(email);
                        entity.setSex(gender);
                        entity.setAvatar(avatar);
                        String method = request.getMethod();
                        String path = request.getServletPath();
                        String ip = this.getIPAddress(request);
                        logService.createOpsRecord(userToken.getUserId(),
                                new ObjectMapper().writeValueAsString(entity),
                                ip,
                                businessId,
                                method,
                                path);
                        return this.success("用户信息更新成功");
                    } else {
                        TRACER.error("Update UserInfo Failed");
                        return this.fail("用户信息更新失败");
                    }
                }
            }
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
