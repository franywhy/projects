package io.renren.service.impl;

import io.renren.dao.CoursesDao;
import io.renren.entity.CoursesEntity;
import io.renren.pojo.SelectionItem;
import io.renren.pojo.course.CoursesPOJO;
import io.renren.service.CourseLiveDetailsService;
import io.renren.service.CoursesService;
import io.renren.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@Transactional(readOnly = true)
@Service("coursesService")
public class CoursesServiceImpl implements CoursesService {
	@Autowired
	private CoursesDao coursesDao;
//	@Autowired
//	private courseLiveDetailsService courseLiveDetailsService;
	@Autowired
	private CourseLiveDetailsService courseLiveDetailsService;
	
	@Override
	public CoursesEntity queryObject(Map<String , Object> map){
		return coursesDao.queryObject(map);
	}
	
	@Override
	public List<CoursesEntity> queryList(Map<String, Object> map){
		return coursesDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return coursesDao.queryTotal(map);
	}
	
	@Override
	public void save(CoursesPOJO courses){
		//创建时间
		courses.setCreationTime(new Date());
		//en
		CoursesEntity en = CoursesPOJO.getEntity(courses);
		//保存主表
		coursesDao.save(en);
	}
	@Override
	public void update(CoursesPOJO courses){
		courses.setModifiedTime(new Date());
		CoursesEntity en = CoursesPOJO.getEntity(courses);
		coursesDao.update(en);
	}
	
	@Override
	public void delete(Long courseId){
		coursesDao.delete(courseId);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void deleteBatch(Long[] courseIds){
		coursesDao.deleteBatch(courseIds);
	}
	@Override
	public void pause(Long[] courseIds){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", courseIds);
    	map.put("status", Constant.Status.PAUSE.getValue());
		coursesDao.updateBatch(map);
	}
		
	@Override
	public void resume(Long[] courseIds){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", courseIds);
    	map.put("status", Constant.Status.RESUME.getValue());
		coursesDao.updateBatch(map);
	}

	@Override
	public List<SelectionItem> querySelectionList(String schoolId) {
		return coursesDao.querySelectionList(schoolId);
	}

	/**
	 * @param map[schoolId]=平台ID
	 * @param map[courseName]=课程名称
	 * @return  课程集合
	 */
	@Override
	public List<Map<String, Object>> queryListGrid(Map<String, Object> map) {
		return coursesDao.queryListGrid(map);
	}

	@Override
	public int queryListGridTotal(Map<String, Object> map) {
		return coursesDao.queryListGridTotal(map);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void delete(Map<String, Object> map) {
		coursesDao.delete(map);
		
	}

	@Override
	public long getCourseIdBycourseNo(Map<String, Object> map) {
		return coursesDao.getCourseIdBycourseNo(map);
	}

	@Override
	public int countCourseIdBycourseNo(Map<String, Object> map) {
		return coursesDao.countCourseIdBycourseNo(map);
	}

	@Override
	public CoursesEntity queryMid(Map<String, Object> map) {
		return coursesDao.queryMid(map);
	}

	@Override
	public List<CoursesPOJO> queryPOJOList(Map<String, Object> map) {
		return this.coursesDao.queryPOJOList(map);
	}

	@Override
	public CoursesEntity queryObjectbyCourseId(Map<String, Object> map) {
		return this.coursesDao.queryObjectbyCourseId(map);
	}

	@Override
	public List<Map<String,Object>> queryMapByCourseId(Long courseId) {
		return coursesDao.queryMapByCourseId(courseId);
	}
}
