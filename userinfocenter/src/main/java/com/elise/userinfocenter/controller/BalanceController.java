package com.elise.userinfocenter.controller;

import com.elise.userinfocenter.entity.LocalConfigProperties;
import com.elise.userinfocenter.pojo.UserInfoPOJO;
import com.elise.userinfocenter.service.BalanceService;
import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.http.HttpConnManager;
import com.hq.http.HttpPlainResult;
import com.hq.http.HttpResultDetail;
import com.hq.http.HttpResultHandler;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * @author hq
 * @email hq@hq.com
 * @date 2018-02-27 17:34:28
 */
@Controller
@RequestMapping("/api")
public class BalanceController extends AbstractRestController {

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private HttpConnManager httpConnManager;

    @Autowired
    private LocalConfigProperties config;

    @ApiOperation(value = "获取用户余额")
    @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/getBalance", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<Map<String, Object>>> getBalance(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            if (StringUtils.isBlank(token)) {
                return this.error("token 不可以为空");
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("token", token);

            String schoolId = this.getSchoolId(request);
            HttpPlainResult result = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost() + "/inner/userInfo", map, schoolId);
            TRACER.info(result.getResult());
            HttpResultDetail<UserInfoPOJO> entry = HttpResultHandler.handle(result, UserInfoPOJO.class);
            if (entry.isOK()) {
                Long userId = Long.valueOf(entry.getResult().getUid());
                return this.success(balanceService.queryBalanceByUserId(userId), TransactionStatus.OK);
            } else if (entry.isClientError()) {
                return this.error(entry.getResponseMessage(), entry.getResponseStatus());
            } else if (entry.isServerError()) {
                return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
            }
            return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "获取用户恒企币余额")
    @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/getUserHqg", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<Map<String, Object>>> getUserHqg(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            if (StringUtils.isBlank(token)) {
                return this.error("token 不可以为空");
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("token", token);

            String schoolId = this.getSchoolId(request);
            HttpPlainResult result = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost() + "/inner/userInfo", map, schoolId);
            TRACER.info(result.getResult());
            HttpResultDetail<UserInfoPOJO> entry = HttpResultHandler.handle(result, UserInfoPOJO.class);

            HashMap<String,String> data = new HashMap<String,String>();
            if (entry.isOK()) {
                Long userId = Long.valueOf(entry.getResult().getUid());
                if(null == balanceService.queryHqgByUserId(userId)){
                    data.put("hqg",0+"");
                    return this.success(data, TransactionStatus.OK);
                }
                double hqg = balanceService.queryHqgByUserId(userId);

                data.put("hqg",hqg+"");
                return this.success(data, TransactionStatus.OK);
            } else if (entry.isClientError()) {
                return this.error(entry.getResponseMessage(), entry.getResponseStatus());
            } else if (entry.isServerError()) {
                return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
            }

        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }


}
