package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.CourseUserplanServiceDao;
import io.renren.entity.CourseUserplanServiceEntity;
import io.renren.service.CourseUserplanServiceService;
import io.renren.utils.Constant;



@Service("courseUserplanServiceService")
public class CourseUserplanServiceServiceImpl implements CourseUserplanServiceService {
	@Autowired
	private CourseUserplanServiceDao courseUserplanServiceDao;
	
	@Override
	public CourseUserplanServiceEntity queryObject(Map<String, Object> map){
		return courseUserplanServiceDao.queryObject(map);
	}
	
	@Override
	public List<CourseUserplanServiceEntity> queryList(Map<String, Object> map){
		return courseUserplanServiceDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseUserplanServiceDao.queryTotal(map);
	}
	
	@Override
	public void save(CourseUserplanServiceEntity courseUserplanService){
		courseUserplanServiceDao.save(courseUserplanService);
	}
	
	@Override
	public void update(CourseUserplanServiceEntity courseUserplanService){
		courseUserplanServiceDao.update(courseUserplanService);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		courseUserplanServiceDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		courseUserplanServiceDao.deleteBatch(map);
	}

	@Override
	public Map<String, Object> queryMap(Map<String, Object> map) {
		return this.courseUserplanServiceDao.queryMap(map);
	}

	@Override
	public List<Map<String, Object>> queryListMap(Map<String, Object> map) {
		return this.courseUserplanServiceDao.queryListMap(map);
	}
	
	
}
