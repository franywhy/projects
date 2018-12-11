package io.renren.dao;

import io.renren.entity.MallLiveRoomEntity;

import java.util.List;
import java.util.Map;

/**
 * 直播间档案表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-21 17:00:10
 */
public interface MallLiveRoomDao extends BaseDao<MallLiveRoomEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	/**
	 * 直播间列表
	 * @param map
	 * @return
	 */
	List<MallLiveRoomEntity> findLiveRoomList(Map<String, Object> map);
	
	List<MallLiveRoomEntity> queryListForCourseOLive(Map<String, Object> map);
	
	/**
	 * 查询status=1,dr=0的直播间列表总数
	 * @param map
	 * @return
	 */
	int queryTotal1(Map<String, Object> map);
	
	MallLiveRoomEntity queryMid(Map<String, Object> map);
	
	MallLiveRoomEntity queryByClassPlanLiveId(Map<String, Object> map);

}
