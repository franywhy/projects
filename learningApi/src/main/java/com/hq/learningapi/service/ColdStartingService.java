package com.hq.learningapi.service;

import com.hq.learningapi.entity.ColdStartingEntity;

import java.util.List;

/**
 * Created by DL on 2018/1/2.
 */
public interface ColdStartingService {
    //获取所有广告页面
    List<ColdStartingEntity> getColdStartingList();

    //获取最新广告页面
    ColdStartingEntity getLatestColdStarting();
}
