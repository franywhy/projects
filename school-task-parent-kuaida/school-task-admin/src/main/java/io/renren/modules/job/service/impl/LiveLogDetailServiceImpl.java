package io.renren.modules.job.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.modules.job.dao.LiveLogDetailDao;
import io.renren.modules.job.entity.LiveLogDetailEntity;
import io.renren.modules.job.service.LiveLogDetailService;


@Service("liveLogDetailService")
public class LiveLogDetailServiceImpl implements LiveLogDetailService {
	@Autowired
	private LiveLogDetailDao liveLogDetailDao;

	@Override
	public void save(LiveLogDetailEntity liveLogDetail){
		liveLogDetailDao.save(liveLogDetail);
	}

	
	/**
	 * 校验直播记录明细是否存在,如果存在 true  不存在:false
	 * @param liveId    直播id
	 * @param liveNum    直播房间号
	 * @param userId    学员id
	 * @param joinTime    观看直播-加入时间
	 * @param leaveTime    观看直播-离开时间
	 * @param classplanLiveId
     * @return
	 */
	@Override
	public boolean selectDetailCount(String liveId, String liveNum, Long userId, Long joinTime, Long leaveTime, String classplanLiveId) {
		return this.liveLogDetailDao.selectDetailCount(liveId, liveNum, userId, joinTime, leaveTime,classplanLiveId) > 0;
	}
	
	
}
