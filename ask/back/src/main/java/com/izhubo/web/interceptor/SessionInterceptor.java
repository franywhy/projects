package com.izhubo.web.interceptor;

import com.izhubo.rest.common.doc.ParamKey;
import com.izhubo.rest.web.data.SimpleJsonView;
import com.izhubo.admin.Web;

import groovy.transform.CompileStatic;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Oauth2 认证登录
 *
 * date: 12-8-24 下午1:52
 *
 * @author: wubinjie@ak.cc
 */
@CompileStatic
public class SessionInterceptor extends HandlerInterceptorAdapter implements com.izhubo.rest.web.spring.SessionInterceptor{

	private static Logger logger = LoggerFactory.getLogger(SessionInterceptor.class) ;
	
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        sessionHolder.remove();
    }


    private static final ThreadLocal<Map<String,Object>> sessionHolder = new ThreadLocal<Map<String,Object>>();
    public Map<String,Object> getSession(){
        return sessionHolder.get();
    }





    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ServletException, IOException {
        String uri = request.getRequestURI();

		if (uri.endsWith("show.xhtml") || uri.endsWith("courseware/addBatch.json")|| uri.endsWith("schooluserreport/readdatalist.json")||uri.endsWith("teacherorderreport/teacherOrderDayTime.json")||uri.endsWith("topicbunusreport/topicBunusReportDay.json")||uri.endsWith("message/send.json")||uri.endsWith("middlelivedetail/camidrecord.json")){ // 读取允许
            return true;
        }

        Map user = (Map) request.getSession().getAttribute("user");
        if( null == user || user.isEmpty()){
            handleNotAuthorized(request, response, notAuthorized);
            return false;
        }

        //{sys:2,stat/finalog:0}

        if(! checkMenu((Map) request.getSession().getAttribute("menus") , uri.substring(1,uri.indexOf('.')) ) ){
            handleNotAuthorized(request, response, notAllowed);
            return false;
        }
        sessionHolder.set(user);
        return true;
    }

    final String LIST_ALLOW = "1";
    final String ALL_ALLOW = "2";
    final String AUDIT = "3";

    boolean checkMenu(Map<String,String> roles,String method){
    	logger.info("method: {}", method);
    	
        if (LIST_ALLOW.equals(roles.get(method))){
            return true;
        }
        String controller = method.substring(0,method.indexOf('/'));
        String value = roles.get(controller);
        if(null == value){
            return false;
        }
        if(value.equals(ALL_ALLOW)){
            return true;
        }
       String str=method.substring(method.indexOf('/'),method.length());
      // if(value.equals(LIST_ALLOW) && method.endsWith("/list")){
        if(value.equals(LIST_ALLOW)){
            return true;
        }
        if(value.equals(AUDIT)){//++
        	return true;
        }
        return false;
    }


    final String notAuthorized  = "{\"code\":30405,\"msg\":\"ACCESS_TOKEN无效\"}";
    final String notAllowed  = "{\"code\":30418,\"msg\":\"权限不足\"}";


    protected void handleNotAuthorized(HttpServletRequest request, HttpServletResponse response, String json)
            throws ServletException, IOException {
        String callback = request.getParameter(ParamKey.In.callback);
        if(StringUtils.isNotBlank(callback)){
            json = callback+'('+json+')';
        }
        SimpleJsonView.rennderJson(json, response);
    }


}
