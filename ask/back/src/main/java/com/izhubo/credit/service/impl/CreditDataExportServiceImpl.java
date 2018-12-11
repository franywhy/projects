package com.izhubo.credit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.izhubo.credit.util.doubleUtilBase;
  



























import com.izhubo.credit.dao.CreditExportDao;
import com.izhubo.credit.dao.CreditPercentDao;
import com.izhubo.credit.dao.CreditPercentDetailDao;
import com.izhubo.credit.dao.CreditRecordDao;
import com.izhubo.credit.dao.CreditStandardDao;
import com.izhubo.credit.service.CreditDataExportService;
import com.izhubo.credit.service.CreditService;
import com.izhubo.credit.util.CreditUtil;
import com.izhubo.credit.util.DateUtil;
import com.izhubo.credit.util.doubleUtilBase;
import com.mysqldb.model.CreditPercentDetail;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordReportTemp;
import com.mysqldb.model.CreditRecordSign;
import com.mysqldb.model.CreditStandard;
import com.mysqldb.model.Creditpercent;

/**
 * 学分业务接口实现
 *
 */
@Service("creditDataExportService")
@Transactional
public class CreditDataExportServiceImpl implements CreditDataExportService {
	
 
	
	@Resource
	CreditExportDao creditExportDao;
	@Resource 
	CreditPercentDetailDao creditPercentDetailDao;
	@Resource 
	CreditPercentDao creditPercentDao;
	@Resource
	CreditStandardDao creditStandardDao;
	@Resource
	CreditRecordDao creditRecordDao;
	@Override
	public List<CreditRecordReportTemp> getRecordbyRegdate(String beginDate,
			String endDate) throws Exception {
		Map<String,String> hids= new HashMap<String,String>();
		Map<String,CreditRecordSign> rhvos= new HashMap<String,CreditRecordSign>();
		Map<String,CreditRecordReportTemp> temps= new LinkedHashMap<String,CreditRecordReportTemp>();


		 System.out.println("开始查询"+beginDate+" - "+endDate+" 。 开始时间为"+DateUtil.DateToString( new Date()));
	
		 
		 
		 
		 
		List<CreditRecord> dbCreditRecord =	creditExportDao.findEntitysByHQL( 
				" from CreditRecord where isEnable=0 and signDate between ? and ? order by orgId,studentId,signDate"
				,
			 	new Object[]{beginDate,endDate});
			
		//遍历结果保存到返回列表
		if (dbCreditRecord!=null&&dbCreditRecord.size()>0){
			
			for (CreditRecord temp:dbCreditRecord){
				CreditRecordReportTemp sub= new CreditRecordReportTemp(temp);
				if (sub!=null&&sub.getsignIDC()!=null){
					temps.put(sub.getsignIDC(), sub);
					hids.put(sub.getSignId(), null);
				}
			}
			 
			 
		}
		//\\
		/**
		 * 根据hids取得本地的报名表姓名
		 */
		 if (hids!=null&&hids.size()>0){
			 List<String> studentids= new ArrayList<String>(hids.keySet());
			 List<CreditRecordSign> hvos= creditExportDao.getLocalDateSign_h(  studentids, 5000);
			  if(hvos!=null&&hvos.size()>0){
				  for (CreditRecordSign s:hvos){
					   if (s!=null&&s.getSignId()!=null){
						   rhvos.put(s.getSignId(), s);
					   }
				  }
			  }
		 }
		
		 /**
		  * 取得全部的校区
		  */
	 	 Map<String, Map<String, String>> NcorgMain= com.izhubo.credit.util.NcUtilHttp.NcorgMain();

		 /**
		  * 取得全部的老师名称 
		  */
	 	 Map<String, Map<String, String>> TeacherNameMain= com.izhubo.credit.util.NcUtilHttp.TeacherNameMain("1");
	 	
	 	 
	 	 
	 	 
	 	 
	 	 for  (Entry<String, CreditRecordReportTemp> t:temps.entrySet()){
				 if (t.getValue()!=null){
					 
					 CreditRecordReportTemp new_temp= t.getValue();
					 String  dqname=NcorgMain.get(new_temp.getOrgId())==null?"~":NcorgMain.get(new_temp.getOrgId()).get("dqname");
					 String  xqname=NcorgMain.get(new_temp.getOrgId())==null?"~":NcorgMain.get(new_temp.getOrgId()).get("xqname");
					 String   teacherName=TeacherNameMain.get(new_temp.getTeacherId())==null?null:TeacherNameMain.get(new_temp.getTeacherId()).get("name");
					 String   teacherCode=TeacherNameMain.get(new_temp.getTeacherId())==null?null:TeacherNameMain.get(new_temp.getTeacherId()).get("code");
					 
					 
					 String	studentName="~";
					String	vbillCode="~";
					String  CourseName="~";
					String  dh="~";
					CreditRecordSign hvo=rhvos.get(new_temp.getSignId());
					if (hvo!=null){
						 studentName=hvo.getStudentName();
						 	vbillCode=hvo.getSignCode();
						   CourseName=hvo.getClassName();
						   dh=hvo.getPhone();
					}
					
				   new_temp.setLargeAreaName(dqname);
 
				   new_temp.setOrgName(xqname);
		 
				   new_temp.setStudentName(studentName);
				   new_temp.setClassName(CourseName);
				   new_temp.setPhone(dh);
				   new_temp.setSignCode(vbillCode);
				   new_temp.setTeacherName(teacherName);
				   new_temp.setTeacherCode(teacherCode);
				   
				   temps.put(t.getKey(), new_temp);
				 }
			 }
 
		 if (temps!=null&&temps.size()>0){
				//List<CreditRecordReportTemp> backlist= new ArrayList<CreditRecordReportTemp>( temps.values());
			 List<CreditRecordReportTemp> backlist= new ArrayList<CreditRecordReportTemp>();
				for (Entry<String, CreditRecordReportTemp> s:temps.entrySet()){
					if (s!=null){
						backlist.add(s.getValue());
					}
				}
				
				
				 System.out.println("取得数据 "+backlist.size()+"个, 结束时间为"+DateUtil.DateToString( new Date()) );

				return backlist;
								
		 }else {
			 return null;
		 }
	 
		
	}

	@Override
	public List<CreditPercentDetail> getPercentDetailbyDbilldate(
			String dbilldate,String beginDate,String endDate) throws Exception {
	 
		
		//hids存放全部的hid,rhvos存放的是根据hid查出来的报名表，temps是后面要转的结果
		Map<String,String> hids= new HashMap<String,String>();
		Map<String,CreditRecordSign> rhvos= new HashMap<String,CreditRecordSign>();
		Map<String,CreditPercentDetail> temps= new HashMap<String,CreditPercentDetail>();
		System.out.println("开始执行 "+DateUtil.DateToString(new Date()));
		 List<CreditPercentDetail>  sourece=creditPercentDetailDao.getPercentDetailByDate(dbilldate, beginDate, endDate);
		 	for (CreditPercentDetail d:sourece){
		 		if (d.getsignID()!=null&&d.getsignIDC()!=null){
		 			hids.put(d.getsignID(), "");
		 			temps.put(d.getsignIDC(),d);
		 		}
		 	
		 	}
	 
		 /**
			 * 根据hids取得本地的报名表姓名
			 */
			 if (hids!=null&&hids.size()>0){
				 List<String> studentids= new ArrayList<String>(hids.keySet());
				 List<CreditRecordSign> hvos= creditExportDao.getLocalDateSign_h(  studentids, 5000);
				  if(hvos!=null&&hvos.size()>0){
					  for (CreditRecordSign s:hvos){
						   if (s!=null&&s.getSignId()!=null){
							   rhvos.put(s.getSignId(), s);
						   }
					  }
				  }
			 }
			
			 /**
			  * 取得全部的校区
			  */
		 	 Map<String, Map<String, String>> NcorgMain= com.izhubo.credit.util.NcUtilHttp.NcorgMain();

			 /**
			  * 取得全部的老师名称 
			  */
		 	 Map<String, Map<String, String>> TeacherNameMain= com.izhubo.credit.util.NcUtilHttp.TeacherNameMain("1");
		 	 /**
		 	  * 取得科目名称
		 	  */
		 	  List<CreditStandard> st_list=creditStandardDao.getAllEntitys() ;
		 	  Map<String,CreditStandard> st_map= new HashMap<String,CreditStandard>();
		 	  for (CreditStandard s:st_list){
		 		  	if (s!=null&&s.getNc_id()!=null){
		 		  		st_map.put(s.getNc_id(), s);
		 		  	}
		 	  }
		 	 
		 	 
		 	 for  (Entry<String, CreditPercentDetail> t:temps.entrySet()){
				 if (t.getValue()!=null){
					 
					 CreditPercentDetail new_temp= t.getValue();
					 String  dqname=NcorgMain.get(new_temp.getOrgId())==null?"~":NcorgMain.get(new_temp.getOrgId()).get("dqname");
					 String  xqname=NcorgMain.get(new_temp.getOrgId())==null?"~":NcorgMain.get(new_temp.getOrgId()).get("xqname");
					 String  xqcode=NcorgMain.get(new_temp.getOrgId())==null?"~":NcorgMain.get(new_temp.getOrgId()).get("xqcode");

					 String   teacherName=TeacherNameMain.get(new_temp.getTeacherId())==null?null:TeacherNameMain.get(new_temp.getTeacherId()).get("name");
					 String   teacherCode=TeacherNameMain.get(new_temp.getTeacherId())==null?null:TeacherNameMain.get(new_temp.getTeacherId()).get("code");
						/**
						 * 从学分标准中取数
						 */
					 String subjectName=new_temp.getNcSubjectId()==null?"~":(st_map.get(new_temp.getNcSubjectId()==null?"-":new_temp.getNcSubjectId())).getSubject_name();
					 String subjectCode=new_temp.getNcSubjectId()==null?"~":(st_map.get(new_temp.getNcSubjectId()==null?"-":new_temp.getNcSubjectId())).getCourse_code();
						
					 String	studentName="~";
					String	vbillCode="~";
					String  CourseName="~";
					String  dh="~";
					CreditRecordSign hvo=rhvos.get(new_temp.getsignID());
					if (hvo!=null){
						 studentName=hvo.getStudentName();
						 	vbillCode=hvo.getSignCode();
						   CourseName=hvo.getClassName();
						   dh=hvo.getPhone();
					}
					
				   new_temp.setLargeAreaName(dqname);
				   new_temp.setOrgCode (xqcode);
				   new_temp.setOrgName(xqname);
				   new_temp.setSequenceName(CourseName);
				   new_temp.setStudentName(studentName);
				   new_temp.setSubjectName(subjectName); 
				   new_temp.setSubjectCode(subjectCode);
				   new_temp.setPhone(dh);
				   new_temp.setSignCode(vbillCode);
				   new_temp.setTeacherName(teacherName);
				   new_temp.setTeacherCode(teacherCode);
				   
				   if (new_temp.getClassName()!=null&&new_temp.getClassName().length()>0){
					   new_temp.setPaikeRemark("已排课");
				   }else{
					   new_temp.setPaikeRemark("未排课");
				   }
				   
				   
				   if (new_temp.getIsPass()==0){
					   new_temp.setPassRemark("已合格");
				   }else {
					   new_temp.setPassRemark("未合格");
				   }
				   
				   
				   temps.put(t.getKey(), new_temp);
				 }
			 }
		 	 
		 	 
		 	 if (temps!=null&&temps.size()>0){
					//List<CreditRecordReportTemp> backlist= new ArrayList<CreditRecordReportTemp>( temps.values());
				 List<CreditPercentDetail> backlist= new ArrayList<CreditPercentDetail>();
					for (Entry<String, CreditPercentDetail> s:temps.entrySet()){
						if (s!=null){
							backlist.add(s.getValue());
						}
					}
					
					
					 System.out.println("取得数据 "+backlist.size()+"个, 结束时间为"+DateUtil.DateToString( new Date()) );

					return backlist;
									
			 }else {
				 return null;
			 }
	}

	@Override
	public  List<CreditRecord>  CheckNcDateByRegdate(String beginDate, String endDate) {
		 
		List<CreditRecord> dbCreditRecord =	creditExportDao.getCreditRecordByMonth(null, beginDate, endDate);
		
		 return  creditExportDao.CheckNcDateByRegdate(dbCreditRecord);
		
  
		
	}
/**
 *  取得学分完成率表数据
 */
	@Override
	public List<Creditpercent>  	getPercentReport(String dbilldate,
			String beginDate, String endDate) throws Exception {
		List<Creditpercent> vos=creditPercentDao.getPercentReportByDate(dbilldate, beginDate, endDate);
		List<Creditpercent> result= new ArrayList<Creditpercent>();
		if (vos==null||vos.size()==0){
			return null;
		}

		int areaSumclaim=0;//分大区合计应修
		int areaSumtotal=0;//合计实修
		int areaSumclaim_pk=0;//分大区合计应排课人数
		int areaSumtotal_pk=0;//合计实 际排课人数
		double areaSummb=0.0;
		double areaSummb_pk=0.0;
		
		int allSumclaim=0;//总合计应修
		int allSumtotal=0;//总合计实修
		int allSumclaim_pk=0;//总合计应排课人数
		int allSumtotal_pk=0;//总合计实排课人数
		double allSummb=0.0;
		double allSummb_pk=0.0;
		boolean is_sum=false;//用于定义是要合计
		boolean is_allsum=false;//用于定义是要大合计
		String key=null;
	 
		for(int i=0;i<vos.size();i++){ 

			if (vos.get(i)!=null&&vos.get(i).getId()!=null){
				
				
				Creditpercent sum= new Creditpercent();
				Creditpercent allsum= new Creditpercent();
				Creditpercent t= vos.get(i);
				//取得每项数据用来叠加
				//
				int claimscore= t.getClaimScore()==null?0:t.getClaimScore();
				int totalscore= t.getTotalscore()==null?0: t.getTotalscore();
				int ispass=t.getIspass()==null?1:t.getIspass();
				//排课的数据
				int claimnum= t.getClaimNum()==null?0:t.getClaimNum();
				int totalnum= t.getTotalNum()==null?0: t.getTotalNum();
				int ispaike=t.getIspaike()==null?0:t.getIspaike();
				
				
				
				String mbpercent=t.getMbpercent()==null?"0.00%":t.getMbpercent();
				String xfpercent=t.getXfPercent()==null?"0.00%":t.getXfPercent();
				
				String pkmbpercent=t.getPkmbpercent()==null?"0.00%":t.getPkmbpercent();
				String pkpercent=t.getPkpercent()==null?"0.00%":t.getPkpercent();
				
				String mbscore=t.getMbscore()==null?"0":t.getMbscore();
				String mbnum=t.getMbnum()==null?"0":t.getMbnum();
				
				String remark=t.getRemark()==null?"错误":t.getRemark();
				String pkremark=t.getPkremark()==null?"错误":t.getPkremark();
		//		println "每项取数完成";
				 
				 //目标完成率
				 t.setMbpercent(  mbpercent );
				 //学分完成率 
				 t.setXfPercent( xfpercent );
				 //是否通过 
				 t.setIspass(ispass);
				 //目标完成率
				 t.setPkmbpercent(pkmbpercent);
				 //学分完成率
				t.setPkpercent(pkpercent);
				 //是否通过
				t.setIspaike(ispaike);
				 
				 //是否通过的文字显示
				 t.setRemark(remark);
				 t.setPkremark(pkremark);
				 //根据总分和目标完成率得到的目标分数 用来给后面的合计叠加的
				 t.setMbscore(mbscore);
				 //
				 t.setMbnum(mbnum);
				 
				 
				 
				//每项叠加的赋值 
				
				  allSumclaim=allSumclaim+claimscore;
				  allSumtotal=allSumtotal+totalscore; 			 
				  allSummb=doubleUtilBase.add(allSummb,Double.parseDouble(mbscore));
				  
				  areaSumclaim=areaSumclaim+claimscore;
				  areaSumtotal=areaSumtotal+totalscore;
				  areaSummb=doubleUtilBase.add(areaSummb,Double.parseDouble( mbscore));
				 
				  
				  
				  allSumclaim_pk=allSumclaim_pk+claimnum;
				  allSumtotal_pk=allSumtotal_pk+totalnum;
				  allSummb_pk=doubleUtilBase.add(allSummb_pk,Double.parseDouble(mbnum));
				  
				  areaSumclaim_pk=areaSumclaim_pk+claimnum;
				  areaSumtotal_pk=areaSumtotal_pk+totalnum;
				  areaSummb_pk=doubleUtilBase.add(areaSummb_pk,Double.parseDouble( mbnum));
				 
				  
				  
				  
				   
				  is_allsum=false;
				  is_sum=false;
				
				 
				
				if (i+1==vos.size()){ //先判断是否是最后一个 是最后一个的话直接合计
					is_allsum=true;
					is_sum=true;
					sum.setLargeAreaName(t.getLargeAreaName());
					sum.setOrgName("大区合计");
					sum.setMonths("-");
					sum.setClaimScore(areaSumclaim);
					sum.setTotalscore(areaSumtotal); 
					sum.setClaimNum(areaSumclaim_pk);
					sum.setTotalNum(areaSumtotal_pk);
					
					
					sum.setXfPercent( doubleUtilBase.setpercentString( doubleUtilBase.div(areaSumtotal, (double)areaSumclaim, 4)) );
					sum.setPkpercent(doubleUtilBase.setpercentString( doubleUtilBase.div(areaSumtotal_pk, (double)areaSumclaim_pk, 4)) );
					//总的目标完成率就等于全部的目标分数除以总的应修的分数areaSummb/areaSumclaim
			 	     sum.setMbpercent(doubleUtilBase.setpercentString( doubleUtilBase.div(areaSummb, (double)areaSumclaim, 4) ));
				     sum.setPkmbpercent(doubleUtilBase.setpercentString( doubleUtilBase.div(areaSummb_pk, (double)areaSumclaim_pk, 4) ));
					  
					  
					allsum.setLargeAreaName("全国校区");
					allsum.setOrgName("全国合计");
					allsum.setMonths("-");
					allsum.setClaimScore(allSumclaim);
					allsum.setTotalscore(allSumtotal); 
					allsum.setClaimNum(allSumclaim_pk);
					allsum.setTotalNum(allSumtotal_pk);
					
					
					allsum.setXfPercent( doubleUtilBase.setpercentString( doubleUtilBase.div(allSumtotal, (double)allSumclaim, 4)) );
				    allsum.setPkpercent(doubleUtilBase.setpercentString(doubleUtilBase.div(allSummb_pk, (double)allSumclaim_pk, 4)) );
					//总的目标完成率就等于全部的目标分数除以总的应修的分数areaSummb/areaSumclaim
					allsum.setMbpercent(doubleUtilBase.setpercentString( doubleUtilBase.div(allSummb, (double)allSumclaim, 4) ));
				    allsum.setPkmbpercent(doubleUtilBase.setpercentString( doubleUtilBase.div(allSummb_pk, (double)allSumclaim_pk, 4) ));
				
					/**
					 * 判断学分是否达标的
					 */
					
					  //大区判断是否达标
					if (areaSummb==0||areaSumclaim==0){
						sum.setRemark("无需运算");
					}else {
					   if (areaSumtotal>=areaSummb){
						   sum.setRemark("是");
					   }else {
						   sum.setRemark("否");
					   }
					
					}
					//全国是否达标
					if (allSummb==0||allSumclaim==0){
						allsum.setRemark("无需运算");
					}else {
					   if (allSumtotal>=allSummb){
						   allsum.setRemark("是");
					   }else {
						   allsum.setRemark("否");
					   }
					
					}
					
					
					/**
					 * 判断排课是否达标的
					 */
					
					  //大区判断是否达标
					if (areaSummb_pk==0||areaSumclaim_pk==0){
						sum.setPkremark("无需运算");
					}else {
					   if (areaSumtotal_pk>=areaSummb_pk){
						   sum.setPkremark("是");
					   }else {
						   sum.setPkremark("否");
					   }
					
					}
					//全国是否达标
					if (allSummb_pk==0||allSumclaim_pk==0){
						allsum.setPkremark("无需运算");
					}else {
					   if (allSumtotal_pk>=allSummb_pk){
						   allsum.setPkremark("是");
					   }else {
						   allsum.setPkremark("否");
					   }
					
					}
					
					
					
					
					
					
					
				}else if ( vos.get(i+1)!=null){ //还能取到下一个
				Creditpercent nt= vos.get(i+1);
					if (!t.getLargeAreaName().equals( nt.getLargeAreaName())){// 当下一个不是相同大区时 area合计
						is_sum=true;
						sum.setLargeAreaName(t.getLargeAreaName());
						sum.setOrgName("大区合计");
						sum.setMonths("-");
						sum.setClaimScore(areaSumclaim);
						sum.setTotalscore(areaSumtotal);
						sum.setClaimNum(areaSumclaim_pk);
						sum.setTotalNum(areaSumtotal_pk);
					    sum.setXfPercent( doubleUtilBase.setpercentString( doubleUtilBase.div(areaSumtotal, (double)areaSumclaim, 4)) );
					    sum.setMbpercent(doubleUtilBase.setpercentString(doubleUtilBase.div(areaSummb, (double)areaSumclaim, 4)) );
					    sum.setPkpercent(doubleUtilBase.setpercentString( doubleUtilBase.div(areaSumtotal_pk, (double)areaSumclaim_pk, 4)) );
					    sum.setPkmbpercent(doubleUtilBase.setpercentString( doubleUtilBase.div(areaSummb_pk, (double)areaSumclaim_pk, 4) ));
						
					    
					    if (areaSummb==0||areaSumclaim==0){
							 sum.setRemark("无需运算");
						 }else {
						    if (areaSumtotal>=areaSummb){
								sum.setRemark("是");
							}else {
								sum.setRemark("否");
							}
						 
						 }
						 
						 if (areaSummb_pk==0||areaSumclaim_pk==0){
							 sum.setPkremark("无需运算");
						 }else {
							if (areaSumtotal_pk>=areaSummb_pk){
								sum.setPkremark("是");
							}else {
								sum.setPkremark("否");
							}
						 
						 }
						
						
						
						}
				
				}
				
				result.add(t);
			 
				if (is_sum){
					areaSumclaim=0;
					areaSumtotal=0;
					areaSummb=0;
					areaSumclaim_pk=0;
					areaSumtotal_pk=0;
					areaSummb_pk=0;
					result.add(sum);
					 
				}
				if (is_allsum){
					 
					allSumclaim=0;
					allSumtotal=0;
					allSummb=0;
					allSumclaim_pk=0;
					allSumtotal_pk=0;
					allSummb_pk=0;
					result.add(allsum);
					 
				}
				
				
				
				
				
				
				
			}
		
			
			
		}
	 
		 
	
		
		
		return result;
	}

@Override
public List<CreditRecord> getTest(String beginDate, String endDate) {
	
	 return  creditRecordDao.getTest();
}
	
	
	
	 


}
