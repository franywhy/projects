package com.hq.learningapi.service.impl;

import com.hq.learningapi.dao.BalanceDao;
import com.hq.learningapi.pojo.BalancePOJO;
import com.hq.learningapi.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DL on 2018/9/11.
 */
@Service("balanceService")
public class BalanceServiceImpl implements BalanceService {
    @Autowired
    private BalanceDao balanceDao;

    @Override
    public BalancePOJO queryBalance(Long userId) {
        return balanceDao.queryBalance(userId);
    }

    @Override
    public void updateHqg(BalancePOJO balance) {
        balanceDao.updateHqg(balance);
    }

    @Override
    public void insertBalance(BalancePOJO balance) {
        balanceDao.insertBalance(balance);
    }
}
