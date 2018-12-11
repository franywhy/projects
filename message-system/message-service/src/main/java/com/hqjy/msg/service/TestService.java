package com.hqjy.msg.service;


import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.model.MsgMessageDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author baobao
 * @email hq@hq.com
 * @date 2017-12-20 14:20:09
 */

public interface TestService {

	public void batchSaveMsgMessageDetail(String tableName, List<MsgMessageDetail> detailList);

	public List selectAll();

	public  int tableExist(String tableName);

	public void createTable(String oldName,String newName);

	public void check(String oldName,String newName);

	public String getTableName(String after,String oldTableName,Date sendTime);

	public MsgMessageDetail getMsgMessageDetail(String userId,MsgMessage message);
	public int isExistMsgDetail(String userId,MsgMessage message);
	public int updateMsgDetailSetReaded(String userId,MsgMessage message);

	public int batchUpdateMsgDetailReaded( List lists);


	public String updateMsgDetailSetReadedSql(String userId,String code, Date sendTime);

	public List<MsgMessageDetail> getDetailsByUserAndTimes(String userId,String startTime,String endTime,Map map );
		
}
