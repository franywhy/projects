package com.school.controller.app;

import com.school.common.annotation.LcLog;
import com.school.controller.AbstractBaseController;
import com.school.pojo.LiveInfoPOJO;
import com.school.pojo.UserInfoPOJO;
import com.school.service.LiveService;
import com.school.service.SysConfigService;
import com.school.utils.LogEnum;
import com.school.web.model.WrappedResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/learningCenter/app")
@Api(description = "APP获取直播/回放地址接口")
public class AppLiveController extends AbstractBaseController {

    private static final String MAX_ANDROID_APP_VERSION = "max_android_version";
    private static final String MAX_IOS_APP_VERSION = "max_ios_version";

    private static final String IOS_CLIENT = "ios";
    private static final String ANDROID_CLIENT = "android";

    @Autowired
    private LiveService liveService;
    @Autowired
    private SysConfigService sysConfigService;

    @LcLog(value = "获取直播间直播信息:/getLiveInfo", type = LogEnum.LIVE)
    @ApiOperation(value = "获取直播间信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "classplanLiveId", value = "排课计划明细id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "appVersion", value = "app版本号", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "clientType", value = "客户端类型(ios,android)", required = false, dataType = "string", paramType = "query"),
    })
    @RequestMapping(value = "/getLiveInfo", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getLiveInfo(String classplanLiveId, HttpServletRequest request, HttpServletResponse response) {

        Long startTime = System.currentTimeMillis();

        UserInfoPOJO userInfo = this.getUserInfo(request, response);

        //String appVersion,String clientType,
        int appVersion = ServletRequestUtils.getIntParameter(request, "appVersion", 0);
        String clientType = ServletRequestUtils.getStringParameter(request, "clientType", "");
        if (null == classplanLiveId) return this.error("参数提交有误:classplanLiveId");

        String maxAndroidVersionStr = sysConfigService.getValue(MAX_ANDROID_APP_VERSION, "0");
        String maxIosVersionStr = sysConfigService.getValue(MAX_IOS_APP_VERSION, "0");
        int maxAndroidVersion = Integer.parseInt(maxAndroidVersionStr);
        int maxIosVersion = Integer.parseInt(maxIosVersionStr);
        LiveInfoPOJO result = liveService.queryLiveInfo(classplanLiveId, userInfo);
        //不传客户端默认是旧版
        if (StringUtils.isBlank(clientType)
                && (result.getBanSpeaking() == 1
                || result.getBanAsking() == 1
                || result.getHideAsking() == 1
                || result.getHideSpeaking() == 1)) {
            return this.updateVersion("当前版本过久，请升级您的APP", null);
        }
        //ios端
        if (IOS_CLIENT.equals(clientType) && maxIosVersion > appVersion
                && (result.getBanSpeaking() == 1
                || result.getBanAsking() == 1
                || result.getHideAsking() == 1
                || result.getHideSpeaking() == 1)) {
            return this.updateVersion("当前版本过久，请升级您的APP", null);
        }
        if (ANDROID_CLIENT.equals(clientType) && maxAndroidVersion > appVersion
                && (result.getBanSpeaking() == 1
                || result.getBanAsking() == 1
                || result.getHideAsking() == 1
                || result.getHideSpeaking() == 1)) {
            return this.updateVersion("当前版本过久，请升级您的APP", null);
        }

        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/app/getLiveInfo " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/app/getLiveInfo " + (endTime - startTime) + "ms");
        }

        if (null != result) {
            return this.success(result);
        }
        return this.fail("服务器内部错误", null);
    }

    @LcLog(value = "获取回放信息:/getReplayInfo", type = LogEnum.REPLAY)
    @ApiOperation(value = "获取回放信息", notes = "0:没有回放地址 1:展示互动 2:cc 3:保利威视")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "classplanLiveId", value = "排课计划明细id", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getReplayInfo", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getReplayInfo(String classplanLiveId, HttpServletRequest request, HttpServletResponse response) {

        Long startTime = System.currentTimeMillis();

        UserInfoPOJO userInfo = this.getUserInfo(request, response);

        //String classplanLiveId = request.getParameter("classplanLiveId");
        if (null == classplanLiveId) return this.error("参数提交有误:classplanLiveId");

        Map<String, Object> result = liveService.queryReplayInfo(classplanLiveId, userInfo);

        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/app/getReplayInfo " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/app/getReplayInfo " + (endTime - startTime) + "ms");
        }

        if (null != result) {
            return this.success(result);
        }
        return this.fail("服务器内部错误", null);
    }
}
