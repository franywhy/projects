package com.hq.learningapi.controller;

import javax.servlet.http.HttpServletRequest;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hq.learningapi.service.AppTodayLearningService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * Created by LiuHai on 2018/01/05
 *
 */

@Controller
@RequestMapping("/api")
public class AppTodayLearingController extends AbstractRestController {

	@Autowired
	private AppTodayLearningService appTodayLearningService;
	
	
	/**
     * 获取课后作业地址接口
     * @date 2018年1月10日
     * @param @param request
     */
	@ApiOperation(value = "获取课后作业地址接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "classplanLiveId", value = "课次id", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "phaseId", value = "阶段id", required = true, dataType = "String", paramType = "query"),
//			@ApiImplicitParam(name = "type", value = "练习类型（1:课后作业;2:错题重做）", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "courseFk", value = "课程服务id", required = true, dataType = "String", paramType = "query")})
	@RequestMapping(value = "/todayLearing/getHomework", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> getHomework(HttpServletRequest request) {
		try {
			String token = ServletRequestUtils.getStringParameter(request, "token", "");
			String classplanLiveId = ServletRequestUtils.getStringParameter(request, "classplanLiveId", "");
			String phaseId = ServletRequestUtils.getStringParameter(request, "phaseId", "");
			String type = ServletRequestUtils.getStringParameter(request, "type", "1");
			String courseFk = ServletRequestUtils.getStringParameter(request, "courseFk", "");
			TRACER.info(String.format("\nRequest Token:%s,\nRequest phaseID:%s,\nRequest type:%s,\nRequest courseFk:%s,\nRequest classplanLiveId:%s",token, phaseId, type, courseFk,classplanLiveId));
			
			if("0".equals(phaseId) || StringUtils.isBlank(phaseId)){
				return this.success("");
			}
			String jointUrl = appTodayLearningService.jointURL(token, phaseId, type, courseFk,classplanLiveId,request);
			
			if(StringUtils.isNotBlank(jointUrl)){
				return this.success(jointUrl);
			}
			
			return this.fail("暂时无法做题");
		} catch (Throwable t) {
			TRACER.error("", t);
			return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
