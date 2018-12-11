package com.hq.learningcenter.school.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.learningcenter.school.dao.LcLogDao;
import com.hq.learningcenter.school.entity.LcLogEntity;
import com.hq.learningcenter.school.service.LcLogService;



@Service("lcLogService")
public class LcLogServiceImpl implements LcLogService {

	@Autowired
	private LcLogDao lcLogDao; 
	
	@Override
	public void save(LcLogEntity lcLogEntity) {
		
		lcLogDao.save(lcLogEntity);
	}
	
}
