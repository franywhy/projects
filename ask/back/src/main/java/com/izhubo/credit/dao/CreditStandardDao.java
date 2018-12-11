package com.izhubo.credit.dao;

import org.springframework.stereotype.Repository;

import com.mysqldb.dao.BaseDaoImpl;
import com.mysqldb.model.CreditStandard;

@Repository("creditStandardDao")
public class CreditStandardDao extends BaseDaoImpl<CreditStandard>{

	public CreditStandardDao() {
	
	}

}
