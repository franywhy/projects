package io.renren.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.renren.entity.ReplayCallbackDetailEntity;
import io.renren.pojo.log.ReplayCallbackForLogGenseeWatchPOJO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import io.renren.dao.GenseeCallbackDao;
import io.renren.entity.CourseClassplanLivesEntity;
import io.renren.entity.LiveCallbackDetailEntity;
import io.renren.entity.VideoCallbackDetailEntity;
import io.renren.service.GenseeCallbackService;

import javax.annotation.Resource;

@Service("genseeCallbackService")
public class GenseeCallbackServiceImpl implements GenseeCallbackService {

    private final static String VIDEOID = "videoId:";

	@Autowired
	private GenseeCallbackDao genseeCallbackDao;

    @Resource
    public StringRedisTemplate mainRedis;
	
	@Override
	public void saveLiveCallbackDetail(LiveCallbackDetailEntity entity) {
		genseeCallbackDao.saveLiveCallbackDetail(entity);
	}

	@Override
	public List<LiveCallbackDetailEntity> queryLiveCallbackDetail(Map<String, Object> map) {
		return genseeCallbackDao.queryLiveCallbackDetail(map);
	}
	
	@Override
	public List<LiveCallbackDetailEntity> queryLiveJoinDetail(Map<String, Object> map) {
		return genseeCallbackDao.queryLiveJoinDetail(map);
	}

	@Override
	public void updateLiveCallbackDetail(LiveCallbackDetailEntity entity) {
		genseeCallbackDao.updateLiveCallbackDetail(entity);
	}

	@Override
	public void saveVideoCallbackDetail(VideoCallbackDetailEntity entity) {
		genseeCallbackDao.saveVideoCallbackDetail(entity);
	}

	@Override
	public VideoCallbackDetailEntity queryVideoCallbackDetail(Map<String, Object> map) {
		return genseeCallbackDao.queryVideoCallbackDetail(map);
	}

	@Override
	public void updateVideoCallbackDetail(VideoCallbackDetailEntity entity) {
		genseeCallbackDao.updateVideoCallbackDetail(entity);
	}

	@Override
	public void updateLiveCallbackLeaveTime(Map<String, Object> map) {
		genseeCallbackDao.updateLiveCallbackLeaveTime(map);
	}

	@Override
	public List<LiveCallbackDetailEntity> queryByLiveId(Map<String, Object> map) {
		return genseeCallbackDao.queryByLiveId(map);
	}

	@Override
	public LiveCallbackDetailEntity queryClassLivesEndTime(Map<String, Object> map) {
		return genseeCallbackDao.queryClassLivesEndTime(map);
	}

    @Override
    public void saveReplayCallbackDetail(ReplayCallbackDetailEntity entity) {
	    //保存前先检查数据没有重复
        int count = genseeCallbackDao.checkUserIdByJoinTime(entity.getUserId(),entity.getJoinTime());
        if (count == 0){
            genseeCallbackDao.saveReplayCallbackDetail(entity);
        }
    }

    @Override
    public List<ReplayCallbackDetailEntity> queryReplayJoinDetail(Map<String, Object> map) {
        return genseeCallbackDao.queryReplayJoinDetail(map);
    }

    @Override
    public List<ReplayCallbackDetailEntity> queryReplayLeaveDetail(Map<String, Object> map) {
        return genseeCallbackDao.queryReplayLeaveDetail(map);
    }

    @Override
    public void updateReplayCallbackDetail(ReplayCallbackDetailEntity entity) {
        genseeCallbackDao.updateReplayCallbackDetail(entity);
    }

    @Override
    public List<LiveCallbackDetailEntity> queryLiveLeaveDetail(Map<String, Object> map) {
        return genseeCallbackDao.queryLiveLeaveDetail(map);
    }

    @Override
    public List<ReplayCallbackForLogGenseeWatchPOJO> queryReplayCallback(Map<String, Object> map) {
        return genseeCallbackDao.queryReplayCallback(map);
    }

    @Override
    public List<ReplayCallbackForLogGenseeWatchPOJO> queryReplayCallbackIsOfflive(Map<String, Object> map) {
        return genseeCallbackDao.queryReplayCallbackIsOfflive(map);
    }

    @Override
    public Long queryProductIdByVideoId(String videoId) {
	    Long productId = null;
        String productIdStr = this.mainRedis.opsForValue().get(VIDEOID + videoId);
        if (StringUtils.isNotBlank(productIdStr) && !"null".equals(productIdStr)){
            productId = Long.valueOf(productIdStr);
        }else {
            productId = genseeCallbackDao.queryProductIdByVideoId(videoId);
            this.mainRedis.opsForValue().set(VIDEOID + videoId, String.valueOf(productId), 30*60*1000, TimeUnit.MILLISECONDS);
        }
        return productId;
    }
}
