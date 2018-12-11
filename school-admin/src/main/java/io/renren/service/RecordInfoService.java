package io.renren.service;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import io.renren.entity.MallOrderEntity;
import io.renren.entity.OrderMessageConsumerEntity;
import io.renren.entity.RecordInfoEntity;
import io.renren.pojo.order.OrderPOJO;

public interface RecordInfoService {
	void saveRecordInfo(RecordInfoEntity e);

	void upDateRecordInfo(RecordInfoEntity e);
	
	List< RecordInfoEntity> queryList(Map<String,Object> queryMap);
	/**
	 * 根据电话判断是保存或者更新
	 *@param e
	 * @author lintf
	 * 2018年8月14日
	 */
	int upDateOrSaveByMobile(RecordInfoEntity e);
	
	String RecordInfoImport(List<String[]> dateList);
	OutputStream RecordInfoExport(List<Long> ids);
	/**
	 * 根据订单和报名表消息生成学员档案
	 *@param m
	 *@param message
	 *@return
	 * @author lintf
	 * 2018年7月30日
	 */
	RecordInfoEntity getRecordInfo (MallOrderEntity m, OrderMessageConsumerEntity message);

	
	void CheckRecordInfo(MallOrderEntity m, OrderMessageConsumerEntity message);
	
	
	/**
	 * 这个是用于蓝鲸后台手动新增订单时生成学员档案中的基础信息
	 *@param o
	 * @author lintf
	 * 2018年8月25日
	 */

	void CheckRecordInfoByOrder(MallOrderEntity m);
	
	
	/**
	 * 根据主键取得单条记录
	 *@param queryMap
	 *@return
	 * @author lintf
	 * 2018年8月13日
	 */
	RecordInfoEntity queryObject(Map<String, Object> queryMap);
	/**
	 * 查询总页数
	 *@param map
	 *@return
	 * @author lintf
	 * 2018年8月13日
	 */
	int queryTotal(Map<String, Object> queryMap);

}
