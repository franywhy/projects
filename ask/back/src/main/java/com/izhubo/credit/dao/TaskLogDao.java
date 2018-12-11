package com.izhubo.credit.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.izhubo.credit.util.CreditUtil;
import com.mysqldb.dao.BaseDaoImpl;
import com.mysqldb.model.CreditOperationLog;
import com.mysqldb.model.CreditRecord;
/**
 *   CreditOperationLog任务运算日志DAO 
 *
 * @author lintf 
 *
 */
@Repository("taskLogDao")
public class TaskLogDao extends BaseDaoImpl<CreditOperationLog>{
	
	public TaskLogDao() {
	
	}
	/*@SuppressWarnings("unchecked")
	public String  BeginLog(String taskname,Date startTime,Date endTime,String logstr){
		String logid=CreditUtil.getId();
		
		CreditOperationLog log= new CreditOperationLog();
		log.setTaskname(taskname);
		log.setLogid(logid);		
		log.setStartTime(startTime);
		log.setEndTime(endTime);
		log.setLogstr(logstr);
		System.out.println("开始保存日志");
		saveEntity(log);
		System.out.println("保存成功"+logid);
		return logid;
		
	}
	@SuppressWarnings("unchecked")
	public String  EndLog( Date endTime,String logstr,String logid){
		 
		String code="";
		
		 
		    List<CreditOperationLog> loglist= findEntitysByHQL(" from CreditOperationLog where LOGID=? ", logid);
			if (loglist!=null&&loglist.size()>0){
				CreditOperationLog log=loglist.get(0);
				String newlogstr=log.getLogstr()+"\n 结束:"+logstr;
				log.setLogstr(newlogstr);
				log.setEndTime(new Date());
				saveEntity(log);
				code="成保存日志";
			}else {
				code="没有找到日志";
			}
		
	return code;
	}*/
	
	 

}
