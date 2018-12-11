package com.hq.learningapi.service;

import java.util.Map;

/**
 * Created by ShanYaofeng on 2017/6/7 0007.
 */
public interface MallLiveRoomService {

    Map<String, Object> queryByClassPlanDetailId(String classPlanDetailId, String schoolId);

    Map<String, Object> queryByOliveId(Long oliveId);
}
