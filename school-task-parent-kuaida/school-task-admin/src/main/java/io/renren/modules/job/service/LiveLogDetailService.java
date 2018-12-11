package io.renren.modules.job.service;

import io.renren.modules.job.entity.LiveLogDetailEntity;

/**
 * 观看直播详细日志
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-22 09:57:27
 */
public interface LiveLogDetailService {

	void save(LiveLogDetailEntity liveLogDetail);

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
	boolean selectDetailCount(String liveId, String liveNum, Long userId, Long joinTime, Long leaveTime, String classplanLiveId);
	
}
