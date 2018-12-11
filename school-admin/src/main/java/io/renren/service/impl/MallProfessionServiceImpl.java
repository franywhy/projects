package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.MallProfessionDao;
import io.renren.entity.MallClassEntity;
import io.renren.entity.MallProfessionEntity;
import io.renren.service.MallProfessionService;
import io.renren.utils.BeanHelper;
import io.renren.utils.Constant;



@Service("mallProfessionService")
public class MallProfessionServiceImpl implements MallProfessionService {
	@Autowired
	private MallProfessionDao mallProfessionDao;
	
	@Override
	public MallProfessionEntity queryObject(Map<String, Object> map){
		return mallProfessionDao.queryObject(map);
	}
	
	@Override
	public List<MallProfessionEntity> queryList(Map<String, Object> map){
		return mallProfessionDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallProfessionDao.queryTotal(map);
	}
	
	@Override
	public void save(MallProfessionEntity mallProfession){
		mallProfession.setAppPic(mallProfession.getAppPic());
		BeanHelper.beanAttributeValueTrim(mallProfession);
		mallProfessionDao.save(mallProfession);
	}
	
	@Override
	public void update(MallProfessionEntity mallProfession){
		mallProfession.setAppPic(mallProfession.getAppPic());
		BeanHelper.beanAttributeValueTrim(mallProfession);
		mallProfessionDao.update(mallProfession);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		mallProfessionDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		mallProfessionDao.deleteBatch(map);
	}

	@Override
	public void pause(Long[] professionIds) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", professionIds);
    	map.put("status", Constant.Status.PAUSE.getValue());
    	mallProfessionDao.updateBatch(map);
	}

	@Override
	public void resume(Long[] professionIds) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", professionIds);
    	map.put("status", Constant.Status.RESUME.getValue());
    	mallProfessionDao.updateBatch(map);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void deleteClassType(Map<String, Object> map) {
		  mallProfessionDao.deleteClassType(map);
		
	}

	@Override
	public List<MallProfessionEntity> queryList1(Map<String, Object> map) {
		return this.mallProfessionDao.queryList1(map);
	}

	@Override
	public MallProfessionEntity queryMID(Map<String, Object> map) {
		return mallProfessionDao.queryMID(map);
	}

	@Override
	public MallProfessionEntity queryObjectById(Long professionId) {
		return mallProfessionDao.queryObjectById(professionId);
	}
	
	
}
