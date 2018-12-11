package com.school.controller.pc;

import com.school.controller.AbstractBaseController;
import com.school.pojo.CoursesPOJO;
import com.school.pojo.RatePOJO;
import com.school.pojo.UserInfoPOJO;
import com.school.service.CourseRecordDetailService;
import com.school.service.MyCourseService;
import com.school.utils.BusinessIdUtils;
import com.school.web.model.WrappedResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 录播课页面
 *
 * @author liuhai
 * date 2017-08-18
 */
@Controller
@RequestMapping("/learningCenter/web")
public class CourseRecordDetailController extends AbstractBaseController {
    @Autowired
    private CourseRecordDetailService courseRecordDetailService;
    @Autowired
    private MyCourseService myCourseService;

    //获取录播课详情
    @ApiOperation(value = "获取录播课程详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单id", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public ModelAndView getCourseRecord(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        UserInfoPOJO userInfo = this.getUserInfo(request, response);
        String businessId = BusinessIdUtils.getBusinessId(request);

        List<Long> productIdList = this.courseRecordDetailService.queryProductId(businessId);
        String orderIdStr = request.getParameter("orderId");
        Long orderId = Long.parseLong(orderIdStr);

        List<CoursesPOJO> recordCourseList = this.courseRecordDetailService.getRecordCourseList(orderId, productIdList, businessId);
        Map<String, Object> hearResult = this.courseRecordDetailService.getRecordHear(userInfo.getUserId());
        RatePOJO rate = this.myCourseService.getRecordRate(orderId, businessId, userInfo.getUserId());

        ModelAndView mav = this.createModelAndView(request, response, true);
        mav.setViewName("learnCenter/recording_list");

        mav.addObject("recordCourseList", recordCourseList);
        mav.addObject("hearResult", hearResult);
        mav.addObject("rateResult", rate);
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/record " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/record " + (endTime - startTime) + "ms");
        }
        return mav;
    }

    //获取录播课列表详情
    @ApiOperation(value = "获取录播课列表详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单id", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/record/detail", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getCourseRecordDetail(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        String businessId = BusinessIdUtils.getBusinessId(request);
        String orderIdStr = request.getParameter("orderId");
        Long orderId = Long.parseLong(orderIdStr);
        if (null == orderIdStr) return this.error("参数提交有误:orderId");

        List<Long> productIdList = this.courseRecordDetailService.queryProductId(businessId);
        List<CoursesPOJO> recordCourseList = this.courseRecordDetailService.getRecordCourseList(orderId, productIdList, businessId);

        if (null != recordCourseList)
            return this.success(recordCourseList);
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/record/detail " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/record/detail " + (endTime - startTime) + "ms");
        }

        return this.fail("服务器内部错误", null);
    }

    //获取录播课页面头详情
    @ApiOperation(value = "获取录播课程页面头详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单id", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/record/hear", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getCourseRecordHear(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        UserInfoPOJO userInfo = this.getUserInfo(request, response);
        String businessId = BusinessIdUtils.getBusinessId(request);

        String orderIdStr = request.getParameter("orderId");
        if (null == orderIdStr) return this.error("参数提交有误:orderId");

        Long orderId = Long.parseLong(orderIdStr);
        RatePOJO rate = this.myCourseService.getRecordRate(orderId, businessId, userInfo.getUserId());
        Map<String, Object> hearResult = this.courseRecordDetailService.getRecordHear(userInfo.getUserId());

        if (null != hearResult)
            return this.success(hearResult);

        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/record/hear " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/record/hear " + (endTime - startTime) + "ms");
        }

        return this.fail("服务器内部错误", null);
    }
}
