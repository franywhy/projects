package io.renren.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

/**
 * 执行定时任务
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月30日 下午12:49:33
 */
public class ScheduleRunnable implements Runnable {
	private Object target;
	private Method method;
	//private params;
	private Map<String,Object> params;
	
	public ScheduleRunnable(String beanName, String methodName,String params) throws NoSuchMethodException, SecurityException {
		this.target = SpringContextUtils.getBean(beanName);
		//讲string参数转换为map
		Map<String,Object> paramsMap = params2Map(params);
		//if(StringUtils.isNotBlank(params)){
		if(null != paramsMap && paramsMap.size()>0){
			this.params = paramsMap;
			this.method = target.getClass().getDeclaredMethod(methodName, Map.class);
		}else{
			this.method = target.getClass().getDeclaredMethod(methodName);
		}
	}
	
	private Map<String,Object> params2Map(String params){
		String[] paramsPair = params.split(";");
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		for(int i=0;i<paramsPair.length;i++){
			String[] paramPair = paramsPair[i].split(":");
			paramsMap.put(paramPair[0], paramPair[1]);
		}
		return paramsMap;
	}

	@Override
	public void run() {
		try {
			ReflectionUtils.makeAccessible(method);
			if(null != params){
				method.invoke(target, params);
			}else{
				method.invoke(target);
			}
		}catch (Exception e) {
			throw new RRException("执行定时任务失败", e);
		}
	}

}
