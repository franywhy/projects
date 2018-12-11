package com.izhubo.credit.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.izhubo.credit.vo.ClassEndStudentVO;
import com.mysqldb.model.CreditOperationLog;
import com.mysqldb.model.CreditPercentDetail;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditStandard;
import com.mysqldb.model.Creditpercent;

/**
 * 生成学分完成单据的接口
 * 1. 根据日期范围取得还没有生成过的月份的单据，以dbilldate+months为key 存为monthlist
 * 2. 从学分档案单中取得报名日期范围在monthlist中的学员存到credit_percent_detail中 存的是每个学 员的详细
 * 3. 从credit_percent_detail取得credit_percent 存成报表 每个校区的数据
 * 4. 
 *  
 *
 *
 *
 */
public interface CreditXfpercentService {
	
	/**
	 * 1.根据查询日期取得还没有录单的月份的 月份
	 * @param session
	 * @param s_months
	 * @param e_months
	 * @param dbilldate
	 * @return 
	 * @return
	 */
	 void getMonthList(Map<String,String> MonthList ,Session session,String s_months,String e_months,String dbilldate,String note ) throws Exception;

	 /**
	  * 2.根据没有录单的月份去调用 getCreditPerentDetail 取得学员明细
	  * @param session
	  * @param s_months
	  * @param e_months
	  * @param dbilldate
	  * @param pid
	  * @return
	  */
	void  getPerentDetailByMonthList(List<CreditPercentDetail> dvos,Session session,String dbilldate,Map<String,String> monthlist, String pid,String note) throws Exception;
	
	/**
	 * 根据学员月份取学员明细
	 * @param session
	 * @param s_months
	 * @param e_months
	 * @param dbilldate
	 * @param pid
	 * @return
	 */
	void getCreditPerentDetail(List<CreditPercentDetail> dvos,Session session,String s_months,String e_months,String dbilldate , String pid,String note) throws Exception;
	/**
	 * 2.生成校区汇总
	 * @param session
	 * @param pvos
	 * @return
	 */
	List<Creditpercent> 	CreateCreditPerentReport(Session session,List<CreditPercentDetail> dvos,String dbilldate,String note) throws Exception;
	
	
	
	/**3.保存报表数据
	 * @param session
	 * @param dvos
	 * @param pvos
	 * @param dbilldate
	 * @return
	 */
	String  SaveAllData(Session session,  Transaction txreg,List<CreditPercentDetail> dvos ,List<Creditpercent> pvos ,String dbilldate,String pid,Map<String,String> monthlist,String note,CreditOperationLog log) ;
 
		
}
