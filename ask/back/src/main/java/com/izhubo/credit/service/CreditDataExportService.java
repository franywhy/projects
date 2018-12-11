package com.izhubo.credit.service;

import java.util.Date;
import java.util.List;

import com.mysqldb.model.CreditPercentDetail;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordReportTemp;
import com.mysqldb.model.CreditStandard;
import com.mysqldb.model.Creditpercent;

/**
 * 学分数据导出功能
 * @author lintf
 * 	2017年11月21日14:07:20
 *
 */
public interface CreditDataExportService {

	/**
	 * 根据报名日期取得档案单
	 * 
	 */
	List<CreditRecordReportTemp> getRecordbyRegdate(String beginDate,String endDate) throws Exception ;
	/**
	  * 根据月份及考核算日期取得学员明细 ，如果考核日期为空时则算全部月份
	  * @param dbilldate  月份
	  * @param beginDate 报名开始月份
	  * @param endDate 报名结束月份
	  * @return
	  */
	List<CreditPercentDetail> getPercentDetailbyDbilldate(String dbilldate,String beginDate,String endDate) throws Exception ;
/**
 * 取得学分完成率报表数据
 * 
 * @param beginDate
 * @param endDate
 * @return
 * @throws Exception
 */
	List<Creditpercent> getPercentReport(String dbilldate,String beginDate,String endDate) throws Exception ;

	
	
	List<CreditRecord>  CheckNcDateByRegdate(String beginDate,String endDate );
	 
	List<CreditRecord>  getTest(String beginDate,String endDate);
	
	}
