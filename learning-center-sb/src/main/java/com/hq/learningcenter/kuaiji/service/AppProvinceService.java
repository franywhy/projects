package com.hq.learningcenter.kuaiji.service;

import com.hq.learningcenter.kuaiji.entity.AppProvince;

public interface AppProvinceService {
	
	AppProvince findByProvince(String province);

	AppProvince findByProvinceName(String provinceName);
}
