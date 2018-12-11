package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.lang.reflect.Field
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import net.sf.json.JSONArray
import net.sf.json.JSONObject

import org.apache.commons.lang3.StringUtils
import org.hibernate.Criteria
import org.hibernate.SQLQuery
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.ResponseBody

import com.google.gson.reflect.TypeToken
import com.izhubo.credit.util.JsonUtil
import com.izhubo.credit.util.SubjectIdUtil;
import com.izhubo.rest.anno.RestWithSession
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mysqldb.model.CreditStandard

/**
 * 学分标准列表
 * @author yanzhicheng
 * 2017年2月22日00:37:56
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class CreditstandardController extends BaseController {

	@Resource
	private SessionFactory sessionFactory;

	DBCollection coursesTable(){
		return mainMongo.getCollection('commodity_courses');
	}
	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		int pageSize = ServletRequestUtils.getIntParameter(req, "size", 20);
		int page = ServletRequestUtils.getIntParameter(req, "page", 1);
		def query = Web.fillTimeBetween(req);
		def subject_name = req.getParameter("subject_name");
		def subject_code = req.getParameter("subject_code");
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CreditStandard.class);
		//总条数-分页
		Criteria criterion_count = sessionFactory.getCurrentSession().createCriteria(CreditStandard.class).setProjection(Projections.rowCount());
		
		
		if(StringUtils.isNotBlank(subject_name)){
			criteria.add(Restrictions.like("subject_name","%"+subject_name+"%"));
			criterion_count.add(Restrictions.like("subject_name","%"+subject_name+"%"));
		}
		if(StringUtils.isNotBlank(subject_code)){
			criteria.add(Restrictions.like("course_code","%"+subject_code+"%"));
			criterion_count.add(Restrictions.like("course_code","%"+subject_code+"%"));
		}
		
		
		List result  = criteria.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize).list();
		
		//查询的总行数
		int sta_count = (Integer) criterion_count.uniqueResult();
  
		
		int allpage = sta_count / pageSize + sta_count% pageSize >0 ? 1 : 0;
		
		
		Map map = new HashMap();
		map.put("code", 1);
		map.put("msg", "success");
		map.put("count",sta_count);
		map.put("data", result);
		map.put("allPage",allpage);
		return map;
	}
	
	
	/**
	 *修改更新
	 */
	@ResponseBody
	def update(HttpServletRequest req){
		Session session = sessionFactory.getCurrentSession();
		Transaction  transaction  = session.beginTransaction();
		Map user = (Map) req.getSession().getAttribute("user");
		Map requestParams = req.getParameterMap();
		Map<String,CreditStandard> map = new HashMap<String,CreditStandard>();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			if(!"callback".equals(name)){
				//如果输入有空格，也就复制粘贴就会解析错误
				List<CreditStandard> creditStandards = JsonUtil.fromJson(name, new TypeToken<List<CreditStandard>>() {}.getType());
				for (int i = 0;i<creditStandards.size();i++) {
					CreditStandard vo = creditStandards.get(i);
					Integer total_credits = vo.getTotal_credits();
					Integer attendance_score = vo.getAttendance_score();
					Integer activity_fraction = vo.getActivity_fraction();
					Integer examination_score = vo.getGraduation_examination_score();
					String remarks = vo.getRemarks();
					String subject_type = vo.getSubject_type();
					String id = vo.getId();
					if(map.get(id) == null){
						map.put(id, vo)
					}else{
						CreditStandard votemp = map.get(id);
						if(total_credits != null){
							votemp.setTotal_credits(total_credits);
						}
						if(attendance_score != null){
							votemp.setAttendance_score(attendance_score)
						}
						
						if(activity_fraction != null){
							votemp.setActivity_fraction(activity_fraction)
						}
					
						if(examination_score != null){
							votemp.setGraduation_examination_score(examination_score);
						}
						
						if(remarks != null){
							votemp.setRemarks(remarks);
						}
						if(subject_type != null){
							votemp.setRemarks(subject_type);
						}
					}
				}
				
			}
		}
		for (Map.Entry<String,CreditStandard> entry : map.entrySet()){ 
			SQLQuery query = session.createSQLQuery(buildUpdateSql(entry.getValue()));
			query.executeUpdate();
		}
		transaction.commit();
		session.flush();
		return OK();
	}
	
	
	/**
	 * 构建更新SQL
	 * @param creditStandard
	 * @return
	 * @throws Exception
	 */
	private static String buildUpdateSql(CreditStandard creditStandard) throws Exception {

		StringBuffer sql = new StringBuffer("update credit_standard set ");
		String whereSql = null;
		StringBuffer setSql = new StringBuffer();
		   Field[] fields = creditStandard.getClass().getDeclaredFields ();
		   for ( int i = 0 ; i < fields. length ; i++){
			   Field field = fields[i];
			   field.setAccessible(true);
			   String fieldName = field.getName();
			   Object fieldValue = field.get(creditStandard);
			   String fieldType = field.getType().toString();
			   if(fieldName.equals("id")){
				   whereSql = " where id=" + fieldValue;
				   continue;
			   }
			   if(null != fieldValue){
				   setSql.append(fieldName).append("=");
				   if(fieldType.endsWith("Integer")){
					   setSql.append(fieldValue).append(",");
				   }else{
					   setSql.append("\""+fieldValue+"\"").append(",");
				   }
			   }
		   }
		   String tempSql = setSql.toString();
		   tempSql = tempSql.substring(0, tempSql.length() - 1);
		   return sql.append(tempSql).append(whereSql).toString();
	}
	

	/**
	 * 查询课程科目
	 * @param req
	 * @return
	 */
	def query(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		def subject_name = req.getParameter("subject_name");
		def subject_code = req.getParameter("subject_code");
		if (StringUtils.isNotBlank(subject_name)){
			Pattern pattern = Pattern.compile("^.*" + subject_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nc_name").regex(pattern)
		}
		if (StringUtils.isNotBlank(subject_code)){
			Pattern pattern = Pattern.compile("^.*" + subject_code + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("course_code").regex(pattern)
		}
		//查询本地标准表科目
		Map<String,CreditStandard> mapSubject = new HashMap<String,CreditStandard>();
		List<CreditStandard>  creditStandards = sessionFactory.getCurrentSession().createQuery("from CreditStandard").list();
			  	for (int i = 0; i < creditStandards.size(); i++) {
			CreditStandard vo =  creditStandards.get(i);
			String subType = vo.getSubject_type();
			mapSubject.put(vo.getNc_id(), vo);
		}
		//**************科目 begin*****************//
		def coursesResult = coursesTable().find(query.get(),$$("nc_name":1,"course_code":1,"nc_id":1,"_id":0)).toArray();
		//过滤掉存在的科目
		Iterator<BasicDBObject> iter = coursesResult.iterator();
		while(iter.hasNext()){
			BasicDBObject o = iter.next();
			String nc_id = o.get("nc_id");
			if(mapSubject.get(nc_id) != null){
				iter.remove();
			}
		}
		Map map = new HashMap();
		map.put("code", 1);
		map.put("msg", "success");
		map.put("data", coursesResult);
		return map;
		//**************科目 end******************//
	}
	/**
	 * 添加科目
	 * @param req
	 * @return
	 */
	def add(HttpServletRequest req){
		def detailjson =  req.getParameter("detailjson");
		JSONArray ja =  new JSONArray();
		ja = ja.fromObject(detailjson);

		double subSumMoney = 0;
		for(int i=0;i<ja.size();i++){
			JSONObject json = ja.get(i);
			String nc_id =json.get("nc_id");
			String nc_name =json.get("nc_name");
			String course_code =json.get("course_code");
			CreditStandard s = new CreditStandard();
			s.setNc_id(nc_id);
			s.setSubject_name(nc_name);
			s.setCourse_code(course_code);
			s.setActivity_fraction(0);
			s.setAttendance_score(0);
			s.setGraduation_examination_score(0);
			s.setTotal_credits(0);
			s.setRemarks("");
			sessionFactory.getCurrentSession().save(s);
		}
		return OK();
	}
	
	/**
	 * 删除科目
	 * @param req
	 * @return
	 */
	def del(HttpServletRequest req){
		Integer id = req.getParameter("_id") as Integer;

		CreditStandard cs = (CreditStandard) sessionFactory.getCurrentSession().get(CreditStandard.class, id);

		sessionFactory.getCurrentSession().delete(cs);

		sessionFactory.getCurrentSession().flush();
		return OK();
	}
}
