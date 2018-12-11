package com.izhubo.credit.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.izhubo.admin.BaseController;
import com.izhubo.credit.service.CreditService;
import com.izhubo.credit.util.MD5Util;
import com.izhubo.credit.util.NcSyncConstant;
import com.izhubo.credit.util.SubjectIdUtil;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditStandard;

/**
 * 学分控制器
 *
 */
@Controller
@RequestMapping("/credit")
public class CreditController extends BaseController{
	
	@Resource
	CreditService creditService;
	
	
	/**
	 * secretKey=算法MD5(studentid+ getMyCreditkey())
	 * 获取学员的学分
	 * @param request
	 * @param secretKey
	 * @param type pc/mobile
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="getMyCredit")
	@ResponseBody
	public void getMyCredit(HttpServletRequest request,HttpServletResponse response, String secretKey,String studentid,String type) throws IOException{
		/*******************测试用*******************************/
		System.out.println(studentid);
		System.out.println(secretKey);
		/********************测试用**********************************/
		//校验key
		if(!MD5Util.getMD5Code(studentid+NcSyncConstant.getMyCreditkey()).equals(secretKey)){
			return;
		}
		String html = "";
		if(studentid !=null){
			//查询该学员的学分
			List<CreditRecord> voList =creditService.getCreditRecordByStudentId(studentid);
			
			
			//这里要过滤掉重复的值
			Map<String,CreditRecord> map = new HashMap<String,CreditRecord>();
			//过滤掉重复【学员主键+科目主键】数据,如果是重复科目按照最新报名表显示
			for(int i = 0; i< voList.size();i++){
				CreditRecord vo = voList.get(i);
				String stubject_id = vo.getSubjectId();
				String student_id  = vo.getStudentId();
				String key = student_id +"@"+stubject_id;
				if(map.get(key) ==null){
					map.put(key, vo);
				}else{
					//重复科目，存报名表最新的班型
					CreditRecord old = map.get(key);
					if(vo.getSignDate().compareTo(old.getSignDate()) > 0){
						map.put(key,vo);
					}
				}
			}
			
			Map<String,List<CreditRecord>> classMap = new HashMap<String,List<CreditRecord>>();
			int subjectNum = 0;
			if(map.size() > 0){
				//计算总科目
				for (Map.Entry<String, CreditRecord> entry : map.entrySet()) {
					CreditRecord votemp = entry.getValue();
					subjectNum++;
					if(classMap.get(votemp.getClassId()) == null){
						List<CreditRecord> list =  new ArrayList<CreditRecord>();
						list.add(votemp);
						classMap.put(votemp.getClassId(), list);
					}else{
						List<CreditRecord> list  = classMap.get(votemp.getClassId());
						list.add(votemp);
					}
				}
				
				//查询标准科目学分
				List<CreditStandard> creditStandard =creditService.getCreditStandard();
				Map<String,CreditStandard> all_accountMap = new HashMap<String,CreditStandard>();//会计证所有科目
				Map<String,CreditStandard> all_juniorMap = new HashMap<String,CreditStandard>();//初级职称所有科目
				for (int i = 0; i < creditStandard.size(); i++) {
					CreditStandard vo =  creditStandard.get(i);
					String subType = vo.getSubject_type();
					//会计证
				/*	if(SubjectIdUtil.ACCOUNTING_BASIC.equals(subType)){
						all_accountMap.put(vo.getNc_id(), vo);
					}
					if(SubjectIdUtil.ACCOUNTING_FE_PE.equals(subType)){
						all_accountMap.put(vo.getNc_id(), vo);
					}
					if(SubjectIdUtil.ACCOUNTING_ACCOUNTING_COMPUTERIZATION.equals(subType)){
						all_accountMap.put(vo.getNc_id(), vo);
					}
					*/
					//初级职称
					if(SubjectIdUtil.JUNIOR_ACCOUNTING_PRACTICE.equals(subType)){
						all_juniorMap.put(vo.getNc_id(), vo);
					}
					if(SubjectIdUtil.JUNIOR_BASIC_ECONOMIC_LAW.equals(subType)){
						all_juniorMap.put(vo.getNc_id(), vo);
					}
					
					
				}
				
				if(classMap.size()>0){
					int allSum = 0;//学员全部满分
					int cash = 0;//每个班级的优惠卷
					//计算每个班级的学员对应的总分
					Map<String,List<Integer>> classSumMap = new HashMap<String,List<Integer>>();
					for (Map.Entry<String, List<CreditRecord>> entry : classMap.entrySet()) {
						int sum = 0;//实际每个班学分总分
						int baseSum = 0;//每个班标准满分
						boolean junior_b = true;
						//boolean accounting_b = true;
						List<CreditRecord> vos = entry.getValue();
						for (int i = 0; i < vos.size(); i++) {
							CreditRecord votemp = vos.get(i);
							if(!"Y".equals(votemp.getIsGainCash())){
								cash += votemp.getAttendanceActualScore()+votemp.getWorkActualScore();
							}
							
							sum += votemp.getAttendanceActualScore()+votemp.getWorkActualScore();
							baseSum += votemp.getAttendanceClaimScore()+votemp.getWorkClaimScore();
							//计算总分,有特殊科目，
							if(all_juniorMap.get(votemp.getSubjectId()) !=null && junior_b){
								//只加一次考核分
								sum += votemp.getExamActualScore();
								if(!"Y".equals(votemp.getIsGainCash())){
									cash += votemp.getExamActualScore();
								}
								baseSum += votemp.getExamClaimScore();
								junior_b = false;
							}/*else if(all_accountMap.get(votemp.getNcSubjectId())!=null && accounting_b){
								//只加一次考核分
								sum += votemp.getExamActualScore();
								if(!"Y".equals(votemp.getIsGainCash())){
									cash += votemp.getExamActualScore();
								}
								baseSum += votemp.getExamClaimScore();
								accounting_b = false;
							}*/else if(all_juniorMap.get(votemp.getSubjectId()) ==null ){//&& all_accountMap.get(votemp.getNcSubjectId())==null
								sum += votemp.getExamActualScore();
								if(!"Y".equals(votemp.getIsGainCash())){
									cash += votemp.getExamActualScore();
								}
								baseSum += votemp.getExamClaimScore();
							}
						}
						List<Integer> l = new ArrayList<Integer>();
						l.add(baseSum);
						l.add(sum);
						l.add(cash);
						allSum += baseSum;
						classSumMap.put(entry.getKey(), l);
					}
					if("mobile".equalsIgnoreCase(type)){
						html = getStudentCreditTable(classMap,subjectNum,classSumMap,allSum,all_juniorMap,all_accountMap,type);
					}else{
						html = getStudentCreditTable(classMap,subjectNum,classSumMap,allSum,all_juniorMap,all_accountMap);
					}
					
				}
				
			}
			
		}
		response.getWriter().print(html);
		response.getWriter().close();
	}
	
	
	/**
	 *获取学员对应学分优惠卷
	 * @param request
	 * @param secretKey
	 * @param lastDate
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(method = RequestMethod.POST,value="getCashCoupon")
	@ResponseBody
	public void getCashCoupon (HttpServletRequest request,HttpServletResponse response, String secretKey,String studentid,String classId) throws IOException{
		JSONObject o = new JSONObject();
		//校验key
		if(!MD5Util.getMD5Code(studentid+NcSyncConstant.getMyCreditkey()).equals(secretKey)){
			o.put("code", 1);//代表失败 
			o.put("number",0);//金额
		}else{
		
			if(studentid !=null){
				//查询该学员的学分
				List<CreditRecord> voList =creditService.getStudentCredit(studentid);
				if(voList ==null || voList.size() == 0){
					o.put("code", 1);//代表失败
					o.put("number",0);//金额
				}else{
					//这里要过滤掉重复的值
					Map<String,CreditRecord> map = new HashMap<String,CreditRecord>();
					//过滤掉重复【学员主键+科目主键】数据,如果是重复科目按照最新报名表显示
					for(int i = 0; i< voList.size();i++){
						CreditRecord vo = voList.get(i);
						String stubject_id = vo.getSubjectId();
						String student_id  = vo.getStudentId();
						String key = student_id +"@"+stubject_id;
						if(map.get(key) ==null){
							map.put(key, vo);
						}else{
							//重复科目，存报名表最新的班型
							CreditRecord old = map.get(key);
							if(vo.getSignDate().compareTo(old.getSignDate()) > 0){
								map.put(key,vo);
							}
						}
					}
						
						//根据班型合并
						Map<String,List<CreditRecord>> classMap = new HashMap<String,List<CreditRecord>>();
						for (Map.Entry<String, CreditRecord> entry : map.entrySet()) {
							CreditRecord vo= entry.getValue();
							if(vo.getClassId().equals(classId)){
								if(classMap.get(vo.getClassId()) == null){
									List<CreditRecord> list =  new ArrayList<CreditRecord>();
									list.add(vo);
									classMap.put(vo.getClassId(), list);
								}else{
									List<CreditRecord> list  = classMap.get(vo.getClassId());
									list.add(vo);
								}
							}
							
						}
						
						//查询标准科目学分
						List<CreditStandard> creditStandard =creditService.getCreditStandard();
						Map<String,CreditStandard> all_accountMap = new HashMap<String,CreditStandard>();//会计证所有科目
						Map<String,CreditStandard> all_juniorMap = new HashMap<String,CreditStandard>();//初级职称所有科目
						for (int i = 0; i < creditStandard.size(); i++) {
							CreditStandard vo =  creditStandard.get(i);
							String subType = vo.getSubject_type();
							//会计证
						/*	if(SubjectIdUtil.ACCOUNTING_BASIC.equals(subType)){
								all_accountMap.put(vo.getNc_id(), vo);
							}
							if(SubjectIdUtil.ACCOUNTING_FE_PE.equals(subType)){
								all_accountMap.put(vo.getNc_id(), vo);
							}
							if(SubjectIdUtil.ACCOUNTING_ACCOUNTING_COMPUTERIZATION.equals(subType)){
								all_accountMap.put(vo.getNc_id(), vo);
							}*/
							
							//初级职称
							if(SubjectIdUtil.JUNIOR_ACCOUNTING_PRACTICE.equals(subType)){
								all_juniorMap.put(vo.getNc_id(), vo);
							}
							if(SubjectIdUtil.JUNIOR_BASIC_ECONOMIC_LAW.equals(subType)){
								all_juniorMap.put(vo.getNc_id(), vo);
							}
							
							
						}
						
						int allCash = 0;
						List<String> classids = new ArrayList<String>();
						for (Map.Entry<String, List<CreditRecord>> entry : classMap.entrySet()) {
							boolean junior_b = true;
							//boolean accounting_b = true;
							String classid = entry.getKey();
							int sum = 0;//实际每个班学分总分
							int baseSum = 0;//每个班标准满分
							int classCash = 0;//每个班级的现金
							List<CreditRecord> vos = entry.getValue();
							for (int i = 0; i < vos.size(); i++) {
								CreditRecord vo = vos.get(i);
								if(!"Y".equals(vo.getIsGainCash())){
									classCash += vo.getAttendanceActualScore()+vo.getWorkActualScore();
								}
								//计算总分,有特殊科目，
								sum += vo.getAttendanceActualScore()+vo.getWorkActualScore();
								baseSum += vo.getAttendanceClaimScore()+vo.getWorkClaimScore();
								//计算总分,有特殊科目，
								if(all_juniorMap.get(vo.getSubjectId()) !=null && junior_b){
									//只加一次考核分
									sum += vo.getExamActualScore();
									if(!"Y".equals(vo.getIsGainCash())){
										classCash += vo.getExamActualScore();
									}
									baseSum += vo.getExamClaimScore();
									junior_b = false;
								}/*else if(all_accountMap.get(vo.getNcSubjectId())!=null && accounting_b){
									//只加一次考核分
									sum += vo.getExamActualScore();
									if(!"Y".equals(vo.getIsGainCash())){
										classCash += vo.getExamActualScore();
									}
									baseSum += vo.getExamClaimScore();
									accounting_b = false;
								}*/else if(all_juniorMap.get(vo.getSubjectId()) ==null){// && all_accountMap.get(vo.getNcSubjectId())==null
									sum += vo.getExamActualScore();
									if(!"Y".equals(vo.getIsGainCash())){
										classCash += vo.getExamActualScore();
									}
									baseSum += vo.getExamClaimScore();
								}
							}
							//判断该班型是否可以毕业
							if(sum >= ((int)baseSum*0.8)){
								allCash += classCash*2;
								classids.add(classid);
							}
						}
						
						if(allCash > 0){
							int count = 0;
							for (int i = 0; i < classids.size(); i++) {
								String classid = classids.get(i);
								int num = creditService.updateStudentCredit(studentid,classid);
								if(num>0){
									count ++;
								}
							}
							if(count !=0 && count == classids.size()){
									o.put("code", 0);//代表成功
									o.put("number",allCash);//金额
							   
							}else{
							   o.put("code", 1);//代表失败
							   o.put("number",0);//金额
							}
							
						}else{
							o.put("code", 1);//代表失败
							o.put("number",0);//金额
						}
					}
				
			}else{
				o.put("code", 1);//代表失败
				o.put("number",0);//金额
			}
		}
		response.getWriter().print(o.toString());
		response.getWriter().close();
	}
	
/**
 * 
 * @param classMap
 * @param subjectNum
 * @param classSumMap
 * @param allSum
 * @param all_juniorMap
 * @param all_accountMap
 * @param type
 * @return
 */
	public String getStudentCreditTable(Map<String, List<CreditRecord>> classMap, 
			int subjectNum,Map<String, List<Integer>> classSumMap,
			int allSum, 
			Map<String, CreditStandard> all_juniorMap, 
			Map<String, CreditStandard> all_accountMap,String type){
		StringBuilder str = new StringBuilder();
		str.append("<div class=\"pageMain\">");
		str.append("<div class=\"xuefen-table\">");
		for (Map.Entry<String, List<CreditRecord>> entry : classMap.entrySet()) {
			boolean junior_b = true;
			//boolean accounting_b = true;
			String classKey = entry.getKey();
			int baseSum = classSumMap.get(classKey).get(0);
			int sum = classSumMap.get(classKey).get(1);
			int cash = classSumMap.get(classKey).get(2);
			
			List<CreditRecord> list = entry.getValue();
			
			str.append("<table>");
			str.append("<tr>");
			str.append("<th>班级</th>");
			str.append("<th>综合学分<br>满分"+allSum+"分</th>");
			str.append("<th>毕业学分标准≥<br>每科总分*80％</th>");
			str.append("<th>学习建议</th>");
			str.append("</tr>");
			
			str.append("<tr>");
			str.append("<td>"+list.get(0).getClassName()+"</td>");
			str.append("<td>");
			str.append("<div class=\"t-h1\">当前学分<span>"+sum+"分！</span></div>");
			str.append("</td>");
			if(sum >= ((int)baseSum*0.8)){
				//满足毕业
				if(cash == 0){
					//已经领取
					str.append("<td>您已经兑换过优惠卷！</td>");
				}else{
					//没有领取
					str.append("<td>恭喜你!顺利毕业了!<a rel=\""+classKey+"\" href=\"#\"><font color='#FF0000'>获取优惠卷</font></a>");
					str.append("</td>");
				}
			}else{
				str.append("<td>您的学分还没达到毕业标准要加油哦！</td>");
			}
			str.append("<td>"+(list.get(0).getLearningTip()==null?"":list.get(0).getLearningTip())+"</td>");
			str.append("</tr>");
			
			
			str.append("<tr>");
			str.append("<th>科目</th>");
			str.append("<th>考勤学分</th>");
			str.append("<th>作业学分</th>");
			str.append("<th>结课考核<br>学分</th>");
			str.append("</tr>");
			//排序科目
			//int account_sum = 0;
			int junior_sum = 0;
			/*for (int i = 0; i < list.size(); i++) {
				CreditRecord vo = list.get(i);
				if(all_accountMap.get(vo.getNcSubjectId()) !=null){
					account_sum++;
					list.add(0, list.remove(i));
				}
			}*/
			
			for (int i = 0; i < list.size(); i++) {
				CreditRecord vo = list.get(i);
				 if(all_juniorMap.get(vo.getSubjectId()) !=null){
					junior_sum++;
					list.add(0, list.remove(i));
				}
			}
			//科目的遍历
			for (int i = 0; i < list.size(); i++) {
				CreditRecord vo = list.get(i);
				str.append("<tr>");
				
				str.append("<td>"+vo.getSubjectName()+"</td>");
				str.append("<td>");
				if(vo.getAttendanceClaimScore() == null || vo.getAttendanceClaimScore() == 0){
					str.append("<div align=\"center\">———</div>");
				}else{
					str.append("<div align=\"center\">");
					str.append("<div class=\"t-h1\">实修<span>"+vo.getAttendanceActualScore()+"分</span></div>");
					str.append("<div class=\"t-h2\">应修<span>"+vo.getAttendanceClaimScore()+"分</span></div>");
					str.append("</div>");
				}
				str.append("</td>");
				str.append("<td>");
				if(vo.getWorkClaimScore() == null || vo.getWorkClaimScore()== 0){
					str.append("<div align=\"center\">———</div>");
				}else{
					str.append("<div align=\"center\">");
					str.append("<div class=\"t-h1\">实修<span>"+vo.getWorkActualScore()+"分</span></div>");
					str.append("<div class=\"t-h2\">应修<span>"+vo.getWorkClaimScore()+"分</span></div>");
					str.append("</div>");
				}
				str.append("</td>");
				
				
				
				if(vo.getExamClaimScore() == null || vo.getExamClaimScore()== 0){
					str.append("<td>");
					str.append("<div align=\"center\">———</div>");
					str.append("</td>");
				}else{
					//前提要排序好
					/*if(all_accountMap.get(vo.getNcSubjectId()) !=null && accounting_b){
						str.append("<td rowspan=\""+(account_sum)+"\">");
						str.append("<div align=\"center\">");
						str.append("<div class=\"t-h1\">实修<span>"+vo.getExamActualScore()+"分</span></div>");
						str.append("<div class=\"t-h2\">应修<span>"+vo.getExamClaimScore()+"分</span></div>");
						str.append("</div>");
						str.append("</td>");
						accounting_b =false;
					}else*/ if(all_juniorMap.get(vo.getSubjectId()) !=null && junior_b){
						str.append("<td rowspan=\""+(junior_sum)+"\">");
						str.append("<div align=\"center\">");
						str.append("<div class=\"t-h1\">实修<span>"+vo.getExamActualScore()+"分</span></div>");
						str.append("<div class=\"t-h2\">应修<span>"+vo.getExamClaimScore()+"分</span></div>");
						str.append("</div>");
						str.append("</td>");
						junior_b = false;
					}else if( all_juniorMap.get(vo.getSubjectId()) ==null ){//all_accountMap.get(vo.getNcSubjectId()) ==null &&
						str.append("<td>");
						str.append("<div align=\"center\">");
						str.append("<div class=\"t-h1\">实修<span>"+vo.getExamActualScore()+"分</span></div>");
						str.append("<div class=\"t-h2\">应修<span>"+vo.getExamClaimScore()+"分</span></div>");
						str.append("</div>");
						str.append("</td>");
					}
				}
				str.append("</tr>");
			}
			
			str.append("</table>");
		}
		str.append("</div>");
		str.append("</div>");
		return str.toString();
		
	}
	
	/**
	 * 
	 * @param classMap key=classid value = 班级对应的科目
	 * @param sum
	 * @param subjectNum 
	 * @param learnTipmap
	 * @param ispass 
	 * @param classSumMap 
	 * @param allSum 
	 * @param baseClassMap 
	 * @param all_accountMap 
	 * @param all_juniorMap 
	 * @param allcash 
	 * @param allpass 
	 * @param type pc/mobile
	 * @param baseSum 
	 * @return
	 */
	public String getStudentCreditTable(Map<String, List<CreditRecord>> classMap, 
			int subjectNum,Map<String, List<Integer>> classSumMap,
			int allSum, 
			Map<String, CreditStandard> all_juniorMap, 
			Map<String, CreditStandard> all_accountMap){
		
		StringBuilder str = new StringBuilder();
		str.append("<div class=\"xuefen-table\">");
		str.append("<table style=\"BORDER-COLLAPSE: collapse\" bordercolor=#666 cellspacing=0 width=950px align=\"center\" bgcolor=#ffffff border=1>");
		/******************表头 end*******************************/
		str.append("<tr style=\"background-color: #F3F3F3\">");
		
		str.append("<th rowspan=\"2\">");
		str.append("<div align=\"center\">班级</div>");
		str.append("</th>");
		str.append("<th rowspan=\"2\">");
		str.append("<div align=\"center\">科目</div>");
		str.append("</th>");
		str.append("<th colspan=\"3\">");
		str.append("<div align=\"center\">获得的学分</div>");
		str.append("</th>");
		str.append("<th rowspan=\"2\">");
		str.append("<div align=\"center\">综合学分</br>满分"+allSum+"分</div>");
		str.append("</th>");
		str.append("<th rowspan=\"2\">");
		str.append("<div align=\"center\">毕业学分标准</br>≥每科总分*80%</div>");
		str.append("</th>");
		str.append("<th rowspan=\"2\">");
		str.append("<div align=\"center\">学习建议</div>");
		str.append("</th>");
		
		str.append("</tr>");
		/******************表头 end*******************************/
		str.append("<tr style=\"background-color: #F3F3F3\">");
		str.append("<td>");
		str.append("<div align=\"center\">考勤</div>");
		str.append("</td>");
		str.append("<td>");
		str.append("<div align=\"center\">作业</div>");
		str.append("</td>");
		str.append("<td>");
		str.append("<div align=\"center\">结课考核</div>");
		str.append("</td>");
		
		str.append("</tr>");
		
		for (Map.Entry<String, List<CreditRecord>> entry : classMap.entrySet()) {
			boolean junior_b = true;
			//boolean accounting_b = true;
			String classKey = entry.getKey();
			List<CreditRecord> list = entry.getValue();
			
			String gainCash = list.get(0).getIsGainCash();
			int baseSum = classSumMap.get(classKey).get(0);
			int sum = classSumMap.get(classKey).get(1);
			int cash = classSumMap.get(classKey).get(2);
			str.append("<tr>");
			
			str.append("<td rowspan=\""+(list.size()+1)+"\">");
			str.append("<div align=\"center\">"+list.get(0).getClassName()+"</div>");
			str.append("</td>");
			
			str.append("</tr>");
			
			
			//排序科目
			//int account_sum = 0;
			int junior_sum = 0;
			/*for (int i = 0; i < list.size(); i++) {
				CreditRecord vo = list.get(i);
				if(all_accountMap.get(vo.getNcSubjectId()) !=null){
					account_sum++;
					list.add(0, list.remove(i));
				}
			}*/
			
			for (int i = 0; i < list.size(); i++) {
				CreditRecord vo = list.get(i);
				 if(all_juniorMap.get(vo.getSubjectId()) !=null){
					junior_sum++;
					list.add(0, list.remove(i));
				}
			}
			
			
			for (int i = 0; i < list.size(); i++) {
				CreditRecord vo = list.get(i);
				str.append("<tr>");
				
				str.append("<td>");
				str.append("<div align=\"center\">"+vo.getSubjectName()+"</div>");
				str.append("</td>");
				str.append("<td>");
				if(vo.getAttendanceClaimScore() == null || vo.getAttendanceClaimScore() == 0){
					str.append("<div align=\"center\">———</div>");
				}else{
					str.append("<div align=\"center\">");
					str.append("<div class=\"t-h1\">实修<span>"+vo.getAttendanceActualScore()+"分</span></div>");
					str.append("<div class=\"t-h2\">应修<span>"+vo.getAttendanceClaimScore()+"分</span></div>");
					str.append("</div>");
				}
				str.append("</td>");
				str.append("<td>");
				if(vo.getWorkClaimScore() == null || vo.getWorkClaimScore()== 0){
					str.append("<div align=\"center\">———</div>");
				}else{
					str.append("<div align=\"center\">");
					str.append("<div class=\"t-h1\">实修<span>"+vo.getWorkActualScore()+"分</span></div>");
					str.append("<div class=\"t-h2\">应修<span>"+vo.getWorkClaimScore()+"分</span></div>");
					str.append("</div>");
				}
				str.append("</td>");
				
				
				
				if(vo.getExamClaimScore() == null || vo.getExamClaimScore()== 0){
					str.append("<td>");
					str.append("<div align=\"center\">———</div>");
					str.append("</td>");
				}else{
					//前提要排序好
					/*if(all_accountMap.get(vo.getNcSubjectId()) !=null && accounting_b){
						str.append("<td rowspan=\""+(account_sum)+"\">");
						str.append("<div align=\"center\">");
						str.append("<div class=\"t-h1\">实修<span>"+vo.getExamActualScore()+"分</span></div>");
						str.append("<div class=\"t-h2\">应修<span>"+vo.getExamClaimScore()+"分</span></div>");
						str.append("</div>");
						str.append("</td>");
						accounting_b =false;
					}else */if(all_juniorMap.get(vo.getSubjectId()) !=null && junior_b){
						str.append("<td rowspan=\""+(junior_sum)+"\">");
						str.append("<div align=\"center\">");
						str.append("<div class=\"t-h1\">实修<span>"+vo.getExamActualScore()+"分</span></div>");
						str.append("<div class=\"t-h2\">应修<span>"+vo.getExamClaimScore()+"分</span></div>");
						str.append("</div>");
						str.append("</td>");
						junior_b = false;
					}else if(all_juniorMap.get(vo.getSubjectId()) ==null ){//all_accountMap.get(vo.getNcSubjectId()) ==null && 
						str.append("<td>");
						str.append("<div align=\"center\">");
						str.append("<div class=\"t-h1\">实修<span>"+vo.getExamActualScore()+"分</span></div>");
						str.append("<div class=\"t-h2\">应修<span>"+vo.getExamClaimScore()+"分</span></div>");
						str.append("</div>");
						str.append("</td>");
					}
				}
				
				
				if(i == 0){
					String info = "";
					String get ="";
					str.append("<td rowSpan="+(list.size()+1)+">");
					str.append("<div align=center>当前学分</br>"+sum+"分 !</div>");
					str.append("</td>");
					str.append("<td rowSpan="+(list.size()+1)+">");
					if("Y".equals(gainCash)){
						//按班型获取优惠卷，如果一个班有一个获取的就代表获取
						str.append("<div align=center>"+info+"</br><font color='#FF0000'>已经兑换优惠卷</font></div>");
					}else{
						if(sum >= ((int)baseSum*0.8)){
							info = "恭喜你顺利毕业了!";
							if(cash != 0){
								get = "<a  rel=\""+classKey+"\" href=\"#\"><font color='#FF0000'>获取优惠卷</font></a>";
							}
							//满足毕业
							str.append("<div align=center>"+info+"</br>"+get+"</div>");
						}else{
							info = "你的学分还没</br>达到毕业标准</br>要加油哦!";
							str.append("<div align=center>"+info+"</br>"+get+"</div>");
						}
						
					}
					str.append("</td>");
					
					
					str.append("<td rowSpan="+(list.size()+1)+">");
					str.append("<div align=center>"+(vo.getLearningTip()==null?"":vo.getLearningTip())+"</div>");
					str.append("</td>");
					
				}
				
				str.append("</tr>");
			}
			
		}
		
		str.append("</table>");
		str.append("</div>");
		return str.toString();
	}
	
}
