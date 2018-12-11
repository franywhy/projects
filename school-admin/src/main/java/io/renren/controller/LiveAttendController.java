package io.renren.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.pojo.log.LiveAttendPOJO;
import io.renren.pojo.log.LogStudentAttendPOJO;
import io.renren.service.LogStudentAttendService;
import io.renren.utils.DateUtils;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

/**
 * 考勤统计
 * 班主任角色观看
 * @author shanyaofeng
 * @date 2017-12-14
 */
@Controller
@RequestMapping("liveAttend")
public class LiveAttendController extends AbstractController {
	@Autowired
	private LogStudentAttendService logStudentAttendService;
	/**
	 * 按班级统计
	 */
	@ResponseBody
	@RequestMapping("/queryGroupByClass")
	public R queryGroupByClass(HttpServletRequest request){
		
		Long classTeacherId = null;//班主任
		Long classId = null;//班级PK
		
		Long areaId = null;//省份PK
		Long professionId = null;//专业PK
		Long levelId = null;//层次PK
		String statusId = null;
		Integer reportType = null;
		String startDate = null;
		String endDate = null;
		try {
			classTeacherId = ServletRequestUtils.getLongParameter(request, "classTeacherId", 0);
			classId = ServletRequestUtils.getLongParameter(request, "classId", 0);
			
			areaId = ServletRequestUtils.getLongParameter(request, "areaId", 0);
			professionId = ServletRequestUtils.getLongParameter(request, "professionId", 0);
			levelId = ServletRequestUtils.getLongParameter(request, "levelId", 0);
			statusId = ServletRequestUtils.getStringParameter(request, "statusId");
			reportType = ServletRequestUtils.getIntParameter(request, "reportType", 0);
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
		map.put("professionId", professionId);
		map.put("levelId", levelId);
		map.put("statusId", statusId);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		
		List<LiveAttendPOJO> logStudentAttenList = null;
		if(reportType == 1){
			logStudentAttenList = logStudentAttendService.queryWeekGroupByClass(map);
		}else if(reportType == 2){
			logStudentAttenList = logStudentAttendService.queryMonthGroupByClass(map);
		}else{
			logStudentAttenList = logStudentAttendService.queryWeekGroupByClass(map);
			logStudentAttenList.addAll(logStudentAttendService.queryMonthGroupByClass(map));
		}
		//小计
		if(null != logStudentAttenList && !logStudentAttenList.isEmpty()){
			BigDecimal attendPer = new BigDecimal(0);
			BigDecimal divParams = new BigDecimal(100);
			for(LiveAttendPOJO pojo:logStudentAttenList){
				pojo.setLivePerTxt(pojo.getLivePer().multiply(divParams).setScale(2,BigDecimal.ROUND_HALF_UP) + "%");
				pojo.setAttendPerTxt(pojo.getAttendPer().multiply(divParams).setScale(2,BigDecimal.ROUND_HALF_UP) + "%");
				//小计人数
				attendPer = attendPer.add(pojo.getAttendPer());
			}
			LiveAttendPOJO totalPOJO = new LiveAttendPOJO();
			totalPOJO.setClassId("小计");
			totalPOJO.setClassName("--");
			totalPOJO.setReportType("--");
			totalPOJO.setReportCreateTime("--");
			totalPOJO.setAreaName("--");
			totalPOJO.setProfessionName("--");
			totalPOJO.setLevelName("--");
			totalPOJO.setClassTeacherName("--");
			//查询班级人数
			Long classtotal = logStudentAttendService.queryClassTotal(map);
			totalPOJO.setTotalStudent(classtotal);
			totalPOJO.setCheckTotalStudent(classtotal);
			totalPOJO.setLivePerTxt("--");
			totalPOJO.setAttendPer(attendPer);
			totalPOJO.setAttendPerTxt(attendPer.divide(new BigDecimal(logStudentAttenList.size()),4).multiply(divParams).setScale(2,BigDecimal.ROUND_HALF_UP) + "%");
			logStudentAttenList.add(totalPOJO);
		}
		int total = 0;
		if(null != logStudentAttenList && !logStudentAttenList.isEmpty()){
			total = logStudentAttenList.size();
		}
		PageUtils pageUtil = new PageUtils(logStudentAttenList, total, request);	
		return R.ok().put(pageUtil);
	}
	
	/**
	 * 按商品统计
	 */
	@ResponseBody
	@RequestMapping("/queryGroupByGoods")
	public R queryGroupByGoods(HttpServletRequest request){
		Long goodsId = null;//商品
		String statusId = null;
		Integer reportType = null;
		String startDate = null;
		String endDate = null;
		try {
			goodsId = ServletRequestUtils.getLongParameter(request, "goodsId", 0);
			statusId = ServletRequestUtils.getStringParameter(request, "statusId");
			reportType = ServletRequestUtils.getIntParameter(request, "reportType", 0);
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
		map.put("goodsId", goodsId);
		map.put("statusId", statusId);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		
		List<LiveAttendPOJO> logStudentAttenList = null;
		if(reportType == 1){
			logStudentAttenList = logStudentAttendService.queryWeekGroupByGoods(map);
		}else if(reportType == 2){
			logStudentAttenList = logStudentAttendService.queryMonthGroupByGoods(map);
		}else{
			logStudentAttenList = logStudentAttendService.queryWeekGroupByGoods(map);
			logStudentAttenList.addAll(logStudentAttendService.queryMonthGroupByGoods(map));
		}
		//小计
		if(null != logStudentAttenList && !logStudentAttenList.isEmpty()){
			Long totalStudent = 0L;
			Long checkTotalStudent = 0L;
			BigDecimal attendPer = new BigDecimal(0);
			BigDecimal divParams = new BigDecimal(100);
			for(LiveAttendPOJO pojo:logStudentAttenList){
				pojo.setLivePerTxt(pojo.getLivePer().multiply(divParams).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
				pojo.setAttendPerTxt(pojo.getAttendPer().multiply(divParams).setScale(2,BigDecimal.ROUND_HALF_UP) + "%");
				totalStudent += pojo.getTotalStudent();
				checkTotalStudent += pojo.getCheckTotalStudent();
				attendPer = attendPer.add(pojo.getAttendPer());
			}
			LiveAttendPOJO totalPOJO = new LiveAttendPOJO();
			totalPOJO.setGoodsId("小计");
			totalPOJO.setGoodsName("--");
			totalPOJO.setClassTypeName("--");
			totalPOJO.setReportCreateTime("--");
			totalPOJO.setProfessionName("--");
			totalPOJO.setLevelName("--");
			totalPOJO.setTotalStudent(totalStudent);
			totalPOJO.setCheckTotalStudent(checkTotalStudent);
			totalPOJO.setLivePerTxt("--");
			totalPOJO.setAttendPer(attendPer);
			totalPOJO.setAttendPerTxt(attendPer.divide(new BigDecimal(logStudentAttenList.size()),4).multiply(divParams).setScale(2,BigDecimal.ROUND_HALF_UP) + "%");
			logStudentAttenList.add(totalPOJO);
		}
		int total = 0;
		if(null != logStudentAttenList && !logStudentAttenList.isEmpty()){
			total = logStudentAttenList.size();
		}
		PageUtils pageUtil = new PageUtils(logStudentAttenList, total, request);
		return R.ok().put(pageUtil);
	}
	
	/**
	 * 按排课统计
	 */
	@ResponseBody
	@RequestMapping("/queryGroupByClassplan")
	public R queryGroupByClassplan(HttpServletRequest request){
		
		Long teacherId = null;//班主任
		String classplanId = null;//排课
		String statusId = null;
		Integer reportType = null;
		String startDate = null;
		String endDate = null;
		try {
			teacherId = ServletRequestUtils.getLongParameter(request, "teacherId", 0);
			classplanId = ServletRequestUtils.getStringParameter(request, "classplanId");
			statusId = ServletRequestUtils.getStringParameter(request, "statusId");
			reportType = ServletRequestUtils.getIntParameter(request, "reportType", 0);
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
		map.put("teacherId", teacherId);
		map.put("classplanId", classplanId);
		map.put("statusId", statusId);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		
		List<LiveAttendPOJO> logStudentAttenList = null;
		if(reportType == 1){
			logStudentAttenList = logStudentAttendService.queryWeekGroupByClassplan(map);
		}else if(reportType == 2){
			logStudentAttenList = logStudentAttendService.queryMonthGroupByClassplan(map);
		}else{
			logStudentAttenList = logStudentAttendService.queryWeekGroupByClassplan(map);
			logStudentAttenList.addAll(logStudentAttendService.queryMonthGroupByClassplan(map));
		}
		//小计
		if(null != logStudentAttenList && !logStudentAttenList.isEmpty()){
			List<String> countClassplanIdList = new ArrayList<>();
			boolean classplanIdExist = false;
			Long totalLive = 0L;
			Long totalStudent = 0L;
			Long checkTotalStudent = 0L;
			BigDecimal attendPer = new BigDecimal(0);
			BigDecimal divParams = new BigDecimal(100);
			for(LiveAttendPOJO pojo:logStudentAttenList){
				pojo.setLivePerTxt(pojo.getLivePer().multiply(divParams).setScale(2,BigDecimal.ROUND_HALF_UP) + "%");
				pojo.setAttendPerTxt(pojo.getAttendPer().multiply(divParams).setScale(2,BigDecimal.ROUND_HALF_UP) + "%");
				//小计人数
				if(countClassplanIdList.isEmpty()){
					countClassplanIdList.add(pojo.getClassplanId());
					totalLive += pojo.getTotalLive();
					totalStudent += pojo.getTotalStudent();
					checkTotalStudent += pojo.getCheckTotalStudent();
				}else{
					for(String countClassplanId : countClassplanIdList){
						if(countClassplanId.equals(pojo.getClassplanId())){
							classplanIdExist = true;
							break;
						}
					}
					if(!classplanIdExist){
						countClassplanIdList.add(pojo.getClassplanId());
						totalLive += pojo.getTotalLive();
						totalStudent += pojo.getTotalStudent();
						checkTotalStudent += pojo.getCheckTotalStudent();
					}
					classplanIdExist = false;
				}
				
				attendPer = attendPer.add(pojo.getAttendPer());
			}
			LiveAttendPOJO totalPOJO = new LiveAttendPOJO();
			totalPOJO.setClassplanName("小计");
			totalPOJO.setTeacherName("--");
			totalPOJO.setReportType("--");
			totalPOJO.setReportCreateTime("--");
			totalPOJO.setStartClassTime("--");
			totalPOJO.setTotalLive(totalLive);
			totalPOJO.setTotalStudent(totalStudent);
			totalPOJO.setCheckTotalStudent(checkTotalStudent);
			totalPOJO.setLivePerTxt("--");
			totalPOJO.setAttendPer(attendPer);
			totalPOJO.setAttendPerTxt(attendPer.divide(new BigDecimal(logStudentAttenList.size()),4).multiply(divParams).setScale(2,BigDecimal.ROUND_HALF_UP) + "%");
			logStudentAttenList.add(totalPOJO);
		}
		int total = 0;
		if(null != logStudentAttenList && !logStudentAttenList.isEmpty()){
			total = logStudentAttenList.size();
		}
		PageUtils pageUtil = new PageUtils(logStudentAttenList, total, request);	
		return R.ok().put(pageUtil);
	}
}
