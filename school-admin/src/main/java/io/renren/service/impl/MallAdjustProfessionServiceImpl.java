package io.renren.service.impl;

import io.renren.dao.MallAdjustAreaDao;
import io.renren.dao.MallAdjustProfessionDao;
import io.renren.entity.MallAdjustAreaEntity;
import io.renren.entity.MallAdjustProfessionEntity;
import io.renren.pojo.MallAdjustAreaPOJO;
import io.renren.pojo.MallAdjustProfessionPOJO;
import io.renren.service.MallAdjustAreaService;
import io.renren.service.MallAdjustProfessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("mallAdjustProfessionService")
public class MallAdjustProfessionServiceImpl implements MallAdjustProfessionService {
	@Autowired
	private MallAdjustProfessionDao mallAdjustProfessionDao;

	
	@Override
	public List<MallAdjustProfessionPOJO> queryPojoList(Map<String, Object> map){
		return  mallAdjustProfessionDao.queryPojoList(map);
	}


    @Override
	public int queryTotal(Map<String, Object> map){
		return mallAdjustProfessionDao.queryTotal(map);
	}
	
	@Override
	public void save(MallAdjustProfessionEntity mallAdjustProfessionEntity){
		mallAdjustProfessionDao.save(mallAdjustProfessionEntity);
	}
	
	
}
