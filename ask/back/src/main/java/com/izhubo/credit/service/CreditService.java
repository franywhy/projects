package com.izhubo.credit.service;

import java.util.List;

import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditStandard;

/**
 * 学分业务接口
 *
 */
public interface CreditService {

	/**
	 * 获取学员的学分
	 * 
	 */
	List<CreditRecord> getStudentCredit(String studentid);

	
	/**
	 * 更新学员是否获取现金卷
	 * @param classid 
	 * 
	 */
	int updateStudentCredit(String studentid, String classid);
	


	
	/**
	 * 查询标准科目学分
	 * @return
	 */
	List<CreditStandard> getCreditStandard();

	/**
	 * 获取学员的学分信息
	 * 
	 */
	List<CreditRecord> getCreditRecordByStudentId(String studentid);
}
