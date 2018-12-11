package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.CourseExamTimeDao;
import io.renren.entity.CourseExamTimeEntity;
import io.renren.service.CourseExamTimeService;
import io.renren.utils.Constant;



@Service("courseExamTimeService")
public class CourseExamTimeServiceImpl implements CourseExamTimeService {
	@Autowired
	private CourseExamTimeDao courseExamTimeDao;
	
	@Override
	public CourseExamTimeEntity queryObject(Map<String, Object> map){
		return courseExamTimeDao.queryObject(map);
	}
	
	@Override
	public List<CourseExamTimeEntity> queryList(Map<String, Object> map){
		return courseExamTimeDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseExamTimeDao.queryTotal(map);
	}
	
	@Override
	public void save(CourseExamTimeEntity courseExamTime){
		courseExamTimeDao.save(courseExamTime);
	}
	
	@Override
	public void update(CourseExamTimeEntity courseExamTime){
		courseExamTimeDao.update(courseExamTime);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		courseExamTimeDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		courseExamTimeDao.deleteBatch(map);
	}
	
	
}
