package io.renren.service;

import io.renren.entity.MallLiveRoomEntity;
import io.renren.pojo.liveRoom.LiveRoomPOJO;

import java.util.List;
import java.util.Map;

/**
 * 直播间档案表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-21 17:00:10
 */
public interface MallLiveRoomService {
	
	MallLiveRoomEntity queryObject(Map<String, Object> map);
	
	MallLiveRoomEntity queryMid(Map<String, Object> map);
	
	List<MallLiveRoomEntity> queryList(Map<String, Object> map);
	
	List<MallLiveRoomEntity> queryListForCourseOLive(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(LiveRoomPOJO mallLiveRoom);
	void save(MallLiveRoomEntity mallLiveRoom);
	
	void update(LiveRoomPOJO mallLiveRoom);
	
	void delete(Long liveRoomId);
	
	void deleteBatch(Long[] liveRoomIds);
	
	void pause(Long[] liveRoomIds);
	
	void resume(Long[] liveRoomIds);
	
	/**
	 * 直播间列表
	 * @param map
	 * @return
	 */
	List<MallLiveRoomEntity> findLiveRoomList(Map<String, Object> map);
	
	/**
	 * 查询status=1，dr=0的直播间列表总数
	 * @param map
	 * @return
	 */
	int queryTotal1(Map<String, Object> map);
	
	String queryLiveUrl(String classplanLiveId, String schoolId, Map<String, Object>parameters);
    
	String queryGenseeLiveNum(String liveId);
}
