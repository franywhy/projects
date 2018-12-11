package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.CourseMaterialTypeDao;
import io.renren.entity.CourseMaterialTypeEntity;
import io.renren.pojo.coursematerial.CourserMaterialPOJO;
import io.renren.pojo.coursematerial.CourserMaterialTypePOJO;
import io.renren.service.CourseMaterialTypeService;
import io.renren.utils.Constant;


@Transactional(readOnly = true)
@Service("courseMaterialTypeService")
public class CourseMaterialTypeServiceImpl implements CourseMaterialTypeService {
	@Autowired
	private CourseMaterialTypeDao courseMaterialTypeDao;
	
	@Override
	public CourseMaterialTypeEntity queryObject(Map<String, Object> map){
		return courseMaterialTypeDao.queryObject(map);
	}
	
	@Override
	public List<CourseMaterialTypeEntity> queryList(Map<String, Object> map){
		return courseMaterialTypeDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseMaterialTypeDao.queryTotal(map);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void save(CourseMaterialTypeEntity courseMaterialType){
		courseMaterialTypeDao.save(courseMaterialType);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void update(CourseMaterialTypeEntity courseMaterialType){
		courseMaterialTypeDao.update(courseMaterialType);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void delete(Map<String, Object> map){
		courseMaterialTypeDao.delete(map);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void deleteBatch(Map<String, Object> map){
		courseMaterialTypeDao.deleteBatch(map);
	}

	@Override
	public void deleteBatchNotIn(Map<String, Object> map) {
		courseMaterialTypeDao.deleteBatchNotIn(map);
	}

	@Override
	public List<CourseMaterialTypeEntity> queryObject(Long number) {
		return courseMaterialTypeDao.queryList(number);
	}

	@Override
	public CourserMaterialTypePOJO queryPojoObject(Long number) {
		return courseMaterialTypeDao.queryPojoObject(number);
	}

	@Override
	public boolean countMaterialType(long materialTypeId) {
		
		return courseMaterialTypeDao.countMaterialType(materialTypeId)>0;
	}
	
	
}
