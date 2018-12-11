package com.izhubo.common.util;

import com.izhubo.rest.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: haigen.xiong
 * Date: 14-3-4
 * Time: 下午5:47
 * To change this template use File | Settings | File Templates.
 */
public  class ChatExecutor
{//
   static final  Logger logger = LoggerFactory.getLogger(ChatExecutor.class);
    private static final String MIN_SIZE = AppProperties.get("msg.thread.minSize");
    private static final String MAX_SIZE = AppProperties.get("msg.thread.maxSize");
       //Math.max(400,Integer.parseInt(MIN_SIZE))
    //Math.max(500,Integer.parseInt(MAX_SIZE))
    static final BaseThreadPoolExecutor PUB_SUB =
            new BaseThreadPoolExecutor(Integer.parseInt(MIN_SIZE),Integer.parseInt(MAX_SIZE),
                    60L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    new CustomizableThreadFactory("ChatExecutor.EXE")) ;

    public static void execute(Runnable run){
        PUB_SUB.execute(run);
    }

    public static int poolSize()
    {
       return  PUB_SUB.getChatPoolSize();
    }

    public static int activeCount()
    {
        return  PUB_SUB.getActiveCount() ;
    }

    public static String threadPoolInfoDetail()
    {
        String[] msg = PUB_SUB.toString().split("\\[");

        String info = msg[1].replace("\\]","") ;

        return info ;
    }
}