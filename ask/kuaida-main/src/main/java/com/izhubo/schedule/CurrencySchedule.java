package com.izhubo.schedule;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.izhubo.model.CurrencySyncState;
import com.izhubo.rest.AppProperties;
import com.izhubo.rest.common.util.http.HttpClientUtil;
import com.izhubo.webservice.MemberaccountModel;
import com.izhubo.webservice.SynchroMemberSoapProxy;
import com.mchange.v2.c3p0.impl.NewPooledConnection;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

import static com.izhubo.rest.common.util.WebUtils.$$;

public class CurrencySchedule extends QuartzJobBean {



	private String api_domain = com.izhubo.rest.AppProperties.get("api.domain").toString();
	
	
	private String sync_url = "currencynologin/TryToSync";

	private static Logger logger = LoggerFactory
			.getLogger(CurrencySchedule.class);

	/* 业务实现 */
	public void work() {
		logger.info("定时同步开始，检查是否有失败的订单：" + new Date());
		try {
			  HttpClientUtil.get(api_domain+sync_url, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block10.170.19.223:27017
			logger.info("执行保报错" + new Date());
			e.printStackTrace();
		}

	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		this.work();
	}


}
