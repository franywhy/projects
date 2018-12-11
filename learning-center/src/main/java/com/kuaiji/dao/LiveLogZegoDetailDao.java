package com.kuaiji.dao;

import java.util.Map;

import com.kuaiji.entity.LiveLogZegoDetailEntity;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-10-12 16:02:30
 */
public interface LiveLogZegoDetailDao{
	
	
	/**
	 * 保存
	 */
	int save(LiveLogZegoDetailEntity liveLogZegoDetailEntity);
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
}
