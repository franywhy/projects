package com.hq.learningapi.service;

import java.util.List;
import java.util.Map;

import com.hq.learningapi.entity.CourseClassplanLivesEntity;

/**
 * Created by Zhaowenwei on 2018/1/22
 */

public interface CourseClassplanLivesService {

//	String jointURL(String token,String phaseId,String type,String courseFk,String classplanLiveId);

	CourseClassplanLivesEntity queryObject(String classplanLiveId) ;
		
	List<CourseClassplanLivesEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseClassplanLivesEntity courseClassplanLives);
	
	void update(CourseClassplanLivesEntity courseClassplanLives);
	
	List<CourseClassplanLivesEntity> queryClassPlanLiveListByCourseId(Long CourseId);
}
