package com.hq.learningcenter.school.controller.pc;

import com.hq.learningcenter.school.controller.AbstractBaseController;
import com.hq.learningcenter.school.pojo.UserInfoPOJO;
import com.hq.learningcenter.school.web.model.WrappedResponse;
import com.hq.learningcenter.utils.BusinessIdUtils;
import com.hq.learningcenter.school.enums.AuditStatusEnum;
import com.hq.learningcenter.school.pojo.CourseAbnormalOrderPOJO;
import com.hq.learningcenter.school.service.CourseAbnormalOrderService;
import com.hq.learningcenter.school.service.UserstopService;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/learningCenter/web")
@Api(description = "休学pc接口")
public class CourseAbnormalOrderController extends AbstractBaseController {
	@Autowired
	private UserstopService userstopService;

	@ApiOperation(value = "休学列表页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/courseabnormalorderList", method = RequestMethod.GET)
	public ModelAndView userstopListPage(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        String businessId = BusinessIdUtils.getBusinessId(request);
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId",userInfo.getUserId());
        map.put("businessId",businessId);
	    //查询学员休学列表数据
		ModelAndView mav = this.createModelAndView(request, response, true);
		mav.setViewName("learnCenter/attendance");
		mav.addObject("data",courseAbnormalOrderService.queryList(map));
        mav.addObject("classtypeData",courseAbnormalOrderService.queryOrderList(userInfo.getUserId(),businessId));
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/courseabnormalorderList " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/courseabnormalorderList " + (endTime - startTime) + "ms");
        }
        return mav;
	}

    @Autowired
    private CourseAbnormalOrderService courseAbnormalOrderService;

    @ApiOperation(value = "休学列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "auditStatus", value = "状态", required = false, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/courseabnormalorder/list", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> list(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        Map<String,Object> map = new HashMap<String,Object>();
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        String businessId = BusinessIdUtils.getBusinessId(request);
        map.put("userId",userInfo.getUserId());
        map.put("businessId",businessId);
        String auditStatus = request.getParameter("auditStatus");
        if(StringUtils.isNotBlank(auditStatus)){
            map.put("auditStatus",Long.parseLong(auditStatus));
        }
        List<CourseAbnormalOrderPOJO> list = courseAbnormalOrderService.queryList(map);
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/courseabnormalorder/list " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/courseabnormalorder/list " + (endTime - startTime) + "ms");
        }
        return this.success(list);
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
        Long startTimestamp = System.currentTimeMillis();
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
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/courseabnormalorder/save " + (endTime - startTimestamp) + "ms");
        if ((endTime - startTimestamp) > 1000) {
            logger.error("/learningCenter/web/courseabnormalorder/save " + (endTime - startTimestamp) + "ms");
        }
        return this.success("操作成功！！！");
    }

    @ApiOperation(value = "取消")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "pk", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/courseabnormalorder/updateCancel", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> updateCancel(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        Long id = Long.parseLong(request.getParameter("id"));
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        courseAbnormalOrderService.updateCancel(AuditStatusEnum.quxiao.getValue(),id,userInfo.getUserId(),new Date());
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/courseabnormalorder/updateCancel " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/courseabnormalorder/updateCancel " + (endTime - startTime) + "ms");
        }
        return this.success("操作成功！！！");
    }
}
