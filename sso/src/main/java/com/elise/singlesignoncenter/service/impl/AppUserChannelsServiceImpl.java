package com.elise.singlesignoncenter.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elise.singlesignoncenter.dao.AppUserChannelsDao;
import com.elise.singlesignoncenter.entity.AppUserChannelsEntity;
import com.elise.singlesignoncenter.service.AppUserChannelsService;

/**
 * 
 * @author Created by LiuHai 2018/02/05
 *
 */
@Service("appUserChannelsService")
public class AppUserChannelsServiceImpl implements AppUserChannelsService {
	@Autowired
	private AppUserChannelsDao appUserChannelsDao;
	
	@Override
	public void save(AppUserChannelsEntity entity) {
		this.appUserChannelsDao.save(entity);

	}

}
