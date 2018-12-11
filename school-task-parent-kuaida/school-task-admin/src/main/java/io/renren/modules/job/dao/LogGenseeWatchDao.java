package io.renren.modules.job.dao;

import io.renren.modules.job.entity.LogGenseeWatchEntity;
import io.renren.modules.job.pojo.log.LogAttendWatchTimePOJO;

import java.util.Map;

/**
 * 直播录播观看记录-时间求和
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-21 10:33:05
 */
public interface LogGenseeWatchDao extends BaseMDao<LogGenseeWatchEntity> {
    LogAttendWatchTimePOJO sumWatchTimeByClassPlanLives(Map<String, Object> map);
}
