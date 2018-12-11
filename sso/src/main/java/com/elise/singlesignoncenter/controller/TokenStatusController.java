package com.elise.singlesignoncenter.controller;


import com.elise.singlesignoncenter.annotation.BusinessId;
import com.elise.singlesignoncenter.config.LocalConfigEntity;
import com.elise.singlesignoncenter.dao.SysBusinessDao;
import com.elise.singlesignoncenter.pojo.TokenStatus;
import com.elise.singlesignoncenter.service.impl.GetBusinessIdServiceImpl;
import com.elise.singlesignoncenter.service.impl.UserInfoRedisServiceImpl;
import com.elise.singlesignoncenter.token.UserToken;
import com.elise.singlesignoncenter.token.UserTokenGenerator;
import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import io.swagger.annotations.ApiImplicitParam;
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
public class TokenStatusController extends AbstractRestController {

    @Autowired
    protected LocalConfigEntity config;

    @Autowired
    private UserInfoRedisServiceImpl userInfoRedisService;

    @ApiOperation(value = "检查Token是否过期")
    @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/tokenExpired", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<TokenStatus>> checkToken(@BusinessId String businessId , HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", null);
            TRACER.info("\nUser Token:" + token);
            if(StringUtils.isBlank(businessId)){
                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
            }
            if (StringUtils.isBlank(token)) {
                return this.error("token 不可以为空");
            } else {
                UserToken userToken = UserTokenGenerator.parse(token);
                String tokenInRedis = userInfoRedisService.getTokenByUserId(businessId,userToken.getClientType(),userToken.getUserId());
                TokenStatus status = new TokenStatus();
                if (tokenInRedis != null && tokenInRedis.equals(token)) {
                    Boolean isExpired = UserTokenGenerator.isExpired(token, config.getTokenExpiredTime());
                    if (isExpired) {
                        userInfoRedisService.removeOldInfo(userToken.getUserId(),businessId,userToken.getClientType());
                        status.setExpired(true);
                    } else {
                        status.setExpired(false);
                    }
                } else {
                    status.setExpired(true);
                }
                TRACER.info(String.format("User Token Status:%s", status.toString()));
                return this.success(status);
            }
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
