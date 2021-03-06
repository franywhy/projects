package com.school.controller.pc;

import com.school.controller.AbstractBaseController;
import com.school.enums.AuditStatusEnum;
import com.school.pojo.CourseAbnormalFreeAssessmentPOJO;
import com.school.pojo.UserInfoPOJO;
import com.school.service.CourseAbnormalFreeAssessmentService;
import com.school.service.CourseAbnormalOrderService;
import com.school.swagger.model.CourseModel;
import com.school.utils.BusinessIdUtils;
import com.school.utils.DateUtils;
import com.school.web.model.WrappedResponse;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/learningCenter/web")
@Api(description = "免考pc接口")
public class CourseAbnormalFreeAssessmentController extends AbstractBaseController {

    @Autowired
    private CourseAbnormalFreeAssessmentService courseAbnormalFreeAssessmentService;

    @Autowired
    private CourseAbnormalOrderService courseAbnormalOrderService;

    @ApiOperation(value = "免考列表页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormalFreeAssessmentList", method = RequestMethod.GET)
    public ModelAndView userstopListPage(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        String businessId = BusinessIdUtils.getBusinessId(request);
        UserInfoPOJO userInfo = this.getUserInfo(request, response);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userInfo.getUserId());
        map.put("businessId", businessId);
        ModelAndView mav = this.createModelAndView(request, response, true);
        mav.setViewName("learnCenter/avoid_exam");
        mav.addObject("data", courseAbnormalFreeAssessmentService.queryList(map));//免考列表
        mav.addObject("classtypeData", courseAbnormalOrderService.queryOrderList(userInfo.getUserId(), businessId));//订单列表
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/courseAbnormalFreeAssessmentList " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/courseAbnormalFreeAssessmentList " + (endTime - startTime) + "ms");
        }
        return mav;
    }

    @ApiOperation(value = "免考列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "auditStatus", value = "状态（暂时不需要做）", required = false, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormalFreeAssessment/list", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功", response = CourseAbnormalFreeAssessmentPOJO.class),
    })
    public ResponseEntity<WrappedResponse> list(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        UserInfoPOJO userInfo = this.getUserInfo(request, response);
        String businessId = BusinessIdUtils.getBusinessId(request);
        map.put("userId", userInfo.getUserId());
        map.put("businessId", businessId);
        String auditStatus = request.getParameter("auditStatus");
        if (StringUtils.isNotBlank(auditStatus)) {
            map.put("auditStatus", Long.parseLong(auditStatus));
        }
        return this.success(courseAbnormalFreeAssessmentService.queryList(map));
    }

    @ApiOperation(value = "课程列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单pk", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormalFreeAssessment/courseListByOrderId", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功", response = CourseModel.class),
    })
    public ResponseEntity<WrappedResponse> courseListByOrderId(HttpServletRequest request, HttpServletResponse response) {
        Long orderId = Long.parseLong(request.getParameter("orderId"));
        return this.success(courseAbnormalFreeAssessmentService.queryCourseList(orderId));
    }

    @ApiOperation(value = "申请免考")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "班型Id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "courseId", value = "课程id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间(yyyy-MM-dd)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间(yyyy-MM-dd)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "abnormalReason", value = "原因", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormalFreeAssessment/save", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> save(HttpServletRequest request, HttpServletResponse response) {
        Long orderId = Long.parseLong(request.getParameter("orderId"));
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String abnormalReason = request.getParameter("abnormalReason");
        UserInfoPOJO userInfo = this.getUserInfo(request, response);
        String errorMsg = courseAbnormalFreeAssessmentService.verifyStatus(orderId, DateUtils.parse(startTime), DateUtils.parse(endTime), courseId);
        if (StringUtils.isNotBlank(errorMsg)) {
            return this.error(errorMsg);
        }
        courseAbnormalFreeAssessmentService.save(userInfo.getUserId(), orderId, courseId, startTime, endTime, abnormalReason);
        return this.success("操作成功！！！");
    }

    @ApiOperation(value = "取消")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "pk", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormalFreeAssessment/updateCancel", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> updateCancel(HttpServletRequest request, HttpServletResponse response) {
        Long id = Long.parseLong(request.getParameter("id"));
        UserInfoPOJO userInfo = this.getUserInfo(request, response);
        courseAbnormalFreeAssessmentService.updateCancel(AuditStatusEnum.quxiao.getValue(), id, userInfo.getUserId(), new Date());
        return this.success("操作成功！！！");
    }
}
