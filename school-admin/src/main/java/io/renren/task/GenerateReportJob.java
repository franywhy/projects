package io.renren.task;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.renren.entity.AttendReportEntity;
import io.renren.entity.CourseUserplanEntity;
import io.renren.service.GenerateReportService;
import io.renren.utils.DateUtils;
import io.renren.utils.SpringContextUtils;
/**
 * 生成月报表
 * @class io.renren.task.GenerateMonthReportJob.java
 * @Description:
 * @author yaofeng
 * @dete 2017年12月15日
 */
@Component("io.renren.task.GenerateReportJob")
public class GenerateReportJob{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private GenerateReportService _generateReportService;
	
	public void month(Map<String,Object> params) {
		String date = (String) params.get("date");
		logger.info("GenerateMonthReportJob start==> date:{},time:{}" ,
				date,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
		
		Map<String,Object> paramsMap = new HashMap<>();
		Date startDate = null;
		Date endDate = null;
		Date generDate = date==null?new Date():DateUtils.parse(date);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(generDate);
		int month_index = cal.get(Calendar.DAY_OF_MONTH);
		if(month_index != 3){
			return;
		}
		//获取生成月报的时间范围
		cal.set(Calendar.DAY_OF_MONTH, 0);//设定日期为上个月的最后一日
		endDate = cal.getTime();//获取上个月最后一天
		cal.set(Calendar.DAY_OF_MONTH, 1);//此时日历为当月的上个月,再设定为第一天
		startDate = cal.getTime();//获取上个月第一天
		
		paramsMap.put("startDate", DateUtils.format(startDate)+" 00:00:00");
		paramsMap.put("endDate", DateUtils.format(endDate)+" 23:59:59");
		
		generateReport(paramsMap,0);
	}
	
	public void week(Map<String,Object> params) {
		String date = (String) params.get("date");
		logger.info("GenerateWeekReportJob start==> date:{},time:{}" ,
				date,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
		
		Map<String,Object> paramsMap = new HashMap<>();
		Date startDate = null;
		Date endDate = null;
		Date generDate = date==null?new Date():DateUtils.parse(date);
		int week = DateUtils.getWeek(generDate);
		//获取生成周报的时间范围
		if(week == 2){
			startDate = DateUtils.getDateBefore(generDate,3);
			endDate = DateUtils.getDateBefore(generDate,1);
		}else if(week == 5){
			startDate = DateUtils.getDateBefore(generDate,4);
			endDate = DateUtils.getDateBefore(generDate,1);
		}else{
			return;
		}
		paramsMap.put("startDate", DateUtils.format(startDate)+" 00:00:00");
		paramsMap.put("endDate", DateUtils.format(endDate)+" 23:59:59");
		
		generateReport(paramsMap,1);
	}
	
	private void generateReport(Map<String,Object> params,int type){
		GenerateReportService generateReportService = getCourseClassplanLivesService();
		try{
			//根据时间,查询范围内有课的classplanList
			List<String> classplanIdList = generateReportService.queryClassplanIdList(params);
			if(null != classplanIdList && !classplanIdList.isEmpty()){
				for(String classplanId : classplanIdList){
					params.put("classplanId", classplanId);
					List<Long> teacherIdList = generateReportService.queryTeacherByClassplan(params);//classplanId,查询这时间段内,此classplan,有授课的老师
					List<CourseUserplanEntity> userplanList = generateReportService.queryUserplanByClassplan(classplanId);
					if(null != userplanList && !userplanList.isEmpty()){
						//根据classplanId,查询关联有该排课的学员规划
						for(CourseUserplanEntity userplan : userplanList){
							params.put("userId", userplan.getUserId());
							if(null != teacherIdList && !teacherIdList.isEmpty()){
								for(Long teacherId : teacherIdList){
									AttendReportEntity report = new AttendReportEntity();
									report.setUserId(userplan.getUserId());
									report.setClassId(userplan.getClassId());
									report.setAreaId(userplan.getAreaId());
									report.setProfessionId(userplan.getProfessionId());
									report.setLevelId(userplan.getLevelId());
									report.setGoodsId(userplan.getCommodityId());
									report.setClassTypeId(userplan.getClassTypeId());
									report.setClassplanId(classplanId);
									report.setTeacherId(teacherId);
									report.setCreateTime(new Date());
									params.put("teacherId", teacherId);
									AttendReportEntity result = generateReportService.queryAttendPer(params);
									if(null != result){
										report.setLivePer(result.getLivePer());
										report.setAttendPer(result.getAttendPer());
									}else{
										report.setLivePer(new BigDecimal(0));
										report.setAttendPer(new BigDecimal(0));
									}
									if(type == 0){
										generateReportService.saveMonthReport(report);
									}else if(type == 1){
										generateReportService.saveWeekReport(report);
									}
								}
							}
							
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("execute:{}",e.toString());
		}
		
	}
	
	//GET SERVICE
	private GenerateReportService getCourseClassplanLivesService(){
		if(null == _generateReportService)_generateReportService = (GenerateReportService)SpringContextUtils.getBean("generateReportService");
		return _generateReportService;
	}
}
