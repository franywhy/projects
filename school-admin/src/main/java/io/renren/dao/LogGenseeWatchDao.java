package io.renren.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

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
public interface LogGenseeWatchDao extends BaseMDao<LogGenseeWatchEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	/**
	 * 统计某时间段内排课下所有课次实际出勤时长和应出勤时长
	 * @param userId
	 * @param classplanId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	LogAttendWatchTimePOJO sumWatchTime(@Param("userId")Long userId, @Param("classplanId") List<String> classplanId, @Param("classTypeId")String classTypeId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    LogAttendWatchTimePOJO sumWatchTimeByClassPlanLives(Map<String, Object> map);
    
    List<StudyInfoEntity> queryVideoWatch(Map<String,Object> map);
}
