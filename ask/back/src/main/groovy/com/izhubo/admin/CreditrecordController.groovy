package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.text.DecimalFormat
import java.util.List;

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.hibernate.Criteria
import org.hibernate.Query
import org.hibernate.SessionFactory
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.springframework.web.bind.ServletRequestUtils

import com.izhubo.credit.util.SubjectIdUtil
import com.izhubo.rest.anno.RestWithSession
import com.mongodb.DBCollection
import com.mongodb.QueryBuilder
import com.mysqldb.model.CreditQueryPermission
import com.mysqldb.model.CreditRecord
import com.mysqldb.model.CreditRecordReportTemp
import com.mysqldb.model.CreditRecordSign
import com.mysqldb.model.CreditStandard

/**
 * 学分记录列表
 * @author yanzhicheng
 * 2017年2月22日00:37:56
 *
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class CreditrecordController extends BaseController {


	@Resource
	private SessionFactory sessionFactory;

	DBCollection areasTable(){
		return mainMongo.getCollection('area');
	}

	/**
	 * 查询
	 */
	def list(HttpServletRequest req){
		int pageSize = ServletRequestUtils.getIntParameter(req, "size", 20);
		int page = ServletRequestUtils.getIntParameter(req, "page", 1);
		req.getParameterNames();
		req.getParameterMap();
		String studentName = req.getParameter("studentName");
		String className = req.getParameter("className");
		String idCard = req.getParameter("idCard");
		String phone = req.getParameter("phone");
		String orgName = req.getParameter("orgName");
		String bSignDate = req.getParameter("beginSignDate");
		String eSignDate = req.getParameter("endSignDate");
		String largeAreaName = req.getParameter("largeAreaName");

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CreditRecordSign.class);
		//总条数-分页
		Criteria criterion_count = sessionFactory.getCurrentSession().createCriteria(CreditRecordSign.class).setProjection(Projections.rowCount());
		 

		//修改查询时过滤enable=1的记录 lintf 2017年10月29日9:49:56
		criteria.add(Restrictions.sqlRestriction(" is_enable=0 "));
		criterion_count.add(Restrictions.sqlRestriction(" is_enable=0 "));

		//println "开始添加添加完成"
		if(StringUtils.isNotBlank(studentName)){
			criteria.add(Restrictions.like("studentName","%"+studentName+"%"));
			criterion_count.add(Restrictions.like("studentName","%"+studentName+"%"));
		}
		if(StringUtils.isNotBlank(idCard)){
			criteria.add(Restrictions.like("idCard","%"+idCard+"%"));
			criterion_count.add(Restrictions.like("idCard","%"+idCard+"%"));
		}
		if(StringUtils.isNotBlank(phone)){
			criteria.add(Restrictions.like("phone","%"+phone+"%"));
			criterion_count.add(Restrictions.like("phone","%"+phone+"%"));
		}
		if(StringUtils.isNotBlank(orgName)){
			criteria.add(Restrictions.like("orgName","%"+orgName+"%"));
			criterion_count.add(Restrictions.like("orgName","%"+orgName+"%"));
		}

		if(StringUtils.isNotBlank(className)){
			criteria.add(Restrictions.like("className","%"+className+"%"));
			criterion_count.add(Restrictions.like("className","%"+className+"%"));
		}

		if(StringUtils.isNotBlank(bSignDate)) {
			//查询制定时间之后的记录
			criteria.add(Restrictions.ge("signDate",bSignDate));
			criterion_count.add(Restrictions.ge("signDate",bSignDate));
		}
		if(StringUtils.isNotBlank(eSignDate)){
			//查询指定时间之前的记录
			criteria.add(Restrictions.le("signDate",eSignDate));
			criterion_count.add(Restrictions.le("signDate",eSignDate));
		}


		//权限控制，校长、经理
		Map user = (Map) req.getSession().getAttribute("user");
		//根据用户id查询对应的权限-校区
		String hql = " from CreditQueryPermission where userId = ?";
		Query q = sessionFactory.getCurrentSession().createQuery(hql);
		q.setString(0, user.get("_id"));
		List<CreditQueryPermission> vos = q.list();
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
		//println "权限完成2"

		List<CreditRecordSign> result  = criteria.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize).list();
		//println "查完数据1"
		int sign_count = criterion_count.uniqueResult();
		//println "查完数据2"
		int allpage = sign_count / pageSize + sign_count% pageSize >0 ? 1 : 0;
		//println "查完数据3"

		//println "数据取完成"
		//拼装【班型名称-实修分-应修分-是否合格-学分完成率】
		//根据学员主键查询学分记录表
		List<String> tempList= new ArrayList<String>();
		Map<String,String> codeMap = new HashMap<String,String>();
		//原来的key是studentid+@+classid的   现在改为新的的 直接以 sign_id
		for(int i = 0;i < result.size();i++){
			CreditRecordSign s = result.get(i);
			tempList.add(s.getStudentId());
			//	String key = s.getStudentId()+"@"+s.getClassId();
			String key =s.getSignId();
			codeMap.put(key,s.getOrgCode());
		}

		if(tempList.size() >0 ){
			//添加enable=0的限制
			List<CreditRecord> recordList = sessionFactory.getCurrentSession().createQuery("from CreditRecord  where  isEnable=0 and isCheck=0 and studentId in (:alist) ").setParameterList("alist",tempList).list();
			//这里要过滤掉重复的值
			Map<String,CreditRecord> map = new HashMap<String,CreditRecord>();
			//过滤掉重复【学员主键+科目主键】数据,如果是重复科目按照最新报名表显示
			for(int i = 0; i< recordList.size();i++){
				CreditRecord vo = recordList.get(i);
				//	String stubject_id = vo.getNcSubjectId();
				//	String student_id  = vo.getNcUserId();
				//String key = student_id +"@"+stubject_id;
				String key=vo.getSignId()+vo.getSubjectType();

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

			recordList = map.values().toList();

			//按班型-计算学员的实修分、应修分、是否合格、学分完成率
			Map<String,List<Integer>> remap  = new HashMap<String,List<Integer>>();

			//
			//原来是key = 学员-班级-科目 现在改成 学员-报名表-科目 lintf 2017年11月7日17:16:05
			Map<String, Map<String, Map<String,CreditRecord>>> stuclassMap = new HashMap<String, Map<String, Map<String,CreditRecord>>>();
			for (int i = 0; i < recordList.size(); i++) {
				CreditRecord vo = recordList.get(i);
				String stuid = vo.getStudentId();
			//	String classid = vo.getClassId();
				String signid=vo.getSignId(); //由原来的班级关联改为报名表关联 
				String subid = vo.getSubjectId();
				if(stuclassMap.get(stuid) == null){
					Map<String, Map<String,CreditRecord>> classmap = new HashMap<String, Map<String,CreditRecord>>();
					Map<String,CreditRecord> submap = new HashMap<String,CreditRecord>();
					submap.put(subid, vo);
					classmap.put(signid, submap);
					stuclassMap.put(stuid, classmap);
				}else{
					Map<String, Map<String,CreditRecord>>  classmap = stuclassMap.get(stuid);
					if(classmap.get(signid) == null){
						Map<String,CreditRecord> submap = new HashMap<String,CreditRecord>();
						submap.put(subid, vo);
						classmap.put(signid, submap);
					}else{
						Map<String,CreditRecord> submap = classmap.get(signid);
						submap.put(subid, vo);
					}
				}
			}

			Map<String,Map<String,List<Integer>>>  stuclassSumMap = computeStudent(stuclassMap);
			for(int i = 0;i < result.size();i++){
				CreditRecordSign s = result.get(i);
				s.setIdCard(null);
				String stuid = s.getStudentId()
				String baseSignid =s.getSignId();
				
				Map<String,List<Integer>> tempmap  = stuclassSumMap.get(stuid);
				if(tempmap !=null){
					List<Integer> list = tempmap.get(baseSignid);
					if(list != null){
						s.setClassClaimScore(list.get(0));
						s.setClassActualScore(list.get(1));
						s.setIspass(list.get(2));
						double rate = (list.get(1)/list.get(0))*100;
						DecimalFormat df=new DecimalFormat("0.00");
						s.setCreditCompletionRate(df.format(rate)+"%");
					}
				}
			}

			//添加大区名称
			addLagerArea(result);
		}

		Map map = new HashMap();
		map.put("code", 1);
		map.put("msg", "success");
		map.put("count",sign_count);
		map.put("data", result);
		map.put("allPage",allpage);
		return map;

	}

	/**
	 * 查询身份证
	 */
	def query_idcard(HttpServletRequest req){
		String signId = req.getParameter("signId");
		
		Query q  = sessionFactory.getCurrentSession().createQuery("from CreditRecordSign  where isEnable=0 and  signId = ?")
		q.setString(0, signId);
		//println "查身份证"+q.list().size()+signId;
		return getResultOK(q.list());
	}

	//计算出每一个学员没一个班型实修分、应修分、是否合格
	private Map<String,Map<String,List<Integer>>> computeStudent(Map<String, Map<String, Map<String,CreditRecord>>> stuclassMap){
		List<CreditStandard> creditStandard = sessionFactory.getCurrentSession().createQuery("from CreditStandard").list();
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
		Map<String,Map<String,List<Integer>>> stuclassSumMap = new HashMap<String,Map<String,List<Integer>>>();
		for (Map.Entry<String, Map<String, Map<String,CreditRecord>>> entry : stuclassMap.entrySet()){
			Map<String, Map<String,CreditRecord>> classMap = entry.getValue();
			String studentid = entry.getKey();
			for (Map.Entry<String, Map<String,CreditRecord>> centry : classMap.entrySet()) {
				String classid = centry.getKey();
				int sum = 0;//实际每个班学分总分
				int baseSum = 0;//每个班标准满分
				boolean junior_b = true;
				boolean accounting_b = true;
				Map<String,CreditRecord> subvos = centry.getValue();
				for (Map.Entry<Map<String,CreditRecord>> subentry : subvos.entrySet()) {
					CreditRecord votemp = subentry.getValue();
					sum += votemp.getAttendanceActualScore()+votemp.getWorkActualScore();
					baseSum += votemp.getAttendanceClaimScore()+votemp.getWorkClaimScore();

					//boolean j = all_juniorMap.get(votemp.getNcSubjectId()) ==null;
					//boolean a = all_accountMap.get(votemp.getNcSubjectId())==null;
					//计算总分,有特殊科目，
					if(all_juniorMap.get(votemp.getSubjectId()) !=null && junior_b){
						//只加一次考核分
						sum += votemp.getExamActualScore();
						baseSum += votemp.getExamClaimScore();
						junior_b = false;
					}/*else if(all_accountMap.get(votemp.getNcSubjectId())!=null && accounting_b){
					 //只加一次考核分
					 sum += votemp.getExamActualScore();
					 baseSum += votemp.getExamClaimScore();
					 accounting_b = false;
					 }*/else if(all_juniorMap.get(votemp.getSubjectId()) ==null ){//&& all_accountMap.get(votemp.getNcSubjectId())==null
						sum += votemp.getExamActualScore();
						baseSum += votemp.getExamClaimScore();
					}
				}
				List<Integer> l = new ArrayList<Integer>();
				l.add(baseSum);
				l.add(sum);
				if(sum >= (int)(baseSum*0.8)){
					//是否合格0为合格
					l.add(0);
				}else{
					l.add(1);
				}
				classMap.put(classid, l);
			}

			stuclassSumMap.put(studentid, classMap);
		}
		return stuclassSumMap;
	}

	/**
	 * 卡片界面查询
	 */
	def show(HttpServletRequest req){
		String nc_user_id = req.getParameter("nc_user_id");
		String classId = req.getParameter("class_id");
		String signId = req.getParameter("signId");
		////println "signId:"+signId
		//如果
		Criteria criteria  = sessionFactory.getCurrentSession().createCriteria(CreditRecord.class)
	 
		criteria.add(Restrictions.eq("isEnable", new Integer(0)));
		criteria.add(Restrictions.eq("studentId", nc_user_id));
		criteria.add(Restrictions.eq("signId", signId));
		//criteria.add(Restrictions.eq("classId", classId))
		List<CreditRecord> list = criteria.list();
	//	//println "查到"+list.size();
		//这里要过滤掉重复的值
		Map<String,CreditRecord> map = new HashMap<String,CreditRecord>();
		//过滤掉重复【学员主键+科目主键】数据,如果是重复科目按照最新报名表显示
		for(int i = 0; i< list.size();i++){
			CreditRecord vo = list.get(i);
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
		List<CreditRecord> relist = new ArrayList<CreditRecord>();
		//按照报名表ID分类
		for(Map.Entry<Map<String,CreditRecord>> entry : map.entrySet()){
			//CreditRecord vo = entry.getValue();
			CreditRecordReportTemp t=new CreditRecordReportTemp(entry.getValue());
			if(t.getSignId().equals(signId)){
				relist.add(t);
			}
		}
		return getResultOKS(relist);
	}

def add(HttpServletRequest req){
		 
		return OK();
	}
	/**
	 * 添加大区名称
	 * @param vos
	 * @param table
	 * @return
	 */
	def addLagerArea(List<CreditRecordSign> vos){
		List<String> codes = new ArrayList<String>();
		HashSet<String> set = new HashSet<String>();//创建一个set用来去重复
		for(int i = 0 ; i<vos.size; i++){
			CreditRecordSign vo = vos.get(i);
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
			CreditRecordSign vo = vos.get(i);
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
}
