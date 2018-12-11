package com.hq.learningcenter.kuaiji.service.impl;

import com.hq.learningcenter.kuaiji.entity.AppExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.learningcenter.kuaiji.dao.AppMapper;
import com.hq.learningcenter.kuaiji.entity.App;
import com.hq.learningcenter.kuaiji.service.AppService;

import java.util.List;

@Service
public class AppServiceImpl implements AppService {

	@Autowired
	private AppMapper appMapper;

	@Override
	public App findByCodeProvince(String code, String province) {
		AppExample example = new AppExample();
		AppExample.Criteria criteria = example.createCriteria();
		criteria.andCodeEqualTo(code);
		if("A001".equals(code) || "A004".equals(code)) {
			criteria.andProvinceEqualTo("all");
		}
		/*else {
			criteria.andProvinceEqualTo(province);
		}*/
		criteria.andDrEqualTo(0);
		
		return appMapper.selectByExampleFetchOne(example);
	}

	@Override
	public App findByCode(String code) {
		AppExample example = new AppExample();
		example.createCriteria().andCodeEqualTo(code).andCourseidEqualTo("").andDrEqualTo(0);
		return appMapper.selectByExampleFetchOne(example);
	}

	@Override
	public List<App> findListByCode(String code) {
		AppExample example = new AppExample();
		AppExample.Criteria criteria = example.createCriteria();
		criteria.andCodeEqualTo(code).andDrEqualTo(0);
		return appMapper.selectByExample(example);
	}
}
