package com.school.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.accountant.util.HttpUtil;
import com.school.accountant.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.school.accountant.common.SystemConfig;
import com.school.accountant.service.UserService;
import com.school.accountant.vo.SSOResult;


/**
 * 拦截器，对于未登录的用户，如果客户端的cookie里有token，自动登录(把token存入session)
 */
public class SecureOperationInterceptor extends HandlerInterceptorAdapter {

    @Value("${sso.tokenExpired.url}")
    private String SSO_TOKENEXPIRED_URL;

    private  static ObjectMapper objectMapper = new ObjectMapper();

    private List<String> excludeUris; // 不拦截的URIs

    public SecureOperationInterceptor() {
        setExcludeUris(new ArrayList<String>());
    }

    /**
     * @return 不拦截的URIs
     */
    public List<String> getExcludeUris() {
        return excludeUris;
    }

    /**
     * @param excludeUris 不拦截的URIs
     */
    public void setExcludeUris(List<String> excludeUris) {
        this.excludeUris = excludeUris;
    }
    
    @Autowired
	private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /*String uri = request.getRequestURI();
        for (String excludeUri : excludeUris) {
            if (uri.equals(excludeUri)) {
                return true;
            }
        }*/
        if (request.getRequestURI().equals("/static/")) {
            return  true;
        }
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute(SystemConfig.SESSION_TOKEN_KEY);
        Cookie[] cookies = request.getCookies();

        if (StringUtils.isBlank(token) && cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie != null && SystemConfig.COOKIE_TOKEN_NAME.equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
            if (StringUtils.isNotBlank(token)) {
                //判断token是否过期
                String httpResult = "";
                try {
                   // System.out.println("sso测试token"+token);
                    String userInfoUrl = SSO_TOKENEXPIRED_URL.replaceAll("@token", token);
                    httpResult = HttpUtil.doGet4Json(userInfoUrl);
                } catch (Exception ex) {
                    //wrappedResponse = WrappedResponse.fail("请求SSO验证失败", ex.getMessage());
                }

                if (!httpResult.equals("")) {
                    Map<String, Object> resultMap = JsonUtil.jsonToMap(httpResult);
                    Integer code = (Integer) resultMap.get("code");
                    if (code == 200) {
                        Map<String, Object> data = (Map<String, Object>) resultMap.get("data");
                        boolean expired = (boolean) data.get("expired");
                        //token过期,进行拦截,清除cookie信息
                        if (expired) {
                            this.removeCookie(request,response);
                        } else {
                            //token有效
                            //获取用户信息操作
                            SSOResult sSOResult = userService.userInfo(token);
                            if (sSOResult != null && StringUtils.isNotBlank(sSOResult.getCode()) && SystemConfig.SSO_SUCCESS_CODE.equals(sSOResult.getCode())) {
                                //证明token有效，把token存入session
                                session.setAttribute(SystemConfig.SESSION_TOKEN_KEY, token);
                            }
                        }
                    }else if (code == 401){
                        this.removeCookie(request,response);
                    }
                }
            }
        /*if (!result) {
            PrintWriter out = null;
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                out = response.getWriter();
                out.append(objectMapper.writeValueAsString(wrappedResponse));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }*/
        return true;
    }
    private void removeCookie(HttpServletRequest request,HttpServletResponse response){
        Cookie cookieNickname = new Cookie(SystemConfig.COOKIE_NICKNAME, null);
        cookieNickname.setPath(SystemConfig.COOKIE_PATH);
        cookieNickname.setDomain(SystemConfig.COOKIE_DOMAIN);
        cookieNickname.setMaxAge(0);
        response.addCookie(cookieNickname);

        Cookie cookieAvatar = new Cookie(SystemConfig.COOKIE_AVATAR, null);
        cookieAvatar.setPath(SystemConfig.COOKIE_PATH);
        cookieAvatar.setDomain(SystemConfig.COOKIE_DOMAIN);
        cookieAvatar.setMaxAge(0);
        response.addCookie(cookieAvatar);

        Cookie cookie = new Cookie(SystemConfig.COOKIE_TOKEN_NAME, null);
        cookie.setPath(SystemConfig.COOKIE_PATH);
        cookie.setDomain(SystemConfig.COOKIE_DOMAIN);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        //与学习中心对接的SSOTOKEN
        Cookie ssoToken = new Cookie(SystemConfig.COOKIE_SSOTOKEN_NAME, null);
        ssoToken.setPath(SystemConfig.COOKIE_PATH);
        ssoToken.setDomain(SystemConfig.COOKIE_DOMAIN);
        ssoToken.setMaxAge(0);
        response.addCookie(ssoToken);
        //result = false;
        // wrappedResponse = WrappedResponse.notFound("登录信息已过期!请重新登录");
    }
}
