package com.elise.userinfocenter.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-02-27 17:34:28
 */
public interface BalanceDao {

    Map<String,Object> queryBalanceByUserId(@Param("userId") Long userId);

    Double queryHqgByUserId(Long userId);
}
