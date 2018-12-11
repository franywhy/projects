package com.hq.learningapi.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface StudentCourseDao {

    Set<String> queryCourseNoList(@Param("userId") Long userId, @Param("businessId") String businessId);

    Set<String> queryNcCommodityIdList(@Param("userId") Long userId);

}