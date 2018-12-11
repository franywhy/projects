package com.school.controller.app;

import com.school.controller.AbstractBaseController;
import com.school.enums.AuditStatusEnum;
import com.school.pojo.CourseAbnormalFreeAssessmentPOJO;
import com.school.pojo.CourseAbnormallRegistrationPOJO;
import com.school.pojo.OrderPOJO;
import com.school.pojo.UserInfoPOJO;
import com.school.service.*;
import com.school.swagger.model.CourseModel;
import com.school.utils.BusinessIdUtils;
import com.school.utils.DateUtils;
import com.school.web.model.WrappedResponse;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/learningCenter/app")
@Api(description = "报考app接口")
public class AppCourseAbnormallRegistrationController extends AbstractBaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CourseAbnormallRegistrationService courseAbnormallRegistrationService;

    @Autowired
    private CourseAbnormalOrderService courseAbnormalOrderService;

    @Autowired
    private CourseAbnormalFreeAssessmentService courseAbnormalFreeAssessmentService;

    @Autowired
    private CourseAbnormalAbandonExamService courseAbnormalAbandonExamService;


    @ApiOperation(value = "报考列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "auditStatus", value = "状态（暂时不需要做）", required = false, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormallRegistration/list", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code=200,message="成功",response=CourseAbnormalFreeAssessmentPOJO.class),
    })
    public ResponseEntity<WrappedResponse> list(HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> map = new HashMap<String,Object>();
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        String businessId = BusinessIdUtils.getBusinessId(request);
        map.put("userId",userInfo.getUserId());
        map.put("businessId",businessId);
        if(StringUtils.isNotBlank(request.getParameter("auditStatus"))){
            map.put("auditStatus",Long.parseLong(request.getParameter("auditStatus")));
        }
        return this.success(courseAbnormallRegistrationService.queryList(map));
    }

	@ApiOperation(value = "班型列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
	})
	@RequestMapping(value = "/courseAbnormallRegistration/orderList", method = RequestMethod.GET)
	public ResponseEntity<WrappedResponse> orderList(HttpServletRequest request, HttpServletResponse response) {
        String businessId = BusinessIdUtils.getBusinessId(request);
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        return this.success(courseAbnormalOrderService.queryOrderList(userInfo.getUserId(),businessId));
	}

    @ApiOperation(value = "课程列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单pk", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormallRegistration/courseListByOrderId", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code=200,message="成功",response=CourseModel.class),
    })
    public ResponseEntity<WrappedResponse> courseListByOrderId(HttpServletRequest request, HttpServletResponse response) {
        Long orderId = Long.parseLong(request.getParameter("orderId"));
        return this.success(courseAbnormalFreeAssessmentService.queryCourseList(orderId));
    }


    @ApiOperation(value = "省份列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/courseAbnormallRegistration/areaList", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code=200,message="成功",response=CourseModel.class),
    })
    public ResponseEntity<WrappedResponse> areaList(HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> map = new HashMap<String,Object>();
        return this.success(courseAbnormallRegistrationService.queryAreaList(map));
    }


    @ApiOperation(value = "考试时间列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/courseAbnormallRegistration/examSchedule", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code=200,message="成功",response=CourseModel.class),
    })
    public ResponseEntity<WrappedResponse> examSchedule(HttpServletRequest request, HttpServletResponse response) {
        String businessId = BusinessIdUtils.getBusinessId(request);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("businessId",businessId);
        return this.success(courseAbnormalAbandonExamService.queryScheduleDateList(map));
    }

    @ApiOperation(value = "申请报考")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "班型Id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "courseId", value = "课程id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "areaId", value = "报考省份", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "scheduleId", value = "报考时间", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "registerPK", value = "报名表号", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormallRegistration/save", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> save(HttpServletRequest request, HttpServletResponse response) {
        Long orderId = Long.parseLong(request.getParameter("orderId"));
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        Long areaId = Long.parseLong(request.getParameter("areaId"));
        Long scheduleId = Long.parseLong(request.getParameter("scheduleId"));
        String registerPK = request.getParameter("registerPK");
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        String errorMsg = courseAbnormallRegistrationService.save(orderId,courseId,areaId,scheduleId,registerPK,userInfo.getUserId());
        if(StringUtils.isNotBlank(errorMsg)){
            return this.error(errorMsg);
        }
        return this.success("操作成功！！！");
    }

    @ApiOperation(value = "取消")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "pk", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormallRegistration/updateCancel", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> updateCancel(HttpServletRequest request, HttpServletResponse response) {
        Long id = Long.parseLong(request.getParameter("id"));
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        try {
            courseAbnormallRegistrationService.updateCancel(AuditStatusEnum.quxiao.getValue(),id,userInfo.getUserId(),new Date());
        }catch (Exception e){
            return this.error("取消失败!");
        }
        return this.success("操作成功！！！");
    }
}
