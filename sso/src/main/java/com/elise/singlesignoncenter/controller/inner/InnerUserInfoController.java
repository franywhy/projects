package com.elise.singlesignoncenter.controller.inner;


import com.elise.singlesignoncenter.annotation.BusinessId;
import com.elise.singlesignoncenter.dao.SysBusinessDao;
import com.elise.singlesignoncenter.entity.NcIdEntity;
import com.elise.singlesignoncenter.pojo.MobileNumber;
import com.elise.singlesignoncenter.pojo.UserMobileNoPOJO;
import com.elise.singlesignoncenter.service.UserInfoService;
import com.elise.singlesignoncenter.service.impl.UserInfoRedisServiceImpl;
import com.elise.singlesignoncenter.token.UserToken;
import com.elise.singlesignoncenter.token.UserTokenGenerator;
import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
public class InnerUserInfoController extends AbstractRestController {

    @Autowired
    private UserInfoRedisServiceImpl userInfoRedisService;

    @Autowired
    private SysBusinessDao sysBusinessDao;

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "通过Token获取内部详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
    })

    @RequestMapping(value = "/userTokenDetail", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<UserToken>> getUserIdByToken(@BusinessId String businessId , HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", null);
//            String businessId = this.getSchoolId(request);
            TRACER.info("\nUser Token:" + token);
            if (sysBusinessDao.checkValidationOfBusiness(businessId) != 1) {
                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
            }
            UserToken userToken = UserTokenGenerator.parse(token);
            String tokenInRedis = userInfoRedisService.getTokenByUserId(businessId, userToken.getClientType(), userToken.getUserId());
            if (tokenInRedis == null || !tokenInRedis.equals(token)) {
                TRACER.info(String.format("Token %s is timeout", token));
                return this.error("登录信息失效，请重新登陆", TransactionStatus.USER_TOKEN_NOT_FOUND);
            } else {
                return this.success(userToken);
            }
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "通过token获取NcId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/ncId", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<NcIdEntity>> getNcIdByToken(@BusinessId String businessId,HttpServletRequest request) {

        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", null);
            //String businessId = this.getSchoolId(request);
            TRACER.info("\nUser Token:" + token);
            if (sysBusinessDao.checkValidationOfBusiness(businessId) != 1) {
                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
            }
            UserToken userToken = UserTokenGenerator.parse(token);
            String tokenInRedis = userInfoRedisService.getTokenByUserId(businessId, userToken.getClientType(), userToken.getUserId());
            if (tokenInRedis == null || !tokenInRedis.equals(token)) {
                TRACER.info(String.format("Token %s is timeout", token));
                return this.error("登录信息失效，请重新登陆", TransactionStatus.USER_TOKEN_NOT_FOUND);
            } else {
                String ncId = userInfoService.getNcId(userToken.getUserId());
                NcIdEntity entity = new NcIdEntity();
                entity.setId(ncId);
                return this.success(entity);
            }
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @ApiOperation(value = "通过Token获取用户手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
    })

    @RequestMapping(value = "/userMobileNo", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<MobileNumber>> getMobileNodByToken(@BusinessId String businessId , HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", null);
            TRACER.info("\nUser Token:" + token);
            if (sysBusinessDao.checkValidationOfBusiness(businessId) != 1) {
                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
            }
            UserToken userToken = UserTokenGenerator.parse(token);
            String tokenInRedis = userInfoRedisService.getTokenByUserId(businessId, userToken.getClientType(), userToken.getUserId());
            if (tokenInRedis == null || !tokenInRedis.equals(token)) {
                TRACER.info(String.format("Token %s is timeout", token));
                return this.error("登录信息失效，请重新登陆", TransactionStatus.USER_TOKEN_NOT_FOUND);
            } else {
                String mobileNo = userInfoRedisService.getMobileNo(userToken.getUserId(), businessId);
                if (mobileNo == null) {
                    TRACER.info(String.format("Can't find mobile number by token : %s", token));
                    return this.fail("缓存失效");
                } else {
                    MobileNumber userInfoPOJO = new MobileNumber();
                    userInfoPOJO.setMobileNo(mobileNo);
                    return this.success(userInfoPOJO);
                }
            }
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "TOKEN获取用户手机号码和用户ID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/userIdMobileNo", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<MobileNumber>> getUserIdMobileNodByToken(@BusinessId String businessId , HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", null);
            TRACER.info("\nUser Token:" + token);
            if (sysBusinessDao.checkValidationOfBusiness(businessId) != 1) {
                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
            }
            UserToken userToken = UserTokenGenerator.parse(token);
            String tokenInRedis = userInfoRedisService.getTokenByUserId(businessId, userToken.getClientType(), userToken.getUserId());
            if (tokenInRedis == null || !tokenInRedis.equals(token)) {
                TRACER.info(String.format("Token %s is timeout", token));
                return this.error("登录信息失效，请重新登陆", TransactionStatus.USER_TOKEN_NOT_FOUND);
            } else {
                String mobileNo = userInfoRedisService.getMobileNo(userToken.getUserId(), businessId);
                if (mobileNo == null) {
                    TRACER.info(String.format("Can't find mobile number by token : %s", token));
                    return this.fail("缓存失效");
                } else {
                    UserMobileNoPOJO userInfoPOJO = new UserMobileNoPOJO();
                    userInfoPOJO.setMobileNo(mobileNo);
                    userInfoPOJO.setUserId(userToken.getUserId());
                    return this.success(userInfoPOJO);
                }
            }
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
