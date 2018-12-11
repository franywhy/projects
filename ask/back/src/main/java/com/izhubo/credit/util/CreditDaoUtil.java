package com.izhubo.credit.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.mysqldb.model.CreditDef;
import com.mysqldb.model.CreditPerMon;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordNcSore;
import com.mysqldb.model.CreditRecordSign;
import com.mysqldb.model.CreditRecordSignCVO;
import com.mysqldb.model.CreditRecordTiKuScore;
import com.mysqldb.model.Creditpercent;

/**
 * 学分制中业务工具类
 * @author lintf	
 *
 */
public class CreditDaoUtil {
	
	 
	/**
	 * 按deflist取得自定义的map
	 * @param session
	 * @param deflist
	 * @return key为def_key value 为nc_code
	 */
	 public static Map<String,String> getDefMap(Session session,String deflist){
		 Map<String,String> m= new HashMap<String,String>();
	    	Criteria criteria  = session.createCriteria(CreditDef.class);
			criteria.add(Restrictions.eq("defList", deflist));	
			criteria.add(Restrictions.eq("dr", new Integer(0)));
			List<CreditDef> result=criteria.list();
			if (result!=null&&result.size()>0){
				for (CreditDef def:result){
					if(def!=null&&def.getDefKey()!=null){
						m.put(def.getDefKey(), def.getNcCode());
					}
				}
			}
			return m;
	    }
    
  
    
    public static List<String> getDef(Session session,String deflist){
    	List<String> root= new ArrayList<String>();
    	Criteria criteria  = session.createCriteria(CreditDef.class);
		criteria.add(Restrictions.eq("defList", deflist));	
		criteria.add(Restrictions.eq("dr", new Integer(0)));
		List<CreditDef> result=criteria.list();
		if (result!=null&&result.size()>0){
			for (CreditDef def:result){
				if(def!=null&&def.getDefKey()!=null){
					root.add(def.getDefKey());
				}
			}
		}
		return root;
    }
    /**
     * 某个月份到某个月份的目标完成率 
     * @param session
     * @param start_dbilldate   当前计算月份_开始
     * @param end_dbilldate    当前计算月份_结束
     
     * @return  key:dbilldate_months </p> value:Double 
     */
    public static Map<String,Double> getPercentMonth(Session session,String start_dbilldate,String end_dbilldate ){
    Map<String,Double>  PerMon=new HashMap<String,Double> ();
    if (start_dbilldate==null||end_dbilldate==null||start_dbilldate.length()<7||end_dbilldate.length()<7){
		return null;
	}
	
	String qslq = " from CreditPerMon where dr =0  and dbilldate >= ? and dbilldate<=?";
	Query qs = session.createQuery(qslq);
	qs.setString(0, start_dbilldate.substring(0,7));
	qs.setString(1, end_dbilldate.substring(0,7));
	 
	List<CreditPerMon> pvo = qs.list();
	 
	
	if (pvo!=null&&pvo.size()>0){
		for (CreditPerMon p:pvo){
			PerMon.put(p.getDbilldate()+"_"+p.getMonths(), p.getMbpercent().doubleValue());
		}
	} 
    
    return PerMon;
    }
    
    
    /**
     * 某个月份到某个月份的排课目标完成率 
     * @param session
     * @param start_dbilldate   当前计算月份
     * @param end_dbilldate    报名表的开始月份
     
     * @return  key:dbilldate_months </p> value:Double 
     */
    public static Map<String,Double> getPaikePercentMonth(Session session,String start_dbilldate,String end_dbilldate ){
    Map<String,Double>  PerMon=new HashMap<String,Double> ();
	 
	if (start_dbilldate==null||end_dbilldate==null||start_dbilldate.length()<7||end_dbilldate.length()<7){
		return null;
	}
	String qslq = " from CreditPerMon where dr =0  and dbilldate >= ? and dbilldate<=?";
	Query qs = session.createQuery(qslq);
	qs.setString(0, start_dbilldate.substring(0,7));
	qs.setString(1, end_dbilldate.substring(0,7));
	 
	List<CreditPerMon> pvo = qs.list();
	 
	
	if (pvo!=null&&pvo.size()>0){
		for (CreditPerMon p:pvo){
			PerMon.put(p.getDbilldate()+"_"+p.getMonths(), p.getPkpercent().doubleValue());
		}
	} 
    
    return PerMon;
    }
    
    
     
    
    
    /**
	  * 根据科目类型+学员ID取得学员学分档案数据</p> 
	  * from CreditRecord where ENABLE=x and  CONCAT (subjectType,ncUserId) in (:alist) 
	  * @param session
	  * @param idkeys
	  * @param row
	  * @param enable 0:0;1:1,3:1,0
	  * @return
	  */
	 public static List<CreditRecord> getLocalDateRecordBySubtypeUserid(Session session,List<String> idkeys,int row ,int enable ){
		 List<CreditRecord> backlist= new ArrayList<CreditRecord>();
		 
		 List<String> tempid= new ArrayList<String>(); 
		 List<Integer> enablelist= new ArrayList<Integer>();
		 if (enable==1){
			 enablelist.add(1);
		 }else if (enable==0){
			 enablelist.add(0);
		 }else if (enable==3){
			 enablelist.add(1); 
            enablelist.add(0); 
		 }else {
			 enablelist.add(0);
		 }
		 
		 
		 for (int i=0;i<idkeys.size();i++){
			 tempid.add(idkeys.get(i));
			 
			 if (tempid.size()==row){				 
				 			
				 List<CreditRecord> hvolist = session
							.createQuery("from CreditRecord where  isEnable in (:enablelist)  and  CONCAT (subjectType,studentId) in (:alist) ")
							.setParameterList("alist",tempid)
                            .setParameterList("enablelist",enablelist).list();
				 backlist.addAll(hvolist);
				 
				 tempid.clear();
			 }else if (i+1==idkeys.size()){
				 List<CreditRecord> hvolist = session
						 .createQuery("from CreditRecord where  isEnable in (:enablelist) and    CONCAT (subjectType,studentId) in (:alist) ")
							.setParameterList("alist",tempid)
							.setParameterList("enablelist",enablelist).list();
				 backlist.addAll(hvolist);
				 tempid.clear();
			 }
			 
		 }
		 
		return backlist;
		 
	 }
	 /**
		 * 根据hid 分段取得本地的订单 
		 * @param session
		 * @param idkeys
		 * @param row 多少行分段
		 * @return List<CreditRecordSign> 
		 */
		 public static List<CreditRecordSign> getLocalDateSign_h(Session session,List<String> idkeys,int row){
			 List<CreditRecordSign> backlist= new ArrayList<CreditRecordSign>();
			 List<String> tempid= new ArrayList<String>();
			 
			 for (int i=0;i<idkeys.size();i++){
				 tempid.add(idkeys.get(i));
				 
				 if (tempid.size()==row){
					 List<CreditRecordSign> hvolist = session
								.createQuery(
										"from CreditRecordSign where  signId in (:alist) ")
								.setParameterList("alist", tempid).list();
					 backlist.addAll(hvolist);
					 
					 tempid.clear();
				 }else if (i+1==idkeys.size()){
					 List<CreditRecordSign> hvolist = session
								.createQuery(
										"from CreditRecordSign where  signId in (:alist) ")
								.setParameterList("alist", tempid).list();
					 backlist.addAll(hvolist);
					 tempid.clear();
				 }
				 
			 }
			 
			return backlist;
			 
		 }
		 
	 /**
	  * 根据CID 分段 取得订单中C表的信息</p>
	  * from CreditRecordSignCVO where  SIGN_ID_C in (:alist)
	  * @param session
	  * @param idkeys
	  * @param row
	  * @return
	  */
	 public static List<CreditRecordSignCVO> getLocalDateSign_c(Session session,List<String> idkeys,int row){
		 List<CreditRecordSignCVO> backlist= new ArrayList<CreditRecordSignCVO>();
		 List<String> tempid= new ArrayList<String>();
		 
		 for (int i=0;i<idkeys.size();i++){
			 tempid.add(idkeys.get(i));
			 
			 if (tempid.size()==row){
				 List<CreditRecordSignCVO> hvolist = session
							.createQuery(
									"from CreditRecordSignCVO where  signIdc in (:alist) ")
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 
				 tempid.clear();
			 }else if (i+1==idkeys.size()){
				 List<CreditRecordSignCVO> hvolist = session
							.createQuery(
									"from CreditRecordSignCVO where  signIdc in (:alist) ")
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 tempid.clear();
			 }
			 
		 }
		 
		return backlist;
		 
	 }
	 
	 /**
	  * 根据userid  分段 取得订单中C表的信息 
	  * @param session
	  * @param idkeys
	  * @param row
	  * @param where " and "
	  * @return
	  */
	 public static List<CreditRecordSignCVO> getLocalDateSign_cByUserIWhere(Session session,List<String> idkeys,int row,String where ){
		 List<CreditRecordSignCVO> backlist= new ArrayList<CreditRecordSignCVO>();
		 List<String> tempid= new ArrayList<String>();//ENABLE=0 and 
		 String hql="from CreditRecordSignCVO where  studentId in (:alist) ";
		 if (where!=null){
			 hql=hql+where;
		 }
		 
		 for (int i=0;i<idkeys.size();i++){
			 tempid.add(idkeys.get(i));
			 
			 if (tempid.size()==row){
				 List<CreditRecordSignCVO> hvolist = session
							.createQuery(
									 hql)
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 
				 tempid.clear();
			 }else if (i+1==idkeys.size()){
				 List<CreditRecordSignCVO> hvolist = session
							.createQuery(hql)
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 tempid.clear();
			 }
			 
		 }
		 
		return backlist;
		 
	 }

	 /**
	  * 根据 userid 分段 取得订单中H表的信息
	  * @param session
	  * @param idkeys
	  * @param row
	  * @return
	  */
	 public static List<CreditRecordSign> getLocalDateSignHvoByUserID(Session session,List<String> idkeys,int row){
		 List<CreditRecordSign> backlist= new ArrayList<CreditRecordSign>();
		 List<String> tempid= new ArrayList<String>();
		 
		 for (int i=0;i<idkeys.size();i++){
			 tempid.add(idkeys.get(i));
			 
			 if (tempid.size()==row){
				 List<CreditRecordSign> hvolist = session
							.createQuery(
									"from CreditRecordSign where  isEnable=0 and  studentId in (:alist) ")
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 
				 tempid.clear();
			 }else if (i+1==idkeys.size()){
				 List<CreditRecordSign> hvolist = session
							.createQuery(
									"from CreditRecordSign where  isEnable=0 and  studentId in (:alist) ")
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 tempid.clear();
			 }
			 
		 }
		 
		return backlist;
		 
	 }
	 
	 
	 
	 
/**
 * 根据userid和where取得NC成绩单
 * @param session
 * @param studentids
 * @param row
 * @param where 字段 要加and </p>
 *  如取得学分成绩通过的学员  and passScore=100 and examType='xf'  </p>
 *  取得从业通过的学员  and passScore=100 and examType='cy'  </p>
 *  取得初级通过的学员  and passScore=100 and examType='cj'  </p>
 *   取得中级实务通过的学员  and passScore=100 and examType='zj_shiwu'  </p>
 *   取得中级经济法通过的学员  and passScore=100 and examType='zj_jingjifa'  </p>
 *   取得中级财务管理 通过的学员  and passScore=100 and examType='zj_guangli'   </p>
 *  


 * @return
 */
	 public static List<CreditRecordNcSore> getNCScoreByUseridWhere(Session session,List<String> studentids,int row,String where){
		 List<CreditRecordNcSore> backlist= new ArrayList<CreditRecordNcSore>();
		 List<String> tempid= new ArrayList<String>();
		 
		 String hql=	"from CreditRecordNcSore where   "
					+ " studentId  in (:alist)  ";
		 if (where!=null){
			 hql=hql+where;
		 }
		 
		 for (int i=0;i<studentids.size();i++){
			 tempid.add(studentids.get(i));
			 
			 if (tempid.size()==row){
				 List<CreditRecordNcSore> hvolist = session
							.createQuery(						
									hql)
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 
				 tempid.clear();
			 }else if (i+1==studentids.size()){
				 List<CreditRecordNcSore> hvolist = session
							.createQuery(hql)
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 tempid.clear();
			 }
			 
		 }
		 
		return backlist;
		 
	 }
	 /**
	  * 根据考试类型和学员id取得档案<p>
	  * ("from CreditRecord where  ENABLE in (:enablelist) and    CONCAT (examType,ncUserId) in (:alist) ")
	  * @param session
	  * @param idkeys
	  * @param row
	  * @param enable
	  * @return
	  */
	 public static List<CreditRecord> getLocalDateRecordByExamTypeUserid(Session session,List<String> idkeys,int row ,int enable ){
		 List<CreditRecord> backlist= new ArrayList<CreditRecord>();
		 
		 List<String> tempid= new ArrayList<String>(); 
		 List<Integer> enablelist= new ArrayList<Integer>();
		 if (enable==1){
			 enablelist.add(1);
		 }else if (enable==0){
			 enablelist.add(0);
		 }else if (enable==3){
			 enablelist.add(0); 
             enablelist.add(1); 
		 }else {
			 enablelist.add(0);
		 }
		 
		 
		 for (int i=0;i<idkeys.size();i++){
			 tempid.add(idkeys.get(i));
			 
			 if (tempid.size()==row){				 
				 			
				 List<CreditRecord> hvolist = session
							.createQuery("from CreditRecord where  isEnable in (:enablelist)  and  CONCAT (examType,studentId) in (:alist) ")
							.setParameterList("alist",tempid)
                             .setParameterList("enablelist",enablelist).list();
				 backlist.addAll(hvolist);
				 
				 tempid.clear();
			 }else if (i+1==idkeys.size()){
				 List<CreditRecord> hvolist = session
						 .createQuery("from CreditRecord where  isEnable in (:enablelist) and    CONCAT (examType,studentId) in (:alist) ")
							.setParameterList("alist",tempid)
							.setParameterList("enablelist",enablelist).list();
				 backlist.addAll(hvolist);
				 tempid.clear();
			 }
			 
		 }
		 
		return backlist;
		 
	 }
	 /**
	  * 根据bid取得学分库的nc成绩单</p>
	  * from CreditRecordNcSore where    bId in (:alist) 
	  * @param session
	  * @param idkeys
	  * @param row
	  * @return
	  */
	 public static List<CreditRecordNcSore> getRecordNcScoreBid(Session session,List<String> idkeys,int row  ){
	
		 List<CreditRecordNcSore> backlist= new ArrayList<CreditRecordNcSore>();
		 List<String> tempid= new ArrayList<String>();
		 
		 for (int i=0;i<idkeys.size();i++){
			 tempid.add(idkeys.get(i));
			 
			 if (tempid.size()==row){
				 List<CreditRecordNcSore> hvolist = session
							.createQuery(
									"from CreditRecordNcSore where    bId in (:alist) ")
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 
				 tempid.clear();
			 }else if (i+1==idkeys.size()){
				 List<CreditRecordNcSore> hvolist = session
							.createQuery(
									"from CreditRecordNcSore where   bId in (:alist) ")
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 tempid.clear();
			 }
			 
		 }
		 
		return backlist;
		 
	 
	 }
	 
	 
	 
	 /**
	  * 题库成绩单中以BillKey（科目类型+USERID+type(E(考试)/W(作业))）取得成绩单</p>
	  * from CreditRecordTiKuScore where  BillKey in (:alist) 
	  * @param session
	  * @param idkeys
	  * @param row
	  * @return
	  */
	 public static List<CreditRecordTiKuScore> getRecordTiKuScoreByBillkey(Session session,List<String> idkeys,int row  ){
		 
		 List<CreditRecordTiKuScore> backlist= new ArrayList<CreditRecordTiKuScore>();
		 List<String> tempid= new ArrayList<String>();
		 
		 for (int i=0;i<idkeys.size();i++){
			 tempid.add(idkeys.get(i));
			 
			 if (tempid.size()==row){
				 List<CreditRecordTiKuScore> hvolist = session
							.createQuery(
									 "from CreditRecordTiKuScore where  BillKey in (:alist) ")
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 
				 tempid.clear();
			 }else if (i+1==idkeys.size()){
				 List<CreditRecordTiKuScore> hvolist = session
							.createQuery(
									"from CreditRecordTiKuScore where  BillKey in (:alist) ")
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 tempid.clear();
			 }
			 
		 }
		 
		return backlist;
		 
	 }
	 /**
	  * 题库成绩单中以科目类型+学员ID 取得成绩单
	  * @param session
	  * @param idkeys
	  * @param row
	  * @return
	  */
	 public static List<CreditRecordTiKuScore> getRecordTiKuScoreBySubtypeUserid(Session session,List<String> idkeys,int row  ){
		 
		 List<CreditRecordTiKuScore> backlist= new ArrayList<CreditRecordTiKuScore>();
		 List<String> tempid= new ArrayList<String>();
		 
		 for (int i=0;i<idkeys.size();i++){
			 tempid.add(idkeys.get(i));
			 
			 if (tempid.size()==row){
				 List<CreditRecordTiKuScore> hvolist = session
							.createQuery(
									 "from CreditRecordTiKuScore where   CONCAT (SubjectType,studentNCCode) in (:alist) ")
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 
				 tempid.clear();
			 }else if (i+1==idkeys.size()){
				 List<CreditRecordTiKuScore> hvolist = session
							.createQuery(
									"from CreditRecordTiKuScore where   CONCAT (SubjectType,studentNCCode) in (:alist) ")
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 tempid.clear();
			 }
			 
		 }
		 
		return backlist;
		 
	 }
	 /**
	  * 题库成绩单中以科目类型+学员ID 取得exam成绩单
	  * @param session
	  * @param idkeys
	  * @param row
	  * @param type E:exam,W:work,null：all;
	  * @return
	  */
	 public static List<CreditRecordTiKuScore> getRecordTiKuScoreBySubtypeUseridType(Session session,List<String> idkeys,int row,String type  ){
		 
		 List<CreditRecordTiKuScore> backlist= new ArrayList<CreditRecordTiKuScore>();
		 List<String> tempid= new ArrayList<String>();
		 String hql= "from CreditRecordTiKuScore where   CONCAT (SubjectType,studentNCCode) in (:alist) ";
		 if (type!=null){
			 if ("W".equals(type)||"E".equals(type)){
				 hql=hql+" and BillType='"+type+"' ";
			 }
		 }
		 
		 
		 for (int i=0;i<idkeys.size();i++){
			 tempid.add(idkeys.get(i));
			 
			 if (tempid.size()==row){
				 List<CreditRecordTiKuScore> hvolist = session
							.createQuery(hql)
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 
				 tempid.clear();
			 }else if (i+1==idkeys.size()){
				 List<CreditRecordTiKuScore> hvolist = session
							.createQuery(hql)
							.setParameterList("alist", tempid).list();
				 backlist.addAll(hvolist);
				 tempid.clear();
			 }
			 
		 }
		 
		return backlist;
		 
	 }

/**
 * 根据userid 取得本地档案中的信息
 * @param session
 * @param idkeys
 * @param row 
 * @param enable  1:1 0:0 3:0,1 显示enable 当3时则显示0,1的
 * @return
 */
	 public static List<CreditRecord> getLocalDateRecordByUserID(Session session,List<String> idkeys,int row,int enable ){
		 List<CreditRecord> backlist= new ArrayList<CreditRecord>();
		 
		 List<String> tempid= new ArrayList<String>();
		 List<Integer> enablelist= new ArrayList<Integer>();
		 if (enable==1){
			 enablelist.add(1);
		 }else if (enable==0){
			 enablelist.add(0);
		 }else if (enable==3){
			 enablelist.add(0); 
             enablelist.add(1); 
		 }else {
			 enablelist.add(0);
		 }
		 
		 
		 for (int i=0;i<idkeys.size();i++){
			 tempid.add(idkeys.get(i));
			 
			 if (tempid.size()==row){
				 List<CreditRecord> hvolist = session
							.createQuery("from CreditRecord where isEnable in (:enablelist) and  studentId in (:alist) ")
							.setParameterList("alist",tempid) 
				            .setParameterList("enablelist",enablelist).list();
				 backlist.addAll(hvolist);
				 
				 tempid.clear();
			 }else if (i+1==idkeys.size()){
				 List<CreditRecord> hvolist = session
						 .createQuery("from CreditRecord where isEnable in (:enablelist) and  studentId in (:alist) ")
							.setParameterList("alist",tempid) 
				            .setParameterList("enablelist",enablelist).list();
				 backlist.addAll(hvolist);
				 tempid.clear();
			 }
			 
		 }
		 
		return backlist;
		 
	 }
	 
		
	 
		
		
		
		
		
}
