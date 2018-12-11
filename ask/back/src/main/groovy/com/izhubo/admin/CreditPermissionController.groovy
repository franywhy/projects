package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.util.regex.Matcher
import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import net.sf.json.JSONArray
import net.sf.json.JSONObject

import org.apache.commons.lang3.StringUtils
import org.hibernate.Criteria
import org.hibernate.Query
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.criterion.Restrictions

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mysqldb.model.CreditQueryPermission

/**
 * 学分权限控制
 * @author yanzhicheng
 * 2017年2月22日00:37:56
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class CreditPermissionController extends BaseController {

	@Resource
	private SessionFactory sessionFactory;

	DBCollection areaTable(){
		return mainMongo.getCollection('area');
	}
	
	DBCollection teachersTable(){
		return mainMongo.getCollection('teachers');
	}
	
	DBCollection adminsTable(){
		return adminMongo.getCollection('admins');
	}
	
	
	def list(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		String name = req.getParameter("name");
		String nick_name = req.getParameter("nick_name");
	    String post_name = req.getParameter("post_name");
		String credit_type = req.getParameter("credit_type");
		if (StringUtils.isNotBlank(name)){
			Pattern pattern = Pattern.compile("^.*" + name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("name").regex(pattern)
		}
		if (StringUtils.isNotBlank(nick_name)){
			Pattern pattern = Pattern.compile("^.*" + nick_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nick_name").regex(pattern)
		}
		
		if (StringUtils.isNotBlank(post_name)){
			Pattern pattern = Pattern.compile("^.*" + post_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("post_name").regex(pattern)
		}
		
		//全国权限
		List<String> ids = new ArrayList<String>();
		HashMap<String,CreditQueryPermission> map = new HashMap<String,CreditQueryPermission>();
		List<Object> list = new ArrayList<Object>();
		Query q = sessionFactory.getCurrentSession().createQuery("from CreditQueryPermission  where  type = 1 ");
		List<CreditQueryPermission> recordList = q.list();
		for(int i = 0;i<recordList.size();i++){
			CreditQueryPermission vo =  recordList.get(i);
			if(map.get(vo.getUserId()) == null){
				map.put(vo.getUserId(), vo);
				list.add(vo.getUserId());
				//判断是否为数字
				Pattern pattern = Pattern.compile("[0-9]*");
				Matcher isNum = pattern.matcher(vo.getUserId());
				if( isNum.matches() ){
					list.add(vo.getUserId() as Long);
				}
			}
			
		}
		
		
		if (StringUtils.isNotBlank(credit_type)){
			if(Integer.valueOf(credit_type) == 0){
				query.and("_id").notIn(list);
			}else if((Integer.valueOf(credit_type) == 1)){
				query.and("_id").in(list);
			}
		}
		
		Crud.list(req,adminsTable(),query.get(),$$("name":1,"nick_name":1,"post_name":1),MongoKey.SJ_DESC){List<BasicDBObject> data->
			for(BasicDBObject obj: data){
				String _id = obj['_id'];
				CreditQueryPermission vo =	map.get(_id);
				if(vo != null){
					obj.put("credit_type",1);
				}else{
					obj.put("credit_type",0);
				}
			}
		}
		
		
	}
	
	/**
	 * 查询课校区
	 * @param req
	 * @return
	 */
	def query_area_mongo(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		def org_code = req.getParameter("org_code");
		def org_name = req.getParameter("org_name");
		String user_id = req.getParameter("_id");
		if (StringUtils.isNotBlank(org_code)){
			//&^.{11}\$
			Pattern pattern = Pattern.compile("^.*" + org_code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("code").regex(pattern)
		}
		if (StringUtils.isNotBlank(org_name)){
			Pattern pattern = Pattern.compile("^.*" + org_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("name").regex(pattern)
		}
		
		def result = areaTable().find(query.get(),$$("code":1,"name":1,"nc_id":1,"_id":0)).toArray();
		Map<String,CreditQueryPermission> mapOrg = new HashMap<String,CreditQueryPermission>();
		Query q = sessionFactory.getCurrentSession().createQuery("from CreditQueryPermission  where  userId =? ");
		q.setString(0,user_id);
		 List<CreditQueryPermission> recordList = q.list();
		for (int i = 0; i < recordList.size(); i++) {
			CreditQueryPermission vo =  recordList.get(i);
			String orgid = vo.getOrgId();
			mapOrg.put(orgid, vo);
		}
		
		//过滤掉存在的校区
		Iterator<BasicDBObject> iter = result.iterator();
		while(iter.hasNext()){
			BasicDBObject o = iter.next();
			String nc_id = o.get("nc_id");
			String code =o.get("code");
			if(code.length()!=11){
				iter.remove();
			}else if(mapOrg.get(nc_id) != null){
				iter.remove();
			}
		}
		
		Map map = new HashMap();
		map.put("code", 1);
		map.put("msg", "success");
		map.put("data", result);
		return map;
	}
	

	
	
	
	/**
	 * 查询校区权限
	 * @param req
	 * @return
	 */
	def query_area_permission(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		String user_id = req.getParameter("user_id");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CreditQueryPermission.class);
		if(StringUtils.isNotBlank(user_id)){
			criteria.add(Restrictions.eq("userId", user_id));
		}else{
			return OK();	
		}
		List<CreditQueryPermission> list = criteria.list();
		
		Iterator<CreditQueryPermission> iter = list.iterator();
		while(iter.hasNext()){
			CreditQueryPermission vo =iter.next();
			if(vo.getOrgId() == null || vo.getOrgId().length()<5){
				iter.remove();
			}
		}
		return getResultOKS(list);
	}
	
	
	/**
	 * 添加校区
	 * @param req
	 * @return
	 */
	def add_area(HttpServletRequest req){
		def detailjson =  req.getParameter("detailjson");
		String user_id  = req.getParameter("_id");
		JSONArray ja =  new JSONArray();
		ja = ja.fromObject(detailjson);
		for(int i=0;i<ja.size();i++){
			JSONObject json = ja.get(i);
			String nc_id =json.get("nc_id");
			String name =json.get("name");
			String code =json.get("code");
			CreditQueryPermission c = new CreditQueryPermission();
			c.setOrgId(nc_id);
			c.setOrgCode(code);
			c.setOrgName(name);
			c.setUserId(user_id);
			sessionFactory.getCurrentSession().save(c);
		}
		return OK();
	}
	
	/**
	 * 修改校区
	 * @param req
	 * @return
	 */
	def edit_area(HttpServletRequest req){
		def detailjson =  req.getParameter("detailjson");
		String user_id  = req.getParameter("_id");
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction() ;
		JSONArray ja =  new JSONArray();
		ja = ja.fromObject(detailjson);
		List<Integer> ids = new ArrayList<Integer>();
		for(int i=0;i<ja.size();i++){
			JSONObject json = ja.get(i);
			ids.add(json.get("id"));
		}
		
		String hql = "Delete FROM CreditQueryPermission Where userId = ?  and (teacherId is null or teacherId ='') " ;
		if(ids.size()>0){
			hql +=" and id not in(:ids) ";
		}
		Query q = session.createQuery(hql);
		q.setString(0, user_id);
		if(ids.size()>0){
			q.setParameterList("ids", ids);
		}
		q.executeUpdate();
		tx.commit();
		return OK();
	}
	
	/**
	 * 查询老师
	 * @param req
	 * @return
	 */
	def query_teacher_mongo(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		def teacher_name = req.getParameter("teacher_name");
		def teacher_phone = req.getParameter("teacher_phone");
		String user_id = req.getParameter("_id");
		if(StringUtils.isNotBlank(teacher_name)){
			Pattern pattern = Pattern.compile("^.*" + teacher_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("name").regex(pattern)
		}
		
		if(StringUtils.isNotBlank(teacher_phone)){
			Pattern pattern = Pattern.compile("^.*" + teacher_phone + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("telephone").regex(pattern)
		}
		def result = teachersTable().find(query.get(),$$("telephone":1,"name":1,"nc_id":1,"_id":0)).toArray();
		
		Map<String,CreditQueryPermission> mapTeacher = new HashMap<String,CreditQueryPermission>();
		Query q = sessionFactory.getCurrentSession().createQuery("from CreditQueryPermission  where  userId =? ");
		q.setString(0,user_id);
		 List<CreditQueryPermission> recordList = q.list();
		for (int i = 0; i < recordList.size(); i++) {
			CreditQueryPermission vo =  recordList.get(i);
			String teacherid = vo.getTeacherId();
			mapTeacher.put(teacherid, vo);
		}
		
		//过滤掉存在的老师
		Iterator<BasicDBObject> iter = result.iterator();
		while(iter.hasNext()){
			BasicDBObject o = iter.next();
			String nc_id = o.get("nc_id");
			if(mapTeacher.get(nc_id) != null){
				iter.remove();
			}
		}
		
		Map map = new HashMap();
		map.put("code", 1);
		map.put("msg", "success");
		map.put("data", result);
		return map;
	}
	
	
	/**
	 * 查询老师权限
	 * @param req
	 * @return
	 */
	def query_teacher_permission(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		String user_id = req.getParameter("user_id");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CreditQueryPermission.class);
		if(StringUtils.isNotBlank(user_id)){
			criteria.add(Restrictions.eq("userId", user_id));
		}else{
			return OK();
		}
		List<CreditQueryPermission> list = criteria.list();
		Iterator<CreditQueryPermission> iter = list.iterator();
		while(iter.hasNext()){
			CreditQueryPermission vo =iter.next();
			if(vo.getTeacherId() == null || vo.getTeacherId().length() == 0){
				iter.remove();
			}
		}
		return getResultOKS(list);
	}
	
	
	/**
	 * 添加老师
	 * @param req
	 * @return
	 */
	def add_teacher(HttpServletRequest req){
		def detailjson =  req.getParameter("detailjson");
		String user_id  = req.getParameter("_id");
		JSONArray ja =  new JSONArray();
		ja = ja.fromObject(detailjson);
		for(int i=0;i<ja.size();i++){
			JSONObject json = ja.get(i);
			String nc_id =json.get("nc_id");
			String name =json.get("name");
			String telephone =json.get("telephone");
			CreditQueryPermission c = new CreditQueryPermission();
			c.setTeacherId(nc_id);
			c.setTeacherName(name);
			c.setTeacherPhone(telephone);
			c.setUserId(user_id);
			sessionFactory.getCurrentSession().save(c);
		}
		return OK();
	}
	
	/**
	 * 修改老师
	 * @param req
	 * @return
	 */
	def edit_teacher(HttpServletRequest req){
		def detailjson =  req.getParameter("detailjson");
		String user_id  = req.getParameter("_id");
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction() ;
		JSONArray ja =  new JSONArray();
		ja = ja.fromObject(detailjson);
		List<Integer> ids = new ArrayList<Integer>();
		for(int i=0;i<ja.size();i++){
			JSONObject json = ja.get(i);
			ids.add(json.get("id"));
		}
		
		String hql = "Delete FROM CreditQueryPermission Where userId = ? and (orgId is null or orgId ='') " ;
		if(ids.size()>0){
			hql +="  and id not in(:ids)";
		}
		Query q = session.createQuery(hql);
		q.setString(0, user_id);
		if(ids.size()>0){
			q.setParameterList("ids", ids);
		}
		q.executeUpdate();
		tx.commit();
		return OK();
	}
	
	
	/**
	 * 全国权限修改
	 * @param req
	 * @return
	 */
	def edit_area_admin(HttpServletRequest req){
		String user_id  = req.getParameter("_id");
		Integer type  = req.getParameter("type") as Integer;
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction() ;
		if(0 == type){
		//更新
			String hql = "Delete FROM CreditQueryPermission Where userId = ? and (orgId is null or orgId ='') and (teacherId is null or teacherId ='') " ;
			Query q = session.createQuery(hql);
			q.setString(0, user_id);
			q.executeUpdate();
		}else if (1 == type){
		//新增
			CreditQueryPermission c = new CreditQueryPermission();
			c.setUserId(user_id);
			c.setType(1);
			session.save(c);
		}
		tx.commit();
		return OK();
	}
	
}
