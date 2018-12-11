package io.renren.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import io.renren.entity.VideoLogDetailEntity;

/**
 * 观看录播详细日志
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-23 16:46:38
 */
public interface VideoLogDetailDao extends BaseMDao<VideoLogDetailEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	/**
	 * 录播记录校验是否存在
	 * @param startTime	开始时间
	 * @param leaveTime	离开时间
	 * @param duration	时长
	 * @param device	设备版本
	 * @param vodId		录播视频ID
	 * @param userId	用户ID
	 * @return			true 可以新增 , false 不可以新增
	 */
	int selectDetailCount(
			@Param("startTime")Long startTime ,@Param("leaveTime")Long leaveTime ,@Param("device")Integer device ,
			@Param("videoId")String videoId ,@Param("userId")Long userId
			);
}
