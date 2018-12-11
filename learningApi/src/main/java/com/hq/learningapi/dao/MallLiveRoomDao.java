package com.hq.learningapi.dao;

import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by Administrator on 2017/5/16 0016.
 */
@Repository
public interface MallLiveRoomDao {
    Long queryLiveRoomChannelId(Long liveRoomId);

    Map<String, Object> queryByClassPlanDetailId(Map<String, Object> map);

    Map<String, Object> queryByOliveId(Map<String, Object> map);
}
