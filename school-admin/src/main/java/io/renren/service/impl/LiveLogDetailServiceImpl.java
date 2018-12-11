package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.LiveLogDetailDao;
import io.renren.entity.LiveLogDetailEntity;
import io.renren.service.LiveLogDetailService;
import io.renren.utils.Constant;



@Service("liveLogDetailService")
public class LiveLogDetailServiceImpl implements LiveLogDetailService {
	@Autowired
	private LiveLogDetailDao liveLogDetailDao;
	
	@Override
	public LiveLogDetailEntity queryObject(Map<String, Object> map){
		return liveLogDetailDao.queryObject(map);
	}
	
	@Override
	public List<LiveLogDetailEntity> queryList(Map<String, Object> map){
		return liveLogDetailDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return liveLogDetailDao.queryTotal(map);
	}
	
	@Override
	public void save(LiveLogDetailEntity liveLogDetail){
		liveLogDetailDao.save(liveLogDetail);
	}
	
	@Override
	public void update(LiveLogDetailEntity liveLogDetail){
		liveLogDetailDao.update(liveLogDetail);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		liveLogDetailDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		liveLogDetailDao.deleteBatch(map);
	}
	
	/**
	 * 校验直播记录明细是否存在,如果存在 true  不存在:false
	 * @param liveId	直播id
	 * @param liveNum	直播房间号
	 * @param userId	学员id
	 * @param joinTime	观看直播-加入时间
	 * @param leaveTime	观看直播-离开时间
	 * @return
	 */
	@Override
	public boolean selectDetailCount(String liveId, String liveNum, Long userId, Long joinTime, Long leaveTime) {
		return this.liveLogDetailDao.selectDetailCount(liveId, liveNum, userId, joinTime, leaveTime) > 0;
	}
	
	
}
