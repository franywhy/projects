package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.util.List;
import java.util.Map.Entry
import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils
import org.hibernate.Criteria
import org.hibernate.Query
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.quartz.SchedulerException;
import org.springframework.web.bind.ServletRequestUtils

import com.izhubo.rest.anno.RestWithSession
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder
import com.mysqldb.model.CreditQueryPermission
import com.mysqldb.model.CreditRecord
import org.hibernate.Criteria
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.hibernate.criterion.MatchMode;
/**
 * 学分记录——教师列表
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class CreditrecordteacherController extends BaseController {
 

	@Resource
	private SessionFactory sessionFactory;
	
	DBCollection teachersTable(){
		return mainMongo.getCollection('teachers');
	}
	
	DBCollection areasTable(){
		return mainMongo.getCollection('area');
	}

	/**
	 * 查询
	 */
	def querytest(){
		 
		}
	
	
	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
	 	int pageSize = ServletRequestUtils.getIntParameter(req, "size", 20);
		int page = ServletRequestUtils.getIntParameter(req, "page", 1);
		def orgName = req.getParameter("orgName");
		String subjectName = req.getParameter("subjectName");
		String className = req.getParameter("className");
		String studentName = req.getParameter("studentName");
		def phone = req.getParameter("phone");
		def teacherName = req.getParameter("teacherName");
		
		String bSignDate = req.getParameter("beginSignDate");
		String eSignDate = req.getParameter("endSignDate");
		String largeAreaName = req.getParameter("largeAreaName"); 
		 
		/**
		 * 优化几点：
		 * 1.当选择了校区又选择了大区的话 大区可以不起作用的了
		 * 
		 * 2.如果是教学老师的 默认是只能查自己  如果是经理级的默认不查自己 
		 */
		 
		
		
		//用户输入的校区名称后转换得到的pk_org
		Map<String,String> org_input= new HashMap<String,String>();
		//可以查的校区
		List<String> orgs_allow  = new ArrayList<String>();
		//用户输入的老师id
		List<String>  teacherid_input= new ArrayList<String>();
		
		 
		 
		Criteria criteria_hql = sessionFactory.getCurrentSession().createCriteria(CreditRecord.class);
		Criteria criteria_counthql = sessionFactory.getCurrentSession().createCriteria(CreditRecord.class);
		//println "开始查询"
		criteria_hql.add(Restrictions.eq("isEnable", new Integer(0)));
	   criteria_counthql.add(Restrictions.eq("isEnable", new Integer(0)));
			
	    if(StringUtils.isNotBlank(bSignDate)) {
		 //println "取得开始时间"+bSignDate;
			criteria_hql.add(Restrictions.ge("signDate", bSignDate  ));
			criteria_counthql.add(Restrictions.ge("signDate", bSignDate  ));
		}
		if(StringUtils.isNotBlank(eSignDate)){
			//println "取得结束时间"+eSignDate;
		 
			criteria_hql.add(Restrictions.le("signDate", eSignDate  ));
			criteria_counthql.add(Restrictions.le("signDate", eSignDate ));
		}
		
	 
		if(StringUtils.isNotBlank(studentName)){
			//println "取得学员名称"+studentName;
			criteria_hql.add(Restrictions.like("studentName", studentName, MatchMode.ANYWHERE));
			criteria_counthql.add(Restrictions.like("studentName", studentName, MatchMode.ANYWHERE));
			}
		if(StringUtils.isNotBlank(orgName)){
			
			 Map<String, String> orgs=getOrgNamekey();
			//orglist.add("Null_Check");//用于非空
			 for( Entry<String,String>  named :orgs.entrySet()){
				 if (named!=null&&named.getValue()!=null&&named.getKey().contains(orgName)){
					
					  org_input.put(named.getValue(),named.getValue());
				 }
			 }
			 //println "取得校区in 到下面再in "+org_input.size();
			/* 
			 criteria_hql.add(Restrictions.in("orgId", orglist));
			 criteria_counthql.add(Restrictions.in("orgId", orglist));
			 */
		}
		if(StringUtils.isNotBlank(className)){
			//println "取得班级名"+className;
			criteria_hql.add(Restrictions.like("arrName", className,MatchMode.ANYWHERE ));
			criteria_counthql.add(Restrictions.like("arrName",className,MatchMode.ANYWHERE ));
		
		}
		if(StringUtils.isNotBlank(subjectName)){
			//println "科目名称 "+subjectName;
			criteria_hql.add(Restrictions.like("subjectName", subjectName,MatchMode.ANYWHERE ));
			criteria_counthql.add(Restrictions.like("subjectName",  subjectName,MatchMode.ANYWHERE ));
		}
		if(StringUtils.isNotBlank(phone)){
			 
			//println "电话 "+phone;
			criteria_hql.add(Restrictions.like("phone",phone,MatchMode.ANYWHERE ));
			criteria_counthql.add(Restrictions.like("phone",phone,MatchMode.ANYWHERE ));
		}
		
	 
		
		
		if(StringUtils.isNotBlank(teacherName)){
			//根据老师名字获取老师id
			def query = Web.fillTimeBetween(req);
			Pattern pattern = Pattern.compile("^.*" + teacherName + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("name").regex(pattern)
			def result = teachersTable().find(query.get(),$$("telephone":1,"name":1,"nc_id":1,"_id":0)).toArray();
			List<Map> vos = result.toList();
			if(vos !=null && vos.size()>0){ 	 
				for (int i = 0;i < vos.size();i++) {
					Map m = vos.get(i);
					 
					teacherid_input.add( m.get("nc_id") );
				}
				 
			}else{
			ReturnMap();
			}
		}
		
		
		
		
		
		//println "2"
 		Map user = (Map) req.getSession().getAttribute("user");
		 getUserOrg(user);
		//根据用户id查询对应的权限-老师
		String phql = " from CreditQueryPermission where userId = ?";
		Query q = sessionFactory.getCurrentSession().createQuery(phql);
		q.setString(0, user.get("_id"));
		List<CreditQueryPermission> vos = q.list();
		List<String> teacherIds  = new ArrayList<String>();
		List<String> orgs  = new ArrayList<String>();
		boolean superAdmin = false; //超级管理员
		boolean manager = false;//经理级
		boolean teacher = false;//普通老师
		
		
		
		
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
		//println "3"
		if(orgs !=null && orgs.size() > 0){ //当取得所管理校区时
			manager=true;
			if(org_input!=null&&org_input.size()>0){//有输入了校区名称并且得到了校区
				
				for (String org_key:orgs){
					if (org_input.get(org_key)!=null){
						orgs_allow.add(org_key);
					}
				}
				if (orgs_allow!=null&&orgs_allow.size()>0){
					 
					
					criteria_hql.add(Restrictions.in("orgId",orgs_allow ));
					criteria_counthql.add(Restrictions.in("orgId",orgs_allow ));
				}else {
				   ReturnMap();
				}
			
			}else { //输入了校区都是没有得到校区的 有可能是输错名称了或者没有输入 直接给全部的结果
			
			 
			criteria_hql.add(Restrictions.in("orgId",orgs ));
			criteria_counthql.add(Restrictions.in("orgId",orgs ));
			
			}
		
			
			
			
		}else if(teacherIds !=null && teacherIds.size() > 0){ //没有管理的校区都是有老师时只取老师的不管校区的
		  
			teacher=true;
		    criteria_hql.add(Restrictions.in("teacherId",teacherIds ));
		    criteria_counthql.add(Restrictions.in("teacherId",teacherIds ));
		
		}else{
		if(!superAdmin){
			Map map = new HashMap();
			map.put("code", 1);
			map.put("msg", "success");
			map.put("count",0);
			map.put("data", new ArrayList());
			map.put("allPage",0);
			return map;
		} else{
	    	if (org_input!=null&&org_input.size()>0){
				
		/*		for (String temp:org_input){
					println " 错误出口："+temp;
				}*/
				criteria_hql.add(Restrictions.in("orgId",org_input.keySet().toList() ));
			criteria_counthql.add(Restrictions.in("orgId",org_input.keySet().toList() ));
	     	}
		}
		}
		//println "4"
		
		if (!teacher){			
			
			//加老师过滤
			
			 if (teacherid_input!=null&&teacherid_input.size()>0){
				 criteria_hql.add(Restrictions.in("teacherId",teacherid_input ));
				 criteria_counthql.add(Restrictions.in("teacherId",teacherid_input ));
			 }
			
		}
		
		
		
		//添加大区过滤
		if(StringUtils.isNotBlank(largeAreaName)&&!manager&&!teacher){
			 
			
			if (org_input!=null&&org_input.size()>0){
				criteria_hql.add(Restrictions.in("orgId",org_input.keySet().toList() ));
				criteria_counthql.add(Restrictions.in("orgId",org_input.keySet().toList() ));
			}else {
			List<String> largeOrgs  = new ArrayList<String>();
			//获取大区对应的所有校区
			def query = QueryBuilder.start()
			query.and("name").is(largeAreaName);
			def largeArea = areasTable().findOne(query.get(),$$("name":1,"code":1,"nc_id":1,"_id":0));
			if(largeArea == null){
			   ReturnMap();
			}else{
				String parent_nc_id = largeArea.get("nc_id");
				getAllChildId(parent_nc_id,largeOrgs);
				if(largeOrgs !=null && largeOrgs.size() > 0){
				 
					
					criteria_hql.add(Restrictions.in("orgId",largeOrgs ));
					criteria_counthql.add(Restrictions.in("orgId",largeOrgs ));
					
					
				}else{
				ReturnMap();
				}
			}
			}
			
			
		} 
		 
		 
	 //	 criteria_hql.add(Restrictions.sqlRestriction(hql.toString()) );
	 //   criteria_counthql.add(Restrictions.sqlRestriction(countHql.toString()));
 
	    criteria_hql.setFirstResult((page - 1)* pageSize).setMaxResults(pageSize);	
	
	 
		List<CreditRecord> result =  criteria_hql.list();
		//println "查询完成";
		
		//封装查询总记录条数
	 
		//Query countquery = sessionFactory.getCurrentSession().createQuery(countHql.toString());
		//println "开始取页数"
 
		 
		criteria_counthql.setProjection(Projections.rowCount());
		int allcount = Integer.valueOf(  criteria_counthql.uniqueResult().toString());
	 
		
		//println "页数取完"
		
		int allpage = allcount / pageSize + allcount% pageSize >0 ? 1 : 0;
	
		 
		//println "开始添加老师名称"
		//添加老师名称
		addTeacher(result);
		//添加大区名称
		addOrgName(result);
		addLagerArea(result);
		
	 
		
		Map map = new HashMap();
		map.put("code", 1);
		map.put("msg", "success");
		map.put("count",allcount);
		map.put("data", result);
		map.put("allPage",allpage);
		return map;

	}
	/**
	 * 添加老师名称
	 * @param vos
	 */
	def  addTeacher(List<CreditRecord> vos){
		List<String> ids = new ArrayList<String>();
		HashSet<String> set = new HashSet<String>();//创建一个set用来去重复
		for(int i = 0 ; i<vos.size; i++){
			CreditRecord vo = vos.get(i);
			set.add(vo.getTeacherId());
		}
		ids.addAll(set);
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
		
		for(int i = 0 ; i<vos.size; i++){
			CreditRecord vo = vos.get(i);
			String teacherId = vo.getTeacherId();
			vo.setTeacherName(teacherMap.get(teacherId));
		}
		
	} 
	
	
	/**
	 * 添加大区名称
	 * @param vos
	 * @param table
	 * @return
	 */
	def addLagerArea(List<CreditRecord> vos){
		List<String> codes = new ArrayList<String>();
		HashSet<String> set = new HashSet<String>();//创建一个set用来去重复
		//println "开始取得省份id";
		for(int i = 0 ; i<vos.size; i++){
			CreditRecord vo = vos.get(i);
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
			CreditRecord vo = vos.get(i);
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
	
	
	 Map<String,Map<String,String>>  addOrgName( List<CreditRecord> vos){
		 
		 def query = QueryBuilder.start()
		 
		 def result = areasTable().find(query.get(),$$("name":1,"code":1,"nc_id":1,"_id":0));
		 
		 
		 Map<String,Map<String,String>> orgMap= new HashMap<String,Map<String,String>>();
	 //<校区org<code,name>>
		 List<Map> resultVOs = result.toList();
		 for (int i=0;i<resultVOs.size();i++){
			 Map m = resultVOs.get(i);
			 Map<String,String> submap=new HashMap<String,String>();
			 String code = m.get("code");
			 String name = m.get("name");
			 String nc_id=m.get("nc_id");
			 submap.put("code", code);
			 submap.put("name", name);
			 orgMap.put(nc_id, submap);
			  
		 } 
		  for (int i=0;i<vos.size();i++){
			  CreditRecord vo=vos.get(i);
			  if (vo.getArrName()==null){
				  vo.setArrName("未排课");
			  }
			  if (vos.get(i).getorgId()!=null){
				 Map<String,String> submap=  orgMap.get(vos.get(i).getorgId());
				 if (submap!=null){
					vo.setOrgName(submap.get("name"));
					vo.setOrgCode(submap.get("code")) ;
				 }
			  }
			  
		  }
		 
		 
		 
		 
		 
		 
		 
	 }
	 
	 Map<String, String > getOrgNamekey( ){
		 
		 def query = QueryBuilder.start()
		 
		 def result = areasTable().find(query.get(),$$("name":1,"code":1,"nc_id":1,"_id":0));
		 //println result.size();
		 
		 Map<String, String > orgMap= new HashMap<String, String >();
	 //<校区org<code,name>>
		 List<Map> resultVOs = result.toList();
		 for (int i=0;i<resultVOs.size();i++){
			 Map m = resultVOs.get(i);
			 
			 
			 String name = m.get("name");
			 String nc_id=m.get("nc_id");
		 
			 orgMap.put(name, nc_id);
			 
		 }
		 return orgMap;
		 
		 
	 }
	 /**
	  * 取得老师所管的校区org
	  * @return
	  */
	 Map<String, String > getUserOrg( Map user ){
		 
		 Map<String, String > own=new HashMap<String, String >()
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
		 //println "3"
		 if(orgs !=null && orgs.size() > 0){
			 
		 }else if(teacherIds !=null && teacherIds.size() > 0){
			 
		 
		 }else if(!superAdmin){
			 Map map = new HashMap();
			 map.put("code", 1);
			 map.put("msg", "success");
			 map.put("count",0);
			 map.put("data", new ArrayList());
			 map.put("allPage",0);
			 return map;
		 }
	 }
	 
	 def ReturnMap(){
		 Map map = new HashMap();
		 map.put("code", 1);
		 map.put("msg", "success");
		 map.put("count",0);
		 map.put("data", new ArrayList());
		 map.put("allPage",0);
		 return map;
	 }
	 
	
}
