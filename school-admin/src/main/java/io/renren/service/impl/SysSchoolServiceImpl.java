package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.dao.SysSchoolDao;
import io.renren.entity.SysSchoolEntity;
import io.renren.service.SysSchoolService;
import io.renren.utils.BeanHelper;



@Service("sysSchoolService")
public class SysSchoolServiceImpl implements SysSchoolService {
	@Autowired
	private SysSchoolDao sysSchoolDao;
	
	@Override
	public SysSchoolEntity queryObject(Map<String, Object> map){
		return sysSchoolDao.queryObject(map);
	}
	
	@Override
	public List<SysSchoolEntity> queryList(Map<String, Object> map){
		return sysSchoolDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysSchoolDao.queryTotal(map);
	}
	
	@Override
	public void save(SysSchoolEntity sysSchool){
		BeanHelper.beanAttributeValueTrim(sysSchool);
		sysSchoolDao.save(sysSchool);
	}
	
	@Override
	public void update(SysSchoolEntity sysSchool){
		BeanHelper.beanAttributeValueTrim(sysSchool);
		sysSchoolDao.update(sysSchool);
	}
	
	@Override
	public void delete(Long id){
		sysSchoolDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		sysSchoolDao.deleteBatch(map);
	}

	@Override
	public SysSchoolEntity queryObjectByCity(String city) {
		return this.sysSchoolDao.queryObjectByCity(city);
	}

	@Override
	public SysSchoolEntity queryObjectByNcId(String nc_id) {
		return this.sysSchoolDao.queryObjectByNcId(nc_id);
	}

	@Override
	public void deleteByNcId(String nc_id) {
		this.sysSchoolDao.deleteByNcId(nc_id);
	}
	
}
