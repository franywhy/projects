package com.hq.learningapi.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hq.learningapi.entity.CourseClassplanLivesEntity;

/**
 * Created by Zhaowenwei on 2018/1/22
 */

@Repository
public interface CourseClassplanLivesDao {

	CourseClassplanLivesEntity queryObject(String classplanLiveId);

	List<CourseClassplanLivesEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(CourseClassplanLivesEntity courseClassplanLives);

	void update(CourseClassplanLivesEntity courseClassplanLives);

	List<CourseClassplanLivesEntity> queryClassPlanLiveListByCourseId(Long CourseId);
	
}
