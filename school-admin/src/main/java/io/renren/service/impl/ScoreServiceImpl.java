package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.ScoreDao;
import io.renren.entity.ScoreEntity;
import io.renren.service.ScoreService;
import io.renren.utils.Constant;



@Service("scoreService")
public class ScoreServiceImpl implements ScoreService {
	@Autowired
	private ScoreDao scoreDao;
	
	@Override
	public ScoreEntity queryObject(Long id){
		return scoreDao.queryObject(id);
	}
	
	@Override
	public List<ScoreEntity> queryList(Map<String, Object> map){
		return scoreDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return scoreDao.queryTotal(map);
	}
	
	@Override
	public void save(ScoreEntity score){
		scoreDao.save(score);
	}
	
	@Override
	public void saveBatch(List<ScoreEntity> list) {
		scoreDao.saveBatch(list);		
	}
	
	@Override
	public void update(ScoreEntity score){
		scoreDao.update(score);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		scoreDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		scoreDao.deleteBatch(map);
	}

	@Override
	public void updateStatusBatch(Map<String, Object> map) {
		scoreDao.updateStatusBatch(map);
		
	}

	
	
	
}
