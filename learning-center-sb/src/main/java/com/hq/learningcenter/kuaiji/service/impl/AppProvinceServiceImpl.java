package com.hq.learningcenter.kuaiji.service.impl;

import com.hq.learningcenter.kuaiji.dao.AppProvinceMapper;
import com.hq.learningcenter.kuaiji.entity.AppProvince;
import com.hq.learningcenter.kuaiji.entity.AppProvinceExample;
import com.hq.learningcenter.kuaiji.service.AppProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
