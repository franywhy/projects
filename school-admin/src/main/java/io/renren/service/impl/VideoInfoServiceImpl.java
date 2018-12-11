package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.VideoInfoDao;
import io.renren.entity.VideoInfoEntity;
import io.renren.service.VideoInfoService;
import io.renren.utils.Constant;



@Service("videoInfoService")
public class VideoInfoServiceImpl implements VideoInfoService {
	@Autowired
	private VideoInfoDao videoInfoDao;
	
	@Override
	public VideoInfoEntity queryObject(Map<String, Object> map){
		return videoInfoDao.queryObject(map);
	}
	
	@Override
	public List<VideoInfoEntity> queryList(Map<String, Object> map){
		return videoInfoDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return videoInfoDao.queryTotal(map);
	}
	
	@Override
	public void save(VideoInfoEntity videoInfo){
		videoInfoDao.save(videoInfo);
	}
	
	@Override
	public void update(VideoInfoEntity videoInfo){
		videoInfoDao.update(videoInfo);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		videoInfoDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		videoInfoDao.deleteBatch(map);
	}
	
	
}
