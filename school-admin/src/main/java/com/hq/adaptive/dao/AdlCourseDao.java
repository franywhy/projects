package com.hq.adaptive.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hq.adaptive.entity.AdlCourseEntity;
import com.hq.adaptive.pojo.query.AdlCourseQuery;

/**
 * 课程
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-07 11:46:18
 */
public interface AdlCourseDao {
	/**
	 * 根据ID查询查询课程
	 * @param courseId	自增id
	 * @return	课程
	 */
	public AdlCourseEntity queryObject(@Param(value="courseId")Long courseId);
	
	/**
	 * 查询课程列表
	 * @param query	查询条件
	 * @return	课程列表
	 */
	public List<AdlCourseEntity> queryList(AdlCourseQuery query);

	/**
	 * 查询课程数量
	 * @return	课程数量
	 */
	public int queryTotal(AdlCourseQuery query);
}
