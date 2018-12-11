package com.izhubo.credit.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mysqldb.model.CreditRecord;

public interface CreditDelSameBillService {
	/**
	 * 取得重复的CID的档案　
	 * @param session
	 * @return
	 */
	List<CreditRecord> getSameRecordByCID(Session session);
	/**
	 *  检测是否重复
	 * @param sourcedate
	 * @param Delvo 要删除的 档案
	 * @param UpdateVo  要修改的档案
	 */
	void ComputeRecordByCid(List<CreditRecord> sourcedate,List<CreditRecord> Delvo,List<CreditRecord> UpdateVo);		
	/**
	 * 保存数据
	 * @param session
	 * @param tx
	 * @param Delvo
	 * @param UpdateVo
	 */
	void delSameRecordBill(Session session, Transaction tx,List<CreditRecord> Delvo,List<CreditRecord> UpdateVo);

	
	/**
	 * 删除重复的题库成绩单
	 * @param session
	 */
	void  delSameTikuScoreByBillkey(Session session,Transaction tx);
	
	/**
	 * 删除重复的CVO
	 * @param session
	 * @param tx
	 */
	void  delSameSignCvoByCid(Session session,Transaction tx);
	
	void  checkNCdate(Session session,Transaction tx,String beginDate,String endDate);
}
