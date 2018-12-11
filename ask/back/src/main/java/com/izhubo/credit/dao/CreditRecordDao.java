package com.izhubo.credit.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.izhubo.credit.util.DateUtil;
import com.mysqldb.dao.BaseDaoImpl;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordSign;
import com.mysqldb.model.CreditRecordSignCVO;

@Repository("creditRecordDao")
public class CreditRecordDao extends BaseDaoImpl<CreditRecord>{
	
	public CreditRecordDao() {
	
	}
	
	
	@SuppressWarnings("unchecked")
	public List<CreditRecord> getCreditRecordByStudentId(String stuid){
		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		//sql.append(" select new com.mysqldb.model.CreditRecord( ");
		sql.append(" s.class_name ");
		sql.append(" ,t.learning_tip ");
		sql.append(" ,r.subject_name ");
		sql.append(" ,r.subject_id ");
		sql.append(" ,r.subject_code ");
		sql.append(" ,r.student_id ");
		sql.append(" ,r.class_id ");
		sql.append(" ,r.sign_date ");
		sql.append(" ,r.attendance_actual_score ");
		sql.append(" ,r.attendance_claim_score ");
		sql.append(" ,r.work_actual_score  ");
		sql.append(" ,r.work_claim_score  ");
		sql.append(" ,r.exam_actual_score ");
		sql.append(" ,r.exam_claim_score  ");
		sql.append(" ,r.total_score  ");
		sql.append(" ,r.is_pass   ");
		sql.append(" ,r.is_gain_cash ");
		//sql.append(" ) ");
		sql.append(" from credit_record  r  ");
		sql.append(" LEFT JOIN credit_record_sign s ");
		
		//修改学分中显示的以新的关联系
		sql.append(" on (s.student_id = r.student_id and s.sign_id = r.sign_id  ) ");
		sql.append(" LEFT JOIN credit_learning_tip t  ");
		sql.append(" on t.nc_class_id = r.class_id ");
		sql.append(" where r.student_id='"+stuid+"'  and s.is_enable=0 and  r.is_enable=0");
		sql.append(" ORDER BY r.class_id ");
		
		//List<Map<String,Object>> listMap =sessionFactory.getCurrentSession().createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP) .list();
		
		List<Object[]> results =sessionFactory.getCurrentSession().createSQLQuery(sql.toString()).list();
		List<CreditRecord> voList = new ArrayList<CreditRecord>();
		//手工拼装
		for (int i = 0; i < results.size(); i++) {
			Object[] os = results.get(i);
			CreditRecord vo =new CreditRecord();
			vo.setClassName(os[0]==null?null:os[0].toString());
			vo.setLearningTip(os[1]==null?null:os[1].toString());
			vo.setSubjectName(os[2]==null?null:os[2].toString());
			vo.setSubjectId(os[3]==null?null:os[3].toString());
			vo.setSubjectCode(os[4]==null?null:os[4].toString());
			vo.setStudentId(os[5]==null?null:os[5].toString());
			vo.setClassId(os[6]==null?null:os[6].toString());
			vo.setSignDate(os[7]==null?null:os[7].toString());
			vo.setAttendanceActualScore(os[8]==null?null:Integer.parseInt(os[8].toString()));
			vo.setAttendanceClaimScore(os[9]==null?null:Integer.parseInt(os[9].toString()));
			vo.setWorkActualScore(os[10]==null?null:Integer.parseInt(os[10].toString()));
			vo.setWorkClaimScore(os[11]==null?null:Integer.parseInt(os[11].toString()));
			vo.setExamActualScore(os[12]==null?null:Integer.parseInt(os[12].toString()));
			vo.setExamClaimScore(os[13]==null?null:Integer.parseInt(os[13].toString()));
			vo.setTotalScore(os[14]==null?null:Integer.parseInt(os[14].toString()));
			vo.setIsPass(os[15]==null?null:Integer.parseInt(os[15].toString()));
			vo.setIsGainCash(os[16]==null?null:os[16].toString());
			voList.add(vo);
		}
		return voList;
	}

	
	public List<CreditRecord> getTest( ){
	 
		
/*	Session session =sessionFactory.openSession();
	Transaction tx=session.beginTransaction();
		com.izhubo.credit.util.SyncUtil.SyncDelSameSignCvoBill (session, tx);
		tx.commit();
		session.close();
	 System.out.println(DateUtil.DateToString(new Date()));*/
	return null ;
	}
	
	
	public List<CreditRecord> findEntitysByHQLS(String hql, Object... params)
	{
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for(int i = 0; i < params.length; i++)
		{
			if ( (params[i] instanceof java.util.ArrayList)  ){
				query.setParameterList("list", (List)params[i]);
			}else {
				query.setParameter(i, params[i]);
			}
			
				
			 
			
			
		}
		return query.list();
	}
	
	public void UpdateEntitBylist(List<CreditRecord> list){
		
		Session session=sessionFactory.openSession();
		Transaction t=session.beginTransaction();
		for (CreditRecord d:list){
			session.update(d);
		}
		t.commit();
		session.close();
	}
}
