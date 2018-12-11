package io.renren.modules.job.service.impl;

import io.renren.modules.job.dao.ClassToTkLogDao;
import io.renren.modules.job.entity.ClassToTkLogEntity;
import io.renren.modules.job.service.ClassToTkLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service("classToTkLogService")
public class ClassToTkLogServiceImpl implements ClassToTkLogService {
	@Autowired
	private ClassToTkLogDao classToTkLogDao;

	@Override
	public void save(ClassToTkLogEntity classToTkLog){
		classToTkLogDao.save(classToTkLog);
	}
	
}
