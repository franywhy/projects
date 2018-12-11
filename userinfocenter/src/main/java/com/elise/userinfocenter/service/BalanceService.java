package com.elise.userinfocenter.service;

import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-02-27 17:34:28
 */
public interface BalanceService {

    Map<String,Object> queryBalanceByUserId(Long userId);

    Double queryHqgByUserId(Long userId);
}
