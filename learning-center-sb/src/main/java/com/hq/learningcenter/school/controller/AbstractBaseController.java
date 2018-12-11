package com.hq.learningcenter.school.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hq.learningcenter.utils.BusinessIdUtils;
import com.hq.learningcenter.utils.EncryptionUtils;
import com.hq.learningcenter.utils.SSOTokenUtils;
import com.hq.learningcenter.utils.UserAgentDetector;
import com.hq.learningcenter.school.entity.SysBusinessEntity;
import com.hq.learningcenter.school.pojo.LcMenuPOJO;
import com.hq.learningcenter.school.pojo.UserInfoPOJO;
import com.hq.learningcenter.school.service.BusinessDiscoverer;
import com.hq.learningcenter.school.service.LcMenuService;
import com.hq.learningcenter.school.service.MyCourseService;
import com.hq.learningcenter.school.web.csrf.CsrfTokenManager;
import com.hq.learningcenter.school.web.model.WrappedResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

/**
 * 控制器的抽象基类。
 * @author XingNing OU
 */
public abstract class AbstractBaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 分析显示列表时每页显示的条目数。
     */
    protected static final int PAGE_SIZE = 5;
    
    /**
     * 静态资源的URL前缀，默认为"//static.live.videocc.net"
     */
    protected static final String ASSETS_URL_PREFIX = "";
    
    protected static final String SUCCESS = "success";
    
    /** 题库地址 */
    protected static String tikuUrl = "http://zktktest.hqjy.com";
    @Value("")
    private void setTiKuUrl(String url){
    	if(StringUtils.isNotBlank(url)){
    		tikuUrl = url;
    	}
    }

    @Autowired
    protected CsrfTokenManager csrfTokenManager;

    @Autowired
    private BusinessDiscoverer businessDiscoverer;
    
    @Autowired
    private LcMenuService lcMenuService;
    
    @Autowired
	private MyCourseService myCourseService;

    @Resource
    protected StringRedisTemplate mainRedis;


    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     * @param time  过期时间 单位分钟
     * @return 缓存的对象
     */
    public <T> ListOperations<String, T> setCacheList(String key, List<T> dataList, long time) {
        ListOperations listOperation = mainRedis.opsForList();
        if (null != dataList) {
            int size = dataList.size();
            for (int i = 0; i < size; i++) {

                listOperation.rightPush(key, dataList.get(i).toString());
            }

        }

        mainRedis.expire(key, time, TimeUnit.MINUTES);
        return listOperation;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(String key) {
        List<T> dataList = new ArrayList<T>();
        ListOperations<String, T> listOperation = (ListOperations<String, T>) mainRedis.opsForList();
        Long size = listOperation.size(key);
        for (int i = 0; i < size; i++) {
            dataList.add((T) listOperation.leftPop(key));
        }
        return dataList;
    }





    /**
     * 获取左侧菜单
     * @param businessId
     * @return
     */
    protected List<LcMenuPOJO> getLcMenu(String businessId, String username){
		List<LcMenuPOJO> queryLcMenu = lcMenuService.queryLcMenu(businessId);
		for(LcMenuPOJO pojo : queryLcMenu){
			if(pojo.getName().equals("实训软件")){
				
				pojo.setUrl("http://app.sso.hqjy.com/redirect_to_other_4old_pc?username="+username+"&rd_url=http://kuaijiold.hqjy.com/UserManage/shixun.html");
			}
		}
		return queryLcMenu;
    }
    
    /**
     * 从SSO获取用户信息
     * @param request
     * @return
     */
	protected UserInfoPOJO getUserInfo(HttpServletRequest request, HttpServletResponse response) {
    	String token = SSOTokenUtils.getToken(request, response);
    	return getUserInfoToken(request, token);
    }

    protected UserInfoPOJO getUserInfoToken(HttpServletRequest request, String token) {
    	
        return SSOTokenUtils.getUserInfo(request, token);
    }

    /**
     * 获取businessId参数的值，如果无法获取则返回<code>null</code>.
     * 
     * @param request 当前HttpServletRequest对象
     * @return businessId参数的值，如果无法获取则返回<code>null</code>
     */
    protected String getBusinessId(HttpServletRequest request) {
        return businessDiscoverer.getBusinessId(request);
    }

    /**
     * 预防CSRF攻击的隐藏字段内容。
     */
    protected String getCsrfHiddenHtml(HttpServletRequest request, HttpServletResponse response) {
        String csrfTokenName = csrfTokenManager.getTokenName();
        String csrfTokenValue = csrfTokenManager.createCsrfToken(request, response);

        String hiddenFieldFormat = "<input type='hidden' id='%s' name='%s' value='%s' />";
        return String.format(hiddenFieldFormat, csrfTokenName, csrfTokenName, csrfTokenValue);
    }

    /**
     * 从Session中读取内容。
     */
    protected Object getFromSession(HttpServletRequest request, String schoolId, String name) {
        String attrName = schoolId + ":" + name;
//        Object o=request.getSession().getAttribute(attrName);
        return request.getSession().getAttribute(attrName);
    }

    /**
     * 从Session中删除内容。
     */
    protected void deleteFromSession(HttpServletRequest request, String schoolId, String name) {
        String attrName = schoolId + ":" + name;
        request.getSession().removeAttribute(attrName);
    }

    /**
     * 把内容保存在Session中。
     */
    protected void setToSession(HttpServletRequest request, String name, String schoolId, Object value) {
        String attrName = schoolId + ":" + name;
        request.getSession().setAttribute(attrName, value);
    }

    /**
     * 获取客户端的IP地址。
     */
    protected String getIPAddress(HttpServletRequest request) {
        String ipv4 = request.getHeader("X-Real-IP");
        if ((null != ipv4) && (ipv4.length() > 0)) {
            return ipv4;
        }
        return request.getRemoteAddr();
    }

    /**
     * 创建一个ModelAndView对象，对象中包含了当前登录用户的数据。
     * @param request 当前HttpServletRequest对象
     */
    protected ModelAndView createModelAndView(HttpServletRequest request, HttpServletResponse response) {
        return this.createModelAndView(request, response, false);
    }

    /**
     * 创建一个ModelAndView对象，对象中包含了当前登录用户的数据。
     * @param req 当前HttpServletRequest对象
     * @param res 当前HttpServletResponse对象
     * @param csrf 是否预防CSRF攻击
     */
    protected ModelAndView createModelAndView(HttpServletRequest req, HttpServletResponse res, boolean csrf) {

    	
    	
        ModelAndView mav = new ModelAndView();
        String businessId = BusinessIdUtils.getBusinessId(req);
        mav.addObject("businessId", businessId);
        mav.addObject("userInfo", this.getUserInfo(req,res));
        String encode = "";
        try {
			encode = URLEncoder.encode(EncryptionUtils.AESencrypt(getUserInfo(req, res).getMobileNo(), "06k9bCEpUAKeL9I3"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        mav.addObject("lcMenu", this.getLcMenu(businessId,encode));
        //mav.addObject("encode", new EncodeURLMethod());
        
        SysBusinessEntity business = myCourseService.getSysBusiness(businessId);
        if(null != business && null != business.getHomeUrl()){
        	mav.addObject("homeUrl", business.getHomeUrl());
        }
        
        if (csrf) {
            mav.addObject("csrfHiddenHtml", this.getCsrfHiddenHtml(req, res)); // 增加预防CSRF攻击的隐藏代码
        }
        /*if (null == this.getCurrentUser(req)) {
            try {
                String url = req.getRequestURI() + "?" + req.getQueryString();
                url = URLEncoder.encode(url, "UTF-8");
                mav.addObject("redirectUrl", url);
            } catch (Exception e) {
                // ignore
            }
        }*/

        return mav;
    }

    /**
     * 处理成功的HTTP响应内容。
     * @param data 响应结果
     */
    protected ResponseEntity<WrappedResponse> success(Object data) {
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        WrappedResponse resp = WrappedResponse.success(data);
        return new ResponseEntity<WrappedResponse>(resp, headers, HttpStatus.OK);
    }
    /**
     * 处理成功的HTTP响应内容。
     * @param data 响应结果
     */
    protected ResponseEntity<WrappedResponse> success() {
    	return success(SUCCESS);
    }

    /**
     * 处理失败，抛出异常。
     * @param message 失败代码或提示
     * @param cause 导致失败的异常或原因
     */
    protected ResponseEntity<WrappedResponse> fail(String message, Object cause) {
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        WrappedResponse resp = WrappedResponse.fail(message, cause);
        return new ResponseEntity<WrappedResponse>(resp, headers, HttpStatus.OK);
    }

    /**
     * 发生错误，由用户提交的参数非法而引起。
     * @param message 错误代码或提示
     */
    protected ResponseEntity<WrappedResponse> error(String message) {
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        WrappedResponse resp = WrappedResponse.error(message);
        return new ResponseEntity<WrappedResponse>(resp, headers, HttpStatus.OK);
    }

    /**
     * 发生错误，由用户提交的参数非法而引起。
     * @param message 错误代码或提示
     * @param cause 错误原因
     */
    protected ResponseEntity<WrappedResponse> error(String message, Object cause) {
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        WrappedResponse resp = WrappedResponse.error(message, cause);
        return new ResponseEntity<WrappedResponse>(resp, headers, HttpStatus.OK);
    }

    /**
     * 区分新旧版,如果是旧版返回头此提示
     * @param message 错误代码或提示
     * @param cause 错误原因
     */
    protected ResponseEntity<WrappedResponse> updateVersion(String message, Object cause) {
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        WrappedResponse resp = WrappedResponse.updateVersion(message, cause);
        return new ResponseEntity<WrappedResponse>(resp, headers, HttpStatus.OK);
    }

    /**
     * jQuery-validation插件验证的响应结果。
     * @param isvalid 用户输入的是否合法
     * @param message 不合法时展示的错误码或错误信息
     */
    protected ResponseEntity<Map<String, Object>> jqueryValidate(boolean isvalid, String message) {
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", isvalid);
        result.put("message", message);

        return new ResponseEntity<Map<String, Object>>(result, headers, HttpStatus.OK);
    }

    /**
     * 判断访问终端是否为移动端。
     */
    protected boolean isMobile(HttpServletRequest request) {
        return UserAgentDetector.isMobile(request.getHeader("User-Agent"));
    }

    /**
     * 判断访问终端是否为微信浏览器。
     */
    protected boolean isWeixin(HttpServletRequest request) {
        return UserAgentDetector.isWeixin(request.getHeader("User-Agent"));
    }

    /**
     * 判断是否支持微信支付。
     */
    protected boolean isWeixinPay(HttpServletRequest request) {
        String version = UserAgentDetector.getWeixinVersion(request.getHeader("User-Agent"));

        int versionNumber = 0;
        try {
            int dotIndex = version.indexOf(".");
            if (dotIndex > 0) {
                versionNumber = Integer.parseInt(version.substring(0, dotIndex));
            } else {
                versionNumber = Integer.parseInt(version);
            }
        } catch (NumberFormatException nfe) {
            // ignore
        }

        return (versionNumber >= 5); // 5.0以上的微信版本才支持微信支付
    }

    protected boolean isMobileOrIPad(HttpServletRequest request) {
        return UserAgentDetector.isMobile(request.getHeader("User-Agent"))
            || UserAgentDetector.isIpad(request.getHeader("User-Agent"));
    }

    /**
     * 自定义处理成功的HTTP响应内容。
     * @param data 响应结果
     * @param param 自定义MAP参数
     */
    protected ResponseEntity<Object> success(Object data , Map<Object,Object> param) {
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        Map<Object,Object> resp = WrappedResponse.success(data,param);
        return new ResponseEntity<Object>(resp, headers, HttpStatus.OK);
    }
    
}
