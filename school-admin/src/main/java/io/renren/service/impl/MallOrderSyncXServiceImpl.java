package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.MallOrderSyncXDao;
import io.renren.entity.MallOrderSyncXEntity;
import io.renren.service.MallOrderSyncXService;
import io.renren.utils.Constant;



@Service("mallOrderSyncXService")
public class MallOrderSyncXServiceImpl implements MallOrderSyncXService {
	@Autowired
	private MallOrderSyncXDao mallOrderSyncXDao;
	
	@Override
	public MallOrderSyncXEntity queryObject(Map<String, Object> map){
		return mallOrderSyncXDao.queryObject(map);
	}
	
	@Override
	public List<MallOrderSyncXEntity> queryList(Map<String, Object> map){
		return mallOrderSyncXDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallOrderSyncXDao.queryTotal(map);
	}
	
	@Override
	public void save(MallOrderSyncXEntity mallOrderSyncX){
		mallOrderSyncXDao.save(mallOrderSyncX);
	}
	
	@Override
	public void update(MallOrderSyncXEntity mallOrderSyncX){
		mallOrderSyncXDao.update(mallOrderSyncX);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		mallOrderSyncXDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		mallOrderSyncXDao.deleteBatch(map);
	}
	
	
}
