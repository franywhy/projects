package com.hq.learningcenter.school.controller.app;

import com.hq.learningcenter.school.controller.AbstractBaseController;
import com.hq.learningcenter.school.entity.CourseAbnormalOrderEntity;
import com.hq.learningcenter.school.enums.AuditStatusEnum;
import com.hq.learningcenter.school.pojo.UserInfoPOJO;
import com.hq.learningcenter.school.service.CourseAbnormalOrderService;
import com.hq.learningcenter.school.web.model.WrappedResponse;
import com.hq.learningcenter.utils.BusinessIdUtils;
import com.hq.learningcenter.utils.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
@Api(description = "休学app接口")
public class AppCourseAbnormalOrderController extends AbstractBaseController {
    @Autowired
    private CourseAbnormalOrderService courseAbnormalOrderService;

    @ApiOperation(value = "休学列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "auditStatus", value = "状态", required = false, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/courseabnormalorder/list", method = RequestMethod.GET)
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
        return this.success(courseAbnormalOrderService.queryList(map));
    }

    @ApiOperation(value = "班型列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/courseabnormalorder/orderList", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> orderList(HttpServletRequest request, HttpServletResponse response) {
        String businessId = BusinessIdUtils.getBusinessId(request);
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        return this.success(courseAbnormalOrderService.queryOrderList(userInfo.getUserId(),businessId));
    }


    @ApiOperation(value = "申请休学")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单Id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "休学时间(yyyy-MM-dd)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "expectEndTime", value = "复课时间(yyyy-MM-dd)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "abnormalReason", value = "休学原因", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/courseabnormalorder/save", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> save(HttpServletRequest request, HttpServletResponse response) {
        CourseAbnormalOrderEntity courseAbnormalOrder = new CourseAbnormalOrderEntity();
        Long orderId = Long.parseLong(request.getParameter("orderId"));
        String startTime = request.getParameter("startTime");
        String expectEndTime = request.getParameter("expectEndTime");
        String abnormalReason = request.getParameter("abnormalReason");
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        String errorMsg = courseAbnormalOrderService.verifyStatus(orderId, DateUtils.parse(startTime),DateUtils.parse(expectEndTime));
        if(StringUtils.isNotBlank(errorMsg)){
            return this.error(errorMsg);
        }
        courseAbnormalOrderService.save(userInfo.getUserId(),orderId,startTime,expectEndTime,abnormalReason);
        return this.success("操作成功！！！");
    }

    @ApiOperation(value = "取消")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "pk", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/courseabnormalorder/updateCancel", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> updateCancel(HttpServletRequest request, HttpServletResponse response) {
        Long id = Long.parseLong(request.getParameter("id"));
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        courseAbnormalOrderService.updateCancel(AuditStatusEnum.quxiao.getValue(),id,userInfo.getUserId(),new Date());
        return this.success("操作成功！！！");
    }
}

