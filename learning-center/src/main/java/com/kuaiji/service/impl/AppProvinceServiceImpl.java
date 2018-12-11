package com.kuaiji.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuaiji.dao.AppProvinceMapper;
import com.kuaiji.entity.AppProvince;
import com.kuaiji.entity.AppProvinceExample;
import com.kuaiji.service.AppProvinceService;

@Service
public class AppProvinceServiceImpl implements AppProvinceService {

	@Autowired
	private AppProvinceMapper appProvinceMapper;

	@Override
	public AppProvince findByProvince(String province) {
		AppProvinceExample example = new AppProvinceExample();
		example.createCriteria().andNcProvinceCodeEqualTo(province);
		return appProvinceMapper.selectByExampleFetchOne(example);
	}

	@Override
	public AppProvince findByProvinceName(String provinceName) {
		AppProvinceExample example = new AppProvinceExample();
		example.createCriteria().andProvinceNameEqualTo(provinceName);
		return appProvinceMapper.selectByExampleFetchOne(example);
	}
}
