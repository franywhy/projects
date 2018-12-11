package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.lang.reflect.Field
import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import net.sf.json.JSONArray
import net.sf.json.JSONObject

import org.apache.commons.lang3.StringUtils
import org.hibernate.Criteria
import org.hibernate.SQLQuery
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.web.bind.annotation.ResponseBody

import com.izhubo.rest.anno.RestWithSession
import com.mongodb.DBCollection
import com.mysqldb.model.CreditLearningTip

/**
 * 学习建议列表
 * @author yanzhicheng
 * 2017年2月22日00:37:56
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class CreditLearningTipController extends BaseController {

	@Resource
	private SessionFactory sessionFactory;

	DBCollection coursesTable(){
		return mainMongo.getCollection('commoditys');
	}
	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		int pageSize = ServletRequestUtils.getIntParameter(req, "size", 20);
		int page = ServletRequestUtils.getIntParameter(req, "page", 1);
		String class_name = req.getParameter("class_name");
		String class_code = req.getParameter("class_code");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CreditLearningTip.class);
		//总条数-分页
		Criteria criterion_count = sessionFactory.getCurrentSession().createCriteria(CreditLearningTip.class).setProjection(Projections.rowCount());
		if(StringUtils.isNotBlank(class_name)){
			criteria.add(Restrictions.like("class_name","%"+class_name+"%"));
			criterion_count.add(Restrictions.like("class_name","%"+class_name+"%"));
		}
		if(StringUtils.isNotBlank(class_code)){
			criteria.add(Restrictions.like("class_code","%"+class_code+"%"));
			criterion_count.add(Restrictions.like("class_code","%"+class_code+"%"));
		}
		
		List result  = criteria.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize).list();
		
  
		int tip_count = (Integer) criterion_count.uniqueResult();
		
		int allpage = tip_count / pageSize + tip_count% pageSize >0 ? 1 : 0;
		
		
		Map map = new HashMap();
		map.put("code", 1);
		map.put("msg", "success");
		map.put("count",tip_count);
		map.put("data", result);
		map.put("allPage",allpage);
		return map;
	}
	
	
	
	
	/**
	 * 构建更新SQL
	 * @param CreditLearningTip
	 * @return
	 * @throws Exception
	 */
	private static String buildUpdateSql(CreditLearningTip creditLearningTip) throws Exception {
		
		StringBuffer sql = new StringBuffer("update "+creditLearningTip.returnTable()+" set ");
		String whereSql = null;
		StringBuffer setSql = new StringBuffer();
		   Field[] fields = creditLearningTip.getClass().getDeclaredFields ();
		   for ( int i = 0 ; i < fields. length ; i++){
			   Field field = fields[i];
			   field.setAccessible(true);
			   String fieldName = field.getName();
			   Object fieldValue = field.get(creditLearningTip);
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
	 *修改更新
	 */
	@ResponseBody
	def editTip(HttpServletRequest req){
		Transaction  transaction  = sessionFactory.getCurrentSession().beginTransaction();
		def query = Web.fillTimeBetween(req);
		def learnlingtip = req.getParameter("learnlingtip");
		Integer id = Integer.parseInt(req.getParameter("id"));
		CreditLearningTip  creditLearningTip = new CreditLearningTip();
		creditLearningTip.setId(id);
		creditLearningTip.setLearning_tip(learnlingtip);
		SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(buildUpdateSql(creditLearningTip));
		//sessionFactory.getCurrentSession().update(entry.getValue());
		sqlQuery.executeUpdate();
		transaction.commit();
		sessionFactory.getCurrentSession().flush();
		return OK();
	}
	

	/**
	 * 查询课程序列
	 * @param req
	 * @return
	 */
	def query(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		def subject_name = req.getParameter("class_name");
		if (StringUtils.isNotBlank(subject_name)){
			Pattern pattern = Pattern.compile("^.*" + subject_name + ".*\$", Pattern.CASE_INSENSITIVE)
			query.and("nc_name").regex(pattern)
		}
		//**************课程序列 begin*****************//
		def coursesResult = coursesTable().find(query.get(),$$("nc_name":1,"code":1,"nc_id":1,"_id":0)).toArray();
		Map map = new HashMap();
		map.put("code", 1);
		map.put("msg", "success");
		map.put("data", coursesResult);
		return map;
		//**************课程序列vend******************//
	}
	/**
	 * 添加课程序列
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
			String course_code =json.get("code");
			CreditLearningTip s = new CreditLearningTip();
			s.setNc_class_id(nc_id);
			s.setClass_name(nc_name);
			s.setClass_code(course_code);
			s.setLearning_tip("");
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

		CreditLearningTip cs = (CreditLearningTip) sessionFactory.getCurrentSession().get(CreditLearningTip.class, id);

		sessionFactory.getCurrentSession().delete(cs);

		sessionFactory.getCurrentSession().flush();
		return OK();
	}
}
