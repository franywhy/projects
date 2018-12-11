package io.renren.service;

public interface MessageDivideClassesService {
	
	void pushToTkDivideClassesMessageQueue(String startDate, String endDate, String commodityIds, String productIds, String watchType);

}
