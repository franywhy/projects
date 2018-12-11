package com.school.controller.app;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.school.controller.AbstractBaseController;
import com.school.pojo.ClassplanPOJO;
import com.school.pojo.CourseCalendarPOJO;
import com.school.pojo.UserInfoPOJO;
import com.school.service.LiveCourseService;
import com.school.utils.BusinessIdUtils;
import com.school.utils.ClientEnum;
import com.school.utils.DateUtils;
import com.school.web.model.WrappedResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/learningCenter/app")
public class AppCourseLiveDetailController extends AbstractBaseController {
	
	@Autowired
	private LiveCourseService liveCourseService;
	
	@ApiOperation(value = "获取直播课程详情", notes="0:未开始 1:即将开始 2:正在直播 3:已结束 4:观看回放")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userplanId", value = "学员规划id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期", required = false, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/getLiveDetail", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getCourseList(HttpServletRequest request, HttpServletResponse response) {
		
		Long startTime = System.currentTimeMillis();
		
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        
        String businessId = BusinessIdUtils.getBusinessId(request);
        
        String userplanIdStr = request.getParameter("userplanId");
        if(null == userplanIdStr) return this.error("参数提交有误:userplanId");
        
        String dateStr = request.getParameter("date");
        
        Long userplanId = Long.parseLong(userplanIdStr);
        
        Date date = new Date();
        if(null != dateStr && !dateStr.equals("")){
        	if(!DateUtils.matchDateString(dateStr)) return this.error("参数提交有误:date");
        	
        	date = DateUtils.parse(dateStr);
        }
        List<ClassplanPOJO> result = liveCourseService.getLiveDetail(userInfo.getUserId(), userplanId, businessId, date, ClientEnum.APP);
       
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/app/getLiveDetail " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/app/getLiveDetail " + (endTime - startTime) + "ms");
        }
        
        if(null != result){
        	return this.success(result);
        }
        return this.fail("服务器内部错误", null);
    }
	
	@ApiOperation(value = "获取直播课程日历")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userplanId", value = "学员规划id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期", required = false, dataType = "String", paramType = "query")
    })
	@RequestMapping(value = "/getLiveCalendar", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getCourseCalendar(HttpServletRequest request, HttpServletResponse response) {
		
		Long startTime = System.currentTimeMillis();
		
        UserInfoPOJO userInfo = this.getUserInfo(request,response);
        
        String businessId = BusinessIdUtils.getBusinessId(request);
        String userplanIdStr = request.getParameter("userplanId");
        if(null == userplanIdStr) return this.error("参数提交有误:userplanId");
        
        String dateStr = request.getParameter("date");
        
        Long userplanId = Long.parseLong(userplanIdStr);
        
        String firstDay = null;
        String lastDay = null;
        if(null != dateStr && !dateStr.equals("")){
        	if(!DateUtils.matchDateString(dateStr)) return this.error("参数提交有误:date");
        	Date date = DateUtils.parse(dateStr);
        	
        	// 获取该日期当月的第一天  
        	Calendar cale = Calendar.getInstance();
            cale.setTime(date); 
            cale.add(Calendar.MONTH, 0); 
            cale.set(Calendar.DAY_OF_MONTH, 1);
            //获取该日期当月的第一天为周几
            int firstDayWeek = cale.get(Calendar.DAY_OF_WEEK);
    		//计算得此日历的第一格的日期
            if(firstDayWeek == 1){
            	cale.set(Calendar.DATE, cale.get(Calendar.DATE) - 7);
            }else{
            	cale.set(Calendar.DATE, cale.get(Calendar.DATE) - (firstDayWeek-1));
            }
            firstDay = DateUtils.getDayStart(cale.getTime());
            //计算得此日历的最后一格的日期
            cale.set(Calendar.DATE, cale.get(Calendar.DATE) + 41);
            lastDay = DateUtils.getDayEnd(cale.getTime());
        }
        
        List<CourseCalendarPOJO> calendarResult = liveCourseService.getLiveCalendar(userInfo.getUserId(), userplanId, businessId, firstDay, lastDay);
        
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/app/getLiveCalendar " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/app/getLiveCalendar " + (endTime - startTime) + "ms");
        }
        
        if(null != calendarResult){
        	return this.success(calendarResult);
        }
        return this.fail("服务器内部错误", null);
    }
	
}
