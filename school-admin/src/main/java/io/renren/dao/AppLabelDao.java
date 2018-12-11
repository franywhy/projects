package io.renren.dao;

import io.renren.entity.AppLabel;
import io.renren.entity.SysProductEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AppLabelDao {

    List<AppLabel> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

	AppLabel queryObject(Map<String, Object> map);

	int update(AppLabel appLabel);

	int queryCountByLabelNameAndParentId(Map<String, Object> map);

	List<AppLabel> queryListByProfessionName(Map<String, Object> map);

	void save(AppLabel appLabel);

	int updateDr(Map<String, Object> map);

	List<AppLabel> queryParentList(Map<String, Object> map);

	int queryParentTotal(Map<String, Object> map);

	AppLabel selectByPrimaryKey(@Param("id") Long id);
    
}