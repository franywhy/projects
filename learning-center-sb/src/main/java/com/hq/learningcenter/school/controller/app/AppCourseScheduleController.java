package com.hq.learningcenter.school.controller.app;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hq.learningcenter.school.controller.AbstractBaseController;
import com.hq.learningcenter.school.pojo.UserInfoPOJO;
import com.hq.learningcenter.school.web.model.WrappedResponse;
import com.hq.learningcenter.utils.BusinessIdUtils;
import com.hq.learningcenter.utils.ClientEnum;
import com.hq.learningcenter.school.pojo.ClassplanLivesPOJO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hq.learningcenter.school.pojo.ClassplanPOJO;
import com.hq.learningcenter.school.service.LiveCourseService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/learningCenter/app")
public class AppCourseScheduleController extends AbstractBaseController {
	
	@Autowired
	private LiveCourseService liveCourseService;
	
	@ApiOperation(value = "获取直播课程表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userplanId", value = "学员规划id", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/getSchedule", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getCourseList(HttpServletRequest request, HttpServletResponse response) {
		
		Long startTime = System.currentTimeMillis();
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        
        String businessId = BusinessIdUtils.getBusinessId(request);
        
        String userplanIdStr = request.getParameter("userplanId");
        if(null == userplanIdStr) return this.error("参数提交有误:userplanId");
        
        Long userplanId = Long.parseLong(userplanIdStr);
        
        List<ClassplanPOJO> result = liveCourseService.getLiveSchedule(userInfo.getUserId(), userplanId, businessId, ClientEnum.APP);
        
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/app/getSchedule " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/app/getSchedule " + (endTime - startTime) + "ms");
        }
        
        if(null != result){
        	return this.success(result);
        }
        return this.fail("服务器内部错误", null);
    }

	@ApiOperation(value = "获取排课直播课程表_v410(父集)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userplanId", value = "学员规划id", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/getClassPlanSchedule", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getClassPlanSchedule(HttpServletRequest request, HttpServletResponse response) {
		
		Long startTime = System.currentTimeMillis();
        UserInfoPOJO userInfo = this.getUserInfo(request,response);

        String businessId = BusinessIdUtils.getBusinessId(request);

        String userplanIdStr = request.getParameter("userplanId");
        if(null == userplanIdStr) return this.error("参数提交有误:userplanId");

        Long userplanId = Long.parseLong(userplanIdStr);

        List<ClassplanPOJO> result = liveCourseService.getClassPlanSchedule(userInfo.getUserId(), userplanId, businessId, ClientEnum.APP);
        
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/app/getClassPlanSchedule " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/app/getClassPlanSchedule " + (endTime - startTime) + "ms");
        }
        
        if(null != result){
        	return this.success(result);
        }
        return this.fail("服务器内部错误", null);
    }
	@ApiOperation(value = "获取课次直播课程表_v410(子集)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "classplanId", value = "学员规划id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "classtypeId", value = "班型id", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/getClassPlanDetailSchedule", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getClassPlanDetailSchedule(HttpServletRequest request, HttpServletResponse response) {
        
		Long startTime = System.currentTimeMillis();
		
		UserInfoPOJO userInfo = this.getUserInfo(request,response);

        String businessId = BusinessIdUtils.getBusinessId(request);

        String classplanId = request.getParameter("classplanId");
        if(null == classplanId) return this.error("参数提交有误:classplanId");
        String classtypeIdStr = request.getParameter("classtypeId");
        if(null == classtypeIdStr) return this.error("参数提交有误:classtypeId");

        Long classtypeId = Long.parseLong(classtypeIdStr);

        List<ClassplanLivesPOJO> result = liveCourseService.getClassPlanDetailSchedule( userInfo.getUserId(),classplanId,classtypeId, businessId, ClientEnum.APP);
        
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/app/getClassPlanDetailSchedule " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/app/getClassPlanDetailSchedule " + (endTime - startTime) + "ms");
        }
        
        if(null != result){
        	return this.success(result);
        }
        return this.fail("服务器内部错误", null);
    }
}
