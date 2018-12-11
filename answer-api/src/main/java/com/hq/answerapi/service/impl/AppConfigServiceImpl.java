package com.hq.answerapi.service.impl;

import com.hq.answerapi.dao.AppConfigDao;
import com.hq.answerapi.entity.AppConfigEntity;
import com.hq.answerapi.service.AppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


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

	@Override
	public AppConfigEntity queryObjectByKey(Long id){
		return appConfigDao.queryObjectByKey(id);
	}
}
