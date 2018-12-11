package com.hq.learningapi.service.impl;

import com.hq.learningapi.dao.MallLiveRoomDao;
import com.hq.learningapi.service.MallLiveRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/7 0007.
 */
@Service
public class MallLiveRoomServiceImpl implements MallLiveRoomService {

    @Autowired
    private MallLiveRoomDao mallLiveRoomDao;

    @Override
    public Map<String, Object> queryByClassPlanDetailId(String classPlanDetailId, String schoolId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("classPlanDetailId", classPlanDetailId);
        parameters.put("schoolId", schoolId);
        parameters.put("dr",0);
        return mallLiveRoomDao.queryByClassPlanDetailId(parameters);
    }

    @Override
    public Map<String, Object> queryByOliveId(Long oliveId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("oliveId", oliveId);
        parameters.put("dr",0);
        return mallLiveRoomDao.queryByOliveId(parameters);
    }
}
