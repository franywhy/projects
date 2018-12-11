package com.hqjy.msg.aync;

import com.hqjy.msg.provide.AyncRedisToDbService;
import com.hqjy.msg.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/2/1 0001.
 */
@Component
public class AyscJob {

    @Autowired
    private AyncRedisToDbService ayncRedisToDbService;
    public final static long READED_MINUTE =  2 * 60 * 60 * 1000;
    public final static long AYSC_MSG_MINUTE =  1  * 60 * 1000;
    public final static long AYSC_CHANNEL_MINUTE =  1 * 30 * 60 * 1000;
    public final static long AYSC_MSG_DETAIL_MINUTE =  20 *  60 * 1000;

    private boolean isAyscEnable;
    @Value("${is-sync}")
    public void setAyscEnable(String ayscEnable) {
        isAyscEnable = Boolean.valueOf(ayscEnable);
    }

    @Scheduled(fixedDelay=AYSC_MSG_DETAIL_MINUTE)
    public void fixedDelayJob(){
        System.out.println(DateUtils.dateToString(DateUtils.getNowDate()));
        if (isAyscEnable ) {
            try {
                ayncRedisToDbService.syncMsg();
                ayncRedisToDbService.syncChannel();
                ayncRedisToDbService.syncReadedMsg();
                ayncRedisToDbService.syncDetail();
            } catch (Exception e) {
            }
        }


    }

    /*@Scheduled(fixedDelay=READED_MINUTE)
    public void ayncReaded(){
        if (isAyscEnable ) {
            try{
                ayncRedisToDbService.syncReadedMsg();
            }catch (Exception e){}
        }



    }

    @Scheduled(fixedDelay=AYSC_MSG_MINUTE)
    public void ayncMsg(){
        if (isAyscEnable ) {
            try {
                ayncRedisToDbService.syncMsg();
            } catch (Exception e) {
            }
        }


    }

    @Scheduled(fixedDelay=AYSC_CHANNEL_MINUTE)
    public void ayncChannel(){
        if (isAyscEnable ) {
            try {
                ayncRedisToDbService.syncChannel();
            } catch (Exception e) {
            }
        }


    }

    //@Scheduled(fixedDelay=AYSC_MSG_DETAIL_MINUTE,cron="0 0 1 * * ?")
    //@Scheduled(cron="0 0 1 * * ?")
    @Scheduled(fixedDelay=AYSC_MSG_DETAIL_MINUTE)
    public void ayncMsgDetail(){
        if (isAyscEnable ) {
            try {

                ayncRedisToDbService.syncDetail();
            } catch (Exception e) {
            }
        }


    }*/
}
