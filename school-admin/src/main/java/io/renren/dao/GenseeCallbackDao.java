package io.renren.dao;

import io.renren.entity.CourseClassplanLivesEntity;
import io.renren.entity.LiveCallbackDetailEntity;
import io.renren.entity.ReplayCallbackDetailEntity;
import io.renren.entity.VideoCallbackDetailEntity;
import io.renren.pojo.log.ReplayCallbackForLogGenseeWatchPOJO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 展示互动回调
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-12-30 14:39:20
 */
public interface GenseeCallbackDao {
	
	void saveLiveCallbackDetail(LiveCallbackDetailEntity entity);
	
	List<LiveCallbackDetailEntity> queryLiveCallbackDetail(Map<String,Object> map);
	
	List<LiveCallbackDetailEntity> queryLiveJoinDetail(Map<String,Object> map);
	
	void updateLiveCallbackDetail(LiveCallbackDetailEntity entity);
	
	void saveVideoCallbackDetail(VideoCallbackDetailEntity entity);
	
	VideoCallbackDetailEntity queryVideoCallbackDetail(Map<String,Object> map);
	
	void updateVideoCallbackDetail(VideoCallbackDetailEntity entity);
	
	void updateLiveCallbackLeaveTime(Map<String, Object> map);
	
	List<LiveCallbackDetailEntity> queryByLiveId(Map<String,Object> map);
	
	LiveCallbackDetailEntity queryClassLivesEndTime(Map<String,Object> map);

    void saveReplayCallbackDetail(ReplayCallbackDetailEntity entity);

    List<ReplayCallbackDetailEntity> queryReplayJoinDetail(Map<String, Object> map);

    List<ReplayCallbackDetailEntity> queryReplayLeaveDetail(Map<String, Object> map);

    void updateReplayCallbackDetail(ReplayCallbackDetailEntity entity);

    List<LiveCallbackDetailEntity> queryLiveLeaveDetail(Map<String, Object> map);

    List<ReplayCallbackForLogGenseeWatchPOJO> queryReplayCallback(Map<String, Object> map);

    List<ReplayCallbackForLogGenseeWatchPOJO> queryReplayCallbackIsOfflive(Map<String, Object> map);

    Long queryProductIdByVideoId(String videoId);

    int checkUserIdByJoinTime(@Param("userId") Long userId, @Param("joinTime") Long joinTime);
}
