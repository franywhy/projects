package com.hq.learningapi.dao;

import com.hq.learningapi.entity.CoursesEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 课程档案
 * 
 * @author zhaowenwei
 * @date 2018-01-25 19:37:32
 */
@Repository
public interface CoursesDao {
	CoursesEntity queryObject(Long courseId);

	List<CoursesEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(CoursesEntity courses);

	void update(CoursesEntity courses);
	
	CoursesEntity queryObjectByCourseFk(Long courseFk);

	Set<String> queryCourseByOrder(@Param("commodityId") Long commodityId,
								   @Param("areaId") Long areaId,
								   @Param("dr") Integer dr,
								   @Param("valid") Integer valid);
}
