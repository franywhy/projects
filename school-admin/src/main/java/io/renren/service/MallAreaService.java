package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.MallAreaEntity;
import io.renren.mongo.entity.AreaEntity;
import io.renren.pojo.SelectionItem;

/**
 * 省份档案业务表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 16:58:35
 */
public interface MallAreaService {
	
	MallAreaEntity queryObject(Long areaId);
	
	List<MallAreaEntity> queryList(Map<String, Object> map);
	
	List<String> queryAreaIdList(AreaEntity area);
	
	List<MallAreaEntity> findAreaGoodsList(Map<String, Object> map);
	
	Integer queryAreaId(Map<String,Object> map);
	Integer queryAreaIdByZKAreaId(Map<String,Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MallAreaEntity mallArea);
	
	void update(MallAreaEntity mallArea);
	
	void delete(Long areaId);
	
	void deleteBatch(Long[] areaIds);
	
	void pause(Long[] areaIds);
	
	void resume(Long[] areaIds);

	List<MallAreaEntity> findAreaList();
	
	List<SelectionItem> querySelectionList(Map<String, Object> map);
	
	long getAreaIdByprovinceNane(Map<String, Object> map);
	
	int countAreaIdByprovinceNane(Map<String, Object> map);

	Long queryAreaIdByName(Map<String, Object> areaMap);

    int countAreaIdByExamProvinceName(Map<String, Object> map);

    Long getAreaIdByExamProvinceName(Map<String, Object> map);
}
