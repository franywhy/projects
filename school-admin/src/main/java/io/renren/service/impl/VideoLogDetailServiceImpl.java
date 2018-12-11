package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.VideoLogDetailDao;
import io.renren.entity.VideoLogDetailEntity;
import io.renren.service.VideoLogDetailService;
import io.renren.utils.Constant;



@Service("videoLogDetailService")
public class VideoLogDetailServiceImpl implements VideoLogDetailService {
	@Autowired
	private VideoLogDetailDao videoLogDetailDao;
	
	@Override
	public VideoLogDetailEntity queryObject(Map<String, Object> map){
		return videoLogDetailDao.queryObject(map);
	}
	
	@Override
	public List<VideoLogDetailEntity> queryList(Map<String, Object> map){
		return videoLogDetailDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return videoLogDetailDao.queryTotal(map);
	}
	
	@Override
	public void save(VideoLogDetailEntity videoLogDetail){
		videoLogDetailDao.save(videoLogDetail);
	}
	
	@Override
	public void update(VideoLogDetailEntity videoLogDetail){
		videoLogDetailDao.update(videoLogDetail);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		videoLogDetailDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		videoLogDetailDao.deleteBatch(map);
	}

	@Override
	public boolean checkAddAble(Long startTime, Long leaveTime, Integer device, String vodId, Long userId) {
		return this.videoLogDetailDao.selectDetailCount(startTime, leaveTime, device, vodId, userId)==0;
	}

	@Override
	public void saveOldV10(VideoLogDetailEntity detailEntity) {
		
	}
	
	
}
