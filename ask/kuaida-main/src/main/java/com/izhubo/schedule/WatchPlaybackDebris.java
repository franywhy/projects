package com.izhubo.schedule;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.izhubo.rest.common.util.http.HttpClientUtil;

public class WatchPlaybackDebris extends QuartzJobBean {


	private String api_domain = com.izhubo.rest.AppProperties.get("api.domain").toString();
	
	
	private String sync_url = "live_data/save_playback_data";

	private static Logger logger = LoggerFactory
			.getLogger(WatchPlaybackDebris.class);

	/* 业务实现 */
	public void work() {
		logger.info("定时同步开始，开始生成直播录制件：" + new Date());
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
