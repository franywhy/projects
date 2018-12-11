package com.hq.learningapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.learningapi.dao.AppCourseBannerDao;
import com.hq.learningapi.pojo.AppCourseBannerPOJO;
import com.hq.learningapi.service.AppCourseBannerService;

@Service("appCourseBannerService")
public class AppCourseBannerServiceImpl implements AppCourseBannerService {
	@Autowired
	private AppCourseBannerDao appCourseBannerDao;
	
	@Override
	public List<Long> queryProductIdListByBisinessId(String businessId) {
		return this.appCourseBannerDao.queryProductIdListByBisinessId(businessId);
	}

	@Override
	public List<AppCourseBannerPOJO> queryBannerList(List<Long> productIdList) {
		return this.appCourseBannerDao.queryBannerList(productIdList);
	}

}
