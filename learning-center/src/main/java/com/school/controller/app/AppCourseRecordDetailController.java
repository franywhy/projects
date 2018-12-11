package com.school.controller.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.school.controller.AbstractBaseController;
import com.school.pojo.CourseRecordDetailPOJO;
import com.school.pojo.CoursesPOJO;
import com.school.pojo.UserInfoPOJO;
import com.school.service.CourseRecordDetailService;
import com.school.utils.BusinessIdUtils;
import com.school.web.model.WrappedResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 录播课页面
 * @author liuhai
 * date 2017-08-18
 */
@Controller
@RequestMapping("/learningCenter/app")
public class AppCourseRecordDetailController extends AbstractBaseController  {
	@Autowired 
	private CourseRecordDetailService courseRecordDetailService;
	
	//获取录播课详情
	@ApiOperation(value = "获取录播课程详情")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "orderId", value = "订单id", required = true, dataType = "String", paramType = "query")
	})
	@RequestMapping(value = "/getCourseRecordDetail", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> getCourseRecordDetail(HttpServletRequest request, HttpServletResponse response) {
		
		Long startTime = System.currentTimeMillis();
		
		String businessId = BusinessIdUtils.getBusinessId(request);
		
		String orderIdStr = request.getParameter("orderId");
		if(null == orderIdStr) return this.error("参数提交有误:orderId");
		
		List<Long> productIdList = this.courseRecordDetailService.queryProductId(businessId);
		
		Long orderId = Long.parseLong(orderIdStr);
		
		List<CoursesPOJO> recordCourseList = this.courseRecordDetailService.getRecordCourseList(orderId, productIdList, businessId);
		
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/app/getCourseRecordDetail " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/app/getCourseRecordDetail " + (endTime - startTime) + "ms");
        }
		
		if(null != recordCourseList){
			Map<String,Object> contentMap = new HashMap<>();
			contentMap.put("recordDetail", recordCourseList);
			return this.success(contentMap);
		}
		
		return this.fail("服务器内部错误", null);
	}

	@ApiOperation(value = "获取录播课程名称_v410")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "orderId", value = "订单id", required = true, dataType = "String", paramType = "query")
	})
	@RequestMapping(value = "/getCourseRecord", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> getCourseRecord(HttpServletRequest request, HttpServletResponse response) {
		
		Long startTime = System.currentTimeMillis();

		String businessId = BusinessIdUtils.getBusinessId(request);

		String orderIdStr = request.getParameter("orderId");
		if(null == orderIdStr) return this.error("参数提交有误:orderId");

		List<Long> productIdList = this.courseRecordDetailService.queryProductId(businessId);

		Long orderId = Long.parseLong(orderIdStr);

		List<CoursesPOJO> recordCourseList = this.courseRecordDetailService.getCourseRecord(orderId, productIdList, businessId);

        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/app/getCourseRecord " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/app/getCourseRecord " + (endTime - startTime) + "ms");
        }
		
		if(null != recordCourseList){
			/*Map<String,Object> contentMap = new HashMap<>();
			contentMap.put("courseRecord", recordCourseList);*/
			return this.success(recordCourseList);
		}

		return this.fail("服务器内部错误", null);
	}
	@ApiOperation(value = "获取录播课程明细章节_v410")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "courseId", value = "录播课课程id", required = true, dataType = "String", paramType = "query")
	})
	@RequestMapping(value = "/getCourseRecordDetailByCourseId", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> getCourseRecordDetailByCourseId(HttpServletRequest request, HttpServletResponse response) {
		
		Long startTime = System.currentTimeMillis();
		
		UserInfoPOJO userInfo = this.getUserInfo(request,response);

		String courseIdStr = request.getParameter("courseId");
		if(null == courseIdStr) return this.error("参数提交有误:courseId");

		Long courseId = Long.parseLong(courseIdStr);

		List<CourseRecordDetailPOJO> recordCourseDetailList = this.courseRecordDetailService.getCourseRecordDetailByCourseId(userInfo.getUserId(),courseId);

        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/app/getCourseRecordDetailByCourseId " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/app/getCourseRecordDetailByCourseId " + (endTime - startTime) + "ms");
        }
		
		if(null != recordCourseDetailList){
			/*Map<String,Object> contentMap = new HashMap<>();
			contentMap.put("courseRecordDetail", recordCourseDetailList);*/
			return this.success(recordCourseDetailList);
		}

		return this.fail("服务器内部错误", null);
	}
}
