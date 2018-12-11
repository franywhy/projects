package com.school.controller.pc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.school.controller.AbstractBaseController;
import com.school.pojo.ClassplanPOJO;
import com.school.pojo.RatePOJO;
import com.school.pojo.UdeskPOJO;
import com.school.pojo.UserInfoPOJO;
import com.school.service.LiveCourseService;
import com.school.service.MyCourseService;
import com.school.utils.BusinessIdUtils;
import com.school.utils.ClientEnum;
import com.school.web.model.WrappedResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/learningCenter/web")
public class CourseScheduleController extends AbstractBaseController {
	
	@Autowired
	private LiveCourseService liveCourseService;
	
	@Autowired
	private MyCourseService myCourseService;
	
	@ApiOperation(value = "返回直播课程表页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userplanId", value = "学员规划id", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/live/schedule", method = RequestMethod.GET)
    public ModelAndView schedule(HttpServletRequest request, HttpServletResponse response) {

        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        
        String businessId = BusinessIdUtils.getBusinessId(request);
        String userplanIdStr = request.getParameter("userplanId");
        
        Long userplanId = Long.parseLong(userplanIdStr);
        
        List<ClassplanPOJO> result = liveCourseService.getLiveSchedule(userInfo.getUserId(), userplanId, businessId, ClientEnum.WEB);
        RatePOJO rate = myCourseService.getLiveRate(userplanId, businessId, userInfo.getUserId());
        String wxCode = myCourseService.getWxCode(userplanId, businessId);
        UdeskPOJO udesk = myCourseService.getUdesk(userInfo,userplanId, businessId);
        ModelAndView mav = this.createModelAndView(request, response, true);
        mav.setViewName("learnCenter/curriculum");
        mav.addObject("data",result);
        mav.addObject("rate",rate);
        mav.addObject("wxCode",wxCode);
        mav.addObject("udesk",udesk);
        return mav;
    }
	
	@ApiOperation(value = "获取直播课程表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userplanId", value = "学员规划id", required = true, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/live/schedule/swagger", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getCourseList(HttpServletRequest request, HttpServletResponse response) {
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        
        String businessId = BusinessIdUtils.getBusinessId(request);
        String userplanIdStr = request.getParameter("userplanId");
        if(null == userplanIdStr) return this.error("参数提交有误:userplanId");
        Long userplanId = Long.parseLong(userplanIdStr);
        
        List<ClassplanPOJO> result = liveCourseService.getLiveSchedule(userInfo.getUserId(), userplanId, businessId, ClientEnum.WEB);
        if(null != result){
        	return this.success(result);
        }
        return this.fail("服务器内部错误", null);
    }
}
