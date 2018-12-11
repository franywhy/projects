package com.hq.learningapi.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.learningapi.dao.AppConfigDao;
import com.hq.learningapi.entity.AppConfigEntity;
import com.hq.learningapi.service.AppConfigService;




@Service("appConfigService")
public class AppConfigServiceImpl implements AppConfigService {
	@Autowired
	private AppConfigDao appConfigDao;

	@Override
	public String queryValueByKey(Long key) {
		return appConfigDao.queryValueByKey(key);
	}

	@Override
	public AppConfigEntity queryObject(Long id){
		return appConfigDao.queryObject(id);
	}
	
	@Override
	public List<AppConfigEntity> queryList(Map<String, Object> map){
		return appConfigDao.queryList(map);
	}
	
	
}
