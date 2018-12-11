package io.renren.dao;

import io.renren.entity.LiveLogDetailEntity;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 观看直播详细日志
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-22 09:57:27
 */
public interface LiveLogDetailDao extends BaseMDao<LiveLogDetailEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	/**
	 * 查询直播记录明细数量
	 * @param liveId	直播id
	 * @param liveNum	直播房间号
	 * @param userId	学员id
	 * @param joinTime	观看直播-加入时间
	 * @param leaveTime	观看直播-离开时间
	 * @return
	 */
	int selectDetailCount(
			@Param("liveId")String liveId, @Param("liveNum")String liveNum, @Param("userId")Long userId, 
			@Param("joinTime")Long joinTime, @Param("leaveTime")Long leaveTime
			);
}
