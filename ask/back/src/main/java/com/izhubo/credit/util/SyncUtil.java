package com.izhubo.credit.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.izhubo.credit.service.CreditDelSameBillService;
import com.izhubo.credit.service.CreditForArrangedKQVService;
import com.izhubo.credit.service.CreditForNcSYNCService;
import com.izhubo.credit.service.CreditForNcScoreSYNCService;
import com.izhubo.credit.service.CreditForRecordCheckService;
import com.izhubo.credit.service.CreditForTiKuService;
import com.izhubo.credit.service.CreditService;
import com.izhubo.credit.service.CreditServiceComputeRule;
import com.izhubo.credit.service.CreditTaskLogService;
import com.izhubo.credit.service.CreditXfpercentService;
import com.izhubo.credit.service.impl.CreditDelSameBillServiceImpl;
import com.izhubo.credit.service.impl.CreditForArrangedKQVServiceImpl;
import com.izhubo.credit.service.impl.CreditForNcSYNCServiceImpl;
import com.izhubo.credit.service.impl.CreditForNcScoreSYNCServiceImpl;
import com.izhubo.credit.service.impl.CreditForRecordCheckServiceImpl;
import com.izhubo.credit.service.impl.CreditForTiKuServiceImpl;
import com.izhubo.credit.service.impl.CreditServiceComputeRuleImpl;
import com.izhubo.credit.service.impl.CreditServiceImpl;
import com.izhubo.credit.service.impl.CreditTaskLogServiceImpl;
import com.izhubo.credit.service.impl.CreditXfpercentServiceImpl;
import com.izhubo.credit.vo.ArrangedSYNCVO;
import com.izhubo.credit.vo.ClassEndStudentVO;
import com.izhubo.credit.vo.NCscoreSyncVO;
import com.izhubo.credit.vo.RegistrationSYNCVO;
import com.izhubo.credit.vo.TiKuScoreVO;
import com.mysqldb.model.CreditOperationLog;
import com.mysqldb.model.CreditPercentDetail;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordNcSore;
import com.mysqldb.model.CreditRecordSign;
import com.mysqldb.model.CreditRecordSignCVO;
import com.mysqldb.model.CreditRecordTiKuScore;
import com.mysqldb.model.CreditStandard;
import com.mysqldb.model.Creditpercent;

/**
 * 同步数据工具类</p>
 * 这个工具类专门用来同步数据的  调整用接口
 * @author lintf 
 *
 */
public class SyncUtil {
  
	/**
	 * 同步报名表信息 
	 * @param session
	 * @param creditStandardmap
	 * @param notcheckmap 不用考核的班型科目表
	 * @param tx 
	 * @param StartTime 开始日期
	 * @param EndTime 结课日期
	 * @param secretkey
	 * @param note
	 */
	public static List<String> SyncReg(Session session,
			  Map<String,CreditStandard> creditStandardmap,
			  Map<String,String> notcheckmap,
			  Transaction tx,
			  String StartTime,String EndTime,String secretkey,
			  String note){
		
		CreditOperationLog log= new CreditOperationLog();
		log.setStartTime(new Date());
		log.setTaskname("NC报名表同步");
		String Logstr=(" 从NC同步接口取数，范围为 "+StartTime+" 到  "+EndTime+"。");
		 List<String> studentidlists=null;
		 try {
	  
		           
				CreditForNcSYNCService service= new CreditForNcSYNCServiceImpl();
				 List<RegistrationSYNCVO> sourcedate =service.getRegistrationSYNCVO(StartTime ,EndTime,secretkey);
				 Map<String, CreditRecordSignCVO> Sourcecvo = new HashMap <String, CreditRecordSignCVO> ();
				 Map<String, CreditRecordSign> Sourcehvo =new HashMap<String, CreditRecordSign>();
				 Map<String, CreditRecordSignCVO> Addcvo = new HashMap <String, CreditRecordSignCVO> ();
				 Map<String, CreditRecordSign> Addhvo =new HashMap<String, CreditRecordSign>();
				 Map<String, CreditRecordSignCVO> Updatecvo = new HashMap <String, CreditRecordSignCVO> ();
				 Map<String, CreditRecordSign> Updatehvo =new HashMap<String, CreditRecordSign>();
				 List<String> hvoidist = new ArrayList<String>();
				 List<String> cvoidlist= new ArrayList<String>();
				 List<String> studentidlist= new ArrayList<String>();
				 Map<String, String> studentid = new HashMap<String, String>();
				 
				 Map<String, CreditRecordSignCVO> Localcvo = new  HashMap<String, CreditRecordSignCVO>();
				 Map<String, CreditRecordSign> Localhvo  = new  HashMap<String, CreditRecordSign>();

				 service.ComputeSourcedate  (sourcedate, Sourcecvo, Sourcehvo, hvoidist, cvoidlist, creditStandardmap,notcheckmap);
				Logstr+=( "匹配完更新源 "+DateUtil.DateToString(new Date()));
				Logstr+= ( "匹配完更新源 "+DateUtil.DateToString(new Date()));
					
					service.getLocaldateforNC(session, studentidlist, hvoidist, cvoidlist, Localcvo, Localhvo);
			
					Logstr+=( "取得学分库中的信息 "+DateUtil.DateToString(new Date()));
				 service.ProcessCVO(Sourcecvo, Localcvo, Addcvo, Updatecvo);
				 service.ProcessHVO(Sourcehvo, Localhvo, Addhvo, Updatehvo, studentid);
				 //session.clear();
				Logstr+=("本次共要新增"+Addhvo.size()+"条 HVO,"+Addcvo.size()+"条 cvo,更新"+Updatehvo.size()+"条 HVO,"+Updatecvo.size()+"条 cvo.");
				 service.PersistenceVO(session, Addhvo, Updatehvo, Addcvo, Updatecvo);
			  
			//	 tx.commit();
				 //session.clear();
				Logstr+=("同步NC定单完成于"+DateUtil.DateToString(new Date()));
				  studentidlists=new ArrayList<String>(studentid.keySet());
				 return studentidlists;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				 
						Logstr+=("NC同步接口出现问题。"+e.getMessage()+DateUtil.DateToString(new Date()));
						 return studentidlists;
					} finally {
					 
				 
						log.setEndTime(new Date());
						log.setLogid(CreditUtil.getId());
						log.setLogstr(Logstr);
						session.save(log);				
						tx.commit();
						session.clear();
						note=note+Logstr;
					 
				 
				
				}
		
		
		
		
	}
	/**
	 * 根据学员ID运算档案单
	 */
	public static void computeCreditRecordByStudentIdList(
			Session session,  Map<String,CreditStandard> creditStandardmap,
			  Transaction tx, 
			  List<String> studentidlists,
			  String note
			){
		 

		CreditOperationLog log= new CreditOperationLog();
		log.setStartTime(new Date());
		log.setTaskname("学分档案单运算");
		String Logstr="本次共要更新 "+studentidlists.size()+" 个学员的 学分档案单。";
		
		try{
	 
		List<String> templist=new ArrayList<String>();
		 int row=0;
		 templist.clear();
		 String noteinfo="";
		if (studentidlists.size()>0){
			 for (int i=0;i<studentidlists.size();i++){
				 if (studentidlists.get(i)!=null){
				 
				 templist.add(studentidlists.get(i));
				 row++;
				 if (row==500){
					checkRecord(session,templist,creditStandardmap,noteinfo);
					 row=0;
					 templist.clear(); 
				 }else if (i+1==studentidlists.size()){
					checkRecord(session,templist,creditStandardmap,noteinfo);
					 templist.clear();
				 }
				 }}
		}
		Logstr =Logstr+noteinfo+" 执行完成";
		} catch (Exception e) {
			e.printStackTrace();
			Logstr +=(" 执行错误。"+e.getMessage());
		}finally {
			 
			if (tx != null) {
				log.setEndTime(new Date());
				log.setLogid(CreditUtil.getId());
				log.setLogstr(Logstr);
				session.save(log);				
				tx.commit();
				session.clear();
				note=note+Logstr;
				 
			}
		
		}
	
		  
	}
/**
 * 根据学员运算档案单 computeCreditRecordByStudentIdList的子方法
 * @param session
 * @param templist
 * @param creditstandard
 */
	  public static void checkRecord(Session session,List<String> templist,Map<String, CreditStandard> creditstandard,String noteinfo){

			CreditForRecordCheckService t= new CreditForRecordCheckServiceImpl();
			 
				 Map<String, CreditRecord> Localmap= new HashMap<String, CreditRecord> ();
				 Map<String, CreditRecord> Addlist  =new HashMap<String, CreditRecord> ();
			  
				 Map<String, String> dvoSchme=new HashMap<String, String> ();	
				 Map<String,CreditRecordSignCVO> typemap=new HashMap<String,CreditRecordSignCVO>();
			     List<CreditRecordSignCVO>  cvolists= t.getCreditCVOByStudentidlist(session, templist);
			  
				 t.getRecordSchme(session, cvolists, dvoSchme);
				 t.getLocalRecordByStudentidlist(session, templist, cvolists,  typemap,Localmap);
			 
				 t.ComputerRecordCheck(  Addlist,  Localmap,dvoSchme,typemap, creditstandard);
				  	 
				 t.SaveRecord(session, Addlist, Localmap);
				 t.DelCreditRecordSignforNullRecord(session, templist, Addlist, Localmap);
				 
				noteinfo+=("本次取得 "+templist.size()+"个学员进行运算,共要添加"+Addlist.size()+"个档案,修改"+Localmap.size()+"个档案。");
			 
		}
	
	/**
	 * 同步NC中成绩单信息
	 * @param session
	 * @param creditStandardmap
	 * @param tx
	 * @param StartTime
	 * @param EndTime
	 * @param secretkey
	 * @param note
	 */
	public static void SyncNCscore(Session session,
			  Map<String,CreditStandard> creditStandardmap,
			  Transaction tx,
			  String StartTime,String EndTime,String secretkey,String note ){
		
		CreditOperationLog log= new CreditOperationLog();
		log.setStartTime(new Date());
		log.setTaskname("NC成绩单");
		String Logstr="";
	 		try{
			Logstr =("NC成绩单:从接口取数NC成绩单，范围为 "+ StartTime +" 到  "+ EndTime +",开始于"+DateUtil.DateToString(new Date()));  		      

		     CreditForNcScoreSYNCService service= new CreditForNcScoreSYNCServiceImpl();
			 List<NCscoreSyncVO> sourcedate =service.getNCscoreSyncVO (StartTime,EndTime,secretkey);
			 Map<String, NCscoreSyncVO> sourcemap = new  HashMap<String, NCscoreSyncVO>();
		 	
			 Map<String, CreditRecordNcSore> localmap= new HashMap<String, CreditRecordNcSore>();
			 Map<String, CreditRecordNcSore> Addvo= new HashMap<String, CreditRecordNcSore>();
			 Map<String, CreditRecordNcSore> Updatevo= new HashMap<String, CreditRecordNcSore>();
			 Logstr +=( " 匹配完更新源  开始于"+DateUtil.DateToString(new Date()));
			
			  service.ComputeSourcedate(sourcedate, sourcemap, creditStandardmap);
			  Logstr +=( " 匹配完更新源"+sourcemap.size()+" 结束于"+DateUtil.DateToString(new Date()));
		      service.getLocaldateforNCscore(session, sourcemap, localmap);
		      Logstr +=( " 取得本地学分库中的成绩单"+localmap.size()+" 结束于"+DateUtil.DateToString(new Date()));
	        service.ProcessLocalmapTosourcemap(sourcemap, localmap, Addvo, Updatevo);
			   //session.clear();
			   Logstr +=(" 本次共要新增"+Addvo.size()+"条成绩单, 更新"+Updatevo.size()+"条成绩单。开始于"+DateUtil.DateToString(new Date()));
			 String noteinfo="";
			 service.PersistenceVO(session, Addvo, Updatevo,noteinfo);
			 Logstr +=noteinfo;
		//	Logstr+=("NC成绩单:本次共要新增"+Addvo.size()+"条成绩单, 更新"+Updatevo.size()+"条成绩单。结束于"+DateUtil.DateToString(new Date()));
				
			 
			 
			 
			 
			 
	       
			 Logstr +=(" 同步NC成绩单完成于"+DateUtil.DateToString(new Date()));
			} catch (Exception e) {
				e.printStackTrace();
				Logstr +=(" 执行错误。"+e.getMessage());
			}finally {
				 
				if (tx != null) {
					log.setEndTime(new Date());
					log.setLogid(CreditUtil.getId());
					log.setLogstr(Logstr);
					session.save(log);				
					tx.commit();
					note=note+Logstr;
					//session.clear();
				}
			
			}
		
					 
			 
	  
	
	
	}
	/**
	 * 同步题库成 绩单中的作业完成率 
	 * @param session
	 * @param creditStandardmap 学分标准
	 * @param tx
	 * @param StartTime
	 * @param EndTime
	 * @param secretkey
	 * @param note
	 */
	public static void SyncTiKuscoreWork(Session session,
			  Map<String,CreditStandard> creditStandardmap,
			  Transaction tx,
			  String StartTime,String EndTime,String secretkey ,String note){
	    CreditOperationLog log= new CreditOperationLog();
		log.setStartTime(new Date());
		log.setTaskname("题库作业完成率");
		log.setLogid(CreditUtil.getId());
		String Logstr="范围从【"+StartTime+"】-【"+EndTime+"】";
		
		try {
			CreditForTiKuService service = new CreditForTiKuServiceImpl();
			/**
			 * 定义变量list: 
			 *  addTiKuScoreList ：要增加的题库成绩单
			 *  updateTiKuScoreList：要修改成绩的题库成绩单
			 *  passmap :通过的名单 如果本次通过的话 会直接修改学分档案中的指定科目修得学分
			 *  sourcedate:从题库接口处取得的成绩单 为更新源 
			 */
			
			List<CreditRecordTiKuScore> addTiKuScoreList =new ArrayList<CreditRecordTiKuScore>();
			List<CreditRecordTiKuScore> updateTiKuScoreList=new ArrayList<CreditRecordTiKuScore>();
			Map<String,String> passmap =new HashMap<String,String> (); 
			Map<String, TiKuScoreVO>  sourcedate= service.getGetStudentOfClassEndingWork(StartTime, EndTime, secretkey, creditStandardmap);
	 Logstr+=( " 取数"+sourcedate.size()+" 结束于"+DateUtil.DateToString(new Date())+"。 \n");
	 //加入处理会计基础6次课的 直接90分
	 service.getEndClassPassAct(sourcedate, creditStandardmap, StartTime, EndTime);
	 Logstr+=( " 会计基础6次课处理后 "+sourcedate.size()+" 结束于"+DateUtil.DateToString(new Date())+"。 \n");

	 if (sourcedate!=null&&sourcedate.size()>0){
		 /**
		  * localmap为根据来源pk取得的本地学分成绩单
		  */
	     Map<String, CreditRecordTiKuScore>  Localmap= service.getLocaldate(session,sourcedate);
	 Logstr+=( " 取得本地库"+Localmap.size()+", 结束于"+DateUtil.DateToString(new Date())+"。 \n");


	 service.ComputeWorkScoreRule(sourcedate, Localmap, addTiKuScoreList, updateTiKuScoreList,passmap,"W");
	 Logstr+=( " 增加"+addTiKuScoreList.size()+"个成绩单，修改"+updateTiKuScoreList.size()+"个成绩单"+Localmap.size()+", 结束于"+DateUtil.DateToString(new Date())+"。 \n");
		
	  
	 service.SaveRecord(passmap, null, session);
	 Logstr+=( "  保存"+passmap.size()+"个学员成绩通过，结束于"+DateUtil.DateToString(new Date())+"。 \n");

	 service.addTiKuScore(addTiKuScoreList, session);
	   
	 service.UpdateTiKuScore(updateTiKuScoreList, session);	
	 Logstr+=( " 增加"+addTiKuScoreList.size()+"个题库成绩单,增加"+addTiKuScoreList.size()+"个题库成绩单，结束于"+DateUtil.DateToString(new Date())+"。 \n");

    }else {
    	Logstr+=( " 取数错误取到空值了，作业完成率同步无法进行 。 结束于"+DateUtil.DateToString(new Date())+"。 \n");
    }
	 
	         log.setEndTime(new Date());		
		     log.setLogstr(Logstr);
		     session.save(log);		
	 
        } catch (Exception e) {
        	 Logstr+=("题库作业完成率:执行错误"+e.getMessage()+"。 \n");
        	 log.setEndTime(new Date());		
   		     log.setLogstr(Logstr);
   		     session.save(log);
			e.printStackTrace();
			
		}
		
		finally {
			 
			if (tx!=null){
				tx.commit();
			}
						
				
			//	note=note+Logstr;
		 
		
		}
		
		
		
	}
	/**题库中的实操考试
	 * 
	 * @param session
	 * @param creditStandardmap
	 * @param tx
	 * @param StartTime
	 * @param EndTime
	 * @param secretkey
	 * @param note
	 */
	public static void SyncTiKuscoreExam(Session session,
			  Map<String,CreditStandard> creditStandardmap,
			  Transaction tx,
			  String StartTime,String EndTime,String secretkey ,String note){
	 
		 CreditOperationLog log= new CreditOperationLog();
			log.setStartTime(new Date());
			log.setTaskname("题库实操考试");
			String Logstr="范围从【"+StartTime+"】-【"+EndTime+"】";
		
	 	 
		try {
			CreditForTiKuService service = new CreditForTiKuServiceImpl();
			List<CreditRecordTiKuScore> addTiKuScoreList =new ArrayList<CreditRecordTiKuScore>();
		
			List<CreditRecordTiKuScore> updateTiKuScoreList=new ArrayList<CreditRecordTiKuScore>();
			Map<String,String> passmap =new HashMap<String,String> (); 

			Map<String, TiKuScoreVO>  sourcedate= service.getGetStudentOfClassEndingExan(StartTime, EndTime, secretkey, creditStandardmap);
			Logstr+=( " 取数"+sourcedate.size()+" 结束于"+DateUtil.DateToString(new Date()));
		 	
		 	Map<String, CreditRecordTiKuScore>  Localmap= service.getLocaldate(session,sourcedate);
		 	Logstr+=( " 取得本地库"+Localmap.size()+", 结束于"+DateUtil.DateToString(new Date()));
		 	
		 	service.ComputeWorkScoreRule(sourcedate, Localmap, addTiKuScoreList, updateTiKuScoreList,passmap,"E");
		 
		 	service.SaveRecord(null, passmap, session);
		 	service.addTiKuScore(addTiKuScoreList, session);
		 	
		    service.UpdateTiKuScore(updateTiKuScoreList, session);	
		    Logstr+=( " 共要增加【"+addTiKuScoreList.size()+"】个成绩单，修改【"+passmap.size()+"】个成绩单，修改【"+updateTiKuScoreList.size()+"】个档案单的成绩通过。 结束于"+DateUtil.DateToString(new Date()) );
			 
		} catch (Exception e) { 	
			e.printStackTrace();
			Logstr+=("题库实操考试:执行失败。"+e.getMessage());
		}
		
		finally {
			 
		 
	 
			log.setEndTime(new Date());
			log.setLogid(CreditUtil.getId());
			log.setLogstr(Logstr);
			session.save(log);				
			
			if (tx!=null){
				tx.commit();
			}
		 
		//	note=note+Logstr;
		}
		
				
	}
	
	
	public static void SyncTiKuscoreExamFail(Session session,
			  Map<String,CreditStandard> creditStandardmap,
			  Transaction tx,
			  String StartTime,String EndTime,String secretkey ,String note){
	 
		 CreditOperationLog log= new CreditOperationLog();
			log.setStartTime(new Date());
			log.setTaskname("题库实操考试补考");
			String Logstr=" ";
		
	 	 
		try {
			CreditForTiKuService service = new CreditForTiKuServiceImpl();
			List<CreditRecordTiKuScore> addTiKuScoreList =new ArrayList<CreditRecordTiKuScore>();
			Map<String,CreditRecordTiKuScore> failStudents=new HashMap<String,CreditRecordTiKuScore>();
			List<CreditRecordTiKuScore> updateTiKuScoreList=new ArrayList<CreditRecordTiKuScore>();
			Map<String,String> examPassMasp =new HashMap<String,String> (); 

			service.getFailExamStudent(session,failStudents);
			Logstr+=( " 取数"+failStudents.size()+" 没有考过的学员，结束于"+DateUtil.DateToString(new Date()));
		 	
			service.getGetStudentOfFailExam(null, null, secretkey, creditStandardmap, failStudents, updateTiKuScoreList, examPassMasp);
			Logstr+=( "从题库取完学员成绩单，结束于"+DateUtil.DateToString(new Date()));
			 
		  	 	 
		 	service.SaveRecord(null, examPassMasp, session);
		 	 
		 	
		    service.UpdateTiKuScore(updateTiKuScoreList, session);	
		    Logstr+=( " 共要 修改【"+updateTiKuScoreList.size()+"】个成绩单，修改【"+examPassMasp.size()+"】个档案单的成绩通过。 结束于"+DateUtil.DateToString(new Date()) );
			 
		} catch (Exception e) { 	
			e.printStackTrace();
			Logstr+=("题库实操考试:执行失败。"+e.getMessage());
		}
		
		finally {
			 
		 
	 
			log.setEndTime(new Date());
			log.setLogid(CreditUtil.getId());
			log.setLogstr(Logstr);
			session.save(log);				
			if (tx!=null){
				tx.commit();
			}
		
			note=note+Logstr;
		}
		
				
	}
	
	
	
	
	
	
	
	/**
	 * 同步考勤有变化的学员的班级和老师
	 * @param session
	 * @param creditStandardmap
	 * @param tx
	 * @param StartTime
	 * @param EndTime
	 * @param secretkey
	 * @param note
	 */
	public static void SyncKQrate(Session session,
			  Map<String,CreditStandard> creditStandardmap,
			  Transaction tx,
			  String StartTime,String EndTime,String secretkey ,String note){
		 
		CreditOperationLog tasklog= new CreditOperationLog();
		tasklog.setTaskname("NC考勤单");
		tasklog.setStartTime( new Date() );
		String Logstr="范围从【"+StartTime+"】-【"+EndTime+"】";
		try{
		Map<String,String> student_list= new HashMap<String,String>();	
		Map<String, CreditRecord> localmap= new HashMap<String, CreditRecord>();
		Map<String, CreditRecord> updatemap= new HashMap<String, CreditRecord>();
		CreditForArrangedKQVService service= new CreditForArrangedKQVServiceImpl();
		Logstr+=( "NC考勤单:开始从接口取数 范围:【"+StartTime+"】-【"+EndTime+"】。　开始于"+DateUtil.DateToString(new Date()));
		List<ArrangedSYNCVO> sourcedate=service.getArrangedSYNCVO(StartTime, EndTime, secretkey);
		List<ArrangedSYNCVO> sourcedate_pass=service.getArrangedSYNCVOOld(StartTime, EndTime, secretkey);
		Logstr+=( "NC考勤单:从接口取得数据　"+sourcedate.size()+"　个学员。旧学员 "+sourcedate_pass.size()+"个。　结束于"+DateUtil.DateToString(new Date()));

		Map<String, ArrangedSYNCVO> sourcemap =service.ComputeArrangedSYNCVO(sourcedate,  creditStandardmap,student_list);
		Map<String, ArrangedSYNCVO> sourcemap_pass =service.ComputeArrangedSYNCVO( sourcedate_pass, creditStandardmap,student_list);
		
		 //两个map都有数据时才需要遍历去重，
		  if (sourcemap!=null&&sourcemap.size()>0&&sourcemap_pass!=null&&sourcemap_pass.size()>0){

			   Iterator<Map.Entry<String, ArrangedSYNCVO>> it = sourcemap_pass.entrySet().iterator();  
		        while(it.hasNext()){  
		            Map.Entry<String, ArrangedSYNCVO> entry=it.next();  
		            
		            ArrangedSYNCVO arr= sourcemap.get(entry.getKey());
		            if (entry.getKey()!=null&&arr!=null&&arr.getStudentId()!=null){
		            	 ArrangedSYNCVO pass=entry.getValue();
		            	 if (arr.getKqV()!=null&&arr.getKqV()>pass.getKqV()){
		            	 
		            		 it.remove();
		            	 }else{
		            		 sourcemap.remove(entry.getKey());
		            	 }
		            }
		          
		        }   
		  }
		    
		
		Logstr+=( "NC考勤单:组合完来源数据共　"+(sourcemap.size()+sourcemap_pass.size())+"　条要更新。　结束于"+DateUtil.DateToString(new Date()));

		
		localmap=service.getLocalRecordMap(session, student_list);
		Logstr+=( "NC考勤单:取得本地共　"+localmap.size()+"　条数据。　结束于"+DateUtil.DateToString(new Date()));

		service.ComputeRecord(sourcemap, sourcemap_pass,localmap, updatemap);
		Logstr+=( "NC考勤单:共要更新　"+updatemap.size()+"　个档案。　开始于"+DateUtil.DateToString(new Date()));
		service.SaveRecord(session, updatemap);
		Logstr+=( "NC考勤单:共要更新　"+updatemap.size()+"　个档案。　结束于"+DateUtil.DateToString(new Date()));
 
		
		}catch (Exception e){
			e.printStackTrace();
			Logstr+="NC考勤单:执行错误."+e.getMessage();
		
		}
		finally {
			 
			if (tx != null) {
				Logstr+=( "NC考勤单:执行完成。　结束于"+DateUtil.DateToString(new Date()));
				tasklog.setEndTime(new Date());
				tasklog.setLogstr(Logstr);
				tasklog.setLogid(CreditUtil.getId());
				session.save(tasklog);
				tx.commit();
				note=note+Logstr;
			//	System.out.println(note);
				
			}
		
		}
	}
	
	 
	
	
	
	
	
	public static void SyncCreditRecordRule(Session session,
			  Map<String,CreditStandard> creditStandardmap,
			  Transaction tx,
			  String StartTime,String EndTime,String secretkey ,String note){
		 CreditOperationLog log= new CreditOperationLog();
			log.setStartTime(new Date());
			log.setTaskname("学分总运算");
			String Logstr="范围从【"+StartTime+"】-【"+EndTime+"】";
		try{
		CreditServiceComputeRule service= new  CreditServiceComputeRuleImpl();
		
		 Map<String, CreditRecord> localmap=new HashMap<String, CreditRecord>();
		 
		 Map<String, ClassEndStudentVO>   sourcemap=service.getClassEndStudentlist(StartTime, EndTime, secretkey, creditStandardmap);
		 Logstr+=( "  取得完课学员"+sourcemap.size()+"人。　结束于"+DateUtil.DateToString(new Date()));

		 service.ComputeAttendanceScore(session, sourcemap, localmap);
		 Logstr+=( " 运算考勤分数: 运算完成，共要修改"+localmap.size()+"人。　结束于"+DateUtil.DateToString(new Date()));
		  service.ComputeScore_tiku(session, sourcemap, localmap);
		  Logstr+=( " 运算题库成绩单: 运算完成，共要修改"+localmap.size()+"人。　结束于"+DateUtil.DateToString(new Date()));
		  service.ComputeScore_NC(session, sourcemap, localmap) ;
		  Logstr+=( " 运算NC成绩单: 运算完成，共要修改"+localmap.size()+"人。　结束于"+DateUtil.DateToString(new Date()));

		 service.SaveCreditRecord(session, localmap);
		}catch (Exception e){
			
			e.printStackTrace();
			Logstr+=(" 执行错误："+e.getMessage());
		}
		finally {
			 
			if (tx != null) {
				Logstr+=( " 执行完成。" );
				log.setEndTime(new Date());
				log.setLogstr(Logstr);
				log.setLogid(CreditUtil.getId());
				session.save(log);
				tx.commit();
				note=note+Logstr;
			 
			 
			}
		
		}
	}
	/**
	 * 根据题库月份取得题库中的作业完成率和结业作业 
	 * @param session
	 * @param creditStandardmap
	 * @param tx
	 * @param StartTime
	 * @param EndTime
	 * @param secretkey
	 * @param note
	 */
	public static void SyncCreditTikuMonth(Session session,
			  Map<String,CreditStandard> creditStandardmap,
			   
			  String  tikuMonths,String secretkey ,String note){
		try{
			tikuMonths=tikuMonths+"-01 00:00:00";
			Map<String, String> tikudata=DateUtil.GetTikuMonths(DateUtil.StringToDate(tikuMonths));
		 
						
			String Startdate= tikudata.get("StartTime"); //取得开始日期
			 String Enddate = tikudata.get("EndTime");//取得结束日期
			
			 	String StartTime="";
			 	String EndTime="";
		 	    Date s_date= DateUtil.StringToDate(Startdate);//循环开始时间
			    Date e_date=  DateUtil.getNextDay(s_date, 10);//结束时间 为开始加10天
			    Date FE_date=  DateUtil.StringToDate(Enddate); //循环结束时间
			    //当结束日期大于今天时 以今天为最后时间
			    if (FE_date.after(new Date())){
			    	FE_date= new Date();
			    }
			    
		 			 for (int i=0;i<20;i++){
				  if (e_date.after(FE_date)){ //当超过最大时间时
					  StartTime= DateUtil.DateToString(s_date).substring(0,10);

					  EndTime=DateUtil.DateToString(FE_date).substring(0,10);
					 
					   
					  SyncCreditTikuDay(session,creditStandardmap,StartTime,EndTime,secretkey,note);
					   break;
				  }else {
					  StartTime= DateUtil.DateToString(s_date).substring(0,10);

					  EndTime=DateUtil.DateToString(e_date).substring(0,10);
					 
					   
					  SyncCreditTikuDay(session,creditStandardmap,StartTime,	EndTime,secretkey,note);
					 s_date=e_date;
					 e_date=DateUtil.getNextDay(s_date, 10);
				  }
				
				 
				
				 
			 }
			
		}catch (Exception e){
			
			e.printStackTrace();
		}
		finally {
			 
			 
				 
			//	Logstr+=( "学分档案运算考勤分数:修改完成。　结束于"+DateUtil.DateToString(new Date()));

			 
		
		}
	}
	
	/**
	 * 以天为单位取得题库中作业完成率和结课考试成绩保存到学分并写到学分档案
	 * @param session
	 * @param creditStandardmap
	 * @param StartTime
	 * @param EndTime
	 * @param secretkey
	 * @param note
	 */
	public static void SyncCreditTikuDay (Session session, Map<String,CreditStandard> creditStandardmap,String StartTime,
			  String EndTime,String secretkey,String note
			  ){
	 //题库成绩单
	 Transaction txtikuwork = session.beginTransaction();
	  SyncTiKuscoreWork(session, creditStandardmap, txtikuwork, StartTime, EndTime, secretkey, note);
	 //题库考试单
	 Transaction txtikuexam = session.beginTransaction();	
	  SyncTiKuscoreExam(session, creditStandardmap, txtikuexam, StartTime, EndTime, secretkey, note);
	
	}
	
	
	
	
	public static void SyncXfPercentreReport(Session session, 
			  Transaction tx,String dbilldate,
			  String s_months,String e_months, String pid,String note,CreditOperationLog log) throws Exception{
		
		 CreditXfpercentService service = new CreditXfpercentServiceImpl();
		 
		  Map<String,String> MonthList= new HashMap<String,String>(); 
		  List<CreditPercentDetail> dvos= new ArrayList<CreditPercentDetail>();	 
	
		  //取得需要运算的月份
		   service.getMonthList(MonthList,session, s_months, e_months, dbilldate,note); 
		 //根据月份取得全部的学员明细列表
		   System.out.println("月份大小"+MonthList.size());
		   service.getPerentDetailByMonthList(dvos,session, dbilldate, MonthList, pid,note);
		   //取得全部的报表数据
			  System.out.println( "dvo大小"+dvos.size());
		  List<Creditpercent> pvos=service.CreateCreditPerentReport(session, dvos,dbilldate,note);
		  System.out.println("pvo大小"+pvos.size());
		  String noteinfo= service.SaveAllData(session,tx, dvos, pvos, dbilldate,pid,MonthList,note, log);
		  
		
		
	       }
	
	public static void SyncDelSameRecordBill(Session session,Transaction tx){
		
		List<CreditRecord> Delvo= new ArrayList<CreditRecord>();
		List<CreditRecord> UpdateVo= new ArrayList<CreditRecord>();
		CreditDelSameBillService service= new CreditDelSameBillServiceImpl();
		List<CreditRecord> sourcedate= service.getSameRecordByCID(session);
		service.ComputeRecordByCid(sourcedate, Delvo, UpdateVo);
		service.delSameRecordBill(session, tx, Delvo, UpdateVo);
	}
	/**
	 * 删除重复的CVO
	 * @param session
	 * @param tx
	 */
public static void SyncDelSameSignCvoBill(Session session,Transaction tx){
		
	CreditDelSameBillService service= new CreditDelSameBillServiceImpl();
	service.delSameSignCvoByCid(session, tx); 
	}
	
public static void SyncDelSameTikuRecordBill(Session session,Transaction tx){
		
 
		CreditDelSameBillService service= new CreditDelSameBillServiceImpl();
		service.delSameTikuScoreByBillkey(session, tx);
	}
	
	

public static void SyncCheckNcData(Session session,Transaction tx){
	String beginDate =null;
	String endDate=null;

	CreditDelSameBillService service= new CreditDelSameBillServiceImpl();
	
	 Date startday=DateUtil.StringToDate("2017-01-01 00:00:00");
		Date endday=new Date();
		 Map<String, String> mon=DateUtil.getMonthList(startday, endday);
	 
			//service.checkNCdate(session, tx, beginDate, endDate);
		for (Entry<String, String> s:mon.entrySet()){
			
			Date date=DateUtil.StringToDate(s.getKey()+"-01 00:00:00");
			beginDate =DateUtil.getMonthFirstDay(date).substring(0,10);
			endDate=DateUtil.getMonthLastDay(date).substring(0,10);
			//creditDataExportService.CheckNcDateByRegdate(beginDate, endDate);
		 	service.checkNCdate(session, tx, beginDate, endDate);
		}
	
	tx.commit();
	
	//service.checkNCdate(session, tx, beginDate, endDate);
}
	
 
	 
public static void CheckHomeWorkCompletion(Session session,Transaction tx,String sDate,String eDate) throws Exception{
	
 
		 
		// Date  StartTime= DateUtil.StringToDate(sDate+" 00:00:00");
		// Date  EndTime= DateUtil.StringToDate(eDate+" 23:59:59"); 
	
	
    CreditOperationLog log= new CreditOperationLog();
		log.setStartTime(new Date());
		log.setTaskname("旧数据题库成绩");
		log.setLogid(CreditUtil.getId());
		String Logstr="范围从【"+sDate+"】-【"+eDate+"】";
		try{
		 Criteria q = session.createCriteria(CreditRecord.class);                    
		  q.add(Restrictions.eq("isObsoleted", 0));
		  q.add(Restrictions.eq("isPass", 1));
		 
		  q.add( Restrictions.sqlRestriction(" substr(create_date,1,10) between '"+sDate+"' and '"+eDate+"'  "));
		 //q.add(Restrictions.between("createDate", StartTime, EndTime) );
	/*	 q.add(Restrictions.ge("ts", StartTime));
	      q.add(Restrictions.le("ts", EndTime));*/
		 
	  	List<CreditRecord> root=q.list();  
	  	 Logstr+=( " 取数"+root.size()+" 结束于"+DateUtil.DateToString(new Date())+"。 \n");

	 
	     CreditForTiKuService service = new CreditForTiKuServiceImpl();
	     service.getHomeWorkCompletionByRecord(root, null, session);
	     Logstr+=( " 运算完成， 结束于"+DateUtil.DateToString(new Date())+"。 \n");
} catch (Exception e) {
	 Logstr+=("题库作业完成率:执行错误"+e.getMessage()+"。 \n");
	 log.setEndTime(new Date());		
      log.setLogstr(Logstr);
      session.save(log);
	e.printStackTrace();
	
}
		finally{
			if (tx!=null){
				  tx.commit();
			}
		}
	   
	 
	 
	 }
	
	
	
	
}
 



