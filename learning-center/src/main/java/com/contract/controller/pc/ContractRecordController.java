package com.contract.controller.pc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.contract.service.ContractRecordService;
import com.school.controller.AbstractBaseController;
import com.school.pojo.UserInfoPOJO;
import com.school.utils.BusinessIdUtils;
import com.school.web.model.WrappedResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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

    @ApiOperation(value = "获取协议未签订数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/contractRecord/getContractUnSignNum", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getContractUnSignNum(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map = new HashMap<String,Object>();
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        String businessId = BusinessIdUtils.getBusinessId(request);
        map.put("userId",userInfo.getUserId());
        map.put("businessId",businessId);
        int result = contractRecordService.getContractSignNum(map);
        Map<String,Object> m = new HashMap<>();
        m.put("count", result);
        return this.success(m);
    }
}

