package com.hq.learningapi.controller;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.learningapi.entity.AppBannerEntity;
import com.hq.learningapi.service.AdaptiveGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DL on 2018/10/26.
 */
@Controller
@RequestMapping("/api")
@Api(description = "判断是否是自适应用户")
public class AdaptiveUsersController extends AbstractRestController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AdaptiveGoodsService adaptiveGoodsService;

    @ApiOperation(value = "判断是否是自适应用户",notes = "0:不是,1:是")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/isAdaptiveUser", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> isAdaptive(HttpServletRequest request) {
        String token = ServletRequestUtils.getStringParameter(request, "token", "");
        try {
            int isAdaptiveUser = adaptiveGoodsService.isAdaptiveUsers(request,token);
            HashMap<String,Object> result = new HashMap<>();
            result.put("isAdaptiveUser",isAdaptiveUser);
            return this.success(result);
        } catch (Exception e) {
            logger.error("判断是否是自适应用户执行失败,cause:{}",e.toString());
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
