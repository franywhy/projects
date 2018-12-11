package io.renren.task;

import java.util.HashMap;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import io.renren.service.MessageProductorService;
import io.renren.utils.SpringContextUtils;

//@Component("PushMsgContentOnTime")
@Component("io.renren.task.PushMsgContentUserplanClassOnTime")
public class PushMsgContentUserplanClassOnTime {

    public void execute(Map<String, Object> params) throws JobExecutionException {
//		System.out.println("xxxxxxxxxxxxxxx");
        //实例化MsgContentService
        MessageProductorService messageProductorCourseUserplanClassServiceImpl = (MessageProductorService) SpringContextUtils.getBean("messageProductorCourseUserplanClassServiceImpl");
        messageProductorCourseUserplanClassServiceImpl.pushToMessageQueue();


    }


}
