package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.VideoLogDao;
import io.renren.entity.VideoLogEntity;
import io.renren.service.VideoLogService;
import io.renren.utils.Constant;



@Service("videoLogService")
public class VideoLogServiceImpl implements VideoLogService {
	@Autowired
	private VideoLogDao videoLogDao;
	
	@Override
	public VideoLogEntity queryObject(Map<String, Object> map){
		return videoLogDao.queryObject(map);
	}
	
	@Override
	public List<VideoLogEntity> queryList(Map<String, Object> map){
		return videoLogDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return videoLogDao.queryTotal(map);
	}
	
	@Override
	public void save(VideoLogEntity videoLog){
		videoLogDao.save(videoLog);
	}
	
	@Override
	public void update(VideoLogEntity videoLog){
		videoLogDao.update(videoLog);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		videoLogDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		videoLogDao.deleteBatch(map);
	}

	@Override
	public VideoLogEntity queryByVideoId(Map<String, Object> map) {
		return videoLogDao.queryByVideoId(map);
	}

	@Override
	public int videoLogExist(String mId) {
		return videoLogDao.videoLogExist(mId);
	}
	
	
}
