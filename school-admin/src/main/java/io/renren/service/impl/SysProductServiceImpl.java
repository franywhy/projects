package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.SysProductDao;
import io.renren.entity.SysProductEntity;
import io.renren.service.SysProductService;
import io.renren.utils.Constant;



@Service("sysProductService")
public class SysProductServiceImpl implements SysProductService {
	@Autowired
	private SysProductDao sysProductDao;
	
	@Override
	public SysProductEntity queryObject(Map<String, Object> map){
		return sysProductDao.queryObject(map);
	}
	
	@Override
	public List<SysProductEntity> queryList(Map<String, Object> map){
		return sysProductDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysProductDao.queryTotal(map);
	}
	
	@Override
	public void save(SysProductEntity sysProduct){
		sysProductDao.save(sysProduct);
	}
	
	@Override
	public void update(SysProductEntity sysProduct){
		sysProductDao.update(sysProduct);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		sysProductDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		sysProductDao.deleteBatch(map);
	}

	@Override
	public void pause(Long[] productIds) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", productIds);
    	map.put("status", Constant.Status.PAUSE.getValue());
    	sysProductDao.updateBatch(map);
	}

	@Override
	public void resume(Long[] productIds) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", productIds);
    	map.put("status", Constant.Status.RESUME.getValue());
    	sysProductDao.updateBatch(map);
	}

	@Override
	public List<SysProductEntity> queryList1(Map<String, Object> map) {
		return this.sysProductDao.queryList1(map);
	}

	@Override
	public int queryTotal1(Map<String, Object> map) {
		return this.sysProductDao.queryTotal1(map);
	}

	@Override
	public long getProductIdByProductName(Map<String, Object> map) {
		return this.sysProductDao.getProductIdByProductName(map);
	}

	@Override
	public Float queryCoefficient(Long productId) {
		return this.sysProductDao.queryCoefficient(productId);
	}
	
	
}
