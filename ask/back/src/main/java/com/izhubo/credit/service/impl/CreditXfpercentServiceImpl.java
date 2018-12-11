package com.izhubo.credit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.izhubo.credit.service.CreditXfpercentService;
import com.izhubo.credit.util.CreditDaoUtil;
import com.izhubo.credit.util.CreditUtil;
import com.izhubo.credit.util.DateUtil;
import com.izhubo.credit.util.doubleUtilBase;
import com.mysqldb.model.CreditOperationLog;
import com.mysqldb.model.CreditPercentDetail;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.Creditpercent;

public class CreditXfpercentServiceImpl implements CreditXfpercentService {

	@Override
	public void getPerentDetailByMonthList(List<CreditPercentDetail>  dvos,
			Session session, String dbilldate, Map<String, String> monthlist,
			String pid,String note)  throws Exception {
 
	//	List<CreditPercentDetail>  dvos= new ArrayList<CreditPercentDetail> ();
		if (monthlist!=null&&monthlist.size()>0){
			String info="";
			for(Entry<String, String> mlist:monthlist.entrySet()){
				if (mlist.getKey()!=null){
					String months=mlist.getKey();
				 
					 Map<String, Double> permon= CreditUtil.getPercentMonth(session,dbilldate,dbilldate);
						
					 Map<String, Double> permon_pk= CreditUtil.getPaikePercentMonth(session,dbilldate,dbilldate);

					 
					 /**
					 * 当月没有完成率的话则无需计算并退出
					 */
					 if ((permon==null||permon.size()==0)&&(permon_pk==null||permon_pk.size()==0)){
						 note+="当前月份【"+dbilldate+"】没有完成率，无需计算。";
						continue;
					 }else { //如果有完成率 但是当前月份没有的话 则无须运算 
						if ( (permon.get(dbilldate+"_"+months)==null||permon.get(dbilldate+"_"+months).compareTo(new Double("0.0"))<=0)&&
								(permon_pk.get(dbilldate+"_"+months)==null||permon_pk.get(dbilldate+"_"+months).compareTo(new Double("0.0"))<=0)
								
								) {
							continue;
						}
					 }
					
					
					
					List<CreditPercentDetail> temp=	new ArrayList<CreditPercentDetail>();
				    getCreditPerentDetail(temp,session,months,months,dbilldate,pid,info);
				   
					if (temp!=null){
						dvos.addAll(temp);
					}
				}
			}
		}
		 
		
		
		
	 
	}

	
	
	
	@Override
	public void getCreditPerentDetail(List<CreditPercentDetail> dvos,Session session,
			String s_months, String e_months, String dbilldate ,String pid,String note)  throws Exception{
		//List<CreditPercentDetail>  dvos= new ArrayList<CreditPercentDetail> ();
		
		 
		 

 //根据月份取得详细第一天和最后一天
		String lo=DateUtil.getMonthFirstDay(DateUtil.MonthToDate(s_months));
		String hi=DateUtil.getMonthLastDay(DateUtil.MonthToDate(e_months));
    
 		Criteria criteria  = session.createCriteria(CreditRecord.class);
				criteria.add(Restrictions.eq("isEnable", new Integer(0)));	
				criteria.add(Restrictions.eq("isCheck", new Integer(0)));	//只取考核的科目
				criteria.add(Restrictions.between("signDate", lo, hi) );
				
				
				/**
				 * 取得不考核的科目的
				 */
				List<String> notcheck=CreditDaoUtil.getDef(session, "DEFDEB201712110163888888");
				Map<String,String> notcheckmap= new HashMap<String,String>();
				if (notcheck!=null&&notcheck.size()>0){
					for (String sub:notcheck){
						if (sub!=null){
							notcheckmap.put(sub, sub);
						}
					}
					
				}
				/*
				 * 取得自定义的不用加入报表中的科目   财经法规 电算化 和中级三科      
				 */
				List<String> subject=CreditDaoUtil.getDef(session, "DEF201711180162649104479406");
				
				
				if (subject!=null&&subject.size()>0){
				String[] subjects = new String[subject.size()];

				subject.toArray(subjects);
				
				criteria.add( Restrictions.not(Restrictions.in("subjectId",subjects)));
				}
				/**
				 * 取得自定义的过滤的校区 华东华南集训基地
				 */
				List<String> n_org=CreditDaoUtil.getDef(session, "DEF201711180162649104888888");
				if (n_org!=null&&n_org.size()>0){
				String[] n_orgs = new String[n_org.size()];

				n_org.toArray(n_orgs);
				
				criteria.add( Restrictions.not(Restrictions.in("orgId",n_orgs)));
				}
				
				
				
				
				
				List<CreditRecord> list = criteria.list();
			 				for (CreditRecord temp:list){
					if (temp!=null&temp.getId()!=null){
						CreditPercentDetail t=new CreditPercentDetail(temp);
						if (notcheckmap!=null&&notcheckmap.size()>0){
							if (notcheckmap.get(t.getClassId()+"_"+t.getSubjectType())!=null){
								continue;
							} 
						}
						
						
						t.setPid(pid);
						t.setDbilldate(dbilldate);
						t.setDr(0);
						dvos.add(t);
					}
				}
				
				
				
				note+=( "【"+lo+" - "+hi+"取得学员明细" +dvos.size()+"条。】");
				
				 
		//	} catch (Exception e1) {
				// TODO Auto-generated catch block
		//		e1.printStackTrace();
		//	note =note+"取得学员明细数据错误："+e1.getMessage();
				 
		//		return null;
		//	}
			
				
		
				
	 
	}

	@Override
	public List<Creditpercent> CreateCreditPerentReport(Session session,
			List<CreditPercentDetail> dvos,String dbilldate,String note) throws Exception {
		//pmap orgid+报名月份 为key value:List<CreditPercentDetail>
		Map<String, Creditpercent > pmap= new HashMap<String, Creditpercent> ();

		 Map<String, Map<String, String>> org;
			List<Creditpercent> root=null;
		//try {
			org = com.izhubo.credit.util.NcUtilHttp.NcorgMain();
		
		
		 Map<String, Double> permon= CreditUtil.getPercentMonth(session,dbilldate,dbilldate);
		
		 Map<String, Double> permon_pk= CreditUtil.getPaikePercentMonth(session,dbilldate,dbilldate);

		 
		 /**
		 * 当月没有完成率的话则无需计算并退出
		 */
		 if ((permon==null||permon.size()==0)&&(permon_pk==null||permon_pk.size()==0)){
			 note+="当前月份【"+dbilldate+"】没有完成率，无需计算。";
			 return null;
		 }
		 
		 
		 
		 
		 
		 /**
		  * 开始拼凑报表数据
		  */
		 
		for (CreditPercentDetail d:dvos){
	 
			String orgid=d.getOrgId()==null?"~":d.getOrgId();
			
			String months=d.getSignDate().substring(0,7);
			String keys= orgid+months;
			if (pmap.get(keys)==null){ //如果不存在的 说明是新的 
				Creditpercent p= new Creditpercent();
				p.setPid(d.getPid());			 
				p.setOrgId(orgid);			 			
				p.setMonths(months);  //存月份
				p.setDr(0);
				 
				p.setDbilldate(d.getDbilldate());
				
				//存学分完成率中的应修和已修
				p.setClaimScore(d.getClaimScore());
				p.setTotalscore(d.getTotalScore());	
				//存排课率的数据 应排课
				p.setClaimNum(1);
				p.setTotalNum(d.getIsPaike());
				
				String  xqcode=org.get(orgid)==null?"~":org.get(orgid).get("xqcode");
				String  dqname=org.get(orgid)==null?"~":org.get(orgid).get("dqname");
				String  xqname=org.get(orgid)==null?"~":org.get(orgid).get("xqname");
			 //	double mbpercent=permon.get(dbilldate+"_"+d.getDbilldate())==null?0:permon.get(dbilldate+"_"+d.getDbilldate());
			// 	p.setMbpercent(String.valueOf( mbpercent));		
				p.setLargeAreaName(dqname);		
				p.setOrgCode(xqcode);
				p.setOrgName(xqname);
				pmap.put(keys, p);
			}else { //如果存在的则更新
				Creditpercent p=pmap.get(keys);
				int totalScore=p.getTotalscore()+d.getTotalScore();
				int claimScore=p.getClaimScore()+d.getClaimScore();
				int totalNum=p.getTotalNum()+d.getIsPaike();
				int claimNum=p.getClaimNum()+1;
				
				p.setTotalscore(totalScore);
				p.setClaimScore(claimScore);
				p.setTotalNum(totalNum);
				p.setClaimNum(claimNum);
				pmap.put(keys, p);
			}
			
			
		}
		//遍历这个map 计算完成率和是否通过
		
		for (Entry<String, Creditpercent> e:pmap.entrySet()){
			if (e.getValue()!=null&&e.getValue().getDbilldate()!=null){
				Creditpercent p=e.getValue();
				int totalscore=p.getTotalscore()==null?0:p.getTotalscore();
				int claimscore=p.getClaimScore()==null?0:p.getClaimScore();
				int totalnum=p.getTotalNum()==null?0:p.getTotalNum();
				int claimnum=p.getClaimNum()==null?0:p.getClaimNum();
				double mbscore=0;
				double mbnum=0;
				 double mbpercent =permon.get(dbilldate+"_"+p.getMonths())==null?0:permon.get(dbilldate+"_"+p.getMonths());
				 double  mbpercent_pk= permon_pk.get(dbilldate+"_"+p.getMonths())==null?0:permon_pk.get(dbilldate+"_"+p.getMonths());
					
				
				 
				 mbscore= doubleUtilBase.round(doubleUtilBase.mul(claimscore, mbpercent),4);
				 mbnum= doubleUtilBase.round(doubleUtilBase.mul(claimnum, mbpercent_pk),4);
				 if (  CreditUtil.isPass((double)totalscore, (double)claimscore, mbpercent)){
					p.setIspass(0); //是否通过
				 }else {
					 p.setIspass(1);
				 }
				 
				 
				 
				 
				 if (  CreditUtil.isPass((double)totalnum, (double)claimnum, mbpercent_pk)){
						p.setIspaike(0); //是否完成排课
					 }else {
						 p.setIspaike(1);
					 }
					 
				 p.setMbnum(String.valueOf(mbnum));
				 p.setMbscore(String.valueOf(mbscore));
				 
                  p.setXfPercent(doubleUtilBase.setpercentString(doubleUtilBase.div(totalscore, claimscore, 4)));
				  p.setMbpercent(doubleUtilBase.setpercentString(mbpercent));
				  p.setPkpercent (doubleUtilBase.setpercentString(doubleUtilBase.div(totalnum, claimnum, 4)));
				  p.setPkmbpercent (doubleUtilBase.setpercentString(mbpercent_pk));
					
				  
				  
				  
				  
				  if (mbpercent==0){
					 p.setRemark("无须运算");
				 }else{
				 	if (p.getIspass()==0){
						 p.setRemark("是");
					 }else {
					 	 p.setRemark("否");
					 }
				 }
				  if (mbpercent_pk==0){
						 p.setPkremark("无须运算");
					 }else{
					 	if (p.getIspaike()==0){
							 p.setPkremark("是");
						 }else {
						 	 p.setPkremark("否");
						 }
					 }
					 
				  
				  
				  
				  
				  
				  pmap.put(e.getKey(), p);
				 
			}
		}
		//\\
		
		//} catch (Exception e1) {
			// TODO Auto-generated catch block
		//	e1.printStackTrace();
		//	note+="生成报表数据错误："+e1.getMessage();
		//	return root;
		//}
		
		
	 root= new ArrayList<Creditpercent>(pmap.values());
		note+="取得报表数据"+root.size()+"条";
		return root;
	}

	@Override
	public String SaveAllData(Session session,  Transaction txreg, List<CreditPercentDetail> dvos,
			List<Creditpercent> pvos, String dbilldate,String pid,Map<String,String> monthlist,String note,CreditOperationLog log)   {
			//清掉全部相同月份的报表数据dr
		 
	 	try{
	/*	Criteria criteria  = session.createCriteria(Creditpercent.class);
		criteria.add(Restrictions.eq("dr", new Integer(0)));
		criteria.add(Restrictions.eq("dbilldate", dbilldate));
		
		List<Creditpercent> list = criteria.list();
		note+="取得月份【"+dbilldate+"】的数据 "+list.size() +"条。";*/
		//int i=0;
		int d_num=0;
		int p_num=0;
/* 		for (Creditpercent delvo:list){
			delvo.setDr(1);
			delvo.setRemark(pid);
			session.merge(delvo);
			i++;
		} */
		//  note+=" 已经删除 "+i+"条。";
 		 if (dvos!=null&&dvos.size()>0){
			 for (CreditPercentDetail d:dvos){
				 if (d!=null&&d.getPid()!=null){
					 session.save(d);
					 d_num++;
				 }
			 }
		 } 
		 
		 if (pvos!=null&&pvos.size()>0){
			 for (Creditpercent p:pvos){
				 if (p!=null&&p.getPid()!=null){
					 session.save(p);
					 p_num++;
				 }
			 }
		 }
		
		 note+=" 增加报表明细数据 "+d_num+"条,  增加报表汇总数据 "+p_num+"条,保存完成。";
		
		 if (log!=null){
	 		 log.setLogstr( log.getLogstr()+" "+note);
	 	 
	 		 session.save(log);
	 	 }
		
		} catch ( Exception e){
			e.printStackTrace();
			note+= "保存【生成学分完成率单据】错误"+e.getMessage();
			 if (log!=null){
		 		 log.setLogstr( log.getLogstr()+" "+note);
		 	 
		 		 session.save(log);
		 	 }
		
		}
		
		
		return note;
		
		 
	}

	@Override
	public void  getMonthList(Map<String,String> MonthList, Session session, String s_months, String e_months,
			String dbilldate ,String note  )  throws Exception { 
 
	 
			
		/*月份转成日期*/
		        Date startday = DateUtil.MonthToDate(s_months);
				Date endday   = DateUtil.MonthToDate(e_months);
				Map<String,String> MonthList_temp  = DateUtil.getMonthList(startday, endday) ;
	 
	             String no_months="";
	 
		 /*查询本 月全部已经录过的月份的单据*/
		 StringBuilder hql = new StringBuilder();
		 hql.append("select p.months From credit_percent p where p.dbilldate='")
		    .append(dbilldate)
		    .append("' and p.dr=0 GROUP BY p.months");			
		 SQLQuery query = session.createSQLQuery(hql.toString());
		 List<String> l=query.list();
		 
		 /*遍历已经录过的 删除*/
		 if (l!=null&&l.size()>0){
			 for (String temp:l){
				 
					 if (temp!=null){
						 
						  MonthList_temp.remove(temp);
						    no_months+=" "+temp+"";
				 
						 
					 } 
				  
			 }
		 } 
	 
		 if(!no_months.equals("")){
		 note+="月份："+no_months+" 已经有运算的数据，将跳过该月份的数据生成。";
		 }
		 MonthList.putAll( MonthList_temp);
		
		 
	}




	 
	
}
