package com.hq.learningapi.service;

import com.hq.learningapi.pojo.BalancePOJO;

/**
 * Created by DL on 2018/9/11.
 */
public interface BalanceService {

    //获取用户恒企币
    BalancePOJO queryBalance(Long userId);

    //修改用户恒企币
    void updateHqg(BalancePOJO balance);

    void insertBalance(BalancePOJO balance);
}
