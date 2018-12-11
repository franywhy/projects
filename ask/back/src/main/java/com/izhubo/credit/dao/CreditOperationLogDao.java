package com.izhubo.credit.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.izhubo.credit.util.CreditUtil;
import com.mysqldb.dao.BaseDaoImpl;
import com.mysqldb.model.CreditOperationLog;
import com.mysqldb.model.CreditRecord;
/**
 *   CreditOperationLog任务运算日志DAO 
 *
 * @author lintf 
 *
 */
@Repository("creditOperationLogDao")

public class CreditOperationLogDao extends BaseDaoImpl<CreditOperationLog>{
	
	public CreditOperationLogDao() {
	
	}
	 @SuppressWarnings("unchecked")
	 @Transactional
	public String  newLog(String taskname, String logstr){
		String logid=CreditUtil.getId();
		
		CreditOperationLog log= new CreditOperationLog();
		log.setTaskname(taskname);
		log.setLogid(logid);		
		log.setStartTime(new Date());
		log.setEndTime(null);
		log.setLogstr(logstr);
		Session session=null;
		try{ //由于在quartz中使用不了getCurrentSession所以改为opensession
	     session=sessionFactory.openSession();  //open session
		Transaction tx=session.beginTransaction();	 
		session.save(log);
		tx.commit();		 
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (session!=null){
				session.close(); //close session
			}
		}
		
		
		return logid;
		
	}
	 
	 public void setLog(String logid, String logstr){
		 
		 
			Session session=null;
			try{//由于在quartz中使用不了getCurrentSession所以改为opensession
		     session=sessionFactory.openSession();
			Transaction tx=session.beginTransaction();	 
		// List<CreditOperationLog> list=
				 
				// findEntitysByHQL(" from CreditOperationLog where LOGID=? ",  logid);
			 

		 String qslq = " from CreditOperationLog where LOGID=? ";
			Query qs = session.createQuery(qslq);
			qs.setString(0, logid);
			List<CreditOperationLog> list=qs.list(); 
				 
				 
		 
		 if (list!=null&&list.size()>0){
			 for (CreditOperationLog c:list){
				 String note=c.getLogstr();
				 c.setLogstr(note+" "+ logstr);
				// updateEntity(c);
				 session.update(c);
			 }
		 }
		 
		 tx.commit();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if (session!=null){
					session.close();
				}
			}
	 
			
		}
	 
	 
	 
 /**
  * 结束写日志
  * @param endTime
  * @param logstr
  * @param logid
  * @return
  */
	public String  closeLog( String logstr,String logid){
		 
		String code="";
		Session session=null;
		try{
	     session=sessionFactory.openSession();
		Transaction tx=session.beginTransaction();	
		 
		 //   List<CreditOperationLog> loglist= findEntitysByHQL(" from CreditOperationLog where LOGID=? ", logid);

		 String qslq = " from CreditOperationLog where LOGID=? ";
			Query qs = session.createQuery(qslq);
			qs.setString(0, logid);
			List<CreditOperationLog> loglist=qs.list(); 
		    
		    
		    if (loglist!=null&&loglist.size()>0){
				CreditOperationLog log=loglist.get(0);
				String newlogstr=log.getLogstr()+"\n 结束:"+logstr;
				log.setLogstr(newlogstr);
				log.setEndTime(new Date());
				//saveEntity(log);
				session.save(log);
				tx.commit();
				code="成保存日志";
			}else {
				code="没有找到日志";
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (session!=null){
				session.close();
			}
		}
 
	return code;
	} 
	 

}
