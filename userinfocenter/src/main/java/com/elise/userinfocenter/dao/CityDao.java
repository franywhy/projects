package com.elise.userinfocenter.dao;

import com.elise.userinfocenter.entity.CityEntity;
import com.elise.userinfocenter.pojo.CityPOJO;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by DL on 2017/12/28.
 */
@Repository
public interface CityDao {

    Map<String,Object> getCityFileUrl();

    Map<String,Object> getCity(Long userId);

    int isExistCity(Long userId);

    void update(Map<String,Object> map);

    void save(CityEntity entity);

    Date getMaxTsTime();

    List<CityPOJO> getParentCityList();

    List<CityPOJO> getChildrenCityList(Long parentId);
}
