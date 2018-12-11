package com.hqjy.msg.service.impl;


import com.hqjy.msg.mapper.TestMapper;
import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.model.MsgMessageDetail;
import com.hqjy.msg.service.TestService;
import com.hqjy.msg.util.DateUtils;
import com.hqjy.msg.util.ListUtils;
import com.hqjy.msg.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-12-20 14:20:09
 */
@Service
public class TestServiceImpl  implements TestService {

	@Autowired
	private TestMapper testMapper;

	@Override
	public void batchSaveMsgMessageDetail(String tableName, List<MsgMessageDetail> detailList) {

		if (ListUtils.listIsExists(detailList)) {
			testMapper.batchSave(tableName,detailList);
		}

	}

	@Override
	public List selectAll() {
		return testMapper.selectAll();
	}

	@Override
	public int tableExist(String tableName) {
		Map map = new HashMap();
		map.put("tableName",tableName);
		return testMapper.tableExist(map);
	}

	@Override
	public void createTable(String oldName, String newName) {
		Map map = new HashMap();
		map.put("newTableName",newName);
		map.put("oldTableName",oldName);
		testMapper.createTable(map);
	}

	@Override
	public void check(String oldName, String newName) {
		int num = tableExist(newName);
		if (num <=0) {
			createTable(oldName,newName);
		}
	}

	@Override
	public String getTableName(String after, String oldTableName,Date sendTime) {

		String date = DateUtils.dateToString(sendTime,"YYYYMM");
 		return oldTableName+"_"+date+"_"+after;
	}

	@Override
	public MsgMessageDetail getMsgMessageDetail(String userId, MsgMessage message) {
 		String tableName = getTableSelect(userId,message.getSendTime());
 		Map map = new HashMap();
 		map.put("user_id",userId);
 		map.put("code",message.getCode());
		return testMapper.getMsgDetail(tableName,map);
	}

	@Override
	public int isExistMsgDetail(String userId, MsgMessage message) {
		String tableName = getTableSelect(userId,message.getSendTime());
		Map map = new HashMap();
		map.put("user_id",userId);
		map.put("code",message.getCode());
		return testMapper.isExistMsgDetail(tableName,map);
		//return 0;
	}

	@Override
	public int updateMsgDetailSetReaded(String userId, MsgMessage message) {
		String tableName = getTableSelect(userId,message.getSendTime());

		///return testMapper.isExistMsgDetail(tableName,map);
		return testMapper.updateMsgDetailReaded(tableName,userId,message.getCode());
	}

	@Override
	public int batchUpdateMsgDetailReaded(List lists) {
		return testMapper.batchUpdateMsgDetailReaded(lists);
	}

	@Override
	public String updateMsgDetailSetReadedSql(String userId,String code, Date sendTime) {
		String tableName = getTableSelect(userId,sendTime);
		String sql = " update "+tableName+" set is_read = 1 " +
				" where rec_by = '"+userId.replaceAll("'","")+"' and `code`='"+code.replaceAll("'","")+"'; \n";
		return sql;
	}

	@Override
	public List<MsgMessageDetail> getDetailsByUserAndTimes(String userId, String startTime, String endTime, Map map) {

		List tables = new ArrayList();
		String startDate = DateUtils.dateToString(DateUtils.stringToDate(startTime,DateUtils.DATE_FORMAT),DateUtils.DATE_FORMAT_MOUNTH);
		String endDate = DateUtils.dateToString(DateUtils.stringToDate(endTime,DateUtils.DATE_FORMAT),DateUtils.DATE_FORMAT_MOUNTH);
		String table = getTableSelect(userId,DateUtils.stringToDate(startTime,DateUtils.DATE_FORMAT));
		if (tableExist(table)>0) {
			tables.add(table);
		}
		if (!startDate.equals(endDate)){
			//tables.add(getTableSelect(userId,DateUtils.stringToDate(startTime,DateUtils.DATE_FORMAT)));
			Date date = DateUtils.addMonths(DateUtils.stringToDate(startTime,DateUtils.DATE_FORMAT),1);
			String date1 = DateUtils.dateToString(date,DateUtils.DATE_FORMAT_MOUNTH);
			while(!date1.equals(endDate))
			{
				table = getTableSelect(userId,date);
				if (tableExist(table)>0) {
					tables.add(table);
				}

				date = DateUtils.addMonths(date,1);
				date1 = DateUtils.dateToString(date,DateUtils.DATE_FORMAT_MOUNTH);
			}
			table = getTableSelect(userId,DateUtils.stringToDate(endTime,DateUtils.DATE_FORMAT));
			if (tableExist(table)>0) {
				tables.add(table);
			}
		}

 		System.out.println(StringUtils.objToJsonStr(tables));

		return testMapper.getDetailsByUserAndTimes(userId,tables,null);
	}

	private String getTableSelect(String userId,Date sendTime){
		Integer num = -1;
		if (StringUtils.isInteger(userId)) {
			num =  Integer.valueOf(userId)%3;
		}else {
			num =  (userId).length()%3;
		}
		String tableName = getTableName(String.valueOf(num),"msg_message_detail",sendTime);
		return tableName;
	}
}
