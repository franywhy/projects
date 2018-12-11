package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import net.sf.json.JSONArray
import net.sf.json.JSONObject

import java.text.DecimalFormat;





import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern

import org.hibernate.criterion.Order

import javax.annotation.Resource

import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils
import org.hibernate.Query
import org.hibernate.SessionFactory
import org.springframework.web.bind.ServletRequestUtils
import org.hibernate.Criteria
 









import com.izhubo.rest.anno.RestWithSession
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
import com.mysqldb.model.CreditDef
import com.mysqldb.model.CreditPerMon
import com.mysqldb.model.CreditPercentDetail
import com.mysqldb.model.CreditQueryPermission
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordSign
import com.mysqldb.model.CreditStandard;
import com.izhubo.credit.util.CheckParameter;
import com.izhubo.credit.util.CreditUtil 
import com.izhubo.credit.util.CreditDaoUtil
import com.izhubo.credit.util.doubleUtilBase;
import com.mysqldb.model.Creditpercent

/**
 * 学分完成率报表
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class CreditpercentController extends BaseController {
 

	@Resource
	private SessionFactory sessionFactory;
 
	DBCollection teachersTable(){
		return mainMongo.getCollection('teachers');
	}
	DBCollection areasTable(){
		return mainMongo.getCollection('area');
	}

  /**
   * 界面上查询当前的月份的完成率
   * @param req
   * @return
   */
	def query_permon(HttpServletRequest req){
		 
		def dbilldate = req.getParameter("dbilldate");
		//检测月份是否符合
		if (!CheckParameter.checkTimeFormatByMonths(dbilldate) ){
			List<Creditpercent> t = new ArrayList<Creditpercent> ();
			Creditpercent sub= new Creditpercent();
			sub.setLargeAreaName("当前月份为空，无法查询，请重新输入。");
			t.add(sub);
			Map map = new HashMap();
			map.put("code", 1);
			map.put("msg", "success");
			map.put("count",1);
			map.put("data", t);
			map.put("allPage",1);
			return map;
		}
	 
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CreditPerMon.class);
		
		criteria.add(Restrictions.sqlRestriction(" dr=0 "));
		if(StringUtils.isNotBlank(dbilldate)){
			criteria.add(Restrictions.eq("dbilldate", dbilldate));
		}
		List<CreditPerMon> t=criteria.list();
	                    
		 
		Map map = new HashMap();
		map.put("code", 1);
		map.put("msg", "success");
		map.put("data", t);
		return map;
		
		 
		
	}
	/**
	 * 按钮弹框显示不考核的科目
	 * @param req
	 * @return
	 */
	def querydefnot(HttpServletRequest req){
		//def dbilldate = req.getParameter("dbilldate");
		//检测月份是否符合
		 
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CreditDef.class);
		 
	     	criteria.add(Restrictions.eq("dr", new Integer(0)));
			 criteria.add(Restrictions.eq("defList", "DEF201711180162649104479406"));
			  
		  List<CreditDef> t=criteria.list();
		 
		 Map map = new HashMap();
		map.put("code", 1);
		map.put("msg", "success");
		map.put("data", t);
		return map; 
		
		/*Map map = new HashMap();
		map["code"]  = 1;
		map["data"]  = t;
		map["all_page"]  = 1;
		map["count"]  = t.size();
		//println "开始查询不用考核的科目"+map.size();*/
		
	}
	
	
	
	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		//int pageSize = ServletRequestUtils.getIntParameter(req, "size", 5000);
	//	int page = ServletRequestUtils.getIntParameter(req, "page", 1);
		def orgName = req.getParameter("orgName");
 
		String dbilldate = req.getParameter("dbilldate");
		String bSignDate = req.getParameter("beginSignDate");
		String eSignDate = req.getParameter("endSignDate");
		String largeAreaName = req.getParameter("largeAreaName");
		//检测当前月份是否为空
		if (!CheckParameter.checkTimeFormatByMonths(dbilldate)||CheckParameter.checkString(dbilldate)){
			Map map = new HashMap();
			map.put("code", 1);
			map.put("msg", "success");
			map.put("count",0);
			map.put("data", new ArrayList());
			map.put("allPage",0);
			return map;
		}
	//	//println "检测完当月";
		
		
		
		//检测月份是否符合
		if (!CheckParameter.checkTimeFormatByMonths(bSignDate)||!CheckParameter.checkTimeFormatByMonths(eSignDate)){
			Map map = new HashMap();
			map.put("code", 1);
			map.put("msg", "success");
			map.put("count",0);
			map.put("data", new ArrayList());
			map.put("allPage",0);
			return map;
		}
		
		
		////println "检测完报名时间";
		
		
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Creditpercent.class);
		//总条数-分页
		Criteria criterion_count = sessionFactory.getCurrentSession().createCriteria(Creditpercent.class).setProjection(Projections.rowCount());
		 
		
		////println "开始添加enable的sql"
 
		criteria.add(Restrictions.sqlRestriction(" dr=0 "));
		criterion_count.add(Restrictions.sqlRestriction(" dr=0 "));
		 //	//println "开始添加enable的sql结束"
		if(StringUtils.isNotBlank(orgName)){
			criteria.add(Restrictions.like("orgName","%"+orgName+"%"));
			criterion_count.add(Restrictions.like("orgName","%"+orgName+"%"));
		}
//加上当前月份过滤 
		if(StringUtils.isNotBlank(dbilldate)){
			criteria.add(Restrictions.eq("dbilldate",dbilldate));
			criterion_count.add(Restrictions.eq("dbilldate",dbilldate));
		}
		
		
		
		
		
		if(StringUtils.isNotBlank(largeAreaName)){
			criteria.add(Restrictions.like("largeAreaName","%"+largeAreaName+"%"));
			criterion_count.add(Restrictions.like("largeAreaName","%"+largeAreaName+"%"));
		}
		 
		
            if(StringUtils.isNotBlank(bSignDate)) {
			//查询制定时间之后的记录
			criteria.add(Restrictions.ge("months",bSignDate));
			criterion_count.add(Restrictions.ge("months",bSignDate));
		}
		if(StringUtils.isNotBlank(eSignDate)){
			//查询指定时间之前的记录
			criteria.add(Restrictions.le("months",eSignDate));
			criterion_count.add(Restrictions.le("months",eSignDate));
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
		
		//添加校区的权限
		if(orgs !=null && orgs.size() > 0){
			criteria.add(Restrictions.in("orgId",orgs));
			criterion_count.add(Restrictions.in("orgId",orgs));
		}else if(!superAdmin){
			//没有权限
			Map map = new HashMap();
			map.put("code", 1);
			map.put("msg", "success");
			map.put("count",0);
			map.put("data", new ArrayList());
			map.put("allPage",0);
			return map;
		}else {
			//没有权限
			Map map = new HashMap();
			map.put("code", 1);
			map.put("msg", "success");
			map.put("count",0);
			map.put("data", new ArrayList());
			map.put("allPage",0);
			return map;
		}

		//添加大区过滤
		boolean isNull = false;
		if(StringUtils.isNotBlank(largeAreaName)){
			List<String> largeOrgs  = new ArrayList<String>();
			//获取大区对应的所有校区
			def query = QueryBuilder.start()
			query.and("name").is(largeAreaName);
			def largeArea = areasTable().findOne(query.get(),$$("name":1,"code":1,"nc_id":1,"_id":0));
			if(largeArea == null){
				isNull = true;
			}else{
				String parent_nc_id = largeArea.get("nc_id");
				getAllChildId(parent_nc_id,largeOrgs);
				if(largeOrgs !=null && largeOrgs.size() > 0){
					criteria.add(Restrictions.in("orgId",largeOrgs));
					criterion_count.add(Restrictions.in("orgId",largeOrgs));
				}else{
					isNull = true;
				}
			}
		}

		if(isNull){
			Map map = new HashMap();
			map.put("code", 1);
			map.put("msg", "success");
			map.put("count",0);
			map.put("data", new ArrayList());
			map.put("allPage",0);
			return map;
		}
		 
	//.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize) 取消了页面	
			  
		
	 	 List<Creditpercent> result  = 
		  criteria.addOrder(Order.asc("largeAreaName")).addOrder(Order.asc("orgCode"))	 	//添加排序
		.list();
	 //	//println "排序取数完成" +result.size()+"  ";
		 
		if (result==null||result.size()==0){
			Map map = new HashMap();
			map.put("code", 1);
			map.put("msg", "success");
			map.put("count",0);
			map.put("data", new ArrayList());
			map.put("allPage",0);
			return map;
		}
		
		
		
		
		/*
		Map<String,Double>  PerMon=new HashMap<String,Double> ();
		 
		
		String qslq = " from CreditPerMon where dr =0 and dbilldate='2017-11' and months >= ? and months<=?";
		Query qs = sessionFactory.getCurrentSession().createQuery(qslq);
 
		qs.setString(0, bSignDate);
		qs.setString(1, eSignDate);
		List<CreditPerMon> pvo = qs.list();
		//println "pvo"+pvo.size();
		
		if (pvo!=null&&pvo.size()>0){
			for (CreditPerMon p:pvo){
				PerMon.put(p.getMonths(), p.getMbpercent().doubleValue());
			}
		}
		
		 
	//	 CreditUtil.getCreditPerMon( sessionFactory.getCurrentSession(), bSignDate, eSignDate,"2017-11")
		
		//println "PerMon完成" */
		
		
		
		
		
		
		
	  
	
		
		
 
		//添加大区名称
		//addLagerArea(result);
	//	//println "添加大区完成" 
		List<Creditpercent> t= addSumResult(result);
     //	//println "合计完成"+t.size(); 
		
		 
		//int sign_count =t.size();
	 
		//int allpage = sign_count / pageSize + sign_count% pageSize >0 ? 1 : 0;
		 
		  
		
		Map map = new HashMap();
		map.put("code", 1);
		map.put("msg", "success");
		//map.put("count",sign_count);
		map.put("data", t);
		//map.put("allPage",allpage);
		return map;

	}
	
	/**
	 * 查看明细
	 * 
	 */
	def query_detail(HttpServletRequest req){
		 
		String pid = req.getParameter("pid");
		String orgId = req.getParameter("orgid");
		 String months = req.getParameter("months");
		  String dbilldate = req.getParameter("dbilldate");
		//println "pid="+pid;
		//println "orgId="+orgId;
		//println "months="+months;
		//println "dbilldate="+dbilldate;
		Query q  = sessionFactory.getCurrentSession().createQuery(
			" from Creditpercent  where dr=0 and "+
			" pid = ? "+
			" and orgId = ?  "+
			" and months = ? "+
			" and dbilldate= ?");
			 
		q.setString(0, pid);
		q.setString(1, orgId);
		q.setString(2, months);
		q.setString(3, dbilldate); 
		List<Creditpercent> result=q.list();
		
		if (result!=null&&result.size()>0){
			for (int i=0;i<result.size();i++){
				result.get(i).setRemark(result.get(i).getRemark().replace("%", ""));
				result.get(i).setPkremark(result.get(i).getPkremark().replace("%", ""));
			}
		}
		
		
		
		return getResultOK(q.list());
	}
	
	
	/**
	 * 查看明细
	 *
	 */
	def show(HttpServletRequest req){
		 
		String pid = req.getParameter("pid");
		String orgId = req.getParameter("orgid");
		 String months = req.getParameter("months");
		  String dbilldate = req.getParameter("dbilldate");
		  String menukey=req.getParameter("a");
		//println "pid="+pid;
		//println "orgId="+orgId;
		//println "months="+months;
		//println "dbilldate="+dbilldate;
		//println "a="+menukey;
		Query q  = sessionFactory.getCurrentSession().createQuery(
			" from CreditPercentDetail  where dr=0 and "+
			" pid = ? "+
			" and orgId = ?  "+
			" and months = ? "+
			" and dbilldate= ?");
			 
		q.setString(0, pid);
		q.setString(1, orgId);
		q.setString(2, months);
		q.setString(3, dbilldate);
		 //开始添加老师名称 和报读班型 和学员名称
		
		
	List<CreditPercentDetail> result=	q.list()
	
	////println "查到"+result.size()+"条。";
		addTeacher(result);
	//	//println "添加老师完成";
		addStudentname(result,menukey);
	//	//println "添加学员名称完成";
		
		 
		return getResultOK(result);
	}
	
	def exportrecord(HttpServletRequest req){
		 
	/*	String beginDate = req.getParameter("beginDate");
		 String endDate = req.getParameter("endDate");
		 
	 
		Query q  = sessionFactory.getCurrentSession().createQuery(
			" from CreditRecord  where enable=0 and "+
			" and signDate >= ? "+
			" and signDate < = ?  " 
		 );
			 
		q.setString(0, beginDate);
		q.setString(1, endDate);
	 
		 //开始添加老师名称 和报读班型 和学员名称
		
		
	List<CreditPercentDetail> result=	q.list()
	
	//println "查到"+result.size()+"条。";
		addTeacher(result);
		//println "添加老师完成";
		addStudentname(result,menukey);
		//println "添加学员名称完成";
		
		 
		return getResultOK(result);
		*/
		
		
	}
	
	
	
	
	
	
	
	
	def setpercentString(double d){
		if (d==null){
			return "0.00%";
		}else {
		BigDecimal p=new BigDecimal(d);
	 
		DecimalFormat df=new DecimalFormat("0.00");
	   return   (df.format(p*100)+"%");
		}
		
	}
	
	
	
	
	/**
	 * 添加大区名称
	 * @param vos
	 * @param table
	 * @return
	 */
	def addLagerArea(List<Creditpercent> vos){
		List<String> codes = new ArrayList<String>();
		HashSet<String> set = new HashSet<String>();//创建一个set用来去重复
		//println "开始取得省份id";
		for(int i = 0 ; i<vos.size; i++){
			Creditpercent vo = vos.get(i);
			//根据校区编码获取省份编码
			String prvinceCcode  = vo.getOrgCode() == null || vo.getOrgCode().length() < 5 ? null:vo.getOrgCode().substring(0,5);
			
			set.add(prvinceCcode);
		}
		codes.addAll(set);
		//println codes.size();
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
			Creditpercent vo = vos.get(i);
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
	 * 大区合计和总和
	 * @param vos
	 * @param PerMon 年月目标完成率
	 * @return
	 */
	def List<Creditpercent> addSumResult(List<Creditpercent> vos ){
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
		List<Creditpercent> result= new ArrayList<Creditpercent>();
	//	//println "完成0"+vos.size();
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
				String pkpercent=t.getPkpercent()==null?"0.00%":t.getPkpercent()
				
				String mbscore=t.getMbscore()==null?"0":t.getMbscore();
				String mbnum=t.getMbnum()==null?"0":t.getMbnum();
				
				String remark=t.getRemark()==null?"错误":t.getRemark();
				String pkremark=t.getPkremark()==null?"错误":t.getPkremark();
		//		//println "每项取数完成";
				 
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
				 
				  
				  
				  
				  
				//  //println "每项叠加完成";
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
					
					
					sum.setXfPercent( setpercentString( doubleUtilBase.div(areaSumtotal, (double)areaSumclaim, 4)) );
					sum.setPkpercent(setpercentString( doubleUtilBase.div(areaSumtotal_pk, (double)areaSumclaim_pk, 4)) )
					//总的目标完成率就等于全部的目标分数除以总的应修的分数areaSummb/areaSumclaim
			 	     sum.setMbpercent(setpercentString( doubleUtilBase.div(areaSummb, (double)areaSumclaim, 4) ));
				     sum.setPkmbpercent(setpercentString( doubleUtilBase.div(areaSummb_pk, (double)areaSumclaim_pk, 4) ))
					  
					  
					allsum.setLargeAreaName("全国校区");
					allsum.setOrgName("全国合计");
					allsum.setMonths("-");
					allsum.setClaimScore(allSumclaim);
					allsum.setTotalscore(allSumtotal); 
					allsum.setClaimNum(allSumclaim_pk);
					allsum.setTotalNum(allSumtotal_pk);
					
					
					allsum.setXfPercent( setpercentString( doubleUtilBase.div(allSumtotal, (double)allSumclaim, 4)) );
				    allsum.setPkpercent(setpercentString(doubleUtilBase.div(allSummb_pk, (double)allSumclaim_pk, 4)) );
					//总的目标完成率就等于全部的目标分数除以总的应修的分数areaSummb/areaSumclaim
					allsum.setMbpercent(setpercentString( doubleUtilBase.div(allSummb, (double)allSumclaim, 4) ));
				    allsum.setPkmbpercent(setpercentString( doubleUtilBase.div(allSummb_pk, (double)allSumclaim_pk, 4) ))
				
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
					    sum.setXfPercent( setpercentString( doubleUtilBase.div(areaSumtotal, (double)areaSumclaim, 4)) );
					    sum.setMbpercent(setpercentString(doubleUtilBase.div(areaSummb, (double)areaSumclaim, 4)) );
						sum.setPkpercent(setpercentString( doubleUtilBase.div(areaSumtotal_pk, (double)areaSumclaim_pk, 4)) )
					    sum.setPkmbpercent(setpercentString( doubleUtilBase.div(areaSummb_pk, (double)areaSumclaim_pk, 4) ))
						  
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
	/**
	 * 添加老师名称
	 * @param vos
	 */
	def  addTeacher(List<CreditPercentDetail> vos){
		List<String> ids = new ArrayList<String>();
		HashSet<String> set = new HashSet<String>();//创建一个set用来去重复
		for(int i = 0 ; i<vos.size; i++){
			CreditPercentDetail vo = vos.get(i);
			set.add(vo.getTeacherId());
		}
		ids.addAll(set);
		//println "查到ids"+ids.size();
		def query = QueryBuilder.start()
			query.and("nc_id").in(ids);
		def result = teachersTable().find(query.get(),$$("telephone":1,"name":1,"nc_id":1,"_id":0));
		List<Map> resultVOs = result.toList();
		Map<String,String> teacherMap = new HashMap<String,String>();
		for (int i = 0;i < resultVOs.size();i++) {
			Map m = resultVOs.get(i);
			String name = m.get("name");
			String id = m.get("nc_id");
			teacherMap.put(id, name);
		}
		//println "查到teacherMap"+teacherMap.size();
		for(int i = 0 ; i<vos.size; i++){
			CreditPercentDetail vo = vos.get(i);
			String teacherId = vo.getTeacherId();
			vo.setTeacherName(teacherMap.get(teacherId)==null?"":teacherMap.get(teacherId));
		}
		
	}
	/**
	 * 添加学员名称 和报读班型 
	 * @param vos
	 * @return
	 */
	def  addStudentname(List<CreditPercentDetail> vos,String menukey){
		Map<String,String> hidmap=new HashMap<String,String>();
		  
		Map<String,CreditRecordSign> stuvo=new HashMap<String,CreditRecordSign>();
		Map<String,CreditStandard> creditStandardmap= new HashMap<String,CreditStandard>();
		
		//先取得报名表的hid 
		for (CreditPercentDetail d:vos){
			 if (d!=null&&d.getsignID()!=null){
				 hidmap.put(d.getsignID(), "");
			 }
			 
		}
		
		if (hidmap!=null&&hidmap.size()>0){
			
			List<String> hidlist= new ArrayList<String>(hidmap.keySet());		
			//println "开始查hid"+hidlist.size();
		   List<CreditRecordSign> svo=CreditDaoUtil.getLocalDateSign_h(sessionFactory.getCurrentSession(), hidlist, 1000);
		   //println "查到"+svo.size();
		   if (svo!=null&&svo.size()>0){
				for (CreditRecordSign temp:svo){
					if (temp!=null&&temp.getSignId()!=null){
						stuvo.put(temp.getSignId(), temp);
					}
				}
			}
				
			  List<CreditStandard> st = sessionFactory.getCurrentSession().createQuery("from CreditStandard").list();
			  //println "1"
			   for(CreditStandard s:st){
				   creditStandardmap.put(s.getNc_id(), s);
			   }
			 
		
		
		}
		CreditPercentDetail allsum= new CreditPercentDetail();
		allsum.setSequenceName("合计");
		allsum.setStudentName("-");
		allsum.setSubjectName("-");
		int claimScore=0;
		int totalScore=0;
		int att_c=0;
		int att_t=0;
		int work_c=0;
		int work_t=0;
		int exam_c=0;
		int exam_t=0;
		int paike_c=0;
		int paike_t=0;
		for (int x=0;x<vos.size();x++){
			 
			CreditPercentDetail vo = vos.get(x);
			paike_c=paike_c+1;
			  claimScore+=vo.getClaimScore();
			  totalScore+=vo.getTotalScore();
			att_t+=vo.getAttendanceActualScore();
			att_c+=vo.getAttendanceClaimScore();
			work_t+=vo.getWorkActualScore();
			work_c+=vo.getWorkClaimScore();
			exam_t+=vo.getExamActualScore();
			exam_c+=vo.getExamClaimScore();
			paike_t+=vo.getIsPaike();
			String hid = vo.getsignID();
			
			String subjectid = vo.getNcSubjectId();
			CreditRecordSign s=stuvo.get(hid);
			CreditStandard st=creditStandardmap.get(subjectid)
			
			if (s!=null){//从订单中来的学员名称和报读班型
				 vos.get(x).setStudentName(s.getStudentName());
				 vos.get(x).setSequenceName(s.getClassName());
				 vos.get(x).setPhone(s.getPhone());
			}
			if (st!=null){//从学分科目标准中来的科目
				 vos.get(x).setSubjectName(st.getSubject_name());
				 vos.get(x).setSubjectCode(st.getCourse_code());
			}
			if (vo.getClassName()==null){
				vos.get(x).setPaikeRemark("未排课");
				vos.get(x).setClassName("");
			}else {
			     vos.get(x).setPaikeRemark("已排课");
			}
			if (vo.getIsPass()==0){
				vos.get(x).setPassRemark("是");
			}else {
			    vos.get(x).setPassRemark("否");
			}
			
			
			//判断是排课完成率的卡片的 就赋是否 排课
			if ("creditpaikecard".equals(menukey)){
				vos.get(x).setTotalScore(vo.getIsPaike());
				vos.get(x).setClaimScore(1);
			}
		}
		allsum.setAttendanceActualScore(att_t) ;
		allsum.setAttendanceClaimScore(att_c);
		allsum.setWorkActualScore(work_t);
		allsum.setWorkClaimScore(work_c);
		allsum.setExamActualScore(exam_t);
		allsum.setExamClaimScore(work_c);
		allsum.setTotalScore(totalScore);
		allsum.setClaimScore(claimScore);
		allsum.setTeacherName("-");
		allsum.setClassName("-");
		if (claimScore==totalScore){
			allsum.setIsPass(0);
		}
		allsum.setIsPaike(0);
		
		
		if ("creditpaikecard".equals(menukey)){
			allsum.setTotalScore(paike_t);
			allsum.setClaimScore(paike_c);
			 
			allsum.setSequenceName("-");
			allsum.setStudentName("-");
			allsum.setSubjectName("-");
			allsum.setClassName ("-");
			allsum.setTeacherName("合计");
			 
		}
		vos.add(allsum);
	
	}
}
