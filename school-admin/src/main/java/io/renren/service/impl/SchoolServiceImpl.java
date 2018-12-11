package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.SchoolDao;
import io.renren.entity.SchoolEntity;
import io.renren.service.SchoolService;
import io.renren.utils.Constant;



@Service("schoolService")
public class SchoolServiceImpl implements SchoolService {
	@Autowired
	private SchoolDao schoolDao;
	
	@Override
	public SchoolEntity queryObject(Map<String, Object> map){
		return schoolDao.queryObject(map);
	}
	
	@Override
	public List<SchoolEntity> queryList(Map<String, Object> map){
		return schoolDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return schoolDao.queryTotal(map);
	}
	
	@Override
	public void save(SchoolEntity school){
		schoolDao.save(school);
	}
	
	@Override
	public void update(SchoolEntity school){
		schoolDao.update(school);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		schoolDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		schoolDao.deleteBatch(map);
	}
	
	
}
