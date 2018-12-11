package com.hq.learningcenter.school.controller.pc;

import com.hq.learningcenter.school.controller.AbstractBaseController;
import com.hq.learningcenter.school.pojo.*;
import com.hq.learningcenter.school.web.model.WrappedResponse;
import com.hq.learningcenter.utils.BusinessIdUtils;
import com.hq.learningcenter.utils.ClientEnum;
import com.hq.learningcenter.utils.DateUtils;
import com.hq.learningcenter.school.service.LiveCourseService;
import com.hq.learningcenter.school.service.MyCourseService;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/learningCenter/web")
public class CourseLiveDetailController extends AbstractBaseController {

    @Autowired
    private LiveCourseService liveCourseService;

    @Autowired
    private MyCourseService myCourseService;

    @ApiOperation(value = "返回直播课页面", notes = "/live/calendar;/live/detail")
    @RequestMapping(value = "/live", method = RequestMethod.GET)
    public ModelAndView getCourseLive(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        UserInfoPOJO userInfo = this.getUserInfo(request, response);

        String businessId = BusinessIdUtils.getBusinessId(request);
        String userplanIdStr = request.getParameter("userplanId");
        String dateStr = request.getParameter("date");
        String isNoClass = request.getParameter("isNoClass");

        Long userplanId = Long.parseLong(userplanIdStr);
        Date date = new Date();
        if (null != dateStr && !dateStr.equals("")) {
            date = DateUtils.parse(dateStr);
        }

        List<CourseCalendarPOJO> initCalendar = null;
        Map<String, Object> hearResult = null;
        RatePOJO rateRsult = null;
        if (isNoClass.equals("0")) {
            // 获取该日期当周的第一天
            Calendar cale = Calendar.getInstance();
            ;
            cale.setTime(date);
            cale.set(Calendar.DAY_OF_WEEK, 1);
            String firstDay = DateUtils.getDayStart(cale.getTime());
            //计算得此日历的最后一格的日期
            cale.set(Calendar.DATE, cale.get(Calendar.DATE) + 6);
            String lastDay = DateUtils.getDayEnd(cale.getTime());

            List<CourseCalendarPOJO> calendarResult = liveCourseService.getLiveCalendar(userInfo.getUserId(), userplanId, businessId, firstDay, lastDay);
            initCalendar = initWebCalendarWeek(calendarResult, userplanId, date);

            hearResult = liveCourseService.getLiveHear(userplanId, businessId);

            rateRsult = myCourseService.getLiveRate(userplanId, businessId, userInfo.getUserId());
        } else {
            initCalendar = initWebCalendarWeek(null, userplanId, date);
            hearResult = new HashMap<String, Object>();
            hearResult.put("courseStatus", "暂无");
            hearResult.put("courseDuration", "暂无");
            rateRsult = new RatePOJO();
            rateRsult.setProgressRate(0);
            rateRsult.setParticipationRate(0);
        }

        String wxCode = myCourseService.getWxCode(userplanId, businessId);

        UdeskPOJO udesk = myCourseService.getUdesk(userInfo, userplanId, businessId);

        ModelAndView mav = this.createModelAndView(request, response, true);
        mav.setViewName("learnCenter/curriculum_detail");

        mav.addObject("wxCode", wxCode);

        mav.addObject("udesk", udesk);

        mav.addObject("calendar", initCalendar);

        mav.addObject("head", hearResult);

        mav.addObject("rate", rateRsult);

        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/live " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/live " + (endTime - startTime) + "ms");
        }
        return mav;
    }

    @ApiOperation(value = "获取直播课程详情", notes = "classStatus 课状态:  0:未开始  1:即将开始  2:直播中  3:已结束  4:观看回放")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userplanId", value = "学员规划id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/live/detail", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getCourseLiveDetail(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        UserInfoPOJO userInfo = this.getUserInfo(request, response);

        String businessId = BusinessIdUtils.getBusinessId(request);
        String userplanIdStr = request.getParameter("userplanId");
        if (null == userplanIdStr) return this.error("参数提交有误:userplanId");

        String dateStr = request.getParameter("date");
        Date date = new Date();
        if (null != dateStr && !dateStr.equals("")) {
            if (!DateUtils.matchDateString(dateStr)) return this.error("参数提交有误:date");
            date = DateUtils.parse(dateStr);
        }

        Long userplanId = Long.parseLong(userplanIdStr);
        Map<String, Object> contentMap = new HashMap<>();
        List<ClassplanPOJO> liveDetailResult = liveCourseService.getLiveDetail(userInfo.getUserId(), userplanId, businessId, date, ClientEnum.WEB);
        List<HopePOJO> hopeResult = liveCourseService.getLiveHopeForWeb(userplanId, businessId, date);
        contentMap.put("liveDetail", liveDetailResult);
        contentMap.put("hope", hopeResult);
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/live/detail " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/live/detail " + (endTime - startTime) + "ms");
        }
        return this.success(contentMap);
    }

    @ApiOperation(value = "获取直播课程日历-月", notes = "monType 标记该日期为  -1:上个月   0:当前月   1:下个月")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userplanId", value = "学员规划id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/live/calendar/mon", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getCourseLiveCalendarMon(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        UserInfoPOJO userInfo = this.getUserInfo(request, response);

        String businessId = BusinessIdUtils.getBusinessId(request);
        String userplanIdStr = request.getParameter("userplanId");
        if (null == userplanIdStr) return this.error("参数提交有误:userplanId");

        String dateStr = request.getParameter("date");

        Date date = new Date();
        if (null != dateStr && !dateStr.equals("")) {
            if (!DateUtils.matchDateString(dateStr)) return this.error("参数提交有误:date");
            date = DateUtils.parse(dateStr);
        }

        Long userplanId = Long.parseLong(userplanIdStr);
        // 获取该日期当月的第一天  
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        //获取该日期当月的第一天为周几
        int firstDayWeek = cale.get(Calendar.DAY_OF_WEEK);
        //计算得此日历的第一格的日期
        if (firstDayWeek == 1) {
            cale.set(Calendar.DATE, cale.get(Calendar.DATE) - 7);
        } else {
            cale.set(Calendar.DATE, cale.get(Calendar.DATE) - (firstDayWeek - 1));
        }
        String firstDay = DateUtils.getDayStart(cale.getTime());
        //计算得此日历的最后一格的日期
        cale.set(Calendar.DATE, cale.get(Calendar.DATE) + 41);
        String lastDay = DateUtils.getDayEnd(cale.getTime());

        List<CourseCalendarPOJO> calendarResult = liveCourseService.getLiveCalendar(userInfo.getUserId(), userplanId, businessId, firstDay, lastDay);

        List<CourseCalendarPOJO> initCalendar = initWebCalendarMon(calendarResult, userplanId, date);
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/live/calendar/mon " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/live/calendar/mon " + (endTime - startTime) + "ms");
        }
        return this.success(initCalendar);
    }

    @ApiOperation(value = "获取直播课程日历-周", notes = "monType 标记该日期为  -1:上个月   0:当前月   1:下个月")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userplanId", value = "学员规划id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/live/calendar/week", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getCourseLiveCalendarWeek(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = System.currentTimeMillis();
        UserInfoPOJO userInfo = this.getUserInfo(request, response);

        String businessId = BusinessIdUtils.getBusinessId(request);
        String userplanIdStr = request.getParameter("userplanId");
        if (null == userplanIdStr) return this.error("参数提交有误:userplanId");

        String dateStr = request.getParameter("date");
        Date date = new Date();
        if (null != dateStr && !dateStr.equals("")) {
            if (!DateUtils.matchDateString(dateStr)) return this.error("参数提交有误:date");
            date = DateUtils.parse(dateStr);
        }

        Long userplanId = Long.parseLong(userplanIdStr);
        // 获取该日期当周的第一天  
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.set(Calendar.DAY_OF_WEEK, 1);
        String firstDay = DateUtils.getDayStart(cale.getTime());
        //计算得此日历的最后一格的日期
        cale.set(Calendar.DATE, cale.get(Calendar.DATE) + 6);
        String lastDay = DateUtils.getDayEnd(cale.getTime());

        List<CourseCalendarPOJO> calendarResult = liveCourseService.getLiveCalendar(userInfo.getUserId(), userplanId, businessId, firstDay, lastDay);

        List<CourseCalendarPOJO> initCalendar = initWebCalendarWeek(calendarResult, userplanId, date);
        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/live/calendar/calendar/week " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/live/calendar/calendar/week " + (endTime - startTime) + "ms");
        }
        return this.success(initCalendar);
    }

    @ApiOperation(value = "获取直播课程页面头")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "SSOTOKEN", value = "用户 Token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userplanId", value = "学员规划id", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/live/hear", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getCourseLiveHear(HttpServletRequest request, HttpServletResponse response) {
        //UserInfoPOJO userInfo = this.getUserInfo(request,response);
        Long startTime = System.currentTimeMillis();
        String businessId = BusinessIdUtils.getBusinessId(request);
        String userplanIdStr = request.getParameter("userplanId");
        if (null == userplanIdStr) return this.error("参数提交有误:userplanId");

        Long userplanId = Long.parseLong(userplanIdStr);

        Map<String, Object> hearResult = liveCourseService.getLiveHear(userplanId, businessId);

        Long endTime = System.currentTimeMillis();
        logger.info("/learningCenter/web/live/hear " + (endTime - startTime) + "ms");
        if ((endTime - startTime) > 1000) {
            logger.error("/learningCenter/web/live/hear " + (endTime - startTime) + "ms");
        }
        return this.success(hearResult);
    }

    private List<CourseCalendarPOJO> initWebCalendarMon(List<CourseCalendarPOJO> dataList, Long userplanId, Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取该日期当月的第一天  
        Calendar cale = Calendar.getInstance();
        ;
        cale.setTime(date);
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        //获取该日期当月的第一天为周几
        int firstDayWeek = cale.get(Calendar.DAY_OF_WEEK);
        //计算得此日历的第一格的日期
        int minuDay = firstDayWeek - 1;
        if (minuDay <= 0) minuDay = 7 + minuDay;
        cale.set(Calendar.DATE, cale.get(Calendar.DATE) - minuDay);
        //用于计算某日期月份较于当前日期月份为上个月还是下个月
        Calendar caleThisMon = Calendar.getInstance();
        caleThisMon.setTime(date);
        List<CourseCalendarPOJO> pojoList = new ArrayList();
        CourseCalendarPOJO pojo = null;
        for (int i = 0; i < 42; i++) {
            pojo = new CourseCalendarPOJO();
            pojo.setUserplanId(userplanId);
            pojo.setIsAttend(0);
            pojo.setStatus(0);
            pojo.setDate(format.format(cale.getTime()));
            //set MonType标志,-1为上个月
            if (cale.get(Calendar.YEAR) == caleThisMon.get(Calendar.YEAR)) {
                int monDiff = cale.get(Calendar.MONTH) - caleThisMon.get(Calendar.MONTH);
                if (monDiff > 0) {
                    pojo.setMonType(1);
                } else if (monDiff == 0) {
                    pojo.setMonType(0);
                } else if (monDiff < 0) {
                    pojo.setMonType(-1);
                }
            } else {
                int yearDiff = cale.get(Calendar.YEAR) - caleThisMon.get(Calendar.YEAR);
                if (yearDiff > 0) {
                    pojo.setMonType(1);
                } else if (yearDiff == 0) {
                    pojo.setMonType(0);
                } else if (yearDiff < 0) {
                    pojo.setMonType(-1);
                }
            }
            cale.set(Calendar.DATE, cale.get(Calendar.DATE) + 1);
            pojoList.add(pojo);
        }

        for (CourseCalendarPOJO data : dataList) {
            for (CourseCalendarPOJO p : pojoList) {
                if (data.getDate().equals(p.getDate())) {
                    p.setIsAttend(data.getIsAttend());
                    p.setStatus(data.getStatus());
                    continue;
                }
            }
        }
        return pojoList;
    }

    private List<CourseCalendarPOJO> initWebCalendarWeek(List<CourseCalendarPOJO> dataList, Long userplanId, Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取该日期当周的第一天
        Calendar cale = Calendar.getInstance();
        ;
        cale.setTime(date);
        cale.set(Calendar.DAY_OF_WEEK, 1);

        //用于计算某日期月份较于当前日期月份为上个月还是下个月
        Calendar caleThisMon = Calendar.getInstance();
        caleThisMon.setTime(date);
        List<CourseCalendarPOJO> pojoList = new ArrayList();
        CourseCalendarPOJO pojo = null;
        for (int i = 0; i < 7; i++) {
            pojo = new CourseCalendarPOJO();
            pojo.setUserplanId(userplanId);
            pojo.setIsAttend(0);
            pojo.setStatus(0);
            pojo.setDate(format.format(cale.getTime()));
            //set MonType标志,-1为上个月
            if (cale.get(Calendar.YEAR) == caleThisMon.get(Calendar.YEAR)) {
                int monDiff = cale.get(Calendar.MONTH) - caleThisMon.get(Calendar.MONTH);
                if (monDiff > 0) {
                    pojo.setMonType(1);
                } else if (monDiff == 0) {
                    pojo.setMonType(0);
                } else if (monDiff < 0) {
                    pojo.setMonType(-1);
                }
            } else {
                int yearDiff = cale.get(Calendar.YEAR) - caleThisMon.get(Calendar.YEAR);
                if (yearDiff > 0) {
                    pojo.setMonType(1);
                } else if (yearDiff == 0) {
                    pojo.setMonType(0);
                } else if (yearDiff < 0) {
                    pojo.setMonType(-1);
                }
            }
            cale.set(Calendar.DATE, cale.get(Calendar.DATE) + 1);
            pojoList.add(pojo);
        }

        if (null != dataList && dataList.size() > 0) {
            for (CourseCalendarPOJO data : dataList) {
                for (CourseCalendarPOJO p : pojoList) {
                    if (data.getDate().equals(p.getDate())) {
                        p.setIsAttend(data.getIsAttend());
                        p.setStatus(data.getStatus());
                        continue;
                    }
                }
            }
        }
        return pojoList;
    }
}
