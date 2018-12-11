package com.hq.learningapi.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.common.util.SchoolIdUtil;
import com.hq.http.HttpConnManager;
import com.hq.http.HttpPlainResult;
import com.hq.http.HttpResultDetail;
import com.hq.http.HttpResultHandler;
import com.hq.learningapi.config.LocalConfigEntity;
import com.hq.learningapi.config.TokenStatus;
import com.hq.learningapi.entity.UserTokenEntity;
import com.hq.learningapi.pojo.CheckVersionPOJO;
import com.hq.learningapi.service.VersionService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Created by DL on 2018/2/3.
 */
@Controller
@RequestMapping("/api")
public class AppVersionController extends AbstractRestController {
    @Autowired
    private VersionService checkVersionService;

    @Autowired
    private HttpConnManager httpConnManager;

    @Autowired
    private LocalConfigEntity config;

    @ApiOperation(value = "检查版本号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "versionCode", value = "客户端当前版本", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "clientType", value = "客户端类型Android/ios", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<CheckVersionPOJO>> checkVersion(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            Integer versionCode = ServletRequestUtils.getIntParameter(request, "versionCode", -1);
            String clientType = ServletRequestUtils.getStringParameter(request, "clientType", "ios");
            if (TRACER.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("\nToken:");
                sb.append(token);
                sb.append("\nVersion Code:");
                sb.append(versionCode);
                sb.append("\nclient Type:");
                sb.append(clientType);
                TRACER.info(sb.toString());
            }
            String schoolId = config.getSchoolId();
            HashMap<String, Object> map = new HashMap<>();
            //token不为空,登录用户,检查是否是灰度升级用户
            if (StringUtils.isNotBlank(token)){
                map.put("token", token);
                //判断token是否过期
                HttpPlainResult httpResult1 = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost() + "/inner/tokenExpired", map, schoolId);
                HttpResultDetail<TokenStatus> entry = HttpResultHandler.handle(httpResult1,TokenStatus.class);
                if (entry.isOK()) {
                    TokenStatus tokenStatus = entry.getResult();
                    if (tokenStatus.getExpired()) {
                        return this.error(TransactionStatus.USER_TOKEN_NOT_FOUND);
                    } else if (entry.isClientError()) {
                        return this.error(TransactionStatus.BAD_REQUEST);
                    } else if (entry.isServerError()) {
                        return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
                    }
                }
                HttpPlainResult httpResult = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost() + "/inner/userTokenDetail", map, schoolId);
                TRACER.info(httpResult.getResult());
                HttpResultDetail<UserTokenEntity> detail = HttpResultHandler.handle(httpResult, UserTokenEntity.class);
                TRACER.info(detail.toString());
                if (detail.isOK()) {
                    CheckVersionPOJO pojo = checkVersionService.getVersionPOJO(schoolId,
                            detail.getResult().getClientType(),
                            detail.getResult().getUserId(),
                            versionCode);
                    return pojo == null ? this.success(null, TransactionStatus.LATEST_VERSION) : this.success(pojo);
                } else if (detail.isClientError()) {
                    return this.error(TransactionStatus.BAD_REQUEST);
                } else {
                    return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
                }
            }else {
                //token为空,默认userId=-1,只是检查更新
                CheckVersionPOJO pojo = checkVersionService.getVersionPOJO(schoolId,clientType,-1,versionCode);
                return pojo == null ? this.success(null, TransactionStatus.LATEST_VERSION) : this.success(pojo);
            }
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.CHECK_VERSION_FAILED);
        }
    }
}
