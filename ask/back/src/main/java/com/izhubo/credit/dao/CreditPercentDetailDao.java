package com.izhubo.credit.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.mysqldb.dao.BaseDaoImpl;
import com.mysqldb.model.CreditPercentDetail;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordSign;
/**
 * 查看完成率中的学员明细的dao
 * @author lintf 2017年11月25日15:23:10
 *
 */
@Repository("creditPercentDetailDao")
public class CreditPercentDetailDao extends BaseDaoImpl<CreditPercentDetail>{
	
	public CreditPercentDetailDao() {
	
	}
	 /**
	  * 查询报表学员明细数据
	  * @param dbilldate
	  * @param beginDate
	  * @param endDate
	  * @return
	  */
	 public List<CreditPercentDetail> getPercentDetailByDate(String dbilldate,String beginDate,String endDate){
	
		 if (beginDate==null||endDate==null){
			 return  findEntitysByHQL(" from CreditPercentDetail where dr=0 and dbilldate=? ",  dbilldate);
			 
		 }else {
		 
		 return  findEntitysByHQL(" from CreditPercentDetail where dr=0 and dbilldate=? and months>=? and months<=? ", new Object[]{dbilldate,beginDate.subSequence(0, 7),endDate.subSequence(0, 7)});
		 }
		 
		 
		 
	 }
	
	
}
