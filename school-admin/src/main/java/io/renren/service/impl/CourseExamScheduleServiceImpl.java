package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import io.renren.dao.CourseExamScheduleDao;
import io.renren.entity.CourseExamScheduleEntity;
import io.renren.pojo.CourseExamSchedulePOJO;
import io.renren.service.CourseExamScheduleService;
import io.renren.utils.Constant;



@Service("courseExamScheduleService")
public class CourseExamScheduleServiceImpl implements CourseExamScheduleService {
	@Autowired
	private CourseExamScheduleDao courseExamScheduleDao;
	
	@Override
	public CourseExamScheduleEntity queryObject(Map<String, Object> map){
		return courseExamScheduleDao.queryObject(map);
	}
	
	@Override
	public List<CourseExamScheduleEntity> queryList(Map<String, Object> map){
		return courseExamScheduleDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseExamScheduleDao.queryTotal(map);
	}
	
	@Override
	public void save(CourseExamScheduleEntity courseExamSchedule){
		courseExamSchedule.setDr(0);
		//创建时间
		courseExamSchedule.setCreateTime(new Date());
		//修改时间
		courseExamSchedule.setModifyTime(courseExamSchedule.getCreateTime());
		courseExamScheduleDao.save(courseExamSchedule);
	}
	
	@Override
	public void update(CourseExamScheduleEntity courseExamSchedule){
		//修改时间
		courseExamSchedule.setModifyTime(new Date());
		courseExamScheduleDao.update(courseExamSchedule);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		courseExamScheduleDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		courseExamScheduleDao.deleteBatch(map);
	}

	@Override
	public List<CourseExamSchedulePOJO> queryPojoList(Map<String, Object> map) {
		return this.courseExamScheduleDao.queryPojoList(map);
	}

	@Override
	public CourseExamSchedulePOJO queryPojoObject(Map<String, Object> map) {
		return this.courseExamScheduleDao.queryPojoObject(map);
	}
	
	
}
