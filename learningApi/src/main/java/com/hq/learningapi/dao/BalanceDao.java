package com.hq.learningapi.dao;

import com.hq.learningapi.pojo.BalancePOJO;
import org.springframework.stereotype.Repository;

/**
 * Created by DL on 2018/9/11.
 */
@Repository
public interface BalanceDao {

    BalancePOJO queryBalance(Long userId);

    void updateHqg(BalancePOJO balance);

    void insertBalance(BalancePOJO balance);
}
