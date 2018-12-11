package io.renren.service;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import io.renren.entity.MallOrderEntity;
import io.renren.entity.OrderMessageConsumerEntity;
import io.renren.entity.RecordInfoEntity;
import io.renren.entity.RecordSignEntity;

public interface RecordSignService {
	void saveRecordSign(RecordSignEntity e);
	List< RecordSignEntity> queryList(Map<String,Object> queryMap);
	int queryTotal(Map<String,Object> queryMap);
	void upDateRecordSign (RecordSignEntity e);
	OutputStream RecordSignExport(List<Long> ids);
	/**
	 * 根据订单消息和学员档案运算报读信息
	 *@param r
	 *@param order
	 * @author lintf
	 * 2018年7月31日
	 */
	void RecordSignCheck(RecordInfoEntity r, OrderMessageConsumerEntity order, MallOrderEntity m);
	 
}
