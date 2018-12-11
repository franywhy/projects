package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.MallOrderSyncDao;
import io.renren.entity.MallOrderSyncEntity;
import io.renren.service.MallOrderSyncService;
import io.renren.utils.Constant;



@Service("mallOrderSyncService")
public class MallOrderSyncServiceImpl implements MallOrderSyncService {
	@Autowired
	private MallOrderSyncDao mallOrderSyncDao;
	
	@Override
	public MallOrderSyncEntity queryObject(Map<String, Object> map){
		return mallOrderSyncDao.queryObject(map);
	}
	
	@Override
	public List<MallOrderSyncEntity> queryList(Map<String, Object> map){
		return mallOrderSyncDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallOrderSyncDao.queryTotal(map);
	}
	
	@Override
	public void save(MallOrderSyncEntity mallOrderSync){
		mallOrderSyncDao.save(mallOrderSync);
	}
	
	@Override
	public void update(MallOrderSyncEntity mallOrderSync){
		mallOrderSyncDao.update(mallOrderSync);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		mallOrderSyncDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		mallOrderSyncDao.deleteBatch(map);
	}
	
	
}
