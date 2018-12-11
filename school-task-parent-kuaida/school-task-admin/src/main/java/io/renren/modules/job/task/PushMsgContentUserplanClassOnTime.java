package io.renren.modules.job.task;

import io.renren.common.utils.SpringContextUtils;
import io.renren.modules.job.service.MessageProductorService;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Map;

//@Component("PushMsgContentOnTime")
@Component("io.renren.modules.job.task.PushMsgContentUserplanClassOnTime")
public class PushMsgContentUserplanClassOnTime {

    public void execute(Map<String,Object> params) throws JobExecutionException {
//		System.out.println("xxxxxxxxxxxxxxx");
        //实例化MsgContentService
        MessageProductorService messageProductorCourseUserplanClassServiceImpl = (MessageProductorService) SpringContextUtils.getBean("messageProductorCourseUserplanClassServiceImpl");
        messageProductorCourseUserplanClassServiceImpl.pushToMessageQueue();


    }


}
