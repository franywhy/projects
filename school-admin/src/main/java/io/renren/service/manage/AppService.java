package io.renren.service.manage;


import io.renren.entity.manage.App;

import java.util.List;

public interface AppService {

	App findByCodeProvince(String code, String province);
	
	App findByCode(String code);

	List<App> findListByCode(String code);
}
