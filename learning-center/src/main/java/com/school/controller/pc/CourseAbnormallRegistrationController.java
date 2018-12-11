package com.school.controller.pc;

import com.school.controller.AbstractBaseController;
import com.school.enums.AuditStatusEnum;
import com.school.pojo.CourseAbnormalFreeAssessmentPOJO;
import com.school.pojo.CourseAbnormallRegistrationPOJO;
import com.school.pojo.MallExamSchedulePOJO;
import com.school.pojo.UserInfoPOJO;
import com.school.service.CourseAbnormalAbandonExamService;
import com.school.service.CourseAbnormalFreeAssessmentService;
import com.school.service.CourseAbnormalOrderService;
import com.school.service.CourseAbnormallRegistrationService;
import com.school.swagger.model.CourseModel;
import com.school.utils.BusinessIdUtils;
import com.school.web.model.WrappedResponse;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/learningCenter/web")
@Api(description = "报考PC接口")
public class CourseAbnormallRegistrationController extends AbstractBaseController {

    @Autowired
    private CourseAbnormallRegistrationService courseAbnormallRegistrationService;

    @Autowired
    private CourseAbnormalOrderService courseAbnormalOrderService;

    @Autowired
    private CourseAbnormalFreeAssessmentService courseAbnormalFreeAssessmentService;

    @Autowired
    private CourseAbnormalAbandonExamService courseAbnormalAbandonExamService;


    @ApiOperation(value = "报考列表页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormallRegistrationList", method = RequestMethod.GET)
    public ModelAndView regeisterListPage(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<String, Object>();
        UserInfoPOJO userInfo = this.getUserInfo(request, response);
        String businessId = BusinessIdUtils.getBusinessId(request);
        map.put("userId", userInfo.getUserId());
        map.put("businessId", businessId);
        if (StringUtils.isNotBlank(request.getParameter("auditStatus"))) {
            map.put("auditStatus", Long.parseLong(request.getParameter("auditStatus")));
        }
        List<CourseAbnormallRegistrationPOJO> list = courseAbnormallRegistrationService.queryList(map);
        //班型
        List<Map<String, Object>> orderList = courseAbnormalOrderService.queryOrderList(userInfo.getUserId(), businessId);
        //时间
        List<MallExamSchedulePOJO> timeList = courseAbnormalAbandonExamService.queryScheduleDateList(map);
        //省份
        List<Map<String, Object>> areaList = courseAbnormallRegistrationService.queryAreaList(map);


        ModelAndView mav = this.createModelAndView(request, response, true);
        mav.setViewName("learnCenter/enroll_exam");
        mav.addObject("data", list);
        mav.addObject("classtypeData", orderList);
        mav.addObject("scheduleDateData", timeList);
        mav.addObject("areaData", areaList);
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/courseAbnormallRegistrationList " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/courseAbnormallRegistrationList " + (endTime - startTime) + "ms");
        }
        return mav;
    }

    @ApiOperation(value = "报考列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "auditStatus", value = "状态（暂时不需要做）", required = false, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormallRegistration/list", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功", response = CourseAbnormalFreeAssessmentPOJO.class),
    })
    public ResponseEntity<WrappedResponse> list(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<String, Object>();
        UserInfoPOJO userInfo = this.getUserInfo(request, response);
        String businessId = BusinessIdUtils.getBusinessId(request);
        map.put("userId", userInfo.getUserId());
        map.put("businessId", businessId);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(request.getParameter("auditStatus"))) {
            map.put("auditStatus", Long.parseLong(request.getParameter("auditStatus")));
        }
        List<CourseAbnormallRegistrationPOJO> list = courseAbnormallRegistrationService.queryList(map);
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/courseAbnormallRegistration/list " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/courseAbnormallRegistration/list " + (endTime - startTime) + "ms");
        }
        return this.success(list);
    }

    @ApiOperation(value = "课程列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单pk", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormallRegistration/courseListByOrderId", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功", response = CourseModel.class),
    })
    public ResponseEntity<WrappedResponse> courseListByOrderId(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        Long orderId = Long.parseLong(request.getParameter("orderId"));
        List<Map<String, Object>> list = courseAbnormalFreeAssessmentService.queryCourseList(orderId);
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web//courseAbnormallRegistration/courseListByOrderId " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web//courseAbnormallRegistration/courseListByOrderId " + (endTime - startTime) + "ms");
        }
        return this.success(list);
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
        Long startTime = System.currentTimeMillis();
        Long orderId = Long.parseLong(request.getParameter("orderId"));
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        Long areaId = Long.parseLong(request.getParameter("areaId"));
        Long scheduleId = Long.parseLong(request.getParameter("scheduleId"));
        String registerPK = request.getParameter("registerPK");
        UserInfoPOJO userInfo = this.getUserInfo(request, response);
        String errorMsg = courseAbnormallRegistrationService.save(orderId, courseId, areaId, scheduleId, registerPK, userInfo.getUserId());
        if (org.apache.commons.lang3.StringUtils.isNotBlank(errorMsg)) {
            return this.error(errorMsg);
        }
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/courseAbnormallRegistration/save " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/courseAbnormallRegistration/save " + (endTime - startTime) + "ms");
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
        Long startTime = System.currentTimeMillis();
        Long id = Long.parseLong(request.getParameter("id"));
        UserInfoPOJO userInfo = this.getUserInfo(request, response);
        try {
            courseAbnormallRegistrationService.updateCancel(AuditStatusEnum.quxiao.getValue(), id, userInfo.getUserId(), new Date());
        } catch (Exception e) {
            return this.error("取消失败!");
        }
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/courseAbnormallRegistration/updateCancel " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/courseAbnormallRegistration/updateCancel " + (endTime - startTime) + "ms");
        }
        return this.success("操作成功！！！");
    }
}
