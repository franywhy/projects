package com.kuaiji.service;

import com.kuaiji.entity.AppCommodity;

/**
 * Created by Administrator on 2018/4/24 0024.
 */
public interface AppCommodityService {

    AppCommodity selectByExampleFetchOne(String ncCommodityId);
}
