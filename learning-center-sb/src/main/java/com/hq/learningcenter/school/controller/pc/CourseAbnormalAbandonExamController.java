package com.hq.learningcenter.school.controller.pc;

/**
 * @author linchaokai
 * @Description
 * @date 2018/4/2 14:35
 */

import com.hq.learningcenter.school.controller.AbstractBaseController;
import com.hq.learningcenter.school.enums.AuditStatusEnum;
import com.hq.learningcenter.school.pojo.CourseAbnormalAbandonExamPOJO;
import com.hq.learningcenter.school.pojo.UserInfoPOJO;
import com.hq.learningcenter.school.service.CourseAbnormalAbandonExamService;
import com.hq.learningcenter.school.service.CourseAbnormalOrderService;
import com.hq.learningcenter.school.service.CourseAbnormallRegistrationService;
import com.hq.learningcenter.school.web.model.WrappedResponse;
import com.hq.learningcenter.utils.BusinessIdUtils;
import com.hq.learningcenter.school.service.CourseAbnormalFreeAssessmentService;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/learningCenter/web")
@Api(description = "弃考pc接口")
public class CourseAbnormalAbandonExamController extends AbstractBaseController {
    @Autowired
    private CourseAbnormalAbandonExamService courseAbnormalAbandonExamService;

    @Autowired
    private CourseAbnormalFreeAssessmentService courseAbnormalFreeAssessmentService;

    @Autowired
    private CourseAbnormalOrderService courseAbnormalOrderService;

    @Autowired
    private CourseAbnormallRegistrationService courseAbnormallRegistrationService;


    @ApiOperation(value = "弃考列表页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormalAbandonExamList", method = RequestMethod.GET)
    public ModelAndView userstopListPage(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<String, Object>();
        UserInfoPOJO userInfo = this.getUserInfo(request, response);
        String businessId = BusinessIdUtils.getBusinessId(request);
        map.put("userId", userInfo.getUserId());
        map.put("businessId", businessId);
        ModelAndView mav = this.createModelAndView(request, response, true);
        mav.setViewName("learnCenter/giveup_exam");
        mav.addObject("data", courseAbnormalAbandonExamService.queryPOJOList(map));
        mav.addObject("classtypeData", courseAbnormalOrderService.queryOrderList(userInfo.getUserId(), businessId));
        mav.addObject("scheduleDateData", courseAbnormalAbandonExamService.queryScheduleDateList(map));
        mav.addObject("areaData", courseAbnormallRegistrationService.queryAreaList(map));
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/live " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/live " + (endTime - startTime) + "ms");
        }
        return mav;
    }

    @ApiOperation(value = "弃考列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "auditStatus", value = "状态", required = false, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormalAbandonExam/list", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功", response = CourseAbnormalAbandonExamPOJO.class),
    })
    public ResponseEntity<WrappedResponse> list(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<String, Object>();
        UserInfoPOJO userInfo = this.getUserInfo(request, response);
        String businessId = BusinessIdUtils.getBusinessId(request);
        map.put("userId", userInfo.getUserId());
        map.put("businessId", businessId);
        String auditStatus = request.getParameter("auditStatus");
        if (StringUtils.isNotBlank(auditStatus)) {
            map.put("auditStatus", Long.parseLong(auditStatus));
        }
        List<CourseAbnormalAbandonExamPOJO> list = courseAbnormalAbandonExamService.queryPOJOList(map);
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/courseAbnormalAbandonExam/list" + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/courseAbnormalAbandonExam/list " + (endTime - startTime) + "ms");
        }
        return this.success(list);
    }

    @ApiOperation(value = "课程列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单pk", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormalAbandonExam/courseListByOrderId", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> courseListByOrderId(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        Long orderId = Long.parseLong(request.getParameter("orderId"));
        List<Map<String, Object>> list = courseAbnormalFreeAssessmentService.queryCourseList(orderId);
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/courseAbnormalAbandonExam/courseListByOrderId " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/courseAbnormalAbandonExam/courseListByOrderId " + (endTime - startTime) + "ms");
        }
        return this.success(list);
    }

    @ApiOperation(value = "申请弃考")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "班型Id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "courseId", value = "课程id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "bkAreaId", value = "省份id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "scheduleId", value = "时间", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "abnormalReason", value = "原因", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormalAbandonExam/save", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> save(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        Long orderId = Long.parseLong(request.getParameter("orderId"));
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        Long bkAreaId = Long.parseLong(request.getParameter("bkAreaId"));
        Long scheduleId = Long.parseLong(request.getParameter("scheduleId"));
        String abnormalReason = request.getParameter("abnormalReason");
        UserInfoPOJO userInfo = this.getUserInfo(request, response);

        Long registrationId = courseAbnormallRegistrationService.checkRegisteration(orderId, courseId, bkAreaId, scheduleId);
        if (registrationId == null) {
            return this.error("此课程未报考，请勿弃考！");
        }
        CourseAbnormalAbandonExamPOJO courseAbnormalAbandonExamPOJO = courseAbnormalAbandonExamService.queryObject(registrationId);
        if (courseAbnormalAbandonExamPOJO != null) {
            return this.error("此课程已弃考，请勿重复提交！");
        }
        courseAbnormalAbandonExamService.save(userInfo.getUserId(), orderId, abnormalReason, registrationId);
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/courseAbnormalAbandonExam/save " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/courseAbnormalAbandonExam/save " + (endTime - startTime) + "ms");
        }
        return this.success("操作成功！！！");
    }

    @ApiOperation(value = "取消")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "pk", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormalAbandonExam/updateCancel", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> updateCancel(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        Long id = Long.parseLong(request.getParameter("id"));
        UserInfoPOJO userInfo = this.getUserInfo(request, response);
        courseAbnormalAbandonExamService.updateCancel(AuditStatusEnum.quxiao.getValue(), id, userInfo.getUserId(), new Date());
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/courseAbnormalAbandonExam/courseAbnormalAbandonExam/updateCancel " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/courseAbnormalAbandonExam/courseAbnormalAbandonExam/updateCancel " + (endTime - startTime) + "ms");
        }
        return this.success("操作成功！！！");
    }
}

