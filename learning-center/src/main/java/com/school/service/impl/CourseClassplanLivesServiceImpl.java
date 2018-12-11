package com.school.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.dao.CourseClassplanLivesDao;
import com.school.entity.CourseClassplanLivesEntity;
import com.school.service.CourseClassplanLivesService;

@Service("courseClassplanService")
public class CourseClassplanLivesServiceImpl implements CourseClassplanLivesService {

	@Autowired
	private CourseClassplanLivesDao courseClassplanLivesDao;
	
	@Override
	public CourseClassplanLivesEntity queryByLiveId(Map<String, Object> map) {
		return courseClassplanLivesDao.queryByLiveId(map);
	}

	@Override
	public void update(CourseClassplanLivesEntity classplanLive) {
		courseClassplanLivesDao.update(classplanLive);
	}
}
