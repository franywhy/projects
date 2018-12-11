package io.renren.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.MallAreaDao;
import io.renren.entity.MallAreaEntity;
import io.renren.mongo.entity.AreaEntity;
import io.renren.pojo.SelectionItem;
import io.renren.service.MallAreaService;
import io.renren.utils.Constant;



@Service("mallAreaService")
public class MallAreaServiceImpl implements MallAreaService {
	@Autowired
	private MallAreaDao mallAreaDao;
	
	@Override
	public MallAreaEntity queryObject(Long areaId){
		return mallAreaDao.queryObject(areaId);
	}
	
	@Override
	public List<MallAreaEntity> queryList(Map<String, Object> map){
		return mallAreaDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallAreaDao.queryTotal(map);
	}
	
	@Override
	public void save(MallAreaEntity mallArea){
		mallAreaDao.save(mallArea);
	}
	
	@Override
	public void update(MallAreaEntity mallArea){
		mallAreaDao.update(mallArea);
	}
	
	@Override
	public void delete(Long areaId){
		mallAreaDao.delete(areaId);
	}
	
	@Override
	public void deleteBatch(Long[] areaIds){
		mallAreaDao.deleteBatch(areaIds);
	}
	@Override
	public void pause(Long[] areaIds){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", areaIds);
    	map.put("status", Constant.Status.PAUSE.getValue());
		mallAreaDao.updateBatch(map);
	}
		
	@Override
	public void resume(Long[] areaIds){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", areaIds);
    	map.put("status", Constant.Status.RESUME.getValue());
		mallAreaDao.updateBatch(map);
	}

	@Override
	public List<MallAreaEntity> findAreaList() {
		// TODO Auto-generated method stub
		return mallAreaDao.findAreaList();
	}

	@Override
	public List<SelectionItem> querySelectionList(Map<String, Object> map) {
		return mallAreaDao.querySelectionList(map);
	}

	@Override
	public long getAreaIdByprovinceNane(Map<String, Object> map) {
		return mallAreaDao.getAreaIdByprovinceNane(map);

	}

	@Override
	public int countAreaIdByprovinceNane(Map<String, Object> map) {
		return mallAreaDao.countAreaIdByprovinceNane(map);
	}


	@Override
	public List<MallAreaEntity> findAreaGoodsList(Map<String, Object> map) {
		return mallAreaDao.findAreaGoodsList(map);
	}


	@Override
	public Integer queryAreaId(Map<String, Object> map) {
		return mallAreaDao.queryAreaId(map);
	}

	@Override
	public List<String> queryAreaIdList(AreaEntity area) {
		return mallAreaDao.queryAreaIdList(area);
	}

	@Override
	public Integer queryAreaIdByZKAreaId(Map<String, Object> map) {
		return this.mallAreaDao.queryAreaIdByZKAreaId(map);
	}

	@Override
	public Long queryAreaIdByName(Map<String, Object> areaMap) {
		return this.mallAreaDao.queryAreaIdByName(areaMap);
	}

    @Override
    public int countAreaIdByExamProvinceName(Map<String, Object> map) {
        return mallAreaDao.countAreaIdByExamProvinceName(map);
    }

    @Override
    public Long getAreaIdByExamProvinceName(Map<String, Object> map) {
        return mallAreaDao.getAreaIdByExamProvinceName(map);
    }

}
