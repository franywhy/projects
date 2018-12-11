package io.renren.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import io.renren.dao.AbandonedAssessmentDao;
import io.renren.entity.AbandonedAssessmentEntity;
import io.renren.entity.FreeAssessmentEntity;
import io.renren.service.AbandonedAssessmentService;


@Service("abandonedAssessmentService")
public class AbandonedAssessmentServiceImpl implements AbandonedAssessmentService {
	@Autowired
	private AbandonedAssessmentDao abandonedAssessmentDao;
	
	@Override
	public void updateStatusBatch(Map<String, Object> map) {
		abandonedAssessmentDao.updateStatusBatch(map);		
	}
			
	@Override
	public List<AbandonedAssessmentEntity> queryList(Map<String, Object> map){
		return abandonedAssessmentDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return abandonedAssessmentDao.queryTotal(map);
	}
	
	@Override
	public void save(AbandonedAssessmentEntity abandonedAssessment){
		abandonedAssessmentDao.save(abandonedAssessment);
	}
	
	@Override
	public void update(AbandonedAssessmentEntity abandonedAssessment){
		abandonedAssessmentDao.update(abandonedAssessment);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		abandonedAssessmentDao.delete(map);
	}


	@Override
	public AbandonedAssessmentEntity queryObject(Long id) {		
		return abandonedAssessmentDao.queryObject(id);
	}

	@Override
	public void deleteBatch(Long[] ids) {
		abandonedAssessmentDao.deleteBatch(ids);		
	}


	@Override
	public void saveBatch(List<AbandonedAssessmentEntity> list) {
		abandonedAssessmentDao.saveBatch(list);
		
	}
	

}
