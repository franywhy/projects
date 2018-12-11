package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.CourseClassplanLivesEntity;
import io.renren.entity.LiveCallbackDetailEntity;
import io.renren.entity.ReplayCallbackDetailEntity;
import io.renren.entity.VideoCallbackDetailEntity;
import io.renren.pojo.log.ReplayCallbackForLogGenseeWatchPOJO;

public interface GenseeCallbackService {
	
	void saveLiveCallbackDetail(LiveCallbackDetailEntity entity);
	
	List<LiveCallbackDetailEntity> queryLiveCallbackDetail(Map<String,Object> map);
	
	/**
	 * 查询只有进入时间的明细记录
	 * @param map
	 * @return
	 */
	List<LiveCallbackDetailEntity> queryLiveJoinDetail(Map<String,Object> map);
	
	void updateLiveCallbackDetail(LiveCallbackDetailEntity entity);
	
	void saveVideoCallbackDetail(VideoCallbackDetailEntity entity);
	
	VideoCallbackDetailEntity queryVideoCallbackDetail(Map<String,Object> map);
	
	void updateVideoCallbackDetail(VideoCallbackDetailEntity entity);
	
	void updateLiveCallbackLeaveTime(Map<String,Object> map);
	
	List<LiveCallbackDetailEntity> queryByLiveId(Map<String,Object> map);
	
	LiveCallbackDetailEntity queryClassLivesEndTime(Map<String,Object> map);

    void saveReplayCallbackDetail(ReplayCallbackDetailEntity entity);

    /**
     * 查询只有进入时间的明细记录
     * @param map
     * @return
     */
    List<ReplayCallbackDetailEntity> queryReplayJoinDetail(Map<String, Object> map);

    /**
     * 查询只有离开时间的明细记录
     * @param map
     * @return
     */
    List<ReplayCallbackDetailEntity> queryReplayLeaveDetail(Map<String, Object> map);

    void updateReplayCallbackDetail(ReplayCallbackDetailEntity entity);

    List<LiveCallbackDetailEntity> queryLiveLeaveDetail(Map<String, Object> map);

    //查询回放记录明细
    List<ReplayCallbackForLogGenseeWatchPOJO> queryReplayCallback(Map<String, Object> map);

    //查询离线观看回放明细
    List<ReplayCallbackForLogGenseeWatchPOJO> queryReplayCallbackIsOfflive(Map<String, Object> map);

    //根据回放视频id查询课次productId
    Long queryProductIdByVideoId(String videoId);
}
