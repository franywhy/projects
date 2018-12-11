package com.hq.learningapi.service;

import java.util.List;

import com.hq.learningapi.pojo.AppCourseBannerPOJO;

public interface AppCourseBannerService {

	List<Long> queryProductIdListByBisinessId(String businessId);

	List<AppCourseBannerPOJO> queryBannerList(List<Long> productIdList);

}
