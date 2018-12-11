package io.renren.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Comsys-LZP
 * @version V2.0
 * @Description: 任务执行类
 * @ClassName: QuartzJob
 * @Copyright: Copyright (c) 2014
 * @date 2014-6-26 下午03:37:11
 */
public class QuartzJob implements Job {

    private static Logger logger = LoggerFactory.getLogger(PushMsgContentJob.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {

        String prms = arg0.getJobDetail().getJobDataMap().getString("k2");
        logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " " + prms);
    }
}  