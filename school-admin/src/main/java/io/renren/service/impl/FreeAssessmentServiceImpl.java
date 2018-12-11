package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.dao.FreeAssessmentDao;
import io.renren.entity.FreeAssessmentEntity;
import io.renren.service.FreeAssessmentService;



@Service("freeAssessmentService")
public class FreeAssessmentServiceImpl implements FreeAssessmentService {
	@Autowired
	private FreeAssessmentDao freeAssessmentDao;

	@Override
	public void updateStatusBatch(Map<String, Object> map) {
		freeAssessmentDao.updateStatusBatch(map);
	}

	@Override
	public FreeAssessmentEntity queryObject(Long id){
		return freeAssessmentDao.queryObject(id);
	}
	
	@Override
	public List<FreeAssessmentEntity> queryList(Map<String, Object> map){
		return freeAssessmentDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return freeAssessmentDao.queryTotal(map);
	}
	
	@Override
	public void save(FreeAssessmentEntity freeAssessment){
		freeAssessmentDao.save(freeAssessment);
	}

	@Override
	public void saveBatch(List<FreeAssessmentEntity> list) {
		freeAssessmentDao.saveBatch(list);
	}

	@Override
	public void update(FreeAssessmentEntity freeAssessment){
		freeAssessmentDao.update(freeAssessment);
	}
	
	@Override
	public void delete(Long id){
		freeAssessmentDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		freeAssessmentDao.deleteBatch(ids);
	}
	
}
