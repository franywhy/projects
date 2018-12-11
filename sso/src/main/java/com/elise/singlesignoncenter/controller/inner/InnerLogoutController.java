package com.elise.singlesignoncenter.controller.inner;


import com.elise.singlesignoncenter.annotation.BusinessId;
import com.elise.singlesignoncenter.dao.SysBusinessDao;
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
@RequestMapping("/inner")
public class InnerLogoutController extends AbstractRestController {

    @Autowired
    private UserInfoRedisServiceImpl userInfoRedisService;

    @Autowired
    private SysBusinessDao sysBusinessDao;


    @ApiOperation(value = "用户登出")
    @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<String>> logout(@BusinessId String businessId , HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            TRACER.info(String.format("Incoming request,token is %s", token));
            if(sysBusinessDao.checkValidationOfBusiness(businessId) != 1){
                return this.error(TransactionStatus.UNKNOWN_BUSINESS_ID);
            }
            if (StringUtils.isBlank(token)) {
                return this.error("token 不可以为空");
            }
            UserToken userToken = UserTokenGenerator.parse(token);
            String tokenInRedis = userInfoRedisService.getTokenByUserId(businessId,userToken.getClientType(),userToken.getUserId());
            /*if (tokenInRedis == null || !tokenInRedis.equals(token)) {
                userInfoRedisService.removeOldInfo(userToken.getUserId(),businessId,userToken.getClientType());
            }*/
            if (tokenInRedis != null || tokenInRedis.equals(token)) {
                userInfoRedisService.removeOldInfo(userToken.getUserId(),businessId,userToken.getClientType());
            }
            return this.success("成功退出");

        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
