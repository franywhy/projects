package com.kuaiji.service;

import com.kuaiji.entity.AppProvince;

public interface AppProvinceService {
	
	AppProvince findByProvince(String province);

	AppProvince findByProvinceName(String provinceName);
}
