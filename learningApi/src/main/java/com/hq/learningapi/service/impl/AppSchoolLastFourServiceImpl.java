package com.hq.learningapi.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.learningapi.dao.AppSchoolLastFourDao;
import com.hq.learningapi.pojo.AppSchoolPOJO;
import com.hq.learningapi.service.AppSchoolLastFourService;

@Service("appSchoolLastFourService")
public class AppSchoolLastFourServiceImpl implements AppSchoolLastFourService {
	@Autowired
	private AppSchoolLastFourDao appSchoolLastFourDao;
	
	@Override
	public List<AppSchoolPOJO> querySchoolList(String businessId, double longitude, double latitude) {
		return this.appSchoolLastFourDao.querySchoolList(businessId, longitude, latitude);
	}

}
