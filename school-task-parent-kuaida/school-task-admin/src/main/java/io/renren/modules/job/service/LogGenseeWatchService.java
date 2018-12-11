package io.renren.modules.job.service;

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
public interface LogGenseeWatchService {

	LogGenseeWatchEntity queryObject(Map<String, Object> map);

	void save(LogGenseeWatchEntity logGenseeWatch);

	void update(LogGenseeWatchEntity logGenseeWatch);

	/**
	 * 新增或保存
	 * @param logGenseeWatch
	 */
	void saveOrUpdate(LogGenseeWatchEntity logGenseeWatch);
	/**
	 * 新增或保存
	 * @param logGenseeWatch
	 */
	void saveOrUpdateFromLog(LogGenseeWatchEntity logGenseeWatch);

	/**
	 * 统计某时间段内所有课次实际出勤时长和应出勤时长
	 * @param userId
	 * @param classplanId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	LogAttendWatchTimePOJO sumWatchTimeByClassPlanLives(Map<String, Object> map);
}
