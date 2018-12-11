package com.izhubo.credit.service;

import java.util.List;
import java.util.Map;

import com.mysqldb.model.CreditRecordSign;
 

/**
 * 学分制中特殊情况的修改数据接口
 * @author lintf 
 * 2017年12月19日17:46:39
 *
 */

public interface  CreditDataModefyService {
	/**
	 * 补充档案信息的
	 */
	void SetRecordInfo();
	void getStudentRecord(Map<String,CreditRecordSign> Student_map,List<String> studentids);
}
