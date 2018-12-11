package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

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
import org.hibernate.criterion.Order
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.quartz.Scheduler
import org.quartz.Trigger
import org.quartz.impl.triggers.CronTriggerImpl
import org.springframework.web.bind.ServletRequestUtils

import com.izhubo.credit.init.OperationTaskInit
import com.izhubo.credit.schedule.OperationTaskSchedule
import com.izhubo.credit.schedule.RunProcessTaskSchedule
import com.izhubo.credit.schedule.XfpercentTaskSchedule
import com.izhubo.credit.util.CheckParameter
import com.izhubo.credit.vo.CreditOperationParameterVO
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.StaticSpring
import com.mysqldb.model.CreditOperationLog
import com.mysqldb.model.CreditOperationTask
import com.mysqldb.model.CreditStandard

/**
 * 学分运算
 * @author yanzhicheng
 * 2017年2月22日00:37:56
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class CreditoperationController extends BaseController {
	@Resource
	KGS discountKGS;
	@Resource
	private SessionFactory sessionFactory;

	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		def discountList = sessionFactory.getCurrentSession().createCriteria(CreditOperationTask.class).list()
		return getResultOKS(discountList);
	}
	
	/**
	 * 启动
	 */
	
	def start(HttpServletRequest req){
		def id = req.getParameter("id");
		String execute_date = req.getParameter("execute_date");
		String aBDate =req.getParameter("aBDate");
		String aEDate =req.getParameter("aEDate");
		String wBDate =req.getParameter("wBDate");
		String wEDate =req.getParameter("wEDate");
		String eBDate =req.getParameter("eBDate");
		String eEDate =req.getParameter("eEDate");
		//校验参数
		if (CheckParameter.checkString(execute_date)|| CheckParameter.checkString(aBDate)|| CheckParameter.checkString(aEDate) || CheckParameter.checkString(wBDate) || CheckParameter.checkString(wEDate)|| CheckParameter.checkString(eBDate)|| CheckParameter.checkString(eEDate)){
			Map map = new HashMap();
			map.put("code", -1);
			map.put("msg", "日期为空");
			map.put("data", "日期为空");
			return map;
		}
		Scheduler scheduler  = OperationTaskInit.getScheduler();
		Trigger trigger = OperationTaskInit.getTrigger();
		
		Session session = sessionFactory.getCurrentSession();
		Transaction  transaction  =session.beginTransaction();
		StringBuilder  upsql = new StringBuilder();
		upsql.append("update credit_operation_task");
		upsql.append(" set execute_date = "+execute_date);
		upsql.append(",");
		upsql.append(" attendance_begin_date = "+aBDate);
		upsql.append(",");
		upsql.append(" attendance_end_date = "+aEDate);
		upsql.append(",");
		upsql.append(" work_begin_date = "+wBDate);
		upsql.append(",");
		upsql.append(" work_end_date = "+wEDate);
		upsql.append(",");
		upsql.append(" exam_begin_date = "+eBDate);
		upsql.append(",");
		upsql.append(" exam_end_date = "+eEDate);
		upsql.append(" where id = "+id);
		SQLQuery query = session.createSQLQuery(upsql.toString());
		query.executeUpdate();
		session.flush();
		try {
		if(scheduler == null){
			System.out. println("启动失败，scheduler为空");
			}else{
					if(trigger == null){
						System.out. println("启动失败，trigger为空");
						return;
					}
					CronTriggerImpl impl = (CronTriggerImpl) trigger;
					impl.setCronExpression("0 0 8 "+execute_date+" * ?");
					impl.setStartTime(new Date());
				   scheduler.rescheduleJob(trigger.getKey(), trigger);
			}
		
		
		} catch (Exception e) {
			e.printStackTrace();
			Map map = new HashMap();
			map.put("code", 2000);
			map.put("msg", "启动失败");
			map.put("data", "启动失败");
			return map;
		}
	
		transaction.commit();
		return OK();
	}
	
	/**
	 * 新的立即执行
	 */
	def runprocess(HttpServletRequest req){
		 
		String aBDate =req.getParameter("begin_att_time");
		String aEDate =req.getParameter("end_att_time");
		 
		String eBDate =req.getParameter("begin_exam_time");
		String eEDate =req.getParameter("end_exam_time");
		
		 
		String tikuMonths =req.getParameter("begin_tk_time");
		 
		String regBDate =req.getParameter("begin_reg_time");
		String regEDate =req.getParameter("end_reg_time");
		 
		Integer attRun =req.getParameter("syncatt") as Integer;
		Integer regRun =req.getParameter("syncreg") as Integer;
		Integer tikuRun =req.getParameter("synctk") as Integer;
		Integer ncexam =req.getParameter("syncexam") as Integer;
	 	 
		 
		
/*		def detailjson =  req.getParameter("detailjson");
		String user_id  = req.getParameter("_id");
		JSONArray ja =  new JSONArray();
		ja = ja.fromObject(detailjson);
		List<Integer> ids = new ArrayList<Integer>();
		for(int i=0;i<ja.size();i++){
			JSONObject json = ja.get(i);
			Integer id =json.get("id") as Integer;
			ids.add(id);
		}*/
		RunProcessTaskSchedule o = new RunProcessTaskSchedule();
		 
		CreditOperationParameterVO paraVO = new CreditOperationParameterVO();
	 		paraVO.setAttendanceBeginDate(aBDate);
		//println paraVO.getAttendanceBeginDate();
		paraVO.setAttendanceEndDate(aEDate);
		//println paraVO.getAttendanceEndDate();
		paraVO.setTikuMonth(tikuMonths);
		//println paraVO.getTikuMonth();
 
 
		paraVO.setRegBeginDate(regBDate);
		//println paraVO.getRegBeginDate();
		paraVO.setRegEndDate(regEDate);
		//println paraVO.getRegEndDate();
		paraVO.setExamBeginDate(eBDate);
		//println paraVO.getExamBeginDate();
		paraVO.setExamEndDate(eEDate);
		//println paraVO.getExamEndDate();
		 
		paraVO.setSyncatt(attRun == 1) ;
		paraVO.setSyncreg(regRun == 1) ;
		paraVO.setSynctk(tikuRun == 1);
		paraVO.setSyncexam(ncexam == 1);
		
		////println "参数装载完成"
		
		 
		StaticSpring.execute(
			new Runnable() {
				public void run() {
				//	//println "开始执行"
					o.work(null,sessionFactory,paraVO,null);
				}
			}
			);
		return OK();
	}
	
	
	/**
	 *生成学分完成单据。从HttpServletRequest取得 当前月份 报名开始日期 报名结束日期
	 */
	def xfpercent(HttpServletRequest req){
		 
		String  dbilldate =req.getParameter("dbilldate");
		String s_months =req.getParameter("s_months");
		 
		String e_months =req.getParameter("e_months"); 
		////println "进入学分生成单据"+dbilldate+" "+s_months+" "+e_months;
		//校验参数
		if (CheckParameter.checkString(dbilldate)|| CheckParameter.checkString(s_months)|| CheckParameter.checkString(e_months) ){
			Map map = new HashMap();
			map.put("code", -1);
			map.put("msg", "日期为空");
			map.put("data", "日期为空");
			return map;
		}		
	 
 
		XfpercentTaskSchedule o = new XfpercentTaskSchedule();
		 
		CreditOperationParameterVO paraVO = new CreditOperationParameterVO();
 
	 
		paraVO.setTikuMonth(dbilldate);
		 
 
 
		paraVO.setRegBeginDate(s_months);
		 
		paraVO.setRegEndDate(e_months);
		  
		 
		 
		
		 
		StaticSpring.execute(
			new Runnable() {
				public void run() {
			 
					o.work(null,sessionFactory,paraVO,null);
				}
			}
			);
		return OK();
	}
	
	
	
	
	/**
	 * 立即执行
	 */
	def executed(HttpServletRequest req){
		//println "进入了旧的执行"
		String aBDate =req.getParameter("begin_att_time");
		String aEDate =req.getParameter("end_att_time");
	
		String eBDate =req.getParameter("begin_exam_time");
		String eEDate =req.getParameter("end_exam_time");
		
		
		String pwBDate =req.getParameter("begin_p_work_time");
		String pwEDate =req.getParameter("end_p_work_time");
		String twBDate =req.getParameter("begin_t_work_time");
		String twEDate =req.getParameter("end_t_work_time");
		String regBDate =req.getParameter("begin_reg_time");
		String regEDate =req.getParameter("end_reg_time");
		
		
		Integer retake =req.getParameter("retake") as Integer;
		Integer junior =req.getParameter("junior")as Integer;
		Integer medium =req.getParameter("medium")as Integer;
		Integer accounting =req.getParameter("accounting")as Integer;
		Integer teacher =req.getParameter("teacher")as Integer;
		
		def detailjson =  req.getParameter("detailjson");
		String user_id  = req.getParameter("_id");
		JSONArray ja =  new JSONArray();
		ja = ja.fromObject(detailjson);
		List<Integer> ids = new ArrayList<Integer>();
		for(int i=0;i<ja.size();i++){
			JSONObject json = ja.get(i);
			Integer id =json.get("id") as Integer;
			ids.add(id);
		}
		OperationTaskSchedule o = new OperationTaskSchedule();
		CreditOperationParameterVO paraVO = new CreditOperationParameterVO();
		paraVO.setAttendanceBeginDate(aBDate);
		paraVO.setAttendanceEndDate(aEDate);
		
		paraVO.setPworkBeginDate(pwBDate);
		paraVO.setPworkEndDate(pwEDate);
		paraVO.setTworkBeginDate(twBDate);
		paraVO.setTworkEndDate(twEDate);
		paraVO.setRegBeginDate(regBDate);
		paraVO.setRegEndDate(regEDate);
		
		paraVO.setExamBeginDate(eBDate);
		paraVO.setExamEndDate(eEDate);
		
		paraVO.setRetake(retake == 1);
		paraVO.setJunior(junior == 1);
		paraVO.setMedium(medium == 1);
		paraVO.setAccounting(accounting == 1);
		paraVO.setTeacher(teacher == 1);
		StaticSpring.execute(
			new Runnable() {
				public void run() {
					o.work(null,sessionFactory,paraVO,ids);
				}
			}
			);
		return OK();
	}
	
	/**
	 * 查询
	 */
	def query_subject(HttpServletRequest req){
		def query = Web.fillTimeBetween(req);
		def subject_name = req.getParameter("subject_name");
		def subject_code = req.getParameter("subject_code");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CreditStandard.class);
		if(StringUtils.isNotBlank(subject_name)){
			criteria.add(Restrictions.like("subject_name","%"+subject_name+"%"));
		}
		if(StringUtils.isNotBlank(subject_code)){
			criteria.add(Restrictions.like("course_code","%"+subject_code+"%"));
		}
		return getResultOK(criteria.list());
	}
	
	
	
	/**
	 * 查询
	 */
	def query_log(HttpServletRequest req){
		//翻页查询
		int pageSize = ServletRequestUtils.getIntParameter(req, "size", 20);
		int page = ServletRequestUtils.getIntParameter(req, "page", 1);
		
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CreditOperationLog.class);
		//总条数-分页
		Criteria criterion_count = sessionFactory.getCurrentSession().createCriteria(CreditOperationLog.class).setProjection(Projections.rowCount());
		
		
		List result  = criteria.addOrder(Order.desc("ts")).setFirstResult((page - 1) * pageSize).setMaxResults(pageSize).list();
		
		
		int log_count = (Integer) criterion_count.uniqueResult();
		
		int allpage = log_count / pageSize + log_count% pageSize >0 ? 1 : 0;
		
		Map map = new HashMap();
		map.put("code", 1);
		map.put("msg", "success");
		map.put("count",log_count);
		map.put("data", result);
		map.put("allPage",allpage);
		return map;
		}
}
