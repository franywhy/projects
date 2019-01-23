package com.hq.bi.offline.task.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author zhouyibin
 * @date 2019/1/10
 * @desc
 */
@Data
public class AzkabanScheduleEntity {
    private Date nextExecTime;
    private String period;
    private String submitUser;
    private String scheduleId;
    private Date firstSchedTime;
}
