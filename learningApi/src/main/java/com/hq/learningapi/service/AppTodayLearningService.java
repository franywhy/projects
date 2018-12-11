package com.hq.learningapi.service;

import javax.servlet.http.HttpServletRequest;

import com.hq.learningapi.entity.CourseClassplanLivesEntity;

/**
 * Created by Zhaowenwei on 2018/1/22
 */

public interface AppTodayLearningService {

	String jointURL(String token,String phaseId,String type,String courseFk,String classplanLiveId,HttpServletRequest request);

	CourseClassplanLivesEntity getCourseClassplanLives(String classplanLiveId) ;
		

		
}
