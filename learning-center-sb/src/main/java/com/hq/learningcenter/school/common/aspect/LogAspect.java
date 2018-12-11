package com.hq.learningcenter.school.common.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hq.learningcenter.utils.BusinessIdUtils;
import com.hq.learningcenter.utils.HttpContextUtils;
import com.hq.learningcenter.utils.IPUtils;
import com.hq.learningcenter.utils.SSOTokenUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hq.learningcenter.school.common.annotation.LcLog;
import com.hq.learningcenter.school.entity.LcLogEntity;
import com.hq.learningcenter.school.service.LcLogService;

/**
 * 系统日志，切面处理类
 *
 */
@Aspect
@Component
public class LogAspect {
	@Autowired
	private LcLogService lcLogService;

	@Pointcut("@annotation(com.hq.learningcenter.school.common.annotation.LcLog)")
	public void logPointCut(){
		
	}
	
	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable{
		try {
			Object result = point.proceed();
			//保存日志
			saveLog(point);
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	private void saveLog(ProceedingJoinPoint joinPoint) {
		//获取目标方法签名
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		//获取目标方法
		Method method = signature.getMethod();
		LcLogEntity lcLogEntity = new LcLogEntity();
		//通过反射获取目标方法注解
		LcLog log = method.getAnnotation(LcLog.class);
		if(log != null){
			//获取目标方法注解上的描述
			String description = log.value();
			Integer type = log.type().getIndex();
			//设置用户操作
			lcLogEntity.setOperation(description);
			lcLogEntity.setType(type);
		}
		
		//获取目标方法类名
		String className = joinPoint.getTarget().getClass().getName();
		//获取目标方法名
		String methodName = signature.getName();
		//设置请求方法
		lcLogEntity.setMethod(className + "." + methodName + "()");
		//获取请求的参数
		Object[] args = joinPoint.getArgs();
		if(null != args && args[0].getClass() == String.class){
			lcLogEntity.setData(args[0].toString());
		}
		//获取当前request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		HttpServletResponse response = HttpContextUtils.getHttpServletResponse();
		//设置请求的IP地址
		lcLogEntity.setIp(IPUtils.getIpAddr(request));
		
		//获取当前用户名
		Long userId = SSOTokenUtils.getUserInfo(request, SSOTokenUtils.getToken(request, response)).getUserId();
		//设置当前用户名
		lcLogEntity.setUserId(userId);
		//设置业务id
		lcLogEntity.setBusinessId(BusinessIdUtils.getBusinessId(request));
		//设置创建时间
		lcLogEntity.setCreateTime(new Date());
		
		try {
			//保存系统日志
			lcLogService.save(lcLogEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
