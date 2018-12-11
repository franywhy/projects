package io.renren.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.renren.common.doc.ParamKey;
/**
 * 
 * @class io.renren.utils.WebUtils.java
 * @Description:
 * @author shihongjie
 * @dete 2017年4月10日
 */
public class WebUtils {
	
    /**
     * 数据填充到 request 中
     * @param request
     * @param map
     */
    public static void populate(HttpServletRequest request,Map<String,Object> map){
        for (Map.Entry<String,Object> entry: map.entrySet()){
            request.setAttribute(entry.getKey(),entry.getValue());
        }
    }
    
    /**
     * 查询条件
     * @param request
     * @param map
     * @return
     */
    public static Map<String,Object> queryMap(HttpServletRequest request,Map<String,Object> map){
    	if(null == map){
    		map = new HashMap<>();
    	}
    	map.put(ParamKey.In.SCHOOLID, SchoolIdUtils.getSchoolId(request));
    	return map;
    }
    
    /**
     * 查询条件
     * @param request
     * @return
     */
    public static Map<String,Object> queryMap(HttpServletRequest request){
    	return queryMap(request, null);
    }

	
}
