package io.renren.modules.job.service.impl;

import io.renren.modules.job.dao.MallLiveRoomDao;
import io.renren.modules.job.entity.MallLiveRoomEntity;
import io.renren.modules.job.service.MallLiveRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("mallLiveRoomService")
public class MallLiveRoomServiceImpl implements MallLiveRoomService {
	@Autowired
	private MallLiveRoomDao mallLiveRoomDao;

	@Override
	public MallLiveRoomEntity queryObject(Map<String, Object> map){
		return mallLiveRoomDao.queryObject(map);
	}

}
