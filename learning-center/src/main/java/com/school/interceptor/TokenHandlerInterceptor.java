package com.school.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.dao.SysBusinessDao;
import com.school.entity.SysBusinessEntity;
import com.school.utils.BusinessIdUtils;
import com.school.utils.JSONUtil;
import com.school.utils.SSOTokenUtils;
import com.school.utils.http.HttpClientUtil4_3;
import com.school.web.model.WrappedResponse;

public class TokenHandlerInterceptor implements HandlerInterceptor {

	private static String url = "";
	@Value("#{application['pom.sso.tokenExpired.url']}")
	private void setUrl(String url){
		TokenHandlerInterceptor.url = url;
	}
	
	@Autowired
	private SysBusinessDao sysBusinessDao;
	
	private  static ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		boolean result = false;
		WrappedResponse wrappedResponse = null;
		if (request.getRequestURI().contains("tikuTest")){
		    result = true;
		    return result;
        }
		//TOKEN;
		String token = SSOTokenUtils.getToken(request , response);
		//业务线ID
		String businessId = BusinessIdUtils.getBusinessId(request);
		if(!StringUtils.isBlank(token) && !StringUtils.isBlank(businessId)){
			String httpResult = "";
			try{
	        	Map<String , String> headerMap = new HashMap<>();
	        	headerMap.put(BusinessIdUtils.HTTP_HEADER_BUSINESS_ID(), businessId);
	        	httpResult = HttpClientUtil4_3.get(url + "?token=" + token, headerMap);
	        }catch(Exception ex){
	        	wrappedResponse = WrappedResponse.fail("请求SSO验证失败", ex.getMessage());
	        }
			
			if(!httpResult.equals("")){
				Map<String,Object> resultMap = JSONUtil.jsonToMap(httpResult);
				Integer code = (Integer) resultMap.get("code");
				if(code == 200){
					Map<String,Object> data = (Map<String, Object>) resultMap.get("data");
					boolean expired = (boolean) data.get("expired");
					result = !expired;
					
					if(expired){
						wrappedResponse = WrappedResponse.notFound("token已过期");
					}
				}
	        }
		}else{
			wrappedResponse = WrappedResponse.error("业务ID或token缺失");
		}
		
        if (!result) {
        	//若此业务线非app业务线,则重定向到改业务线主页
			if(businessId.indexOf("app") == -1){
				SysBusinessEntity business = sysBusinessDao.queryByBusinessId(businessId);
				if(null != business){
					String homeUrl = business.getHomeUrl();
					if(null != homeUrl && !homeUrl.equals("")){
						response.sendRedirect(homeUrl);
					}
				}
			}
			
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
        }
		return result;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
