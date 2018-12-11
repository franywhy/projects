package com.izhubo.credit.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.mysqldb.dao.BaseDaoImpl;
import com.mysqldb.model.CreditPercentDetail;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordSign;
import com.mysqldb.model.Creditpercent;

import org.apache.commons.lang.StringUtils;
/**
 * 查看完成率中的学员明细的dao
 * @author lintf 2017年11月25日15:23:10
 *
 */
@Repository("creditPercentDao")
public class CreditPercentDao extends BaseDaoImpl<Creditpercent>{
	
	public CreditPercentDao() {
	
	}
	                  
	/**
	 *  查看报表数据
	 * @param dbilldate  当前月份
	 * @param beginDate  报名开始月份
	 * @param endDate    报名结束月份
	 * @return
	 */
	 public List<Creditpercent> getPercentReportByDate(String dbilldate,String beginDate,String endDate){
			
	 
	
			 
				Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Creditpercent.class);
				 criteria.add(Restrictions.eq("dr", new Integer(0)));
				if(StringUtils.isNotBlank(dbilldate)){
					criteria.add(Restrictions.eq("dbilldate",dbilldate));
				 
				}else {
					return  null;
				}
				//当开始和结束的报名时间都不为空时才进行报名时间判断
				if(StringUtils.isNotBlank(beginDate)&&StringUtils.isNotBlank(endDate)){
							criteria.add(Restrictions.ge("months",beginDate.subSequence(0, 7)));
							criteria.add(Restrictions.le("months",endDate.subSequence(0, 7)));
				} 
				//最重要的是进行排序
			return  criteria.addOrder(Order.asc("largeAreaName"))
					.addOrder(Order.asc("orgCode")).list();
				
				 
				
		 
		 
		 
	 }
	
}
