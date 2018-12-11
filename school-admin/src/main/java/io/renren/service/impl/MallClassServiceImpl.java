package io.renren.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.MallClassDao;
import io.renren.entity.MallClassEntity;
import io.renren.pojo.Class.ClassPOJO;
import io.renren.service.MallClassService;
import io.renren.utils.BeanHelper;
import io.renren.utils.Constant;


@Transactional(readOnly = true)
@Service("mallClassService")
public class MallClassServiceImpl implements MallClassService {
	@Autowired
	private MallClassDao mallClassDao;
	
	@Override
	public MallClassEntity queryObject(Map<String, Object> map){
		return mallClassDao.queryObject(map);
	}
	
	@Override
	public List<MallClassEntity> queryList(Map<String, Object> map){
		return mallClassDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallClassDao.queryTotal(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void save(MallClassEntity mallClass){
		mallClass.setDefaultClass(0);
		mallClass.setDr(0);
		mallClass.setCreationTime(new Date());
		mallClass.setModifiedTime(mallClass.getCreationTime());
		BeanHelper.beanAttributeValueTrim(mallClass);
		mallClassDao.save(mallClass);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void update(MallClassEntity mallClass){
		mallClass.setModifiedTime(new Date());
		BeanHelper.beanAttributeValueTrim(mallClass);
		mallClassDao.update(mallClass);
	}
	
	@Override
	public void delete(Long classId){
		mallClassDao.delete(classId);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void deleteBatch(Long[] classIds){
		mallClassDao.deleteBatch(classIds);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void pause(Long[] classIds){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", classIds);
    	map.put("status", Constant.Status.PAUSE.getValue());
		mallClassDao.updateBatch(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void resume(Long[] classIds){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", classIds);
    	map.put("status", Constant.Status.RESUME.getValue());
		mallClassDao.updateBatch(map);
	}

	@Override
	public List<MallClassEntity> findClassList() {
		return mallClassDao.findClassList();
	}

	@Override
	public List<ClassPOJO> queryPojoList(Map<String, Object> map) {
		return this.mallClassDao.queryPojoList(map);
	}

	@Override
	public ClassPOJO queryPojoObject(Map<String, Object> map) {
		return this.mallClassDao.queryPojoObject(map);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void delete(Map<String, Object> map) {
		this.mallClassDao.delete(map);     
	}
	/**
	 * 指定默认班级
	 * @param map
	 * @param schoolId		平台ID
	 * @param classId		班级ID
	 * @return
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public boolean updateDefaultClass(Map<String, Object> map) {
		//指定默认班级
		MallClassEntity classEntity = queryObject(map);
		if(null != classEntity){
			//专业ID
			map.put("professionId", classEntity.getProfessionId());
			//省份ID
			map.put("areaId", classEntity.getAreaId());
			//层次ID
			map.put("levelId", classEntity.getLevelId());
			
			this.mallClassDao.removeOtherDefaultClass(map);
			this.mallClassDao.updateDefaultClass(map);
		}
		return true;
	}
	/**
	 * 指定默认班级（非自考）
	 * @param map
	 * @param classId		班级ID
	 * @return
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public boolean updateDefaultClassNotZK(Map<String, Object> map) {
		//指定默认班级
		MallClassEntity classEntity = queryObject(map);
		if(null != classEntity){
			//专业ID
			map.put("professionId", classEntity.getProfessionId());
			//省份ID
			map.put("areaId", classEntity.getAreaId());
			//层次ID
			map.put("levelId", classEntity.getLevelId());
			//班主任
			map.put("userId", classEntity.getUserId());
			
			this.mallClassDao.removeOtherDefaultClassNotZK(map);
			this.mallClassDao.updateDefaultClassNotZK(map);
		}
		return true;
	}

	@Override
	public MallClassEntity queryClassId(Map<String, Object> map) {
		return this.mallClassDao.queryClassId(map);
	}

	@Override
	public boolean checkExist(Map<String, Object> map) {
		return this.mallClassDao.checkExist(map)>0;
	}

	@Override
	public List<MallClassEntity> queryClassList(Map<String, Object> classMap) {
		return this.mallClassDao.queryClassList(classMap);
	}

}
