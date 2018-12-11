package io.renren.modules.job.service;

import io.renren.modules.job.entity.VideoLogDetailEntity;

/**
 * 观看录播详细日志
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-23 16:46:38
 */
public interface VideoLogDetailService {
	

	void save(VideoLogDetailEntity videoLogDetail);

	/**
	 * 录播记录校验是否存在
	 * @param duration	时长
	 * @param startTime    开始时间
	 * @param leaveTime    离开时间
	 * @param device    设备版本
	 * @param vodId        录播视频ID
	 * @param userId    用户ID
	 * @param classplanLiveId
     * @return			true 可以新增 , false 不可以新增
	 */
	boolean checkAddAble(Long startTime, Long leaveTime, Integer device, String vodId, Long userId, String classplanLiveId);

}
