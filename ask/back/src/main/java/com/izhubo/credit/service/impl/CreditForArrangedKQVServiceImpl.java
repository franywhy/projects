package com.izhubo.credit.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Session;

import com.google.gson.reflect.TypeToken;
import com.izhubo.credit.service.CreditForArrangedKQVService;
import com.izhubo.credit.util.CreditDaoUtil;
import com.izhubo.credit.util.CreditUtil;
import com.izhubo.credit.util.HttpRequest;
import com.izhubo.credit.util.JsonUtil;
import com.izhubo.credit.util.NcSyncConstant;
import com.izhubo.credit.vo.ArrangedSYNCVO;
import com.izhubo.credit.vo.ClassEndingAttendanceVO;
import com.izhubo.credit.vo.HttpServiceResult;
import com.izhubo.credit.vo.RegistrationSYNCVO;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordSign;
import com.mysqldb.model.CreditStandard;

public class CreditForArrangedKQVServiceImpl implements CreditForArrangedKQVService {
	 
	
	
	
	
	
	
/**
 * 第一步 从同步接口中取得每天的考勤有变更的学员 和每天变动的报名表中2017年报名且2016年有报名表的学员的2017年以前的考勤
 */
	@Override
	public List<ArrangedSYNCVO> getArrangedSYNCVO(String SYSDATE,
			String SYEDATE, String secretkey) {


		String result;
		List<ArrangedSYNCVO> voList=null;
		String result_old;
		List<ArrangedSYNCVO> voList_old=null;
		try {
		//取两个接口的信息 一个是每天考勤变动的学员，一个是每天变动的2017年的学员的2016年的考勤
			result = HttpRequest.sendPost(NcSyncConstant.getArrangedKQVSYNCUrl(), "syncSDate="+SYSDATE+"&syncEDate="+SYEDATE+"&secretKey="+secretkey,null);
			result_old = HttpRequest.sendPost(NcSyncConstant.getArrangedKQVSYNCUrlOld(), "syncSDate="+SYSDATE+"&syncEDate="+SYEDATE+"&secretKey="+secretkey,null);
			if (result!=null&&result.length()>0){
				HttpServiceResult s = JsonUtil.fromJson(result, new TypeToken<HttpServiceResult>() {}.getType());
				 
				 voList = JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<ArrangedSYNCVO>>() {}.getType());
				
			}
			
			if (result_old!=null&&result_old.length()>0){
				HttpServiceResult s = JsonUtil.fromJson(result_old, new TypeToken<HttpServiceResult>() {}.getType());
				 
				voList_old = JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<ArrangedSYNCVO>>() {}.getType());
				if (voList_old!=null&&voList_old.size()>0){
					voList.addAll(voList_old);
				}
			}
		
		} catch (Exception e) {
			return null;
		}

     	return voList;

	}
	
	/**
 * 第二步 根据来源的list转成map，判断是否为学分科目的 并同时判断来源有多条的时候取dr=0 的那一条
 */
	@Override
	public Map<String, ArrangedSYNCVO> ComputeArrangedSYNCVO(
			List<ArrangedSYNCVO> sourcedate, 
			Map<String, CreditStandard> creditStandardmap,Map<String,String> student_list) {
		
		Map<String, ArrangedSYNCVO> sourcemap= new HashMap<String, ArrangedSYNCVO>();
		
		if (sourcedate!=null&&sourcedate.size()>0){
		for (int i=0;i<sourcedate.size();i++){
			ArrangedSYNCVO temp= sourcedate.get(i);
			String studentid= temp.getStudentId();
			String subjectid= temp.getSubjectId();
			student_list.put(studentid, studentid);
			CreditStandard st= creditStandardmap.get(subjectid);
			if (st!=null){//从学分制中可以取到这个科目 
			String subjecttype=st.getSubject_type()==null?st.getNc_id():st.getSubject_type();//取得科目类型 当没有科目类型时取科目pk	
			
			ArrangedSYNCVO avo=sourcemap.get(subjecttype+studentid);
		         	if (avo==null){//当更新源中没有存过这个学员的同科目的 那么直接加
		         	//	if (temp.getkQ()!=0){
		         			sourcemap.put(subjecttype+studentid, temp);
		         	//	}
		         		
				
		        	}else {//当这个更新源中有相同中的这个学员同科目的更新的话进入判断
		        		if (temp.getdR().equals(avo.getdR())){ //当dr相同时，取分数最大的数
		        			if(temp.getKqV()>avo.getKqV()){ //当temp比已存的avo 的出勤率大时 替换avo
		        				sourcemap.put(subjecttype+studentid, temp); 
		        			}
		        			
		        			
		        		}else{//当dr 不同时 取最小dr的那个
		        			if(Integer.valueOf(temp.getdR())<Integer.valueOf(avo.getdR())){
		        				sourcemap.put(subjecttype+studentid, temp); //当更新源的dr不同时 最小dr的来更新 
		        			}
		        			
		        			
		        			
		        		}
				
		        	}
				
				
				
				
				
				
			}
			
			
			
		}
		
		}
		
		
 
		return sourcemap;
	}
/**
 * 第二步 根据来源map取得学制库中的档案map
 */
	@Override
	public Map<String, CreditRecord> getLocalRecordMap(
			Session session,
			 Map<String,String> student_list) {
		
		 
		 Map<String, CreditRecord> localmap=  new HashMap<String, CreditRecord> ();
		 if (student_list!=null&&student_list.size()>0){
			 List<String> keylist= new ArrayList<String>(student_list.keySet());
			 /**
			   * 由于数据量过大 改成分10000条一次 取科目类型+userid的 不管档案是否停用
			   */
			 List<CreditRecord>  cvolist= CreditDaoUtil.getLocalDateRecordByUserID (session, keylist,1000, 3);
		 				
			 for (int i=0;i<cvolist.size();i++){
				 CreditRecord d= cvolist.get(i);
				 if (d!=null){
					 localmap.put(d.getSubjectType()+d.getStudentId(), d);
				 } 
			 
			  
		         }
			 
		 
	           
		  
  
		
		
		}
		 
		 
		 
		 return localmap;
	}
	
	
	 
	
/**
 *  第三步  比对更新源与学分档案  只取考勤分没有修完的档案，如果来源是1则直接全null 否则更新老师和班级 
 */
	@Override
	public void ComputeRecord(Map<String, ArrangedSYNCVO> sourcemap,Map<String,ArrangedSYNCVO> sourcemap_pass,
			Map<String, CreditRecord> localmap,
			Map<String, CreditRecord> updatemap) {
		 for(Entry<String, CreditRecord> entry:localmap.entrySet()){
			 if(entry.getValue().getAttendanceActualScore()!=entry.getValue().getAttendanceClaimScore()){ //当考勤分没有修满时才会进入到更新
				 ArrangedSYNCVO avo=sourcemap.get(entry.getKey()) ;
				 ArrangedSYNCVO avo_pass=sourcemap_pass.get(entry.getKey()) ;
				 if (avo_pass!=null){
					   //当是2016年的考勤的直接考勤通过			 
						//		 System.out.println("2016年考勤通过"+entry.getValue().getId());
					             entry.getValue().setIsObsoleted(0); //是旧数据
								 entry.getValue().setTeacherId(avo_pass.getTeacherId());
								 entry.getValue().setArrName(avo_pass.getClassName());
								 entry.getValue().setkQ(avo_pass.getkQ());
								 entry.getValue().setrS(avo_pass.getrS());
								 entry.getValue().setkqV(avo_pass.getKqV());
								 entry.getValue().setArrDate("2016-12-31 23:59:59");
								 if (avo_pass.getKqV()>=90){
									 entry.getValue().setAttendanceActualScore(entry.getValue().getAttendanceClaimScore());
									  CreditUtil.CheckCreditRecordPass( entry.getValue());//赋值总分及是否通过
									  updatemap.put(entry.getKey(), entry.getValue());
									  continue; //如果是考勤率达到90的 直接跳出不再去取平常的了
								 }
								  updatemap.put(entry.getKey(), entry.getValue());
								 
								 
								 
								 
								 
								 
							 }
							 
				 if (avo!=null){ 
					 if (avo.getdR().equals("1")){ //当考勤来的是1 为删除
						 entry.getValue().setTeacherId(null);
						 entry.getValue().setArrName(null);
						 entry.getValue().setkQ(0);
						 entry.getValue().setrS(0);
						 entry.getValue().setkqV(0);
						 updatemap.put(entry.getKey(), entry.getValue());
					 }else{
						 entry.getValue().setTeacherId(avo.getTeacherId());
						 entry.getValue().setArrName(avo.getClassName());
						 entry.getValue().setkQ(avo.getkQ());
						 entry.getValue().setrS(avo.getrS());
						 entry.getValue().setkqV(avo.getKqV());
				 
						 updatemap.put(entry.getKey(), entry.getValue());
					 }
				 }
			 }
		 }
		 
		
	}
	/**
	 * 最后一步将有变更的档案持久化
	 */
	@Override
	public void SaveRecord(Session session, Map<String, CreditRecord> updatemap) {
		 	
		if (updatemap!=null&&updatemap.size()>0){
	        for (Entry<String, CreditRecord> entry : updatemap.entrySet()) {
	        	CreditRecord hvo= entry.getValue();
	        	
	        	 
		//		hvo=(CreditRecord) session.merge(hvo);
	             session.update(hvo);
				 
	        		 
	        		 
	        	
			}
			}
			
		
		
	}

	@Override
	public List<ArrangedSYNCVO> getArrangedSYNCVOOld(String SYSDATE,
			String SYEDATE, String secretkey) {
 
		String result_old;
		List<ArrangedSYNCVO> voList_old=null;
		try {
		// 是每天变动的2017年的学员的2016年的考勤
			 	result_old = HttpRequest.sendPost(NcSyncConstant.getArrangedKQVSYNCUrlOld(), "syncSDate="+SYSDATE+"&syncEDate="+SYEDATE+"&secretKey="+secretkey,null);
		 
			
			if (result_old!=null&&result_old.length()>0){
				HttpServiceResult s = JsonUtil.fromJson(result_old, new TypeToken<HttpServiceResult>() {}.getType());
				 
				voList_old = JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<ArrangedSYNCVO>>() {}.getType());
				 
			}
		
		} catch (Exception e) {
			return null;
		}

     	return voList_old;

	}

}
