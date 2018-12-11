/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.renren.modules.job.utils;

import io.renren.common.exception.RRException;
import io.renren.common.utils.SpringContextUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 执行定时任务
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.2.0 2016-11-28
 */
public class ScheduleRunnable implements Runnable {
	private Object target;
	private Method method;
	private Map<String,Object> params;
	
	public ScheduleRunnable(String beanName, String methodName, String params) throws NoSuchMethodException, SecurityException {
//		this.target = SpringContextUtils.getBean(beanName);
//		this.params = params;
//
//		if(StringUtils.isNotBlank(params)){
//			this.method = target.getClass().getDeclaredMethod(methodName, String.class);
//		}else{
//			this.method = target.getClass().getDeclaredMethod(methodName);
//		}
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
		if(StringUtils.isNotBlank(params)){
			String[] paramsPair = params.split(";");
			Map<String,Object> paramsMap = new HashMap<String,Object>();
			for(int i=0;i<paramsPair.length;i++){
				String[] paramPair = paramsPair[i].split(":");
				paramsMap.put(paramPair[0], paramPair[1]);
			}
			return paramsMap;
		}
		return null;
	}
	@Override
	public void run() {
		try {
			ReflectionUtils.makeAccessible(method);
			if(params != null){
				method.invoke(target, params);
			}else{
				method.invoke(target);
			}
		}catch (Exception e) {
			throw new RRException("执行定时任务失败", e);
		}
	}

}
