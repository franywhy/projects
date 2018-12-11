package com.izhubo.credit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.izhubo.credit.dao.CreditRecordDao;
import com.izhubo.credit.dao.CreditStandardDao;
import com.izhubo.credit.service.CreditService;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditStandard;

/**
 * 学分业务接口实现
 *
 */
@Service("creditService")
@Transactional
public class CreditServiceImpl implements CreditService {
	
	@Resource
	CreditRecordDao creditRecordDao;
	
	@Resource
	CreditStandardDao creditStandardDao;
	
	@Override
	public List<CreditRecord> getStudentCredit(String studentid) {
		// 2. 查询本地DB获取所有科目数据
		List<CreditRecord> dbCreditRecord =  creditRecordDao.findEntitysByHQL(" from CreditRecord where studentId=? order by isGainCash  desc",studentid);
		return dbCreditRecord;
	}

	@Override
	public int updateStudentCredit(String studentid,String classid) {
		return creditRecordDao.executeSQL("update credit_record set is_gain_cash = 'Y' where student_id = ? and class_id = ?", new Object[]{studentid,classid});
	}


	

	@Override
	public List<CreditStandard> getCreditStandard() {
		return creditStandardDao.getAllEntitys();
	}

	@Override
	public List<CreditRecord> getCreditRecordByStudentId(String studentid) {
		return creditRecordDao.getCreditRecordByStudentId(studentid);
	}





}
