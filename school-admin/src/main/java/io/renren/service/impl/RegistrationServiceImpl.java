package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import io.renren.dao.RegistrationDao;
import io.renren.entity.RegistrationEntity;
import io.renren.service.RegistrationService;




@Service("registrationService")
public class RegistrationServiceImpl implements RegistrationService {
	@Autowired
	private RegistrationDao registrationDao;
	
	@Override
	public RegistrationEntity queryObject(Map<String, Object> map){
		return registrationDao.queryObject(map);
	}
	
	@Override
	public List<RegistrationEntity> queryList(Map<String, Object> map){
		return registrationDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return registrationDao.queryTotal(map);
	}
	
	@Override
	public void save(RegistrationEntity registration){
		registrationDao.save(registration);
	}
	
	@Override
	public void update(RegistrationEntity registration){
		registrationDao.update(registration);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		registrationDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		registrationDao.deleteBatch(map);
	}

	@Override
	public RegistrationEntity queryObject(Long id) {
		return registrationDao.queryObject(id);
		
	}

	@Override
	public void saveBatch(List<RegistrationEntity> list) {
		registrationDao.saveBatch(list);
		
	}
	
	
}
