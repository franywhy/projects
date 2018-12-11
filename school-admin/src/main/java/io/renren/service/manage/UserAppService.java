package io.renren.service.manage;


import io.renren.entity.manage.UserApp;

import java.util.List;

public interface UserAppService {

	UserApp findByCodeAppId(int appId, String code, Long userId);

	UserApp findByCode(String code, Long userId);

	int findByUseridAppid(Long userId, Integer appId);

	void addUserApp(UserApp userApp);

	void addUserAppUpdateAccountDr(UserApp userApp, Integer accountid);

	void addUserAppListUpdateAccountDr(List<UserApp> list, Integer accountid);
}
