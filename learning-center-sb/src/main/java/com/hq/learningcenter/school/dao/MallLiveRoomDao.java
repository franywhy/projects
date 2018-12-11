package com.hq.learningcenter.school.dao;

import com.hq.learningcenter.school.entity.MallLiveRoomEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 09:58:34
 */
public interface MallLiveRoomDao{
	
	MallLiveRoomEntity queryByLiveRoomId(@Param("classplanLiveId") String classplanLiveId);
}
