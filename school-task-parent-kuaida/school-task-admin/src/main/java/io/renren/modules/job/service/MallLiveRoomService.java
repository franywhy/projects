package io.renren.modules.job.service;

import io.renren.modules.job.entity.MallLiveRoomEntity;

import java.util.Map;

/**
 * 直播间档案表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-21 17:00:10
 */
public interface MallLiveRoomService {
	
	MallLiveRoomEntity queryObject(Map<String, Object> map);
}
