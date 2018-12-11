package com.izhubo.credit.schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.google.gson.reflect.TypeToken;
import com.izhubo.credit.util.BeanUtil;
import com.izhubo.credit.util.CheckParameter;
import com.izhubo.credit.util.HttpRequest;
import com.izhubo.credit.util.JsonUtil;
import com.izhubo.credit.util.NcSyncConstant;
import com.izhubo.credit.util.SubjectIdUtil;
import com.izhubo.credit.vo.AccountingCertificateScoreVO;
import com.izhubo.credit.vo.ArrangedPlanIdVO;
import com.izhubo.credit.vo.ArrangedPlanVO;
import com.izhubo.credit.vo.ClassEndingAttendanceVO;
import com.izhubo.credit.vo.ClassEndingBusyworkVO;
import com.izhubo.credit.vo.CreditOperationParameterVO;
import com.izhubo.credit.vo.HttpServiceResult;
import com.izhubo.credit.vo.JuniorLevelAccountantScoreVO;
import com.izhubo.credit.vo.MediumLevelAccountantScoreVO;
import com.izhubo.credit.vo.RegistrationInfoVO;
import com.izhubo.credit.vo.StudentIdVO;
import com.izhubo.credit.vo.TeacherVO;
import com.izhubo.credit.vo.WorkTheorySoreVO;
import com.izhubo.credit.webservice.WebService1Stub;
import com.izhubo.credit.webservice.WebService1Stub.GetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode;
import com.izhubo.credit.webservice.WebService1Stub.GetStudentOfClassEndingExamComplianceRateByTime;
import com.izhubo.credit.webservice.WebService1Stub.GetStudentOfClassEndingTaskComplianceRateByTime;
import com.izhubo.schedule.PushMsgMainRemainSchedule;
import com.mysqldb.model.CreditFailStudent;
import com.mysqldb.model.CreditOperationLog;
import com.mysqldb.model.CreditOperationTask;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordSign;
import com.mysqldb.model.CreditStandard;

/**
 *  学分运算定时任务
 * 以下成绩明细表的科目没有主键
 * 初级会计实务，经济法基础【初级职称】
 * 中级会计实务、经济法、中级财务管理【中级职称】
 * 会计基础、财经法规与职业道德、初级会计电算化【会计证】
 * @author 严志城
 * @time 2017年3月9日15:41:23
 */
public class OperationTaskSchedule extends QuartzJobBean {
	private static Logger logger = LoggerFactory.getLogger(PushMsgMainRemainSchedule.class);
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		//work(context,null, null,null);
	}
	
 /**
  * @param context
  * @param sessionFactory
 * @param eEDate 考试查询结束日期
 * @param eBDate 考试查询开始日期
 * @param wEDate 作业查询结束日期
 * @param wBDate 作业查询开始日期
 * @param aEDate 出勤查询结束日期
 * @param aBDate 出勤查询开始日期
 * @param eDate 运行日期
 * @param ids 标准学分id
  */
  @SuppressWarnings("unchecked")
public void work(JobExecutionContext context,SessionFactory sessionFactory,CreditOperationParameterVO para,List<Integer> ids ) {
	 /* 
	  logger.info("------------------------运行开始------------------");
	  Date start = new Date();
	  StringBuilder errorMess = new StringBuilder();
	  	if(sessionFactory == null){
	  		//不等于null是直接调用运行，等于null是后台任务调用运行
				if(context != null){
					//后台任务启动时获取sessionFactory
					try {
						SchedulerContext skedCtx = context.getScheduler().getContext();
						sessionFactory = (SessionFactory) skedCtx.get("sessionFactory");
					if(sessionFactory == null){
						String info = "学分运算:通过Scheduler获取的SessionFactory位空";
						System.out.println(info);
						logger.info(info);
						return;
					}
					} catch (SchedulerException e) {
						String info = "学分运算:通过Scheduler获取的SessionFactory异常："+e.getMessage();
						System.out.println(info);
						logger.info(info);
						e.printStackTrace();
					}
				}
		}
	  	
	  
	 int insertSum = 0;
	 int updateSum = 0;
	 int inSignSum = 0;
	 int upSignSum = 0;
	 int infailGuoSum = 0;
	 int infailExamSum =0;
	 int delfailSum =0;
	 Session session =  null;
	 
		//出勤
	   String att_b_date = "";
	   String att_e_date = "";
	   
	   //作业实操
	   String p_work_b_date = "";
	   String p_work_e_date = "";
	   //作业理论
	   String t_work_b_date = "";
	   String t_work_e_date = "";
	   //考试
	   String exam_b_date = "";
	   String exam_e_date = "";
	   
	   //报名日期
	   String reg_b_date = "";
	   String reg_e_date = "";
	   
	   //直接运行还后台运行
	   boolean para_is_not = false;
	 try {
		session =  sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
	  	System.out.println("["+dateToString(new Date())+"]:------begin 获取【学分标准表数据】-------------------");
	  	List<CreditStandard> creditStandard = null;
	  	//会计证
	  	Map<String,CreditStandard> base_abMap = new HashMap<String,CreditStandard>();
	  	Map<String,CreditStandard> base_afepeMap = new HashMap<String,CreditStandard>();
	  	Map<String,CreditStandard> base_aacMap = new HashMap<String,CreditStandard>();
	  	Map<String,CreditStandard> all_accountMap = new HashMap<String,CreditStandard>();//会计证所有科目
	  	//初级职称
	  	Map<String,CreditStandard> base_japMap = new HashMap<String,CreditStandard>();
	  	Map<String,CreditStandard> base_jbelMap = new HashMap<String,CreditStandard>();
	  	Map<String,CreditStandard> all_juniorMap = new HashMap<String,CreditStandard>();//初级职称所有科目
	  	
	  	//中级职称
	  	Map<String,CreditStandard> base_melMap = new HashMap<String,CreditStandard>();
	  	Map<String,CreditStandard> base_mapMap = new HashMap<String,CreditStandard>();
	  	Map<String,CreditStandard> base_mfmMap = new HashMap<String,CreditStandard>();
	  	
	  	Map<String,CreditStandard> mapSubject = new HashMap<String,CreditStandard>();
	  	if(ids != null && ids.size() >0){
	  		creditStandard = session.createQuery("from CreditStandard where  id in (:alist) ").setParameterList("alist",ids).list();;
	  	}else{
	  		creditStandard = session.createQuery("from CreditStandard").list();
	  	}
	  	for (int i = 0; i < creditStandard.size(); i++) {
			CreditStandard vo =  creditStandard.get(i);
			String subType = vo.getSubject_type();
			mapSubject.put(vo.getNc_id(), vo);
			//会计证 -不用计算考试分
			if(SubjectIdUtil.ACCOUNTING_BASIC.equals(subType)){
				base_abMap.put(vo.getNc_id(), vo);
				all_accountMap.put(vo.getNc_id(), vo);
			}
			if(SubjectIdUtil.ACCOUNTING_FE_PE.equals(subType)){
				base_afepeMap.put(vo.getNc_id(), vo);
				all_accountMap.put(vo.getNc_id(), vo);
			}
			if(SubjectIdUtil.ACCOUNTING_ACCOUNTING_COMPUTERIZATION.equals(subType)){
				base_aacMap.put(vo.getNc_id(), vo);
				all_accountMap.put(vo.getNc_id(), vo);
			}
			
			//初级职称
			if(SubjectIdUtil.JUNIOR_ACCOUNTING_PRACTICE.equals(subType)){
				base_japMap.put(vo.getNc_id(), vo);
				all_juniorMap.put(vo.getNc_id(), vo);
			}
			if(SubjectIdUtil.JUNIOR_BASIC_ECONOMIC_LAW.equals(subType)){
				base_jbelMap.put(vo.getNc_id(), vo);
				all_juniorMap.put(vo.getNc_id(), vo);
			}
			
			//中级职称
			if(SubjectIdUtil.MEDIUM_ACCOUNTING_PRACTICE.equals(subType)){
				base_mapMap.put(vo.getNc_id(), vo);
			}
			if(SubjectIdUtil.MEDIUM_ECONOMIC_LAW.equals(subType)){
				base_melMap.put(vo.getNc_id(), vo);
			}
			if(SubjectIdUtil.MEDIUM_FINANCIAL_MANAGEMENT.equals(subType)){
				base_mfmMap.put(vo.getNc_id(), vo);
			}
			
		}
		System.out.println("["+dateToString(new Date())+"]:------end 获取【学分标准表数据】---------------------");
		
		   
	
	   
		boolean retake = true;
		boolean junior = true;
		boolean medium = true;
		boolean accounting = true;
		boolean teacher = true;
	   
	   if(para != null){
		   //直接运行
		   att_b_date = para.getAttendanceBeginDate();
		   att_e_date = para.getAttendanceEndDate();
		   
		   p_work_b_date = para.getPworkBeginDate();
		   p_work_e_date = para.getPworkEndDate();
		   
		   t_work_b_date = para.getTworkBeginDate();
		   t_work_e_date = para.getTworkEndDate();
		   
		   exam_b_date = para.getExamBeginDate();
		   exam_e_date = para.getExamEndDate();
		   
		   
		   reg_b_date = para.getRegBeginDate();
		   reg_e_date = para.getRegEndDate();
		   
		    retake = para.getRetake();
			junior = para.getJunior();
			medium = para.getMedium();
			accounting = para.getAccounting();
			teacher = para.getTeacher();
	   }else{
		 //后台运行
		   List<CreditOperationTask> taskvos = session.createQuery("from CreditOperationTask").list();
		   CreditOperationTask vo = taskvos.get(0);
		   Date execute_date = new Date();
		   String last_y_m = getBeforeDate(execute_date,-1);
		   String last_last_y_m = getBeforeDate(execute_date,-2);
			
		   String a_b_date = vo.getAttendanceBeginDate().toString();
		   if(a_b_date.length() == 1){
			   a_b_date = "0"+a_b_date;
		   }
		   String a_e_date = vo.getAttendanceEndDate().toString();
		   if(a_e_date.length() == 1){
			   a_e_date = "0"+a_e_date;
		   }
		   String w_b_date = vo.getWorkBeginDate().toString();
		   if(w_b_date.length() == 1){
			   w_b_date = "0"+w_b_date;
		   }
		   String w_e_date = vo.getWorkEndDate().toString();
		   if(w_e_date.length() == 1){
			   w_e_date = "0"+w_e_date;
		   }
		   
		   String e_b_date = vo.getExamBeginDate().toString();
		   if(e_b_date.length() == 1){
			   e_b_date = "0"+e_b_date;
		   }
		   String e_e_date = vo.getExamEndDate().toString();
		   if(e_e_date.length() == 1){
			   e_e_date = "0"+e_e_date;
		   }
		   
		   att_b_date = last_y_m+"-"+a_b_date;
		   att_e_date = last_y_m+"-"+a_e_date;
		   
		   //作业日期
		   p_work_b_date = last_last_y_m+"-"+w_b_date;
		   p_work_e_date = last_y_m+"-"+w_e_date;
		   t_work_b_date = last_last_y_m+"-"+w_b_date;
		   t_work_e_date = last_y_m+"-"+w_e_date;
		   
		   exam_b_date = last_last_y_m+"-"+e_b_date;
		   exam_e_date = last_y_m+"-"+e_e_date;
		   //报名日期
		   String lastQueryDate = vo.getLastQueryDate();
		   reg_b_date=lastQueryDate;
		   reg_e_date=dateToStr(execute_date);
		   para_is_not = true;
	   }
		
		
		
		
		Map<String,Map<String,CreditRecord>> insertMapRecord = new HashMap<String,Map<String,CreditRecord>>();//插入key=学员->科目
	
		System.out.println("["+dateToString(new Date())+"]:------begin 获取【根据上一次最后查询的日期或日期段查询报名表信息】--------");
		System.out.println(reg_b_date+" 到 "+reg_e_date+"【报名表数据】");
		Map<String,Object> allIdMap = new HashMap<String,Object>();//存放【日期段-考勤-作业-考试学员的id】
		//key= 学员id->班级id->科目id
		Map<String, Map<String, Map<String, RegistrationInfoVO>>> newrevoMap  =  getRegistrationInfoByDateByHTTP(reg_b_date,reg_e_date,allIdMap,NcSyncConstant.getNcSecretkey());
		System.out.println("["+dateToString(new Date())+"]:------end 获取【根据上一次最后查询的日期或日期段查询报名表信息】--------");
		
		
	
		System.out.println("["+dateToString(new Date())+"]:------begin 获取【之前失败学员的数据】------");
		//国考查询学员 = 日期段的学员+不及格的学员
		Map<String,Object> accountStuIDs = new HashMap<String,Object>();//会计证
		Map<String,Object> juniorStuIDs = new HashMap<String,Object>();//初级职称
		Map<String,Object> mediumStuIDs = new HashMap<String,Object>();//中级职称
		for (Entry<String, Map<String, Map<String, RegistrationInfoVO>>> entry : newrevoMap.entrySet()) {
			//日期段的学员-避免重复学员主键
			String stuid = entry.getKey();
			accountStuIDs.put(stuid,1);
			juniorStuIDs.put(stuid,1);
			mediumStuIDs.put(stuid,1);
		}
		List<CreditFailStudent> failvos = session.createQuery("from CreditFailStudent").list();
		//key=学员id->科目id
		Map<String,Map<String,CreditFailStudent>> oldfailmap = new HashMap<String,Map<String,CreditFailStudent>>();
		for (int i = 0; i < failvos.size(); i++) {
			String stuid = failvos.get(i).getNcUserId();
			String subid = failvos.get(i).getNcSubjectId();
			if(SubjectIdUtil.ACCOUNTING.equals(subid)){
				//accountStuIDs.put(stuid,1);停用
			}else if(SubjectIdUtil.JUNIOR.equals(subid)){
				juniorStuIDs.put(stuid,1);
			}else if(failvos.get(i).getExamType() == 0){
				//不是会计证，初级，并且不是题库的阶段考试的就是中级职称学员
				mediumStuIDs.put(stuid,1);
			}
			if(oldfailmap.get(stuid) == null){
				Map<String,CreditFailStudent> map = new HashMap<String,CreditFailStudent>();
				map.put(subid, failvos.get(i));
				oldfailmap.put(stuid, map);
			}else{
				Map<String,CreditFailStudent> map = oldfailmap.get(stuid);
				map.put(subid, failvos.get(i));
			}
			
		}
	   System.out.println("["+dateToString(new Date())+"]:------end 获取【之前失败学员的数据】------");
	   
		*//***
		 * 考勤当前月是否已经运算过，如果运算过就不在运算
		 * 作业同理
		 * 实操同理
		 * 会把不及格的学员丢到一张表去
		 *//*
		
		*//************考勤 begin*************//*
		Map<String,Object> att_work_exam_IdMap = new HashMap<String,Object>();//存放【日期段-考勤-作业-考试学员的id】
		
		System.out.println("["+dateToString(new Date())+"]:------begin 获取【考勤数据】----------------------");
		System.out.println(att_b_date+" 到 "+att_e_date+"【考勤数据】");
		if(CheckParameter.checkTimeFormat(att_b_date) || CheckParameter.checkTimeFormat(att_e_date)){
			computeAttendance(mapSubject, insertMapRecord,allIdMap,att_b_date,att_e_date,att_work_exam_IdMap);
		}
		System.out.println("["+dateToString(new Date())+"]:------end   获取【考勤数据】----------------------");
		
		
		System.out.println("["+dateToString(new Date())+"]:------begin 获取【作业实操】----------------------");
		System.out.println(p_work_b_date+" 到 "+p_work_e_date+"【作业实操数据】");
		if(CheckParameter.checkTimeFormat(p_work_b_date) || CheckParameter.checkTimeFormat(p_work_e_date)){
			computeBusyworkPractical(mapSubject, insertMapRecord,allIdMap,p_work_b_date,p_work_e_date,att_work_exam_IdMap);
		}
		System.out.println("["+dateToString(new Date())+"]:------end   获取【作业实操】----------------------");
		
		
		System.out.println("["+dateToString(new Date())+"]:------begin 获取【作业理论】----------------------");
		System.out.println(t_work_b_date+" 到 "+t_work_e_date+"【作业理论数据】");
		if(CheckParameter.checkTimeFormat(t_work_b_date) || CheckParameter.checkTimeFormat(t_work_e_date)){
			computeBusyworkTheory(mapSubject, insertMapRecord,allIdMap,t_work_b_date,t_work_e_date,att_work_exam_IdMap);  
		}
		System.out.println("["+dateToString(new Date())+"]:------end   获取【作业理论】----------------------");
		
			
		
		System.out.println("["+dateToString(new Date())+"]:------begin 获取【考试实操】----------------------");
		System.out.println(exam_b_date+"到+"+exam_e_date+"【考试实操】");
		Map<String,WorkTheorySoreVO> failExam = new HashMap<String,WorkTheorySoreVO>();//存放考试失败的学员
		if(CheckParameter.checkTimeFormat(exam_b_date) || CheckParameter.checkTimeFormat(exam_e_date)){
			computeExamPractical(mapSubject, insertMapRecord,allIdMap,exam_b_date,exam_e_date,failExam,oldfailmap,att_work_exam_IdMap);
		}
		System.out.println("["+dateToString(new Date())+"]:------end 获取【考试实操】------------------------");

		
		System.out.println("["+dateToString(new Date())+"]:------begin 获取【上面学员的报名表信息】--------------");
		StudentIdVO stuIdvo = new StudentIdVO();
		stuIdvo.setSecretKey(NcSyncConstant.getNcSecretkey());
		//1.日期段报名表的学员(2.出勤-作业-考试的学员)
		stuIdvo.setStuIdList(Arrays.asList(att_work_exam_IdMap.keySet().toArray(new String[0])));
		//获取所有学员的报名表信息--key=学员id->班级->科目 
		Map<String, Map<String, Map<String, RegistrationInfoVO>>>  reMap = getRegistrationInfoByHTTP(stuIdvo);
		System.out.println("["+dateToString(new Date())+"]:------end  获取【上面学员的报名表信息】---------------");	
			
		Map<String,String> failGuo = new HashMap<String,String>();//存放国考失败的学员
		Map<Integer,CreditFailStudent> delmap = new HashMap<Integer,CreditFailStudent>();//成功学员，要在失败表删除的数据
		if(accountStuIDs.size() >0){
			//************************************begin 国考************************************************ 
			//国考查询学员 = 日期段的学员+不及格的学员
			stuIdvo.setStuIdList(Arrays.asList(accountStuIDs.keySet().toArray(new String[0])));
			System.out.println("["+dateToString(new Date())+"]:------begin 获取【会计证成绩】--------------------");
			if(accounting){
				//停用
				//computeAccountingCertificateScore(mapSubject, insertMapRecord, stuIdvo,failGuo,delmap,oldfailmap,reMap,base_abMap,base_afepeMap,base_aacMap,newrevoMap);
			}
			System.out.println("["+dateToString(new Date())+"]:------end    获取【会计证成绩】-------------------");
			
			
			System.out.println("["+dateToString(new Date())+"]:------begin  获取【初级职称成绩】------------------");
			if(junior){
				stuIdvo.setStuIdList(Arrays.asList(juniorStuIDs.keySet().toArray(new String[0])));
				computeJuniorLevelAccountantScore(mapSubject, insertMapRecord, stuIdvo,failGuo,delmap,oldfailmap,base_japMap ,base_jbelMap, all_juniorMap,reMap,newrevoMap);
			}
			System.out.println("["+dateToString(new Date())+"]:------end    获取【初级职称成绩】------------------");
			
			System.out.println("["+dateToString(new Date())+"]:------begin  获取【中级职称成绩分数】----------------");
			if(medium){
				stuIdvo.setStuIdList(Arrays.asList(mediumStuIDs.keySet().toArray(new String[0])));
				computeMediumLevelAccountantScore(mapSubject, insertMapRecord, stuIdvo,failGuo,delmap,oldfailmap,base_melMap,base_mapMap,base_mfmMap,reMap,newrevoMap);
			}
			System.out.println("["+dateToString(new Date())+"]:------end  获取【中级职称成绩分数】------------------");
			//************************************end 国考************************************************ 
		}
		
		System.out.println("["+dateToString(new Date())+"]:------begin  获取【考试实操重复考】------------------");
		if(retake){
			computeExamPracticalToOne(mapSubject, insertMapRecord,failvos,delmap,oldfailmap);
		}
		System.out.println("["+dateToString(new Date())+"]:------end  获取【考试实操重复考】--------------------");
		
		Map<String, Map<String,CreditRecord>> oldMap = new HashMap<String, Map<String,CreditRecord>>();
		Map<String,CreditRecordSign> signMap = new HashMap<String,CreditRecordSign>();
		if(allIdMap.size() > 0){
			System.out.println("["+dateToString(new Date())+"]:------begin  获取【根据学员id获取学分表记录】-----------");
			//key=（学员主键+科目主键）+班型
			getOldCreditRecord(session,oldMap,allIdMap);
			System.out.println("["+dateToString(new Date())+"]:------end  获取【根据学员id获取学分表记录】-------------");
			
			
			System.out.println("["+dateToString(new Date())+"]:------begin  获取【根据学员id获取本地报名表信息】--------");
			List<String> tempList = new ArrayList<String>();
			tempList.addAll(allIdMap.keySet());
			List<CreditRecordSign> signList = session.createQuery("from CreditRecordSign  where  studentId in (:alist) ").setParameterList("alist",tempList).list();
			for (int i = 0; i < signList.size(); i++) {
				CreditRecordSign vo = signList.get(i);
				signMap.put(vo.getSignId(), vo);
			}
			System.out.println("["+dateToString(new Date())+"]:------end  获取【根据学员id获取学本地报名表信息】---------");
		}
		Map<String,RegistrationInfoVO> inUpSignMap = new HashMap<String,RegistrationInfoVO>();
		
		Map<String,Map<String,CreditRecord>> insertMap =  new HashMap<String,Map<String,CreditRecord>>();//key=学员+(科目+班级)
		Map<String,Map<String,CreditRecord>> updateMap =  new HashMap<String,Map<String,CreditRecord>>();//key=学员+(科目+班级)
		Map<String,Map<String,CreditRecord>> juniorspecialMap =  new HashMap<String,Map<String,CreditRecord>>();//key=学员+(科目+班级)
		Map<String,Map<String,CreditRecord>> accountpecialMap =  new HashMap<String,Map<String,CreditRecord>>();//key=学员+(科目+班级)
		
		//获取需要替换科目的数据
		Map<String,Map<String,String>> replaceMap = getReplaceSubjects(reMap,newrevoMap);
		
		
		//旧学员单据处理
		getCreditRecord(mapSubject, newrevoMap, insertMapRecord, reMap, oldMap,updateMap,inUpSignMap,insertMap,all_accountMap,all_juniorMap,juniorspecialMap,accountpecialMap);
		//新学员单据处理
		getNewRecord(mapSubject, newrevoMap, oldMap,updateMap,inUpSignMap,insertMap,all_accountMap,all_juniorMap,juniorspecialMap,accountpecialMap);
		
	   //添加授课老师信息,班级名称，替换科目
		Map<String,CreditRecord> removeVOMap  = null;
		if(teacher){
			 removeVOMap  =	addTeacherToRecord(session,insertMap,updateMap,replaceMap,mapSubject);
		} 
		
		//获取需要更新报名表的数据
		List<Map<String, CreditRecordSign>> signList = getUpdateRegistrationInfoVO(inUpSignMap,signMap);
		
		
		
		//替换科目涉及的更新学分
		replaceSubjectAndUpScore(all_juniorMap, updateMap, removeVOMap);
		
		
		
		//计算学员特殊课程是否合格
		for (Entry<String, Map<String, CreditRecord>> entry: juniorspecialMap.entrySet()) {
			computeJuniorSpecical(all_juniorMap, oldMap, updateMap, entry);
		}
		//计算学员特殊课程是否合格
		for (Entry<String, Map<String, CreditRecord>> entry: accountpecialMap.entrySet()) {
			computeAccountSpecical(all_accountMap, oldMap, updateMap, entry);
			
		}
		
		//更新学分记录表
		for (Entry<String, Map<String, CreditRecord>> entry: updateMap.entrySet()) {
			Map<String, CreditRecord>  voMap = entry.getValue();
			for (Entry<String, CreditRecord> cEntry : voMap.entrySet()) {
				CreditRecord vo = cEntry.getValue();
				session.update(vo);
				updateSum++;
			}
		}
		
		//插入学分记录表  key=学员->(科目+班级)
		for (Entry<String, Map<String, CreditRecord>> entry: insertMap.entrySet()) {
			//一个学员多个科目
			Map<String, CreditRecord>  voMap = entry.getValue();
			for (Entry<String, CreditRecord> cEntry : voMap.entrySet()) {
				CreditRecord vo = cEntry.getValue();
				//如果旧的科目已存在就移除新的科目，如果新的存在就移除旧的科目（通过sql语句过滤，如果NC数据不正确会出现新旧科目的情况）
				String key = vo.getNcUserId()+"@"+vo.getClassId()+"@"+vo.getNcSubjectId();
				if(removeVOMap != null){
					if(removeVOMap.get(key) != null){
						continue;
					}
				}
				session.save(vo);
				insertSum++;
			}
		}
		
	
		//新增报名表信息 2017年6月15日18:28:10
		inSignSum  = signList.get(0).size();
		for (Map.Entry<String,CreditRecordSign> entry:  signList.get(0).entrySet()) {
			CreditRecordSign  vo = entry.getValue();
			session.save(vo);
		}
		
		//更新报名表信息 2017年6月15日18:28:02
		upSignSum  = signList.get(1).size();
		for (Map.Entry<String,CreditRecordSign> entry:  signList.get(1).entrySet()) {
			CreditRecordSign  vo = entry.getValue();
			session.update(vo);
		}
		
		
		*//***********************失败学员处理 begin*******************************//*
		infailGuoSum = failGuo.size();
		List<CreditFailStudent> insertvo = new ArrayList<CreditFailStudent>();
		for (Map.Entry<String, String> entry : failGuo.entrySet()) {
			//国考的
			CreditFailStudent  vo =  new CreditFailStudent();
			vo.setNcSubjectId(entry.getValue());
			vo.setNcUserId(entry.getKey());
			insertvo.add(vo);
		}
		
		infailExamSum  = failExam.size();
		for (Entry<String, WorkTheorySoreVO> entry:failExam.entrySet()) {
				//考试
					kckm05	电脑账 目前这些要重考
					kckm184	快速财务分析与诊断实战
					kckm04	手工账
					kckm35	工业会计实战
				
				String[] key = entry.getKey().split("@");
				WorkTheorySoreVO wvo = entry.getValue();
				CreditFailStudent  vo =  new CreditFailStudent();
				vo.setNcUserId(key[0]);
				vo.setNcSubjectId(key[1]);
				vo.setExamType(1);
				vo.setNcSubjectCode(wvo.getCourseCode());
				insertvo.add(vo);
		}
		//失败学员插入--
		for (int i = 0; i < insertvo.size(); i++) {
			session.save(insertvo.get(i));
		}
		
		
		//失败学员多次考试通过的删除
		delfailSum = delmap.size();
		for (Entry<Integer, CreditFailStudent> entry:delmap.entrySet()) {
			CreditFailStudent  vo = entry.getValue();
			session.delete(vo);
		}
		*//***********************失败学员处理 end*********************//*
	   if(para == null){
		 //更新最后查询日期,后台运行才更新日期
			Query query = session.createQuery("update CreditOperationTask t set t.lastQueryDate = '"+dateToStr(new Date())+"' where id = 1");  
		    query.executeUpdate();
	   }
		tx.commit();
	} catch(Exception e){
		e.printStackTrace();
		errorMess.append("\r\n"+e.getMessage());
	}finally{
		if(session != null){
			session.close();
		}
		
		Session  session_ =  sessionFactory.openSession(); 
		//更新运行日志
	    CreditOperationLog log = new CreditOperationLog();
	    log.setStartTime(start);
	    log.setEndTime(new Date());
	    StringBuilder success = new StringBuilder();
	    String type = "直接执行";
	   if(para_is_not){
		   type ="后台执行";
	   }
	    success.append("【执行方式："+type+"】\r\n");
	    success.append("【报名表查询日期段："+reg_b_date+"到"+reg_e_date+"】\r\n");
	    success.append("【出勤数据查询日期段："+att_b_date+"到"+att_e_date +"】\r\n");
	    success.append("【作业实操查询日期段："+ p_work_b_date+"到"+ p_work_e_date +"】\r\n");
	    success.append("【作业理论查询日期段："+ t_work_b_date+"到"+t_work_e_date+"】\r\n");
	    success.append("【考试实操查询日期段："+exam_b_date+"到"+ exam_e_date +"】\r\n");
	    success.append("【学分记录表插入数量："+insertSum +"】\r\n");
	    success.append("【学分记录表更新数量："+updateSum +"】\r\n");
	    success.append("【报名表插入的数量："+inSignSum +"】\r\n");
	    success.append("【报名表更新的数量："+upSignSum +"】\r\n");
	    success.append("【国考不及格的学员数量："+infailGuoSum +"】\r\n");
	    success.append("【题库考试不及格的学员数量："+infailExamSum +"】\r\n");
	    success.append("【题库考试重考及格后删除的学员数据："+delfailSum+"】\r\n" );
	    log.setLogstr(errorMess.length()==0?success.toString():errorMess.toString());
	    log.setTaskname("学分运算");
	    session_.save(log);
	    session_.flush(); 
	    session_.clear();
	    if(session_ !=null){
	    	session_.close();
	    }
		System.out.println("-----------------------运算结束---------------------------");
	}
	
  }

 *//**
  * 替换科目涉及的更新学分
  * @param all_juniorMap 全部科目信息【初级职称】
  * @param updateMap 更新的学分信息【key=学员->(科目+班级)】
  * @param removeVOMap 需移除的科目【key = 学员+班级+科目】
  *//*
private void replaceSubjectAndUpScore(
		Map<String, CreditStandard> all_juniorMap,
		Map<String, Map<String, CreditRecord>> updateMap,
		Map<String, CreditRecord> removeVOMap) {
	for (Entry<String, Map<String, CreditRecord>> entry: updateMap.entrySet()) {
		Map<String, CreditRecord>  voMap = entry.getValue();
		for (Entry<String, CreditRecord> cEntry : voMap.entrySet()) {
			CreditRecord vo = cEntry.getValue();
			if(removeVOMap != null && removeVOMap.size() > 0){
				//更新学分
				String key = vo.getNcUserId()+"@"+vo.getClassId()+"@"+vo.getNcSubjectId();
				CreditRecord inVO = removeVOMap.get(key);
				if(inVO != null){
					if(inVO.getAttendanceActualScore() > vo.getAttendanceActualScore()){
						vo.setAttendanceActualScore(inVO.getAttendanceActualScore());
					}
					
					if(inVO.getWorkActualScore() > vo.getWorkActualScore()){
						vo.setWorkActualScore(inVO.getWorkActualScore());
					}
					
					if(inVO.getExamActualScore() > vo.getExamActualScore()){
						vo.setExamActualScore(inVO.getExamActualScore());
					}
					Integer actualtotal = vo.getAttendanceActualScore()+vo.getExamActualScore()+vo.getWorkActualScore();
					Integer claimtotal = vo.getAttendanceClaimScore()+vo.getWorkClaimScore()+vo.getExamClaimScore();
					vo.setTotalScore(vo.getAttendanceActualScore()+vo.getWorkActualScore()+vo.getExamActualScore());
					//只要不是特殊科目都计算是否合格，特殊科目有特殊方法处理
					if(all_juniorMap.get(vo.getNcSubjectId()) == null ){//&& all_accountMap.get(vo.getNcSubjectId()) ==null
						if(claimtotal != 0 && actualtotal.intValue() >= (int)(claimtotal.intValue()*0.8)){
							vo.setIsPass(0);
						}
					}
				}
			}
		}
	}
}

*//**
 * 替换科目信息
 * @param mapSubject 学分标准分信息
 * @param replaceMap 需要替换的科目信息 key1 = 学员主键 key2 =班型主键+@+科目主键
 * @param vo 待处理的学分记录
 * @param update 是否更新
 *//*
private String replaceSubject(Map<String, CreditStandard> mapSubject,
		Map<String, Map<String,String>> replaceMap,
		CreditRecord vo) {
	String key =vo.getClassId()+"@"+vo.getNcSubjectId();
	Map<String, String> map = replaceMap.get(vo.getNcUserId());
	if(map != null){
		String subId = map.get(key);
		if(subId != null){
			CreditStandard standeard = mapSubject.get(subId);
			if( standeard != null){
				vo.setSubjectCode(standeard.getCourse_code());
				vo.setSubjectName(standeard.getSubject_name());
				vo.setNcSubjectId(standeard.getNc_id());
			}
			return subId;
		}
	}
	return null;
}

*//**
 * 获取需要替换科目的数据
* @param reMap 根据学员主键查询的报名表信息【 key=学员id->班级id->科目id】 
 * @param newrevoMap 根据日期段查询的报名表信息【key = 学员id->班级id->科目id】
 * @return key = 学员id->班型id+原科目（要被替换科目）id
 *//*
  private Map<String,Map<String,String>> getReplaceSubjects(
		Map<String, Map<String, Map<String, RegistrationInfoVO>>> reMap,
		Map<String, Map<String, Map<String, RegistrationInfoVO>>> newrevoMap) {
	  //学员->班型+原科目 = 替换科目
	  Map<String,Map<String,String>> map = new HashMap<String,Map<String,String>>();
	  addData(reMap, map);
	  addData(newrevoMap, map);
	return map;
}

*//**
 * 
 * @param reMap 报名表信息 【 key=学员id->班级id->科目id】 
 * @param map 需要替换科目的信息 【key = 学员id->班级id+科目id】
 *//*
private void addData(
		Map<String, Map<String, Map<String, RegistrationInfoVO>>> reMap,
		Map<String, Map<String, String>> map) {
	for (Entry<String, Map<String, Map<String, RegistrationInfoVO>>> entry : reMap.entrySet()) {
		  Map<String, Map<String, RegistrationInfoVO>> classMap =  entry.getValue();
		  for (Entry<String, Map<String, RegistrationInfoVO>> entry2 : classMap.entrySet()) {
			  Map<String, RegistrationInfoVO> subMap = entry2.getValue();
			  for (Entry<String, RegistrationInfoVO> entry3 : subMap.entrySet()) {
				 RegistrationInfoVO vo =  entry3.getValue();
				 String replaceSub = vo.getDef10();//要替换科目主键
				 if(replaceSub != null){
					 String key = vo.getClassId()+"@"+replaceSub;
					 if( map.get(vo.getStudentId()) == null){
						 HashMap<String,String> temp = new HashMap<String,String>();
						 temp.put(key, vo.getSubjectilesid());
						 map.put(vo.getStudentId(), temp);
					 }else{
						 map.get(vo.getStudentId()).put(key, vo.getSubjectilesid());
					 }
				 }
			}
		}
	}
}

*//**
   * 获取需要更新的报名信息
   * @param inUpSignMap 带插入和更新的所有报名信息
   * @param signMap 本地报名信息
   * @return 插入vomap和更新vomap
   *//*
  private List<Map<String, CreditRecordSign>> getUpdateRegistrationInfoVO(
		Map<String, RegistrationInfoVO> inUpSignMap,
		Map<String, CreditRecordSign> signMap) {
	  List<Map<String, CreditRecordSign>> listMap = new ArrayList<Map<String, CreditRecordSign>>();
	  Map<String,CreditRecordSign> upSignMap = new HashMap<String,CreditRecordSign>();
	  Map<String,CreditRecordSign> inSignMap = new HashMap<String,CreditRecordSign>();
	  for (Entry<String, RegistrationInfoVO> entry: inUpSignMap.entrySet()) {
		  RegistrationInfoVO rvo = entry.getValue();
		  CreditRecordSign  svo = signMap.get(rvo.getSignId());
		  if(svo == null){
			//插入
			CreditRecordSign signvo = getNewCreditRecordSign(rvo);
			inSignMap.put(signvo.getSignId(), signvo);
		}else{
			if(!svo.getOrgId().equals(rvo.getOrgId())){
				svo.setOrgId(rvo.getOrgId());
				upSignMap.put(svo.getSignId(), svo);
			}
		}
		
	  }
	  listMap.add(inSignMap);
	  listMap.add(upSignMap);
	return listMap;
}


private void computeAccountSpecical(Map<String, CreditStandard> all_accountMap,
		Map<String, Map<String, CreditRecord>> oldMap,
		Map<String, Map<String, CreditRecord>> updateMap,
		Entry<String, Map<String, CreditRecord>> entry) {
	String stuid = entry.getKey();
	Map<String, CreditRecord> subMap =entry.getValue();
	if(subMap.size()<3){
		int actualSum = 0;
		int claimSum = 0;
		boolean b = true;
		List<CreditRecord> oldvos = new ArrayList<CreditRecord>();
		List<CreditRecord> upvos = new ArrayList<CreditRecord>();
		Map<String,Object> typemap = new HashMap<String,Object>();
		Map<String,Object> submap = new HashMap<String,Object>();
		typemap.put(SubjectIdUtil.ACCOUNTING_BASIC, 1);
		typemap.put(SubjectIdUtil.ACCOUNTING_FE_PE, 1);
		typemap.put(SubjectIdUtil.ACCOUNTING_ACCOUNTING_COMPUTERIZATION, 1);
		for (Entry<String, CreditRecord> _entry: subMap.entrySet()) {
			String subid_c = _entry.getKey();
			CreditRecord vo = _entry.getValue();
			oldvos.add(vo);
			if(b){
				//考试分只加一次
				b = false;
				actualSum += vo.getExamActualScore();
				claimSum += vo.getExamClaimScore();
			}
			actualSum += vo.getAttendanceActualScore()+vo.getWorkActualScore();
			claimSum += vo.getAttendanceClaimScore()+vo.getWorkClaimScore();
			
			String type = all_accountMap.get(subid_c).getSubject_type();
		
			//会计证
			if(SubjectIdUtil.ACCOUNTING_BASIC.equals(type)){
				typemap.remove(SubjectIdUtil.ACCOUNTING_BASIC);
			}
			if(SubjectIdUtil.ACCOUNTING_FE_PE.equals(type)){
				typemap.remove(SubjectIdUtil.ACCOUNTING_FE_PE);
			}
			if(SubjectIdUtil.ACCOUNTING_ACCOUNTING_COMPUTERIZATION.equals(type)){
				typemap.remove(SubjectIdUtil.ACCOUNTING_ACCOUNTING_COMPUTERIZATION);
			}
			submap.put(subid_c, 1);
		}
		
		
		for (Entry<String, CreditStandard> accountEntry: all_accountMap.entrySet()) {
			String subid_s =accountEntry.getKey();
			CreditStandard vo_s =accountEntry.getValue();
			if(submap.get(subid_s) !=null){
				continue;
			}
			if(typemap.get(vo_s.getSubject_type()) !=null){
				//学员+科目->班型
				String s_s_key = stuid+"@"+vo_s.getNc_id();
				Map<String, CreditRecord> classmap =oldMap.get(s_s_key);
				if(classmap != null){
					boolean c = true;
					for (Entry<String, CreditRecord> classEntry: classmap.entrySet()) {
						CreditRecord _vo = classEntry.getValue();
						if(c){
							//多班型只加一次
							c = false;
							actualSum += _vo.getAttendanceActualScore()+_vo.getWorkActualScore();
							claimSum += _vo.getAttendanceClaimScore()+_vo.getWorkClaimScore();
						}
						upvos.add(_vo);
					}
				}
			}
		}
		
		if(actualSum >= (int)(claimSum*0.8)){
			//合格
			for (int i = 0; i < oldvos.size(); i++) {
				oldvos.get(i).setIsPass(0);
			}
			for (int i = 0; i < upvos.size(); i++) {
				CreditRecord vo = upvos.get(i);
				vo.setIsPass(0);
				String stukey = vo.getNcUserId();
				String s_c_key  = vo.getNcSubjectId()+"@"+vo.getClassId();
				 //key=学员->(科目+班级)
				Map<String, CreditRecord> temp = new HashMap<String, CreditRecord>();
				temp.put(s_c_key, vo);
				updateMap.put(stukey, temp);
			}
		}
		
	}else{
		int actualSum = 0;
		int claimSum = 0;
		boolean b = true;
		for (Entry<String, CreditRecord> _entry: subMap.entrySet()) {
			CreditRecord vo = _entry.getValue();
			if(b){
				//考试分只加一次
				b = false;
				actualSum += vo.getExamActualScore();
				claimSum += vo.getExamClaimScore();
			}
			actualSum += vo.getAttendanceActualScore()+vo.getWorkActualScore();
			claimSum += vo.getAttendanceClaimScore()+vo.getWorkClaimScore();
		}
		
		if(actualSum >= (int)(claimSum*0.8)){
			//合格
			for (Entry<String, CreditRecord> _entry: subMap.entrySet()) {
				_entry.getValue().setIsPass(0);;
			}
		}
	}
}
*//**
 * 初级职称特殊科目的计算
 * @param all_juniorMap 全部科目信息【初级职称】
 * @param oldMap 本地学分表信息  key=（学员主键+科目主键）+班型
 * @param updateMap  待更新学分信息 key=学员->(科目+班级)
 * @param entry
 *//*
private void computeJuniorSpecical(Map<String, CreditStandard> all_juniorMap,
		Map<String, Map<String, CreditRecord>> oldMap,
		Map<String, Map<String, CreditRecord>> updateMap,
		Entry<String, Map<String, CreditRecord>> entry) {
	String stuid = entry.getKey();
	Map<String, CreditRecord> subMap =entry.getValue();
	if(subMap.size()==1){
		//运算的数据里要替换的科目只有一个科目，需要找到另一个科目一起计算
		int actualSum = 0;
		int claimSum = 0;
		boolean b = true;
		List<CreditRecord> oldvos = new ArrayList<CreditRecord>();
		List<CreditRecord> upvos = new ArrayList<CreditRecord>();
		for (Entry<String, CreditRecord> _entry: subMap.entrySet()) {
			String subid_c = _entry.getKey();
			CreditRecord vo = _entry.getValue();
			oldvos.add(vo);
			if(b){
				//考试分只加一次
				b = false;
				actualSum += vo.getExamActualScore();
				claimSum += vo.getExamClaimScore();
			}
			actualSum += vo.getAttendanceActualScore()+vo.getWorkActualScore();
			claimSum += vo.getAttendanceClaimScore()+vo.getWorkClaimScore();
			
			String type = all_juniorMap.get(subid_c).getSubject_type();
			//初级职称
			boolean jap = true;
			boolean jbel = true;
			if(SubjectIdUtil.JUNIOR_ACCOUNTING_PRACTICE.equals(type)){
				jap = false;
			}else if(SubjectIdUtil.JUNIOR_BASIC_ECONOMIC_LAW.equals(type)){
				jbel = false;
			}
			for (Entry<String, CreditStandard> juniorEntry: all_juniorMap.entrySet()) {
				String subid_s =juniorEntry.getKey();
				CreditStandard vo_s =juniorEntry.getValue();
				if(subid_s.equals(subid_c)){
					continue;
				}
				if(jap){
					//学员+科目->班型
					String s_s_key = stuid+"@"+vo_s.getNc_id();
					Map<String, CreditRecord> classmap =oldMap.get(s_s_key);
					if(classmap != null){
						boolean c = true;
						for (Entry<String, CreditRecord> classEntry: classmap.entrySet()) {
							CreditRecord _vo = classEntry.getValue();
							if(c){
								//多班型只加一次
								c = false;
								actualSum += _vo.getAttendanceActualScore()+_vo.getWorkActualScore();
								claimSum += _vo.getAttendanceClaimScore()+_vo.getWorkClaimScore();
							}
							upvos.add(_vo);
						}
						break;
					}
				}else if(jbel){
					//学员+科目->班型
					String s_s_key = stuid+"@"+vo_s.getNc_id();
					Map<String, CreditRecord> classmap =oldMap.get(s_s_key);
					if(classmap != null){
						boolean c = true;
						for (Entry<String, CreditRecord> classEntry: classmap.entrySet()) {
							CreditRecord _vo = classEntry.getValue();
							if(c){
								//多班型只加一次
								c = false;
								actualSum += _vo.getAttendanceActualScore()+_vo.getWorkActualScore();
								claimSum += _vo.getAttendanceClaimScore()+_vo.getWorkClaimScore();
							}
							upvos.add(_vo);
						}
						break;
					}
				}
			}
		}
		
		if(actualSum >= (int)(claimSum*0.8)){
			//合格
			for (int i = 0; i < oldvos.size(); i++) {
				oldvos.get(i).setIsPass(0);
			}
			for (int i = 0; i < upvos.size(); i++) {
				CreditRecord vo = upvos.get(i);
				vo.setIsPass(0);
				String stukey = vo.getNcUserId();
				String s_c_key  = vo.getNcSubjectId()+"@"+vo.getClassId();
				 //key=学员->(科目+班级)
				Map<String, CreditRecord> temp = new HashMap<String, CreditRecord>();
				temp.put(s_c_key, vo);
				updateMap.put(stukey, temp);
			}
		}
		
	}else{
		int actualSum = 0;
		int claimSum = 0;
		boolean b = true;
		for (Entry<String, CreditRecord> _entry: subMap.entrySet()) {
			CreditRecord vo = _entry.getValue();
			if(b){
				//考试分只加一次
				b = false;
				actualSum += vo.getExamActualScore();
				claimSum += vo.getExamClaimScore();
			}
			actualSum += vo.getAttendanceActualScore()+vo.getWorkActualScore();
			claimSum += vo.getAttendanceClaimScore()+vo.getWorkClaimScore();
		}
		
		if(actualSum >= (int)(claimSum*0.8)){
			//合格
			for (Entry<String, CreditRecord> _entry: subMap.entrySet()) {
				_entry.getValue().setIsPass(0);;
			}
		}
	}
}

  *//**
   *   //添加授课老师信息到学分表和替换科目
   * @param insertMap key=学员->(科目+班级)
 * @param updateMap key=学员->(科目+班级)
 * @param replaceMap  key = 学员id->班型id+原科目（要被替换科目）id
 * @param mapSubject 标准学分科目map 【key = 科目主键】
 *  @return key = 学员+班级+科目
 * @throws Exception 
   *//*

 @SuppressWarnings("unchecked")
private   Map<String,CreditRecord>  addTeacherToRecord(Session session,
		Map<String,Map<String,CreditRecord>> insertMap,
		Map<String,Map<String,CreditRecord>> updateMap,
		Map<String, Map<String, String>> replaceMap,
		Map<String, CreditStandard> mapSubject) throws Exception {
	 Map<String,CreditRecord> removeVOMap = new HashMap<String,CreditRecord>();//key=学员+班型+科目
	try {
		 StudentIdVO svo = new StudentIdVO();
		Map<String,String> tempMap = new HashMap<String,String>();
		 for (Entry<String, Map<String, CreditRecord>> entry:insertMap.entrySet()) {
			 tempMap.put(entry.getKey(), entry.getKey());
		}
		 for (Entry<String, Map<String, CreditRecord>> entry:updateMap.entrySet()) {
			 tempMap.put(entry.getKey(), entry.getKey());
		}
		 for (Entry<String, Map<String, String>> entry:replaceMap.entrySet()) {
			 tempMap.put(entry.getKey(), entry.getKey());
		}
		 
		
		List<CreditRecord> upvos = new ArrayList<CreditRecord>();
		//学员替换科目的学员信息
		String[] replaceIds = replaceMap.keySet().toArray(new String[0]);
		List<CreditRecord> recordList = null;
		if(replaceIds != null && replaceIds.length > 0){
			 recordList = session.createQuery("from CreditRecord  where className is null or teacherId is null or className ='' or teacherId ='' or ncUserId in (:alist) ")
					.setParameterList("alist",replaceIds).list();	
		}else{
			 recordList = session.createQuery("from CreditRecord  where className is null or teacherId is null or className ='' or teacherId =''").list();
		}
		
		for (int i = 0; i < recordList.size(); i++) {
			CreditRecord vo = recordList.get(i);
			Map<String, CreditRecord> in = insertMap.get(vo.getNcUserId());
			Map<String, CreditRecord> up = updateMap.get(vo.getNcUserId());
			if(in == null && up == null){
				//不存在
				upvos.add(vo);
			}else{
				//存在，但是科目+班级不存在
				String su_c_key  = vo.getNcSubjectId()+"@"+vo.getClassId();
					if((in != null && in.get(su_c_key) == null) || (up != null && up.get(su_c_key) == null)){
						upvos.add(vo);
					}
			}
			tempMap.put(vo.getNcUserId(),vo.getNcUserId());
		} 
	
		 svo.setSecretKey(NcSyncConstant.getNcSecretkey());
		 svo.setStuIdList(Arrays.asList(tempMap.keySet().toArray(new String[0])));
		 Map<String, TeacherVO> tmap =  getTeacherFromArrangedPlanByHTTP(svo);
		 
		 
			//记录需要移除的新科目信息
			Map<String,String> removeKeyMap = new HashMap<String,String>();//key=学员+班型+科目
		 for (Entry<String, Map<String, CreditRecord>> entry:updateMap.entrySet()) {
			 for (Entry<String, CreditRecord> cEntry :entry.getValue().entrySet()) {
				 CreditRecord vo = cEntry.getValue();
				 String ssKey = vo.getNcUserId()+"@"+vo.getNcSubjectId();
				 //替换老师和班级
				 ssKey = replaceTeacherAndClassName(replaceMap, vo, ssKey);
				 if(tmap.get(ssKey) != null){
					 vo.setTeacherId(tmap.get(ssKey).getTeacherId());
					 vo.setClassName(tmap.get(ssKey).getClassName());
				 }
				 
				//替换科目
				String subId = replaceSubject(mapSubject, replaceMap,vo);
				if(subId != null){
					String key = vo.getNcUserId()+"@"+vo.getClassId()+"@"+subId;
					removeKeyMap.put(key, key);
				}
					
			}
			
		}
		 
		 //需要更新老师和班级的vo
			for (int i = 0; i < upvos.size(); i++) {
				CreditRecord vo = upvos.get(i);
				String su_c_key  = vo.getNcSubjectId()+"@"+vo.getClassId();
				String stkey =vo.getNcUserId();
				String ssKey = vo.getNcUserId()+"@"+vo.getNcSubjectId();
				 //替换老师和班级
				 ssKey = replaceTeacherAndClassName(replaceMap, vo, ssKey);
				 if(tmap.get(ssKey) != null){
					 vo.setTeacherId(tmap.get(ssKey).getTeacherId());
					 vo.setClassName(tmap.get(ssKey).getClassName());
				 }
				 
				//替换科目
				String subId = replaceSubject(mapSubject, replaceMap,vo);
				if(subId != null){
					String key = vo.getNcUserId()+"@"+vo.getClassId()+"@"+subId;
					removeKeyMap.put(key, key);
				}
				
				
				if(tmap.get(ssKey) != null || subId != null){
					 //有的科目要更新，但是没有排课的情况，
					 Map<String, CreditRecord> upvoMap = updateMap.get(stkey);
						if(upvoMap == null){
							Map<String, CreditRecord> temp = new HashMap<String,CreditRecord>();
							temp.put(su_c_key, vo);
							updateMap.put(vo.getNcUserId(), temp);
						}else{
							upvoMap.put(su_c_key, vo);
						}
				}
				 
			}
		 
		 
		 for (Entry<String, Map<String, CreditRecord>> entry:insertMap.entrySet()) {
			 for (Entry<String, CreditRecord> cEntry :entry.getValue().entrySet()) {
				 CreditRecord vo = cEntry.getValue();
				 String ssKey = vo.getNcUserId()+"@"+vo.getNcSubjectId();
				 //替换老师和班级
				 ssKey = replaceTeacherAndClassName(replaceMap, vo, ssKey);
				 if(tmap.get(ssKey) != null){
					 vo.setTeacherId(tmap.get(ssKey).getTeacherId());
					 vo.setClassName(tmap.get(ssKey).getClassName());
				 }
				 
				//记录要被移除的vo信息
				String key = vo.getNcUserId()+"@"+vo.getClassId()+"@"+vo.getNcSubjectId();
				if(removeKeyMap.get(key) != null){
					removeVOMap.put(key, vo);
				}
				
			}
			
		}
		
	} catch (Exception e) {
		e.printStackTrace();
		throw new Exception("获取授课老师信息异常："+e.getMessage()) ;
	}
	return removeVOMap;
	
	
}

private String replaceTeacherAndClassName(
		Map<String, Map<String, String>> replaceMap, CreditRecord vo,
		String ssKey) {
	//判断该学员是否要替换排课计划
	 Map<String,String> map = replaceMap.get(vo.getNcUserId());
	 if(map != null){
		 String key = vo.getClassId()+"@"+vo.getNcSubjectId();
		 //获取最终显示的科目主键
		 String subId = map.get(key);
		 if(subId != null){
			 ssKey = vo.getNcUserId()+"@"+subId;
		 }
	 }
	return ssKey;
}
*//**
 * 通过http请求获取对应学员排课计划的学员信息
 * @return 
 * @throws Exception 
 *//*
private Map<String, TeacherVO> getTeacherFromArrangedPlanByHTTP(StudentIdVO stuIdvo) throws Exception {
	String result2 = HttpRequest.sendPost(NcSyncConstant.getTeacherFromArrangedPlanUrl(), JsonUtil.toJson(stuIdvo),"json");
	HttpServiceResult s = JsonUtil.fromJson(result2, new TypeToken<HttpServiceResult>() {}.getType());
	Map<String, TeacherVO> tvoMap = new HashMap<String, TeacherVO>();
	if(s.getData()!=null){
		List<TeacherVO> voList = JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<TeacherVO>>() {}.getType());
		for (int i = 0; i < voList.size(); i++) {
	 		TeacherVO vo = voList.get(i);
	 		String ssKey = vo.getStudentId()+"@"+vo.getSubjectilesId();
	 		tvoMap.put(ssKey, vo);
		}
	}
	return tvoMap;

}

*//**
  * 根据学员主键获取历史的学分数据
  * @param sessionFactory
  * @param stuIdMap
  * @param oldMap key=（学员主键+科目主键）+班型
 * @param allIdMap 
  * @return
  *//*
@SuppressWarnings("unchecked")
private void getOldCreditRecord(Session session,
		 Map<String, Map<String,CreditRecord>> oldMap, Map<String, Object> allIdMap) {
	//查询学分记录表，为了更新
	List<String> tempList = new ArrayList<String>();
	tempList.addAll(allIdMap.keySet());
	List<CreditRecord> recordList = session.createQuery("from CreditRecord  where  ncUserId in (:alist) ").setParameterList("alist",tempList).list();
	if(recordList !=null && recordList.size()>0){
		for (int i = 0; i < recordList.size(); i++) {
			CreditRecord vo = recordList.get(i);
			String key = vo.getNcUserId()+"@"+vo.getNcSubjectId();
			String classkey = vo.getClassId();
			if(oldMap.get(key) == null){
				Map<String,CreditRecord> map =new HashMap<String,CreditRecord>();
				map.put(classkey, vo);
				oldMap.put(key, map);
			}else{
				//一个学员多个班型，班型之间有相同科目
				Map<String, CreditRecord> old = oldMap.get(key);
				if(old.get(classkey)!=null){
					CreditRecord oldvo = old.get(classkey);
					if(oldvo.getTotalScore() < vo.getTotalScore()){
						old.put(classkey, vo);
					}
				}else{
					old.put(classkey, vo);
				}
					
				
			}
		}
	}
}

*//**
 * 获取计算后的CreditRecord数据
 * @param mapSubject 标准学分科目map 【key = 科目主键】
 * @param newrevoMap 根据日期段查询的报名表信息【key = 学员id->班级id->科目id】
 * @param insertMapRecord 待插入的学分记录信息 【 key= 学员->科目】
 * @param reMap 获取所有学员的报名表信息--key=学员id->班级->科目 
 * @param oldMap 本地学分表信息【key=（学员主键+科目主键）+班型】
 * @param updateMap  更新的学分信息【key=学员->(科目+班级)】
 * @param inUpSignMap  待插入或更新的报名表信息【key= 报名表主键】
 * @param insertMap  插入的学分信息【key=学员->(科目+班级)】
 * @param all_accountMap  全部科目信息【会计证】 -不用计算考试分
 * @param all_juniorMap  全部科目信息【初级职称】
 * @param juniorspecialMap 初级职称需要计算是否通过【key=学员+(科目+班级)】
 * @param accountpecialMap 会计证需要计算是否通过【key=学员+(科目+班级)】
 *//*
private void getCreditRecord(Map<String, CreditStandard> mapSubject,
		Map<String, Map<String, Map<String, RegistrationInfoVO>>> newrevoMap,
		Map<String, Map<String, CreditRecord>> insertMapRecord,
		Map<String, Map<String, Map<String, RegistrationInfoVO>>> reMap,
		Map<String, Map<String, CreditRecord>> oldMap,
		Map<String,Map<String,CreditRecord>> updateMap ,
		Map<String,RegistrationInfoVO> inUpSignMap,
		Map<String,Map<String,CreditRecord>> insertMap,
		Map<String, CreditStandard> all_accountMap,
		Map<String, CreditStandard> all_juniorMap,
		Map<String, Map<String, CreditRecord>> juniorspecialMap,
		Map<String, Map<String, CreditRecord>> accountpecialMap) {
	
	for (Map.Entry<String, Map<String, CreditRecord>> entry:insertMapRecord.entrySet()) {
		String stukey = entry.getKey();//学员主键
		Map<String, CreditRecord>  subvoMap = entry.getValue();//key = 科目
		
		//这个学员报读哪些班级 key =classid
		Map<String, Map<String, RegistrationInfoVO>> classMap = reMap.get(stukey);
		if(classMap == null){
			if(newrevoMap.get(stukey) != null){
				classMap = newrevoMap.get(stukey);
				 newrevoMap.remove(stukey);
			}else{
				//没有报名表的学员不处理,有的数据是有国考没有报名表信息
				//并且以前没有计算过的不处理,报名表过来一段时间就暂停，然后又重新做一个一张
				for (Entry<String, CreditRecord> tentry: subvoMap.entrySet()) {
					String _subkey = tentry.getKey();
					String _s_s_key = stukey+"@"+_subkey;
					CreditRecord vo = tentry.getValue();
					Map<String, CreditRecord> _classMap = oldMap.get(_s_s_key);
					if(_classMap != null){
						for (Entry<String, CreditRecord> centry: _classMap.entrySet()) {
							CreditRecord oldvo =centry.getValue();
							//需要更新的vo
							boolean up = false;
							if(oldvo.getExamActualScore().intValue() < vo.getExamActualScore().intValue()){
								oldvo.setExamActualScore(vo.getExamActualScore());
								oldvo.setExamClaimScore(vo.getExamClaimScore());
								up = true;
							}
							if(oldvo.getWorkActualScore().intValue() < vo.getWorkActualScore().intValue()){
								oldvo.setWorkActualScore(vo.getWorkActualScore());
								oldvo.setWorkClaimScore(vo.getWorkClaimScore());
								up = true;
							}
							if(oldvo.getAttendanceActualScore().intValue() <vo.getAttendanceActualScore().intValue()){
								oldvo.setAttendanceActualScore(vo.getAttendanceActualScore());
								oldvo.setAttendanceClaimScore(vo.getAttendanceClaimScore());
								up = true;
							}
							
							if(up){
								//如果小于新的vo就更新
								Integer actualtotal = oldvo.getAttendanceActualScore()+oldvo.getExamActualScore()+oldvo.getWorkActualScore();
								Integer claimtotal = oldvo.getAttendanceClaimScore()+oldvo.getWorkClaimScore()+oldvo.getExamClaimScore();
								oldvo.setTotalScore(actualtotal);
								String stkey = oldvo.getNcUserId();
								String su_c_key  = oldvo.getNcSubjectId()+"@"+oldvo.getClassId();
								//只要不是特殊科目都计算是否合格
								if(all_juniorMap.get(vo.getNcSubjectId()) == null ){//&& all_accountMap.get(vo.getNcSubjectId()) ==null
									if(claimtotal != 0 && actualtotal.intValue() >= (int)(claimtotal.intValue()*0.8)){
										oldvo.setIsPass(0);
									}
								}
							
								//String s_s_c_key  = oldvo.getNcUserId()+"@"+oldvo.getNcSubjectId()+"@"+oldvo.getClassId();
								CreditRecord specialVO = oldvo;
								
								Map<String, CreditRecord> upvoMap = updateMap.get(stkey);
								//判断map是否存在该学员
								if(upvoMap == null){
									Map<String, CreditRecord> temp = new HashMap<String,CreditRecord>();
									temp.put(su_c_key, oldvo);
									updateMap.put(stkey, temp);
								}else{
									//判断科目+班型是否存在
									if(upvoMap.get(su_c_key) == null){
										upvoMap.put(su_c_key, oldvo);
									}else{
										CreditRecord upvo = upvoMap.get(su_c_key);
										if(upvo.getTotalScore() < oldvo.getTotalScore()){
											upvoMap.put(su_c_key, oldvo);
										}else{
											specialVO = upvo;
										}
									}
								}
								handleSpecialSubject(all_accountMap, all_juniorMap,juniorspecialMap, accountpecialMap,specialVO);
							}
						}
					}
				}
				continue;
			}
		}else{
			newrevoMap.remove(stukey);
		}
		
		
		//这个报名表下-这个班型对应科目与运算的科目匹配
		Map<String,Map<String,CreditRecord>> stuMap = new HashMap<String,Map<String,CreditRecord>>();
		//找出科目对应的班型
		for (Map.Entry<String, Map<String, RegistrationInfoVO>> tentry: classMap.entrySet()) {
			String classKey = tentry.getKey();
			//获取科目对应的班级信息
			Map<String, RegistrationInfoVO> submap = tentry.getValue();//key = 科目
			
			Map<String,CreditRecord> tempMap = new HashMap<String,CreditRecord>();
			boolean b = false;
			for (Entry<String, RegistrationInfoVO> subEntry: submap.entrySet()) {
				String subkey = subEntry.getKey();//科目
				RegistrationInfoVO rvo = subEntry.getValue();

				//运算的科目与报名表科目匹配
				CreditRecord cvo =subvoMap.get(subkey);
				if(cvo != null){
					b = true;
					if(cvo.getClassId() != null && !cvo.getClassId().equals(rvo.getClassId())){
						cvo = BeanUtil.cloneTo(cvo);
					}
					
					cvo.setClassId(rvo.getClassId());
					cvo.setSignDate(rvo.getSignDate());
					
					//设置应修分
					CreditStandard svo = mapSubject.get(subkey);
					cvo.setAttendanceClaimScore(svo.getAttendance_score());
					cvo.setWorkClaimScore(svo.getActivity_fraction());
					cvo.setExamClaimScore(svo.getGraduation_examination_score());
					cvo.setTotalScore(cvo.getAttendanceActualScore()+cvo.getWorkActualScore()+cvo.getExamActualScore());
					tempMap.put(subkey, cvo);
					if(signMap.get(rvo.getSignId()) == null){
						//插入
						CreditRecordSign signvo = getNewCreditRecordSign(rvo);
						inSignMap.put(signvo.getSignId(), signvo);
					}
					inUpSignMap.put(rvo.getSignId(), rvo);
					
				}else{
					//判断是否标准学分科目
					if(mapSubject.get(subkey)!=null){
						b = true;
						CreditStandard svo = mapSubject.get(subkey);
						CreditRecord cvo1 = new CreditRecord();
						//设置应修分
						cvo1.setAttendanceClaimScore(svo.getAttendance_score());
						cvo1.setWorkClaimScore(svo.getActivity_fraction());
						cvo1.setExamClaimScore(svo.getGraduation_examination_score());
						cvo1.setClassId(rvo.getClassId());
						cvo1.setSignDate(rvo.getSignDate());
						cvo1.setNcSubjectId(subkey);
						cvo1.setNcUserId(stukey);
						cvo1.setSubjectCode(svo.getCourse_code());
						cvo1.setSubjectName(svo.getSubject_name());
						tempMap.put(subkey, cvo1);
						if(signMap.get(rvo.getSignId()) == null){
							//插入
							CreditRecordSign signvo = getNewCreditRecordSign(rvo);
							inSignMap.put(signvo.getSignId(), signvo);
						}
						inUpSignMap.put(rvo.getSignId(), rvo);
					}
				}
			}
			
			if(b){
				//只要这个班型有一个科目匹配上运算学员科目或者标准学分科目就存起来
				stuMap.put(classKey, tempMap);
			}
			
		}
		computeStudentRecord(oldMap, updateMap, insertMap, stukey, stuMap,all_accountMap,all_juniorMap,juniorspecialMap,accountpecialMap);
		
	}
}

*//**
 * 
 * @param oldMap 本地学分表信息【key=（学员主键+科目主键）+班型】
 * @param updateMap 本地学分表信息【key=（学员主键+科目主键）+班型】
 * @param insertMap插入的学分信息【key=学员->(科目+班级)】
 * @param stukey 学员主键
 * @param stuMap 一个学员的科目信息【key=班型主键->科目主键】
 * @param all_accountMap 全部科目信息【会计证】-不用计算考试分
 * @param all_juniorMap 全部科目信息【初级职称】
 * @param juniorspecialMap 初级职称需要计算是否通过【key=学员+(科目+班级)】
 * @param accountpecialMap 会计证需要计算是否通过【key=学员+(科目+班级)】
 *//*
private void computeStudentRecord(
		Map<String, Map<String, CreditRecord>> oldMap,
		Map<String, Map<String, CreditRecord>> updateMap,
		Map<String, Map<String, CreditRecord>> insertMap, String stukey,
		Map<String,Map<String,CreditRecord>> stuMap, 
		Map<String, CreditStandard> all_accountMap,
		Map<String, CreditStandard> all_juniorMap,
		Map<String, Map<String, CreditRecord>> juniorspecialMap,
		Map<String, Map<String, CreditRecord>> accountpecialMap) {
	//一个学员多个班型的处理
	for (Entry<String, Map<String, CreditRecord>> subEntry: stuMap.entrySet()) {
		
		Map<String, CreditRecord> vosmap = subEntry.getValue();
		for (Entry<String, CreditRecord> entry: vosmap.entrySet()) {
			CreditRecord vo  = entry.getValue();
			String subkey = vo.getNcSubjectId();
			String classkey = vo.getClassId();
			String ssKey = stukey+"@"+subkey;
			//判断是更新还是插入
			if(oldMap.get(ssKey) != null){
				//多个班型下相同科目
				Map<String, CreditRecord> oldvos = oldMap.get(ssKey);
				boolean insert = true;
				for (Entry<String, CreditRecord> classentry: oldvos.entrySet()) {
					//如果数据库存在该学员科目的班型就更新，否则就插入，其他班型也更新
					String oldclassKey = classentry.getKey();
					if(classkey.equals(oldclassKey)){
						//如果一个都匹配不上就插入
						insert =  false;
					}
					CreditRecord oldvo = classentry.getValue();
						//不能按照总分算，因为不是每次都会有国考成绩，考试成绩，考勤分，
						boolean up = false;
						if(oldvo.getExamActualScore().intValue() < vo.getExamActualScore().intValue()){
							oldvo.setExamActualScore(vo.getExamActualScore());
							oldvo.setExamClaimScore(vo.getExamClaimScore());
							up = true;
						}
						if(oldvo.getWorkActualScore().intValue() < vo.getWorkActualScore().intValue()){
							oldvo.setWorkActualScore(vo.getWorkActualScore());
							oldvo.setWorkClaimScore(vo.getWorkClaimScore());
							up = true;
						}
						if(oldvo.getAttendanceActualScore().intValue() <vo.getAttendanceActualScore().intValue()){
							oldvo.setAttendanceActualScore(vo.getAttendanceActualScore());
							oldvo.setAttendanceClaimScore(vo.getAttendanceClaimScore());
							up = true;
						}
						if(up){
							//如果小于新的vo就更新
							Integer actualtotal = oldvo.getAttendanceActualScore()+oldvo.getExamActualScore()+oldvo.getWorkActualScore();
							Integer claimtotal = oldvo.getAttendanceClaimScore()+oldvo.getWorkClaimScore()+oldvo.getExamClaimScore();
							oldvo.setTotalScore(actualtotal);
							
							String stkey = oldvo.getNcUserId();
							String su_c_key  = oldvo.getNcSubjectId()+"@"+oldvo.getClassId();
							//只要不是特殊科目都计算是否合格
							if(all_juniorMap.get(vo.getNcSubjectId()) == null ){//&& all_accountMap.get(vo.getNcSubjectId()) ==null
								if(claimtotal != 0 && actualtotal.intValue() >= (int)(claimtotal.intValue()*0.8)){
									oldvo.setIsPass(0);
								}
							}
							
							
							CreditRecord specialVO =  oldvo;
							Map<String, CreditRecord> upvoMap = updateMap.get(stkey);
							//判断map是否存在该学员
							if(upvoMap == null){
								Map<String, CreditRecord> temp = new HashMap<String,CreditRecord>();
								temp.put(su_c_key, oldvo);
								updateMap.put(stkey, temp);
							}else{
								//判断科目+班型是否存在
								if(upvoMap.get(su_c_key) == null){
									upvoMap.put(su_c_key, oldvo);
								}else{
									CreditRecord upvo = upvoMap.get(su_c_key);
									if(upvo.getTotalScore() < oldvo.getTotalScore()){
										upvoMap.put(su_c_key, oldvo);
									}else{
										specialVO = upvo;
									}
								}
							}
							handleSpecialSubject(all_accountMap, all_juniorMap, juniorspecialMap,accountpecialMap, specialVO);
							
						}
				
				}
				if(insert){
					handleInsert(insertMap, all_accountMap, all_juniorMap,juniorspecialMap, accountpecialMap, vo);
					
				}
				
			}else{
				handleInsert(insertMap, all_accountMap, all_juniorMap,juniorspecialMap, accountpecialMap, vo);
				}
		}
	}
}

*//**
 * 记录需要特殊处理的科目信息
 * @param all_accountMap 全部科目信息【会计证】-不用计算考试分
 * @param all_juniorMap 全部科目信息【初级职称】
 * @param juniorspecialMap 初级职称需要计算是否通过【key=学员+(科目+班级)】
 * @param accountpecialMap 会计证需要计算是否通过【key=学员+(科目+班级)】
 * @param specialVO 待处理学分信息
 *//*
private void handleSpecialSubject(Map<String, CreditStandard> all_accountMap,
		Map<String, CreditStandard> all_juniorMap,
		Map<String, Map<String, CreditRecord>> juniorspecialMap,
		Map<String, Map<String, CreditRecord>> accountpecialMap, CreditRecord specialVO) {
	
	String stkey = specialVO.getNcUserId();
	String subid = specialVO.getNcSubjectId();
	if(all_juniorMap.get(specialVO.getNcSubjectId()) != null){
			//特殊科目处理
			Map<String, CreditRecord> subMap =juniorspecialMap.get(stkey);
			if(subMap == null){
				Map<String, CreditRecord> temp =new HashMap<String, CreditRecord>();
				temp.put(subid, specialVO);
				juniorspecialMap.put(stkey, temp);
			}else{
				CreditRecord old = subMap.get(subid);
				if(old == null){
					subMap.put(subid, specialVO);
				}else{
					if(old.getTotalScore() < specialVO.getTotalScore()){
						subMap.put(subid, specialVO);
					}
				}
			}
		}else if(all_accountMap.get(specialVO.getNcSubjectId()) != null){
			//特殊科目处理
			Map<String, CreditRecord> subMap =accountpecialMap.get(stkey);
			if(subMap == null){
				Map<String, CreditRecord> temp =new HashMap<String, CreditRecord>();
				temp.put(subid, specialVO);
				accountpecialMap.put(stkey, temp);
			}else{
				CreditRecord old = subMap.get(subid);
				if(old == null){
					subMap.put(subid, specialVO);
				}else{
					if(old.getTotalScore() < specialVO.getTotalScore()){
						subMap.put(subid, specialVO);
					}
				}
			}
		}
}

*//**
 * 存储需要插入的学分记录
 * @param insertMap插入的学分信息【key=学员->(科目+班级)】
 * @param all_accountMap 全部科目信息【会计证】-不用计算考试分
 * @param all_juniorMap 全部科目信息【初级职称】
 * @param juniorspecialMap 初级职称需要计算是否通过【key=学员+(科目+班级)】
 * @param accountpecialMap 会计证需要计算是否通过【key=学员+(科目+班级)】
 * @param vo 待处理学分信息
 *//*
private void handleInsert(Map<String, Map<String, CreditRecord>> insertMap,
		Map<String, CreditStandard> all_accountMap,
		Map<String, CreditStandard> all_juniorMap,
		Map<String, Map<String, CreditRecord>> juniorspecialMap,
		Map<String, Map<String, CreditRecord>> accountpecialMap, CreditRecord vo) {
	String stkey = vo.getNcUserId();
	String su_c_key  = vo.getNcSubjectId()+"@"+vo.getClassId();
	//插入
	Integer actualtotal = vo.getAttendanceActualScore()+vo.getExamActualScore()+vo.getWorkActualScore();
	
	Integer claimtotal = vo.getAttendanceClaimScore()+vo.getWorkClaimScore()+vo.getExamClaimScore();
	
	vo.setTotalScore(actualtotal);
	//只要不是特殊科目都计算是否合格
	if(all_juniorMap.get(vo.getNcSubjectId()) == null ){//&& all_accountMap.get(vo.getNcSubjectId()) ==null
		if(claimtotal != 0 && actualtotal.intValue() >= (int)(claimtotal.intValue()*0.8)){
			vo.setIsPass(0);
		}
	}

	//获取该学员的信息
	Map<String, CreditRecord> invoMap = insertMap.get(stkey);
	CreditRecord specialVO =  vo;
	//判断该学员是否已经存过
	if(invoMap == null){
		Map<String, CreditRecord> temp = new HashMap<String,CreditRecord>();
		temp.put(su_c_key, vo);
		insertMap.put(stkey, temp);
	}else{
		if(invoMap.get(su_c_key) == null){
			invoMap.put(su_c_key, vo);
		}else{
			CreditRecord invo = invoMap.get(su_c_key);
			if(invo.getTotalScore() < vo.getTotalScore()){
				invoMap.put(su_c_key, vo);
			}else{
				specialVO = invo;
			}
		}
	}
	handleSpecialSubject(all_accountMap, all_juniorMap, juniorspecialMap, accountpecialMap,specialVO);
}
  
private CreditRecordSign getNewCreditRecordSign(RegistrationInfoVO rvo) {
	CreditRecordSign vo = new CreditRecordSign();
	vo.setClassCode(rvo.getClassCode());
	vo.setClassId(rvo.getClassId());
	vo.setClassName(rvo.getClassName());
	vo.setIdCard(rvo.getIdCard());
	vo.setOrgId(rvo.getOrgId());
	vo.setOrgName(rvo.getOrgName());
	vo.setOrgCode(rvo.getOrgCode());
	vo.setPhone(rvo.getPhone());
	vo.setSignCode(rvo.getVbillcode());
	vo.setSignId(rvo.getSignId());
	vo.setStudentId(rvo.getStudentId());
	vo.setStudentName(rvo.getStudentName());
	vo.setSignDate(rvo.getSignDate());
	return vo;
}

*//**
 * 
 * @param mapSubject 标准学分科目map 【key = 科目主键】
 * @param newrevoMap 根据日期段查询的报名表信息【key = 学员id->班级id->科目id】
 * @param oldMap 本地学分表信息【key=（学员主键+科目主键）+班型】
 * @param updateMap  更新的学分信息【key=学员->(科目+班级)】
 * @param inSignMap 待插入或更新的报名表信息【key= 报名表主键】
 * @param insertMap 插入的学分信息【key=学员->(科目+班级)】
 * @param all_accountMap  全部科目信息【会计证】
 * @param all_juniorMap 全部科目信息【初级职称】
 * @param juniorspecialMap 初级职称需要计算是否通过【key=学员+(科目+班级)】
 * @param accountpecialMap 会计证需要计算是否通过【key=学员+(科目+班级)】
 *//*
private void getNewRecord(Map<String, CreditStandard> mapSubject,
		Map<String, Map<String, Map<String, RegistrationInfoVO>>> newrevoMap,
		Map<String, Map<String,CreditRecord>> oldMap,
		Map<String,Map<String,CreditRecord>> updateMap ,
		Map<String,RegistrationInfoVO> inUpSignMap ,
		Map<String,Map<String,CreditRecord>> insertMap, 
		Map<String, CreditStandard> all_accountMap,
		Map<String, CreditStandard> all_juniorMap, 
		Map<String, Map<String, CreditRecord>> juniorspecialMap,
		Map<String, Map<String, CreditRecord>> accountpecialMap) {
	
	for (Map.Entry<String, Map<String, Map<String, RegistrationInfoVO>>> entry:newrevoMap.entrySet()) {
		String stukey = entry.getKey();
		//班型对应的报名信息
		Map<String, Map<String, RegistrationInfoVO>> classMap = entry.getValue();
		Map<String,Map<String,CreditRecord>> stuMap = new HashMap<String,Map<String,CreditRecord>>();
		for (Map.Entry<String, Map<String, RegistrationInfoVO>> tentry: classMap.entrySet()) {
			String classKey = tentry.getKey();
			//班级下的科目
			Map<String, RegistrationInfoVO> submap = tentry.getValue();//key = 科目
			//List<CreditRecord> tempList = new ArrayList<CreditRecord>();
			Map<String,CreditRecord> tempMap = new HashMap<String,CreditRecord>();
			boolean b = false;
			for (Entry<String, RegistrationInfoVO> subEntry: submap.entrySet()) {
				String subkey = subEntry.getKey();//科目
				
				RegistrationInfoVO rvo = subEntry.getValue();
				//判断是否标准学分的科目
				if(mapSubject.get(subkey) != null){
					b = true;
					CreditStandard svo = mapSubject.get(subkey);
					CreditRecord cvo = new CreditRecord();
					//设置应修分
					cvo.setAttendanceClaimScore(svo.getAttendance_score());
					cvo.setWorkClaimScore(svo.getActivity_fraction());
					cvo.setExamClaimScore(svo.getGraduation_examination_score());
					
					cvo.setClassId(rvo.getClassId());
					cvo.setSignDate(rvo.getSignDate());
						if(signMap.get(rvo.getSignId()) == null){
						//插入
						CreditRecordSign signvo = getNewCreditRecordSign(rvo);
						inSignMap.put(signvo.getSignId(), signvo);
					}
					
					inUpSignMap.put(rvo.getSignId(), rvo);
					
					cvo.setNcSubjectId(subkey);
					cvo.setNcUserId(stukey);
					cvo.setSubjectCode(svo.getCourse_code());
					cvo.setSubjectName(svo.getSubject_name());
					tempMap.put(subkey, cvo);
				}
			}
			
			if(b){
				//只要该班型有一个科目匹配上就存起来
				stuMap.put(classKey, tempMap);
			}
		}
		computeStudentRecord(oldMap, updateMap, insertMap, stukey, stuMap,all_accountMap,all_juniorMap,juniorspecialMap,accountpecialMap);
	}
}

*//**
 * 计算结业考核 -考试实操重复考（不及格）
 * @param mapSubject 标准学分科目map 【key = 科目主键】
 * @param insertMapRecord 待插入的学分记录信息 【 key= 学员->科目】
 * @param studentList 历史考试失败的学员数据
 * @param delmap 成功学员，要在失败表删除的数据 【key = id】
 * @param oldfailmap 历史考试失败的学员信息 【key = 学员id->科目id】
 * @throws Exception
 *//*
private void computeExamPracticalToOne(Map<String, CreditStandard> mapSubject,
		Map<String, Map<String, CreditRecord>> insertMapRecord,
		 List<CreditFailStudent> studentList,Map<Integer,CreditFailStudent> delmap, Map<String, Map<String, CreditFailStudent>> oldfailmap) throws Exception {
	try{
		//获取所有不及格的学员pk
		 WebService1Stub ww = new WebService1Stub(); 
		 List<String> arrPlanIdList = new ArrayList<String>();
		 List<WorkTheorySoreVO> allvos = new ArrayList<WorkTheorySoreVO>();
		for (int i = 0; i < studentList.size(); i++) {
			CreditFailStudent stuvo = studentList.get(i);
			if(stuvo.getExamType() != 1){
				//不是考试类型不查询
				continue;
			}
			WebService1Stub.GetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode w = new GetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode();  
	    	w.setCourseCode(stuvo.getNcSubjectCode());
	    	w.setStudentNCCode(stuvo.getNcUserId());
	        String examstub_s = ww.getStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode(w).getGetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCodeResult();  
	        if(examstub_s == null){
	        	continue;
	        }
	    	List<WorkTheorySoreVO> vos = JsonUtil.fromJson(examstub_s,new TypeToken<List<WorkTheorySoreVO>>(){}.getType());
	    	for (int j = 0; j < vos.size(); j++) {
	    		arrPlanIdList.add(vos.get(j).getClassNCCode());
	    		allvos.add(vos.get(j));
	    	}
		}
		
		ArrangedPlanIdVO  arrPlanId = new ArrangedPlanIdVO();
    	arrPlanId.setArrPlanIdList(arrPlanIdList);
    	arrPlanId.setSecretKey(NcSyncConstant.getNcSecretkey());
	 	//根据最小和最大日期请求获取排课计划数据
	 	Map<String,String> apvoMap = getArrangedPlan(arrPlanId);
	 	
	 	for (int k = 0; k < allvos.size(); k++) {
	 		WorkTheorySoreVO wsvo = allvos.get(k);
	 		if(wsvo.getStandarRate()>=60){
	 			//根据排课计划主键获取科目主键，因为传过来的科目主键和NC的科目主键不同
	 			String subjectilesid = apvoMap.get(wsvo.getClassNCCode());
	 			
	 			if(oldfailmap.get(wsvo.getStudentNCCode()) != null){
	 				//如果是成功了就要删除
					Map<String, CreditFailStudent> map = oldfailmap.get(wsvo.getStudentNCCode());
					CreditFailStudent vo = map.get(subjectilesid);
					if(vo !=null){
						delmap.put(vo.getId(), vo);
					}
				}
	 			
	 			//根据科目主键获取科目标准学分表
	 			 CreditStandard standard = mapSubject.get(subjectilesid);
	 			 if(standard != null){
					int score = standard.getActivity_fraction();
					
				 Map<String, CreditRecord> submap = insertMapRecord.get(wsvo.getStudentNCCode());
					if(submap != null){
						CreditRecord record  = submap.get(subjectilesid);
						if(record != null){
							if(record.getExamActualScore() < score){
								record.setExamActualScore(score);
								record.setExamClaimScore(standard.getGraduation_examination_score());
								Integer total = record.getAttendanceActualScore()+record.getExamActualScore()+record.getWorkActualScore();
								record.setTotalScore(total);
							}
						}else{
							CreditRecord newRecord = new CreditRecord();
							newRecord.setNcUserId(wsvo.getStudentNCCode());
							newRecord.setNcSubjectId(subjectilesid);
							newRecord.setExamActualScore(score);
							newRecord.setExamClaimScore(standard.getGraduation_examination_score());
							newRecord.setSubjectName(standard.getSubject_name());
							newRecord.setSubjectCode(standard.getCourse_code());
							Integer total = newRecord.getAttendanceActualScore()+newRecord.getExamActualScore()+newRecord.getWorkActualScore();
							newRecord.setTotalScore(total);
							submap.put(newRecord.getNcSubjectId(), newRecord);
						}
						
					}else{

						CreditRecord newRecord = new CreditRecord();
						newRecord.setNcUserId(wsvo.getStudentNCCode());
						newRecord.setNcSubjectId(subjectilesid);
						newRecord.setExamActualScore(score);
						newRecord.setExamClaimScore(standard.getGraduation_examination_score());
						newRecord.setSubjectName(standard.getSubject_name());
						newRecord.setSubjectCode(standard.getCourse_code());
						Integer total = newRecord.getAttendanceActualScore()+newRecord.getExamActualScore()+newRecord.getWorkActualScore();
						newRecord.setTotalScore(total);
						Map<String, CreditRecord> temp = new HashMap<String, CreditRecord>();
						temp.put(newRecord.getNcSubjectId(), newRecord);
						insertMapRecord.put(newRecord.getNcUserId(), temp);
					
					}
					
	 			 }
	 		}
		}
	  } catch (Exception e) {
		  	e.printStackTrace();
			throw new Exception("计算考试实操重复考失败:"+e.getMessage());
	  }
}


*//**
 * 计算结业考核 -考试实操
 * @param mapSubject 标准学分科目map 【key = 科目主键】
 * @param insertMapRecord 标准学分科目map 【key = 科目主键】
 * @param stuIdMap 标准学分科目map 【key = 科目主键】
 * @param beginDate 开始日期
 * @param endDate 结束日期
 * @param failExam 考试失败的学员信息【key = 学员主键@科目主键】
 * @param oldfailmap 历史考试失败的学员信息 【key = 学员id->科目id】
 * @param att_work_exam_IdMap  存放【考勤+作业+考试的学员的id】
 * @throws Exception
 *//*
private void computeExamPractical(Map<String, CreditStandard> mapSubject,
		Map<String, Map<String, CreditRecord>> insertMapRecord, Map<String, Object> stuIdMap,  String beginDate, String endDate, Map<String,WorkTheorySoreVO> failExam, Map<String, Map<String, CreditFailStudent>> oldfailmap, Map<String, Object> att_work_exam_IdMap) throws Exception {
	//(webservice)
	//传学员pk和科目pk过去获取对应的结业实操分数(webservice)
	//如果学员考试分不及格要存起来，下次运行要重新去取数
	try {
		WebService1Stub.GetStudentOfClassEndingExamComplianceRateByTime w = new GetStudentOfClassEndingExamComplianceRateByTime();  
	    w.setBeginDate(beginDate);
	    w.setEndDate(endDate);
	    WebService1Stub ww = new WebService1Stub();
	    Options options = ww._getServiceClient().getOptions();
	    options.setProperty(HTTPConstants.SO_TIMEOUT, 600000);//10分钟
	    ww._getServiceClient().setOptions(options);
	    String examstub_s = ww.getStudentOfClassEndingExamComplianceRateByTime(w).getGetStudentOfClassEndingExamComplianceRateByTimeResult();  
	    if(examstub_s == null){
	    	return;
	    }
		List<WorkTheorySoreVO> vos = JsonUtil.fromJson(examstub_s,new TypeToken<List<WorkTheorySoreVO>>(){}.getType()); 
	 	List<String> arrPlanIdList = new ArrayList<String>();
    	for (int j = 0; j < vos.size(); j++) {
    		stuIdMap.put(vos.get(j).getStudentNCCode(), 1);
    		att_work_exam_IdMap.put(vos.get(j).getStudentNCCode(), 1);
    		arrPlanIdList.add(vos.get(j).getClassNCCode());
    	}
    	ArrangedPlanIdVO  arrPlanId = new ArrangedPlanIdVO();
    	arrPlanId.setArrPlanIdList(arrPlanIdList);
    	arrPlanId.setSecretKey(NcSyncConstant.getNcSecretkey());
	 	//根据请求获取排课计划数据
	 	Map<String,String> apvoMap = getArrangedPlan(arrPlanId);
	 	for (int i = 0; i < vos.size(); i++) {
	 		WorkTheorySoreVO wsvo = vos.get(i);
	 		//根据排课计划主键获取科目主键，因为传过来的科目主键和NC的科目主键不同
	 		String subjectilesid = apvoMap.get(wsvo.getClassNCCode());
	 		//String key = wsvo.getStudentNCCode()+"@"+subjectilesid;
	 		if(wsvo.getStandarRate()>=60){
	 			//根据科目主键获取科目标准学分表
	 			 CreditStandard standard = mapSubject.get(subjectilesid);
	 			 if(standard != null){
					int score = standard.getGraduation_examination_score();
					//根据学员主键获取
					Map<String, CreditRecord> submap = insertMapRecord.get(wsvo.getStudentNCCode());
					if(submap != null){
						CreditRecord record  = submap.get(subjectilesid);
						if(record != null){
							if(record.getExamActualScore() < score){
								record.setExamActualScore(score);
								record.setExamClaimScore(standard.getGraduation_examination_score());
								Integer total = record.getAttendanceActualScore()+record.getExamActualScore()+record.getWorkActualScore();
								record.setTotalScore(total);
							}
						}else{
							CreditRecord newRecord = new CreditRecord();
							newRecord.setNcUserId(wsvo.getStudentNCCode());
							newRecord.setNcSubjectId(subjectilesid);
							newRecord.setExamActualScore(score);;
							newRecord.setExamClaimScore(standard.getGraduation_examination_score());
							newRecord.setSubjectName(standard.getSubject_name());
							newRecord.setSubjectCode(standard.getCourse_code());
							Integer total = newRecord.getAttendanceActualScore()+newRecord.getExamActualScore()+newRecord.getWorkActualScore();
							newRecord.setTotalScore(total);
							submap.put(newRecord.getNcSubjectId(), newRecord);
						}
						
					}else{

						CreditRecord newRecord = new CreditRecord();
						newRecord.setNcUserId(wsvo.getStudentNCCode());
						newRecord.setNcSubjectId(subjectilesid);
						newRecord.setExamActualScore(score);;
						newRecord.setExamClaimScore(standard.getGraduation_examination_score());
						newRecord.setSubjectName(standard.getSubject_name());
						newRecord.setSubjectCode(standard.getCourse_code());
						Integer total = newRecord.getAttendanceActualScore()+newRecord.getExamActualScore()+newRecord.getWorkActualScore();
						newRecord.setTotalScore(total);
						Map<String, CreditRecord> temp = new HashMap<String, CreditRecord>();
						temp.put(newRecord.getNcSubjectId(), newRecord);
						insertMapRecord.put(newRecord.getNcUserId(), temp);
					
					}
	 			 }
	 		}else{
	 			String tkey = wsvo.getStudentNCCode()+"@"+subjectilesid;
	 			//判断该学员是不是存在失败表中
	 			Map<String, CreditFailStudent> failvo = oldfailmap.get(wsvo.getStudentNCCode());
	 			if(failvo !=null && failvo.size()>0){
	 				if(failvo.get(subjectilesid)==null){
	 					failExam.put(tkey, wsvo);
	 				}
	 			}else{
	 				   failExam.put(tkey, wsvo);
	 			}
	 		}
		}
	} catch (Exception e) {
		e.printStackTrace();
		throw new Exception ("获取结课考核 -考试实操 失败："+e.getMessage());
	}
}
*//**
 *  中级职称成绩分数
 * @param mapSubject  标准学分科目map 【key = 科目主键】
 * @param insertMapRecord 待插入的学分记录信息 【 key= 学员->科目】
 * @param stuIdvo 待插入的学分记录信息 【 key= 学员->科目】
 * @param failGuo 存放国考失败的学员【key = 学员主键】
 * @param delmap  成功学员，要在失败表删除的数据 【key = id】
 * @param oldfailmap 历史考试失败 的学员信息 【key = 学员id->科目id】
 * @param reMap 根据学员主键查询的报名表信息【 key=学员id->班级id->科目id】 
 * @param base_melMap 全部的经济法【中级职称】
 * @param base_mapMap 全部的中级会计实务【中级职称】
 * @param base_mfmMap  全部的中级财务管理【中级职称】
 * @param newrevoMap 根据日期段查询的报名表信息【key = 学员id->班级id->科目id】
 * @throws Exception
 *//*
private void computeMediumLevelAccountantScore(
		Map<String, CreditStandard> mapSubject,
		Map<String, Map<String, CreditRecord>> insertMapRecord, 
		StudentIdVO stuIdvo, Map<String, String> failGuo, 
		Map<Integer,CreditFailStudent> delmap, 
		Map<String, Map<String, CreditFailStudent>> oldfailmap,
		Map<String, CreditStandard> base_melMap,
		Map<String, CreditStandard> base_mapMap,
		Map<String, CreditStandard> base_mfmMap,
		Map<String, Map<String, Map<String, RegistrationInfoVO>>> reMap,
		Map<String, Map<String, Map<String, RegistrationInfoVO>>> newrevoMap) throws Exception {
	try {
		List<MediumLevelAccountantScoreVO> mlasList= getMediumLevelAccountantScore(stuIdvo);
		for (int i = 0; i < mlasList.size(); i++) {
			MediumLevelAccountantScoreVO mlas = mlasList.get(i);
			

			CreditStandard  mel = null;
			CreditStandard  mfm = null;
			CreditStandard  map = null;
			
			String stuid = mlas.getStudentId();
			Map<String, Map<String, RegistrationInfoVO>>  classMap = reMap.get(stuid);
			if(classMap == null){
				classMap = newrevoMap.get(stuid);
			}
			if(classMap !=null){
				for (Entry<String, Map<String, RegistrationInfoVO>> classEntry : classMap.entrySet()) {
					if(classEntry.getKey() == null){
						continue;
					}
					Map<String, RegistrationInfoVO> subMap = classEntry.getValue();
					for (Entry<String, RegistrationInfoVO> subEntry : subMap.entrySet()) {
						String subid =subEntry.getKey();
						//这个科目在会计基础的总科目里吗，如果在就拿到主键，会出现有的学员报读了，然后涨停了就没信息
						if(base_melMap.get(subid) !=null){
							mel = base_melMap.get(subid);
						}
						
						if(base_mapMap.get(subid) !=null){
							map =base_mapMap.get(subid);
						}
						
						if(base_mfmMap.get(subid) !=null){
							mfm =base_mfmMap.get(subid);
						}
					}
					
				}
			}else{
				continue;
			}
			
			if(mel != null){
				if("Y".equals(mlas.getJingjifa())){
					//判断是否是失败成员，然而这次又成功
					if(oldfailmap.get(mlas.getStudentId()) != null){
						Map<String, CreditFailStudent> tmap = oldfailmap.get(mlas.getStudentId());
						CreditFailStudent vo = tmap.get(mel.getNc_id());
						if(vo !=null){
							delmap.put(vo.getId(), vo);
						}
					}
					int score = mel.getGraduation_examination_score();
					Map<String, CreditRecord> submap = insertMapRecord.get(mlas.getStudentId());
					if(submap != null){
						CreditRecord record  = submap.get(mel.getNc_id());
						if(record != null){
							if(record.getExamActualScore() < score){
								record.setExamActualScore(score);
								record.setExamClaimScore(mel.getGraduation_examination_score());
								Integer total = record.getAttendanceActualScore()+record.getExamActualScore()+record.getWorkActualScore();
								record.setTotalScore(total);
							}
							
						}else{
							CreditRecord newRecord = new CreditRecord();
							newRecord.setNcUserId(mlas.getStudentId());
							newRecord.setNcSubjectId(mel.getNc_id());
							newRecord.setExamActualScore(score);
							newRecord.setExamClaimScore(mel.getGraduation_examination_score());
							newRecord.setSubjectName(mel.getSubject_name());
							newRecord.setSubjectCode(mel.getCourse_code());
							submap.put(newRecord.getNcSubjectId(), newRecord);
						}
					}else{

						CreditRecord newRecord = new CreditRecord();
						newRecord.setNcUserId(mlas.getStudentId());
						newRecord.setNcSubjectId(mel.getNc_id());
						newRecord.setExamActualScore(score);
						newRecord.setExamClaimScore(mel.getGraduation_examination_score());
						newRecord.setSubjectName(mel.getSubject_name());
						newRecord.setSubjectCode(mel.getCourse_code());
						Map<String, CreditRecord> temp = new HashMap<String, CreditRecord>();
						temp.put(newRecord.getNcSubjectId(), newRecord);
						insertMapRecord.put(newRecord.getNcUserId(), temp);
					
					}
				}else{
					Map<String, CreditFailStudent> failvo = oldfailmap.get(mlas.getStudentId());
		 			if(failvo !=null && failvo.size()>0){
		 				//有这个学员，对应科目是否也有
		 				CreditFailStudent vo = failvo.get(mel.getNc_id());
		 				if(vo==null){
		 					//存起来
			 				failGuo.put(mlas.getStudentId(), mel.getNc_id());
		 				}
		 			}else{
		 				//没有这个学员
		 				failGuo.put(mlas.getStudentId(), mel.getNc_id());
		 			}
				}
			}
			
			if(mfm != null)	{
				if("Y".equals(mlas.getZhongjicaiwuguanli())){
					//判断是否是失败成员，然而这次又成功
					if(oldfailmap.get(mlas.getStudentId()) != null){
						Map<String, CreditFailStudent> tmap = oldfailmap.get(mlas.getStudentId());
						CreditFailStudent vo = tmap.get(mfm.getNc_id());
						if(vo !=null){
							delmap.put(vo.getId(), vo);
						}
					}
					
					int score = mfm.getGraduation_examination_score();
					Map<String, CreditRecord> submap = insertMapRecord.get(mlas.getStudentId());
					if(submap != null){
						CreditRecord record  = submap.get(mfm.getNc_id());
						if(record != null){
							if(record.getExamActualScore() < score){
								record.setExamActualScore(score);
								record.setExamClaimScore(mfm.getGraduation_examination_score());
								Integer total = record.getAttendanceActualScore()+record.getExamActualScore()+record.getWorkActualScore();
								record.setTotalScore(total);
							}
						}else{
							CreditRecord newRecord = new CreditRecord();
							newRecord.setNcUserId(mlas.getStudentId());
							newRecord.setNcSubjectId(mfm.getNc_id());
							newRecord.setExamActualScore(score);
							newRecord.setExamClaimScore(mfm.getGraduation_examination_score());
							newRecord.setSubjectName(mfm.getSubject_name());
							newRecord.setSubjectCode(mfm.getCourse_code());
							submap.put(newRecord.getNcSubjectId(), newRecord);
						}
					}else{

						CreditRecord newRecord = new CreditRecord();
						newRecord.setNcUserId(mlas.getStudentId());
						newRecord.setNcSubjectId(mfm.getNc_id());
						newRecord.setExamActualScore(score);
						newRecord.setExamClaimScore(mfm.getGraduation_examination_score());
						newRecord.setSubjectName(mfm.getSubject_name());
						newRecord.setSubjectCode(mfm.getCourse_code());
						Map<String, CreditRecord> temp = new HashMap<String, CreditRecord>();
						temp.put(newRecord.getNcSubjectId(), newRecord);
						insertMapRecord.put(newRecord.getNcUserId(), temp);
					
					}
					
				}else{
					Map<String, CreditFailStudent> failvo = oldfailmap.get(mlas.getStudentId());
		 			if(failvo !=null && failvo.size()>0){
		 				//有这个学员，对应科目是否也有
		 				CreditFailStudent vo = failvo.get(mfm.getNc_id());
		 				if(vo==null){
		 					//存起来
			 				failGuo.put(mlas.getStudentId(), mfm.getNc_id());
		 				}
		 			}else{
		 				//没有这个学员
		 				failGuo.put(mlas.getStudentId(), mfm.getNc_id());
		 			}
				}
			}
			
			if(map != null){
				if("Y".equals(mlas.getZhongjishiwu())){
					//判断是否是失败成员，然而这次又成功
					if(oldfailmap.get(mlas.getStudentId()) != null){
						Map<String, CreditFailStudent> tmap = oldfailmap.get(mlas.getStudentId());
						CreditFailStudent vo = tmap.get(map.getNc_id());
						if(vo !=null){
							delmap.put(vo.getId(), vo);
						}
					}
					int score = map.getGraduation_examination_score();
					Map<String, CreditRecord> submap = insertMapRecord.get(mlas.getStudentId());
					if(submap != null){
						CreditRecord record  = submap.get(map.getNc_id());
						if(record != null){
							if(record.getExamActualScore() < score){
								record.setExamActualScore(score);
								record.setExamClaimScore(map.getGraduation_examination_score());
								Integer total = record.getAttendanceActualScore()+record.getExamActualScore()+record.getWorkActualScore();
								record.setTotalScore(total);
							}
						}else{
							CreditRecord newRecord = new CreditRecord();
							newRecord.setNcUserId(mlas.getStudentId());
							newRecord.setNcSubjectId(map.getNc_id());
							newRecord.setExamActualScore(score);
							newRecord.setExamClaimScore(map.getGraduation_examination_score());
							newRecord.setSubjectName(map.getSubject_name());
							newRecord.setSubjectCode(map.getCourse_code());
							Integer total = newRecord.getAttendanceActualScore()+newRecord.getExamActualScore()+newRecord.getWorkActualScore();
							newRecord.setTotalScore(total);
							submap.put(newRecord.getNcSubjectId(), newRecord);
						}
					}else{
						CreditRecord newRecord = new CreditRecord();
						newRecord.setNcUserId(mlas.getStudentId());
						newRecord.setNcSubjectId(map.getNc_id());
						newRecord.setExamActualScore(score);
						newRecord.setExamClaimScore(map.getGraduation_examination_score());
						newRecord.setSubjectName(map.getSubject_name());
						newRecord.setSubjectCode(map.getCourse_code());
						Integer total = newRecord.getAttendanceActualScore()+newRecord.getExamActualScore()+newRecord.getWorkActualScore();
						newRecord.setTotalScore(total);
						Map<String, CreditRecord> temp = new HashMap<String, CreditRecord>();
						temp.put(newRecord.getNcSubjectId(), newRecord);
						insertMapRecord.put(newRecord.getNcUserId(), temp);
					
					}
				}else{
					Map<String, CreditFailStudent> failvo = oldfailmap.get(mlas.getStudentId());
		 			if(failvo !=null && failvo.size()>0){
		 				//有这个学员，对应科目是否也有
		 				CreditFailStudent vo = failvo.get(map.getNc_id());
		 				if(vo==null){
		 					//存起来
			 				failGuo.put(mlas.getStudentId(), map.getNc_id());
		 				}
		 			}else{
		 				//没有这个学员
		 				failGuo.put(mlas.getStudentId(), map.getNc_id());
		 			}
				}
			}
	  }
	} catch (Exception e) {
		e.printStackTrace();
		throw new Exception("获取中级职称成绩分数失败："+e.getMessage());
	}
}


*//**
 * 初级职称成绩分数 
 * @param mapSubject 标准学分科目map 【key = 科目主键】
 * @param insertMapRecord 待插入的学分记录信息 【 key= 学员->科目】
 * @param stuIdvo 存放【日期段+考勤+作业+考试的学员的id】
 * @param failGuo 存放国考失败的学员【key = 学员主键】
 * @param delmap   成功学员，要在失败表删除的数据 【key = id】
 * @param oldfailmap 历史考试失败 的学员信息 【key = 学员id->科目id】
 * @param base_japMap  全部初级会计实务信息【初级职称】
 * @param base_jbelMap 全部经济法基础【初级职称】
 * @param all_juniorMap 全部科目信息【初级职称】
 * @param reMap  根据学员主键查询的报名表信息【 key=学员id->班级id->科目id】 
 * @param newrevoMap 根据日期段查询的报名表信息【key = 学员id->班级id->科目id】
 * @throws Exception
 *//*
private void computeJuniorLevelAccountantScore(
		Map<String, CreditStandard> mapSubject,
		Map<String, Map<String, CreditRecord>> insertMapRecord, 
		StudentIdVO stuIdvo, Map<String, String> failGuo,
		Map<Integer,CreditFailStudent> delmap,
		Map<String, Map<String, CreditFailStudent>> oldfailmap,
		Map<String, CreditStandard> base_japMap,
		Map<String, CreditStandard> base_jbelMap,
		Map<String, CreditStandard> all_juniorMap,
		Map<String, Map<String, Map<String, RegistrationInfoVO>>> reMap,
		Map<String, Map<String, Map<String, RegistrationInfoVO>>> newrevoMap) throws Exception {
	try {
		List<JuniorLevelAccountantScoreVO> jasList = getJuniorLevelAccountantScore(stuIdvo);
		for (int i = 0; i < jasList.size(); i++) {
			JuniorLevelAccountantScoreVO jasvo = jasList.get(i);
			if("Y".equals(jasvo.getIspass())){
				//判断是否是失败成员，然而这次又成功
				if(oldfailmap.get(jasvo.getStudentId()) != null){
					Map<String, CreditFailStudent> map = oldfailmap.get(jasvo.getStudentId());
					CreditFailStudent vo = map.get(SubjectIdUtil.JUNIOR);
					if(vo !=null){
						delmap.put(vo.getId(), vo);
					}
				}
				CreditStandard  jap = null;
				CreditStandard  jbel = null;
				
				String stuid = jasvo.getStudentId();
				Map<String, Map<String, RegistrationInfoVO>>  classMap = reMap.get(stuid);
				if(classMap == null){
					classMap = newrevoMap.get(stuid);
				}
				if(classMap !=null){
					for (Entry<String, Map<String, RegistrationInfoVO>> classEntry : classMap.entrySet()) {
						if(classEntry.getKey() == null){
							continue;
						}
						Map<String, RegistrationInfoVO> subMap = classEntry.getValue();
						for (Entry<String, RegistrationInfoVO> subEntry : subMap.entrySet()) {
							String subid =subEntry.getKey();
							//这个科目在会计基础的总科目里吗，如果在就拿到主键，会出现有的学员报读了，然后涨停了就没信息
							if(base_japMap.get(subid) !=null){
								jap = base_japMap.get(subid);
							}
							
							if(base_jbelMap.get(subid) !=null){
								jbel =base_jbelMap.get(subid);
							}
						}
					}
					
				}else{
					continue;
				}
				
				
				if(jap != null){
					int score = jap.getGraduation_examination_score();

					Map<String, CreditRecord> submap = insertMapRecord.get(jasvo.getStudentId());
					if(submap != null){
						CreditRecord record  = submap.get(jap.getNc_id());
						if(record != null){
							if(record.getExamActualScore() < score){
								record.setExamActualScore(score);
								record.setExamClaimScore(jap.getGraduation_examination_score());
							}
						}else{
							CreditRecord newRecord = new CreditRecord();
							newRecord.setNcUserId(jasvo.getStudentId());
							newRecord.setNcSubjectId(jap.getNc_id());
							newRecord.setExamActualScore(score);
							newRecord.setExamClaimScore(jap.getGraduation_examination_score());
							newRecord.setSubjectName(jap.getSubject_name());
							newRecord.setSubjectCode(jap.getCourse_code());
							submap.put(newRecord.getNcSubjectId(), newRecord);
						}
						
					}else{
						CreditRecord newRecord = new CreditRecord();
						newRecord.setNcUserId(jasvo.getStudentId());
						newRecord.setNcSubjectId(jap.getNc_id());
						newRecord.setExamActualScore(score);
						newRecord.setExamClaimScore(jap.getGraduation_examination_score());
						newRecord.setSubjectName(jap.getSubject_name());
						newRecord.setSubjectCode(jap.getCourse_code());
						Map<String, CreditRecord> temp = new HashMap<String, CreditRecord>();
						temp.put(newRecord.getNcSubjectId(), newRecord);
						insertMapRecord.put(newRecord.getNcUserId(), temp);
					
					}
			
				}
				
				if(jbel != null){
					int score = jbel.getGraduation_examination_score();
					Map<String, CreditRecord> submap = insertMapRecord.get(jasvo.getStudentId());
					if(submap != null){
						CreditRecord record  = submap.get(jbel.getNc_id());

						if(record != null){
							if(record.getExamActualScore() <score){
								record.setExamActualScore(score);
								record.setExamClaimScore(jbel.getGraduation_examination_score());
							}
						}else{
							CreditRecord newRecord = new CreditRecord();
							newRecord.setNcUserId(jasvo.getStudentId());
							newRecord.setNcSubjectId(jbel.getNc_id());
							newRecord.setExamActualScore(score);
							newRecord.setExamClaimScore(jbel.getGraduation_examination_score());
							newRecord.setSubjectName(jbel.getSubject_name());
							newRecord.setSubjectCode(jbel.getCourse_code());
							submap.put(newRecord.getNcSubjectId(), newRecord);
						}
					}else{
						CreditRecord newRecord = new CreditRecord();
						newRecord.setNcUserId(jasvo.getStudentId());
						newRecord.setNcSubjectId(jbel.getNc_id());
						newRecord.setExamActualScore(score);
						newRecord.setExamClaimScore(jbel.getGraduation_examination_score());
						newRecord.setSubjectName(jbel.getSubject_name());
						newRecord.setSubjectCode(jbel.getCourse_code());
						Map<String, CreditRecord> temp = new HashMap<String, CreditRecord>();
						temp.put(newRecord.getNcSubjectId(), newRecord);
						insertMapRecord.put(newRecord.getNcUserId(), temp);
					
					}
				}
				
			
			}else{
				//判断该学员是不是存在失败表中
	 			Map<String, CreditFailStudent> failvo = oldfailmap.get(jasvo.getStudentId());
	 			if(failvo ==null || failvo.size()==0){
	 				//存起来
	 				failGuo.put(jasvo.getStudentId(),SubjectIdUtil.JUNIOR);
	 			}else{
	 				CreditFailStudent vo = failvo.get(SubjectIdUtil.JUNIOR);
	 				if(vo == null){
	 					failGuo.put(jasvo.getStudentId(), SubjectIdUtil.JUNIOR);
	 				}
	 			}
				
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
		throw new Exception("获取初级职称成绩分数失败:"+e.getMessage());
	}
}


*//**
 * 计算会计证成绩分数 
 * @param mapSubject  标准学分科目map 【key = 科目主键】
 * @param insertMapRecord 待插入的学分记录信息 【 key= 学员->科目】
 * @param stuIdvo 待插入的学分记录信息 【 key= 学员->科目】
 * @param failGuo 存放国考失败的学员【key = 学员主键】
 * @param delmap  成功学员，要在失败表删除的数据 【key = id】
 * @param oldfailmap 历史考试失败 的学员信息 【key = 学员id->科目id】
 * @param reMap 根据学员主键查询的报名表信息【 key=学员id->班级id->科目id】 
 * @param base_abMap  全部的会计基础【会计证】
 * @param base_afepeMap 全部的财经法规与职业道德【会计证】
 * @param base_aacMap 全部的初级会计电算化【会计证】
 * @param newrevoMap 根据日期段查询的报名表信息【key = 学员id->班级id->科目id】
 * @throws Exception
 *//*
private void computeAccountingCertificateScore(
		Map<String, CreditStandard> mapSubject,
		Map<String, Map<String, CreditRecord>> insertMapRecord,  
		StudentIdVO stuIdvo, 
		Map<String, String> failGuo, Map<Integer,CreditFailStudent> delmap,
		Map<String, Map<String, CreditFailStudent>> oldfailmap,
		Map<String, Map<String, Map<String, RegistrationInfoVO>>> reMap,
		Map<String, CreditStandard> base_abMap,
		Map<String, CreditStandard> base_afepeMap, 
		Map<String, CreditStandard> base_aacMap,
		Map<String, Map<String, Map<String, RegistrationInfoVO>>> newrevoMap) throws Exception {
	//会计证成绩分数 
	try {
		List<AccountingCertificateScoreVO> acsList = getAccountingCertificateScore(stuIdvo);
		for (int i = 0; i < acsList.size(); i++) {
			AccountingCertificateScoreVO acsvo = acsList.get(i);
			if("Y".equals(acsvo.getIspass())){
				//判断是否是失败成员，然而这次又成功
				Map<String, CreditFailStudent> map = oldfailmap.get(acsvo.getStudentId());
				if(map != null){
					CreditFailStudent vo = map.get(SubjectIdUtil.ACCOUNTING);
					if(vo !=null){
						delmap.put(vo.getId(), vo);
					}
				}
				
				CreditStandard  aac = null;
				CreditStandard  ab = null;
				CreditStandard  afepe = null;
				
				String stuid = acsvo.getStudentId();
				Map<String, Map<String, RegistrationInfoVO>>  classMap = reMap.get(stuid);
				if(classMap == null){
					classMap = newrevoMap.get(stuid);
				}
				if(classMap !=null){
					for (Entry<String, Map<String, RegistrationInfoVO>> classEntry : classMap.entrySet()) {
						if(classEntry.getKey() == null){
							continue;
						}
						Map<String, RegistrationInfoVO> subMap = classEntry.getValue();
						for (Entry<String, RegistrationInfoVO> subEntry : subMap.entrySet()) {
							String subid =subEntry.getKey();
							//这个科目在会计基础的总科目里吗，如果在就拿到主键，会出现有的学员报读了，然后涨停了就没信息
							if(base_abMap.get(subid) !=null){
								aac = base_abMap.get(subid);
							}
							
							if(base_afepeMap.get(subid) !=null){
								ab =base_afepeMap.get(subid);
							}
							
							if(base_aacMap.get(subid) !=null){
								afepe =base_aacMap.get(subid);
							}
						}
						
					}
					
				}else{
					continue;
				}
				if(aac != null){
					int score = aac.getGraduation_examination_score();
					
					Map<String, CreditRecord> submap = insertMapRecord.get(acsvo.getStudentId());
					if(submap != null){
						CreditRecord record  = submap.get(aac.getNc_id());
						if(record != null){
							if(record.getExamActualScore() < score){
								record.setExamActualScore(score);
								record.setExamClaimScore(aac.getGraduation_examination_score());
							
							}
						}else{
							CreditRecord newRecord = new CreditRecord();
							newRecord.setNcUserId(acsvo.getStudentId());
							newRecord.setNcSubjectId(aac.getNc_id());
							newRecord.setExamActualScore(score);
							newRecord.setExamClaimScore(aac.getGraduation_examination_score());
							newRecord.setSubjectName(aac.getSubject_name());
							newRecord.setSubjectCode(aac.getCourse_code());
							
							submap.put(newRecord.getNcSubjectId(), newRecord);
						}
						
					}else{
						CreditRecord newRecord = new CreditRecord();
						newRecord.setNcUserId(acsvo.getStudentId());
						newRecord.setNcSubjectId(aac.getNc_id());
						newRecord.setExamActualScore(score);
						newRecord.setExamClaimScore(aac.getGraduation_examination_score());
						newRecord.setSubjectName(aac.getSubject_name());
						newRecord.setSubjectCode(aac.getCourse_code());
						
						Map<String, CreditRecord> temp = new HashMap<String, CreditRecord>();
						temp.put(newRecord.getNcSubjectId(), newRecord);
						insertMapRecord.put(newRecord.getNcUserId(), temp);
					}
				
				}
				
				
				if(ab != null){
					int score = ab.getGraduation_examination_score();
					
					Map<String, CreditRecord> submap = insertMapRecord.get(acsvo.getStudentId());
					if(submap != null){
						CreditRecord record  = submap.get(ab.getNc_id());
						if(record != null){
							if(record.getExamActualScore() < score){
								record.setExamActualScore(score);
								record.setExamClaimScore(ab.getGraduation_examination_score());
							}
						}else{
							CreditRecord newRecord = new CreditRecord();
							newRecord.setNcUserId(acsvo.getStudentId());
							newRecord.setNcSubjectId(ab.getNc_id());
							newRecord.setExamActualScore(score);
							newRecord.setExamClaimScore(ab.getGraduation_examination_score());
							newRecord.setSubjectName(ab.getSubject_name());
							newRecord.setSubjectCode(ab.getCourse_code());
							
							submap.put(newRecord.getNcSubjectId(), newRecord);
						}
					}else{

						CreditRecord newRecord = new CreditRecord();
						newRecord.setNcUserId(acsvo.getStudentId());
						newRecord.setNcSubjectId(ab.getNc_id());
						newRecord.setExamActualScore(score);
						newRecord.setExamClaimScore(ab.getGraduation_examination_score());
						newRecord.setSubjectName(ab.getSubject_name());
						newRecord.setSubjectCode(ab.getCourse_code());
						
						Map<String, CreditRecord> temp = new HashMap<String, CreditRecord>();
						temp.put(newRecord.getNcSubjectId(), newRecord);
						insertMapRecord.put(newRecord.getNcUserId(), temp);
					}
					
				}
				
				if(afepe != null){
					int score = afepe.getGraduation_examination_score();
					Map<String, CreditRecord> submap = insertMapRecord.get(acsvo.getStudentId());
					if(submap != null){
						CreditRecord record  = submap.get(afepe.getNc_id());
						if(record != null){
							if(record.getExamActualScore() < score){
								record.setExamActualScore(score);
								record.setExamClaimScore(afepe.getGraduation_examination_score());
								
							}
						}else{
							CreditRecord newRecord = new CreditRecord();
							newRecord.setNcUserId(acsvo.getStudentId());
							newRecord.setNcSubjectId(afepe.getNc_id());
							newRecord.setExamActualScore(score);
							newRecord.setExamClaimScore(afepe.getGraduation_examination_score());
							newRecord.setSubjectName(afepe.getSubject_name());
							newRecord.setSubjectCode(afepe.getCourse_code());
							
							submap.put(newRecord.getNcSubjectId(), newRecord);
						}
					}else{

						CreditRecord newRecord = new CreditRecord();
						newRecord.setNcUserId(acsvo.getStudentId());
						newRecord.setNcSubjectId(afepe.getNc_id());
						newRecord.setExamActualScore(score);
						newRecord.setExamClaimScore(afepe.getGraduation_examination_score());
						newRecord.setSubjectName(afepe.getSubject_name());
						newRecord.setSubjectCode(afepe.getCourse_code());
						
						Map<String, CreditRecord> temp = new HashMap<String, CreditRecord>();
						temp.put(newRecord.getNcSubjectId(), newRecord);
						insertMapRecord.put(newRecord.getNcUserId(), temp);
					
					}
				}
			}else{
				//判断该学员是不是存在失败表中
	 			Map<String, CreditFailStudent> failvo = oldfailmap.get(acsvo.getStudentId());
	 			if(failvo ==null || failvo.size()==0){
	 				//存放不及格的学员，下次要查询
	 					failGuo.put(acsvo.getStudentId(), SubjectIdUtil.ACCOUNTING);
	 			}else{
	 				CreditFailStudent vo = failvo.get(SubjectIdUtil.ACCOUNTING);
	 				if(vo == null){
	 					failGuo.put(acsvo.getStudentId(), SubjectIdUtil.ACCOUNTING);
	 				}
	 			}
				
				
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
		throw new Exception("获取会计证成绩失败:"+e.getMessage());
	}

}

*//**
 * 作业理论分
 * @param mapSubject 标准学分科目map 【key = 科目主键】
 * @param insertMapRecord 待插入的学分记录信息 【 key= 学员->科目】
 * @param stuIdMap  存放【日期段+考勤+作业+考试的学员的id】
 * @param beginDate 开始日期
 * @param endDate 结束日期
 * @param att_work_exam_IdMap 存放【考勤+作业+考试的学员的id】
 * @throws Exception
 *//*
private void computeBusyworkTheory(Map<String, CreditStandard> mapSubject,
		Map<String, Map<String, CreditRecord>> insertMapRecord, Map<String, Object> stuIdMap, String beginDate, String endDate, Map<String, Object> att_work_exam_IdMap) throws Exception {
	try {
		 	WebService1Stub.GetStudentOfClassEndingTaskComplianceRateByTime task = new GetStudentOfClassEndingTaskComplianceRateByTime();  
		 	task.setBeginDate(beginDate);
		 	task.setEndDate(endDate);
		 	WebService1Stub taskstub = new WebService1Stub();
		 	 Options options = taskstub._getServiceClient().getOptions();
		     options.setProperty(HTTPConstants.SO_TIMEOUT, 600000);//10分钟
		     taskstub._getServiceClient().setOptions(options);
		 	String taskstub_s = taskstub.getStudentOfClassEndingTaskComplianceRateByTime(task).getGetStudentOfClassEndingTaskComplianceRateByTimeResult();  
		 	if(taskstub_s == null){
		 		return;
		 	}
		 	List<WorkTheorySoreVO> vos = JsonUtil.fromJson(taskstub_s,new TypeToken<List<WorkTheorySoreVO>>(){}.getType()); 
		 	List<String> arrPlanIdList = new ArrayList<String>();
	    	for (int j = 0; j < vos.size(); j++) {
	    		arrPlanIdList.add(vos.get(j).getClassNCCode());
	    		stuIdMap.put(vos.get(j).getStudentNCCode(), 1);
	    		att_work_exam_IdMap.put(vos.get(j).getStudentNCCode(), 1);
	    	}
	    	ArrangedPlanIdVO  arrPlanId = new ArrangedPlanIdVO();
	    	arrPlanId.setArrPlanIdList(arrPlanIdList);
	    	arrPlanId.setSecretKey(NcSyncConstant.getNcSecretkey());
		 	//根据请求获取排课计划数据
		 	Map<String,String> apvoMap = getArrangedPlan(arrPlanId);
		 	for (int i = 0; i < vos.size(); i++) {
		 		WorkTheorySoreVO wsvo = vos.get(i);
		 		if(wsvo.getStandarRate()>=90){
		 			//根据排课计划主键获取科目主键，因为传过来的科目主键和NC的科目主键不同
		 			String subjectilesid = apvoMap.get(wsvo.getClassNCCode());
		 			//根据科目主键获取科目标准学分表
		 			 CreditStandard standard = mapSubject.get(subjectilesid);
		 			 if(standard != null){
						int score = standard.getActivity_fraction();
						//根据学员主键获取
						Map<String, CreditRecord> submap = insertMapRecord.get(wsvo.getStudentNCCode());
						if(submap != null){
							CreditRecord record  = submap.get(subjectilesid);
							if(record != null){
								if(record.getWorkActualScore() < score){
									record.setWorkActualScore(score);
									record.setWorkClaimScore(standard.getActivity_fraction());
									Integer total = record.getAttendanceActualScore()+record.getExamActualScore()+record.getWorkActualScore();
									record.setTotalScore(total);
								}
							}else{
								CreditRecord newRecord = new CreditRecord();
								newRecord.setNcUserId(wsvo.getStudentNCCode());
								newRecord.setNcSubjectId(subjectilesid);
								newRecord.setWorkActualScore(score);;
								newRecord.setWorkClaimScore(standard.getActivity_fraction());
								newRecord.setSubjectName(standard.getSubject_name());
								newRecord.setSubjectCode(standard.getCourse_code());
								Integer total = newRecord.getAttendanceActualScore()+newRecord.getExamActualScore()+newRecord.getWorkActualScore();
								newRecord.setTotalScore(total);
								submap.put(newRecord.getNcSubjectId(), newRecord);
							}
						}else{

							CreditRecord newRecord = new CreditRecord();
							newRecord.setNcUserId(wsvo.getStudentNCCode());
							newRecord.setNcSubjectId(subjectilesid);
							
							newRecord.setWorkActualScore(score);;
							newRecord.setWorkClaimScore(standard.getActivity_fraction());
							newRecord.setSubjectName(standard.getSubject_name());
							newRecord.setSubjectCode(standard.getCourse_code());
							Integer total = newRecord.getAttendanceActualScore()+newRecord.getExamActualScore()+newRecord.getWorkActualScore();
							newRecord.setTotalScore(total);
							Map<String, CreditRecord> temp = new HashMap<String, CreditRecord>();
							temp.put(newRecord.getNcSubjectId(), newRecord);
							insertMapRecord.put(newRecord.getNcUserId(), temp);
						
						}
						
		 			 }
		 		}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("获取【作业理论】失败:"+e.getMessage());
		}
}
*//**
 * 作业实操分
 * @param mapSubject  标准学分科目map 【key = 科目主键】
 * @param insertMapRecord 待插入的学分记录信息 【 key= 学员->科目】
 * @param stuIdMap  存放【日期段+考勤+作业+考试的学员的id】
 * @param beginDate 开始日期
 * @param endDate  结束日期
 * @param att_work_exam_IdMap 存放【考勤+作业+考试的学员的id】
 * @throws Exception
 *//*
private void computeBusyworkPractical(Map<String, CreditStandard> mapSubject,
		Map<String, Map<String, CreditRecord>> insertMapRecord, Map<String, Object> stuIdMap,  String beginDate, String endDate, Map<String, Object> att_work_exam_IdMap) throws Exception {
	//获取在读的学员（上个月已经结课）的实操作业分数（NC新单据）（查询语句），如果有个别学员要重新计算就单独计算(ok)
	try {
		List<ClassEndingBusyworkVO> busywork = getClassEndingBusywork(beginDate,endDate,NcSyncConstant.getNcSecretkey());
		if(busywork == null){
			return;
		}
		for (int i = 0; i < busywork.size(); i++) {
			ClassEndingBusyworkVO work = busywork.get(i);
			stuIdMap.put(work.getStudentId(), 1);
			att_work_exam_IdMap.put(work.getStudentId(), 1);
			if("Y".equals(work.getDef11())){//是否合格
				CreditStandard  standard = mapSubject.get(work.getSubjectilesId());
				if(standard != null){
					int score = standard.getActivity_fraction();
					
					//根据学员主键获取
					Map<String, CreditRecord> submap = insertMapRecord.get(work.getStudentId());
					if(submap != null){
						CreditRecord record = submap.get(work.getSubjectilesId());
						if(record != null){
							if(record.getWorkActualScore() < score){
								record.setWorkActualScore(score);
								record.setWorkClaimScore(standard.getActivity_fraction());
								Integer total = record.getAttendanceActualScore()+record.getExamActualScore()+record.getWorkActualScore();
								record.setTotalScore(total);
							}
						}else{
							CreditRecord newRecord = new CreditRecord();
							newRecord.setNcUserId(work.getStudentId());
							newRecord.setNcSubjectId(work.getSubjectilesId());
							newRecord.setWorkActualScore(score);
							newRecord.setWorkClaimScore(standard.getActivity_fraction());
							newRecord.setSubjectName(standard.getSubject_name());
							newRecord.setSubjectCode(standard.getCourse_code());
							Integer total = newRecord.getAttendanceActualScore()+newRecord.getExamActualScore()+newRecord.getWorkActualScore();
							newRecord.setTotalScore(total);
							//是否通过
							Integer baseTotal = standard.getTotal_credits();
							if(baseTotal.intValue()*0.8 == total.intValue()){
								newRecord.setIsPass(0);
							}
							submap.put(newRecord.getNcSubjectId(), newRecord);
						}
					}else{

						CreditRecord newRecord = new CreditRecord();
						newRecord.setNcUserId(work.getStudentId());
						newRecord.setNcSubjectId(work.getSubjectilesId());
						newRecord.setWorkActualScore(score);
						newRecord.setWorkClaimScore(standard.getActivity_fraction());
						newRecord.setSubjectName(standard.getSubject_name());
						newRecord.setSubjectCode(standard.getCourse_code());
						Integer total = newRecord.getAttendanceActualScore()+newRecord.getExamActualScore()+newRecord.getWorkActualScore();
						newRecord.setTotalScore(total);
						Map<String, CreditRecord> temp = new HashMap<String, CreditRecord>();
						temp.put(newRecord.getNcSubjectId(), newRecord);
						insertMapRecord.put(newRecord.getNcUserId(), temp);
					
					}
				}
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
		throw new Exception("获取【作业实操】失败:"+e.getMessage());
	}
	
}

*//**
 * 计算考勤分数
 * @param mapSubject 标准学分科目map 【key = 科目主键】
 * @param insertMapRecord 待插入的学分记录信息 【 key= 学员->科目】
 * @param stuIdMap  存放【日期段+考勤+作业+考试的学员的id】
 * @param beginDate 开始日期
 * @param endDate 结束日期
 * @param att_work_exam_IdMap 存放【考勤+作业+考试的学员的id】
 * @throws Exception
 *//*
private void computeAttendance(Map<String, CreditStandard> mapSubject,
		Map<String, Map<String, CreditRecord>> insertMapRecord, Map<String, Object> stuIdMap, String beginDate, String endDate, Map<String, Object> att_work_exam_IdMap) throws Exception {
	//获取上个月已经结课的学员的考勤（ok） 
	try{
		List<ClassEndingAttendanceVO> attendance  = getClassEndingAttendance(beginDate,endDate,NcSyncConstant.getNcSecretkey());
		if(attendance == null){
			return;
		}
		for (int i = 0; i < attendance.size(); i++) {
			ClassEndingAttendanceVO  att = attendance.get(i);
			stuIdMap.put(att.getStudentId(), 1);
			att_work_exam_IdMap.put(att.getStudentId(), 1);
			if(att.getAttendanceRate() >= 90){
				//出勤率达到90%为合格，获得出勤分
				//拿到学分标准表的对应科目学分
				 CreditStandard standard = mapSubject.get(att.getSubjectilesId());
				if(standard != null){
					int score = standard.getAttendance_score();
					//String key = att.getStudentId()+"@"+att.getSubjectilesId();
					//根据学员主键获取
					Map<String, CreditRecord> submap = insertMapRecord.get(att.getStudentId());
					
					if(submap != null){
						CreditRecord record =  submap.get(att.getSubjectilesId());
						if(record != null){
							if(record.getAttendanceActualScore() < score){
								record.setAttendanceActualScore(score);
								record.setAttendanceClaimScore(standard.getAttendance_score());
							}
						}else{
							CreditRecord newRecord = new CreditRecord();
							newRecord.setNcUserId(att.getStudentId());
							newRecord.setNcSubjectId(att.getSubjectilesId());
							newRecord.setAttendanceActualScore(score);
							newRecord.setAttendanceClaimScore(standard.getAttendance_score());
							newRecord.setSubjectName(standard.getSubject_name());
							newRecord.setSubjectCode(standard.getCourse_code());
							submap.put(newRecord.getNcSubjectId(), newRecord);
						}
					}else{
						CreditRecord newRecord = new CreditRecord();
						newRecord.setNcUserId(att.getStudentId());
						newRecord.setNcSubjectId(att.getSubjectilesId());
						newRecord.setAttendanceActualScore(score);
						newRecord.setAttendanceClaimScore(standard.getAttendance_score());
						newRecord.setSubjectName(standard.getSubject_name());
						newRecord.setSubjectCode(standard.getCourse_code());
						Map<String, CreditRecord> temp = new HashMap<String, CreditRecord>();
						temp.put(newRecord.getNcSubjectId(), newRecord);
						insertMapRecord.put(newRecord.getNcUserId(), temp);
					
					}
				}
			}
			
		}
	}catch(Exception e){
		e.printStackTrace();
		throw new Exception("获取考勤数据失败:"+e.getMessage());
	}
}
  

	*//**
	 * 日期转为字符串
	 * @param time
	 * @return
	 *//*
	public  String dateToString(Date time){ 
	    SimpleDateFormat formatter; 
	    formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
	    String ctime = formatter.format(time); 
	    return ctime; 
	} 
  
	
	*//**
	 * 获取中级职称成绩分数
	 * @return
	 * @throws Exception 
	 *//*
	private List<MediumLevelAccountantScoreVO> getMediumLevelAccountantScore(StudentIdVO stuIdvo) throws Exception {
		String result5 = HttpRequest.sendPost(NcSyncConstant.getMediumLevelAccountantUrl(),JsonUtil.toJson(stuIdvo),"json");
		HttpServiceResult s = JsonUtil.fromJson(result5, new TypeToken<HttpServiceResult>() {}.getType());
		if(s.getData() == null){
			return new ArrayList<MediumLevelAccountantScoreVO>();
		}
		return JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<MediumLevelAccountantScoreVO>>() {}.getType());
	}
	*//**
	 * 获取初级职称成绩分数
	 * @param stuIdvo 
	 * @return
	 * @throws Exception 
	 *//*
	private List<JuniorLevelAccountantScoreVO> getJuniorLevelAccountantScore(StudentIdVO stuIdvo) throws Exception {
		String result4 = HttpRequest.sendPost(NcSyncConstant.getJuniorLevelAccountantUrl(),JsonUtil.toJson(stuIdvo),"json");
		HttpServiceResult s = JsonUtil.fromJson(result4, new TypeToken<HttpServiceResult>() {}.getType());
		if(s.getData() == null){
			return new ArrayList<JuniorLevelAccountantScoreVO>();
		}
		return JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<JuniorLevelAccountantScoreVO>>() {}.getType());
	}
	*//**
	 * 会计证成绩分数
	 * @param stuIdvo 
	 * @return
	 * @throws Exception 
	 *//*
	private List<AccountingCertificateScoreVO> getAccountingCertificateScore(StudentIdVO stuIdvo) throws Exception {
		String result3 = HttpRequest.sendPost(NcSyncConstant.getAccountingCertificateUrl(),JsonUtil.toJson(stuIdvo),"json");
		HttpServiceResult s = JsonUtil.fromJson(result3, new TypeToken<HttpServiceResult>() {}.getType());
		if(s.getData() == null){
			return new ArrayList<AccountingCertificateScoreVO>();
		}
		return JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<AccountingCertificateScoreVO>>() {}.getType());
	}
	

	*//**
	 * 获取结课学员实操作业分
	 * @param endDate 
	 * @param beginDate 
	 * @return
	 * @throws Exception 
	 *//*
	private List<ClassEndingBusyworkVO>  getClassEndingBusywork(String beginDate, String endDate, String secretkey) throws Exception {
		String result2 = HttpRequest.sendPost(NcSyncConstant.getBusyworkUrl(), "beginDate="+beginDate+"&endDate="+endDate+"&secretKey="+secretkey,null);
		HttpServiceResult s = JsonUtil.fromJson(result2, new TypeToken<HttpServiceResult>() {}.getType());
		if(s.getData() == null){
			return new ArrayList<ClassEndingBusyworkVO>();
		}
		return JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<ClassEndingBusyworkVO>>() {}.getType());
	}
	*//**
	 * 获取结课学员考勤数据
	 * @param endDate 
	 * @param beginDate 
	 * @param secretkey 
	 * @return
	 * @throws Exception 
	 *//*
	private List<ClassEndingAttendanceVO> getClassEndingAttendance(String beginDate, String endDate, String secretkey) throws Exception {
		String result = HttpRequest.sendPost(NcSyncConstant.getAttendanceUrl(), "beginDate="+beginDate+"&endDate="+endDate+"&secretKey="+secretkey,null);
		HttpServiceResult s = JsonUtil.fromJson(result, new TypeToken<HttpServiceResult>() {}.getType());
		if(s.getData() == null){
			return new ArrayList<ClassEndingAttendanceVO>();
		}
		return JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<ClassEndingAttendanceVO>>() {}.getType());
	}
	
	
	*//**
	 * 根据排课计划表头主键获取排课计划数据
	 * @param arrPlanId
	 * @return
	 * @throws Exception
	 *//*
	private Map<String, String> getArrangedPlan(ArrangedPlanIdVO arrPlanId) throws Exception {
		String result2 = HttpRequest.sendPost(NcSyncConstant.getArrangedPlanUrl(), JsonUtil.toJson(arrPlanId),"json");
		HttpServiceResult s = JsonUtil.fromJson(result2, new TypeToken<HttpServiceResult>() {}.getType());
		Map<String,String> apvoMap = new HashMap<String,String>();
		if(s.getData() == null){
			return apvoMap;
		}
		List<ArrangedPlanVO> apvoList = JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<ArrangedPlanVO>>() {}.getType());
	 	for (int i = 0; i < apvoList.size(); i++) {
	 		ArrangedPlanVO apvo = apvoList.get(i);
	 		apvoMap.put(apvo.getPk_arrangedplanhid(), apvo.getSubjectilesid());
		}
		return apvoMap;

	}

	*//**
	 * 根据学员主键获取报名表信息，有可能是一个学员多张报名表
	 * key学员-班级-科目
	 * @param arrPlanId
	 * @return 
	 * @throws Exception
	 *//*
	private Map<String, Map<String, Map<String,RegistrationInfoVO>>> getRegistrationInfoByHTTP(StudentIdVO stuIdvo) throws Exception {
		String result2 = HttpRequest.sendPost(NcSyncConstant.getRegistrationInfoUrl(), JsonUtil.toJson(stuIdvo),"json");
		HttpServiceResult s = JsonUtil.fromJson(result2, new TypeToken<HttpServiceResult>() {}.getType());
		Map<String, Map<String, Map<String,RegistrationInfoVO>>> stuvoMap = new HashMap<String, Map<String, Map<String,RegistrationInfoVO>>>();
		if(s.getData() == null){
			return stuvoMap;
		}
		List<RegistrationInfoVO> voList = JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<RegistrationInfoVO>>() {}.getType());
	 	for (int i = 0; i < voList.size(); i++) {
	 		RegistrationInfoVO vo = voList.get(i);
	 		if(stuvoMap.get(vo.getStudentId()) == null){
	 			Map<String, Map<String,RegistrationInfoVO>> classmap = new HashMap<String, Map<String,RegistrationInfoVO>>();
	 			Map<String,RegistrationInfoVO> submap = new HashMap<String,RegistrationInfoVO>();
	 			submap.put(vo.getSubjectilesid(), vo);
	 			classmap.put(vo.getClassId(), submap);
	 			stuvoMap.put(vo.getStudentId(), classmap);
	 		}else{
	 			Map<String, Map<String,RegistrationInfoVO>>  classmap = stuvoMap.get(vo.getStudentId());
	 			if(classmap.get(vo.getClassId()) == null){
	 				Map<String,RegistrationInfoVO> submap = new HashMap<String,RegistrationInfoVO>();
		 			submap.put(vo.getSubjectilesid(), vo);
	 				classmap.put(vo.getClassId(), submap);
	 			}else{
	 				 Map<String,RegistrationInfoVO> submap = classmap.get(vo.getClassId());
	 				 submap.put(vo.getSubjectilesid(), vo);
	 			}
	 		}
		}
		return stuvoMap;

	}
	
	
	*//**
	 * 根据日期获取报名表信息，有可能是一个学员多张报名表
	 * key学员-班级-科目
	 * @param string 
	 * @param allIdMap 
	 * @param secretkey 
	 * @param arrPlanId
	 * @return 
	 * @throws Exception
	 *//*
	private Map<String, Map<String, Map<String,RegistrationInfoVO>>> getRegistrationInfoByDateByHTTP(String beginDate,String endDate, Map<String, Object> allIdMap, String secretkey) throws Exception {
		Map<String, Map<String, Map<String,RegistrationInfoVO>>> stuvoMap = new HashMap<String, Map<String, Map<String,RegistrationInfoVO>>>();
		try{
			String result2 = HttpRequest.sendPost(NcSyncConstant.getRegistrationInfoByDateUrl(), "beginDate="+beginDate+"&endDate="+endDate+"&secretKey="+secretkey,null);
			HttpServiceResult s = JsonUtil.fromJson(result2, new TypeToken<HttpServiceResult>() {}.getType());
			if(s.getData() == null){
				return stuvoMap;
			}
			List<RegistrationInfoVO> voList = JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<RegistrationInfoVO>>() {}.getType());
		 	for (int i = 0; i < voList.size(); i++) {
		 		RegistrationInfoVO vo = voList.get(i);
		 		allIdMap.put(vo.getStudentId(), 1);
		 		if(stuvoMap.get(vo.getStudentId()) == null){
		 			Map<String, Map<String,RegistrationInfoVO>> classmap = new HashMap<String, Map<String,RegistrationInfoVO>>();
		 			Map<String,RegistrationInfoVO> submap = new HashMap<String,RegistrationInfoVO>();
		 			submap.put(vo.getSubjectilesid(), vo);
		 			classmap.put(vo.getClassId(), submap);
		 			stuvoMap.put(vo.getStudentId(), classmap);
		 		}else{
		 			Map<String, Map<String,RegistrationInfoVO>>  classmap = stuvoMap.get(vo.getStudentId());
		 			if(classmap.get(vo.getClassId()) == null){
		 				Map<String,RegistrationInfoVO> submap = new HashMap<String,RegistrationInfoVO>();
			 			submap.put(vo.getSubjectilesid(), vo);
		 				classmap.put(vo.getClassId(), submap);
		 			}else{
		 				 Map<String,RegistrationInfoVO> submap = classmap.get(vo.getClassId());
		 				 submap.put(vo.getSubjectilesid(), vo);
		 			}
		 		}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("根据日期获取报名表信息异常："+e.getMessage());
		}
		return stuvoMap;

	}
	
	*//**
	 * 根据参数日期获得上几个月的月份
	 * @param begindate
	 * @param i
	 * @return
	 *//*
	private String getBeforeDate(Date begindate, int i) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar ca = Calendar.getInstance();
		ca.setTime(begindate);
		ca.add(Calendar.MONTH, i);//上一个月
		begindate = ca.getTime();
		String backTime = format.format(begindate);
		return backTime;
	}
	
	
	*//**
	* 字符串转换成日期
	* @param str
	* @return date
	*//*
	public static Date strToDate(String str) {
	  
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	   Date date = null;
	    try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.info("转换日期失败：："+e.getMessage());
			System.out.println("转换日期失败：："+e.getMessage());
		}
	   return date;
	}
	
	*//**
	* 日期转换成字符串
	* @param date 
	* @return str
	*//*
	public static String dateToStr(Date date) {
	  
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	   String str = format.format(date);
	   return str;*/
	} 
}
