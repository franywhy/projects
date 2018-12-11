package com.hq.learningcenter.kuaiji.service;

import com.hq.learningcenter.kuaiji.entity.App;

import java.util.List;

public interface AppService {

	App findByCodeProvince(String code, String province);
	
	App findByCode(String code);

	List<App> findListByCode(String code);
}
