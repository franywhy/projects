package com.hq.learningcenter.kuaiji.service.impl;

import com.hq.learningcenter.kuaiji.dao.AppCommodityMapper;
import com.hq.learningcenter.kuaiji.entity.AppCommodity;
import com.hq.learningcenter.kuaiji.entity.AppCommodityExample;
import com.hq.learningcenter.kuaiji.service.AppCommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/4/24 0024.
 */
@Service
public class AppCommodityServiceImpl implements AppCommodityService {

    @Autowired
    private AppCommodityMapper appCommodityMapper;

    @Override
    public AppCommodity selectByExampleFetchOne(String ncCommodityId) {
        AppCommodityExample example = new AppCommodityExample();
        example.createCriteria().andNcCommodityIdEqualTo(ncCommodityId);
        return appCommodityMapper.selectByExampleFetchOne(example);
    }
}
