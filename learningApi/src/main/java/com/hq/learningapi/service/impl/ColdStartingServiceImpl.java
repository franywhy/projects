package com.hq.learningapi.service.impl;

import com.hq.learningapi.dao.ColdStartingDao;
import com.hq.learningapi.entity.ColdStartingEntity;
import com.hq.learningapi.service.ColdStartingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by DL on 2018/1/2.
 */
@Service("coldStartingService")
public class ColdStartingServiceImpl implements ColdStartingService {
    @Autowired
    private ColdStartingDao coldStartingDao;
    @Override
    public List<ColdStartingEntity> getColdStartingList() {
        return coldStartingDao.getColdStartingList();
    }

    @Override
    public ColdStartingEntity getLatestColdStarting() {
        return coldStartingDao.getLatestColdStarting();
    }

}
