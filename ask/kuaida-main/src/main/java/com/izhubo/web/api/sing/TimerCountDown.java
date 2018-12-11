package com.izhubo.web.api.sing;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: wubinjie@ak.cc
 * Date: 14-3-13 下午5:42
 */
/*public abstract class TimerCountDown {
    private static Timer timer = new Timer();

    public void CountDownBegin(long millis){
        if(timer == null)
            timer = new Timer();
        timer.schedule(new Task(), millis);
    }
    public abstract void execute() throws Exception;

    class Task extends TimerTask {
        public void run(){
            try{
                execute();
            } catch (Exception e) {
                timer = null;
                e.printStackTrace();
            } finally {
                this.cancel();
                //timer.cancel();
            }
        }
    }

}*/
