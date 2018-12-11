package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.text.DecimalFormat
import java.util.List;
import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils
import org.hibernate.Query
import org.hibernate.SQLQuery
import org.hibernate.SessionFactory
import org.springframework.web.bind.ServletRequestUtils
import com.izhubo.credit.util.CreditUtil
import com.izhubo.credit.util.DateUtil
import com.izhubo.rest.anno.RestWithSession
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder
import com.mysqldb.model.CreditQueryPermission 
import com.mysqldb.model.CreditRecordReportTemp

/**
 * 学分报表 月科目汇总表
 * @author lintf
 * @date 2017年9月20日21:28:49
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class CreditsubjectmonController extends BaseController {
  
	@Resource
	private SessionFactory sessionFactory;
	
	/*DBCollection teachersTable(){
		return mainMongo.getCollection('teachers');
	}*/
	
	DBCollection areasTable(){
		return mainMongo.getCollection('area');
	}

	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		int pageSize = ServletRequestUtils.getIntParameter(req, "size", 5000);
		int page = ServletRequestUtils.getIntParameter(req, "page", 1);
		def orgName = req.getParameter("orgName");
		String subjectName = req.getParameter("subjectName");
		 
	 
		String bSignDate = req.getParameter("beginSignDate");
		String eSignDate = req.getParameter("endSignDate");
		String largeAreaName = req.getParameter("largeAreaName");
		 
		StringBuilder hql = new StringBuilder();
 
		//hql.append(" select a.org_name,substr(a.sign_date,1,7),a.subject_name,count(student_id) rs, ");
		//hql.append(" sum(a.total_score) total_score,sum(a.claim_score) claim_score,sum(ispk) pkrs,sum(ispass) passrs ");
		//hql.append(" from 		 ( ");
		hql.append(" select  s.org_code,s.org_name, r.class_name, r.teacher_id,s.student_name,s.student_id,s.sign_date,r.subject_name,		   ");
		hql.append(" r.total_score,r.attendance_claim_score+r.work_claim_score+r.exam_claim_score claim_score, ");
		hql.append(" case when r.is_pass=0 then 1 else 0 end as ispass, ");
		hql.append(" case when r.arr_name is not null then 1 else 0 end as ispk ");
		hql.append(" from credit_record  r   LEFT JOIN credit_record_sign s  on (s.sign_id = r.sign_id )  ");
	
		hql.append(" where s.org_name is not null and s.is_enable=0 and r.is_enable=0 ");
		
		
		//增加过滤不考核的科目
		hql.append(" and r.subject_id not in ( ");
		hql.append(" select def_key from  credit_def d where d.def_list='DEF201711180162649104479406' and dr=0 ");
		hql.append(" ) ");
		//hql.append("  ) a  ");
		//hql.append("  group by a.org_name,substr(a.sign_date,1,7),a.subject_name  ");
		
		 
		
		 
		 
		if(StringUtils.isNotBlank(orgName)){
			hql.append(" AND s.org_name  LIKE '%" + orgName + "%'");
		 
		}
		 	if(StringUtils.isNotBlank(subjectName)){
			hql.append(" AND r.subject_name LIKE '%" + subjectName + "%'");
			 
		}
	 
		
		if(StringUtils.isNotBlank(bSignDate)) {
			//查询制定时间之后的记录
			hql.append(" AND r.sign_date >= '" + bSignDate + "'");
		 
		}
		if(StringUtils.isNotBlank(eSignDate)){
			//查询指定时间之前的记录
			hql.append(" AND r.sign_date <= '" + eSignDate + "'");
		 
		}
		
		
 
		Map user = (Map) req.getSession().getAttribute("user");
		//根据用户id查询对应的权限-老师
		String phql = " from CreditQueryPermission where userId = ?";
		Query q = sessionFactory.getCurrentSession().createQuery(phql);
		q.setString(0, user.get("_id"));
		List<CreditQueryPermission> vos = q.list();
		List<String> teacherIds  = new ArrayList<String>();
		List<String> orgs  = new ArrayList<String>();
		boolean superAdmin = false;
		if(vos !=null && vos.size()>0){
			for (int i = 0;i < vos.size();i++) {
				CreditQueryPermission vo = vos.get(i);
				//超级管理员可以查看全部数据type=1
				if(vo.getType()!=null && vo.getType() == 1){
					superAdmin = true;
					break;
				}
				//获取校区权限
				if(vo.getOrgId()!=null && vo.getOrgId().length()>0 ){
					orgs.add(vo.getOrgId());
				}else if(vo.getTeacherId()!=null && vo.getTeacherId().length()>0){
					//获取老师权限
					teacherIds.add(vo.getTeacherId());
				}
			}
		}
		
		if(orgs !=null && orgs.size() > 0){
			//添加校区的权限
			hql.append(" and s.org_id in ")
			hql.append("(");
			 
			for (int i = 0;i < orgs.size();i++) {
				String id =orgs.get(i);
				hql.append(" '"+id+"' ");
				 
				if(i != orgs.size()-1){
					hql.append(",");
				 
				}
			}
			hql.append(")"); 
		}else if(teacherIds !=null && teacherIds.size() > 0){
 			//添加老师的权限
 			hql.append(" and r.teacher_id in ")
 			hql.append("(");
 			 
 			for (int i = 0;i < teacherIds.size();i++) {
 				String id =teacherIds.get(i);
 				hql.append(" '"+id+"' ");
 		 
 				if(i != teacherIds.size()-1){
 					hql.append(",");
 				 
 				}
 			}
 			hql.append(")");
			 
		}else if(!superAdmin){
			Map map = new HashMap();
			map.put("code", 1);
			map.put("msg", "success");
			map.put("count",0);
			map.put("data", new ArrayList());
			map.put("allPage",0);
			return map;
		}else {
			Map map = new HashMap();
			map.put("code", 1);
			map.put("msg", "success");
			map.put("count",0);
			map.put("data", new ArrayList());
			map.put("allPage",0);
			return map;
		}
		
		//添加大区过滤
		if(StringUtils.isNotBlank(largeAreaName)){
			List<String> largeOrgs  = new ArrayList<String>();
			//获取大区对应的所有校区
			def query = QueryBuilder.start()
			query.and("name").is(largeAreaName);
			def largeArea = areasTable().findOne(query.get(),$$("name":1,"code":1,"nc_id":1,"_id":0));
			if(largeArea == null){
				hql.append(" AND 1=2 ");
			 
			}else{
				String parent_nc_id = largeArea.get("nc_id");
				getAllChildId(parent_nc_id,largeOrgs);
				if(largeOrgs !=null && largeOrgs.size() > 0){
					hql.append(" and s.org_id in ")
					hql.append("(");
				 
					for (int i = 0;i < largeOrgs.size();i++) {
						String id =largeOrgs.get(i);
						hql.append(" '"+id+"' ");
					 
						if(i != largeOrgs.size()-1){
							hql.append(",");
						 
						}
					}
					hql.append(")");
					 
				}else{
					hql.append(" AND 1=2 ");
					 
				}
			}
		}
		
	/**
	 * 开始查询	
	 */
		StringBuilder hqls = new StringBuilder();		
		/**用hqls用来做group by 分组的**/
		 hqls.append(" select a.org_code,a.org_name,substr(a.sign_date,1,7),a.subject_name,count(student_id) rs, ");
		 hqls.append(" sum(a.total_score) total_score,sum(a.claim_score) claim_score,sum(ispk) pkrs,sum(ispass) passrs ");
		 hqls.append(" from 		 ( ");
		 hqls.append(hql.toString());
		 hqls.append("  ) a  ");
		 hqls.append("  group by a.org_code,a.org_name,substr(a.sign_date,1,7),a.subject_name order by  a.org_code ");
		
		// println hqls.toString();
		// println "开始查询 加入合计";
		 SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(hqls.toString());
		// println "查完成";
		List l= query.list();
		
        List<CreditRecordReportTemp> result = new ArrayList<CreditRecordReportTemp>();
		/**
		 * 加入每个月的汇总 判断当前的跟上一个的不一致 直接将上一个汇总 如果当前的是最后一个 则直接将当前的汇总
		 */ 
 
					  boolean is_sum=false;//用于定义是要合计
					 int  studentnumber_all=0;//这一段开始是用来存sum值的
					  int  total_score_all=0;
					  int  claim_score_all=0;
					  int  pkrs_all=0;
					  int  nopkrs_all=0;
					  int  passrs_all=0;
			 	for (int i=0;i<l.size();i++){
					
					 Object[] o=(Object[])l.get(i);
					String org_code=o[0]==null?null:o[0].toString();
					 String org_name=o[1]==null?null:o[1].toString();
					 String  sign_date=o[2]==null?null:o[2].toString();
					  String  subject_name=o[3]==null?null:o[3].toString();
					  int  studentnumber=o[4]==null?0:Integer.valueOf( o[4].toString());
					  int  total_score=o[5]==null?0:Integer.valueOf( o[5].toString());
					  int  claim_score=o[6]==null?0:Integer.valueOf( o[6].toString());
					  int  pkrs=o[7]==null?0:Integer.valueOf( o[7].toString());
					  int  passrs=o[8]==null?0:Integer.valueOf( o[8].toString());
					  
					  int nopkrs=0;
					  if (studentnumber==0){
						  nopkrs=0;
					  }else {
					  nopkrs=studentnumber-pkrs;
					  }
					 // println nopkrs;
					  
					 CreditRecordReportTemp t= new CreditRecordReportTemp();
					 CreditRecordReportTemp allvo= new CreditRecordReportTemp();
					/**当每个合计值叠加数据
					 * **/
					 studentnumber_all=studentnumber_all+studentnumber;
					 total_score_all=total_score_all+total_score;
				     claim_score_all=claim_score_all+claim_score;
					 pkrs_all=pkrs_all+pkrs;
				     passrs_all=passrs_all+passrs;
					 nopkrs_all=nopkrs_all+nopkrs;
					 
					 
					 is_sum=false;
					 
					 
					 if (i+1==l.size()){ //先判断是否是最后一个 是最后一个的话直接合计
						 is_sum=true;
						 allvo.setOrgCode(org_code);
						 allvo.setOrgName(org_name);
						 allvo.setSignDate(sign_date);
						 allvo.setSubjectName("合计");
						 allvo.setIssum(1);
						 allvo.setPassrate ("0.00%");
						 allvo.setPaikerate   ("0.00%");
						 allvo.setCreditcompletionrate  ("0.00%");
						 allvo.setStudentnumber(studentnumber_all);
						 allvo.setTotalScore(total_score_all);
						 allvo.setClaimScore(claim_score_all);
						 allvo.setPkrs(pkrs_all);
						 allvo.setNopkrs(nopkrs_all);
						 allvo.setPassrs(passrs_all);
						 allvo.setPassrate(getPercentString(passrs_all,studentnumber_all));
						 allvo.setPaikerate (getPercentString(pkrs_all,studentnumber_all));
						allvo.setCreditcompletionrate (getPercentString(total_score_all,claim_score_all));
					   
						 studentnumber_all=0;
						 total_score_all=0;
					   claim_score_all=0;
						pkrs_all=0;
						nopkrs_all=0;
					   passrs_all=0;
						// note="最后一个要合计";
					 }else if ( l.get(i+1)!=null){ //还能取到下一个
					
					 Object[] n_o=(Object[])l.get(i+1);
					 String org_code_next=n_o[0]==null?null:n_o[0].toString();
					 String org_name_next=n_o[1]==null?null:n_o[1].toString();
					 String  sign_date_next=n_o[2]==null?null:n_o[2].toString();
					 String key=org_code_next+org_name_next+sign_date_next;
					 String nkey=org_code+org_name+sign_date;
					 if (!key.equals(nkey)){//取到下一个但是校区 校区编号 月份不一致时 要合计
						 is_sum=true;
						 allvo.setOrgCode(org_code);
						 allvo.setOrgName(org_name);
						 allvo.setSignDate(sign_date);
						 allvo.setSubjectName("合计");
						 allvo.setIssum(1);
						 allvo.setPassrate ("0.00%");
						 allvo.setPaikerate   ("0.00%");
						 allvo.setCreditcompletionrate  ("0.00%");
						 allvo.setStudentnumber(studentnumber_all);
						 allvo.setTotalScore(total_score_all);
						 allvo.setClaimScore(claim_score_all);
						 allvo.setPkrs(pkrs_all);
						 allvo.setNopkrs(nopkrs_all);
						 allvo.setPassrs(passrs_all);
						 allvo.setPassrate(getPercentString(passrs_all,studentnumber_all));
						 allvo.setPaikerate (getPercentString(pkrs_all,studentnumber_all));
						allvo.setCreditcompletionrate (getPercentString(total_score_all,claim_score_all));
				
						 studentnumber_all=0;
						 total_score_all=0;
					   claim_score_all=0;
						pkrs_all=0;
						nopkrs_all=0;
					   passrs_all=0;
					 }
					 

					 }  
					 
				 
					 
					 
					  t.setOrgCode(org_code);
					  t.setOrgName(org_name);
					  t.setSignDate(sign_date);
					  t.setSubjectName(subject_name);
					  t.setStudentnumber(studentnumber);
					  t.setTotalScore(total_score);
					  t.setClaimScore(claim_score);
					  t.setPkrs(pkrs);
					  t.setIssum(0);
					  t.setNopkrs(nopkrs);
					  t.setPassrs(passrs);
					  
					  t.setPassrate(getPercentString(passrs,studentnumber));
					  t.setPaikerate (getPercentString(pkrs,studentnumber));
					  t.setCreditcompletionrate (getPercentString(total_score,claim_score));
				 
					  result.add(t);
					   
					   if (is_sum){
						   if (allvo.getOrgName()!=null&&allvo.getIssum()==1){
							 
							   result.add(allvo);
						   }
						 
					  } 

				 }	
		 
				 //int allcount =l.size();
				 int allcount=result.size();
				 query.setFirstResult((page - 1)* pageSize).setMaxResults(pageSize);
		
		int allpage = allcount / pageSize + allcount% pageSize >0 ? 1 : 0;
	
		println "列表运算完成"+DateUtil.DateToString( new Date());
//		
//		//这里要过滤掉重复的值
//		Map<String,CreditRecord> newmap = new HashMap<String,CreditRecord>();
//		//过滤掉重复【学员主键+科目主键】数据,如果是重复科目按照最新报名表显示
//		for(int i = 0; i< result.size();i++){
//			CreditRecord vo = result.get(i);
//			//添加应修总分合计
//			Integer claimtotal = vo.getAttendanceClaimScore()+vo.getWorkClaimScore()+vo.getExamClaimScore();
//			vo.setClaimScore(claimtotal);
//			String stubject_id = vo.getNcSubjectId();
//			String student_id  = vo.getNcUserId();
//			String key = student_id +"@"+stubject_id;
//			if(newmap.get(key) ==null){
//				newmap.put(key, vo);
//			}else{
//				//重复科目，存报名表最新的班型
//				CreditRecord old = newmap.get(key);
//				if(vo.getSignDate().compareTo(old.getSignDate()) > 0){
//					newmap.put(key,vo);
//				}
//			}
//		}
		
		/*//添加老师名称
		addTeacher(result);
		//添加大区名称
		*/
		 
		addLagerArea(result);
		 
		
		Map map = new HashMap();
		map.put("code", 1);
		map.put("msg", "success");
		map.put("count",allcount);
//		map.put("data", newmap.values().toList());
//		map.put("data",query.list())
		map.put("data", result) 
		map.put("allPage",allpage);
		println "装载完成";
		return map;

	}
	 
	
	/**
	 * 添加大区名称
	 * @param vos
	 * @param table
	 * @return
	 */
	def addLagerArea(List<CreditRecordReportTemp> vos){
		List<String> codes = new ArrayList<String>();
		HashSet<String> set = new HashSet<String>();//创建一个set用来去重复
		for(int i = 0 ; i<vos.size; i++){
			CreditRecordReportTemp vo = vos.get(i);
			//根据校区编码获取省份编码
			String prvinceCcode  = vo.getOrgCode() == null || vo.getOrgCode().length() < 5 ? null:vo.getOrgCode().substring(0,5);
			set.add(prvinceCcode);
		}
		codes.addAll(set);
		def query = QueryBuilder.start()
			query.and("code").in(codes);
			//获取对应省份的信息
		def result = areasTable().find(query.get(),$$("parent_nc_id":1,"nc_id":1,"code":1,"_id":0));
		List<Map> resultVOs = result.toList();
		Map<String,String> prvinceMap = new HashMap<String,String>();
		for (int i = 0;i < resultVOs.size();i++) {
			Map m = resultVOs.get(i);
			String code = m.get("code"); 
			String pid = m.get("parent_nc_id");
			prvinceMap.put(code, pid);
		}
		
		Map<String,String> largeAreaMap = new HashMap<String,String>();
		for(int i = 0 ; i< vos.size; i++){
			CreditRecordReportTemp vo = vos.get(i);
			//根据校区编码获取省份编码
			String prvinceCode =vo.getOrgCode()==null ||vo.getOrgCode().length()< 5 ?null:vo.getOrgCode().substring(0,5);
			String  pid  =prvinceMap.get(prvinceCode);
			def q = QueryBuilder.start()
			q.and("nc_id").is(pid);
			if(largeAreaMap.get(pid) == null){
				def largeArea = areasTable().findOne(q.get(),$$("name":1,"_id":0));
				if(largeArea != null){
					largeAreaMap.put(pid, largeArea.get("name"));
					vo.setLargeAreaName(largeArea.get("name"));
				}
			}else{
				vo.setLargeAreaName(largeAreaMap.get(pid));
			}
		}
	}
	
	
	/**
	 * 获取参数id下的所有子id
	 * @param parent_nc_id
	 * @param orgs
	 * @return
	 */
	def getAllChildId(String parent_nc_id,List<String> orgs){
		def query2 = QueryBuilder.start()
		query2.and("parent_nc_id").is(parent_nc_id);
		def result = areasTable().find(query2.get(),$$("name":1,"nc_id":1,"code":1,"_id":0)).toList();
		if(result == null){
			return;
		}
		for (int i = 0;i < result.size();i++) {
			Map m = result.get(i);
			orgs.add(m.get("nc_id"))
			getAllChildId(m.get("nc_id"),orgs);
		}
	}
	/**
	 * x/y的百分比
	 * @param x
	 * @param y
	 * @return
	 */
	def String getPercentString(int x,int y){
		if (x==0||y==0){
			return "0.00%";
		}else{
			 double parate = (x/y)*100;
			  DecimalFormat df=new DecimalFormat("0.00");
			 return   (df.format(parate)+"%");
		}
	}
	
}
