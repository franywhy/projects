package com.hq.answerapi.controller;

import com.hq.answerapi.config.props.LocalConfigProperties;
import com.hq.answerapi.pojo.Token;
import com.hq.answerapi.service.UserService;
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
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * 登陆管理
 *
 * @author Glenn
 */
@Controller
@RequestMapping("/api/")
public class LoginController extends AbstractRestController {

    private final static String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,16}$";

    @Autowired
    private HttpConnManager httpConnManager;

    @Autowired
    private LocalConfigProperties config;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "员工端登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobileNo", value = "用户手机号码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "passWord", value = "用户密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "clientType", value = "客户端类型\n(小写ios,android,web)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "versionCode", value = "客户端版本", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "经度参数", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "latitude", value = "纬度参数", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/tLogin", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<Token>> tLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            String mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo", "");
            String passWord = ServletRequestUtils.getStringParameter(request, "passWord", "");
            String clientType = ServletRequestUtils.getStringParameter(request, "clientType", "");
            Integer versionCode = ServletRequestUtils.getIntParameter(request, "versionCode", -1);
            String strLongitude = ServletRequestUtils.getStringParameter(request, "longitude", "");
            String strLatitude = ServletRequestUtils.getStringParameter(request, "latitude", "");
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
            Map<String, Object> map = new HashMap<>();
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
            if(!userService.isTeacher(mobileNo)) {
                TransactionStatus status = TransactionStatus.ANSWER_NOT_PERMISSION_LOGIN_EMPLOYEES_APP;
                return this.entityGenerator(null, status.getReasonPhrase(), status, null);
            }
            HttpPlainResult result = httpConnManager.invoke(HttpMethod.POST, config.getSsoHost() + "/inner/login", map, schoolId);
            TRACER.info(result.getResult());
            HttpResultDetail<Token> entry = HttpResultHandler.handle(result,Token.class);
            if(entry.isOK()){
                //密码规则：6-16个由字母&数字组成的字符，至少一个字母，一个数字
                String passStr = new String(Base64.decodeBase64(passWord), StandardCharsets.UTF_8);
                if(!Pattern.matches(PASSWORD_REGEX, passStr)) {
                    TransactionStatus status = TransactionStatus.ANSWER_PASSWORD_IS_TOO_SIMPLE;
                    return this.entityGenerator(null, status.getReasonPhrase(), status, null);
                }
                String token = entry.getResult().getToken();
                // TODO 密码正确的前提下，必须是内部员工才能登录
                Map<String,Object> mongoMap = userService.mongoCheck(token, passWord, schoolId);
                int code = Integer.parseInt(mongoMap.get("code").toString());
                if(TransactionStatus.OK.value() == code) {
                    return this.success(entry.getResult(), entry.getResponseStatus());
                } else {
                    TransactionStatus status = TransactionStatus.valueOf(code);
                    return this.entityGenerator(null, status.getReasonPhrase(), status, null);
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