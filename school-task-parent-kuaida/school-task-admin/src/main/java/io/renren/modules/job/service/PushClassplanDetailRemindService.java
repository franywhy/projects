package io.renren.modules.job.service;


import io.renren.modules.job.entity.PushClassplanDetailRemindEntity;

import java.util.List;

/**
 * 课次通知提醒详情表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-10-25 10:54:14
 */
public interface PushClassplanDetailRemindService {


    List<PushClassplanDetailRemindEntity> queryListByTs(String ts);

    void updateMsgId(Integer Id, String msgId);
}
