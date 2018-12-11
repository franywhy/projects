package com.izhubo.credit.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.izhubo.credit.util.DateUtil;
import com.mysqldb.dao.BaseDaoImpl;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordSign;

@Repository("creditRecordSignDao")
public class CreditRecordSignDao extends BaseDaoImpl<CreditRecordSign>{
	
	public CreditRecordSignDao() {
	
	}
	
	
	
	
	
}