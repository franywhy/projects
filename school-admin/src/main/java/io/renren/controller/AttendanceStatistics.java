package io.renren.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.service.AttendanceStatisticsService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

/**
 * 考勤统计
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-26 09:22:46
 */
@Controller
@RequestMapping("attendanceStatistics")
public class AttendanceStatistics extends AbstractController {
	@Autowired
	private AttendanceStatisticsService attendanceStatisticsService;
	/**
	 * 考勤统计
	 */
	@ResponseBody
	@RequestMapping("/attendance")
	@RequiresPermissions("attendanceStatistics:attendance")
	public R attendanceStatistics(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		
		stringQuery(map, request, "classplanId");
		longQuery(map, request, "areaId");
		longQuery(map, request, "professionId");
		longQuery(map, request, "classTypeId");
		longQuery(map, request, "classId");
		longQuery(map, request, "classTeacherId");
		stringQuery(map, request, "mobile");
		//查询列表数据
		List<Map<String,Object>> attendanceList = attendanceStatisticsService.queryAttendanceList(map);
		map.put("classTypeId", null);
		longQuery(map, request, "classTypeId");
		int total = attendanceStatisticsService.queryAttendanceTotal(map);
		PageUtils pageUtil = new PageUtils(attendanceList, total, request);	
		return R.ok().put(pageUtil);
	}
	/**
	 * 考勤详情
	 * @throws ServletRequestBindingException 
	 */
	@ResponseBody
	@RequestMapping("/detailsList")
	@RequiresPermissions("attendanceStatistics:attendance")
	public R detailsList(HttpServletRequest request) {
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "classplanId");//由前台传来的排课计划id
		longQuery(map, request, "userPlanId");//由前台传来的学员规划id
		longQuery(map, request, "userId");//由前台传来的学员id
		longQuery(map, request, "classTypeId");//由前台传来的班型id
		map.put("dateTime", new Date());//当前时间
		//查询列表数据
		List<Map<String,Object>> detailsList = attendanceStatisticsService.queryDetailsList(map);
		int total = attendanceStatisticsService.queryDetailsTotal(map);
		PageUtils pageUtil = new PageUtils(detailsList, total, request);	
		return R.ok().put(pageUtil);
	}
}
