package com.izhubo.credit.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.mysqldb.dao.BaseDaoImpl;
import com.mysqldb.model.CreditNcData;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordSign;

@Repository("creditExportDao")
public class CreditExportDao extends BaseDaoImpl<CreditRecord>{
	
	public CreditExportDao() {
	
	}
	 
	/**
	 * 根据hid 分段取得本地的订单 
	 * @param session
	 * @param idkeys
	 * @param row 多少行分段
	 * @return List<CreditRecordSign> 
	 */
	 public   List<CreditRecordSign> getLocalDateSign_h( List<String> idkeys,int row){
		 List<CreditRecordSign> backlist= new ArrayList<CreditRecordSign>();
		 List<String> tempid= new ArrayList<String>();
		 
		 for (int i=0;i<idkeys.size();i++){
			 tempid.add(idkeys.get(i));
			 
			 if (tempid.size()==row){
				 List<CreditRecordSign> hvolist = sessionFactory.getCurrentSession()
							.createQuery(
									"from CreditRecordSign where  signId in (:alist) ")
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 
				 tempid.clear();
			 }else if (i+1==idkeys.size()){
				 List<CreditRecordSign> hvolist =sessionFactory.getCurrentSession().
							 createQuery(
									"from CreditRecordSign where  signId in (:alist) ")
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 tempid.clear();
			 }
			 
		 }
		 
		return backlist;
		 
	 }
	
	 public   List<Object> getReportDetail(String dbilldate){
		 
		 
		 
		 
		 
		 
		// sessionFactory.getCurrentSession().createSQLQuery( )
		return null;
		 
	 } 
	 
	 public    List<CreditRecord> getCreditRecordByMonth(String dbilldate,String beginDate,String endDate){
		 System.out.println(beginDate+" "+endDate);
		 
		return  findEntitysByHQL(" from CreditRecord where isEnable=0 and signDate>=? and signDate<=? ", new Object[]{beginDate,endDate});
	   
			 
		 } 
	
	 
	 public    List<CreditRecord> CheckNcDateByRegdate(List<CreditRecord> Creditrecords){
		 if (Creditrecords==null||Creditrecords.size()==0){
			 return null;
		 }
		 Map<String,CreditRecord> creditmap= new HashMap<String,CreditRecord>();
		 List<CreditNcData> updateNC= new  ArrayList<CreditNcData>();
		 for (CreditRecord d:Creditrecords){
			 String key=d.getStudentId()+d.getSubjectType();
			 creditmap.put(key, d);
			 
		 }
		 Session session= sessionFactory.getCurrentSession();
		 Transaction tx=   session.beginTransaction();
		 List<String> keys = new ArrayList<String>(creditmap.keySet() );
		 
		 List <CreditNcData> list= session.createQuery( 
 " from CreditNcData   where concat(studentid,subjecttype) in (:alist)")
					.setParameterList("alist", keys).list();
		  
		  System.out.println("取到了"+list.size()+"个数据。");
		  
		  for (CreditNcData d:list){
		    	
		    	 String studentid=d.getstudentid();
		    	 String subject_type=d.getsubjecttype();
		      creditmap.remove(studentid+subject_type);
		    	 d.setDr(1);
		    	//session.update(d);
		    	
		    	updateNC.add(d);
			 	      }
		 
		      List<CreditRecord> root= new ArrayList<CreditRecord>(creditmap.values());
		       for(CreditRecord n:root){
		    	  CreditNcData nc= new CreditNcData();
		    	  nc.setstudentid(n.getStudentId());
		    	  nc.setsubjecttype(n.getSubjectType());
		    	 nc.setDr(0);
		    	 nc.setType(99);
		    	 session.save(nc);
		    	 
		      } 
		      
		      for (CreditNcData a:updateNC){
		    //	  CreditNcData ndd= new CreditNcData(); 
		    	  a.setDr(1);
		    	 // ndd.setstudentid(a.getstudentid());
		    	 // ndd.setsubjecttype(a.getsubjecttype());
		    	 // ndd.setType(88);
		    	 session.delete (a);
		      }
		      
		      
		              tx.commit();
		             
		              System.out.println("完成了");
		        return root;
		 
			 //return  findEntitysByHQL(" from CreditRecord where enable=0 and signDate>=? and signDate<=? ", new Object[]{beginDate,endDate});
		   
				 
			 } 
	
}
