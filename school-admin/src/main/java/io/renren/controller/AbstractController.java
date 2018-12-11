package io.renren.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import io.renren.common.doc.ParamKey;
import io.renren.entity.SysUserEntity;
import io.renren.utils.ShiroUtils;
import io.renren.utils.WebUtils;

/**
 * Controller公共组件
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月9日 下午9:42:26
 */
public abstract class AbstractController{
    @Resource
    public StringRedisTemplate mainRedis;
	
    @Resource
    public StringRedisTemplate schoolLearningRedis;
    
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected SysUserEntity getUser() {
		return ShiroUtils.getUserEntity();
	}

	/**
	 * 用户ID
	 */
	protected Long getUserId() {
		SysUserEntity sysUserEntity = getUser();
		if(null != sysUserEntity){
			return sysUserEntity.getUserId();
		}
		return null;
//		return getUser().getUserId();
		
	}
	
	/**
	 * 是否是教学老师
	 */
	protected boolean isTer(){
		boolean defa = false;
		defa = getUser().getTeacher() == 1;
		return defa;
	}
	
	/**
	 * 是否是班主任
	 */
	protected boolean isClassTer(){
		boolean defa = false;
		defa = getUser().getClassTeacher() == 1;
		return defa;
	}
	
	/**
	 * 查询条件
	 * @param request
	 * @return 平台ID
	 */
	public Map<String , Object> getMap(HttpServletRequest request){
		Map<String, Object> queryMap = WebUtils.queryMap(request);
		return WebUtils.queryMap(request , queryMap);
	}
	
	/**
	 * 分页查询条件
	 * @param request
	 * @return 平台ID 可能有分页参数(一定有分页 最大条数 @see ParamKey.In.MAX_LIMIT)
	 */
	public Map<String , Object> getMapPage(HttpServletRequest request){
		Map<String , Object> map = getMap(request);
		pageQuery(map, request, ParamKey.In.DEFAULT_MAX_LIMIT);
		return map;
	}
	
	/**
	 * 查询条件
	 * @param request
	 * @param defaultPage	默认页数
	 * @param defaultLimit	默认条数
	 * @return 平台ID 一定带分页参数
	 */
	public Map<String , Object> getMapPage(HttpServletRequest request , Integer defaultLimit){
		Map<String , Object> map = getMap(request);
		pageQuery(map, request, ParamKey.In.DEFAULT_LIMIT);
		return map;
	}
	
	public boolean pageQuery(Map<String, Object> map, HttpServletRequest request , Integer defaultVal) {
		try {
			if(null == defaultVal){
				defaultVal =  ParamKey.In.DEFAULT_MAX_LIMIT;
			}
			Integer page = ServletRequestUtils.getIntParameter(request,  ParamKey.In.PAGE, 1);
			Integer limit = ServletRequestUtils.getIntParameter(request,  ParamKey.In.LIMIT, defaultVal);
			
			if(null == map){
				map = new HashMap<>();
			}
			//分页最大值
			if(limit > ParamKey.In.PAGE_MAX_LIMIT){
				limit = ParamKey.In.PAGE_MAX_LIMIT;
			}
//			map.put(ParamKey.In.PAGE, page);
			map.put(ParamKey.In.LIMIT, limit);
			map.put(ParamKey.In.OFFSET, (page - 1) * limit);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean stringQueryDecodeUTF8(Map<String, Object> map, HttpServletRequest request, String field ) {
		String str = ServletRequestUtils.getStringParameter(request, field, null);
		if (StringUtils.isNotBlank(str)) {
			if(null == map){
				map = new HashMap<>();
			}
			try {
				str = URLDecoder.decode(str,"UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			map.put(field, str.trim());
			return true;
		}
		return false;
	}

	/**
	 * map.put(field , request.getParameter(field))
	 * @param map			查询条件
	 * @param request		request
	 * @param field			key
	 * @return
	 */
	public boolean stringQuery(Map<String, Object> map, HttpServletRequest request, String field ) {
		return stringQuery(map, request, field , null);
	}
	/**
	 * map.put(field , request.getParameter(field))
	 * @param map			查询条件
	 * @param request		request
	 * @param field			key
	 * @param defaultVal 	默认值
	 * @return
	 */
	public boolean stringQuery(Map<String, Object> map, HttpServletRequest request, String field , String defaultVal) {
		String str = ServletRequestUtils.getStringParameter(request, field, defaultVal);
		if (StringUtils.isNotBlank(str)) {
			if(null == map){
				map = new HashMap<>();
			}
			map.put(field, str.trim());
			return true;
		}
		return false;
	}
	

	
	public boolean intQuery(Map<String, Object> map, HttpServletRequest request, String field) {
		try {
			String val = ServletRequestUtils.getStringParameter(request, field);
			if (StringUtils.isNotBlank(val)) {
				if(null == map){
					map = new HashMap<>();
				}
				map.put(field, Integer.valueOf(val));
				return true;
			}
		} catch (ServletRequestBindingException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean intQuery(Map<String, Object> map, HttpServletRequest request, String field , Integer defaultVal) {
		Integer val = ServletRequestUtils.getIntParameter(request, field, defaultVal);
		if (null != val) {
			if(null == map){
				map = new HashMap<>();
			}
			map.put(field, val);
			return true;
		}
		return false;
	}
	public boolean longQuery(Map<String, Object> map, HttpServletRequest request, String field) {
		try {
			String val = ServletRequestUtils.getStringParameter(request, field);
			if (StringUtils.isNotBlank(val)) {
				if(null == map){
					map = new HashMap<>();
				}
				map.put(field, Long.valueOf(val));
				return true;
			}
		} catch (ServletRequestBindingException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean longQuery(Map<String, Object> map, HttpServletRequest request, String field , Long defaultVal) {
		Long val = ServletRequestUtils.getLongParameter(request, field, defaultVal);
		if (null != val) {
			if(null == map){
				map = new HashMap<>();
			}
			map.put(field, val);
			return true;
		}
		return false;
	}
	
	
	 
}
