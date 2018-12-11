package com.hq.learningapi.service.impl;

import com.hq.learningapi.dao.AppGuideDao;
import com.hq.learningapi.entity.AppGuideEntity;
import com.hq.learningapi.service.AppGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glenn on 2017/5/2 0002.
 */
@Service("AppGuideService")
public class AppGuideServiceImpl implements AppGuideService {

    @Autowired
    private AppGuideDao appGuideDao;

    @Override
    public List<AppGuideEntity> queryList(Long professionId, Long areaId,Long levelId,String schoolId) {
        List<Long> areaIdList = new ArrayList<Long>();
        areaIdList.add(areaId);
        areaIdList.add(0L);

        List<Long> professionIdList = new ArrayList<Long>();
        professionIdList.add(professionId);
        professionIdList.add(0L);

        List<Long> levelIdList = new ArrayList<Long>();
        levelIdList.add(levelId);
        levelIdList.add(0L);

        return appGuideDao.queryList(areaIdList,professionIdList,levelIdList,schoolId,1);
    }

    @Override
    public List<AppGuideEntity> queryList(String schoolId) {
        return appGuideDao.queryList0(0L,0L,0L,schoolId,1);
    }

}
