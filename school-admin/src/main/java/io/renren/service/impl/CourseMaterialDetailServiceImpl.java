package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.CourseMaterialDetailDao;
import io.renren.entity.CourseMaterialDetailEntity;
import io.renren.service.CourseMaterialDetailService;
import io.renren.utils.Constant;



@Service("courseMaterialDetailService")
public class CourseMaterialDetailServiceImpl implements CourseMaterialDetailService {
	@Autowired
	private CourseMaterialDetailDao courseMaterialDetailDao;
	
	@Override
	public CourseMaterialDetailEntity queryObject(Map<String, Object> map){
		return courseMaterialDetailDao.queryObject(map);
	}
	
	@Override
	public List<Map<String, Object>> queryListMap(Map<String, Object> map){
		return courseMaterialDetailDao.queryListMap(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseMaterialDetailDao.queryTotal(map);
	}
	
	@Override
	public void save(CourseMaterialDetailEntity courseMaterialDetail){
		courseMaterialDetailDao.save(courseMaterialDetail);
	}
	
	@Override
	public void update(CourseMaterialDetailEntity courseMaterialDetail){
		courseMaterialDetailDao.update(courseMaterialDetail);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		courseMaterialDetailDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		courseMaterialDetailDao.deleteBatch(map);
	}

	@Override
	public CourseMaterialDetailEntity queryObjectById(Map<String, Object> map) {
		return courseMaterialDetailDao.queryObjectById(map);
	}

	@Override
	public List<CourseMaterialDetailEntity> queryObjectByMaterialTypeId(Map<String, Object> map) {
		return courseMaterialDetailDao.queryObjectByMaterialTypeId(map);
	}

	@Override
	public boolean countMaterialDetail(long materialDetailId) {
		return courseMaterialDetailDao.countMaterialDetail(materialDetailId)>0;
	}
	
	
}
