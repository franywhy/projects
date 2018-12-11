package com.school.controller.app;

/**
 * @author linchaokai
 * @Description
 * @date 2018/4/2 14:35
 */

import com.school.controller.AbstractBaseController;
import com.school.entity.CourseAbnormalAbandonExamEntity;
import com.school.enums.AuditStatusEnum;
import com.school.pojo.CourseAbnormalAbandonExamPOJO;
import com.school.pojo.OrderPOJO;
import com.school.pojo.UserInfoPOJO;
import com.school.service.CourseAbnormalAbandonExamService;
import com.school.service.CourseAbnormalFreeAssessmentService;
import com.school.service.CourseAbnormalOrderService;
import com.school.service.CourseAbnormallRegistrationService;
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
import java.util.*;

@Controller
@RequestMapping("/learningCenter/app")
@Api(description = "弃考app接口")
public class AppCourseAbnormalAbandonExamController extends AbstractBaseController {
    @Autowired
    private CourseAbnormalAbandonExamService courseAbnormalAbandonExamService;

    @Autowired
    private CourseAbnormalFreeAssessmentService courseAbnormalFreeAssessmentService;

    @Autowired
    private CourseAbnormalOrderService courseAbnormalOrderService;

    @Autowired
    private CourseAbnormallRegistrationService courseAbnormallRegistrationService;

    @ApiOperation(value = "弃考列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "auditStatus", value = "状态", required = false, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormalAbandonExam/list", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code=200,message="成功",response=CourseAbnormalAbandonExamPOJO.class),
    })
    public ResponseEntity<WrappedResponse> list(HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> map = new HashMap<String,Object>();
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        String businessId = BusinessIdUtils.getBusinessId(request);
        map.put("userId",userInfo.getUserId());
        map.put("businessId",businessId);
        String auditStatus = request.getParameter("auditStatus");
        if(StringUtils.isNotBlank(auditStatus)){
            map.put("auditStatus",Long.parseLong(auditStatus));
        }
        return this.success(courseAbnormalAbandonExamService.queryPOJOList(map));
    }



    @ApiOperation(value = "班型列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/courseAbnormalAbandonExam/orderList", method = RequestMethod.GET)
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
    @RequestMapping(value = "/courseAbnormalAbandonExam/courseListByOrderId", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> courseListByOrderId(HttpServletRequest request, HttpServletResponse response) {
        Long orderId = Long.parseLong(request.getParameter("orderId"));
        return this.success(courseAbnormalFreeAssessmentService.queryCourseList(orderId));
    }

    @ApiOperation(value = "省份列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/courseAbnormalAbandonExam/areaList", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> areaList(HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> map = new HashMap<String,Object>();
        return this.success(courseAbnormallRegistrationService.queryAreaList(map));
    }

    @ApiOperation(value = "报考时间列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/courseAbnormalAbandonExam/scheduleList", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> scheduleList(HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> map = new HashMap<String,Object>();
        String businessId = BusinessIdUtils.getBusinessId(request);
        map.put("businessId",businessId);
        return this.success(courseAbnormalAbandonExamService.queryScheduleDateList(map));
    }

    @ApiOperation(value = "申请弃考")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "班型id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "courseId", value = "课程id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "bkAreaId", value = "省份id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "scheduleId", value = "时间", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "abnormalReason", value = "原因", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormalAbandonExam/save", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> save(HttpServletRequest request, HttpServletResponse response) {
        Long orderId = Long.parseLong(request.getParameter("orderId"));
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        Long bkAreaId = Long.parseLong(request.getParameter("bkAreaId"));
        Long scheduleId = Long.parseLong(request.getParameter("scheduleId"));
        String abnormalReason = request.getParameter("abnormalReason");
        UserInfoPOJO userInfo = this.getUserInfo(request,response);

        Long registrationId = courseAbnormallRegistrationService.checkRegisteration(orderId,courseId,bkAreaId,scheduleId);
        if(registrationId == null){
            return this.error("此课程未报考，请勿弃考！！");
        }
        CourseAbnormalAbandonExamPOJO courseAbnormalAbandonExamPOJO = courseAbnormalAbandonExamService.queryObject(registrationId);
        if(courseAbnormalAbandonExamPOJO != null){
            return this.error("此课程已弃考，请勿重复提交！");
        }
        courseAbnormalAbandonExamService.save(userInfo.getUserId(),orderId,abnormalReason,registrationId);
        return this.success("操作成功！！！");
    }

    @ApiOperation(value = "取消")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "pk", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/courseAbnormalAbandonExam/updateCancel", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> updateCancel(HttpServletRequest request, HttpServletResponse response) {
        Long id = Long.parseLong(request.getParameter("id"));
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        courseAbnormalAbandonExamService.updateCancel(AuditStatusEnum.quxiao.getValue(),id,userInfo.getUserId(),new Date());
        return this.success("操作成功！！！");
    }
}

