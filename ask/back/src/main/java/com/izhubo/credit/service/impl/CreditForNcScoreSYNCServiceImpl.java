package com.izhubo.credit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Session;
import org.quartz.SchedulerException;

import com.google.gson.reflect.TypeToken;
import com.izhubo.credit.service.CreditForNcScoreSYNCService;
import com.izhubo.credit.util.CreditDaoUtil;
import com.izhubo.credit.util.CreditUtil;
import com.izhubo.credit.util.DateUtil;
import com.izhubo.credit.util.HttpRequest;
import com.izhubo.credit.util.JsonUtil;
import com.izhubo.credit.util.NcSyncConstant;
import com.izhubo.credit.vo.ArrangedSYNCVO;
import com.izhubo.credit.vo.HttpServiceResult;
import com.izhubo.credit.vo.NCscoreSyncVO;
import com.izhubo.credit.vo.RegistrationSYNCVO;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordNcSore;
import com.mysqldb.model.CreditStandard;

public class CreditForNcScoreSYNCServiceImpl  implements CreditForNcScoreSYNCService {

	@Override
	public List<NCscoreSyncVO> getNCscoreSyncVO(String SYSDATE, String SYEDATE,
			String secretkey) {
		String result;
		try { 
			result = HttpRequest.sendPost(NcSyncConstant.getNCscoreSYNCUrl(), "syncSDate="+SYSDATE+"&syncEDate="+SYEDATE+"&secretKey="+secretkey,null);
		} catch (Exception e) {
			return null;
		}
		HttpServiceResult s = JsonUtil.fromJson(result, new TypeToken<HttpServiceResult>() {}.getType());
		 
		List<NCscoreSyncVO> voList=null;
		 try{
		 voList = JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<NCscoreSyncVO>>() {}.getType());
		 } catch (Exception e) {
			 e.printStackTrace();
				return null;
			}

     	return voList;
	}

	@Override
	public void ComputeSourcedate(List<NCscoreSyncVO> sourcedate,
			Map<String, NCscoreSyncVO> sourcemap,
			Map<String, CreditStandard> creditStandardmap) {
		for (int i=0;i<sourcedate.size();i++){
			NCscoreSyncVO temp = sourcedate.get(i);
			if (temp!=null){
				//1.判断更新源的科目是否在学分制中 如果是在的话 更新科目类型，如果不在的话 更新源dr=1
				CreditStandard d = creditStandardmap.get(temp.getSubjectId());
				  if (d!=null){
					  String subjecttype=d.getSubject_type()==null?d.getNc_id():d.getSubject_type();//取得科目类型 当没有科目类型时取科目pk
					  temp.setSubjectType(subjecttype);
					  
				  }else {
					  temp.setdR(1);
					  temp.setSubjectType("~");
				  }
				  
				  sourcemap.put(temp.getbId(), temp);
				  
				  
			}
		}
		 
		
	}

	@Override
	public void getLocaldateforNCscore(Session session,
			Map<String, NCscoreSyncVO> sourcemap,
			Map<String, CreditRecordNcSore> localmap) {
		
		if (sourcemap!=null&&sourcemap.size()>0){
		try{
			
			
	        	List<String> keylist= new ArrayList<String>(sourcemap.keySet());
	  	  
				 List<CreditRecordNcSore>  cvolist= CreditDaoUtil.getRecordNcScoreBid(session, keylist, 10000);
						 if (cvolist!=null&&cvolist.size()>0){
					     for (int i=0;i<cvolist.size();i++){
					       CreditRecordNcSore d= cvolist.get(i);
					         if (d!=null){
					       	 localmap.put(d.getbId(), d);
					        }		 
				          }
						 }
			
	 
		
		
	 
		 
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
	}

	@Override
	public void ProcessLocalmapTosourcemap(
			Map<String, NCscoreSyncVO> sourcemap,
			Map<String, CreditRecordNcSore> localmap,
			Map<String, CreditRecordNcSore> Addvo,
			Map<String, CreditRecordNcSore> Updatevo) {
		 for(Entry<String, NCscoreSyncVO> entry:sourcemap.entrySet()){//遍历更新源 来比对
			 
			 CreditRecordNcSore temp=localmap.get(entry.getKey());
			 if (temp==null){ //当本地没有这个bid时 判断dr 是否为0 如果是0 则更新 
				 if (entry.getValue().getdR()==0){
					 CreditRecordNcSore newvo =new CreditRecordNcSore(entry.getValue());
			  
					 Addvo.put(entry.getKey(), newvo);
				 }
			 }else if(temp.getId()!=null){ //当本地中有这个bid时
				 if (entry.getValue().getSyTs().after(temp.getSyTs())){//判断更新时间 
					 temp.setdR(entry.getValue().getdR());
					 temp.setSyTs(entry.getValue().getSyTs());
					 temp.setExamType(entry.getValue().getExamType());
					 temp.setPassScore(entry.getValue().getPassScore());
					 temp.setStudentId(entry.getValue().getStudentId());
					 temp.setSubjectId(entry.getValue().getSubjectId());
					 temp.setSubjectType(entry.getValue().getSubjectType());
					 temp.sethId(entry.getValue().gethId());
					 Updatevo.put(entry.getKey(), temp);
				 }
				 
			 }
			 
			 
		 		 }
		
	}

	@Override
	public void PersistenceVO(Session session,
			Map<String, CreditRecordNcSore> Addvo,
			Map<String, CreditRecordNcSore> Updatevo,String noteinfo) {
		
		
		Map<String,String> examTypeStuMap= new HashMap<String,String>();
		int recordchange=0;
		int recordadd=0;
		int recordupdate=0;
		
		if (Addvo!=null&&Addvo.size()>0){
	        for (Entry<String, CreditRecordNcSore> entry : Addvo.entrySet()) {
	        	CreditRecordNcSore vo= entry.getValue();
	    
	             session.merge(vo);
	             recordadd++;
	             //当是100的说明是考试通过的 直接以考试类型+userid存到map 
				 if (vo.getExamType()!=null&&vo.getExamType()!="xf"&&vo.getPassScore()==100){
					 examTypeStuMap.put(vo.getExamType()+vo.getStudentId(), "");
				 }
	        		 
	        		 
	        	
			}
			}
		if (Updatevo!=null&&Updatevo.size()>0){
	        for (Entry<String, CreditRecordNcSore> entry : Updatevo.entrySet()) {
	        	CreditRecordNcSore vo= entry.getValue();
	 
	             session.update(vo);
	             recordupdate++;
	           //当是100的说明是考试通过的 直接以考试类型+userid存到map 
				 if (vo.getExamType()!=null&&vo.getExamType()!="xf"&&vo.getPassScore()==100){
					 examTypeStuMap.put(vo.getExamType()+vo.getStudentId(), "");
				 }
	        		 
	        		 
	        		 
	        	
			}
			}
		
		//将成绩通过的写到档案的分数
		if (examTypeStuMap!=null&&examTypeStuMap.size()>0){
			
			List<String> idkeys= new ArrayList<String>(examTypeStuMap.keySet());
			 List<CreditRecord>  localpass=	CreditDaoUtil.getLocalDateRecordByExamTypeUserid(session, idkeys, 1000, 3);
			 for (CreditRecord d :localpass){
				if (d.getExamActualScore()!=d.getExamClaimScore()){
					d.setExamActualScore(d.getExamClaimScore());
					 session.update(d);
					 recordchange++;
				}
			 }
		}
		
		noteinfo+="NC成绩单运算:"+"增加了【"+recordadd+"】个NC成绩单，修改了【"+recordupdate+"】个NC成绩单,修改了【"+recordchange+"】个档案单。";
	 
		
	}

}
