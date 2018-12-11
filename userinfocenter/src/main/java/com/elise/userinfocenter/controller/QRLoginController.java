package com.elise.userinfocenter.controller;


import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import com.hq.http.HttpConnManager;
import com.hq.http.HttpPlainResult;
import com.hq.http.HttpResultDetail;
import com.hq.http.HttpResultHandler;
import com.elise.userinfocenter.entity.LocalConfigProperties;
import com.elise.userinfocenter.pojo.Token;
import com.elise.userinfocenter.pojo.UUIDPOJO;
import com.elise.userinfocenter.qrcode.QuickResCodeGenerator;
import com.elise.userinfocenter.service.impl.QuickResLoginServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.UUID;

@Controller
@RequestMapping("/api/qrLogin")
public class QRLoginController extends AbstractRestController {

    @Autowired
    private QuickResCodeGenerator generator;

    @Autowired
    private LocalConfigProperties config;

    @Autowired
    private QuickResLoginServiceImpl loginService;

    @Autowired
    private HttpConnManager httpConnManager;

    @ApiOperation(value = "通过UUID获取QR Code")
    @RequestMapping(value = "/qrCode/{uuid}", method = RequestMethod.GET)
    public void getQuickResponseCode(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            StringBuilder quickResMsgBuilder = new StringBuilder();
            String schoolId = this.getSchoolId(request);
            if (loginService.loadUrl(uuid, schoolId) == null) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "二维码超时");
                return;
            }
            quickResMsgBuilder.append(config.getQrCodeUrl());
            quickResMsgBuilder.append(uuid);
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setContentType("image/jpeg");
            ServletOutputStream out = response.getOutputStream();
            generator.writeToStream("jpg", quickResMsgBuilder.toString(), out);
            try {
                out.flush();
            } finally {
                out.close();
            }
        } catch (Throwable t) {
            TRACER.error("", t);
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "生成二维码失败");
        }
    }

    @ApiOperation(value = "获取二维码登录所需的UUID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "redirectUrl", value = "重定向资源(请求URL建议去掉token)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "._", value = "Current TimeStamp", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/uuid", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<UUIDPOJO>> getLoginUUID(HttpServletRequest request) {
        try {
            String redirectUrl = ServletRequestUtils.getStringParameter(request, "redirectUrl", "");
            Long timeStamp = ServletRequestUtils.getLongParameter(request, "._", -1);
            String schoolId = this.getSchoolId(request);
            if (StringUtils.isBlank(redirectUrl)) {
                return this.error("跳转地址为null");
            }
            ByteBuffer byteWrapper = ByteBuffer.allocate(16);
            UUID uuid = UUID.randomUUID();
            byteWrapper.putLong(uuid.getMostSignificantBits());
            byteWrapper.putLong(uuid.getLeastSignificantBits());
            String uuidStr = Base64.encodeBase64String(byteWrapper.array());
            loginService.saveUUID(uuidStr, redirectUrl, schoolId);
            UUIDPOJO pojo = new UUIDPOJO();
            pojo.setUuid(uuidStr);
            return this.success(pojo);
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "客户端确认登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "客户端token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "uuid", value = "扫码得到uuid", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<String>> confirmLogin(HttpServletRequest request) {
        try {
            String token = ServletRequestUtils.getStringParameter(request, "token", "");
            String uuid = ServletRequestUtils.getStringParameter(request, "uuid", "");
            String schoolId = this.getSchoolId(request);
            if (TRACER.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("\n[Incoming Request] \nToken is ");
                sb.append(token);
                sb.append("\nuuid is");
                sb.append(uuid);
                TRACER.info(sb.toString());
            }
            if (StringUtils.isBlank(uuid)) {
                return this.error("缺少登录uuid");
            }
            Boolean b = loginService.saveToken(uuid, token, schoolId);
            return b ? this.success() : this.error("二维码过期，请重新获取");
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "Web登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "从服务器获得的uuid", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "._", value = "Current TimeStamp", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/jsLogin", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse<Token>> jsParallelLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            String uuid = ServletRequestUtils.getStringParameter(request, "uuid", "");
            Long timeStamp = ServletRequestUtils.getLongParameter(request, "._", -1);
            String schoolId = this.getSchoolId(request);
            if(StringUtils.isBlank(uuid)){
                return this.error("uuid 不可为空");
            }
            String token = loginService.loadToken(uuid,schoolId);
            if(token == null){
                return this.error("未获取确认登录信息",TransactionStatus.USER_TOKEN_NOT_FOUND);
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("token",token);
            HttpPlainResult result = httpConnManager.invoke(HttpMethod.GET, config.getSsoHost() + "/inner/webToken", map, schoolId);
            TRACER.info(result.getResult());
            HttpResultDetail<Token> entry = HttpResultHandler.handle(result,Token.class);
            if(entry.isOK()){
                StringBuilder redirectUrl = new StringBuilder();
                redirectUrl.append(loginService.loadUrl(uuid,schoolId));
                redirectUrl.append("&token=");
                redirectUrl.append(entry.getResult().getToken());
                Cookie cookie = new Cookie("token",token);
                response.addCookie(cookie);
                response.sendRedirect(redirectUrl.toString());
                loginService.clear(uuid,schoolId);
                return null;
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
