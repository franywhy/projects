package io.renren.service.impl;

import io.renren.dao.ClassToTkLogDao;
import io.renren.entity.ClassToTkLogEntity;
import io.renren.service.ClassToTkLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;




@Service("classToTkLogService")
public class ClassToTkLogServiceImpl implements ClassToTkLogService {
	@Autowired
	private ClassToTkLogDao classToTkLogDao;
	
	@Override
	public ClassToTkLogEntity queryObject(Long id){
		return classToTkLogDao.queryObject(id);
	}
	
	@Override
	public List<ClassToTkLogEntity> queryList(Map<String, Object> map){
		return classToTkLogDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return classToTkLogDao.queryTotal(map);
	}
	
	@Override
	public void save(ClassToTkLogEntity classToTkLog){
		classToTkLogDao.save(classToTkLog);
	}
	
	@Override
	public void update(ClassToTkLogEntity classToTkLog){
		classToTkLogDao.update(classToTkLog);
	}
	
	@Override
	public void delete(Long id){
		classToTkLogDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		classToTkLogDao.deleteBatch(ids);
	}
	
}
