package com.hq.learningcenter.school.service.impl;

import java.util.Map;

import com.hq.learningcenter.school.dao.CourseClassplanLivesDao;
import com.hq.learningcenter.school.entity.CourseClassplanLivesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.learningcenter.school.service.CourseClassplanLivesService;

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
