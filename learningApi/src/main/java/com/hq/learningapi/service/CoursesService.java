package com.hq.learningapi.service;

import com.hq.learningapi.entity.CoursesEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 课程档案
 * @date 2018-01-25 19:37:32
 */
public interface CoursesService {

	CoursesEntity queryObject(Long courseId);

	List<CoursesEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(CoursesEntity courses);

	void update(CoursesEntity courses);

	CoursesEntity queryObjectByCourseFk(Long courseId);

	Set<String> queryCourseNoList(Long userId, String businessId);
}
