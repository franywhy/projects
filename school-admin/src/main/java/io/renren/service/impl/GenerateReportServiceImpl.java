package io.renren.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.GenerateReportDao;
import io.renren.entity.AttendReportEntity;
import io.renren.entity.CourseUserplanEntity;
import io.renren.service.GenerateReportService;

@Service("generateReportService")
public class GenerateReportServiceImpl implements GenerateReportService {
	@Autowired
	private GenerateReportDao generateReportDao;
	
	@Override
	public List<String> queryClassplanIdList(Map<String, Object> map) {
		return generateReportDao.queryClassplanIdList(map);
	}

	@Override
	public List<CourseUserplanEntity> queryUserplanByClassplan(String classplanId) {
		return generateReportDao.queryUserplanByClassplan(classplanId);
	}

	@Override
	public AttendReportEntity queryAttendPer(Map<String, Object> map) {
		return generateReportDao.queryAttendPer(map);
	}

	@Override
	public void saveWeekReport(AttendReportEntity entity) {
		generateReportDao.saveWeekReport(entity);
	}

	@Override
	public void saveMonthReport(AttendReportEntity entity) {
		generateReportDao.saveMonthReport(entity);
	}

	@Override
	public List<Long> queryTeacherByClassplan(Map<String, Object> map) {
		return generateReportDao.queryTeacherByClassplan(map);
	}

}
