package com.hq.learningapi.service.impl;

import com.hq.learningapi.dao.AppBannerDao;
import com.hq.learningapi.dao.AppBannerDao;
import com.hq.learningapi.entity.AppBannerEntity;
import com.hq.learningapi.service.AppBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
@Service("appBannerService")
public class AppBannerServiceImpl implements AppBannerService {

    @Autowired
    private AppBannerDao appBannerDao;


    @Override
    public List<AppBannerEntity> queryList(Long levelId, Long professionId, String schoolId) {

        Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("levelId",levelId);
        parameters.put("professionId",professionId);
        parameters.put("schoolId",schoolId);
        parameters.put("status",1);
        return appBannerDao.queryList(parameters);
    }
}
