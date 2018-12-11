package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.CourseUserstopDao;
import io.renren.entity.CourseUserstopEntity;
import io.renren.service.CourseUserstopService;
import io.renren.utils.Constant;



@Service("courseUserstopService")
public class CourseUserstopServiceImpl implements CourseUserstopService {
	@Autowired
	private CourseUserstopDao courseUserstopDao;
	
	@Override
	public CourseUserstopEntity queryObject(Map<String, Object> map){
		return courseUserstopDao.queryObject(map);
	}
	
	@Override
	public List<CourseUserstopEntity> queryList(Map<String, Object> map){
		return courseUserstopDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseUserstopDao.queryTotal(map);
	}
	
	@Override
	public void save(CourseUserstopEntity courseUserstop){
		courseUserstopDao.save(courseUserstop);
	}
	
	@Override
	public void update(CourseUserstopEntity courseUserstop){
		courseUserstopDao.update(courseUserstop);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		courseUserstopDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		courseUserstopDao.deleteBatch(map);
	}

}
