package com.kuaiji.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuaiji.dao.LiveLogZegoDetailDao;
import com.kuaiji.entity.LiveLogZegoDetailEntity;
import com.kuaiji.service.LiveLogZegoDetailService;



@Service("liveLogZegoDetailService")
public class LiveLogZegoDetailServiceImpl implements LiveLogZegoDetailService {
	@Autowired
	private LiveLogZegoDetailDao liveLogZegoDetailDao;
	
	/*@Override
	public LiveLogZegoDetailEntity queryObject(Map<String, Object> map){
		return liveLogZegoDetailDao.queryObject(map);
	}
	
	@Override
	public List<LiveLogZegoDetailEntity> queryList(Map<String, Object> map){
		return liveLogZegoDetailDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return liveLogZegoDetailDao.queryTotal(map);
	}*/
	
	@Override
	public int save(LiveLogZegoDetailEntity liveLogZegoDetail){
		return liveLogZegoDetailDao.save(liveLogZegoDetail);
	}
	
	/*@Override
	public void update(LiveLogZegoDetailEntity liveLogZegoDetail){
		liveLogZegoDetailDao.update(liveLogZegoDetail);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		liveLogZegoDetailDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		liveLogZegoDetailDao.deleteBatch(map);
	}*/
	
	
}
