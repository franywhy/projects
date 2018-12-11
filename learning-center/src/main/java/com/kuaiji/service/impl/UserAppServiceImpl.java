package com.kuaiji.service.impl;

import com.kuaiji.dao.AppAccountMapper;
import com.kuaiji.dao.UserAppMapper;
import com.kuaiji.entity.AppAccount;
import com.kuaiji.entity.UserApp;
import com.kuaiji.entity.UserAppExample;
import com.kuaiji.service.AppAccountService;
import com.kuaiji.service.UserAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserAppServiceImpl implements UserAppService {

	@Autowired
	private UserAppMapper userAppMapper;

	@Autowired
	private AppAccountMapper appAccountMapper;

	@Autowired
	private AppAccountService appAccountService;

	@Override
	public UserApp findByCodeAppId(int appId, String code, Long userId) {
		UserAppExample example = new UserAppExample();
		example.createCriteria().andAppidEqualTo(appId).andCodeEqualTo(code).andUseridEqualTo(userId).andDrEqualTo(0);
		return userAppMapper.selectByExampleFetchOne(example);
	}

	@Override
	public UserApp findByCode(String code, Long userId) {
		UserAppExample example = new UserAppExample();
		example.createCriteria().andCodeEqualTo(code).andUseridEqualTo(userId).andDrEqualTo(0);
		return userAppMapper.selectByExampleFetchOne(example);
	}

	@Override
	public int findByUseridAppid(Long userId, Integer appId) {
		UserAppExample example = new UserAppExample();
		example.createCriteria().andUseridEqualTo(userId).andAppidEqualTo(appId);
		return userAppMapper.countByExample(example);
	}

	@Override
	@Transactional(value = "transactionManager",readOnly = false)
	public void addUserApp(UserApp userApp) {
		userAppMapper.insert(userApp);
	}

	@Override
	@Transactional(value = "transactionManager",readOnly = false)
	public void addUserAppUpdateAccountDr(UserApp userApp, Integer accountid) {
		int result = appAccountMapper.updateDrByPrimaryKey(1, accountid);
		if(1 == result) {
			userAppMapper.insert(userApp);
		} else {
			AppAccount appAccount = appAccountService.findIsStudentByAppId(userApp.getAppid());
			if(appAccount != null) {
				appAccountMapper.updateDrByPrimaryKey(1, appAccount.getAccountid());
				userApp.setUsername(appAccount.getUsername());
				userApp.setUserpass(appAccount.getUserpass());
				userAppMapper.insert(userApp);
			}
		}
	}

	@Override
	@Transactional(value = "transactionManager")
	public void addUserAppListUpdateAccountDr(List<UserApp> list, Integer accountid) {
		int result = appAccountMapper.updateDrByPrimaryKey(1, accountid);
		if(1 == result) {
			userAppMapper.insertBatch(list);
		}
	}
}
