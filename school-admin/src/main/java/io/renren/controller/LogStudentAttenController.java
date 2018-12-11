package io.renren.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.renren.pojo.log.LogStudentAttentLiveLogDetails;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.pojo.log.LiveAttendPOJO;
import io.renren.pojo.log.LogStudentAttendPOJO;
import io.renren.service.LogStudentAttenService;
import io.renren.service.LogStudentAttendService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

/**
 * 考勤统计
 * 班主任角色观看
 * 每个学员
 * @author shihongjie
 * @date 2017-08-23
 */
@Controller
@RequestMapping("logStudentAtten")
public class LogStudentAttenController extends AbstractController {
	@Autowired
	private LogStudentAttendService logStudentAttendService;
	@Autowired
	private LogStudentAttenService logStudentAttenService;
	/**
	 * 考勤统计
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R list(HttpServletRequest request){
		
		Long classTeacherId = null;//班主任
		Long classId = null;//班级PK
		
		Long areaId = null;//省份PK
		Long deptId = null;//部门PK
		String statusId = null;
		String date = null;
		try {
			classTeacherId = ServletRequestUtils.getLongParameter(request, "classTeacherId", 0);
			classId = ServletRequestUtils.getLongParameter(request, "classId", 0);
			
			areaId = ServletRequestUtils.getLongParameter(request, "areaId", 0);
			deptId = ServletRequestUtils.getLongParameter(request, "deptId", 0);
			statusId = ServletRequestUtils.getStringParameter(request, "statusId");
			
			date = ServletRequestUtils.getStringParameter(request, "date");
		} catch (ServletRequestBindingException e) {
			e.printStackTrace();
		}
		if(StringUtils.isBlank(date)){
			return R.error("请选择日期！");
		}
		//查询列表数据
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("classTeacherId", classTeacherId);
		map.put("areaId", areaId);
		map.put("classId", classId);
		map.put("deptId", deptId);
		map.put("statusId", statusId);
		map.put("startDate", date+" 00:00:00");
		map.put("endDate", date+" 23:59:59");
		List<LogStudentAttendPOJO> logStudentAttenList = logStudentAttendService.queryLiveAttendList(map);
		int total = 0;
		if(null != logStudentAttenList && !logStudentAttenList.isEmpty()){
			total = logStudentAttenList.size();
		}
		PageUtils pageUtil = new PageUtils(logStudentAttenList, total, request);	
		return R.ok().put(pageUtil);
	}
	
	@ResponseBody
	@RequestMapping("/listByMobile")
	public R listByMobile(HttpServletRequest request){
		String mobile = null; 
		
		String startDate = null;
		String endDate = null;
		try {
			mobile = ServletRequestUtils.getStringParameter(request, "mobile");
			startDate = ServletRequestUtils.getStringParameter(request, "startDate");
			endDate = ServletRequestUtils.getStringParameter(request, "endDate");
		} catch (ServletRequestBindingException e) {
			e.printStackTrace();
		}
		if(StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)){
			return R.error("请选择日期！");
		}
		//根据学院手机号查询列表数据
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mobile", mobile);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		List<LogStudentAttendPOJO> logStudentAttenList = logStudentAttendService.queryLiveAttendList(map);
		int total = 0;
		if(null != logStudentAttenList && !logStudentAttenList.isEmpty()){
			total = logStudentAttenList.size();
		}
		PageUtils pageUtil = new PageUtils(logStudentAttenList, total, request);
		return R.ok().put(pageUtil);
	}
	
	@ResponseBody
	@RequestMapping("/listByRequirement")
	public R listByRequirement(HttpServletRequest request){
		
		Long classTeacherId = null;//班主任
		Long classId = null;//班级PK
		
		Long areaId = null;//省份PK
		Long deptId = null;//部门PK
		String statusId = null;
		String startDate = null;
		String endDate = null;
		try {
			classTeacherId = ServletRequestUtils.getLongParameter(request, "classTeacherId", 0);
			classId = ServletRequestUtils.getLongParameter(request, "classId", 0);
			
			areaId = ServletRequestUtils.getLongParameter(request, "areaId", 0);
			deptId = ServletRequestUtils.getLongParameter(request, "deptId", 0);
			statusId = ServletRequestUtils.getStringParameter(request, "statusId");
			
			startDate = ServletRequestUtils.getStringParameter(request, "startDate");
			endDate = ServletRequestUtils.getStringParameter(request, "endDate");
		} catch (ServletRequestBindingException e) {
			e.printStackTrace();
		}
		if(StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)){
			return R.error("请选择日期！");
		}
		//查询列表数据
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("classTeacherId", classTeacherId);
		map.put("areaId", areaId);
		map.put("classId", classId);
		map.put("deptId", deptId);
		map.put("statusId", statusId);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		List<LogStudentAttendPOJO> logStudentAttenList = logStudentAttendService.queryLiveAttendList(map);
		int total = 0;
		if(null != logStudentAttenList && !logStudentAttenList.isEmpty()){
			total = logStudentAttenList.size();
		}
		PageUtils pageUtil = new PageUtils(logStudentAttenList, total, request);	
		return R.ok().put(pageUtil);
	}
	
	/*
	@Description:查看学员进入直播间日志
	@Author:DL
	@Date:17:30 2017/11/22
	@params:
	 * @param null
	*/
	@RequestMapping(value = "logDetails")
    @ResponseBody
    public R logDetalis(HttpServletRequest request){
        Long userplanId = null;//学员规划
        String startDateString = null;//起始时间
        String endDateString = null;//结束时间
        userplanId = ServletRequestUtils.getLongParameter(request, "userplanId", 0);
        try {
             startDateString = ServletRequestUtils.getStringParameter(request, "startDate");
             endDateString = ServletRequestUtils.getStringParameter(request, "endDate");
        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
        }
        if(userplanId == 0){
            return R.error("请选择某一学员(规划)信息!");
        }
        List<LogStudentAttentLiveLogDetails> logDetailsList = logStudentAttenService.queryLogStudentAttenLiveLogDetails(userplanId, startDateString, endDateString);
        //查询列表数据
        int total = 0;
        if(null != logDetailsList && !logDetailsList.isEmpty()){
            total = logDetailsList.size();
        }
        PageUtils pageUtil = new PageUtils(logDetailsList, total, request);
        return R.ok().put(pageUtil);
    }
}
