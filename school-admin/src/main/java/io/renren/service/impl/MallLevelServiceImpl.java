package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.MallLevelDao;
import io.renren.entity.MallLevelEntity;
import io.renren.pojo.SelectionItem;
import io.renren.service.MallLevelService;
import io.renren.utils.Constant;



@Service("mallLevelService")
public class MallLevelServiceImpl implements MallLevelService {
	@Autowired
	private MallLevelDao mallLevelDao;
	
	@Override
	public MallLevelEntity queryObject(Long levelId){
		return mallLevelDao.queryObject(levelId);
	}
	
	@Override
	public List<MallLevelEntity> queryList(Map<String, Object> map){
		return mallLevelDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallLevelDao.queryTotal(map);
	}
	
	@Override
	public void save(MallLevelEntity mallLevel){
		mallLevelDao.save(mallLevel);
	}
	
	@Override
	public void update(MallLevelEntity mallLevel){
		mallLevelDao.update(mallLevel);
	}
	
	@Override
	public void delete(Long levelId){
		mallLevelDao.delete(levelId);
	}
	
	@Override
	public void deleteBatch(Long[] levelIds){
		mallLevelDao.deleteBatch(levelIds);
	}
	@Override
	public void pause(Long[] levelIds){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", levelIds);
    	map.put("status", Constant.Status.PAUSE.getValue());
		mallLevelDao.updateBatch(map);
	}
		
	@Override
	public void resume(Long[] levelIds){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", levelIds);
    	map.put("status", Constant.Status.RESUME.getValue());
		mallLevelDao.updateBatch(map);
	}

	@Override
	public List<MallLevelEntity> findLevelList(Map<String, Object> map) {
		return mallLevelDao.findLevelList(map);
	}

	@Override
	public List<SelectionItem> querySelectionList(String schoolId) {
		return mallLevelDao.querySelectionList(schoolId);
	}
}
