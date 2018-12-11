package com.hq.learningapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.learningapi.dao.AppUserChannelsDao;
import com.hq.learningapi.service.AppUserChannelsService;

/**
 * Created by LiuHai on 2017/12/29
 */

@Service("appUserChannelsService")
public class AppUserChannelsServiceImpl implements AppUserChannelsService {
	@Autowired
	private AppUserChannelsDao appUserChannelsDao;

	@Override
	public List<String> queryChannelIdListByUserId(Long userId) {
		return this.appUserChannelsDao.queryChannelIdListByUserId(userId);
	}
	

}
