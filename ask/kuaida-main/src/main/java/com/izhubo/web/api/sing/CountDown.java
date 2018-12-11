package com.izhubo.web.api.sing;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: wubinjie@ak.cc
 * Date: 14-3-13 下午5:42
 */
public abstract class CountDown {
    static final Logger logger = LoggerFactory.getLogger(CountDown.class);

    public static ScheduledThreadPoolExecutor scheduledThreadPool = new ScheduledThreadPoolExecutor(1);

    public void CountDownBegin(long millis){
        scheduledThreadPool.schedule(new Schedule(), millis, TimeUnit.MILLISECONDS);
    }
    public abstract void execute() throws Exception;

    class Schedule implements Runnable  {
        public void run(){
            try{
                execute();
            } catch (Exception e) {
                logger.info(e.toString());
            }
        }
    }

}
