package com.izhubo.credit.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.izhubo.credit.service.CreditDelSameBillService;
import com.izhubo.credit.util.CreditUtil;
import com.mysqldb.model.CreditNcData;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordSignCVO;
import com.mysqldb.model.CreditRecordTiKuScore;

public class CreditDelSameBillServiceImpl implements CreditDelSameBillService {

	 
	@Override
	public List<CreditRecord> getSameRecordByCID(Session session) {
		List<CreditRecord> root = new ArrayList<CreditRecord> ();
	 	String sql="select d.id from credit_record d where d.sign_id_c in ( "+
                 	 "select d.sign_id_c from credit_record d  "+
					 "group by d.sign_id_c "+
					 "having count(d.id)>1)   "+
					 "order by d.sign_id_c,d.ts desc "; 
		
/* String sql=
  " select d.id from credit_record d where concat(d.nc_user_id,d.subject_type) in (   " +
		  " select concat(d.nc_user_id,d.subject_type) from (  " +

		  " select d.nc_user_id,d.subject_type from credit_record  d   " +
		  " group by d.nc_user_id,d.subject_type  " +
		  " having count(d.id)>1  " +
		  " ) d   " +
		  " )    " +
		  " order by d.nc_user_id  "; */


		
		
		
		SQLQuery query=	 session.createSQLQuery(sql)  ;
		List ids=query.list();
		 if (ids!=null&&ids.size()>0){
		List<Integer> idslist= new ArrayList<Integer>();
		 for (Object o:ids){
			 if (o!=null){
				 idslist.add(Integer.valueOf( o.toString()));
			 }
		 }
		 
		 if(idslist!=null&&idslist.size()>0){
			 List<CreditRecord> dvo= session.createQuery( " from CreditRecord where id in (:studentids) ").setParameterList("studentids", idslist).list();
		     System.out.println("取得档案单大小"+dvo.size());
			 return dvo;
		 }
		 
		 
		 }
		
		 
		 
		 
		return null;
	}
	@Override
	public void ComputeRecordByCid(List<CreditRecord> sourcedate,
			List<CreditRecord> Delvo, List<CreditRecord> UpdateVo) {
		//key为cid+enable+userid+subject
		Map<String,CreditRecord> root=new HashMap<String,CreditRecord>();
			
		
		
	 if (sourcedate!=null&&sourcedate.size()>0){
		
	     	for (CreditRecord dvo:sourcedate){
	     		if (dvo!=null){
	     			String cid=dvo.getSignIdc()==null?"~":dvo.getSignIdc();
	     			String enable=dvo.getIsEnable()==null?"0":dvo.getIsEnable().toString();
	     			String studentid=dvo.getStudentId()==null?"~":dvo.getStudentId();
	     			String subject=dvo.getSubjectId()==null?"~":dvo.getSubjectId();
	     			String subject_type=dvo.getSubjectType()==null?"~":dvo.getSubjectType();
	     		
	     			String key= studentid+subject_type;
	     			CreditRecord n_dvo=root.get(key);
	     			if (n_dvo==null){ //如果为空说明这个是没有重复的 直接 存到root中
	     				root.put(key, dvo);
	     			}else { //如果有重复的进入比对  删除后来者 并将root中的数值取到最大
	     				Delvo.add(dvo);
	     				CreditUtil.CompareSameRecord(n_dvo,dvo);
	     				root.put(key, n_dvo);
	     				
	     			}
	     			
	     			
	     			
	     		}
	    	}
	     	 if (root!=null&&root.size()>0){
	     		 for(Entry<String, CreditRecord> updatevo:root.entrySet()){
	     			 if (updatevo.getValue()!=null){
	     				UpdateVo.add(updatevo.getValue());
	     			 }
	     			
	     		 }
	     	 }
	     	 
	     	
	     	
	 }
		
	}
	@Override
	public void delSameRecordBill(Session session, Transaction tx,
			List<CreditRecord> Delvo, List<CreditRecord> UpdateVo) {
		System.out.println("修改"+UpdateVo.size()+"个，删除"+Delvo.size()+"个.");
	 	if (Delvo!=null&&Delvo.size()>0){
	 		for (CreditRecord d:Delvo){
	 			d.setIsEnable(96);
	 			session.save(d);
	 		}
	 	}
		
		if (UpdateVo!=null&&UpdateVo.size()>0){
	 		for (CreditRecord d:UpdateVo){
	 		 
	 			session.save(d);
	 		}
	 	}
		 
	}
	@Override
	public void delSameTikuScoreByBillkey(Session session,Transaction tx) {
        //第一个取到的数据为更新数据，之后进来的到删除列表中，分数取最大值
		Map<String,CreditRecordTiKuScore> updateData= new HashMap<String,CreditRecordTiKuScore>();
		Map<String,CreditRecordTiKuScore> delData= new HashMap<String,CreditRecordTiKuScore>();

		String sql=
            " select t.id from credit_record_tikuscore t where t.billkey in ( "
				+ " 	select c.billkey from ( "
				+ " select  tk.billkey from  credit_record_tikuscore tk  "
				+ " 	group by tk.billkey " + " 	having count(tk.id)>1 " 
				+ " ) c ) " ; 
				 
		SQLQuery query=	 session.createSQLQuery(sql)  ;
		List ids=query.list();
		 if (ids!=null&&ids.size()>0){
		List<Integer> idslist= new ArrayList<Integer>();
		 for (Object o:ids){
			 if (o!=null){
				 idslist.add(Integer.valueOf( o.toString()));
			 }
		 }
		 
		 if(idslist!=null&&idslist.size()>0){
			 List<CreditRecordTiKuScore> dvo= session.createQuery( " from CreditRecordTiKuScore where id in (:studentids) ").setParameterList("studentids", idslist).list();
		   
			 
			 		if (dvo!=null&&dvo.size()>0){
			 			//开始遍历全部来源 
			 			for(CreditRecordTiKuScore t:dvo){
			 						CreditRecordTiKuScore sub= updateData.get(t.getBillKey());
			 				if (sub!=null){//取到说明是已经存在的了 要进到删除列表
			 					t.setDr(1);
			 					delData.put(t.getBillKey(), t);//放到删除列表
			 					 
			 					double newScore=	CreditUtil.Double_half_up(t.getStandarRate()==null?0.0:t.getStandarRate());
				 				double oldScore=	CreditUtil.Double_half_up(sub.getStandarRate()==null?0.0:sub.getStandarRate());
				 				if (newScore>oldScore){
				 					sub.setStandarRate(newScore);
				 				}else {
				 					sub.setFirstRate (newScore);
				 				}
				 				updateData.put(t.getBillKey(), sub); //将更新后的再次保存回去
			 					
			 					
			 				}else {
			 					updateData.put(t.getBillKey(), t);
			 				}
			 				
			 				
			 				
			 			}
			 			
			 			//遍历之后保存
			 			
			 			  if (updateData!= null&&updateData.size()>0){
			 				  for (Entry<String, CreditRecordTiKuScore> u:updateData.entrySet()){
			 					 CreditRecordTiKuScore tkvo=u.getValue();
			 					  session.save(tkvo);
			 				  }
			 			  }
			 			
			 			  if (delData!= null&&delData.size()>0){
			 				 for (Entry<String, CreditRecordTiKuScore> u:delData.entrySet()){
			 					 CreditRecordTiKuScore tkvo=u.getValue();
			 					  session.save(tkvo);
			 				  }
			 			  }
			 			  tx.commit();
			 		 }
			 
			 
		 }
		 
		 
		 }
		
	}
	@Override
	public void checkNCdate(Session session, Transaction tx,String beginDate,String endDate) {
		 List<CreditRecord> Creditrecords=session.createQuery( 
		" from CreditRecord where enable=0 and signDate>=:beginDate and signDate<=:endDate ")
		.setParameter("beginDate", beginDate)
		.setParameter("endDate", endDate).list();
		 Map<String,CreditRecord> creditmap= new HashMap<String,CreditRecord>();
		 List<CreditNcData> updateNC= new  ArrayList<CreditNcData>();
		 if (Creditrecords==null||Creditrecords.size()==0){
			 return;
		 }
		 for (CreditRecord d:Creditrecords){
			 String key=d.getStudentId()+d.getSubjectType();
			 creditmap.put(key, d);
			 
		 }
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
		    	 d.setType(98);
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
		    	 session.update  (a);
		      }
	 
		
	}
	@Override
	public void delSameSignCvoByCid(Session session, Transaction tx) {
		List<CreditRecordSignCVO> root = new ArrayList<CreditRecordSignCVO> ();
	 	String sql="select c.id from credit_record_sign_c c where c.sign_id_c in ( "+
                 	 "select c.sign_id_c from credit_record_sign_c c  "+
					 "group by c.sign_id_c "+
					 "having count(c.id)>1)   "+
					 "order by c.sign_id_c,c.ts desc "; 
	 	
	 	SQLQuery query=	 session.createSQLQuery(sql)  ;
		List ids = query.list();
		if (ids != null && ids.size() > 0) {
			List<Integer> idslist = new ArrayList<Integer>();
			for (Object o : ids) {
				if (o != null) {
					idslist.add(Integer.valueOf(o.toString()));
				}
			}
			 if(idslist!=null&&idslist.size()>0){
				 List<CreditRecordSignCVO> dvo= session.createQuery( " from CreditRecordSignCVO where id in (:studentids) ").setParameterList("studentids", idslist).list();
			     System.out.println("取得档案单大小"+dvo.size());
			     		if (dvo!=null&&dvo.size()>0){
			     			
			     			Map<String,CreditRecordSignCVO> cMap=new HashMap<String,CreditRecordSignCVO>();
			     			Map<Integer,CreditRecordSignCVO> cMap_del=new HashMap<Integer,CreditRecordSignCVO>();
			     			
			     			for (CreditRecordSignCVO c:dvo){
			     				if (c!=null){
			     					CreditRecordSignCVO sub=	cMap.get(c.getSignIdc());
			     					if (sub!=null){
			     						if(sub.getTs()!=null&&sub.getTs().after(c.getTs())){
			     							cMap.put(c.getSignIdc(), c);
			     						}else {
			     							cMap_del.put(c.getId(), c);
			     						}
			     							
			     						
			     						
			     						
			     					}else {
			     						cMap.put(c.getSignIdc(), c);
			     					}
			     				}
			     			}
			     			
			     			 
			     			
                            if (cMap_del!=null&&cMap_del.size()>0){
                            	
                            	for (Entry<Integer,CreditRecordSignCVO> entry:cMap_del.entrySet()){
                            		CreditRecordSignCVO c=	entry.getValue();
                            		//c.setIsEnable(6);
			     					//session.update(c);
                            		session.delete(c);
			     				}
			     			}
			     
			     		}
			     
			     
			     
			 
			 }
			
			

		}
	}
	

}
