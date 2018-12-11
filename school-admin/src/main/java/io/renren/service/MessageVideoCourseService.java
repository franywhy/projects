package io.renren.service;


/**
 * 录播课消息推送到题库
 * @dete 2017年11月23日
 */
public interface MessageVideoCourseService {
	
	//根据日期范围参数或者ncId推送到题库,若均参数为null,则为推送所有历史消息
	void pushToMessageQueueClass(String startDate, String endDate, String orderNo, String commodityIds);
}
