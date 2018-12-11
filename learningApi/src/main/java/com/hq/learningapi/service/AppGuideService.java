package com.hq.learningapi.service;

import com.hq.learningapi.entity.AppGuideEntity;

import java.util.List;

/**
 * Created by Glenn on 2017/5/2 0002.
 */
public interface AppGuideService {

    List<AppGuideEntity> queryList(Long professionId, Long areaId, Long levelId, String schoolId);
    List<AppGuideEntity> queryList(String schoolId);
}
