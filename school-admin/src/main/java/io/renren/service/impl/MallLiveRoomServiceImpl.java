package io.renren.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.MallLiveRoomDao;
import io.renren.dao.SysProductDao;
import io.renren.entity.MallLiveRoomEntity;
import io.renren.entity.SysProductEntity;
import io.renren.pojo.liveRoom.LiveRoomPOJO;
import io.renren.service.MallLiveRoomService;
import io.renren.utils.BeanHelper;
import io.renren.utils.Constant;


@Service("mallLiveRoomService")
public class MallLiveRoomServiceImpl implements MallLiveRoomService {
	@Autowired
	private MallLiveRoomDao mallLiveRoomDao;
	@Autowired
	private SysProductDao sysProductDao;
	
	private static String LIVE_URL = "";
	@Value("#{application['pom.gensee.webcast.web']}")
	private void setLiveUrl(String liveUrl){
		LIVE_URL = liveUrl;
	}
	
	@Override
	public MallLiveRoomEntity queryObject(Map<String, Object> map){
		return mallLiveRoomDao.queryObject(map);
	}
	
	@Override
	public List<MallLiveRoomEntity> queryList(Map<String, Object> map){
		return mallLiveRoomDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallLiveRoomDao.queryTotal(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void save(LiveRoomPOJO mallLiveRoom){
		
		mallLiveRoom.setDr(0);
		mallLiveRoom.setCreationTime(new Date());
		mallLiveRoom.setModifiedTime(mallLiveRoom.getCreationTime());
		MallLiveRoomEntity en = LiveRoomPOJO.getEntity(mallLiveRoom);
		BeanHelper.beanAttributeValueTrim(mallLiveRoom);
		mallLiveRoomDao.save(en);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void update(LiveRoomPOJO mallLiveRoom){
		
		mallLiveRoom.setModifiedTime(new Date());
		MallLiveRoomEntity en = LiveRoomPOJO.getEntity(mallLiveRoom);
		BeanHelper.beanAttributeValueTrim(mallLiveRoom);
		mallLiveRoomDao.update(en);
	}
	
	@Override
	public void delete(Long liveRoomId){
		mallLiveRoomDao.delete(liveRoomId);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void deleteBatch(Long[] liveRoomIds){
		mallLiveRoomDao.deleteBatch(liveRoomIds);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void pause(Long[] liveRoomIds){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", liveRoomIds);
    	map.put("status", Constant.Status.PAUSE.getValue());
    	//map.put("modifiedTime", new Date());
		mallLiveRoomDao.updateBatch(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void resume(Long[] liveRoomIds){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", liveRoomIds);
    	map.put("status", Constant.Status.RESUME.getValue());
    	//map.put("modifiedTime", new Date());
		mallLiveRoomDao.updateBatch(map);
	}

	/**
	 * 直播间列表
	 */
	@Override
	public List<MallLiveRoomEntity> findLiveRoomList(Map<String, Object> map) {
		return mallLiveRoomDao.findLiveRoomList(map);
	}

	@Override
	public List<MallLiveRoomEntity> queryListForCourseOLive(Map<String, Object> map) {
		return mallLiveRoomDao.queryListForCourseOLive(map);
	}

	@Override
	public int queryTotal1(Map<String, Object> map) {
		return this.mallLiveRoomDao.queryTotal1(map);
	}

	@Override
	public MallLiveRoomEntity queryMid(Map<String, Object> map) {
		return this.mallLiveRoomDao.queryMid(map);
	}


	@Override
	public String queryLiveUrl(String classplanLiveId, String schoolId, Map<String, Object> parameters) {
		Map<String, Object> queryParams = new HashMap<String,Object>();
		queryParams.put("classPlanLiveId", classplanLiveId);
		queryParams.put("schoolId", schoolId);
		queryParams.put("dr", 0);
		MallLiveRoomEntity liveRoom = mallLiveRoomDao.queryByClassPlanLiveId(queryParams);
		if(null != liveRoom){
			String liveId = liveRoom.getGenseeLiveId();
	    	if(null == liveId || liveId.equals("")){
	    		return "";
	    	}else{
	    		SysProductEntity product = sysProductDao.queryByclassplanLiveId(queryParams);
	    		return LIVE_URL.replace("@genseeDomain", product.getGenseeDomain()) + liveId + spliceParameter(parameters);
	    	}
		}else{
			return "";
		}
	}
	
	private static String spliceParameter(Map<String, Object> parameters){
        StringBuilder parameterStr = new StringBuilder();
        parameterStr.append("?");
        for (Map.Entry<String, Object> entry:parameters.entrySet()) {
            parameterStr.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        return  parameterStr.deleteCharAt(parameterStr.length()-1).toString();
    }

	@Override
	public void save(MallLiveRoomEntity mallLiveRoom) {
		this.mallLiveRoomDao.save(mallLiveRoom);
	}

	@Override
	public String queryGenseeLiveNum(String liveId) {
		return this.mallLiveRoomDao.queryGenseeLiveNum(liveId);
	}
}
