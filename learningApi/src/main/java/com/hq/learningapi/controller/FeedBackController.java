package com.hq.learningapi.controller;

/**
 * 新版吐槽接口
 * Created by DL on 2018/1/20.
 */

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.http.HttpConnManager;
import com.hq.http.HttpPlainResult;
import com.hq.http.HttpResultDetail;
import com.hq.http.HttpResultHandler;
import com.hq.learningapi.config.LocalConfigEntity;
import com.hq.learningapi.entity.AppFeedBackEntity;
import com.hq.learningapi.entity.ColdStartingEntity;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.FeedBackService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/api")
public class FeedBackController  extends AbstractRestController {

    @Autowired
    private HttpConnManager httpConnManager;
    @Autowired
    private LocalConfigEntity config;
    @Autowired
    private FeedBackService feedBackService;


    @ApiOperation(value = "保存用户新版吐槽信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "context", value = "反馈文本信息 context", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "picture", value = "反馈图片 picture", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "client", value = "客户端类型\n(小写ios,android,web)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "version", value = "客户端版本", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "productId", value = "产品线", required = true, dataType = "Long", paramType = "query"),
    })
    @RequestMapping(value = "saveFeedBack", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<WrappedResponse<List<ColdStartingEntity>>> saveFeedBack(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            String context = ServletRequestUtils.getStringParameter(request, "context", "");
            String picture = ServletRequestUtils.getStringParameter(request, "picture", "");
            String client = ServletRequestUtils.getStringParameter(request, "client", "");
            int version = ServletRequestUtils.getIntParameter(request, "version");
            Long productId = ServletRequestUtils.getLongParameter(request, "productId");
            if (StringUtils.isBlank(token)) {
                return this.error("token 不可以为空");
            }
            if (StringUtils.isBlank(context)) {
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
