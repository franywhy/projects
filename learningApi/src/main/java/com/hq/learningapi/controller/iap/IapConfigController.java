package com.hq.learningapi.controller.iap;

import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.learningapi.entity.AppConfigEntity;
import com.hq.learningapi.pojo.AppConfigPOJO;
import com.hq.learningapi.service.AppConfigService;
import com.hq.learningapi.service.SysConfigService;
import com.hq.learningapi.service.SysProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ios内购配置:是否可以充值
 * Created by DL on 2018/9/18.
 */
@Controller
@RequestMapping("/buy")
@Api(description = "ios内购配置参数接口")
public class IapConfigController extends AbstractRestController {

    private final static String IS_PHURCHASE_KEY = "is_phurchase";
    @Autowired
    private SysConfigService sysConfigService;

    @ApiOperation(value = "ios内购配置接口",notes = "0:不可充值,1:可以充值")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "query")
    })

    @RequestMapping(value = "/iapConfig",method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> constantValueList(HttpServletRequest request) {
        Map<String,Object> result = new HashedMap();
        String token = ServletRequestUtils.getStringParameter(request, "token", "");

        String keyValue = sysConfigService.getValue(IS_PHURCHASE_KEY, "0");

        int isPhurchase = Integer.parseInt(keyValue);
        String value = isPhurchase == 0 ? "充值失败":"请充值";
        result.put("isPhurchase",isPhurchase);
        result.put("value",value);
        return this.success(result);
    }
}
