package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.CourseGuideDao;
import io.renren.entity.CourseGuideEntity;
import io.renren.service.CourseGuideService;
import io.renren.utils.Constant;

@Transactional
@Service("courseGuideService")
public class CourseGuideServiceImpl implements CourseGuideService {
	@Autowired
	private CourseGuideDao courseGuideDao;
	
	@Override
	public Map<String, Object> queryMap(Map<String, Object> map){
		return courseGuideDao.queryMap(map);
	}
	
	@Override
	public List<Map<String, Object>> queryListMap(Map<String, Object> map){
		return courseGuideDao.queryListMap(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseGuideDao.queryTotal(map);
	}
	
	@Override
	public void save(CourseGuideEntity courseGuide){
		courseGuideDao.save(courseGuide);
	}
	
	@Override
	public void update(CourseGuideEntity courseGuide){
		courseGuideDao.update(courseGuide);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		courseGuideDao.delete(map);
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Override
	public void deleteBatch(Map<String, Object> map){
		courseGuideDao.deleteBatch(map);
	}

	@Override
	public int checkProfession(long id) {
		return courseGuideDao.checkProfession(id);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void pause(Long[] numbers) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", numbers);
    	map.put("status", Constant.Status.PAUSE.getValue());
		courseGuideDao.updateBatch(map);
		
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void resume(Long[] numbers) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", numbers);
    	map.put("status", Constant.Status.RESUME.getValue());
		courseGuideDao.updateBatch(map);
	}
	
	
}
