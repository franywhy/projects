package com.hq.learningcenter.school.service;

import java.util.Map;

import com.hq.learningcenter.school.entity.CourseClassplanLivesEntity;

/**
 * 排课计划表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 09:58:34
 */
public interface CourseClassplanLivesService {
	
	CourseClassplanLivesEntity queryByLiveId(Map<String,Object> map);
	
	void update(CourseClassplanLivesEntity classplanLive);
}
