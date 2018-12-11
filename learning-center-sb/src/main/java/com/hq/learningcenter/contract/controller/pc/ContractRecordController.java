package com.hq.learningcenter.contract.controller.pc;

import com.hq.learningcenter.contract.service.ContractRecordService;
import com.hq.learningcenter.school.controller.AbstractBaseController;
import com.hq.learningcenter.school.pojo.UserInfoPOJO;
import com.hq.learningcenter.school.web.model.WrappedResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auther linchaokai
 * @description 云合同接口
 * @date 2018/6/5
 */
@Controller
@RequestMapping("/learningCenter/web")
@Api(description = "云合同接口")
public class ContractRecordController extends AbstractBaseController {
    @Autowired
    private ContractRecordService contractRecordService;

    @ApiOperation(value = "获取云协议用户token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/contractRecord/getToken", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getToken(HttpServletRequest request, HttpServletResponse response) {
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        long signerId = contractRecordService.querySignerId(userInfo.getUserId());
        return this.success(contractRecordService.getToken(signerId));
    }
}

