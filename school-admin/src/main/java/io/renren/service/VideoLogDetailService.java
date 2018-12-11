package io.renren.service;

import io.renren.entity.VideoLogDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 观看录播详细日志
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-23 16:46:38
 */
public interface VideoLogDetailService {
	
		
	VideoLogDetailEntity queryObject(Map<String, Object> map);
	
	List<VideoLogDetailEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(VideoLogDetailEntity videoLogDetail);
	
	void update(VideoLogDetailEntity videoLogDetail);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
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
	boolean checkAddAble(Long startTime , Long leaveTime , Integer device , String vodId , Long userId);
	
	/**
	 * 保存mongodb 1.0 考勤数据
	 * @param detailEntity
	 */
	void saveOldV10(VideoLogDetailEntity detailEntity);
}
