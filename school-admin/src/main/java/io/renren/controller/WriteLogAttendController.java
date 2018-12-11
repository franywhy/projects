package io.renren.controller;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import io.renren.common.doc.SysLog;
import io.renren.entity.CourseClassplanLivesEntity;
import io.renren.entity.LogGenseeWatchEntity;
import io.renren.service.CourseClassplanLivesService;
import io.renren.service.LogGenseeWatchService;
import io.renren.service.UsersService;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.R;

/**
 * 考勤统计数据录入
 * @author shanyaofeng
 * @date 2017-11-01
 */
@Controller
@RequestMapping("logStudentAtten")
public class WriteLogAttendController extends AbstractController {
	@Autowired
	private LogGenseeWatchService logGenseeWatchService;
	@Autowired
	private CourseClassplanLivesService courseClassplanLivesService;
	@Autowired
	private UsersService usersService;
	
	/**
	 * 分数登记EXCEL批量导入
	 * @throws Exception 
	 * */
	@SysLog("考勤数据EXCEL批量导入")
	@ResponseBody
	@RequestMapping("/getExcelLogAttendData")
	public R getExcelCourseScoreRecordData(String classplanLiveId, HttpServletRequest request) throws Exception {
		try{
			Map<String,Object> classplanLiveMap = new HashMap<>();
			classplanLiveMap.put("classplanLiveId", classplanLiveId);
			classplanLiveMap.put("dr", 0);
			CourseClassplanLivesEntity classplanLiveEntity = courseClassplanLivesService.queryByClassPlanLiveId(classplanLiveMap);
			
			MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
			MultipartFile file = mpReq.getFile("file_data");
			FileInputStream fio = (FileInputStream) file.getInputStream();
			List<String[]> dataList = ExcelReaderJXL.readExcel(fio);
			List<LogGenseeWatchEntity> logGenseeWatchList = new ArrayList<LogGenseeWatchEntity>();
			if(null != dataList && dataList.size() > 1){
				for(int i=1;i<dataList.size();i++){
					Map<String,Object> usersMap = new HashMap<>();
					usersMap.put("mobile", dataList.get(i)[0].trim());
					Integer userId = usersService.queryUserId(usersMap);
					if(null != userId){
						LogGenseeWatchEntity logGenseeWatch = new LogGenseeWatchEntity();
						logGenseeWatch.setUserId(Long.valueOf(userId.toString()));
						logGenseeWatch.setFullDur(classplanLiveEntity.getEndTime().getTime() - classplanLiveEntity.getStartTime().getTime());
						logGenseeWatch.setWatchDur(classplanLiveEntity.getEndTime().getTime() - classplanLiveEntity.getStartTime().getTime());
						logGenseeWatch.setVideoDur(0L);
						logGenseeWatch.setLiveDur(0L);
						logGenseeWatch.setAttendPer(BigDecimal.valueOf(Double.valueOf(dataList.get(i)[1])));
						logGenseeWatch.setBusinessId(classplanLiveId);
						logGenseeWatch.setCreateTime(new Date());
						logGenseeWatchList.add(logGenseeWatch);
					}
				}
				
				logGenseeWatchService.saveOrUpdateBatch(logGenseeWatchList);
				
			}
			return R.ok();
		}catch(Exception e){
			e.printStackTrace();
			return R.error("保存失败");
		}
	}
}
