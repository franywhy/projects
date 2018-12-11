package com.hq.learningapi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hq.learningapi.pojo.AppCourseBannerPOJO;

public interface AppCourseBannerDao {

	List<Long> queryProductIdListByBisinessId(@Param("businessId")String businessId);

	List<AppCourseBannerPOJO> queryBannerList(@Param("productIdList")List<Long> productIdList);

}
