package com.hq.adaptive.service;

import java.util.List;

import com.hq.adaptive.entity.AdlCourseEntity;
import com.hq.adaptive.pojo.query.AdlCourseQuery;

/**
 * 课程
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-06 16:48:13
 */
public interface AdlCourseService {
	/**
	 * 根据ID查询查询课程
	 * @param courseId	id
	 * @return	课程
	 */
	AdlCourseEntity queryObject(Long courseId);
	
	/**
	 * 查询课程列表
	 * @param query	查询条件
	 * @return	课程列表
	 */
	List<AdlCourseEntity> queryList(AdlCourseQuery query);
	
	/**
	 * 查询课程数量
	 * @return	课程数量
	 */
	int queryTotal(AdlCourseQuery query);
}
