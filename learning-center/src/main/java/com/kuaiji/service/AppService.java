package com.kuaiji.service;

import com.kuaiji.entity.App;

import java.util.List;

public interface AppService {

	App findByCodeProvince(String code, String province);
	
	App findByCode(String code);

	List<App> findListByCode(String code);
}
