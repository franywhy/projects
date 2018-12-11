package com.hq.learningapi.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.learningapi.dao.CourseClassplanLivesDao;
import com.hq.learningapi.entity.CourseClassplanLivesEntity;
import com.hq.learningapi.service.CourseClassplanLivesService;

/**
 * Created by Zhaowenwei on 2018/1/22
 */

@Service("courseClassplanLivesService")
public class CourseClassplanLivesServiceImpl implements CourseClassplanLivesService {
	@Autowired
	private CourseClassplanLivesDao courseClassplanLivesDao;
	
	@Override
	public CourseClassplanLivesEntity queryObject(String classplanLiveId){
		return courseClassplanLivesDao.queryObject(classplanLiveId);
	}
	
	@Override
	public List<CourseClassplanLivesEntity> queryList(Map<String, Object> map){
		return courseClassplanLivesDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseClassplanLivesDao.queryTotal(map);
	}
	
	@Override
	public void save(CourseClassplanLivesEntity courseClassplanLives){
		courseClassplanLivesDao.save(courseClassplanLives);
	}
	
	@Override
	public void update(CourseClassplanLivesEntity courseClassplanLives){
		courseClassplanLivesDao.update(courseClassplanLives);
	}

	@Override
	public List<CourseClassplanLivesEntity> queryClassPlanLiveListByCourseId(Long CourseId) {
		return courseClassplanLivesDao.queryClassPlanLiveListByCourseId(CourseId);
	}
	
	
	

}
