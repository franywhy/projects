package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.MallBussinessOppDao;
import io.renren.entity.MallBussinessOppEntity;
import io.renren.service.MallBussinessOppService;
import io.renren.utils.Constant;



@Service("mallBussinessOppService")
public class MallBussinessOppServiceImpl implements MallBussinessOppService {
	@Autowired
	private MallBussinessOppDao mallBussinessOppDao;
	
	@Override
	public MallBussinessOppEntity queryObject(Map<String, Object> map){
		return mallBussinessOppDao.queryObject(map);
	}
	
	@Override
	public List<MallBussinessOppEntity> queryList(Map<String, Object> map){
		return mallBussinessOppDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallBussinessOppDao.queryTotal(map);
	}
	
	@Override
	public void save(MallBussinessOppEntity mallBussinessOpp){
		mallBussinessOppDao.save(mallBussinessOpp);
	}
	
	@Override
	public void update(MallBussinessOppEntity mallBussinessOpp){
		mallBussinessOppDao.update(mallBussinessOpp);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		mallBussinessOppDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		mallBussinessOppDao.deleteBatch(map);
	}
	
	
}
