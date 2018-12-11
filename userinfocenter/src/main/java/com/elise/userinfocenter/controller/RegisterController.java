package com.elise.userinfocenter.controller;


import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.http.HttpConnManager;
import com.hq.http.HttpPlainResult;
import com.hq.http.HttpResultDetail;
import com.hq.http.HttpResultHandler;
import com.elise.userinfocenter.entity.LocalConfigProperties;
import com.elise.userinfocenter.pojo.SchoolId;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Created by Glenn on 2017/4/22 0022.
 */

@Controller
@RequestMapping("/api")
public class RegisterController extends AbstractRestController {

    @Autowired
    private HttpConnManager httpConnManager;

    @Autowired
    private LocalConfigProperties config;

    @ApiOperation(value = "新用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobileNo", value = "新用户手机号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "passWord", value = "新用户密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "otp", value = "短信验证码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "redirectUrl", value = "重定向资源(Web需要传)", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<String>> register(HttpServletRequest request,HttpServletResponse response) {
        try {
            String mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo", null);
            String passWord = ServletRequestUtils.getStringParameter(request, "passWord", null);
            String otp = ServletRequestUtils.getStringParameter(request, "otp", null);
            String schoolId = this.getSchoolId(request);
            String redirectUrl  = null;
            HttpPlainResult schoolIdResult = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost()+"/inner/checkSchoolId", new HashMap<>(),schoolId);
            TRACER.info(schoolIdResult.getResult());
            HttpResultDetail<SchoolId> schoolIdEntry = HttpResultHandler.handle(schoolIdResult,SchoolId.class);
            if(schoolIdEntry.isOK()){
                Boolean isMobile  = schoolIdEntry.getResult().getMobile();
                if(!isMobile){
                    redirectUrl = ServletRequestUtils.getStringParameter(request, "redirectUrl", null);
                    if (StringUtils.isBlank(redirectUrl)) {
                        return this.error("重定向地址不能为空");
                    }
                }
                HashMap<String, Object> map = new HashMap<String, Object>();
                if( mobileNo != null){
                    map.put("mobileNo", mobileNo);
                }
                if( passWord != null){
                    map.put("passWord", passWord);
                }
                if( otp!=null){
                    map.put("otp", otp);
                }
                HttpPlainResult registerResult = httpConnManager.invoke(HttpMethod.POST, config.getSsoHost()+"/inner/register", map,schoolId);
                TRACER.info(registerResult.getResult());
                HttpResultDetail<String> registerEntry = HttpResultHandler.handle(registerResult,String.class);
                if(registerEntry.isOK()){
                    if(!isMobile){
                        return this.success(redirectUrl);
                    }else{
                        return this.success(registerEntry.getResult(), registerEntry.getResponseStatus());
                    }
                }else if(registerEntry.isClientError()){
                    return this.error(registerEntry.getResponseMessage(), registerEntry.getResponseStatus());
                }else {
                    return this.fail(registerEntry.getResponseMessage(), registerEntry.getResponseStatus());
                }
            }else if(schoolIdEntry.isClientError()){
                return this.error(schoolIdEntry.getResponseMessage(), schoolIdEntry.getResponseStatus());
            }else {
                return this.fail(schoolIdEntry.getResponseMessage(), schoolIdEntry.getResponseStatus());
            }
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiOperation(value = "app4.0新用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobileNo", value = "新用户手机号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "passWord", value = "新用户密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "otp", value = "短信验证码", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/register_v410", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<String>> register_v410(HttpServletRequest request,HttpServletResponse response) {
        try {
            String mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo", null);
            String passWord = ServletRequestUtils.getStringParameter(request, "passWord", null);
            String otp = ServletRequestUtils.getStringParameter(request, "otp", null);
            String schoolId = this.getSchoolId(request);
                HashMap<String, Object> map = new HashMap<String, Object>();
                if( mobileNo != null){
                    map.put("mobileNo", mobileNo);
                }
                if( passWord != null){
                    map.put("passWord", passWord);
                }
                if( otp!=null){
                    map.put("otp", otp);
                }
                HttpPlainResult registerResult = httpConnManager.invoke(HttpMethod.POST, config.getSsoHost()+"/inner/register", map,schoolId);
                TRACER.info(registerResult.getResult());
                HttpResultDetail<String> registerEntry = HttpResultHandler.handle(registerResult,String.class);
                if(registerEntry.isOK()){
                        return this.success(registerEntry.getResult(), registerEntry.getResponseStatus());
                }else if(registerEntry.isClientError()){
                    return this.error(registerEntry.getResponseMessage(), registerEntry.getResponseStatus());
                }else {
                    return this.fail(registerEntry.getResponseMessage(), registerEntry.getResponseStatus());
                }
        } catch (Throwable t) {
            TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
        }
    }
}