package io.renren.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.CourseMaterialDao;
import io.renren.dao.SysCheckQuoteDao;
import io.renren.entity.CourseMaterialEntity;
import io.renren.entity.CoursesEntity;
import io.renren.pojo.coursematerial.CourserMaterialPOJO;
import io.renren.service.CourseMaterialService;
import io.renren.service.CoursesService;


@Transactional(readOnly = true)
@Service("courseMaterialService")
public class CourseMaterialServiceImpl implements CourseMaterialService {
	@Autowired
	private CourseMaterialDao courseMaterialDao;
	@Autowired
	private SysCheckQuoteDao sysCheckQuoteDao;
	@Autowired
	private CoursesService coursesService;
	
	@Override
	public CourseMaterialEntity queryObject(Map<String, Object> map){
		return courseMaterialDao.queryObject(map);
	}
	
	@Override
	public List<Map<String, Object>> queryListMap(Map<String, Object> map){
		return courseMaterialDao.queryListMap(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseMaterialDao.queryTotal(map);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void save(CourserMaterialPOJO courseMaterial){

		//创建时间
		courseMaterial.setCreationTime(new Date(System.currentTimeMillis()));
		//修改时间
		courseMaterial.setModifiedTime(new Date(System.currentTimeMillis()));
		//en
		CourseMaterialEntity cme = CourserMaterialPOJO.getInstance(courseMaterial);
		//课程获取产品线ID
		Map<String , Object> queryCourseMap = new HashMap<>();
		queryCourseMap.put("courseId", cme.getCourseId());
		CoursesEntity courses = coursesService.queryObject(queryCourseMap);
		cme.setProductId(courses.getProductId());
		//保存主表
		courseMaterialDao.save(cme);
	
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void update(CourserMaterialPOJO courseMaterial){
		//修改时间
		courseMaterial.setModifiedTime(new Date());
		CourseMaterialEntity cme = CourserMaterialPOJO.getInstance(courseMaterial);
		//课程获取产品线ID
		Map<String , Object> queryCourseMap = new HashMap<>();
		queryCourseMap.put("courseId", cme.getCourseId());
		CoursesEntity courses = coursesService.queryObject(queryCourseMap);
		cme.setProductId(courses.getProductId());
		//保存主表修改
		courseMaterialDao.update(cme);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		courseMaterialDao.delete(map);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void deleteBatch(Map<String, Object> map){
		courseMaterialDao.deleteBatch(map);
	}

	@Override
	public CourserMaterialPOJO queryPojoObject(Long number) {
		return courseMaterialDao.queryPojoObject(number);
	}

	@Override
	public int checkCourses(Map<String, Object> map) {
		map.put("tableName", "course_material");
		map.put("columnName", "course_id");
		return sysCheckQuoteDao.checkQuote(map);
	}
	
	
}
