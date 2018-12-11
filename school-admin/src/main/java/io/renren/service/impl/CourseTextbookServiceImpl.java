package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.CourseTextbookDao;
import io.renren.entity.CourseTextbookDetailEntity;
import io.renren.entity.CourseTextbookEntity;
import io.renren.service.CourseTextbookService;
import io.renren.utils.Constant;



@Service("courseTextbookService")
public class CourseTextbookServiceImpl implements CourseTextbookService {
	@Autowired
	private CourseTextbookDao courseTextbookDao;
	
	@Override
	public CourseTextbookDetailEntity queryObject(Map<String, Object> map){
		return courseTextbookDao.queryObject(map);
	}
	
	@Override
	public List<CourseTextbookDetailEntity> queryList(Map<String, Object> map){
		return courseTextbookDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseTextbookDao.queryTotal(map);
	}
	
	@Override
	public void save(CourseTextbookEntity courseTextbook){
		courseTextbookDao.save(courseTextbook);
	}
	
	@Override
	public void update(CourseTextbookEntity courseTextbook){
		courseTextbookDao.update(courseTextbook);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		courseTextbookDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		courseTextbookDao.deleteBatch(map);
	}

	@Override
	public void pause(Long[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.PAUSE.getValue());
    	courseTextbookDao.updateBatch(map);
	}

	@Override
	public void resume(Long[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.RESUME.getValue());
    	courseTextbookDao.updateBatch(map);
	}
	
	
}
