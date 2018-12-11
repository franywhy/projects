package com.hq.learningapi.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by DL on 2018/10/26.
 */
@Repository
public interface AdaptiveGoodsDao {

    List<Long> queryGoodId(Map<String, Object> map);

    //用户是否存在自适应商品的订单
    int isExistOrder(@Param("userId") Long userId, @Param("goodIdList")List<Long> goodsList);
}
