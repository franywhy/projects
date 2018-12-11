package com.hq.adaptive.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.adaptive.dao.AdlCourseDao;
import com.hq.adaptive.entity.AdlCourseEntity;
import com.hq.adaptive.pojo.query.AdlCourseQuery;
import com.hq.adaptive.service.AdlCourseService;


/**
 * 课程
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-06 16:48:13
 */
@Service("adlCourseService")
public class AdlCourseServiceImpl implements AdlCourseService {
	@Autowired
	private AdlCourseDao adlCourseDao;
	
	/**
	 * 根据ID查询查询课程
	 * @param courseId	id
	 * @return	课程
	 */
	@Override
	public AdlCourseEntity queryObject(Long courseId){
		return adlCourseDao.queryObject(courseId);
	}
	
	/**
	 * 查询课程列表
	 * @param query	查询条件
	 * @return	课程列表
	 */
	@Override
	public List<AdlCourseEntity> queryList(AdlCourseQuery query){
		return adlCourseDao.queryList(query);
	}
	
	/**
	 * 查询课程数量
	 * @return	课程数量
	 */
	@Override
	public int queryTotal(AdlCourseQuery query){
		return adlCourseDao.queryTotal(query);
	}
	
}
