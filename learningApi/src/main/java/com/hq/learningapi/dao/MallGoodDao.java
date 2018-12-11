package com.hq.learningapi.dao;

import com.hq.learningapi.pojo.MallGoodDetailPOJO;
import com.hq.learningapi.pojo.MallGoodPOJO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by DL on 2018/9/10.
 */
@Repository
public interface MallGoodDao {
    //获取商品列表
    List<MallGoodPOJO> getMallGoodList(@Param("goodIdList") List<Long> goodIdList);

    //获取ios内购商品id
    List<Long> queryGoodId();

    MallGoodDetailPOJO getMallGoodDetail(Long goodId);

    int isExistOrder(@Param("userId") Long userId, @Param("goodId")Long goodId);

}
