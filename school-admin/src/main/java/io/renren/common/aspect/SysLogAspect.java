package io.renren.common.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import io.renren.common.doc.SysLog;
import io.renren.entity.SysLogEntity;
import io.renren.entity.SysUserEntity;
import io.renren.service.SysLogService;
import io.renren.utils.HttpContextUtils;
import io.renren.utils.IPUtils;

/**
 * 系统日志，切面处理类
 * @author Liuhai
 *
 */
@Aspect
@Component
public class SysLogAspect {
	@Autowired
	private SysLogService sysLogService;

	@Pointcut("@annotation(io.renren.common.doc.SysLog)")
	public void logPointCut(){
		
	}
	
	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable{
		try {
			long beginTime = System.currentTimeMillis();
			//执行方法
			Object result = point.proceed();
			//执行时长(毫秒)
			long time = System.currentTimeMillis() - beginTime;
			//保存日志
			saveSysLog(point , time);
			
			return result;
		} catch (Exception e) {
			throw e;
//			return null;
		}
	}
	
	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		//获取目标方法签名
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		//获取目标方法
		Method method = signature.getMethod();
		SysLogEntity sysLogEntity = new SysLogEntity();
		//通过反射获取目标方法注解
		SysLog syslog = method.getAnnotation(SysLog.class);
		if(syslog != null){
			//获取目标方法注解上的描述
			String description = syslog.value();
			//设置用户操作
			sysLogEntity.setOperation(description);
		}
		
		//获取目标方法类名
		String className = joinPoint.getTarget().getClass().getName();
		//获取目标方法名
		String methodName = signature.getName();
		//设置请求方法
		sysLogEntity.setMethod(className + "." + methodName + "()");
		
		//获取请求的参数
		Object[] args = joinPoint.getArgs();
		
		try {
			if(
					null != args[0] 
					&& !(args[0] instanceof javax.servlet.http.HttpServletRequest) 
					&& !(args[0] instanceof javax.servlet.http.HttpServletResponse)){
				String params = new Gson().toJson(args[0]);
				//设置请求参数
				sysLogEntity.setParams(params);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		//获取当前request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		//设置请求的IP地址
		sysLogEntity.setIp(IPUtils.getIpAddr(request));
		
		//获取当前用户名
		String username = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUsername();
		//设置当前用户名
		sysLogEntity.setUsername(username);
		//设置执行时长
		sysLogEntity.setExecutionTime(time);
		//设置创建时间
		sysLogEntity.setCreateTime(new Date());
		
		try {
			//保存系统日志
			sysLogService.save(sysLogEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
