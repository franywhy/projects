package io.renren.service.impl;

import io.renren.dao.MallAdjustAreaDao;
import io.renren.entity.MallAdjustAreaEntity;
import io.renren.pojo.MallAdjustAreaPOJO;
import io.renren.service.MallAdjustAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service("mallAdjustAreaService")
public class MallAdjustAreaServiceImpl implements MallAdjustAreaService {
	@Autowired
	private MallAdjustAreaDao mallAdjustAreaDao;

	
	@Override
	public List<MallAdjustAreaPOJO> queryPojoList(Map<String, Object> map){
		return  mallAdjustAreaDao.queryPojoList(map);
	}


    @Override
	public int queryTotal(Map<String, Object> map){
		return mallAdjustAreaDao.queryTotal(map);
	}
	
	@Override
	public void save(MallAdjustAreaEntity mallAdjustArea){
		mallAdjustAreaDao.save(mallAdjustArea);
	}
	
	
}
