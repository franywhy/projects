package io.renren.modules.job.service;

/**
 * 排课计划表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-06 10:45:58
 */
public interface MessageProductorService {
	
	 void pushToMessageQueue();
	/* List<CourseClassplanEntity> queryList(Map<String, Object> map);*/
}
