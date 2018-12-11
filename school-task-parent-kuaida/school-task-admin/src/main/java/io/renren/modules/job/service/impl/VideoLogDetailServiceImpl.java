package io.renren.modules.job.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.modules.job.dao.VideoLogDetailDao;
import io.renren.modules.job.entity.VideoLogDetailEntity;
import io.renren.modules.job.service.VideoLogDetailService;


@Service("videoLogDetailService")
public class VideoLogDetailServiceImpl implements VideoLogDetailService {
	@Autowired
	private VideoLogDetailDao videoLogDetailDao;
	

	@Override
	public void save(VideoLogDetailEntity videoLogDetail){
		videoLogDetailDao.save(videoLogDetail);
	}


	@Override
	public boolean checkAddAble(Long startTime, Long leaveTime, Integer device, String vodId, Long userId, String classplanLiveId) {
		return this.videoLogDetailDao.selectDetailCount(startTime, leaveTime, device, vodId, userId,classplanLiveId)==0;
	}

}
