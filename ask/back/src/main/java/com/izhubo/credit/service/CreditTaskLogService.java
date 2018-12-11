package com.izhubo.credit.service;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public interface CreditTaskLogService {
	/**
	 * 开始写日志 并返回logid
	 * @param taskname 日志名称
 
	 * @param logstr  日志内容
	 * 
	 */
	   String  newLog( String taskname,String logstr);
	 /**
	  * 根据日志ID变更日志
	  * @param logid 日志id
	  * @param logstr 日志内容
	  */
	   void setLog( String logid,String logstr );
		 
	   /**
	    * 结束日志
	    * @param logid 日志id
	    * @param logstr 日志内容
	    */
	   void closeLog( String logid,String logstr );
		
}
