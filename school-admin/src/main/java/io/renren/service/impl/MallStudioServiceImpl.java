package io.renren.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.MallStudioDao;
import io.renren.entity.MallStudioEntity;
import io.renren.service.MallStudioService;
import io.renren.utils.BeanHelper;
import io.renren.utils.Constant;


@Transactional(readOnly = true)
@Service("mallStudioService")
public class MallStudioServiceImpl implements MallStudioService {
	@Autowired
	private MallStudioDao mallStudioDao;
	
	@Override
	public MallStudioEntity queryObject(Map<String, Object> map){
		return mallStudioDao.queryObject(map);
	}
	
	@Override
	public List<MallStudioEntity> queryList(Map<String, Object> map){
		return mallStudioDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallStudioDao.queryTotal(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void save(MallStudioEntity mallStudio){
		
		mallStudio.setDr(0);
		mallStudio.setCreationTime(new Date());
		mallStudio.setModifiedTime(mallStudio.getCreationTime());
		BeanHelper.beanAttributeValueTrim(mallStudio);
		mallStudioDao.save(mallStudio);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void update(MallStudioEntity mallStudio){
		
		mallStudio.setModifiedTime(new Date());
		BeanHelper.beanAttributeValueTrim(mallStudio);
		mallStudioDao.update(mallStudio);
	}
	
	@Override
	public void delete(Long studioId){
		mallStudioDao.delete(studioId);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void deleteBatch(Long[] studioIds){
		mallStudioDao.deleteBatch(studioIds);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void pause(Long[] studioIds){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", studioIds);
    	map.put("status", Constant.Status.PAUSE.getValue());
    	//map.put("modifiedTime", new Date());
		mallStudioDao.updateBatch(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void resume(Long[] studioIds){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", studioIds);
    	map.put("status", Constant.Status.RESUME.getValue());
    	//map.put("modifiedTime", new Date());
		mallStudioDao.updateBatch(map);
	}

	@Override
	public List<MallStudioEntity> findStudioList(Map<String, Object> map) {
		return mallStudioDao.findStudioList(map);
	}

	@Override
	public List<MallStudioEntity> queryList1(Map<String, Object> map) {
		return this.mallStudioDao.queryList1(map);
	}

	@Override
	public int queryTotal1(Map<String, Object> map) {
		return this.mallStudioDao.queryTotal1(map);
	}
}
