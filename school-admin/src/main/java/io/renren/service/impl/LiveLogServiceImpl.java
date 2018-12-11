package io.renren.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.LiveLogDao;
import io.renren.entity.LiveLogEntity;
import io.renren.service.LiveLogService;



@Service("liveLogService")
public class LiveLogServiceImpl implements LiveLogService {
	@Autowired
	private LiveLogDao liveLogDao;
	
	@Override
	public LiveLogEntity queryObject(Map<String, Object> map){
		return liveLogDao.queryObject(map);
	}
	
	@Override
	public List<LiveLogEntity> queryList(Map<String, Object> map){
		return liveLogDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return liveLogDao.queryTotal(map);
	}
	
	@Override
	public void save(LiveLogEntity liveLog){
		liveLogDao.save(liveLog);
	}
	
	@Override
	public void update(LiveLogEntity liveLog){
		liveLogDao.update(liveLog);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		liveLogDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		liveLogDao.deleteBatch(map);
	}

	@Override
	public int liveLogExist(String mId) {
		return liveLogDao.liveLogExist(mId);
//		return 0;
	}

}
