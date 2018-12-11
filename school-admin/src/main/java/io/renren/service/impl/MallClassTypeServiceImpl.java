package io.renren.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.MallClassTypeDao;
import io.renren.entity.MallClassTypeEntity;
import io.renren.pojo.classType.ClassTypePOJO;
import io.renren.service.MallClassTypeService;
import io.renren.utils.BeanHelper;
import io.renren.utils.Constant;


@Transactional(readOnly = true)
@Service("mallClassTypeService")
public class MallClassTypeServiceImpl implements MallClassTypeService {
	@Autowired
	private MallClassTypeDao mallClassTypeDao;
	
	@Override
	public MallClassTypeEntity queryObject(Long classtypeId){
		return mallClassTypeDao.queryObject(classtypeId);
	}
	
	@Override
	public List<MallClassTypeEntity> queryList(Map<String, Object> map){
		return mallClassTypeDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallClassTypeDao.queryTotal(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void save(MallClassTypeEntity mallClassType){
		
		mallClassType.setDr(0);
		mallClassType.setCreationTime(new Date());
		mallClassType.setModifiedTime(mallClassType.getCreationTime());
		BeanHelper.beanAttributeValueTrim(mallClassType);
		mallClassTypeDao.save(mallClassType);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void update(MallClassTypeEntity mallClassType){
		mallClassType.setModifiedTime(new Date());
		BeanHelper.beanAttributeValueTrim(mallClassType);
		mallClassTypeDao.update(mallClassType);
	}
	
	@Override
	public void delete(Long classtypeId){
		mallClassTypeDao.delete(classtypeId);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void deleteBatch(Long[] classtypeIds){
		mallClassTypeDao.deleteBatch(classtypeIds);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void pause(Long[] classtypeIds){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", classtypeIds);
    	map.put("status", Constant.Status.PAUSE.getValue());
		mallClassTypeDao.updateBatch(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void resume(Long[] classtypeIds){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", classtypeIds);
    	map.put("status", Constant.Status.RESUME.getValue());
    	//map.put("modifiedTime", new Date());
		mallClassTypeDao.updateBatch(map);
	}


	@Override
	public List<Map<String, Object>> querySelectList(Map<String, Object> map) {
		return this.mallClassTypeDao.querySelectList(map);
	}

	@Override
	public List<ClassTypePOJO> queryPojoList(Map<String, Object> map) {
		return this.mallClassTypeDao.queryPojoList(map);
	}

	@Override
	public ClassTypePOJO queryPojoObject(Map<String, Object> map) {
		return this.mallClassTypeDao.queryPojoObject(map);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void deleteClassType(Map<String, Object> map) {
		mallClassTypeDao.deleteClassType(map);
	}

	@Override
	public MallClassTypeEntity queryClassId(String classtypeName) {
		return mallClassTypeDao.queryClassId(classtypeName);
	}
}
