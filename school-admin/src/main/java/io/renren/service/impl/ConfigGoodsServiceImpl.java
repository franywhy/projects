package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.ConfigGoodsDao;
import io.renren.entity.ConfigGoodsEntity;
import io.renren.service.ConfigGoodsService;
import io.renren.utils.Constant;



@Service("configGoodsService")
public class ConfigGoodsServiceImpl implements ConfigGoodsService {
	@Autowired
	private ConfigGoodsDao configGoodsDao;
	
	@Override
	public ConfigGoodsEntity queryObject(Map<String, Object> map){
		return configGoodsDao.queryObject(map);
	}
	
	@Override
	public List<ConfigGoodsEntity> queryList(Map<String, Object> map){
		return configGoodsDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return configGoodsDao.queryTotal(map);
	}
	
	@Override
	public void save(ConfigGoodsEntity configGoods){
		configGoodsDao.save(configGoods);
	}
	
	@Override
	public void update(ConfigGoodsEntity configGoods){
		configGoodsDao.update(configGoods);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		configGoodsDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		configGoodsDao.deleteBatch(map);
	}

	@Override
	public int queryNumByGoodId(String nc_commodity_id) {
		return this.configGoodsDao.queryNumByGoodId(nc_commodity_id);
	}
	
	
}
