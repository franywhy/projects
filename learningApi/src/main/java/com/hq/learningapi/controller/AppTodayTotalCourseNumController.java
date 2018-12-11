package com.hq.learningapi.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.AppTodayTotalCourseNumService;
import com.hq.learningapi.util.SSOTokenUtils;

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
public class AppTodayTotalCourseNumController extends AbstractRestController {
	
	@Autowired
	private AppTodayTotalCourseNumService appTodayTotalCourseNumService;
	
	@ApiOperation(value = "获取用户今日课程总数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start_time", value = "开始日期 yyyy-MM-dd", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "结束日期 yyyy-MM-dd", required = true, dataType = "String", paramType = "query")
            
    })
    @RequestMapping(value = "/totalCourseNum", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> getTodayTotalCourseNum(HttpServletRequest request) {
		try {
			String token = ServletRequestUtils.getStringParameter(request, "token", "");
			String start_time = ServletRequestUtils.getStringParameter(request, "start_time" , "");
			String end_time = ServletRequestUtils.getStringParameter(request, "end_time" , "");
			
			UserInfoPOJO userInfo = SSOTokenUtils.getUserInfo(request, token);
			Long userId = userInfo.getUserId();
			if(null != userId && userId > 0){
				List<Map<String , Object>> resultList = this.appTodayTotalCourseNumService.getTodayTotalCourseNum(userId,start_time,end_time);
				return this.success(resultList);
			}
			return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable t) {
			TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
