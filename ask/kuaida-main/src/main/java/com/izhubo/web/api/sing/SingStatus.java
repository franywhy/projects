package com.izhubo.web.api.sing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * k歌比赛状态
 * @author: wubinjie@ak.cc
 * Date: 14-3-19 上午11:27
 */
public abstract class SingStatus{
    static final Logger logger = LoggerFactory.getLogger(SingStatus.class);

    protected Long waitTime = 0l;
    public SingStatus(){}
    public SingStatus(Long waitTime){
        this.waitTime = waitTime;
    }
    public SingStatus execute() throws InterruptedException {
        logger.debug("等待{}秒...", (waitTime/1000));
    /*    CountDown countDown = new CountDown(){
            public void execute() throws Exception {
                fire();
            }
        };
        countDown.CountDownBegin(waitTime);
        */
        Thread.sleep(waitTime);
        return this.fire();
    }
    abstract public SingStatus fire();
}