package io.renren.service;

import io.renren.entity.MallLevelEntity;
import io.renren.pojo.SelectionItem;

import java.util.List;
import java.util.Map;

/**
 * 学历层次表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-25 16:38:54
 */
public interface MallLevelService {
	
	MallLevelEntity queryObject(Long levelId);
	
	List<MallLevelEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MallLevelEntity mallLevel);
	
	void update(MallLevelEntity mallLevel);
	
	void delete(Long levelId);
	
	void deleteBatch(Long[] levelIds);
	
	void pause(Long[] levelIds);
	
	void resume(Long[] levelIds);

	List<MallLevelEntity> findLevelList(Map<String, Object> map);
	
	List<SelectionItem> querySelectionList(String schoolId);
}
