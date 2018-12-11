package com.hq.learningapi.controller;

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
import com.hq.learningapi.pojo.AppMarketCoursePOJO;
import com.hq.learningapi.pojo.UserInfoPOJO;
import com.hq.learningapi.service.AppMarketCourseService;
import com.hq.learningapi.util.BusinessIdUtils;
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
public class AppMarketCourseController extends AbstractRestController {
	@Autowired
	private AppMarketCourseService appMarketCourseService;
	
	@ApiOperation(value = "移动端获取热门课程列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "businessId", value = "业务线id", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/app/marketCourse", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> getAppMarketCourse(HttpServletRequest request) {
		try {
			String businessId = ServletRequestUtils.getStringParameter(request, "businessId", "");
			if(null != businessId){
				List<Long> productList = this.appMarketCourseService.queryProductIdListByBisinessId(businessId);
				List<AppMarketCoursePOJO> courseList = this.appMarketCourseService.queryCourseList(productList);
				Map<String, Object> resultMap = new HashMap<>();
				if(null != courseList && courseList.size() > 0){
					resultMap.put("courseList", courseList);
					return this.success(resultMap);
				}else{
					resultMap.put("courseList", null);
					return this.success(resultMap);
				}
			}
			return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable t) {
			TRACER.error("", t);
            return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "移动端获取4个最热门课程列表")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "businessId", value = "业务线id", required = true, dataType = "String", paramType = "query")
	})
	@RequestMapping(value = "/app/marketCourseMostHot", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> getAppMarketCourseMostHot(HttpServletRequest request) {
		try {
			String businessId = ServletRequestUtils.getStringParameter(request, "businessId", "");
			if(null != businessId){
				List<Long> productList = this.appMarketCourseService.queryProductIdListByBisinessId(businessId);
				List<AppMarketCoursePOJO> courseList = this.appMarketCourseService.queryMostHotCourseList(productList);
				Map<String, Object> resultMap = new HashMap<>();
				if(null != courseList && courseList.size() > 0){
					resultMap.put("courseList", courseList);
					return this.success(resultMap);
				}else{
					resultMap.put("courseList", null);
					return this.success(resultMap);
				}
			}
			return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable t) {
			TRACER.error("", t);
			return this.fail(TransactionStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
