package com.hq.learningcenter.school.service;

import com.hq.learningcenter.school.entity.LcLogEntity;

/**
 * 学习中心日志
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-29 09:57:44
 */
public interface LcLogService {

	void save(LcLogEntity sysLog);
	
}
