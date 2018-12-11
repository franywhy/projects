package io.renren.service;

import io.renren.entity.LiveLogDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 观看直播详细日志
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-22 09:57:27
 */
public interface LiveLogDetailService {
	
		
	LiveLogDetailEntity queryObject(Map<String, Object> map);
	
	List<LiveLogDetailEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(LiveLogDetailEntity liveLogDetail);
	
	void update(LiveLogDetailEntity liveLogDetail);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	/**
	 * 校验直播记录明细是否存在,如果存在 true  不存在:false
	 * @param liveId	直播id
	 * @param liveNum	直播房间号
	 * @param userId	学员id
	 * @param joinTime	观看直播-加入时间
	 * @param leaveTime	观看直播-离开时间
	 * @return
	 */
	boolean selectDetailCount(String liveId , String liveNum , Long userId , Long joinTime , Long leaveTime);
	
}
