package io.renren.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.renren.entity.LogGenseeWatchEntity;
import io.renren.entity.StudyInfoEntity;
import io.renren.pojo.log.LogAttendWatchTimePOJO;

/**
 * 直播录播观看记录-时间求和
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-21 10:33:05
 */
public interface LogGenseeWatchService {
	
		
	LogGenseeWatchEntity queryObject(Map<String, Object> map);
	
	List<LogGenseeWatchEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(LogGenseeWatchEntity logGenseeWatch);
	
	void update(LogGenseeWatchEntity logGenseeWatch);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
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
	 * 批量新增或保存
	 * @param logGenseeWatch
	 */
	void saveOrUpdateList(List<LogGenseeWatchEntity> logGenseeWatchList);
	
	/**
	 * 批量导入考勤
	 * @param logGenseeWatch
	 */
	void saveOrUpdateBatch(List<LogGenseeWatchEntity> logGenseeWatchList);
	
	/**
	 * 统计某时间段内排课下所有课次实际出勤时长和应出勤时长
	 * @param userId
	 * @param classplanId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	LogAttendWatchTimePOJO sumWatchTime(Long userId, List<String> classplanId, String classTypeId, String startTime, String endTime);

    /**
     * 统计某时间段内所有课次实际出勤时长和应出勤时长
     * @param userId
     * @param classplanId
     * @param startTime
     * @param endTime
     * @return
     */
    LogAttendWatchTimePOJO sumWatchTimeByClassPlanLives(Map<String, Object> map);
    
    List<StudyInfoEntity> queryVideoWatch(Map<String,Object> map);
    
    BigDecimal getVideoWatchPersents(Map<String,Object> map);
}
