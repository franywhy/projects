package com.hq.answerapi.controller;

import com.hq.answerapi.config.LocalConfigEntity;
import com.hq.answerapi.entity.AppConfigEntity;
import com.hq.answerapi.entity.AppFeedBackEntity;
import com.hq.answerapi.pojo.AppConfigPOJO;
import com.hq.answerapi.pojo.CheckVersionPOJO;
import com.hq.answerapi.pojo.UserInfoPOJO;
import com.hq.answerapi.service.AppConfigService;
import com.hq.answerapi.service.FeedBackService;
import com.hq.answerapi.service.VersionService;
import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.http.HttpConnManager;
import com.hq.http.HttpPlainResult;
import com.hq.http.HttpResultDetail;
import com.hq.http.HttpResultHandler;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Administrator on 2018/9/25 .
 */
@Controller
@RequestMapping("/api/myself")
public class MyselfSettingsController extends AbstractRestController {

    @Autowired
    private VersionService checkVersionService;

    @Autowired
    private LocalConfigEntity config;
    @Autowired
    private HttpConnManager httpConnManager;
    @Autowired
    private FeedBackService feedBackService;

    @Autowired
    private AppConfigService appConfigService;



    @ApiOperation(value = "检查版本号")
    @ApiImplicitParams({
             @ApiImplicitParam(name = "token", value = "用户 Token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "versionCode", value = "客户端当前版本", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "clientType", value = "客户端类型android/ios", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户id", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "businessId", value = "业务id", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<CheckVersionPOJO>> checkVersion(HttpServletRequest request) {
        try {
            // String token = ServletRequestUtils.getStringParameter(request, "token", "");
            Integer versionCode = ServletRequestUtils.getIntParameter(request, "versionCode", -1);
            Integer userId = ServletRequestUtils.getIntParameter(request, "userId", -1);
            String clientType = ServletRequestUtils.getStringParameter(request, "clientType", "");
            String businessId = ServletRequestUtils.getStringParameter(request, "businessId", "");
            if (TRACER.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("\nuserId:");
                sb.append(userId);
                sb.append("\nVersion Code:");
                sb.append(versionCode);
                sb.append("\nclient Type:");
                sb.append(clientType);
                sb.append("\nbusinessId:");
                sb.append(businessId);
                TRACER.info(sb.toString());
            }
            if (versionCode == -1) {
                return this.error("VersionCode 不可为空");
            }
            if (StringUtils.isBlank(clientType)) {
                return this.error("系统版本号clientType 不可为空");
            }
            if (StringUtils.isBlank(businessId)) {
                return this.error("业务ID businessId 不可为空");
            }
            CheckVersionPOJO pojo = checkVersionService.getVersionPOJO("teacher",clientType,userId,versionCode);
            return pojo == null ? this.success(null, TransactionStatus.LATEST_VERSION) : this.success(pojo);
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.CHECK_VERSION_FAILED);
        }
    }



    @ApiOperation(value = "保存用户新版吐槽信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "context", value = "反馈文本信息 context", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "picture", value = "反馈图片 picture", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "client", value = "客户端类型\n(小写ios,android,web)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "version", value = "客户端版本", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "productId", value = "(教师端这里填2！)", required = true, dataType = "Long", paramType = "query"),
    })
    @RequestMapping(value = "saveFeedBack", method = RequestMethod.POST)
    @ResponseBody
//    public ResponseEntity<WrappedResponse<List<ColdStartingEntity>>> saveFeedBack(HttpServletRequest request) {
        public ResponseEntity<WrappedResponse<List<String>>> saveFeedBack(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            String context = ServletRequestUtils.getStringParameter(request, "context", "");
            String picture = ServletRequestUtils.getStringParameter(request, "picture", "");
            String client = ServletRequestUtils.getStringParameter(request, "client", "");
            int version = ServletRequestUtils.getIntParameter(request, "version");
            Long productId = ServletRequestUtils.getLongParameter(request, "productId");
            if (org.apache.commons.lang3.StringUtils.isBlank(token)) {
                return this.error("token 不可以为空");
            }
            if (org.apache.commons.lang3.StringUtils.isBlank(context)) {
                return this.error("反馈内容 不可以为空");
            }
            if (context.length() > 1000) {
                return this.error("反馈内容 不可以超过1000字");
            }
            if (null == productId) {
                return this.error("产品线 不可以为空");
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            if (token != null) {
                map.put("token", token);
            }
            String schoolId = this.getSchoolId(request);
            HttpPlainResult result = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost() + "/inner/userMobileNo", map, schoolId);
            HttpResultDetail<UserInfoPOJO> entry = HttpResultHandler.handle(result, UserInfoPOJO.class);
            TRACER.info(result.getResult());
            if (entry.isOK()) {
                AppFeedBackEntity entity = new AppFeedBackEntity();
                entity.setUserMobile(entry.getResult().getMobileNo());
                entity.setFeedBackContext(context);
                entity.setPicture(picture);
                entity.setProductId(productId);
                entity.setClient(client);
                entity.setVersion(version);
                entity.setCreateTime(new Date());
                feedBackService.saveFeedBack(entity);
                return this.success(TransactionStatus.OK);
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

}
