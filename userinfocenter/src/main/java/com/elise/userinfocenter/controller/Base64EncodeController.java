package com.elise.userinfocenter.controller;

import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Glenn on 2017/5/18 0018.
 */
@Controller
@RequestMapping("/api/")
public class Base64EncodeController extends AbstractRestController {

    @ApiOperation(value = "Base64混淆接口，内部测试使用")
    @ApiImplicitParam(name = "passWord", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/base64", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<String>> encode(HttpServletRequest request){
        String passWord = ServletRequestUtils.getStringParameter(request, "passWord", "");
        return  this.success(Base64.encodeBase64String(passWord.getBytes()));
    }


}