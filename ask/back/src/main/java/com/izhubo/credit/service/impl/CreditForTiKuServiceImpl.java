package com.izhubo.credit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.metamodel.MetadataSources;
import org.hibernate.service.ServiceRegistry;

import com.google.gson.reflect.TypeToken; 
import com.izhubo.credit.service.CreditForTiKuService;
import com.izhubo.credit.util.CreditDaoUtil;
import com.izhubo.credit.util.CreditUtil;
import com.izhubo.credit.util.DateUtil;
import com.izhubo.credit.util.HttpRequest;
import com.izhubo.credit.util.JsonUtil;
import com.izhubo.credit.util.NcSyncConstant;
import com.izhubo.credit.util.doubleUtilBase;
import com.izhubo.credit.vo.ArrangedPlanIdVO;
import com.izhubo.credit.vo.ArrangedPlanVO;
import com.izhubo.credit.vo.ClassEndStudentVO;
import com.izhubo.credit.vo.ClassEndingAttendanceVO;
import com.izhubo.credit.vo.HttpServiceResult;
import com.izhubo.credit.vo.RegistrationInfoVO;
import com.izhubo.credit.vo.TiKuScoreVO; 
import com.izhubo.credit.webservice.WebService1Stub;
import com.izhubo.credit.webservice.WebService1Stub.GetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode;
import com.izhubo.credit.webservice.WebService1Stub.GetStudentOfClassEndingExamComplianceRateByTime;
import com.izhubo.credit.webservice.WebService1Stub.GetStudentOfClassEndingTaskComplianceRateByTime;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordTiKuScore;
import com.mysqldb.model.CreditStandard; 
 

public class CreditForTiKuServiceImpl implements CreditForTiKuService {
	 
	@Override
	public  Map<String,TiKuScoreVO> getGetStudentOfClassEndingWork(String beginDate, String endDate, String secretkey,Map<String, CreditStandard> creditStandardmap) throws Exception {
 		 Map<String,TiKuScoreVO> tikumap= new HashMap<String,TiKuScoreVO>();
		WebService1Stub.GetStudentOfClassEndingTaskComplianceRateByTime task = new GetStudentOfClassEndingTaskComplianceRateByTime();  
	 	task.setBeginDate(beginDate);
	 	task.setEndDate(endDate);
	 	WebService1Stub taskstub = new WebService1Stub();
	 	 Options options = taskstub._getServiceClient().getOptions();
	     options.setProperty(HTTPConstants.SO_TIMEOUT, 600000);//10分钟
	     taskstub._getServiceClient().setOptions(options);
	 	String taskstub_s = taskstub.getStudentOfClassEndingTaskComplianceRateByTime(task).getGetStudentOfClassEndingTaskComplianceRateByTimeResult();  
	 	if(taskstub_s == null){
	 		return null;
	 	}
	 	   
	 	List<TiKuScoreVO>  tvos=JsonUtil.fromJson(taskstub_s,new TypeToken<List<TiKuScoreVO>>(){}.getType());
	 	 
	 	getSubjectTypebyNC(tvos, creditStandardmap);
	 	
	 	
	 	
	 	for (int i=0;i<tvos.size();i++){
	 		//当来源的排课是空的则直接跳过 
	 		 if (tvos.get(i).getClassNCCode()==null||tvos.get(i).getClassNCCode().length()<10){
	 			 continue;
	 		 }
	 		tvos.get(i).setBillType("W");
	 		tvos.get(i).setBillKey(tvos.get(i).getStudentNCCode()+"_"+tvos.get(i).getSubjectType()+"_"+tvos.get(i).getBillType());
	 		TiKuScoreVO submap= tikumap.get(tvos.get(i).getBillKey());
	 		/**进入加入判断**/
			if (submap!=null){/**当本地的列表中有这个学员科目时 判断谁分数大*/
				
				if (tvos.get(i).getStandarRate()>submap.getStandarRate()){
					tikumap.put(tvos.get(i).getBillKey(), tvos.get(i));
				}
				
				
				
			}else {/**列表中没有它的直接加到列表*/
				tikumap.put(tvos.get(i).getBillKey(), tvos.get(i));
			}
			/**结束判断**/
	 	}
	 	
	 
	 return tikumap; 
	 	
		 
	}

	@Override
	public Map<String,TiKuScoreVO>  getGetStudentOfClassEndingExan(String beginDate, String endDate, String secretkey,Map<String, CreditStandard> creditStandardmap)   throws Exception{
 
		 Map<String,TiKuScoreVO> tikumap= new HashMap<String,TiKuScoreVO>();
			WebService1Stub.GetStudentOfClassEndingExamComplianceRateByTime task = new GetStudentOfClassEndingExamComplianceRateByTime();  
		 	task.setBeginDate(beginDate);
		 	task.setEndDate(endDate);
		 	WebService1Stub taskstub = new WebService1Stub();
		 	 Options options = taskstub._getServiceClient().getOptions();
		     options.setProperty(HTTPConstants.SO_TIMEOUT, 600000);//10分钟
		     taskstub._getServiceClient().setOptions(options);
		 	String taskstub_s = taskstub.getStudentOfClassEndingExamComplianceRateByTime(task).getGetStudentOfClassEndingExamComplianceRateByTimeResult();  
		 	if(taskstub_s == null){
		 		return null;
		 	}
		 	   
		 	List<TiKuScoreVO>  tvos=JsonUtil.fromJson(taskstub_s,new TypeToken<List<TiKuScoreVO>>(){}.getType());
		 	
		 	getSubjectTypebyNC(tvos, creditStandardmap);
		 	
		 	
		 	
		 	for (int i=0;i<tvos.size();i++){
		 		//当来源的排课是空的则直接跳过 
		 		 if (tvos.get(i).getClassNCCode()==null||tvos.get(i).getClassNCCode().length()<10){
		 			 continue;
		 		 }
		 		tvos.get(i).setBillType("E");
		 		tvos.get(i).setBillKey(tvos.get(i).getStudentNCCode()+"_"+tvos.get(i).getSubjectType()+"_"+tvos.get(i).getBillType());
		 		TiKuScoreVO submap= tikumap.get(tvos.get(i).getBillKey());
		 		/**进入加入判断**/
				if (submap!=null){/**当本地的列表中有这个学员科目时 判断谁分数大*/
					
					if (tvos.get(i).getStandarRate()>submap.getStandarRate()){
						tikumap.put(tvos.get(i).getBillKey(), tvos.get(i));
					}
					
					
					
				}else {/**列表中没有它的直接加到列表*/
					tikumap.put(tvos.get(i).getBillKey(), tvos.get(i));
				}
				/**结束判断**/
		 	}
		 	
		 
		 return tikumap; 
	}

	 
	@Override
	public void ComputeWorkScoreRule(Map<String,TiKuScoreVO> Sourcedate,
			Map<String,CreditRecordTiKuScore> localdate,
			List<CreditRecordTiKuScore> addTiKuScoreList,
			List<CreditRecordTiKuScore> updateTiKuScoreList,
			Map<String,String> passmap,String type
			) throws Exception {
		int score=0;
		if ("E".equals(type)){
			score=60;
		}else if ("W".equals(type)){
			score=90;
		}
		 	  
		for (Entry<String, TiKuScoreVO> entry : Sourcedate.entrySet()) {
			CreditRecordTiKuScore tvo= localdate.get(entry.getKey());//从本地列表中取题库的数据
			 if (tvo!=null){
				 if (CreditUtil.Double_half_up(tvo.getStandarRate())<CreditUtil.Double_half_up( entry.getValue().getStandarRate())){ //当本地的比远程来的过来小的 进入修改列表
	 						tvo.setStandarRate(CreditUtil.Double_half_up(entry.getValue().getStandarRate()));
	 						tvo.setLastTime(new Date());
	 					 	tvo.setExanTime(entry.getValue().getExanTime());
	 					 	tvo.setCreateTime(entry.getValue().getCreateTime());
	 					 	
					         updateTiKuScoreList.add(tvo);
					 //进入是否考试通过判断
					 if (score!=0){
						 if (score==60){
							 if (CreditUtil.Double_half_up(tvo.getStandarRate())>=60){
								 passmap.put(tvo.getSubjectType()+tvo.getStudentNCCode(), "E");
							 }
							 
						 }else if (score==90){
							 if (CreditUtil.Double_half_up(tvo.getStandarRate())>=90){
								 passmap.put(tvo.getSubjectType()+tvo.getStudentNCCode(), "W");
							 }
							  
						 }
					 }
					 
					 
				 }
				 
			 }else {//当学分库中没有这个数据时 添加到本次的增加列表中
				 CreditRecordTiKuScore tvos= new CreditRecordTiKuScore(entry.getValue());
				 tvos.setLastTime(new Date());
				 addTiKuScoreList.add(tvos);
				 if (score!=0){
					 if (score==60&&tvos.getStandarRate()!=null){
						 if (CreditUtil.Double_half_up(tvos.getStandarRate())>=60){
							 passmap.put(tvos.getSubjectType()+tvos.getStudentNCCode(), "E");
						 }
						 
					 }else if (score==90&&tvos.getStandarRate()!=null){
						 if (CreditUtil.Double_half_up(tvos.getStandarRate())>=90){
							 passmap.put(tvos.getSubjectType()+tvos.getStudentNCCode(), "W");
						 }
						  
					 }
				 }
			 }
		}
		 
	    
	}

	@Override
	public  Map<String, CreditRecordTiKuScore> getLocaldate(Session session,Map<String,TiKuScoreVO> sourcedate)
			throws Exception {
	 List<String> billkeylist = new ArrayList<String>(); //用来顾查出来的全部学员的billkey_subject_billtype
	 
	  Map<String,CreditRecordTiKuScore> localmap= new HashMap<String,CreditRecordTiKuScore>();

		for (Entry<String, TiKuScoreVO> entry : sourcedate.entrySet()) {
			billkeylist.add(entry.getKey());
		}
	  
	         
		 
	List<CreditRecordTiKuScore> locallist=  CreditDaoUtil.getRecordTiKuScoreByBillkey(session, billkeylist, 10000);
				
			
		//	session.createQuery("from CreditRecordTiKuScore where  BillKey in (:alist) ").setParameterList("alist",billkeylist).list();;
		
	/**
	 * 将本地查出来的结果转成map，如果有两个相同的学员的信息的 取最大值
	 */
	if (locallist!=null&&locallist.size()>0){
			for (CreditRecordTiKuScore tvo :locallist){
				if(tvo.getBillKey()!=null){
					CreditRecordTiKuScore submap= localmap.get(tvo.getBillKey());
					/**进入加入判断**/
					if (submap!=null){/**当本地的列表中有这个学员科目时 判断谁分数大*/
						
						if (CreditUtil.Double_half_up(tvo.getStandarRate())>CreditUtil.Double_half_up(submap.getStandarRate())){
							
										
							localmap.put(tvo.getBillKey(), tvo);
						}
						
						
						
					}else {/**列表中没有它的直接加到列表*/
						localmap.put(tvo.getBillKey(), tvo);
					}
					/**结束判断**/
				}
			}
		}
		
		return localmap ; 
	}

	@Override
	public List<TiKuScoreVO> getTiKuScoreByBillKeyList(List<String>  billkeylist,
			boolean QueryAll) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public  void addTiKuScore(List<CreditRecordTiKuScore> addTiKuScoreList,Session session) {
	     
		
		for (int i=0;i<addTiKuScoreList.size();i++){
			CreditRecordTiKuScore vo=addTiKuScoreList.get(i);
			session.save(vo);
		
		} 
		 
	}

	@Override
	public  void UpdateTiKuScore(List<CreditRecordTiKuScore> updateTiKuScoreList ,Session session) {
	     
			 for (int i=0;i<updateTiKuScoreList.size();i++){
			if (updateTiKuScoreList.get(i).getBillKey()!=null){
				CreditRecordTiKuScore vo=updateTiKuScoreList.get(i);
				session.update(vo);
			}
			
		}
	}

	@Override
	public void getSubjectTypebyNC( 
			List<TiKuScoreVO> tikuvo,
			Map<String, CreditStandard> creditStandardmap) throws Exception {
		Map<String,String> keymap= new HashMap<String,String>();
	 for (int i=0;i<tikuvo.size();i++){
		 TiKuScoreVO t =tikuvo.get(i);
		//去取NC数据的 去掉空排课的学员
		 if (t!=null&&t.getClassNCCode()!=null&&t.getClassNCCode().length()>10){
			 keymap.put(t.getClassNCCode(), t.getClassNCCode());
		 }	 
		 }
	 
	  if (keymap!=null&&keymap.size()>0){
	 List<String> arrpklist= new ArrayList<String>(keymap.keySet());
	 
	 ArrangedPlanIdVO  arrPlanId = new ArrangedPlanIdVO();
 	arrPlanId.setArrPlanIdList(arrpklist);
 	arrPlanId.setSecretKey(NcSyncConstant.getNcSecretkey());
 	String result2 = HttpRequest.sendPost( NcSyncConstant.getArrangedPlanUrl(), JsonUtil.toJson(arrPlanId),"json");
	HttpServiceResult s = JsonUtil.fromJson(result2, new TypeToken<HttpServiceResult>() {}.getType());
    List<ArrangedPlanVO> arrlist = JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<ArrangedPlanVO>>() {}.getType());
	
	
	
	
	
	Map<String,ArrangedPlanVO> arrmap= new HashMap<String,ArrangedPlanVO>();
	/**遍历从接口得来的排课计划单，取得科目ID并与题库中的学分档案中取得科目类型**/
		for (int a=0;a<arrlist.size();a++){
		ArrangedPlanVO arrvo=arrlist.get(a);
		if (arrvo.getPk_arrangedplanhid()!=null&&arrvo.getSubjectilesid()!=null){
			CreditStandard d=creditStandardmap.get(arrvo.getSubjectilesid());
			String subjecttype=null;
			if (d!=null){
				subjecttype=d.getSubject_type()==null?d.getNc_id():d.getSubject_type();
			}else{
				continue;
			}
			arrvo.setSubjecttype(subjecttype);
			arrmap.put(arrvo.getPk_arrangedplanhid(), arrvo);
		}
		}
	/**
	 * 再次遍历tikuvo 从arrmap中取值
	 */
		for (int i=0;i<tikuvo.size();i++){
			 TiKuScoreVO t =tikuvo.get(i);
			 
			 if (t!=null&&t.getClassNCCode()!=null&&t.getClassNCCode().length()>10){

				 ArrangedPlanVO tempa =arrmap.get(t.getClassNCCode());
				 if (tempa!=null){
					 tikuvo.get(i).setArrSubject(tempa.getSubjectilesid());
					 tikuvo.get(i).setSubjectType(tempa.getSubjecttype());
			 	       Date exantime= DateUtil.StringToDateByTiku(tikuvo.get(i).getCreateTime())==null?new Date():DateUtil.StringToDateByTiku(tikuvo.get(i).getCreateTime());
			 	    tikuvo.get(i).setExanTime(exantime); 
					 
					 
					 
					 
					 
				 }
				 
				 
			 }		
			 }
		
		
		
	  }
	}
/**
 * 题库单通过的 修改学分档案中的分数
 */
	@Override
	public void SaveRecord(Map<String,String> workPassMap,Map<String,String> examPassMasp,
			Session session) {
		Map<String,CreditRecord> r= new HashMap<String,CreditRecord>();
		 /**
		  * 先遍历作业完成率的 从学分档案 中取得列表
		  */
		if (workPassMap!=null&&workPassMap.size()>0){
			List<String> idkeys=new ArrayList<String>(workPassMap.keySet());
			// System.out.println( "题库作业完成率: 开始要运算"+idkeys.size()+"个学员成绩通过， 开始于"+DateUtil.DateToString(new Date()));

			List<CreditRecord> rvolist=CreditDaoUtil.getLocalDateRecordBySubtypeUserid(session, idkeys, 500, 3);
			for (CreditRecord d:rvolist){  //取到的数据后赋值作业完成
				if (d!=null&&d.getWorkActualScore()!=d.getWorkClaimScore()){
					// System.out.println("当前学员 "+d.getNcUserId()+"分数为 "+d.getWorkActualScore()+"/"+d.getAttendanceActualScore()+"/"+d.getExamActualScore());
					d.setWorkActualScore(d.getWorkClaimScore());
					// System.out.println("作业完成后 "+d.getNcUserId()+"分数为 "+d.getWorkActualScore()+"/"+d.getAttendanceActualScore()+"/"+d.getExamActualScore());
						
					CreditUtil.CheckCreditRecordPass(d);
					r.put(d.getSubjectType()+d.getStudentId(), d);
				}
			}
		}
		
		if (examPassMasp!=null&&examPassMasp.size()>0){
			List<String> idkeys=new ArrayList<String>(examPassMasp.keySet());
			List<CreditRecord> rvolist=CreditDaoUtil.getLocalDateRecordBySubtypeUserid(session, idkeys, 500, 3);
			for (CreditRecord d:rvolist){  //取到的数据后赋值考试完成
				if (d!=null&&d.getExamActualScore()!=d.getExamClaimScore()){
					d.setExamActualScore(d.getExamClaimScore());
					CreditUtil.CheckCreditRecordPass(d);
					CreditRecord sub =r.get(d.getSubjectType()+d.getStudentId());
					if (sub!=null){ //如果相同中的话有可通是这个学员又完成了作业 又完成了考试
						// System.out.println("当前学员 "+d.getNcUserId()+"分数为 "+d.getWorkActualScore()+"/"+d.getAttendanceActualScore()+"/"+d.getExamActualScore());
							
						sub.setExamActualScore(sub.getExamClaimScore());
						// System.out.println(" 考试完成后 "+d.getNcUserId()+"分数为 "+d.getWorkActualScore()+"/"+d.getAttendanceActualScore()+"/"+d.getExamActualScore());
							
						CreditUtil.CheckCreditRecordPass(sub);
						r.put(sub.getSubjectType()+sub.getStudentId(), sub);
						
					}else {
						r.put(d.getSubjectType()+d.getStudentId(), d);
					}
					
					
				}
			}
		}
		
		for (Entry<String,CreditRecord> entry:r.entrySet()){
			
			if (entry!=null&&entry.getValue().getId()!=null){
				CreditRecord d =entry.getValue();
				session.update(d);
			}
			
		}
		
		
		
		
	}

@Override
public void getEndClassPassAct(Map<String,TiKuScoreVO> tikuMap,
		Map<String, CreditStandard> creditStandardmap ,String beginDate,String endDate) throws Exception {

	 
   String result2  = HttpRequest.sendPost(NcSyncConstant.getClassEndStudentActUrl(), "syncSDate="+beginDate+"&syncEDate="+endDate+"&secretKey="+NcSyncConstant.getNcSecretkey(),null);

 
 HttpServiceResult s = JsonUtil.fromJson(result2, new TypeToken<HttpServiceResult>() {}.getType());
 	 
List<ClassEndStudentVO> arrlist = JsonUtil.fromJson(JsonUtil.toJson(s.getData()), new TypeToken<List<ClassEndStudentVO>>() {}.getType());

if (arrlist==null||arrlist.size()==0){
	return ;
}

Map<String,TiKuScoreVO> ActMap= new HashMap<String,TiKuScoreVO>();//商机ID_科目类型_W
 



		for (int a = 0; a < arrlist.size(); a++) {
			ClassEndStudentVO arrvo = arrlist.get(a);
			if (arrvo.getStudentId() != null && arrvo.getSubjectId() != null) {
				TiKuScoreVO t = new TiKuScoreVO();
				CreditStandard d = creditStandardmap.get(arrvo.getSubjectId());
				String subjecttype = null;
				if (d != null&&"会计基础".equals( d.getSubject_type())) {
					
					subjecttype = d.getSubject_type() == null ? d.getNc_id()
							: d.getSubject_type();
					t.setBillKey(arrvo.getStudentId() + "_" + subjecttype
							+ "_W");
					t.setClassNCCode(arrvo.getArrId());
					t.setStudentNCCode(arrvo.getStudentId());
					t.setArrSubject(d.getSubject_name());
					t.setCourseCode(d.getCourse_code());
					t.setStandarRate(90.0);
					t.setStudentName("会计基础6节课的");
					
					ActMap.put(arrvo.getStudentId() + "_" + subjecttype+ "_W", t);
				}

			}
		}
		
		if (ActMap!=null&&ActMap.size()>0){
		 
			for (Entry<String,TiKuScoreVO> entry:ActMap.entrySet()){
				
				
				TiKuScoreVO sub=tikuMap.get(entry.getKey()) ;
					if (sub!=null){ //不为空时说明已经存在了 直接满分
						sub.setStandarRate(90.0);
						tikuMap.put(entry.getKey(), sub);	
					}else {  //如果空的话 直接增加
						tikuMap.put(entry.getKey(), entry.getValue());
					}
				
				
			}
			} else {
				
			}
		 
		
		 
	
	

	
}

@Override
public void getFailExamStudent(Session session,
		Map<String, CreditRecordTiKuScore> failStudents 
		 ) {
	 List<CreditRecordTiKuScore> hvolist = session
	  .createQuery("from CreditRecordTiKuScore where  dr=0 and standarRate<60 and BillType='E'  ").list();
				 if (hvolist!=null&&hvolist.size()>0){
					 for (CreditRecordTiKuScore c:hvolist){
						 if (c!=null&&c.getStudentNCCode()!=null&&c.getCourseCode()!=null){
							 String key=c.getStudentNCCode()+"_"+c.getCourseCode();
							 failStudents.put(key, c);
						 }
						 
					 }
				 } 
	
}

@Override
public void getGetStudentOfFailExam(String beginDate,
		String endDate, String secretkey,
		Map<String, CreditStandard> creditStandardmap,
		Map<String, CreditRecordTiKuScore> failStudents,
		List<CreditRecordTiKuScore> updateTiKuScoreList,
		Map<String, String> examPassMasp) throws Exception {
	 WebService1Stub ww = new WebService1Stub(); 
if (failStudents!=null&&failStudents.size()>0){
	List<String> keylist= new ArrayList<String>(failStudents.keySet());
//	int num=0;
	
	List<TiKuScoreVO> resultList= new ArrayList<TiKuScoreVO> ();
	
	
	for (String str:keylist){
		
		 String[] sp_str=str.split("_");
	/*	 num++;
		 System.out.println(num+"/"+keylist.size()+"  "+DateUtil.DateToString(new Date()));
*/		 if (sp_str!=null&&sp_str.length>1){
			 WebService1Stub.GetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode w = new GetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode();  
		    	w.setCourseCode(sp_str[1]);
		    	w.setStudentNCCode(sp_str[0]);
		        String examstub_s = ww.getStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode(w).getGetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCodeResult();  
		        if(examstub_s == null){
		        	continue;
		        }
		    	 
		    	List<TiKuScoreVO>  tvos=JsonUtil.fromJson(examstub_s,new TypeToken<List<TiKuScoreVO>>(){}.getType());
		    //	getSubjectTypebyNC(tvos, creditStandardmap);
		    	
		    	resultList.addAll(tvos);
		 }
		 
	 }
		 
		 
 			 	for (int i=0;i<resultList.size();i++){
 			 		TiKuScoreVO sub= resultList.get(i);
 			 		
					 sub.setBillType("E");
				   	
 			 		 if (sub.getClassNCCode()==null||sub.getClassNCCode().length()<10||sub.getStandarRate()<=60){
 			 			 continue;
 			 		 }
 			 		 sub.setBillKey(sub.getStudentNCCode()+"_"+sub.getSubjectType()+"_"+sub.getBillType());
					 
				 	 String key=sub.getStudentNCCode()+"_"+sub.getCourseCode();
					
			 		
						CreditRecordTiKuScore tkvo=	failStudents.get(key);
			 		 if (tkvo!=null ){
			 			 //当从接口取到的学员比本地的分数高时 才更新
			 			 if (sub.getStandarRate().compareTo(tkvo.getStandarRate())>0
			 				 ){
			 				 if (tkvo.getFirstRate()==null||tkvo.getFirstRate()==0){
			 					tkvo.setFirstRate(tkvo.getStandarRate()); 
			 				 }
			 				tkvo.setStandarRate(sub.getStandarRate());
			 				
			 				updateTiKuScoreList.add(tkvo);
			 				//当分数超过60分时 直接增加到考试通过列表
			 				 if (CreditUtil.Double_half_up(tkvo.getStandarRate())>=60){
			 					examPassMasp.put(tkvo.getSubjectType()+tkvo.getStudentNCCode(), "E");
							 }
			 				
			 				
			 			 }
			 		  
			 		
			 
			 	}
		        
		
	} 
}

	
	
	 
}

@Override
public double getHomeWorkCompletionByDhandCoursecode(String dh,
		String coursecode  )
		throws Exception {
	 
	 WebService1Stub.GetHomeWorkCompletion w = new  WebService1Stub.GetHomeWorkCompletion();  
 	Double StandarRate=0.0;
 	w.setCourseCode(coursecode);
 	w.setLoginName(dh);
 	 WebService1Stub ww = new WebService1Stub(); 
     String examstub_s = ww.getHomeWorkCompletion(w).getGetHomeWorkCompletionResult();
     if(examstub_s == null||examstub_s.trim().length()<2){
     	return StandarRate.doubleValue();
     }
  
  	List<Map<String,String>>  list=JsonUtil.fromJson(examstub_s,new TypeToken<List<Map<String,String>>>(){}.getType());
 	for (Map<String,String> maps:list){
 		 
 		 
 	Double RightNum=Double.valueOf( maps.get("作对题数"));
 	Double PlanNum=Double.valueOf( maps.get("计划做题数"));
 	 
 	Double temp= (doubleUtilBase.div(RightNum, PlanNum, 2) );
 	        if (temp.compareTo(StandarRate)>=0){
 	        	StandarRate=temp;
 	        }
 	}
 	
 	return StandarRate.doubleValue();
 	 	
}

@Override
public void getHomeWorkCompletionByRecord(List<CreditRecord> Record,Map<String, String> codemap,Session session)
		throws Exception {
	
	//由于题库中的科目编号跟NC中的科目编号不一致的  由题库那边取得数据手动导入了自定义档案
	    codemap= CreditDaoUtil.getDefMap(session, "DEB201801150163888888");

	     int i=0;
	     
		for (CreditRecord d:Record){
			i++;
		 	//System.out.println(Record.size()+"/"+i+" "+DateUtil.DateToString(new Date()));
			if (d.getPhone()!=null&&d.getSubjectCode()!=null){
				
				String code_list=codemap.get( "TK_"+d.getSubjectType());
				double stardRate=0.0;
				double examRate=0.0;
				boolean isSave=false;
				if (code_list!=null){
					String [] code_str=code_list.split(",");
					for (String code:code_str){
						//当分数没有修完时才去题库取数 
						 if (d.getWorkActualScore()!=d.getWorkClaimScore()){
							double stardRate_s=getHomeWorkCompletionByDhandCoursecode(d.getPhone(),code);
							if (stardRate_s>stardRate){
								stardRate=stardRate_s;
							}	
						} 
						if (d.getExamActualScore()!=d.getExamClaimScore()){
						    double examRate_s=getExamComplianceRateByStudentIdAndCoursecode(d.getStudentId(),code);
						    if (examRate_s>examRate){
								examRate=examRate_s;
							}
						}
					
					
					}
					
					
				}else {
					stardRate=getHomeWorkCompletionByDhandCoursecode(d.getPhone(),d.getSubjectCode());
                    examRate=getExamComplianceRateByStudentIdAndCoursecode(d.getStudentId(),d.getSubjectCode());

				}
				
				
				  BigDecimal  a    =   new   BigDecimal(stardRate);
				  BigDecimal  b    =   new   BigDecimal(0.9);
				  
				if (a.compareTo(b)>=0){
					 	isSave=true;
					d.setWorkActualScore(d.getWorkClaimScore());	
					 //写入题库分数
					CreditRecordTiKuScore tk= new CreditRecordTiKuScore();
					tk.setCourseCode(d.getSubjectCode());
					tk.setStudentNCCode(d.getStudentId());
					tk.setBillKey(d.getStudentId()+"_"+d.getSubjectType()+"_"+"W");
					tk.setBillType("W");
					tk.setBillType(d.getSubjectType());
					tk.setStudentName(d.getStudentName());
					tk.setFirstRate(stardRate);
					tk.setStandarRate(stardRate);
					tk.setClassNCCode("none_from_oldTiku_W");
					tk.setDr(0);
					session.save(tk);
				}
				   
				  if (examRate>=60&&d.getExamClaimScore()!=d.getExamActualScore()){
					  isSave=true;
					
					d.setExamActualScore(d.getExamClaimScore());
					CreditRecordTiKuScore tk= new CreditRecordTiKuScore();
					tk.setCourseCode(d.getSubjectCode());
					tk.setStudentNCCode(d.getStudentId());
					tk.setBillKey(d.getStudentId()+"_"+d.getSubjectType()+"_"+"E");
					tk.setFirstRate(examRate);
					tk.setStandarRate(examRate);
					tk.setBillType(d.getSubjectType());
					tk.setStudentName(d.getStudentName());
					tk.setBillType("E");
					tk.setClassNCCode("none_from_oldTiku_E");
					tk.setDr(0);
					session.save(tk);
				}
				
				
				if (isSave){
					CreditUtil.CheckCreditRecordPass(d);		
				//	d.setIsObsoleted(0);//是旧数据
					session.update(d);
				}
				
				
			
		
				
			}
		}
	
	 
 
	
}

@Override
public double getExamComplianceRateByStudentIdAndCoursecode(String studentID,
		String coursecode) throws Exception {
	 WebService1Stub ww = new WebService1Stub(); 
	 WebService1Stub.GetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode w = new GetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode();  
 	w.setCourseCode(coursecode);
 	w.setStudentNCCode(studentID);
     String examstub_s = ww.getStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode(w).getGetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCodeResult();  
     if(examstub_s == null||examstub_s.trim().length()<10){
     	 return 0.0;
     }
   
 	List<TiKuScoreVO>  tvos=JsonUtil.fromJson(examstub_s,new TypeToken<List<TiKuScoreVO>>(){}.getType());

 /*
  	for (TiKuScoreVO list:tvos){
		 if (list!=null&&StandarRate<=list.getStandarRate()&&list.getStandarRate()>=60){
			 StandarRate=list.getStandarRate();
		 }
 
	 
	}*/
 	
 	 
    Double in_map=0.0; //在排课的
    Double  off_map=0.0;//没在排课的
    boolean is_paike=false;
 	//加入遍历了 当同时查到多条的按下面的方式来排列
	//如果有还在排课的 以在排课的为准 并取最大值，如果都是删除排课的 取最大值 
	for (TiKuScoreVO t:tvos){
		if (t!=null&&t.getClassNCCode()!=null&&t.getClassNCCode().length()>10){
			if (t.getStandarRate()!=null&&t.getStandarRate().compareTo(in_map)>0){
				in_map=t.getStandarRate();
			}
			is_paike=true;
		}else  {
			if (t.getStandarRate()!=null&&t.getStandarRate().compareTo(off_map)>0){
				off_map=t.getStandarRate();
			}
		}
	}
	 
	if (is_paike){
		return in_map.doubleValue();
	}else {
		return off_map.doubleValue();
	}
	
	
}
 
}
