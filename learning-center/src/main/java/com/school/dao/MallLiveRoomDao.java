package com.school.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.school.entity.MallLiveRoomEntity;
import com.school.entity.MallOrderEntity;

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
