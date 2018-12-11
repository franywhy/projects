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
import com.mysqldb.model.CreditRecordSignCVO;

@Repository("creditRecordSignCDao")
public class CreditRecordSignCDao extends BaseDaoImpl<CreditRecordSignCVO>{
	
	public CreditRecordSignCDao() {
	
	}
	public List<CreditRecordSignCVO> findEntitysByHQLS(String hql, Object... params)
	{
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for(int i = 0; i < params.length; i++)
		{
			if ( (params[i] instanceof java.util.ArrayList)  ){
				query.setParameterList("list", (List)params[i]);
			}else {
				query.setParameter(i, params[i]);
			}
			
				
			 
			
			
		}
		return query.list();
	}
	public void UpdateEntitBylist(List<CreditRecordSignCVO> list){
		
		Session session=sessionFactory.openSession();
		Transaction t=session.beginTransaction();
		for (CreditRecordSignCVO c:list){
			session.update(c);
		}
		t.commit();
		session.close();
	}
	
	
}