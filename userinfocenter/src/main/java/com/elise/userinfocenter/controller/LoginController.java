package com.elise.userinfocenter.controller;

import com.elise.userinfocenter.entity.LocalConfigProperties;
import com.elise.userinfocenter.pojo.Token;
import com.hq.common.enumeration.ClientTypeEnum;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;


/**
 * 登陆管理
 *
 * @author Glenn
 */
@Controller
@RequestMapping("/api/")
public class LoginController extends AbstractRestController {

    @Autowired
    private HttpConnManager httpConnManager;

    @Autowired
    private LocalConfigProperties config;

    @ApiOperation(value = "用户登陆v1.2(无需用户订单登录)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobileNo", value = "用户手机号码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "passWord", value = "用户密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "clientType", value = "客户端类型\n(小写ios,android,web)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "versionCode", value = "客户端版本", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "redirectUrl", value = "重定向资源(Web需要传)", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "经度参数", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "latitude", value = "纬度参数", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/loginV1_2", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<Token>> loginV1_2(HttpServletRequest request,HttpServletResponse response) {
        try {
            String mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo", "");
            String passWord = ServletRequestUtils.getStringParameter(request, "passWord", "");
            String clientType = ServletRequestUtils.getStringParameter(request, "clientType", "");
            Integer versionCode = ServletRequestUtils.getIntParameter(request, "versionCode", -1);
            String strLongitude = ServletRequestUtils.getStringParameter(request, "longitude", "");
            String strLatitude = ServletRequestUtils.getStringParameter(request, "latitude", "");
            String redirectUrl = ServletRequestUtils.getStringParameter(request, "redirectUrl", "");
            String schoolId = this.getSchoolId(request);
            if (TRACER.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("\n[Incoming Request] \nmobileNo is ");
                sb.append(mobileNo);
                sb.append("\npassWord is ");
                sb.append(passWord);
                sb.append("\nschoolId is ");
                sb.append(schoolId);
                sb.append("\nclientType is ");
                sb.append(clientType);
                sb.append("\nversionCode is ");
                sb.append(versionCode);
                TRACER.info(sb.toString());
            }
            if ("web".equals(clientType)) {
                if (redirectUrl == null && redirectUrl.equals("")) {
                    return this.error("重定向地址不能为空");
                }
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            if( mobileNo != null) {
                map.put("mobileNo", mobileNo);
            }
            if( passWord != null){
                map.put("passWord", passWord);
            }
            if( passWord != null){
                map.put("clientType", clientType);
            }
            if( versionCode != -1){
                map.put("versionCode", versionCode);
            }
            if( strLongitude != null){
                map.put("strLongitude", strLongitude);
            }
            if( strLatitude != null){
                map.put("strLatitude", strLatitude);
            }
            HttpPlainResult result = httpConnManager.invoke(HttpMethod.POST, config.getSsoHost() + "/inner/login", map, schoolId);
            TRACER.info(result.getResult());
            HttpResultDetail<Token> entry = HttpResultHandler.handle(result,Token.class);
            if(entry.isOK()){
                ClientTypeEnum clientTypeEnum = ClientTypeEnum.convertString2ClientType(clientType);
                if(clientTypeEnum == ClientTypeEnum.WEB){
                    String token = entry.getResult().getToken();

                    Cookie[] cookies = request.getCookies();
                    if(null != cookies) {
                        for (int i = 0; i < cookies.length; i++) {
                            Cookie cookie = cookies[i];
                            if (cookie != null && config.getCookieTokenName().equals(cookie.getName())) {
                                HashMap<String, Object> outMap = new HashMap<>();
                                String outToken = cookie.getValue();
                                if( outToken != null) {
                                    outMap.put("token", outToken);
                                }
                                HttpPlainResult outResult = httpConnManager.invoke(HttpMethod.POST, config.getSsoHost()+"/inner/logout", outMap,schoolId);
                                TRACER.info(outResult.getResult());
                                break;
                            }
                        }
                    }
                    Cookie token_cookie = new Cookie(config.getCookieTokenName(),token);
                    token_cookie.setMaxAge(config.getCookieTokenTimeout());
                    token_cookie.setDomain(request.getHeader("Host").split(":")[0]);
                    response.addCookie(token_cookie);

                    return this.success(redirectUrl,token_cookie);
                }else{
                    return this.success(entry.getResult(), entry.getResponseStatus());
                }
            }else if(entry.isClientError()){
                return this.error(entry.getResponseMessage(), entry.getResponseStatus());
            }else if(entry.isServerError()){
                return this.fail(entry.getResponseMessage(), entry.getResponseStatus());
            }
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }
}