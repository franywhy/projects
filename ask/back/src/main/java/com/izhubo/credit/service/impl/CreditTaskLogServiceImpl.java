package com.izhubo.credit.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.Resources;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 

 

import com.izhubo.credit.dao.CreditOperationLogDao;
import com.izhubo.credit.service.CreditTaskLogService;
import com.izhubo.credit.util.CreditUtil;
import com.mysqldb.model.CreditOperationLog;
/**
 * 消息日志操作接口
 * @author lintf
 *
 */
@Service("creditTaskLogService")

public class CreditTaskLogServiceImpl implements CreditTaskLogService  {
  
	 @Resource
	CreditOperationLogDao creditOperationLogDao;

	@Override
	@Transactional
	public String newLog(String taskname, String logstr) {
		 
		return creditOperationLogDao.newLog(taskname, logstr);
	}

	@Override
	public void setLog(String logid, String logstr) {
		
		creditOperationLogDao.setLog(logid, logstr);
	}

	@Override
	public void closeLog(String logid, String logstr) {
	 
		creditOperationLogDao.closeLog(logstr, logid);
	}
	 
}
