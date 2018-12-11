package com.school.controller.app;

import com.school.controller.AbstractBaseController;
import com.school.pojo.UserInfoPOJO;
import com.school.service.LcOffliveLogService;
import com.school.web.model.WrappedResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by DL on 2018/4/13.
 */
@Controller
@RequestMapping("/learningCenter/app")
public class AppOffLiveController extends AbstractBaseController {

    @Autowired
    private LcOffliveLogService lcOffliveLogService;

    @ApiOperation(value = "记录APP离线观看信息", notes="0:离线 1:在线")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "contextJson", value = "视频内容明细", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/saveOffLiveLog", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse> getReplayInfo(HttpServletRequest request, HttpServletResponse response) {
        UserInfoPOJO userInfo = this.getUserInfo(request,response);

        String contextJson = request.getParameter("contextJson");

        if(null == contextJson) return this.error("参数提交有误:contextJson");
        if(null == userInfo) return this.error("参数提交有误:SSOTOKEN");

       String errMsg = lcOffliveLogService.save(userInfo,contextJson);
        if(StringUtils.isNotBlank(errMsg)){
            return this.fail(errMsg,null);
        }
        return this.success("操作成功!");
    }
}
