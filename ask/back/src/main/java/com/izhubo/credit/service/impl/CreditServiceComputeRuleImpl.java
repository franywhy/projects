package com.izhubo.credit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Session;

import com.google.gson.reflect.TypeToken;
import com.izhubo.credit.service.CreditServiceComputeRule;
import com.izhubo.credit.util.CreditDaoUtil;
import com.izhubo.credit.util.CreditUtil;
import com.izhubo.credit.util.DateUtil;
import com.izhubo.credit.util.HttpRequest;
import com.izhubo.credit.util.JsonUtil;
import com.izhubo.credit.util.NcSyncConstant;
import com.izhubo.credit.util.SubjectIdUtil;
import com.izhubo.credit.vo.ArrangedSYNCVO;
import com.izhubo.credit.vo.ClassEndStudentVO;
import com.izhubo.credit.vo.HttpServiceResult;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordNcSore;
import com.mysqldb.model.CreditRecordTiKuScore;
import com.mysqldb.model.CreditStandard;

public class CreditServiceComputeRuleImpl implements CreditServiceComputeRule {

	@Override
	public Map<String,ClassEndStudentVO> getClassEndStudentlist(String beginDate, String endDate,
			String secretkey, Map<String, CreditStandard> creditStandardmap) throws Exception  {
		String result;
		try {
			 
			result = HttpRequest.sendPost(NcSyncConstant.getClassEndStudentUrl(), "syncSDate="+beginDate+"&syncEDate="+endDate+"&secretKey="+secretkey,null);
		} catch (Exception e) {
			return null;
		}
		HttpServiceResult s = JsonUtil.fromJson(result, new TypeToken<HttpServiceResult>() {}.getType());
		 
		List<ClassEndStudentVO> voList = JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<ClassEndStudentVO>>() {}.getType());
		Map<String,ClassEndStudentVO> sourcemap=new HashMap<String,ClassEndStudentVO>();
		 if (voList!=null&&voList.size()>0){
			 for (int i=0;i<voList.size();i++){
				 ClassEndStudentVO  tempvo=voList.get(i);
				 if (tempvo!=null&&tempvo.getSubjectId()!=null){
					 CreditStandard st = creditStandardmap.get(tempvo.getSubjectId());
					 if (st!=null){//只有是学分档案中有科目的 才进入
						 String subjecttype=st.getSubject_type()==null?st.getNc_id():st.getSubject_type();
						 sourcemap.put(subjecttype+tempvo.getStudentId(), tempvo);
					 }
				 }
			 }
		 }

     	return sourcemap;
		
	}

	@Override
	public void getExamPassStudentlistByLastTime(String beginDate,
			String endDate, String secretkey,
			Map<String, CreditStandard> creditStandardmap) {
		// TODO Auto-generated method stub
		
	}
 
  

	@Override
	public void ComputeExamScore_tiku() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SaveCreditRecord(Session session,Map<String, CreditRecord> localmap) throws Exception {
		 if (localmap!=null&&localmap.size()>0){
		for (Entry<String, CreditRecord> entry : localmap.entrySet()) {
			CreditRecord vo= entry.getValue();
    
             session.update (vo);
			 
        		 
        		 
        	
		}
	}
	}

	@Override
	public void ComputeAttendanceScore(Session session,
			Map<String, ClassEndStudentVO> sourcemap,
			Map<String, CreditRecord> localmap)  throws Exception {
	 List<String> studentlist=new ArrayList<String>(sourcemap.keySet());
	 if (studentlist!=null&&studentlist.size()>0){
		 List<CreditRecord> cvolist=  CreditDaoUtil.getLocalDateRecordBySubtypeUserid(session, studentlist, 1000, 3);
				 
				// session.createQuery("from CreditRecord where  CONCAT (subjectType,ncUserId)  in (:alist) ").setParameterList("alist",studentlist).list();;
		 for (int i=0;i<cvolist.size();i++){
			 CreditRecord d= cvolist.get(i);
			 if (d!=null){
				 int AttendanceActualScore =d.getAttendanceActualScore()==null?0:d.getAttendanceActualScore();
				 int AttendanceClaimScore =d.getAttendanceClaimScore()==null?0:d.getAttendanceClaimScore();
				int kqv=d.getkqV()==null?0:d.getkqV();
				 /**
				  * 当出勤率大于等于90时 修得考勤分且重新运算是否通过
				  */
				 if (AttendanceActualScore != AttendanceClaimScore&&kqv>=90){
					 
					 d.setAttendanceActualScore(AttendanceClaimScore);
					  CreditUtil.CheckCreditRecordPass(d);//赋值总分及是否通过
					 
				 }
				 //完课时间
				 ClassEndStudentVO endvo=sourcemap.get(d.getSubjectType()+d.getStudentId());
				 if (endvo!=null&&d.getArrDate()==null&&endvo.getLastDate()!=null){
					d.setArrDate(endvo.getLastDate());
				 }
				 localmap.put(d.getSubjectType()+d.getStudentId(), d);
			 }		 
		 }
	 }
	   	}

	@Override
	public void ComputeScore_tiku(Session session,
			Map<String, ClassEndStudentVO> sourcemap,
			Map<String, CreditRecord> localmap) throws Exception  {
  	 
		Map<String,CreditRecordTiKuScore> passmap= new HashMap<String,CreditRecordTiKuScore> ();
		
		Map<String,CreditRecordTiKuScore> passmapExam= new HashMap<String,CreditRecordTiKuScore> ();
		List<String> studentlist=new ArrayList<String>(sourcemap.keySet());
		int examnum=0;
		int worknum=0;
		 if (localmap!=null&&localmap.size()>0&&studentlist!=null&&studentlist.size()>0){
				//取得全部的成绩单
			 List<CreditRecordTiKuScore> locallist= CreditDaoUtil.getRecordTiKuScoreBySubtypeUserid(session, studentlist, 10000);
					 
					 
	 		 
			 
			 
			 /**
			  * 开始取得通过的学员名单存到passmap中
			  */
			 for (CreditRecordTiKuScore tvo:locallist){
				  
				 //作业完成率90%及以上时作业通过
					 if (tvo!=null&&tvo.getStandarRate()!=null&&tvo.getStandarRate()>=90&&tvo.getBillType()!=null&&tvo.getBillType().equals("W")){
						 
						 passmap.put(tvo.getSubjectType()+tvo.getStudentNCCode(), tvo);
					 }
					 //实操完课考勤60分的考试通过
                     if (tvo!=null&&tvo.getStandarRate()!=null&&tvo.getStandarRate()>=60&&tvo.getBillType()!=null&&tvo.getBillType().equals("E")){
						 
                    	 passmapExam.put(tvo.getSubjectType()+tvo.getStudentNCCode(), tvo);
					 }
				 }
			/**
			 * 遍历结课学员map 将没有修满学分的修完	 
			 */
			 for (Entry<String, CreditRecord> entry : localmap.entrySet()) {
				 CreditRecord nvo=entry.getValue();
				 if (nvo!=null){
					 	 	 int WorkActualScore=nvo.getWorkActualScore()==null?0: nvo.getWorkActualScore();
					 	 	 int WorkClaimScore=nvo.getWorkClaimScore() ==null?0:nvo.getWorkClaimScore();
					 	 	 int ExamActualScore= nvo.getExamActualScore()==null?0:nvo.getExamActualScore();
					 	 	 int ExamClaimScore=nvo.getExamClaimScore()==null?0:nvo.getExamClaimScore();
					 /**
					  * 开始运算作业完成率	 	 
					  */
					 if(WorkActualScore!=WorkClaimScore){//只有没有通过的学员才进行运算
						 CreditRecordTiKuScore passd= passmap.get(entry.getKey());
						 if (passd!=null){ //当能从通过列表中取得
							 nvo.setWorkActualScore(WorkClaimScore); //修完作业学分
							 CreditUtil.CheckCreditRecordPass(nvo);//赋值总分及是否通过
								 
								 examnum++;
							 
							 
						 }
					 }
					 /**
					  * 开始运算实操考试
					  */
					 if (ExamActualScore!=ExamClaimScore){
						 CreditRecordTiKuScore passd= passmapExam.get(entry.getKey());
						 if (passd!=null){ //当能从通过列表中取得
							 nvo.setExamActualScore(ExamClaimScore);  //修完实操学分
							  CreditUtil.CheckCreditRecordPass(nvo);//赋值是否通过
							 
							 examnum++;
						 }
						
					 }
					 /**
					  * 最后将运算结果put
					  */
					 localmap.put(entry.getKey(), nvo);
					 
				 }
			 }
			 System.out.println( "学分档案运算题库成绩单: 运算完成，共要修改作业成绩 "+worknum+" 人，修改实操考试成绩"+examnum+"  人。　结束于"+DateUtil.DateToString(new Date()));
			 worknum=0;
			 examnum=0;
			 
		 }
		
	}

   

	@Override
	public void ComputeScore_NC(Session session,
			Map<String, ClassEndStudentVO> sourcemap,
			Map<String, CreditRecord> localmap) throws Exception  {
		Map<String, CreditRecordNcSore> xfpassmap= new HashMap<String, CreditRecordNcSore>();
		 Map<String, CreditRecordNcSore> ncscorepassmap= new HashMap<String, CreditRecordNcSore>();
	  
		
		 
		
		List<String> studentlist=new ArrayList<String>(sourcemap.keySet());
		Map<String,String> recordstumap=new HashMap<String,String> ();
		 if (localmap!=null&&localmap.size()>0&&studentlist!=null&&studentlist.size()>0){
			 
			 //根据所查出来的localmap中取得学员的userid 
			 for(Entry<String, CreditRecord>entry:localmap.entrySet()){
				 if (entry.getValue()!=null&entry.getValue().getStudentId()!=null){
					 recordstumap.put(entry.getValue().getStudentId(), "");
				 }
			 }
			 
			 List<String> recordstulist=new ArrayList<String>(recordstumap.keySet());
			 
			 		//分别从CreditRecordNcSore中取得相应的考试通过的学员和学分单
			 List<CreditRecordNcSore> locallist= CreditDaoUtil.getNCScoreByUseridWhere(session, studentlist, 
					 1000, " and passScore=100 and examType='xf' ");			
		 	 List<CreditRecordNcSore> cy_list= CreditDaoUtil.getNCScoreByUseridWhere(session, studentlist, 
		 			 1000, " and passScore=100 and examType='cy' ");
			 List<CreditRecordNcSore> cj_list= CreditDaoUtil.getNCScoreByUseridWhere(session, studentlist,
					 1000, " and passScore=100 and examType='cj' ");
			 List<CreditRecordNcSore> zj_shiwu_list= CreditDaoUtil.getNCScoreByUseridWhere(session, studentlist,
					 1000, " and passScore=100 and examType='zj_shiwu' ");
			 List<CreditRecordNcSore> zj_jingjifa_list= CreditDaoUtil.getNCScoreByUseridWhere(session, studentlist, 
					 1000, " and passScore=100 and examType='zj_jingjifa' ");
			 List<CreditRecordNcSore> zj_guangli_list= CreditDaoUtil.getNCScoreByUseridWhere(session, studentlist,
					 1000, " and passScore=100 and examType='zj_guangli' ");
		
			 
			 
			 
			 
			 
				         //开始将取得的成绩单的存到map
			 
						for (CreditRecordNcSore tvo:locallist){
				         	if (tvo!=null){
					     	xfpassmap.put(tvo.getSubjectType()+tvo.getStudentId(), tvo);
				        	}
			         	}
				//从业一通过则是三科通过
						for (CreditRecordNcSore tvo:cy_list){
				         	if (tvo!=null){
				         		ncscorepassmap.put(SubjectIdUtil.ACCOUNTING_ACCOUNTING_COMPUTERIZATION+tvo.getStudentId(), tvo);
				         		ncscorepassmap.put(SubjectIdUtil.ACCOUNTING_BASIC+tvo.getStudentId(), tvo);
				         		ncscorepassmap.put(SubjectIdUtil.ACCOUNTING_FE_PE+tvo.getStudentId(), tvo);

				         	}
			         	}
						//初级通过是两科通过
						for (CreditRecordNcSore tvo:cj_list){
				         	if (tvo!=null){
				         		ncscorepassmap.put(SubjectIdUtil.JUNIOR_ACCOUNTING_PRACTICE+tvo.getStudentId(), tvo);
				         		ncscorepassmap.put(SubjectIdUtil.JUNIOR_BASIC_ECONOMIC_LAW+tvo.getStudentId(), tvo);
					    	
				         	}
			         	}
						 //下面是中级三科的通过 有一科通过则只更新一科
						for (CreditRecordNcSore tvo:zj_shiwu_list){
				         	if (tvo!=null){
				         		ncscorepassmap.put(SubjectIdUtil.MEDIUM_ACCOUNTING_PRACTICE+tvo.getStudentId(), tvo);
				        	}
			         	}
						for (CreditRecordNcSore tvo:zj_jingjifa_list){
				         	if (tvo!=null){
				         		ncscorepassmap.put(SubjectIdUtil.MEDIUM_ECONOMIC_LAW+tvo.getStudentId(), tvo);
				        	}
			         	}
						for (CreditRecordNcSore tvo:zj_guangli_list){
				         	if (tvo!=null){
				         		ncscorepassmap.put(SubjectIdUtil.MEDIUM_FINANCIAL_MANAGEMENT+tvo.getStudentId(), tvo);
				        	}
			         	}
				
				
				
				
				
				
				
				
				
				
				
				 for (Entry<String, CreditRecord> entry : localmap.entrySet()) {
					 CreditRecord nvo=entry.getValue();
					 if (nvo!=null){
				 	 	 int WorkActualScore=nvo.getWorkActualScore()==null?0: nvo.getWorkActualScore();
				 	 	 int WorkClaimScore=nvo.getWorkClaimScore() ==null?0:nvo.getWorkClaimScore();
				 	 	 int ExamClaimScore=nvo.getExamClaimScore()==null?0: nvo.getExamClaimScore();
				 	 	 int ExamActualScore=nvo.getExamActualScore()==null?0:nvo.getExamActualScore();
				 	 	 boolean ExamPass =false;
				 	 	 boolean WorkPass =false;
				 	 	 //当档案中的作业分没有修完且在上面的学分成绩单中有 则修完学分
				 	 	 if (WorkActualScore!=WorkClaimScore&&xfpassmap.get(entry.getKey())!=null){
				 	 		WorkPass=true;
				 	 		nvo.setWorkActualScore(WorkClaimScore);
				 	 	 }
				 	 	 //当档案中的考试分没有修完且在上面的成绩单中有有的  则修完
				 	 	 if (ExamClaimScore!=ExamActualScore&&ncscorepassmap.get(entry.getKey())!=null){
				 	 		ExamPass=true;
				 			 nvo.setExamActualScore(ExamClaimScore);
				 	 	 }
				 	 	 if (WorkPass||ExamPass){//当其中有一个是通过的 则更新
				 	 		 CreditUtil.CheckCreditRecordPass(nvo);
							 localmap.put(entry.getKey(), nvo);
				 	 		 
				 	 	 }
				 	 	 
				 	 
				 	 	 
				 	 	 
					 }
				 }
				
		 }
		
		
		
		
	}

}
